--pchem and struc

--resolution of chem_names

--first have to drop the table that was modeled in uml to accomodate cmpd_view

drop table cmpd_view;

--views

drop view if exists cmpd_view;
drop view if exists parent_fragment_view;
drop view if exists all_fragments_view;
drop view if exists cmpd_props_view;

--
--
--
--cmpd_props_view string_agg of one-to-many props
--
--
--

-- MWK 23July2014 this checks out OK with postgres explain

create view cmpd_props_view
as
select
nsc.id, 
nsc.nsc,
string_agg(alia.alias,'xxx') as formatted_aliases_string,
--formatted_plates includes distinct because data are degenerate by set (not tracking plate row/column in dctddb)
string_agg(distinct plat.plate_name,'xxx') as formatted_plates_string,
string_agg(distinct proj.project_code,'xxx') as formatted_projects_string,
string_agg(cset.set_name,'xxx') as formatted_sets_string,
string_agg(targ.target,'xxx') as formatted_targets_string
from
nsc_cmpd nsc
left outer join cmpd_aliases2nsc_cmpds alia2c on (alia2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_alias alia on (alia2c.cmpd_aliases_fk = alia.id)
left outer join cmpd_plates2nsc_cmpds plat2c on (plat2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_plate plat on (plat2c.cmpd_plates_fk = plat.id)
left outer join cmpd_projects2nsc_cmpds proj2c on (proj2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_project proj on (proj2c.cmpd_projects_fk = proj.id)
left outer join cmpd_sets2nsc_cmpds sets2c on (sets2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_set cset on (sets2c.cmpd_sets_fk = cset.id)
left outer join cmpd_targets2nsc_cmpds targ2c on (targ2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_target targ on (targ2c.cmpd_targets_fk = targ.id)
group by 
nsc.id, 
nsc.nsc;

--
--
--
--parent fragment
--
--
--

-- MWK 23July2014 this checks out OK with postgres explain

create view parent_fragment_view
as
select
--ident
nsc.id,
nsc.nsc_cmpd_id,
nsc.name,              
nsc.prefix,                   
nsc.nsc,       
nsc.conf,                     
nsc.distribution,             
nsc.cas, 
--
null ad_hoc_cmpd_id,
null original_ad_hoc_cmpd_id,

--pchem

pchem.mw,      
pchem.mf,      
pchem.alogp,   
pchem.logd,    
pchem.hba,     
pchem.hbd,     
pchem.sa,     
pchem.psa,     

--structure
struc.smiles,     
struc.inchi,      
mol_to_smiles(struc.mol)::varchar(1024) mol,        
struc.inchi_aux,  
null ctab, --struc.ctab,       

--bioassay

bio.nci60,   
bio.hf,      
bio.xeno,

--inventory

inv.inventory

from 
nsc_cmpd nsc
left outer join cmpd_fragment frag on (nsc.cmpd_parent_fragment_fk = frag.id)
left outer join cmpd_fragment_p_chem pchem on (frag.cmpd_fragment_p_chem_fk = pchem.id)
left outer join cmpd_fragment_structure struc on (frag.cmpd_fragment_structure_fk = struc.id)
left outer join cmpd_bio_assay bio on (nsc.cmpd_bio_assay_fk = bio.id)
left outer join cmpd_inventory inv on (nsc.cmpd_inventory_fk = inv.id)

union

select

--ident
ahc.id,                                   
null, --nsc.nsc_cmpd_id,
ahc.name,              
null, --nsc.prefix,                   
null, --nsc.nsc,       
null, --nsc.conf,                     
null, --nsc.distribution,             
null, --nsc.cas, 

ahc.ad_hoc_cmpd_id,
ahc.original_ad_hoc_cmpd_id,

--pchem

ahcpchem.mw,      
ahcpchem.mf,      
ahcpchem.alogp,   
ahcpchem.logd,    
ahcpchem.hba,     
ahcpchem.hbd,     
ahcpchem.sa,     
ahcpchem.psa,     

--structure

ahcstruc.smiles,     
ahcstruc.inchi,      
ahcstruc.mol,        
ahcstruc.inchi_aux,  
null, --struc.ctab,       

--bioassay

null, --bio.nci60,   
null, --bio.hf,      
null, --bio.xeno

--inventory

null -- inventory

from 

ad_hoc_cmpd ahc
left outer join ad_hoc_cmpd_fragment ahcfrag on (ahc.ad_hoc_cmpd_parent_fragment_fk = ahcfrag.id)
left outer join ad_hoc_cmpd_fragment_p_chem ahcpchem on (ahcfrag.ad_hoc_cmpd_fragment_p_chem_fk = ahcpchem.id)
left outer join ad_hoc_cmpd_fragment_structure ahcstruc on (ahcfrag.ad_hoc_cmpd_fragment_struct_fk = ahcstruc.id);




--
--
--
-- all fragments
--
--
--

create index cmpd_fragment_nsc_cmpd_fk on cmpd_fragment(nsc_cmpd_fk);
create index ad_hoc_cmpd_fragment_ad_hoc_cmpd_fk on ad_hoc_cmpd_fragment(ad_hoc_cmpd_fk);

drop view all_fragments_view;

create view all_fragments_view
as
select
nsc.id, 
nsc.nsc,
string_agg(strc.smiles,'xxx') as formatted_fragments_string
from
nsc_cmpd nsc, cmpd_fragment frag, cmpd_fragment_structure strc
where nsc.id = frag.nsc_cmpd_fk
and frag.cmpd_fragment_structure_fk = strc.id
group by 
nsc.id, 
nsc.nsc

union

select
ahc.id, 
ahc.ad_hoc_cmpd_id,
string_agg(ahc_strc.smiles,'xxx') as formatted_fragments_string
from
ad_hoc_cmpd ahc, ad_hoc_cmpd_fragment ahc_frag, ad_hoc_cmpd_fragment_structure ahc_strc
where ahc.id = ahc_frag.ad_hoc_cmpd_fk
and ahc_frag.ad_hoc_cmpd_fragment_struct_fk = ahc_strc.id
group by 
ahc.id;

--
--
--
--cmpd_view
--
--
--

create view cmpd_view
as
select 
'FAKE'::varchar(1024) as "cmpd_owner",
parent.*, 
props.formatted_aliases_string,
props.formatted_sets_string,
props.formatted_plates_string,
props.formatted_targets_string,
props.formatted_projects_string,
frags.formatted_fragments_string
from prod_src src left outer join cmpd_props_view props on (src.nsc = props.nsc)
left outer join parent_fragment_view parent on (src.nsc = parent.nsc)
--from parent_fragment_view parent left outer join cmpd_props_view props on (parent.nsc = props.nsc)
left outer join all_fragments_view frags on (src.nsc = frags.nsc);
            
alter view cmpd_props_view owner to mwkunkel;
alter view parent_fragment_view owner to mwkunkel;
alter view all_fragments_view owner to mwkunkel;
alter view cmpd_view owner to mwkunkel;
