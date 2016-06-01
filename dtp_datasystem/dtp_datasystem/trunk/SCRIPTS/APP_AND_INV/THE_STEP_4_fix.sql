-- global fixes
-- these are applied to fields_and_entries
-- since white space issues will have already been fixed

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

update fields_and_entries
set entry = 'Bcr-Abl'
where entry = 'Bcr-abl'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Cdk'
where entry = 'CDK'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Flt3'
where entry = 'Flt-3'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Ret'
where entry = 'RET'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Tie2'
where entry = 'Tie-2'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Chk1'
where entry = 'Chk-1'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'DNA alkylator'
where entry = 'DNA Alkylator'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Gamma secretase'
where entry = 'Gamma Secretase'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Ribonucleotide reductase'
where entry = 'ribonucleotide reductase'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Stat3'
where ( entry = 'Stat-3' or entry = 'Stat 3' )
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Thymidylate synthase'
where ( entry = 'thymidylate synthase' or entry = 'Thymidylate Synthase' )
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'TopoII'
where ( entry = 'TOPO II' or entry = 'Topo II' )
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'DNA damaging'
where entry = 'DNA-damaging'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'ErkI'
where entry = 'ERK1'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'IGF1R'
where entry = 'IGF-1R'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = ''
where entry = ''
and field_name in ('primary_target', 'other_targets');
