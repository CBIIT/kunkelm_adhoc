truncate ad_hoc_drug_tracker cascade;     
truncate aliases2drug_trackers cascade;   
truncate drug_tracker cascade;            
truncate drug_tracker_alias cascade;      
truncate drug_tracker_plate cascade;      
truncate drug_tracker_set cascade;        
truncate drug_tracker_target cascade;     
truncate drug_trackers2targets cascade;   
truncate nsc_drug_tracker cascade;        
truncate rdkit_mol cascade;               
truncate standardized_smiles cascade;

insert into drug_tracker_set(id, set_name) values (1, 'INVESTIGATIONAL');
insert into drug_tracker_set(id, set_name) values (2, 'APPROVED');
insert into drug_tracker_set(id, set_name) values (3, 'APPROVED NON-ONCOLOGY');
 
insert into drug_tracker_plate(id, plate_name) values (1, 'INVESTIGATIONAL');
insert into drug_tracker_plate(id, plate_name) values (2, 'APPROVED');
insert into drug_tracker_plate(id, plate_name) values (3, 'NOT PLATED');

--top level drug_tracker 

insert into drug_tracker(id, agent, originator, cas)
select id, agent, originator, cas
from loading;

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- PLATE(S)
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

--join(s) to plate(s)

--drug_tracker.id synchs loading.id

--drugtrackerdb=# \d drug_tracker_plates2drug_track
--Table "public.drug_tracker_plates2drug_track"
--         Column         |  Type  | Modifiers 
--------------------------+--------+-----------
-- drug_tracker_plates_fk | bigint | not null
-- drug_trackers_fk       | bigint | not null
--Indexes:
--    "drug_tracker_plates2drug_track_pkey" PRIMARY KEY, btree (drug_tracker_plates_fk, drug_trackers_fk)
--Foreign-key constraints:
--    "drug_tracker_drug_tracker_plac" FOREIGN KEY (drug_tracker_plates_fk) REFERENCES drug_tracker_plate(id)
--    "drug_tracker_plate_drug_trackc" FOREIGN KEY (drug_trackers_fk) REFERENCES drug_tracker(id)


insert into drug_tracker_plates2drug_track(drug_tracker_plates_fk, drug_trackers_fk)
select 1, id
from loading 
where oninvestigationalplate = 'YES';

insert into drug_tracker_plates2drug_track(drug_tracker_plates_fk, drug_trackers_fk)
select 2, id
from loading 
where onapprovedplate = 'YES';

insert into drug_tracker_plates2drug_track(drug_tracker_plates_fk, drug_trackers_fk)
select 3, id
from loading 
where oninvestigationalplate = 'NO' 
and onapprovedplate = 'NO';

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- SET(S)
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

--drugtrackerdb=# \d drug_tracker_sets2drug_tracker
--Table "public.drug_tracker_sets2drug_tracker"
--        Column        |  Type  | Modifiers 
------------------------+--------+-----------
-- drug_tracker_sets_fk | bigint | not null
-- drug_trackers_fk     | bigint | not null
--Indexes:
--    "drug_tracker_sets2drug_tracker_pkey" PRIMARY KEY, btree (drug_tracker_sets_fk, drug_trackers_fk)
--Foreign-key constraints:
--    "drug_tracker_drug_tracker_setc" FOREIGN KEY (drug_tracker_sets_fk) REFERENCES drug_tracker_set(id)
--    "drug_tracker_set_drug_trackerc" FOREIGN KEY (drug_trackers_fk) REFERENCES drug_tracker(id)

-- for now this can be a single-step join since each entry is a member of ONLY one set

insert into drug_tracker_sets2drug_tracker(drug_tracker_sets_fk, drug_trackers_fk)
select set.id, l.id
from loading l, drug_tracker_set set
where l.type = set.set_name;

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- NSC COMPOUNDS
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

--for nsc compounds

insert into nsc_drug_tracker(id, nsc, prefix, standardized_smiles_fk) 
select l.id, l.nsc, 'S', ss.id 
from loading l left outer join standardized_smiles ss on l.nsc = ss.nsc
where l.nsc is not null
and ss.prefix = 'S';


--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- TARGETS and ALIASES
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

--drugtrackerdb=# \d drug_tracker_targets2drug_trac;
--Table "public.drug_tracker_targets2drug_trac"
--         Column          |  Type  | Modifiers 
---------------------------+--------+-----------
-- drug_tracker_targets_fk | bigint | not null
-- drug_trackers_fk        | bigint | not null
--Indexes:
--    "drug_tracker_targets2drug_trac_pkey" PRIMARY KEY, btree (drug_tracker_targets_fk, drug_trackers_fk)
--Foreign-key constraints:
--    "drug_tracker_drug_tracker_tarc" FOREIGN KEY (drug_tracker_targets_fk) REFERENCES drug_tracker_target(id)
--    "drug_tracker_target_drug_tracc" FOREIGN KEY (drug_trackers_fk) REFERENCES drug_tracker(id)

drop table if exists distinct_targets;

create table distinct_targets
as
select distinct target from temp_target;

insert into drug_tracker_target(id, target) 
select nextval('hibernate_sequence'), target from distinct_targets;

--THIS WORKS BECAUSE THE tt.id is actually the id from loading which ends up being the id from drug_tracker
--EVERYTHING is referenced back to the initial combining of the APPROVED and INVESTIGATIONAL lists

insert into drug_tracker_targets2drug_trac(drug_tracker_targets_fk, drug_trackers_fk)
select t.id, tt.id
from drug_tracker_target t, temp_target tt
where tt.target = t.target;

--drugtrackerdb=# \d drug_tracker_aliases2drug_trac
--Table "public.drug_tracker_aliases2drug_trac"
--         Column          |  Type  | Modifiers 
---------------------------+--------+-----------
-- drug_tracker_aliases_fk | bigint | not null
-- drug_trackers_fk        | bigint | not null
--Indexes:
--    "drug_tracker_aliases2drug_trac_pkey" PRIMARY KEY, btree (drug_tracker_aliases_fk, drug_trackers_fk)
--Foreign-key constraints:
--    "drug_tracker_alias_drug_trackc" FOREIGN KEY (drug_trackers_fk) REFERENCES drug_tracker(id)
--    "drug_tracker_drug_tracker_alic" FOREIGN KEY (drug_tracker_aliases_fk) REFERENCES drug_tracker_alias(id)

drop table if exists distinct_aliases;

create table distinct_aliases
as
select distinct alias from temp_alias;

insert into drug_tracker_alias(id, alias) 
select nextval('hibernate_sequence'), alias from distinct_aliases;

insert into drug_tracker_aliases2drug_trac(drug_tracker_aliases_fk, drug_trackers_fk)
select a.id, ta.id
from drug_tracker_alias a, temp_alias ta
where ta.alias = a.alias;

-- clean up temp and distinct tables;

drop table if exists temp_alias;
drop table if exists temp_target;
drop table if exists temp_contractor;
drop table if exists distinct_aliases;
drop table if exists distinct_targets;
drop table if exists distinct_contractors;

-- qc
select count(*) from loading_approved;
select count(*) from loading_approved_nononc;
select count(*) from loading_investigational;
select count(*) from drug_tracker;
select count(*) from nsc_drug_tracker;
select count(*) from ad_hoc_drug_tracker;

drugtrackerdb=# select nsc from loading except select nsc from nsc_drug_tracker;
  nsc   
--------
       << Palidomide
 773262
 773260
 773264
 773261
 773263
 756651
 119875
(8 rows)

