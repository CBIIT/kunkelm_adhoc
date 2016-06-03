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

    // microxeno from local to dev (TEMPORARILY using pptp dev schema)
    static final String[] microXeno_localToDev = new String[]{
        "jdbc:postgresql://localhost:5432/microxenodb",
        "mwkunkel",
        "donkie11",
        "jdbc:postgresql://ncidb-d115-d.nci.nih.gov:5473/microxeno",
        "microxeno",
        "M1cr0x0293025en0"
    };

    // microXeno table names and where clauses
    static final String[] microXeno_tableNamesAndWhereClauses = new String[]{
        "affymetrix_identifier", "",
        "tumor", "",
        "tumor_type", "",
        "flat_data", ""
    };

    // chembl postgres to oracle
    static final String[] chembl_pgToOra = new String[]{
        "jdbc:postgresql://localhost:5432/chembl_20",
        "mwkunkel",
        "donkie11",
        "jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov",
        "ops$kunkel",
        "donkie"
    };

    static final String[] chemblTablesAndWhereClauses = new String[]{
        "my_chembl", "",
        "cmpd_table", ""
    };

    // datasystem postgres to oracle
    static final String[] datasystem_pgToOra = new String[]{
        "jdbc:postgresql://localhost:5432/datasystemdb",
        "mwkunkel",
        "donkie11",
        "jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov",
        //"DIS_CLEANED",
        //"R6pq4$6V8d"
        "ops$kunkel",
        "donkie"
    };

    static final String[] pgToOraWhereClauses = new String[]{
        "kek_tbl", ""
    };

    // datasystem to oncologydrugs
    static final String[] datasystemToOncologyDrugs = new String[]{
        "jdbc:postgresql://localhost:5432/datasystemdb",
        "mwkunkel",
        "donkie11",
        "jdbc:postgresql://localhost:5432/oncologydrugsdb",
        "mwkunkel",
        "donkie11"
//        "jdbc:postgresql://ncidb-d115-d:5474/oncology",
//        "oncology",
//        "OnC0L029302802K1t"
    };

    // compare to oncologydrugs
    static final String[] compareToOncologyDrugs = new String[]{
        "jdbc:postgresql://localhost:5432/privatecomparedb",
        "mwkunkel",
        "donkie11",
        "jdbc:postgresql://localhost:5432/oncologydrugsdb",
        "mwkunkel",
        "donkie11"
//        "jdbc:postgresql://ncidb-d115-d:5474/oncology",
//        "oncology",
//        "OnC0L029302802K1t"
    };

    // private compare to public compare
    static final String[] comparePrivateToPublic = new String[]{
        "jdbc:postgresql://localhost:5432/privatecomparedb",
        "mwkunkel",
        "donkie11",
        "jdbc:postgresql://ncidb-d115-d:5473/pubcompare",
        "publiccompare",
        "P3b092094wlC0m3"
//        "jdbc:postgresql://localhost:5432/publiccomparedb",
//        "mwkunkel",
//        "donkie11"
    };

    static final String[] datasystemTableNamesAndWhereClauses = new String[]{
        "ad_hoc_cmpd", "",
        "ad_hoc_cmpd_fragment", "",
        "ad_hoc_cmpd_fragment_p_chem", "",
        "ad_hoc_cmpd_fragment_structure", "",
        "cmpd_known_salt", "",
        "nsc_cmpd_type", "",
        "cmpd_alias_type", "",
        "cmpd_relation_type", "",
        "cmpd_fragment_type", "",
        "cmpd_inventory", " where id in (select nsc from nsc_for_public_release)",
        "cmpd_annotation", " where id in (select nsc from nsc_for_public_release)",
        "cmpd_bio_assay", " where id in (select nsc from nsc_for_public_release)",
        "cmpd_legacy_cmpd", " where id in (select nsc from nsc_for_public_release)",
        "cmpd_table", " where nsc in (select nsc from nsc_for_public_release)",
        "cmpd", " where id in (select nsc from nsc_for_public_release)",
        "nsc_cmpd", " where nsc in (select nsc from nsc_for_public_release)",
        "cmpd_fragment", " where nsc_cmpd_fk in (select nsc from nsc_for_public_release)",
        "cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_public_release))",
        "cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_public_release))",
        "cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_public_release))",
        "cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_public_release)",
        // "cmpd_list", "NO NO NO",
        // "cmpd_list_member", "NO NO NO",
        "cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_public_release))",
        "cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_public_release)",
        "cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_public_release))",
        "cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_public_release)",
        "cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_public_release))",
        "cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_public_release)",
        "cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_public_release))",
        "cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_public_release)",
        "cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_public_release))",
        "cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_public_release)",
        "cmpd_related", " where nsc_cmpd_fk in (select nsc from nsc_for_public_release)",
        "rdkit_mol", " where nsc in (select nsc from nsc_for_public_release)"
    };

    static final String[] compareTableNamesAndWhereClauses = new String[]{
        //"affymetrix_ident", "NO NO NO",
//        "build_date", "",
//        "cell_line_data_set", " where id in (select id from dtp_cell_line_data_set where id in (select id from nsc_ident where nsc in (select nsc from nsc_for_public_release)))",
//        "cell_line_data_set_ident", " where id in (select id from nsc_ident where nsc in (select nsc from nsc_for_public_release))",
//        "cell_line_data_sets2named_targ", " where cell_line_data_sets_fk in (select id from cell_line_data_set_ident where id in (select id from nsc_ident where nsc in (select nsc from nsc_for_public_release)))",
//        "compare_cell_line", "",
//        //"compare_result", "NO NO NO",
//        "conc_resp_assay", " where nsc_compound_fk in (select id from nsc_compound where nsc in (select nsc from nsc_for_public_release))",
//        "conc_resp_element", " where conc_resp_assay_fk in (select id from conc_resp_assay where nsc_compound_fk in (select id from nsc_compound where nsc in (select nsc from nsc_for_public_release)))",
        "dtp_cell_line_data_set", " where id in (select id from cell_line_data_set_ident where id in (select id from nsc_ident where nsc in (select nsc from nsc_for_public_release)))",
        "dtp_test_result", " where id in (select id from nsc_ident where nsc in (select nsc from nsc_for_public_release))",
        //"grid_compare_columns", "NO NO NO",
        //"grid_compare_job", "NO NO NO",
        //"grid_compare_rows", "NO NO NO",
        //"job", "NO NO NO",
        //"mol_targ_ident", "NO NO NO",
        "named_target_set", "",
        //"nano_string_ident", "NO NO NO",
        //"nat_prod_ident", "NO NO NO",
        "nsc_compound", " where nsc in (select nsc from nsc_for_public_release)",
        "nsc_ident", " where nsc in (select nsc from nsc_for_public_release)",
        //"require_use_ignore", "NO NO NO",
        //"standard_compare_job", "NO NO NO",
        "synthetic_ident", "",
        "test_result", " where cell_line_data_set_fk in (select id from nsc_ident where nsc in (select nsc from nsc_for_public_release))",
        "test_result_type", "", //"uploaded_cell_line_data_set", "NO NO NO",
    //"uploaded_ident", "NO NO NO",
    //"uploaded_test_result", "NO NO NO"
    };

    public static void main(String[] args) {

        String[] whichConnectionInfo = comparePrivateToPublic;
        String[] whichTableNamesAndWhereClauses = compareTableNamesAndWhereClauses;

        Connection destConn = null;
        Connection sourceConn = null;

        try {

            DriverManager.registerDriver(new org.postgresql.Driver());

            sourceConn = DriverManager.getConnection(whichConnectionInfo[0], whichConnectionInfo[1], whichConnectionInfo[2]);
            destConn = DriverManager.getConnection(whichConnectionInfo[3], whichConnectionInfo[4], whichConnectionInfo[5]);

            // archive constraint drop/create statements            
            // ConstraintManagement.saveConstraints(destConn, whichTableNamesAndWhereClauses);
            // drop constraints before build
//            ConstraintManagement.dropConstraints(destConn);
            for (int i = 0; i < whichTableNamesAndWhereClauses.length; i += 2) {
                String curTbl = whichTableNamesAndWhereClauses[i];
                String whereClause = whichTableNamesAndWhereClauses[i + 1];
                // scrape out anything remaining
                // Replicator.nuke(destConn, curTbl);
                // replicate the tables
                Replicator.useMetadata(sourceConn, destConn, curTbl, whereClause);
            }

            // recreate the constraints
//            ConstraintManagement.createConstraints(destConn);
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
