drop table if exists temp_pains;

create table temp_pains(source varchar(1024), smarts varchar(1024), descriptor varchar(1024));

\copy temp_pains from ../PAINS/combo.csv delimiter as E'\t'

drop sequence if exists pains_seq;

create sequence pains_seq;

drop table if exists pains;

create table pains(id bigint, source varchar(1024), smarts varchar(1024), descriptor varchar(1024), mol mol);

insert into pains(id, source, smarts, descriptor, mol)
select
nextval('pains_seq'), source, smarts, descriptor, mol_from_smarts(smarts::cstring) as mol
from temp_pains;

select mol from cmpd_fragment_structure 
where mol @> 
(
select mol 
from pains 
where descriptor = '<regId="diazox_sulfon_B(5)">'
);

drop table if exists pains_survey;

create table pains_survey 
as
select p.descriptor, p.smarts, s.id
from pains p, cmpd_fragment_structure s
where s.mol @> p.mol;

drop table if exists summary_by_pains;;

create table summary_by_pains
as
select p.descriptor, p.smarts, count(id)
from pains_survey p
group by p.descriptor, p.smarts;
