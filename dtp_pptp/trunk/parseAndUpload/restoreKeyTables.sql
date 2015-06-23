\c pptpdb

truncate cell_type cascade;
truncate affy_identifier_type cascade;
truncate cell_line_master cascade;
truncate cell_line_type cascade;
truncate compound_master cascade;
truncate compound_type cascade;
truncate concentration_unit_type cascade;
truncate group_role_type cascade;
truncate implant_site_type cascade;
truncate mouse_type cascade;
truncate panel_type cascade;
truncate retest_type cascade;
truncate schedule_type cascade;
truncate treatment_route_type cascade;
truncate vehicle_type cascade;

\copy panel_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/panel_type.keyTable.csv' csv header null as ''
\copy cell_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/cell_type.keyTable.csv' csv header null as ''
\copy affy_identifier_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/affy_identifier_type.keyTable.csv' csv header null as ''
\copy cell_line_master from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/cell_line_master.keyTable.csv' csv header null as ''
\copy cell_line_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/cell_line_type.keyTable.csv' csv header null as ''
\copy compound_master from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/compound_master.keyTable.csv' csv header null as ''
\copy compound_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/compound_type.keyTable.csv' csv header null as ''
\copy concentration_unit_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/concentration_unit_type.keyTable.csv' csv header null as ''
\copy group_role_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/group_role_type.keyTable.csv' csv header null as ''
\copy implant_site_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/implant_site_type.keyTable.csv' csv header null as ''
\copy mouse_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/mouse_type.keyTable.csv' csv header null as ''
\copy retest_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/retest_type.keyTable.csv' csv header null as ''
\copy schedule_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/schedule_type.keyTable.csv' csv header null as ''
\copy treatment_route_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/treatment_route_type.keyTable.csv' csv header null as ''
\copy vehicle_type from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/keyTables/vehicle_type.keyTable.csv' csv header null as ''

