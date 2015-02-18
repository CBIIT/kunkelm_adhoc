update ad_hoc_cmpd set ad_hoc_cmpd_parent_fragment_fk = null;
update ad_hoc_cmpd_fragment set ad_hoc_cmpd_fragment_p_chem_fk = null;
update ad_hoc_cmpd_fragment set ad_hoc_cmpd_fragment_struct_fk = null;
update ad_hoc_cmpd_fragment set ad_hoc_cmpd_fk = null;
delete from ad_hoc_cmpd_fragment;
delete from ad_hoc_cmpd;
delete from cmpd_table where ad_hoc_cmpd_id is not null and original_ad_hoc_cmpd_id is not null;


