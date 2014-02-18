truncate nsc_drug_tracker cascade;
truncate ad_hoc_drug_tracker cascade;
truncate aliases2drug_trackers cascade;
truncate alias cascade;
truncate drug_trackers2targets cascade;
truncate target cascade;
truncate contractor cascade;
truncate drug_tracker cascade;
truncate drug_status cascade;
truncate drug_tracker_plate cascade;
truncate acquisition cascade;
truncate acquisition_status_type cascade;
truncate acquisition_type cascade;

insert into drug_status values (1, 'INVESTIGATIONAL');
insert into drug_status values (2, 'APPROVED');
insert into drug_status values (3, 'WATCH');
insert into drug_status values (4, 'QUEUE');
insert into drug_status values (5, 'OTHER');
 
insert into drug_tracker_plate values (1, 'INVESTIGATIONAL');
insert into drug_tracker_plate values (2, 'APPROVED');
insert into drug_tracker_plate values (3, 'NOT PLATED');

insert into acquisition_type values (1, 'PURCHASE');
insert into acquisition_type values (2, 'SYNTHESIS');
insert into acquisition_type values (3, 'NOT SPECIFIED');

insert into acquisition_status_type values (1, 'ON ORDER');
insert into acquisition_status_type values (2, 'IN PROCESS');
insert into acquisition_status_type values (3, 'DELIVERED');
insert into acquisition_status_type values (4, 'NOT SPECIFIED');
insert into acquisition_status_type values (5, 'AVAILABLE');

--top level drug_tracker 

insert into drug_tracker(id, agent, originator, cas, drug_status_fk, drug_tracker_plate_fk)
select l.id, l.agent, l.originator, l.cas, ds.id, dtp.id
from loading l left outer join drug_tracker_plate dtp
on l.onplate = dtp.plate_name
left outer join drug_status ds 
on l.approvedorinvestigat = ds.drug_status;

--for nsc compounds

insert into nsc_drug_tracker(id, nsc, prefix, standardized_smiles_fk) 
select l.id, l.nsc, 'S', ss.id 
from loading l left outer join standardized_smiles ss on l.nsc = ss.nsc
where l.nsc is not null;

--ad hoc compounds

insert into ad_hoc_drug_tracker(id, smiles) 
select id, smiles 
from loading
where nsc is null;

--targets

drop table if exists distinct_targets;

create table distinct_targets
as
select distinct target from temp_target;

insert into target(id, target) 
select nextval('hibernate_sequence'), target from distinct_targets;

--THIS WORKS BECAUSE THE tt.id is actually the id from loading which ends up being the id from drug_tracker
--EVERYTHING is referenced back to the initial combining of the APPROVED and INVESTIGATIONAL lists

insert into drug_trackers2targets(targets_fk, drug_trackers_fk)
select t.id, tt.id
from target t, temp_target tt
where tt.target = t.target;

--aliases

drop table if exists distinct_aliases;

create table distinct_aliases
as
select distinct alias from temp_alias;

insert into alias(id, alias) 
select nextval('hibernate_sequence'), alias from distinct_aliases;

insert into aliases2drug_trackers(aliases_fk, drug_trackers_fk)
select a.id, ta.id
from alias a, temp_alias ta
where ta.alias = a.alias;

-- clean up temp and distinct tables;

drop table if exists temp_alias;
drop table if exists temp_target;
drop table if exists temp_contractor;
drop table if exists distinct_aliases;
drop table if exists distinct_targets;
drop table if exists distinct_contractors;

