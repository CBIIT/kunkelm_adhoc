grant select, insert, update, delete on ad_hoc_cmpd to datasystem_user;                    
grant select, insert, update, delete on ad_hoc_cmpd_fragment to datasystem_user;           
grant select, insert, update, delete on ad_hoc_cmpd_fragment_p_chem to datasystem_user;    
grant select, insert, update, delete on ad_hoc_cmpd_fragment_structure to datasystem_user; 
grant select, insert, update, delete on cmpd to datasystem_user; 
grant select, insert, update, delete on cmpd_table to datasystem_user; 

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
                   
grant select on cmpd_target to datasystem_user;                    
grant select on cmpd_targets2nsc_cmpds to datasystem_user;         
grant select on create_constraint_statements to datasystem_user;   
grant select on drop_constraint_statements to datasystem_user;     
grant select on nsc_cmpd to datasystem_user;                       
grant select on nsc_cmpd_type to datasystem_user;                  
grant select on rdkit_mol to datasystem_user;

grant select, update on cmpd_seq to datasystem_user;
grant select, update on ad_hoc_cmpd_fragment_p_che_seq to datasystem_user;
grant select, update on ad_hoc_cmpd_fragment_seq to datasystem_user;       
grant select, update on ad_hoc_cmpd_fragment_struc_seq to datasystem_user; 
grant select, update on cmpd_list_member_seq to datasystem_user;           
grant select, update on cmpd_list_seq to datasystem_user;                  

