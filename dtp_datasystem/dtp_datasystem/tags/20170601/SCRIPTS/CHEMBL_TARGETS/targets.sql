drop table if exists temp;

-- the compound is atorvastatin (chembl1487) 
create table temp
as
select distinct m1.chembl_id as parent_chembl_id,   
m2.chembl_id                 as salt_chembl_id,   
r.compound_key,   
d.pubmed_id as pubmed_id,   
d.doi as doi,   
a.description as assay_description,   
act.standard_type,   
act.standard_relation,   
act.standard_value,   
act.standard_units,   
act.activity_comment,   
t.chembl_id as target_chembl_id,   
t.pref_name as target_name,   
t.target_type 
from molecule_hierarchy h,   
molecule_dictionary m1,   
molecule_dictionary m2,   
compound_records r,   
docs d,   
activities act,   
assays a,   
target_dictionary t 
where m1.molregno = h.parent_molregno 
and h.molregno    = m2.molregno 
and m2.molregno   = r.molregno 
and r.record_id   = act.record_id 
and r.doc_id      = d.doc_id 
and act.assay_id  = a.assay_id 
and a.tid         = t.tid 
and m1.chembl_id  = 'CHEMBL1487';

select count(*) as count_generic_name from app_and_inv where generic_name is not null;
select count(*) as count_preferred_name from app_and_inv where preferred_name is not null;

drop table if exists temp2;

create table temp2
as
select 

ai.nsc, ai.generic_name, ai.preferred_name, m1.chembl_id, r.compound_name

from app_and_inv ai left outer join compound_records r
    on (
        upper(ai.preferred_name) = upper(r.compound_name)
        or
        upper(ai.generic_name) = upper(r.compound_name)
    ),

molecule_dictionary m1,
molecule_dictionary m2,
molecule_hierarchy h

where m1.molregno = h.parent_molregno 
and h.molregno    = m2.molregno 
and m2.molregno   = r.molregno;

--and r.compound_name = 'DOXORUBICIN'; 

drop table if exists uc_names;

create table uc_names
as
select nsc, upper(generic_name) as generic_name, upper(preferred_name) as preferred_name
from app_and_inv;

create index ucn_generic_name on uc_names(generic_name);
create index ucn_preferred_name on uc_names(preferred_name);

vacuum analyze verbose uc_names;

create index cr_compound_name on compound_records(compound_name);

vacuum analyze verbose compound_records;

drop table if exists chembl_name_match;

explain
create table chembl_name_match
as
select ucn.nsc, generic_name, preferred_name, cr.compound_name, cr.molregno, cr.record_id
from uc_names ucn left outer join compound_records cr
on (
ucn.generic_name = cr.compound_name
or 
ucn.preferred_name = cr.compound_name
);

create index cnm_record_id on chembl_name_match(record_id);

vacuum analyze verbose chembl_name_match;

explain 
create table nsc_chembl_target
as
select cnm.nsc, cnm.compound_name, cnm.molregno, cnm.record_id,
  
a.description as assay_description,   
act.standard_type,   
act.standard_relation,   
act.standard_value,   
act.standard_units,   
act.activity_comment,   
t.chembl_id as target_chembl_id,   
t.pref_name as target_name,   
t.target_type 

from chembl_name_match cnm, 
--compound_records r,   
--docs d,   
activities act,   
assays a,   
target_dictionary t 

where cnm.record_id   = act.record_id 
--and r.doc_id      = d.doc_id 
and act.assay_id  = a.assay_id 
and a.tid         = t.tid;