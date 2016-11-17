/*
 
 
 
 */
package mwk.datasystem.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mwkunkel
 */
public class CheckPLPFiles {

    private static final Boolean DEBUG = Boolean.TRUE;
    private static final Boolean TESTING = Boolean.FALSE;
    private static final int BATCH_FETCH_SIZE = 10000;
    private static final int BATCH_INSERT_SIZE = 10000;

    public static class Col {

        String fileHead;
        String colName;
        String sqlType;

        public Col(String fileHead, String colName, String sqlType) {
            this.fileHead = fileHead;
            this.colName = colName;
            this.sqlType = sqlType;
        }

    }

    public static class Tbl {

        String fileName;
        String delimiter;
        String tableName;

        public Tbl(String fileName, String delimiter, String tableName) {
            this.fileName = fileName;
            this.delimiter = delimiter;
            this.tableName = tableName;
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

//        String[] tblArr = new String[]{
//            "/home/mwkunkel/rs3_from_plp_frags.csv", ",", "rs3_from_plp_frags",
//            "/home/mwkunkel/rs3_from_plp_nsc.csv", ",", "rs3_from_plp_nsc",
//            "/home/mwkunkel/rs3_from_plp_frags_ctab.tsv", "\t", "rs3_from_plp_frags_ctab",
//            "/home/mwkunkel/rs3_from_plp_problems.csv", ",", "rs3_from_plp_problems"
//        };
        String[] tblArr = new String[]{
            "/home/mwkunkel/rs3_from_plp_first_round_tautomer_failures.csv", ",", "rs3_from_plp_first_round_tautomer_failures",
            "/home/mwkunkel/rs3_from_plp_frags.csv", ",", "rs3_from_plp_frags",
            "/home/mwkunkel/rs3_from_plp_nsc.csv", ",", "rs3_from_plp_nsc",
            "/home/mwkunkel/rs3_from_plp_strc_parse_fail.tsv", "\t", "rs3_from_plp_strc_parse_fail",
            "/home/mwkunkel/rs3_from_plp_frags_ctab.tsv", "\t", "rs3_from_plp_frags_ctab",
            "/home/mwkunkel/rs3_from_plp_problems.tsv", "\t", "rs3_from_plp_problems",
            "/home/mwkunkel/rs3_from_plp_validate_structure.tsv", "\t", "rs3_from_plp_validate_structure"
        };

        ArrayList<Tbl> tblList = new ArrayList<Tbl>();

        for (int i = 0; i < tblArr.length; i += 3) {
            tblList.add(new Tbl(tblArr[i], tblArr[i + 1], tblArr[i + 2]));
        }

        String[] colArr = new String[]{
            "NormalizationActions", "NormalizationActions", "varchar",
            "ErrorText", "ErrorText", "varchar",
            "ErrorDetails", "ErrorDetails", "varchar",
            "Attribute", "Attribute", "varchar",
            "SourceTag", "SourceTag", "varchar",
            "NSC", "NSC", "int",
            "PROD_MF", "PROD_MF", "varchar",
            "PROD_MW", "PROD_MW", "double precision",
            "CAS", "CAS", "varchar",
            "Name", "Name", "varchar",
            "Comment", "Comment", "varchar",
            "Salts", "Salts", "varchar",
            "SaltSmiles", "SaltSmiles", "varchar",
            "SaltAtoms", "SaltAtoms", "varchar",
            "fragmentIndex", "fragmentIndex", "int",
            "labelArray", "labelArray", "varchar",
            "cleanresult", "cleanresult", "varchar",
            "smiles", "smiles", "varchar",
            "can_smi", "can_smi", "varchar",
            "TautomerIndex", "TautomerIndex", "int",
            "NumberOfTautomers", "NumberOfTautomers", "int",
            "can_taut", "can_taut", "varchar",
            "InChI", "InChI", "varchar",
            "InChI_AuxInfo", "InChI_AuxInfo", "text",
            "mf", "mf", "varchar",
            "mw", "mw", "double precision",
            "Num_Atoms", "Num_Atoms", "int",
            "Num_Bonds", "Num_Bonds", "int",
            "Num_Hydrogens", "Num_Hydrogens", "int",
            "Num_PositiveAtoms", "Num_PositiveAtoms", "int",
            "Num_NegativeAtoms", "Num_NegativeAtoms", "int",
            "Num_RingBonds", "Num_RingBonds", "int",
            "Num_RotatableBonds", "Num_RotatableBonds", "int",
            "Num_AromaticBonds", "Num_AromaticBonds", "int",
            "Num_Rings", "Num_Rings", "int",
            "Num_AromaticRings", "Num_AromaticRings", "int",
            "Num_RingAssemblies", "Num_RingAssemblies", "int",
            "Num_MetalAtoms", "Num_MetalAtoms", "int",
            "Num_StereoAtoms", "Num_StereoAtoms", "int",
            "Num_StereoBonds", "Num_StereoBonds", "int",
            "Num_SingleBonds", "Num_SingleBonds", "int",
            "Num_DoubleBonds", "Num_DoubleBonds", "int",
            "Num_TripleBonds", "Num_TripleBonds", "int",
            "fc", "fc", "int",
            "CoordDimension", "CoordDimension", "int",
            "IsChiral", "IsChiral", "boolean",
            "ALogP", "ALogP", "double precision",
            "LogD", "LogD", "double precision",
            "Molecular_Solubility", "Molecular_Solubility", "double precision",
            "Molecular_SurfaceArea", "Molecular_SurfaceArea", "double precision",
            "Num_H_Acceptors", "Num_H_Acceptors", "int",
            "Num_H_Donors", "Num_H_Donors", "int",
            "Molecular_Formula", "Molecular_Formula", "varchar",
            "Molecular_Weight", "Molecular_Weight", "double precision",
            "FormalCharge", "FormalCharge", "int",
            "mf", "mf", "varchar",
            "mw", "mw", "double precision",
            "fc", "fc", "int",
            "Canonical_Smiles", "Canonical_Smiles", "varchar",
            "InChI_Message", "InChI_Message", "varchar",
            "RegisterCheckErrors", "RegisterCheckErrors", "varchar",
            "TransformationReactions", "TransformationReactions", "varchar",
            "AtomsChangedInReactions", "AtomsChangedInReactions", "varchar",
            "CleanMessage", "CleanMessage", "varchar",
            "Solvents", "Solvents", "varchar",
            "SolventSmiles", "SolventSmiles", "varchar",
            "SolventAtoms", "SolventAtoms", "varchar",
            "Standardize_Actions_Taken", "Standardize_Actions_Taken", "varchar",
            "TooManyTautomers", "TooManyTautomers", "varchar",
            "internal_null_coords", "internal_null_coords", "varchar",
            "CheshireMessage", "CheshireMessage", "varchar",
            "Num_Fragments", "Num_Fragments", "int",
            "Num_RGroupFragments", "Num_RGroupFragments", "int",
            "Molecular_Formula_Fragments", "Molecular_Formula_Fragments", "varchar",
            "CTAB", "CTAB", "varchar",
            "mf_neut", "mf_neut", "varchar",
            "mw_neut", "mw_neut", "double precision",
            "fc_neut", "fc_neut", "int",
            "can_smi_neut", "can_smi_neut", "varchar",
            "ORIGINAL_MOL", "ORIGINAL_MOL", "bytea",
            "NormalizationErrors", "NormalizationErrors", "varchar",
            "AtomsWithQueryFeatures", "AtomsWithQueryFeatures", "varchar",
            "BumpingAtom1", "BumpingAtom1", "varchar",
            "BumpingAtom2", "BumpingAtom2", "varchar",
            "BumpingDistance", "BumpingDistance", "varchar",
            "BadValenceAtoms", "BadValenceAtoms", "varchar",
            "BadStereoAtoms", "BadStereoAtoms", "varchar",
            "NonLinearAlleneAtoms", "NonLinearAlleneAtoms", "varchar",
            "NonLinearCentralTripleBonds", "NonLinearCentralTripleBonds", "varchar",
            "NonLinearTerminalTripleBonds", "NonLinearTerminalTripleBonds", "varchar",
            "BadIsotopeAtoms", "BadIsotopeAtoms", "varchar",
            "CanonicalTautomer", "CanonicalTautomer", "varchar",
            "ChargeTautomer", "ChargeTautomer", "varchar",
            "FormalChargeChanged", "FormalChargeChanged", "varchar",
            "heavyMetal", "heavyMetal", "boolean"
        };

        ArrayList<Col> colList = new ArrayList<Col>();

        for (int i = 0; i < colArr.length; i += 3) {
            colList.add(new Col(colArr[i], colArr[i + 1], colArr[i + 2]));
        }

        HashMap<String, Col> colMap = new HashMap<String, Col>();
        for (Col c : colList) {
            colMap.put(c.colName.toLowerCase(), c);
        }

        for (Tbl t : tblList) {
            doTbl(t.fileName, t.delimiter, t.tableName, colMap);
        }

        for (Tbl t : tblList) {
            
            StringBuilder sb = new StringBuilder();
            
            
            System.out.println("\\copy " + t.tableName + " from " + t.fileName + " csv header delimiter as '" + t.delimiter + "';");
        }

    }

