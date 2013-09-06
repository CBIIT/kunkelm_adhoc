--first view to string_agg aliases, plates, projects, sets and targets

-- ALL of the properties

drop view if exists cmpd_props_view;

create view cmpd_props_view
as
select
nsc.id, 
nsc.nsc,
string_agg(alia.alias,',') as aliases,
string_agg(plat.plate_name,',') as plates,
string_agg(proj.project_code,',') as projects,
string_agg(cset.set_name,',') as sets,
string_agg(targ.target,',') as targets
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


--testing

                Table "public.cmpd_alias"
       Column       |          Type           | Modifiers 
--------------------+-------------------------+-----------
 id                 | bigint                  | not null
 alias              | character varying(1024) | not null
 cmpd_alias_type_fk | bigint                  | not null

           Table "public.cmpd_alias_type"
   Column   |          Type           | Modifiers 
------------+-------------------------+-----------
 id         | bigint                  | not null
 alias_type | character varying(1024) | not null

Table "public.cmpd_aliases2nsc_cmpds"
     Column      |  Type  | Modifiers 
-----------------+--------+-----------
 nsc_cmpds_fk    | bigint | not null
 cmpd_aliases_fk | bigint | not null


begin;
 
insert into cmpd_alias_type(id, alias_type) values (1, 'FAKE');

insert into cmpd_alias(id, alias, cmpd_alias_type_fk) values (1, 'Pleurotin', 1);
insert into cmpd_alias(id, alias, cmpd_alias_type_fk) values (2, 'PLEUROTIN', 1);
insert into cmpd_alias(id, alias, cmpd_alias_type_fk) values (3, 'Pleurotine', 1);

insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk) values (100506, 1);
insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk) values (100506, 2);
insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk) values (100506, 3);
 
commit; 

drop view if exists parent_frag_view;

create view parent_frag_view
as
select
--ident
nsc.id,
nsc.cmpd_id,
nsc.name,              
nsc.prefix,                   
nsc.nsc,       
nsc.conf,                     
nsc.distribution,             
nsc.cas,                      
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
struc.mol,        
struc.inchi_aux,  
struc.ctab,       
--bioassay
bio.nci60,   
bio.hf,      
bio.xeno
from 
nsc_cmpd nsc
left outer join cmpd_fragment frag on (nsc.cmpd_parent_fragment_fk = frag.id)
left outer join cmpd_fragment_p_chem pchem on (frag.cmpd_fragment_p_chem_fk = pchem.id)
left outer join cmpd_fragment_structure struc on (frag.cmpd_fragment_structure_fk = struc.id)
left outer join cmpd_bio_assay bio on (nsc.cmpd_bio_assay_fk = bio.id);

create view cmpd_view
as
select 
parent.*, 
props.aliases,
props.sets,
props.plates,
props.targets,
props.projects
from parent_frag_view parent left outer join cmpd_props_view props
on (parent.nsc = props.nsc);
            

