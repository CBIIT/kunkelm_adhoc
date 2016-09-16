drop table if exists nsc_one_lbl_only;

create table nsc_one_lbl_only
as
select 
nsc.nsc, annot.pseudo_atoms, inv.inventory, bio.nci60
from nsc_cmpd nsc, cmpd_annotation annot, cmpd_inventory inv, cmpd_bio_assay bio
where nsc.cmpd_annotation_fk = annot.id
and nsc.cmpd_bio_assay_fk = bio.id
and nsc.cmpd_inventory_fk = inv.id
and nsc.nsc in (
    select nsc from qc_with_nsc where source = 'one_lbl_no_strc_no_salt'
);

drop table if exists nsc_one_lbl_no_info;

create table nsc_one_lbl_no_info
as
select *
from nsc_one_lbl_only
where pseudo_atoms ilike '%Structure%Not%Available%'
or pseudo_atoms ilike '%Structure%Not%Known%'
or pseudo_atoms ilike '%Recombinant%Protein%'
or pseudo_atoms ilike '%NCI%Singles%'
or pseudo_atoms ilike '%Code%No%Only%'
or pseudo_atoms ilike '%Code%Number%'
or nsc between 900000 and 1000000;

delete from nsc_one_lbl_only
where nsc in (
    select nsc from nsc_one_lbl_no_info
);

select count(*) from nsc_one_lbl_only;
select count(*) from nsc_one_lbl_no_info;

select count(*) from nsc_one_lbl_only;
select count(*) from nsc_one_lbl_only where nci60 > 0 or inventory > 0;
