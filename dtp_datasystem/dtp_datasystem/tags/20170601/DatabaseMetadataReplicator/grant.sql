grant select on ad_hoc_cmpd to oncology_user;                    
grant select on ad_hoc_cmpd_fragment to oncology_user;           
grant select on ad_hoc_cmpd_fragment_p_chem to oncology_user;    
grant select on ad_hoc_cmpd_fragment_structure to oncology_user; 
grant select on build_date to oncology_user;                     
grant select on cell_line_data_set to oncology_user;             
grant select on cell_line_data_set_ident to oncology_user;       
grant select on cell_line_data_sets2named_targ to oncology_user; 
grant select on cmpd to oncology_user;                           
grant select on cmpd_alias to oncology_user;                     
grant select on cmpd_alias_type to oncology_user;                
grant select on cmpd_aliases2nsc_cmpds to oncology_user;         
grant select on cmpd_annotation to oncology_user;                
grant select on cmpd_bio_assay to oncology_user;                 
grant select on cmpd_fragment to oncology_user;                  
grant select on cmpd_fragment_p_chem to oncology_user;           
grant select on cmpd_fragment_structure to oncology_user;        
grant select on cmpd_fragment_type to oncology_user;             
grant select on cmpd_inventory to oncology_user;                 
grant select on cmpd_known_salt to oncology_user;                
grant select on cmpd_legacy_cmpd to oncology_user;               
grant select on cmpd_list to oncology_user;                      
grant select on cmpd_list_member to oncology_user;               
grant select on cmpd_named_set to oncology_user;                 
grant select on cmpd_named_sets2nsc_cmpds to oncology_user;      
grant select on cmpd_plate to oncology_user;                     
grant select on cmpd_plates2nsc_cmpds to oncology_user;          
grant select on cmpd_project to oncology_user;                   
grant select on cmpd_projects2nsc_cmpds to oncology_user;        
grant select on cmpd_pub_chem_sid to oncology_user;              
grant select on cmpd_pub_chem_sids2nsc_cmpds to oncology_user;   
grant select on cmpd_related to oncology_user;                   
grant select on cmpd_relation_type to oncology_user;             
grant select on cmpd_table to oncology_user;                     
grant select on cmpd_target to oncology_user;                    
grant select on cmpd_targets2nsc_cmpds to oncology_user;         
grant select on compare_cell_line to oncology_user;              

grant insert, select, update on compare_result to oncology_user;                 

grant select on conc_resp_element to oncology_user;              
grant select on create_constraint_statements to oncology_user;   
grant select on drop_constraint_statements to oncology_user;     
grant select on five_conc_assay to oncology_user;

grant insert, select, update on grid_compare_columns to oncology_user;           
grant insert, select, update on grid_compare_job to oncology_user;               
grant insert, select, update on grid_compare_rows to oncology_user;              
grant insert, select, update on job to oncology_user;                            

grant select on mol_targ_ident to oncology_user;                 
grant select on named_target_set to oncology_user;               
grant select on nsc_cmpd to oncology_user;                       
grant select on nsc_cmpd_type to oncology_user;                  
grant select on nsc_compound to oncology_user;                   
grant select on nsc_ident to oncology_user;                      
grant select on rdkit_mol to oncology_user;

grant insert, select, update on require_use_ignore to oncology_user;             
grant insert, select, update on standard_compare_job to oncology_user;

grant select on test_result to oncology_user;                    
grant select on test_result_type to oncology_user;               
grant select on uploaded_ident to oncology_user;         

grant all on job_seq to oncology_user;
grant all on compare_result_seq to oncology_user;
grant all on hibernate_sequence to oncology_user;