    static void doTbl(String fileName, String delimiter, String tableName, HashMap<String, Col> colMap) {

        File f = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {

            f = new File(fileName);
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            ArrayList<Col> foundColList = new ArrayList<Col>();

            String firstLine = br.readLine();
            String[] splitLine = firstLine.split(delimiter);

            for (String fileHead : splitLine) {
                if (!colMap.containsKey(fileHead.toLowerCase())) {
                    System.out.println();
                    System.out.println("-----------------tbl: " + tableName + " missing col definition for fileHeader: " + fileHead);
                    System.out.println();
                    foundColList.add(new Col(fileHead, fileHead, "UNK_UNK_UNK"));
                } else {
                    foundColList.add(colMap.get(fileHead.toLowerCase()));
                }
            }

//            for (String colName : colMap.keySet()) {
//                boolean found = false;
//                for (String fileHead : splitLine) {
//                    if (fileHead.toLowerCase().equals(colName)) {
//                        found = true;
//                        break;
//                    }
//                }
//                if (!found) {
//                    System.out.println("Missing file header for: " + colName);
//                }
//            }
            StringBuilder sb = new StringBuilder();

            sb.append("\n").append("drop table if exists ").append(tableName).append(";").append("\n");

            sb.append("\n").append("create table ").append(tableName).append("(").append("\n");
            for (Col c : foundColList) {

                if (foundColList.indexOf(c) == foundColList.size() - 1) {
                    sb.append(c.colName).append(" ").append(c.sqlType).append("\n");
                } else {
                    sb.append(c.colName).append(" ").append(c.sqlType).append(",").append("\n");
                }

            }
            sb.append(");");

            System.out.println(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
