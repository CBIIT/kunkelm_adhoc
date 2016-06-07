-- global fixes
-- these are applied to fields_and_entries
-- since white space issues will have already been fixed

--                 _           _       
-- _ __  _ __ ___ (_) ___  ___| |_ ___ 
--| '_ \| '__/ _ \| |/ _ \/ __| __/ __|
--| |_) | | | (_) | |  __/ (__| |_\__ \
--| .__/|_|  \___// |\___|\___|\__|___/
--|_|           |__/                   

update fields_and_entries
set entry = 'DTP-110'
where entry = 'DPT-110'
and field_name = 'project_code';


--            _       _             _                 
--  ___  _ __(_) __ _(_)_ __   __ _| |_ ___  _ __ ___ 
-- / _ \| '__| |/ _` | | '_ \ / _` | __/ _ \| '__/ __|
--| (_) | |  | | (_| | | | | | (_| | || (_) | |  \__ \
-- \___/|_|  |_|\__, |_|_| |_|\__,_|\__\___/|_|  |___/
--              |___/                                 

update fields_and_entries
set entry = 'AbbVie'
where entry = 'Abbvie'
and field_name = 'originator';

update fields_and_entries
set entry = 'ArQule'
where entry = 'Arqule'
and field_name = 'originator';

update fields_and_entries
set entry = 'AstraZeneca'
where entry = 'Astra Zeneca'
and field_name = 'originator';

-- _                       _       
--| |_ __ _ _ __ __ _  ___| |_ ___ 
--| __/ _` | '__/ _` |/ _ \ __/ __|
--| || (_| | | | (_| |  __/ |_\__ \
-- \__\__,_|_|  \__, |\___|\__|___/
--              |___/              

drop table if exists target_fixes;

create table target_fixes(
fix varchar,
orig varchar
);

insert into target_fixes values('Bcr-Abl', 'Bcr-abl');
insert into target_fixes values('Cdk', 'CDK');
insert into target_fixes values('Chk1', 'Chk-1');
insert into target_fixes values('Erk1', 'ERK1');
insert into target_fixes values('Flt3', 'Flt-3');
insert into target_fixes values('IGF1R', 'IGF-1R');
insert into target_fixes values('Ret', 'RET');
insert into target_fixes values('Stat3', 'Stat-3');
 insert into target_fixes values('Stat3', 'Stat 3');
insert into target_fixes values('Tie2', 'Tie-2');
insert into target_fixes values('TopoII', 'TOPO II');
 insert into target_fixes values('TopoII', 'Topo II');

insert into target_fixes values('DNA alkylator or DNA alkylating','DNA Alkylator');
insert into target_fixes values('DNA alkylator or DNA alkylating','DNA alkylating');
 insert into target_fixes values('DNA alkylator or DNA alkylating','DNA Alkylating');
insert into target_fixes values('DNA damaging', 'DNA-damaging');

insert into target_fixes values('Gamma secretase', 'Gamma Secretase');

insert into target_fixes values('Ribonucleotide reductase', 'ribonucleotide reductase');

insert into target_fixes values('Thymidylate synthase', 'thymidylate synthase');
insert into target_fixes values('Thymidylate synthase', 'Thymidylate Synthase');

update fields_and_entries
set entry = target_fixes.fix
from target_fixes
where entry = target_fixes.orig
and (field_name  = 'primary_target' or field_name = 'other_targets');
