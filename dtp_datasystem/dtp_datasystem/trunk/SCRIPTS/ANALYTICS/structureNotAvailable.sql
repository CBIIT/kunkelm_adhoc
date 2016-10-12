drop table if exists nsc_one_lbl_only;

create table nsc_one_lbl_only
as
select 
nsc.nsc, annot.pseudo_atoms, inv.inventory, bio.nci60
from nsc_cmpd nsc

left outer join cmpd_annotation annot on nsc.cmpd_annotation_fk = annot.id
left outer join  cmpd_inventory inv on nsc.cmpd_inventory_fk = inv.id
left outer join cmpd_bio_assay bio on nsc.cmpd_bio_assay_fk = bio.id
where nsc.nsc in (
    select nsc from qc_with_nsc where source = 'one_lbl_no_strc_no_salt'
);

drop table if exists nsc_one_lbl_sna;

create table nsc_one_lbl_sna
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

alter table nsc_one_lbl_only
rename to nsc_one_lbl_for_review;

delete from nsc_one_lbl_for_review
where nsc in (
    select nsc from nsc_one_lbl_sna
);

\copy nsc_one_label_sna to /tmp/nsc_one_lbl_sna.tsv csv heade4r delimiter as E'\t'
\copy nsc_one_lbl_for_review to /tmp/nsc_one_lbl_for_review.tsv csv header delimiter as E'\t'
