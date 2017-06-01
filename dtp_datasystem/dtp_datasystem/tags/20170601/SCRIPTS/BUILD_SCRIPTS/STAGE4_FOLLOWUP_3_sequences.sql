grant all on aliases2curated_nsc_to_aliases  to datasystem_user;
grant all on aliases2curated_nsc_to_aliases  to oncology_user;
 
grant all on curated_name  to datasystem_user;
grant all on curated_name  to oncology_user;
                   
grant all on curated_nsc  to datasystem_user;
grant all on curated_nsc  to oncology_user;
                    
grant all on curated_nsc_to_secondary_targe  to datasystem_user;
grant all on curated_nsc_to_secondary_targe  to oncology_user;
 
grant all on curated_nscs2projects  to datasystem_user;
grant all on curated_nscs2projects  to oncology_user;
          
grant all on curated_originator  to datasystem_user;
grant all on curated_originator  to oncology_user;
             
grant all on curated_project  to datasystem_user;
grant all on curated_project  to oncology_user;
                
grant all on curated_target  to datasystem_user;
grant all on curated_target  to oncology_user;
  
grant all on curated_nsc_seq  to datasystem_user;
grant all on curated_nsc_seq  to oncology_user;
  
grant all on curated_name_seq  to datasystem_user;
grant all on curated_name_seq  to oncology_user;
  
grant all on curated_originator_seq  to datasystem_user;
grant all on curated_originator_seq  to oncology_user;
  
grant all on curated_project_seq  to datasystem_user;
grant all on curated_project_seq  to oncology_user;
  
grant all on curated_target_seq  to datasystem_user;
grant all on curated_target_seq  to oncology_user;
  
select setval('curated_nsc_seq', (select max(id) from curated_nsc));
select setval('curated_name_seq', (select max(id) from curated_name));
select setval('curated_originator_seq', (select max(id) from curated_originator));
select setval('curated_project_seq', (select max(id) from curated_project));
select setval('curated_target_seq', (select max(id) from curated_target));

-- APP can create ad hoc cmpds

grant select, insert, update, delete on ad_hoc_cmpd  to datasystem_user;
grant select, insert, update, delete on ad_hoc_cmpd  to oncology_user;
                    
grant select, insert, update, delete on ad_hoc_cmpd_fragment  to datasystem_user;
grant select, insert, update, delete on ad_hoc_cmpd_fragment  to oncology_user;
           
grant select, insert, update, delete on ad_hoc_cmpd_fragment_p_chem  to datasystem_user;
grant select, insert, update, delete on ad_hoc_cmpd_fragment_p_chem  to oncology_user;
    
grant select, insert, update, delete on ad_hoc_cmpd_fragment_structure  to datasystem_user;
grant select, insert, update, delete on ad_hoc_cmpd_fragment_structure  to oncology_user;
 


grant select on cmpd  to datasystem_user;
grant select on cmpd  to oncology_user;
 
grant select on cmpd_fragment  to datasystem_user;
grant select on cmpd_fragment  to oncology_user;
                  
grant select on cmpd_known_salt  to datasystem_user;
grant select on cmpd_known_salt  to oncology_user;
 
grant select on cmpd_legacy_cmpd  to datasystem_user;
grant select on cmpd_legacy_cmpd  to oncology_user;
   

-- APP can create cmpd_list and manage cmpd_list_members 

grant select, insert, update, delete on cmpd_list  to datasystem_user;
grant select, insert, update, delete on cmpd_list  to oncology_user;
                      
grant select, insert, update, delete on cmpd_list_member  to datasystem_user;
grant select, insert, update, delete on cmpd_list_member  to oncology_user;
               
grant select, insert, update, delete on cmpd_table  to datasystem_user;
grant select, insert, update, delete on cmpd_table  to oncology_user;
 

grant select on cmpd_alias  to datasystem_user;
grant select on cmpd_alias  to oncology_user;
                     
grant select on cmpd_alias_type  to datasystem_user;
grant select on cmpd_alias_type  to oncology_user;
                
