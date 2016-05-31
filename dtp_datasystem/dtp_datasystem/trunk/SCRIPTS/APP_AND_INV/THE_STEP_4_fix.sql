-- global fixes
-- these are applied to fields_and_entries
-- since white space issues will have already been fixed

update fields_and_entries
set entry = 'DTP-110'
where entry = 'DPT-110'
and field_name = 'project_code';

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

update fields_and_entries
set entry = 'Bcr-Abl'
where entry = 'Bcr-abl'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'DNA Alkylating'
where entry = 'DNA alkylating'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Gamma Secretase'
where entry = 'Gamma secretase'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'IGF1R'
where entry = 'IGF-1R'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Stat3'
where entry in ('Stat 3', 'Stat-3')
and field_name in ('primary_target', 'other_targets');