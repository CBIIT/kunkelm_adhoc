/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkinglot;

/**
 *
 * @author mwkunkel
 */
public class ParkingLot {

    /*
     explain select * from cmpd_known_salt;
     explain select * from nsc_cmpd_type;
     explain select * from cmpd_alias_type;
     explain select * from cmpd_relation_type;
     explain select * from cmpd_fragment_type;
     explain select * from cmpd_inventory where id in (select nsc from for_export);
     explain select * from cmpd_annotation where id in (select nsc from for_export);
     explain select * from cmpd_bio_assay where id in (select nsc from for_export);
     explain select * from cmpd_legacy_cmpd where nsc in (select nsc from for_export);
     explain select * from cmpd_table where nsc in (select nsc from for_export);
     explain select * from cmpd where id in (select nsc from for_export);
     explain select * from nsc_cmpd where nsc in (select nsc from for_export);
     explain select * from cmpd_fragment where nsc_cmpd_fk in (select nsc from for_export);
     explain select * from cmpd_fragment_p_chem where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from for_export));
     explain select * from cmpd_fragment_structure where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from for_export));
     explain select * from cmpd_alias where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export));
     explain select * from cmpd_aliases2nsc_cmpds where nsc_cmpds_fk in (select nsc from for_export);
     explain select * from cmpd_named_set where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export));
     explain select * from cmpd_named_sets2nsc_cmpds where nsc_cmpds_fk in (select nsc from for_export);
     explain select * from cmpd_plate where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export));
     explain select * from cmpd_plates2nsc_cmpds where nsc_cmpds_fk in (select nsc from for_export);
     explain select * from cmpd_project where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export));
     explain select * from cmpd_projects2nsc_cmpds where nsc_cmpds_fk in (select nsc from for_export);
     explain select * from cmpd_pub_chem_sid where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export));
     explain select * from cmpd_pub_chem_sids2nsc_cmpds where nsc_cmpds_fk in (select nsc from for_export);
     explain select * from cmpd_target where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export));
     explain select * from cmpd_targets2nsc_cmpds where nsc_cmpds_fk in (select nsc from for_export);
     explain select * from cmpd_related where nsc_cmpd_fk in (select nsc from for_export);
     explain select * from rdkit_mol where nsc in (select nsc from for_export);
     */
}