grant select on cmpd_aliases2nsc_cmpds  to datasystem_user;
grant select on cmpd_aliases2nsc_cmpds  to oncology_user;
         
grant select on cmpd_annotation  to datasystem_user;
grant select on cmpd_annotation  to oncology_user;
                
grant select on cmpd_bio_assay  to datasystem_user;
grant select on cmpd_bio_assay  to oncology_user;
                 
grant select on cmpd_fragment_p_chem  to datasystem_user;
grant select on cmpd_fragment_p_chem  to oncology_user;
           
grant select on cmpd_fragment_structure  to datasystem_user;
grant select on cmpd_fragment_structure  to oncology_user;
        
grant select on cmpd_fragment_type  to datasystem_user;
grant select on cmpd_fragment_type  to oncology_user;
  
grant select on cmpd_inventory  to datasystem_user;
grant select on cmpd_inventory  to oncology_user;
                 
grant select on cmpd_known_salt  to datasystem_user;
grant select on cmpd_known_salt  to oncology_user;
                
            
grant select on cmpd_named_set  to datasystem_user;
grant select on cmpd_named_set  to oncology_user;
                 
grant select on cmpd_named_sets2nsc_cmpds  to datasystem_user;
grant select on cmpd_named_sets2nsc_cmpds  to oncology_user;
      
grant select on cmpd_plate  to datasystem_user;
grant select on cmpd_plate  to oncology_user;
                     
grant select on cmpd_plates2nsc_cmpds  to datasystem_user;
grant select on cmpd_plates2nsc_cmpds  to oncology_user;
          
grant select on cmpd_project  to datasystem_user;
grant select on cmpd_project  to oncology_user;
                   
grant select on cmpd_projects2nsc_cmpds  to datasystem_user;
grant select on cmpd_projects2nsc_cmpds  to oncology_user;
        
grant select on cmpd_pub_chem_sid  to datasystem_user;
grant select on cmpd_pub_chem_sid  to oncology_user;
              
grant select on cmpd_pub_chem_sids2nsc_cmpds  to datasystem_user;
grant select on cmpd_pub_chem_sids2nsc_cmpds  to oncology_user;
   
grant select on cmpd_related  to datasystem_user;
grant select on cmpd_related  to oncology_user;
                   
grant select on cmpd_relation_type  to datasystem_user;
grant select on cmpd_relation_type  to oncology_user;
    
grant select on cmpd_target  to datasystem_user;
grant select on cmpd_target  to oncology_user;
                    
grant select on cmpd_targets2nsc_cmpds  to datasystem_user;
grant select on cmpd_targets2nsc_cmpds  to oncology_user;
         
grant select on nsc_cmpd  to datasystem_user;
grant select on nsc_cmpd  to oncology_user;
                       
grant select on nsc_cmpd_type  to datasystem_user;
grant select on nsc_cmpd_type  to oncology_user;
                  
grant select on rdkit_mol  to datasystem_user;
grant select on rdkit_mol  to oncology_user;


-- SEQUENCES

grant select, update on ad_hoc_cmpd_fragment_p_che_seq  to datasystem_user;
grant select, update on ad_hoc_cmpd_fragment_p_che_seq  to oncology_user;

grant select, update on ad_hoc_cmpd_fragment_seq  to datasystem_user;
grant select, update on ad_hoc_cmpd_fragment_seq  to oncology_user;
       
grant select, update on ad_hoc_cmpd_fragment_struc_seq  to datasystem_user;
grant select, update on ad_hoc_cmpd_fragment_struc_seq  to oncology_user;
 
grant select, update on cmpd_legacy_cmpd_seq  to datasystem_user;
grant select, update on cmpd_legacy_cmpd_seq  to oncology_user;
           
grant select, update on cmpd_list_member_seq  to datasystem_user;
grant select, update on cmpd_list_member_seq  to oncology_user;
           
grant select, update on cmpd_list_seq  to datasystem_user;
grant select, update on cmpd_list_seq  to oncology_user;
                  
grant select, update on cmpd_seq  to datasystem_user;
grant select, update on cmpd_seq  to oncology_user;