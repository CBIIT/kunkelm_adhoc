\c pptpdb

--populating master from compound_master and cell_line_master

drop table if exists master;

create table master (
        compound_identifier character varying(1024) not null,
        dose double precision not null,
        concentration_unit character varying(1024) not null,
        treatment_route character varying(1024) not null,
        schedule character varying(1024) not null,
        vehicle character varying(1024) not null,
        implant_site character varying(1024) not null,
        cell_line_identifier character varying(1024) not null,
        mouse_type character varying(1024) not null,
        test_number integer not null,				
				group_role character varying(1024) not null,				
        compound_type_key bigint not null,
        concentration_unit_type_key bigint not null,
        treatment_route_type_key bigint not null,
        schedule_type_key bigint not null,
        vehicle_type_key bigint not null,
        implant_site_type_key bigint not null,
        cell_line_type_key bigint not null,
        mouse_type_key bigint not null,				
				group_role_type_key bigint not null				 
    );
 

--cartesian product of compounds and cell lines with placeholders

--once for control and once for treated

--CONTROL

insert into master 
(compound_identifier,
dose,
concentration_unit,
treatment_route,
schedule,
vehicle,
implant_site,
cell_line_identifier,
mouse_type,
test_number,
group_role,
compound_type_key,
concentration_unit_type_key,
treatment_route_type_key,
schedule_type_key,
vehicle_type_key,
implant_site_type_key,
cell_line_type_key,
mouse_type_key,
group_role_type_key
)
select																																		 
c.pptp_identifier,                                                            
10101,                                                                        
'notset',                                                                      
'notset',                                                                         
'notset',                                                                     
'notset',                                                                     
'notset',                                                                     
cl.pptp_identifier,                                                           
'notset',                                                                     
1, --test number 1
'CONTROL',
c.id,
10101,
10101,
10101,
10101,
10101,
cl.id,
10101,
1
from compound_type c, cell_line_type cl;

--TREATED

insert into master 
(compound_identifier,
dose,
concentration_unit,
treatment_route,
schedule,
vehicle,
implant_site,
cell_line_identifier,
mouse_type,
test_number,
group_role,
compound_type_key,
concentration_unit_type_key,
treatment_route_type_key,
schedule_type_key,
vehicle_type_key,
implant_site_type_key,
cell_line_type_key,
mouse_type_key,
group_role_type_key
)
select																																		 
c.pptp_identifier,                                                            
10101,                                                                        
'notset',                                                                      
'notset',                                                                         
'notset',                                                                     
'notset',                                                                     
'notset',                                                                     
cl.pptp_identifier,                                                           
'notset',                                                                     
1, --test number 1
'TREATED',
c.id,
10101,
10101,
10101,
10101,
10101,
cl.id,
10101,
2
from compound_type c, cell_line_type cl;

--
-- RETEST INFO
--

-- CONTROL

insert into master 
(compound_identifier,
dose,
concentration_unit,
treatment_route,
schedule,
vehicle,
implant_site,
cell_line_identifier,
mouse_type,
test_number,
group_role,
compound_type_key,
concentration_unit_type_key,
treatment_route_type_key,
schedule_type_key,
vehicle_type_key,
implant_site_type_key,
cell_line_type_key,
mouse_type_key,
group_role_type_key
)
select																																		 
r.compound_identifier,                                                            
r.dose,                                                                        
'notset',                                                                      
'notset',                                                                         
'notset',                                                                     
'notset',                                                                     
'notset',                                                                     
r.cell_line_identifier,                                                           
'notset',                                                                     
r.test_number,
'CONTROL',
c.id,
10101,
10101,
10101,
10101,
10101,
cl.id,
10101,
1
from retest_type r, compound_type c, cell_line_type cl
where 
r.compound_identifier = c.pptp_identifier
and r.cell_line_identifier = cl.pptp_identifier;


insert into master 
(compound_identifier,
dose,
concentration_unit,
treatment_route,
schedule,
vehicle,
implant_site,
cell_line_identifier,
mouse_type,
test_number,
group_role,
compound_type_key,
concentration_unit_type_key,
treatment_route_type_key,
schedule_type_key,
vehicle_type_key,
implant_site_type_key,
cell_line_type_key,
mouse_type_key,
group_role_type_key
)
select																																		 
r.compound_identifier,                                                            
r.dose,                                                                        
'notset',                                                                      
'notset',                                                                         
'notset',                                                                     
'notset',                                                                     
'notset',                                                                     
r.cell_line_identifier,                                                           
'notset',                                                                     
r.test_number,
'TREATED',
c.id,
10101,
10101,
10101,
10101,
10101,
cl.id,
10101,
2
from retest_type r, compound_type c, cell_line_type cl
where 
r.compound_identifier = c.pptp_identifier
and r.cell_line_identifier = cl.pptp_identifier;

-- set key values for types

--treatment route                                                             
update master 
set treatment_route = compound_master.treatment_route           
from compound_master                                                          
where master.compound_identifier = compound_master.pptp_identifier;

update master 
set treatment_route_type_key = treatment_route_type.id           
from treatment_route_type                                                          
where master.treatment_route = treatment_route_type.display_name;           

--concentration unit                                                          
update master set concentration_unit = compound_master.concentration_unit     
from compound_master 
where master.compound_identifier = compound_master.pptp_identifier;

update master 
set concentration_unit_type_key = concentration_unit_type.id           
from concentration_unit_type                                                          
where master.concentration_unit = concentration_unit_type.display_name;           

--implant site
update master set 
implant_site = cell_line_master.implant_site 
from cell_line_master 
where master.cell_line_identifier = cell_line_master.pptp_identifier;

update master 
set implant_site_type_key = implant_site_type.id           
from implant_site_type                                                          
where master.implant_site = implant_site_type.display_name;           

-- updating of dose checks for value of 10101
-- this way, it won't overwrite non-default, retest doses 

--solid dose
--cell line type id = 1 for all, != 1 for solid tumors
update master 
set dose = compound_master.solid_dose 
from compound_master, cell_line_type 
where master.compound_identifier = compound_master.pptp_identifier 
and master.cell_line_identifier = cell_line_type.pptp_identifier 
and cell_line_type.panel_type_fk != 1
and dose = 10101;

--all dose
update master 
set dose = compound_master.all_dose 
from compound_master, cell_line_type 
where master.compound_identifier = compound_master.pptp_identifier 
and master.cell_line_identifier = cell_line_type.pptp_identifier 
and cell_line_type.panel_type_fk = 1
and dose = 10101;

--schedule
update master set schedule = compound_master.schedule 
from compound_master 
where master.compound_identifier = compound_master.pptp_identifier;

update master 
set schedule_type_key = schedule_type.id           
from schedule_type                                                          
where master.schedule = schedule_type.display_name;           

--vehicle
update master set vehicle = compound_master.vehicle 
from compound_master 
where master.compound_identifier = compound_master.pptp_identifier;

update master 
set vehicle_type_key = vehicle_type.id           
from vehicle_type                                                          
where master.vehicle = vehicle_type.display_name;           

--mouse_type

update master set 
mouse_type = cell_line_master.strain 
from cell_line_master 
where master.cell_line_identifier = cell_line_master.pptp_identifier;

update master 
set mouse_type_key = mouse_type.id           
from mouse_type                                                          
where master.mouse_type = mouse_type.strain;          

