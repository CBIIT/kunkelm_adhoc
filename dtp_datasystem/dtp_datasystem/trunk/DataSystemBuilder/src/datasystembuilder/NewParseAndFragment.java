/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import net.sf.jniinchi.INCHI_OPTION;
import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.ALOGPDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondAcceptorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondDonorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.TPSADescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.WeightDescriptor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 *
 * @author mwkunkel
 */
public class NewParseAndFragment {

    int BATCH_FETCH_SIZE = 1000;
    InChIGeneratorFactory INCHIFACTORY = null;
    SmilesGenerator smilesGenerator = null;
    MDLV2000Writer mdlWriter = null;
    MDLV2000Reader mdlReader = null;

    public NewParseAndFragment() {

        try {

            INCHIFACTORY = InChIGeneratorFactory.getInstance();
            smilesGenerator = new SmilesGenerator();
            mdlWriter = new MDLV2000Writer();
            mdlReader = new MDLV2000Reader();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected Molecule parseMolecule(String ctab) {

        Molecule rtn = new Molecule();

        try {
            mdlReader.setReader(new ByteArrayInputStream(ctab.getBytes()));
            mdlReader.read(rtn);
        } catch (Exception e) {

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            String message = w.toString();
            rtn.setProperty("message", message);

        }

        return rtn;

    }

    protected ArrayList<IMolecule> fragmentMolecule(Molecule theMolecule) {

        ArrayList<IMolecule> fragmentList = new ArrayList<IMolecule>();

        // if the molecule is NOT null
        if (theMolecule != null) {

            if (ConnectivityChecker.isConnected(theMolecule)) {
                fragmentList.add(theMolecule);
            } else {
                IMoleculeSet fragmentSet = ConnectivityChecker.partitionIntoMolecules(theMolecule);
                for (int fragCnt = 0; fragCnt < fragmentSet.getMoleculeCount(); fragCnt++) {
                    fragmentList.add(fragmentSet.getMolecule(fragCnt));
                }
            }

        }

        return fragmentList;

    }

    public void processFragments(Connection postgresConn, Connection postgresInsertConn) {

        // TESTOSTERONE
        // String sqlString = "select nsc, mf, strc from prod_src where nsc in (9700,26499,50917,523833,755838,764937,2509410) order by nsc";
        // inchi problems
        // String sqlString = "select nsc, mf, strc from prod_src where nsc in (123127, 207880, 305308, 322082, 328954, 348662, 670072, 670072, 705701, 709767) order by nsc";
        // a set of specific nsc, mix pseudo atoms, metals, 2-fragment, and problems
        // String sqlString = "select nsc, mf, strc from prod_src where nsc in (2965, 3311, 123127, 163027, 165568, 207880, 220178, 240824, 253968, 262648, 305308, 322082, 328954, 348662, 401005, 673737, 691910, 705701, 670072, 688088, 709767) order by nsc";
        //
        // everything, or with LIMIT       
        String sqlString = "select nsc, mf, strc from prod_src order by nsc";

        // 743380 substructure      
        // String sqlString = "select nsc, mf, strc from prod_src where nsc in (742183,707007,742198,186431,83664,198258,778575,93216,778576,198252,625409,83660,186404,83661,83662,83663,752108,198246,186455,186453,722574,722573,186440,722572,765284,147396,707008,740073,372810,372811,2563532,2563531,717550,750016,340392,726891,717543,717540,717539,717537,726892,78330,717556,336005,717555,2551859,717517,717516,717511,717510,2525067,717530,629428,323539,719411,719410,719409,719415,295314,719413,719412,2557151,742378,347887,742377,717476,717496,751880,198287,686899,719406,719407,719404,719405,93409,93410,728986,671683,678240,765401,763058,719853,719852,54776,143237,704299,54771,54773,54772,54775,54774,132462,751827,778342,745855,778340,611906,632912,719857,719858,719823,764940,728651,719812,749686,719811,749684,741908,749685,728652,741909,719838,674197,719839,689366,689365,772795,674198,719834,751863,106602,719831,621717,719829,751864,671240,665562,695563,695556,695557,695558,719803,749573,749572,759685,682914,759686,682915,759687,759692,724799,759693,759694,759688,759689,724794,759690,722849,759691,717568,95434,375246,722761,734209,749761,180284,17789,2557352,2524806,734251,295160,734255,734254,2557372,665387,734253,734252,268342,268343,775148,736463,775146,665431,775147,656692,775144,268336,775145,268337,268344,772703,268335,772702,268334,14861,757413,528463,745859,745858,745857,749759,744002,744030,2540059,744029,660540,767671,767670,767669,767668,286472,315180,286471,286470,362969,286469,2559656,767672,746056,682214,746057,746058,746052,85072,85073,746053,85074,2552473,746055,85075,746051,750414,2515598,375463,194673,309401,194658,757061,743888,737402,234512,716807,737405,234519,737407,737406,657028,234521,234520,657026,657027,701643,760837,234507,671069,671068,671067,234508,234509,620388,620398,380282,319073,755093,755094,750364,755095,755096,755097,625006,755098,748108,748109,743705,274874,757232,748106,743706,2557616,772415,772414,772413,741774,772412,772411,2557625,757231,757230,672415,741796,146731,2579454,741797,741795,652988,2557580,730668,366674,2557682,2550767,676861,243435,746162,746163,746160,746161,746166,746167,746164,746165,746168,746169,772416,772417,2557635,746151,772422,772423,772424,746155,772425,772426,746152,772427,746159,772429,746158,2557645,746157,746156,604747,756845,756846,756847,763536,763537,604750,668801,659279,756861,756860,756863,756862,756857,2526348,756856,756859,756858,74618,756853,763533,763532,756852,763535,756855,763534,756854,74617,756849,763529,756848,763528,763531,756851,756850,763530,761212,2557708,761213,761210,2524199,761211,761208,2528533,761209,761206,761207,761204,2552760,761205,761202,761203,761200,761201,761199,761198,737564,761197,761196,761195,78355,78352,140661,743626,78366,752297,2552793,750134,140659,750133,78350,78351,2526404,359957,78349,2524257,245979,245716,736849,736850,736852,2552802,359989,359991,2552810,245820,245819,309539,734758,245806,763455,320612,763454,320613,633551,761339,761338,726116,756947,736916,750287,756954,736915,629124,756952,756953,694087,2552666,609542,653269,653268,609543,653271,653270,653267,2543914,763456,763457,653278,653273,653272,653274,756867,756866,756865,756864,774633,756871,756870,756869,746407,746406,756868,756873,756872,668785,245573,286442,286443,286440,734794,286441,774647,286446,286447,774646,286444,774645,286445,774644,164157,733646,733647,142016,754578,668626,668627,234013,234012,754603,754602,754601,754600,754599,754598,754597,760333,2530017,651334,651333,670551,324283,525552,725984,2526121,745384,679316,679314,679315,685911,748745,748744,723561,723562,758489,685909,723563,723564,685907,723565,723566,723567,775698,685917,685916,685914,748742,613348,723569,2547311,723568,2547309,685900,154933,723570,2547307,2547305,2547304,748582,740935,628313,748583,748581,339011,339010,740938,740936,740937,766018,716585,164240,764382,721471,716590,721463,241894,234117,743380,234125,234122,718565,716619,748644,720498,720497,720496,720494,371972,720495,720490,720488,765960,694470,634316,720487,720484,694469,765963,634319,720485,634318,721473,765962,2543601,765964,2562325,2562324,289491,751078,751079,624412,624411,624410,731292,2552856,698966,698967,698969,773835,764019,764016,721820,322087,776028,776029,776030,776025,776026,776027,350842,209000,58073,750672,339344,741264,741265,2533571,750669,750671,716491,339339,2579529,727617,727616,2558378,773817,2558371,2523874,2533400,773706,729440,2543274,714393,714394,714397,714396,687639,750769,723896,764124,729426,754259,754258,754257,754263,754262,754261,748919,754260,748921,754265,754264,690419,752725,752726,752727,731558,374198,752728,374199,374196,374197,374194,374195,731564,2530064,374192,374193,720718,720716,773677,773679,44639,44637,750826,750827,750830,750831,720725,741166,720720,753597,764759,753596,722991,749184,286850,127708,764761,764760,370551,527968,734197,757900,734201,722992,734200,648062,756011,734203,753571,734202,722994,753572,734204,740555,346228,346229,722975,619386,679892,77541,753654,2564166,2564165,198905,66575,66574,740528,740529,740530,376490,608487,608486,751438,2551571,751442,751443,751440,751441,645329,286965,753465,753466,753467,753468,753469,719881,367665,339579,643236,694945,719872,729723,719934,719929,136091,136090,719928,747189,612619,118795,118788,118791,118785,2523014,747227,749132,766522,749130,749131,757830,2544758,773419,719936,720996,608341,377111,367685,720986,696602,367688,2562878,749175,749174,749173,749172,749182,749181,749180,749179,756161,749178,749177,2507893,749176,621249,742490,654292,742488,742489,370296,654291,740852,654290,740853,740842,740840,749442,749441,740838,157078,732025,732026,2529730,747304,368022,2549445,2549444,143465,143478,740803,740802,740805,735829,628186,740804,740807,735830,740806,773285,773284,625666,610420,17389,720338,720339,720337,720343,17384,720340,17383,720344,17382,753396,760180,357115,760181,643398,2564463,405611,405610,2564462,17365,42089,749549,751172,749548,79465,2564464,2549396,749328,762453,751294,744518,751293,762455,762454,744516,762457,762456,762458,762436,762437,610474,2522826,749326,749327,2522824,747412,2522825,720174,720175,747437,720181,720180,720179,762389,762388,199026,740657,199025,762372,710563,762373,710562,762374,2549299,710561,762375,199008,732032,762371,710571,762380,710570,199019,762376,605911,762377,605910,605909,762378,199016,605908,762379,744492,199001,744500,673707,2549271,673708,151476,727292,675641,2549272) order by nsc";


        // FAILURES in APPROVEDONCOLOGY
        // String sqlString = "select nsc, mf, strc from prod_src where nsc in (744009, 757043, 755402, 16895, 757296, 157387, 757105, 33832)";
        Statement postgresStmt = null;
        PreparedStatement postgresPrepStmt = null;
        ResultSet rs = null;

        ArrayList<String> warningList = new ArrayList<String>();

        PreparedStatement molCanNotParsePrepStmt = null;
        PreparedStatement molFragPrepStmt = null;
        PreparedStatement molNoFragPrepStmt = null;
        PreparedStatement molFragRgroupPrepStmt = null;
        PreparedStatement pseudoAtomPrepStmt = null;
        PreparedStatement nullAtomTypePrepStmt = null;

        int nsc = 0;
        String mf = "";
        String strc = "";

        try {

            postgresStmt = postgresConn.createStatement();
            postgresStmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            postgresStmt.setFetchSize(BATCH_FETCH_SIZE);
            String[] sqlArray = new String[]{
                "drop table if exists mol_cannot_parse",
                "create table mol_cannot_parse(nsc int, mf varchar(1024), message varchar(1024))",
                "drop table if exists mol_frag",
                "create table mol_frag(nsc int, frag_id int, count_atoms int, count_pseudo_atoms int, count_bonds int, ctab text, smiles text, inchi text, inchi_aux text, alogp double precision, hba int, hbd int, psa double precIsion, mf varchar(1024), mw double precision, processing_messages varchar(1024))",
                "drop table if exists mol_no_frag",
                "create table mol_no_frag(nsc int, strc_id int, mf varchar(1024))",
                "drop table if exists mol_frag_r_group",
                "create table mol_frag_r_group(nsc int, frag_id int, mf varchar(1024))",
                "drop table if exists mol_frag_pseudo_atom",
                "create table mol_frag_pseudo_atom(nsc int, frag_id int, mf varchar(1024), atom_label varchar(1024))",
                "drop table if exists mol_frag_null_atom_type",
                "create table mol_frag_null_atom_type(nsc int, frag_id int, mf varchar(1024), atom_label varchar(1024))"
            };

            for (int i = 0; i < sqlArray.length; i++) {
                String sqlStr = sqlArray[i];
                System.out.println(sqlStr);
                postgresStmt.execute(sqlStr);
                postgresConn.commit();
            }

            String molCanNotParseStmtString = "insert into  mol_cannot_parse(nsc, mf, message) values (?,?,?)";
            molCanNotParsePrepStmt = postgresInsertConn.prepareStatement(molCanNotParseStmtString);

            String molFragStmtString = "insert into mol_frag(nsc, frag_id, count_atoms, count_pseudo_atoms, count_bonds, ctab, smiles, inchi, inchi_aux, alogp, hba, hbd, psa , mf , mw, processing_messages)  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            molFragPrepStmt = postgresInsertConn.prepareStatement(molFragStmtString);

            String molNoFragStmtString = "insert into mol_no_frag(nsc, mf) values(?,?)";
            molNoFragPrepStmt = postgresInsertConn.prepareStatement(molNoFragStmtString);

            String molFragRgroupStmtString = "insert into mol_frag_r_group(nsc, frag_id, mf)  values(?,?,?)";
            molFragRgroupPrepStmt = postgresInsertConn.prepareStatement(molFragRgroupStmtString);

            String pseudoAtomStmtString = "insert into mol_frag_pseudo_atom(nsc, frag_id, mf, atom_label)  values(?,?,?,?)";
            pseudoAtomPrepStmt = postgresInsertConn.prepareStatement(pseudoAtomStmtString);

            String nullAtomTypeStmtString = "insert into mol_frag_null_atom_type(nsc, frag_id, mf, atom_label)  values(?,?,?,?)";
            nullAtomTypePrepStmt = postgresInsertConn.prepareStatement(nullAtomTypeStmtString);

            int molCnt = 0;
            int fragmentId = 0;

            rs = postgresStmt.executeQuery(sqlString);

            rs.setFetchDirection(ResultSet.FETCH_FORWARD);
            rs.setFetchSize(BATCH_FETCH_SIZE);

            while (rs.next()) {

                nsc = rs.getInt("nsc");
                strc = rs.getString("strc");
                mf = rs.getString("mf");

                molCnt++;

                System.out.println("molCnt: " + molCnt + " nsc: " + nsc + " mf: " + mf);

                Molecule theMolecule = parseMolecule(strc);

                ArrayList<IMolecule> fragmentList = new ArrayList<IMolecule>();

                // failed molecules return with the "message" property set
                if (theMolecule.getProperty("message") != null) {

                    // HAVE to increment the fragmentId HERE!
                    fragmentId++;

                    String msg = theMolecule.getProperty("message").toString();

                    molCanNotParsePrepStmt.setInt(1, nsc);
                    molCanNotParsePrepStmt.setString(2, mf);
                    molCanNotParsePrepStmt.setString(3, msg);

                    molCanNotParsePrepStmt.execute();
                    postgresInsertConn.commit();

                    // write a place-holder to mol_frag to simplify tracking nsc later...
                    String errString = "ERR cannot parse strc " + msg;

                    molFragPrepStmt.setInt(1, nsc);
                    molFragPrepStmt.setInt(2, fragmentId);
                    // count_atoms
                    molFragPrepStmt.setInt(3, 0);
                    // count_pseudo_atoms
                    molFragPrepStmt.setInt(4, 0);
                    // count_bonds
                    molFragPrepStmt.setInt(5, 0);
                    //ctab
                    molFragPrepStmt.setString(6, strc);
                    // smiles
                    molFragPrepStmt.setString(7, null);
                    // inchi
                    molFragPrepStmt.setString(8, null);
                    molFragPrepStmt.setString(9, null);
                    molFragPrepStmt.setDouble(10, 0);
                    molFragPrepStmt.setInt(11, 0);
                    molFragPrepStmt.setInt(12, 0);
                    molFragPrepStmt.setDouble(13, 0);
                    // mf
                    molFragPrepStmt.setString(14, null);
                    molFragPrepStmt.setDouble(15, 0);
                    // processing_messages
                    molFragPrepStmt.setString(16, errString);
                    molFragPrepStmt.execute();
                    postgresInsertConn.commit();

                } else {

                    fragmentList = fragmentMolecule(theMolecule);

                    if (fragmentList.isEmpty()) {

                        molNoFragPrepStmt.setInt(1, nsc);
                        molNoFragPrepStmt.setString(2, mf);
                        molNoFragPrepStmt.execute();
                        postgresInsertConn.commit();

                    } else {

                        int hErrorCnt = 0;

                        for (IMolecule im : fragmentList) {

                            fragmentId++;

                            // use MoleculeStats
                            MoleculeStats molStats = new MoleculeStats(im, nsc);

                            // check (and remove) pseudo atoms, check for null atom types, check for R groups
                            // pseudo atoms
                            if (molStats.countPseudoAtoms > 0) {
                                for (String atomSymbol : molStats.pseudoAtomsList) {
                                    // track pseudoAtoms
                                    pseudoAtomPrepStmt.setInt(1, nsc);
                                    pseudoAtomPrepStmt.setInt(2, fragmentId);
                                    pseudoAtomPrepStmt.setString(3, mf);
                                    pseudoAtomPrepStmt.setString(4, atomSymbol);
                                    pseudoAtomPrepStmt.execute();
                                    postgresInsertConn.commit();
                                }
                            }

                            if (molStats.countNullAtoms > 0) {
                                for (String atomSymbol : molStats.nullAtomsList) {
                                    // track null atoms
                                    nullAtomTypePrepStmt.setInt(1, nsc);
                                    nullAtomTypePrepStmt.setInt(2, fragmentId);
                                    nullAtomTypePrepStmt.setString(3, mf);
                                    nullAtomTypePrepStmt.setString(4, atomSymbol);
                                    nullAtomTypePrepStmt.execute();
                                    postgresInsertConn.commit();
                                }
                            }

                            if (molStats.countRgroups > 0) {
                                molFragRgroupPrepStmt.setInt(1, nsc);
                                molFragRgroupPrepStmt.setInt(2, fragmentId);
                                molFragRgroupPrepStmt.setString(3, mf);
                                molFragRgroupPrepStmt.execute();
                                postgresInsertConn.commit();
                            }

                            // if there were any pseudoatoms, SWAP for the molecule with those removed
                            if (molStats.countPseudoAtoms > 0) {
                                im = molStats.moleculeWithoutPseudoAtoms;
                            }

                            // EVERY FRAG should have an entry in mol_frag
                            if (molStats.countNullAtoms > 0 || molStats.countRgroups > 0 || molStats.countAtoms == 0) {

                                StringBuilder sb = new StringBuilder();

                                if (molStats.countPseudoAtoms > 0) {
                                    sb.append("ERR pseudo-atom(s) -->" + makeStringFromList(molStats.pseudoAtomsList) + "<-- ");
                                }

                                if (molStats.countNullAtoms > 0) {
                                    sb.append("ERR null atom type(s) -->" + makeStringFromList(molStats.nullAtomsList) + "<-- ");
                                }

                                if (molStats.countRgroups > 0) {
                                    sb.append("ERR r group(s) -->" + makeStringFromList(molStats.rGroupsList) + "<-- ");
                                }

                                // THIS has to reference the NEW atom since the stats were calculated BEFORE removing pseudoAtoms
                                if (im.getAtomCount() == 0) {
                                    sb.append("ERR no real atoms ");
                                }

                                molFragPrepStmt.setInt(1, nsc);
                                molFragPrepStmt.setInt(2, fragmentId);
                                molFragPrepStmt.setInt(3, molStats.countAtoms);
                                molFragPrepStmt.setInt(4, molStats.countPseudoAtoms);
                                molFragPrepStmt.setInt(5, molStats.countBonds);
                                molFragPrepStmt.setString(6, null);
                                molFragPrepStmt.setString(7, null);
                                molFragPrepStmt.setString(8, null);
                                molFragPrepStmt.setString(9, null);
                                molFragPrepStmt.setDouble(10, 0);
                                molFragPrepStmt.setInt(11, 0);
                                molFragPrepStmt.setInt(12, 0);
                                molFragPrepStmt.setDouble(13, 0);
                                molFragPrepStmt.setString(14, null);
                                molFragPrepStmt.setDouble(15, 0);
                                molFragPrepStmt.setString(16, sb.toString());

                                molFragPrepStmt.execute();
                                postgresInsertConn.commit();

                            } else {

//                try {
//                  FixBondOrdersTool fbt = new FixBondOrdersTool();
//                  IMolecule temp = fbt.kekuliseAromaticRings(im);
//                  im = temp;
//                } catch (Exception e) {
//                  e.printStackTrace();
//                  warningList.add(e.toString());
//                }
                                try {
                                    CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(im.getBuilder());
                                    Iterator<IAtom> atoms = im.atoms().iterator();
                                    while (atoms.hasNext()) {
                                        IAtom atom = atoms.next();
                                        IAtomType type = matcher.findMatchingAtomType(im, atom);
                                        AtomTypeManipulator.configure(atom, type);
                                    }
                                    CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(im.getBuilder());
                                    hAdder.addImplicitHydrogens(im);

                                    // MWK 11Jan2014, tried keeping H as implicit so that they don't write to the ctab
                                    // but this caused problems with smiles
                                    // AtomContainerManipulator.convertImplicitToExplicitHydrogens(im);

                                } catch (Exception e) {
                                    hErrorCnt++;
                                    warningList.add(e.toString());
                                }

                                //pass this off to doCalcs()
                                CalcContainer calcRtn = doCalcs(im);
                                StructureContainer strucRtn = doStructureStrings(im);

                                //                               
                                molFragPrepStmt.setInt(1, nsc);
                                molFragPrepStmt.setInt(2, fragmentId);
                                molFragPrepStmt.setInt(3, molStats.countAtoms);
                                molFragPrepStmt.setInt(4, molStats.countPseudoAtoms);
                                molFragPrepStmt.setInt(5, molStats.countBonds);
                                molFragPrepStmt.setString(6, strucRtn.ctab);
                                molFragPrepStmt.setString(7, strucRtn.smiles);
                                molFragPrepStmt.setString(8, strucRtn.inchi);
                                molFragPrepStmt.setString(9, strucRtn.inchiAuxInfo);
                                molFragPrepStmt.setDouble(10, calcRtn.alogp);
                                molFragPrepStmt.setInt(11, calcRtn.hba);
                                molFragPrepStmt.setInt(12, calcRtn.hbd);
                                molFragPrepStmt.setDouble(13, calcRtn.psa);
                                molFragPrepStmt.setString(14, calcRtn.mf);
                                molFragPrepStmt.setDouble(15, calcRtn.mw);
                                molFragPrepStmt.setString(16, null);
                                molFragPrepStmt.execute();
                                postgresInsertConn.commit();

                            } // if pseudo atoms, null atoms or r groups

                        } // for each fragment 
                    } // if fragList is not empty
                } // if theMolecule is not null
            } // while (rs.next())

        } catch (Exception e) {

            System.out.println("Caught outer exception while processing nsc: " + nsc + " " + e);
            e.printStackTrace();

        } finally {

            if (postgresStmt != null) {
                try {
                    postgresStmt.close();
                    postgresStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing postgresStmt");
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing rs");
                }
            }

        }
    }

    /**
     *
     * @param im
     * @return
     * @throws Exception
     */
    private CalcContainer doCalcs(IMolecule im) {

        CalcContainer rtn = new CalcContainer();
        DescriptorValue result = null;
        String paramName = null;
        String parsedResult = null;

        try {

            ALOGPDescriptor the_ALOGPDescriptor = new ALOGPDescriptor();
            result = the_ALOGPDescriptor.calculate(im);
            paramName = "ALogP";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.alogp = Double.parseDouble(parsedResult);

            HBondAcceptorCountDescriptor the_HBondAcceptorCountDescriptor = new HBondAcceptorCountDescriptor();
            result = the_HBondAcceptorCountDescriptor.calculate(im);
            paramName = "nHBAcc";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.hba = Integer.parseInt(parsedResult);

            HBondDonorCountDescriptor the_HBondDonorCountDescriptor = new HBondDonorCountDescriptor();
            result = the_HBondDonorCountDescriptor.calculate(im);
            paramName = "nHBDon";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.hbd = Integer.parseInt(parsedResult);

            TPSADescriptor the_TPSADescriptor = new TPSADescriptor();
            result = the_TPSADescriptor.calculate(im);
            paramName = "TopoPSA";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.psa = Double.parseDouble(parsedResult);

            int calcAtomCount = im.getAtomCount();
//build up the molecular formula
            IMolecularFormula formula = im.getBuilder().newInstance(IMolecularFormula.class);
            MolecularFormulaManipulator.getMolecularFormula(im, formula);
            rtn.mf = MolecularFormulaManipulator.getString(formula).trim();
            WeightDescriptor the_WeightDescriptor = new WeightDescriptor();
            result = the_WeightDescriptor.calculate(im);
            paramName = "MW";
            parsedResult = parseDescriptorValue(result, paramName);
            rtn.mw += Double.valueOf(parsedResult);
//System.out.println("Value of alogp is:" + rtn.alogp);
//System.out.println("Value of hba is:" + rtn.hba);
//System.out.println("Value of hbd is:" + rtn.hbd);
//System.out.println("Value of psa is:" + rtn.psa);
//System.out.println("Value of mf is:" + rtn.mf);
//System.out.println("Value of mw is:" + rtn.mw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }

    /**
     *
     * @param dv
     * @param paramName
     * @return
     */
    private String parseDescriptorValue(DescriptorValue dv, String paramName) {
        int paramIndex = -1;
        String[] names = dv.getNames();
        for (int i = 0; i
                < names.length; i++) {
//System.out.print(names[i] + ", ");
            if (names[i].equals(paramName)) {
                paramIndex = i;
            }
        }
        String resultString = dv.getValue().toString();
        String[] splitString = resultString.split(",");
        if (paramIndex > -1) {
            return splitString[paramIndex];
        } else {
            System.out.println("Could not extract: " + paramName + " in parseDescriptorValue in CompoundRegistrationControllerImpl");
            return "notFound";

        }
    }

    /**
     * @return @throws Exception
     */
    protected StructureContainer doStructureStrings(IMolecule im) {

        StructureContainer rtn = new StructureContainer();

        try {

            rtn.smiles = smilesGenerator.createSMILES(im);

        } catch (Exception e) {

            e.printStackTrace();
            im.toString();

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            rtn.smiles = w.toString();

        }

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mdlWriter.setWriter(baos);
            mdlWriter.writeMolecule(im);
            rtn.ctab = baos.toString();

        } catch (Exception e) {

            e.printStackTrace();

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            rtn.ctab = w.toString();

        }

        try {

            ArrayList<INCHI_OPTION> optList = new ArrayList<INCHI_OPTION>();
            optList.add(INCHI_OPTION.SNon);
            
            InChIGenerator gen = INCHIFACTORY.getInChIGenerator(im, optList);
                        
            INCHI_RET ret = gen.getReturnStatus();

            if (ret == INCHI_RET.WARNING) {
                rtn.inchi = gen.getInchi();
                rtn.inchiAuxInfo = gen.getAuxInfo() + " InChI warning: " + gen.getMessage();
            } else if (ret != INCHI_RET.OKAY) {
                rtn.inchi = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
                rtn.inchiAuxInfo = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
            } else {
                rtn.inchi = gen.getInchi();
                rtn.inchiAuxInfo = gen.getAuxInfo();
            }
            
        } catch (Exception e) {
            e.printStackTrace();

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            rtn.inchi = "InChI failed with exception: " + w.toString();
            rtn.inchiAuxInfo = "InChI failed with exception: " + w.toString();

        }

        // System.out.println("Value of ctab is:" + rtn.ctab);
        // System.out.println("Value of smiles is:" + rtn.smiles);
        // System.out.println("Value of inchi is:" + rtn.inchi);
        // System.out.println("Value of inchiAuxInfo is:" + rtn.inchiAuxInfo);
        return rtn;
    }

    public String makeStringFromList(ArrayList<String> incomingList) {

        Collections.sort(incomingList);

        StringBuilder sb = new StringBuilder();
        for (String s : incomingList) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();

    }

    protected class MoleculeStats {

        public IMolecule moleculeWithoutPseudoAtoms;
        public int countBonds;
        public int countAtoms;
        public int countPseudoAtoms;
        public int countNullAtoms;
        public int countRgroups;
        public ArrayList<String> atomsList;
        public ArrayList<String> pseudoAtomsList;
        public ArrayList<String> nullAtomsList;
        public ArrayList<String> rGroupsList;

        public MoleculeStats(IMolecule incomingMolecule, int nsc) {

            this.countAtoms = incomingMolecule.getAtomCount();
            this.countBonds = incomingMolecule.getBondCount();
            this.countPseudoAtoms = 0;
            this.countNullAtoms = 0;
            this.countRgroups = 0;
            this.atomsList = new ArrayList<String>();
            this.pseudoAtomsList = new ArrayList<String>();
            this.nullAtomsList = new ArrayList<String>();
            this.rGroupsList = new ArrayList<String>();

            try {

                if (incomingMolecule == null) {
                } else {

                    this.moleculeWithoutPseudoAtoms = incomingMolecule;

                    CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(incomingMolecule.getBuilder());

                    Iterator<IAtom> atoms = incomingMolecule.atoms().iterator();

                    while (atoms.hasNext()) {

                        IAtom atom = atoms.next();

                        IAtomType type = null;
                        try {
                            type = matcher.findMatchingAtomType(incomingMolecule, atom);
                        } catch (Exception e) {
                            System.out.println("Processing: " + nsc + " caught Exception in match.findMatchingAtomType: " + atom.getSymbol());
                            //e.printStackTrace();
                        }

                        if (type == null) {
                            this.countNullAtoms++;
                            this.nullAtomsList.add(atom.getSymbol());
                        }

                        if (atom instanceof PseudoAtom) {
                            this.countPseudoAtoms++;
                            PseudoAtom pa = (PseudoAtom) atom;
                            this.pseudoAtomsList.add(pa.getLabel());
                            this.moleculeWithoutPseudoAtoms.removeAtom(atom);
                        }

                        if (atom.getSymbol().equals("R")) {
                            this.countRgroups++;
                            this.rGroupsList.add(atom.getSymbol());
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("nsc is: " + nsc);
                e.printStackTrace();
            }

        }
    }
}
