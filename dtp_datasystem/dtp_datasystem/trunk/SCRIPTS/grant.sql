grant select, insert, update, delete on ad_hoc_cmpd to datasystem_user;                    
grant select, insert, update, delete on ad_hoc_cmpd_fragment to datasystem_user;           
grant select, insert, update, delete on ad_hoc_cmpd_fragment_p_chem to datasystem_user;    
grant select, insert, update, delete on ad_hoc_cmpd_fragment_structure to datasystem_user; 
grant select on cmpd to datasystem_user;                           
grant select on cmpd_alias to datasystem_user;                     
grant select on cmpd_alias_type to datasystem_user;                
grant select on cmpd_aliases2nsc_cmpds to datasystem_user;         
grant select on cmpd_annotation to datasystem_user;                
grant select on cmpd_bio_assay to datasystem_user;                 
grant select on cmpd_fragment to datasystem_user;                  
grant select on cmpd_fragment_p_chem to datasystem_user;           
grant select on cmpd_fragment_structure to datasystem_user;        
grant select on cmpd_inventory to datasystem_user;                 
grant select on cmpd_known_salt to datasystem_user;                
grant select on cmpd_legacy_cmpd to datasystem_user;               
grant select, insert, update, delete on cmpd_list to datasystem_user;                      
grant select, insert, update, delete on cmpd_list_member to datasystem_user;               
grant select on cmpd_named_set to datasystem_user;                 
grant select on cmpd_named_sets2nsc_cmpds to datasystem_user;      
grant select on cmpd_plate to datasystem_user;                     
grant select on cmpd_plates2nsc_cmpds to datasystem_user;          
grant select on cmpd_project to datasystem_user;                   
grant select on cmpd_projects2nsc_cmpds to datasystem_user;        
grant select on cmpd_pub_chem_sid to datasystem_user;              
grant select on cmpd_pub_chem_sids2nsc_cmpds to datasystem_user;   
grant select on cmpd_related to datasystem_user;                   
grant select on cmpd_relation_type to datasystem_user;             
grant select on cmpd_table to datasystem_user;                     
grant select on cmpd_target to datasystem_user;                    
grant select on cmpd_targets2nsc_cmpds to datasystem_user;         
grant select on create_constraint_statements to datasystem_user;   
grant select on drop_constraint_statements to datasystem_user;     
grant select on nsc_cmpd to datasystem_user;                       
grant select on nsc_cmpd_type to datasystem_user;                  
grant select on rdkit_mol to datasystem_user;

grant select, update on ad_hoc_cmpd_fragment_p_che_seq to datasystem_user;
grant select, update on ad_hoc_cmpd_fragment_seq to datasystem_user;       
grant select, update on ad_hoc_cmpd_fragment_struc_seq to datasystem_user; 
grant select, update on cmpd_list_member_seq to datasystem_user;           
grant select, update on cmpd_list_seq to datasystem_user;                  



                       List of relations
 Schema |              Name              |   Type   |  Owner   
--------+--------------------------------+----------+----------
 public | ad_hoc_cmpd                    | table    | mwkunkel
 public | ad_hoc_cmpd_fragment           | table    | mwkunkel
 public | ad_hoc_cmpd_fragment_p_che_seq | sequence | mwkunkel
 public | ad_hoc_cmpd_fragment_p_chem    | table    | mwkunkel
 public | ad_hoc_cmpd_fragment_seq       | sequence | mwkunkel
 public | ad_hoc_cmpd_fragment_struc_seq | sequence | mwkunkel
 public | ad_hoc_cmpd_fragment_structure | table    | mwkunkel
 public | cmpd                           | table    | mwkunkel
 public | cmpd_alias                     | table    | mwkunkel
 public | cmpd_alias_seq                 | sequence | mwkunkel
 public | cmpd_alias_type                | table    | mwkunkel
 public | cmpd_alias_type_seq            | sequence | mwkunkel
 public | cmpd_aliases2nsc_cmpds         | table    | mwkunkel
 public | cmpd_annotation                | table    | mwkunkel
 public | cmpd_bio_assay                 | table    | mwkunkel
 public | cmpd_bio_assay_seq             | sequence | mwkunkel
 public | cmpd_fragment                  | table    | mwkunkel
 public | cmpd_fragment_p_chem           | table    | mwkunkel
 public | cmpd_fragment_p_chem_seq       | sequence | mwkunkel
 public | cmpd_fragment_seq              | sequence | mwkunkel
 public | cmpd_fragment_structure        | table    | mwkunkel
 public | cmpd_fragment_structure_seq    | sequence | mwkunkel
 public | cmpd_inventory                 | table    | mwkunkel
 public | cmpd_inventory_seq             | sequence | mwkunkel
 public | cmpd_known_salt                | table    | mwkunkel
 public | cmpd_known_salt_seq            | sequence | mwkunkel
 public | cmpd_legacy_cmpd               | table    | mwkunkel
 public | cmpd_list                      | table    | mwkunkel
 public | cmpd_list_member               | table    | mwkunkel
 public | cmpd_list_member_seq           | sequence | mwkunkel
 public | cmpd_list_seq                  | sequence | mwkunkel
 public | cmpd_named_set                 | table    | mwkunkel
 public | cmpd_named_set_seq             | sequence | mwkunkel
 public | cmpd_named_sets2nsc_cmpds      | table    | mwkunkel
 public | cmpd_plate                     | table    | mwkunkel
 public | cmpd_plate_seq                 | sequence | mwkunkel
 public | cmpd_plates2nsc_cmpds          | table    | mwkunkel
 public | cmpd_project                   | table    | mwkunkel
 public | cmpd_project_seq               | sequence | mwkunkel
 public | cmpd_projects2nsc_cmpds        | table    | mwkunkel
 public | cmpd_pub_chem_sid              | table    | mwkunkel
 public | cmpd_pub_chem_sid_seq          | sequence | mwkunkel
 public | cmpd_pub_chem_sids2nsc_cmpds   | table    | mwkunkel
 public | cmpd_related                   | table    | mwkunkel
 public | cmpd_related_seq               | sequence | mwkunkel
 public | cmpd_relation_type             | table    | mwkunkel
 public | cmpd_relation_type_seq         | sequence | mwkunkel
 public | cmpd_seq                       | sequence | mwkunkel
 public | cmpd_table                     | table    | mwkunkel
 public | cmpd_target                    | table    | mwkunkel
 public | cmpd_target_seq                | sequence | mwkunkel
 public | cmpd_targets2nsc_cmpds         | table    | mwkunkel
 public | hibernate_sequence             | sequence | mwkunkel
 public | nsc_cmpd                       | table    | mwkunkel
 public | nsc_cmpd_type                  | table    | mwkunkel
 public | rdkit_mol                      | table    | mwkunkel
(58 rows)

