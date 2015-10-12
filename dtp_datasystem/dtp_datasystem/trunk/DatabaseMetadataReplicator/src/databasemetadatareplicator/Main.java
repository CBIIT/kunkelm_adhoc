/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static String[] ds2od = new String[]{
        "jdbc:postgresql://localhost:5432/datasystemdb",
        "mwkunkel",
        "donkie11",
        "jdbc:postgresql://localhost:5432/oncologydrugsdb",
        "mwkunkel",
        "donkie11"
    };

    static final String[] tnwc = new String[]{
        // ad_hoc_cmpd not exported
        //                "ad_hoc_cmpd", " where ",
        //                "ad_hoc_cmpd_fragment", " where ",
        //                "ad_hoc_cmpd_fragment_p_chem", " where ",
        //                "ad_hoc_cmpd_fragment_structure", " where ",

        "cmpd_known_salt", "",
        "nsc_cmpd_type", "",
        "cmpd_alias_type", "",
        "cmpd_relation_type", "",
        "cmpd_fragment_type", "",
        //
        "cmpd_inventory", " where id in (select nsc from for_export)",
        "cmpd_annotation", " where id in (select nsc from for_export)",
        "cmpd_bio_assay", " where id in (select nsc from for_export)",
        "cmpd_legacy_cmpd", " where nsc in (select nsc from for_export)",
        "cmpd_table", " where nsc in (select nsc from for_export)",
        // 
        "cmpd", " where id in (select nsc from for_export)",
        //
        "nsc_cmpd", " where nsc in (select nsc from for_export)",
        //
        "cmpd_fragment", " where nsc_cmpd_fk in (select nsc from for_export)",
        "cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from for_export))",
        "cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from for_export))",
        "cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        // 
        //  cmpd_list not exported
        //                "cmpd_list", " where ",
        //                "cmpd_list_member", " where ",
        //
        "cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        //
        "cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        //
        "cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        "cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        "cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        //
        "cmpd_related", " where nsc_cmpd_fk in (select nsc from for_export)",
        //
        "rdkit_mol", " where nsc in (select nsc from for_export)"
    };

    public static void main(String[] args) {

        Connection destConn = null;
        Connection sourceConn = null;

        try {

            DriverManager.registerDriver(new org.postgresql.Driver());

            sourceConn = DriverManager.getConnection(ds2od[0], ds2od[1], ds2od[2]);
            destConn = DriverManager.getConnection(ds2od[3], ds2od[4], ds2od[5]);

            // archive constraint drop/create statements
            // ConstraintManagement.saveConstraints(destConn, tnwc);

            // drop constraints before build
            ConstraintManagement.dropConstraints(destConn);

            for (int i = 0; i < tnwc.length; i += 2) {
                String curTbl = tnwc[i];
                String whereClause = tnwc[i + 1];
                // scrape out anything remaining
                // Replicator.nuke(destConn, curTbl);
                // replicate the tables
                Replicator.useMetadata(sourceConn, destConn, curTbl, whereClause);
            }
            
            // recreate the constraints
            ConstraintManagement.createConstraints(destConn);
            
            System.out.println("Done! in Main");
            
            sourceConn.close();
            destConn.close();
            
            sourceConn = null;
            destConn = null;

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in DatasystemReplicator.main");
            e.printStackTrace();
        } finally {
            if (destConn != null) {
                try {
                    destConn.close();
                    destConn = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing destConn");
                }
            }
            if (sourceConn != null) {
                try {
                    sourceConn.close();
                    sourceConn = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing sourceConn");
                }
            }
        }
    }

}
