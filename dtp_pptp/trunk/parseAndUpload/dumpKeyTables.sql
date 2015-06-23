\c pptpdb

-- these are NOT pointing to the location that is used by restoreKeyTables.sql
-- it will require a MANUAL copy of files in order to overwrite old key tables

\copy affy_identifier_type to '/home/mwkunkel/PPTP/keyTables/affy_identifier_type.keyTable.csv' csv header null as ''
\copy cell_line_master to '/home/mwkunkel/PPTP/keyTables/cell_line_master.keyTable.csv' csv header null as ''
\copy cell_line_type to '/home/mwkunkel/PPTP/keyTables/cell_line_type.keyTable.csv' csv header null as ''
\copy cell_type to '/home/mwkunkel/PPTP/keyTables/cell_type.keyTable.csv' csv header null as ''
\copy compound_master to '/home/mwkunkel/PPTP/keyTables/compound_master.keyTable.csv' csv header null as ''
\copy compound_type to '/home/mwkunkel/PPTP/keyTables/compound_type.keyTable.csv' csv header null as ''
\copy concentration_unit_type to '/home/mwkunkel/PPTP/keyTables/concentration_unit_type.keyTable.csv' csv header null as ''
\copy group_role_type to '/home/mwkunkel/PPTP/keyTables/group_role_type.keyTable.csv' csv header null as ''
\copy implant_site_type to '/home/mwkunkel/PPTP/keyTables/implant_site_type.keyTable.csv' csv header null as ''
\copy mouse_type to '/home/mwkunkel/PPTP/keyTables/mouse_type.keyTable.csv' csv header null as ''
\copy panel_type to '/home/mwkunkel/PPTP/keyTables/panel_type.keyTable.csv' csv header null as ''
\copy retest_type to '/home/mwkunkel/PPTP/keyTables/retest_type.keyTable.csv' csv header null as ''
\copy schedule_type to '/home/mwkunkel/PPTP/keyTables/schedule_type.keyTable.csv' csv header null as ''
\copy treatment_route_type to '/home/mwkunkel/PPTP/keyTables/treatment_route_type.keyTable.csv' csv header null as ''
\copy vehicle_type to '/home/mwkunkel/PPTP/keyTables/vehicle_type.keyTable.csv' csv header null as ''
