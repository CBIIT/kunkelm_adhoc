/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mwk.datasystem.controllers.SessionController;
import mwk.datasystem.controllers.StructureSearchController;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpdImpl;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import mwk.datasystem.vo.CmpdVO;
import newstructureservlet.MoleculeWrangling;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static void main(String[] args) {

        // testStructureSearchController();
        // insertLegacyCmpds();
        // testCtabParse();
        testCtabParseFromDb();
        // testSdfParser();

    }

    public static void testStructureSearchController() {

        CmpdVO cVO = HelperCmpd.getSingleCmpdByNsc(123127, "DTP_kunkelm");
        System.out.println(cVO.toString());

        SessionController sc = new SessionController();
        StructureSearchController ssc = new StructureSearchController();
        ssc.setSessionController(sc);

        ssc.setNscForLoad("123127");
        ssc.performLoadEditorByNsc();
    }

    public static void testSdfParser() {

        MoleculeParser mp = new MoleculeParser();
        File sdFile = new File("/home/mwkunkel/tiny.sdf");
        ArrayList<AdHocCmpd> ahcList = mp.parseSDF(sdFile);

    }

    public static void testCtabParse() {

        String fromDb = "  SciTegic01131510112D\n"
                + "\n"
                + " 39 43  0  0  0  0            999 V2000\n"
                + "   -1.9070   -3.2191    0.0000 O   0  0\n"
                + "   -1.9070    2.1935    0.0000 O   0  0\n"
                + "    4.5783   -5.7685    0.0000 O   0  0\n"
                + "    3.2722   -3.5199    0.0000 O   0  0\n"
                + "    4.5383    1.7041    0.0000 C   0  0\n"
                + "    3.2432    2.4624    0.0000 C   0  0\n"
                + "    0.6891    0.9935    0.0000 C   0  0\n"
                + "    1.9711    0.2243    0.0000 C   0  0\n"
                + "    4.5671    0.2243    0.0000 C   0  0  2  0  0  0\n"
                + "    3.2691    0.9935    0.0000 C   0  0\n"
                + "   -0.6089    0.2243    0.0000 C   0  0\n"
                + "   -1.9070    0.9935    0.0000 C   0  0\n"
                + "   -3.2050    0.2243    0.0000 C   0  0\n"
                + "   -4.5030    0.9935    0.0000 C   0  0\n"
                + "   -5.8010    0.2243    0.0000 C   0  0\n"
                + "    4.5671   -1.2660    0.0000 C   0  0\n"
                + "    1.9711   -1.2660    0.0000 C   0  0\n"
                + "   -0.6089   -1.2660    0.0000 C   0  0\n"
                + "   -3.2050   -1.2660    0.0000 C   0  0\n"
                + "   -5.8010   -1.2660    0.0000 C   0  0\n"
                + "    3.2691   -2.0191    0.0000 C   0  0  1  0  0  0\n"
                + "    0.6891   -2.0191    0.0000 C   0  0\n"
                + "   -1.9070   -2.0191    0.0000 C   0  0\n"
                + "   -4.5030   -2.0191    0.0000 C   0  0\n"
                + "    4.5730   -4.2685    0.0000 C   0  0  3  0  0  0\n"
                + "    5.8695   -3.5140    0.0000 C   0  0\n"
                + "    7.1711   -4.2595    0.0000 C   0  0  3  0  0  0\n"
                + "    5.8799   -6.5140    0.0000 C   0  0  3  0  0  0\n"
                + "    7.1763   -5.7595    0.0000 C   0  0  3  0  0  0\n"
                + "   -4.5061   -3.5199    0.0000 O   0  0\n"
                + "   -5.5462   -4.1184    0.0000 C   0  0\n"
                + "    8.2176   -6.3559    0.0000 O   0  0\n"
                + "    0.6923   -3.2191    0.0000 O   0  0\n"
                + "    0.6923    2.1935    0.0000 O   0  0\n"
                + "    5.8840   -7.7140    0.0000 C   0  0\n"
                + "    8.2083   -3.6559    0.0000 N   0  0\n"
                + "    5.5874   -0.4072    0.0000 O   0  0\n"
                + "    3.2497    3.6624    0.0000 O   0  0\n"
                + "    5.5810    2.2981    0.0000 O   0  0\n"
                + "  1 23  2  0\n"
                + "  2 12  2  0\n"
                + "  3 25  1  0\n"
                + "  3 28  1  0\n"
                + " 21  4  1  6\n"
                + " 25  4  1  4\n"
                + "  9  5  1  1\n"
                + "  5 39  2  0\n"
                + "  5  6  1  0\n"
                + "  6 38  1  0\n"
                + "  7  8  2  0\n"
                + "  7 11  1  0\n"
                + "  7 34  1  0\n"
                + "  8 10  1  0\n"
                + "  8 17  1  0\n"
                + "  9 10  1  0\n"
                + "  9 16  1  0\n"
                + "  9 37  1  6\n"
                + " 11 18  2  0\n"
                + " 11 12  1  0\n"
                + " 12 13  1  0\n"
                + " 13 19  2  0\n"
                + " 13 14  1  0\n"
                + " 14 15  2  0\n"
                + " 15 20  1  0\n"
                + " 16 21  1  0\n"
                + " 17 22  2  0\n"
                + " 17 21  1  0\n"
                + " 18 22  1  0\n"
                + " 18 23  1  0\n"
                + " 19 23  1  0\n"
                + " 19 24  1  0\n"
                + " 20 24  2  0\n"
                + " 22 33  1  0\n"
                + " 24 30  1  0\n"
                + " 25 26  1  0\n"
                + " 26 27  1  0\n"
                + " 27 29  1  0\n"
                + " 27 36  1  4\n"
                + " 28 29  1  0\n"
                + " 28 35  1  4\n"
                + " 29 32  1  4\n"
                + " 30 31  1  0\n"
                + "M  STY  8   1 SUP   2 SUP   3 SUP   4 SUP   5 SUP   6 SUP   7 SUP   8 SUP\n"
                + "M  SLB  8   1   1   2   2   3   3   4   4   5   5   6   6   7   7   8   8\n"
                + "M  SAL   1  2  30  31\n"
                + "M  SBL   1  1  35\n"
                + "M  SMT   1 MeO\n"
                + "M  SBV   1  35    0.0000    0.8760\n"
                + "M  SAL   2  1  32\n"
                + "M  SBL   2  1  42\n"
                + "M  SMT   2 HO\n"
                + "M  SBV   2  42    0.0000    0.8579\n"
                + "M  SAL   3  1  33\n"
                + "M  SBL   3  1  34\n"
                + "M  SMT   3 OH\n"
                + "M  SBV   3  34    0.0000    0.8760\n"
                + "M  SAL   4  1  34\n"
                + "M  SBL   4  1  13\n"
                + "M  SMT   4 OH\n"
                + "M  SBV   4  13    0.0000   -0.8100\n"
                + "M  SAL   5  1  35\n"
                + "M  SBL   5  1  41\n"
                + "M  SMT   5 Me\n"
                + "M  SBV   5  41    0.0000    0.8330\n"
                + "M  SAL   6  1  36\n"
                + "M  SBL   6  1  39\n"
                + "M  SMT   6 NH2\n"
                + "M  SBV   6  39    0.0000    0.8269\n"
                + "M  SAL   7  1  37\n"
                + "M  SBL   7  1  18\n"
                + "M  SMT   7 OH\n"
                + "M  SBV   7  18   -0.6949    0.4020\n"
                + "M  SAL   8  1  38\n"
                + "M  SBL   8  1  10\n"
                + "M  SMT   8 OH\n"
                + "M  SBV   8  10   -0.3309   -0.7529\n"
                + "M  END\n"
                + "$$$$";

        String fromChemDoodle = "Molecule from ChemDoodle Web Components\n"
                + "\n"
                + "http://www.ichemlabs.com\n"
                + " 39 43  0  0  0  0            999 V2000\n"
                + "   -3.1153   -1.1933    0.0000 O   0  0  0  0  0  0\n"
                + "   -3.1153    4.2193    0.0000 O   0  0  0  0  0  0\n"
                + "    3.3700   -3.7427    0.0000 O   0  0  0  0  0  0\n"
                + "    2.0639   -1.4941    0.0000 O   0  0  0  0  0  0\n"
                + "    3.3300    3.7299    0.0000 C   0  0  0  0  0  0\n"
                + "    2.0349    4.4882    0.0000 C   0  0  0  0  0  0\n"
                + "   -0.5192    3.0193    0.0000 C   0  0  0  0  0  0\n"
                + "    0.7628    2.2501    0.0000 C   0  0  0  0  0  0\n"
                + "    3.3588    2.2501    0.0000 C   0  0  0  0  0  0\n"
                + "    2.0608    3.0193    0.0000 C   0  0  0  0  0  0\n"
                + "   -1.8172    2.2501    0.0000 C   0  0  0  0  0  0\n"
                + "   -3.1153    3.0193    0.0000 C   0  0  0  0  0  0\n"
                + "   -4.4133    2.2501    0.0000 C   0  0  0  0  0  0\n"
                + "   -5.7113    3.0193    0.0000 C   0  0  0  0  0  0\n"
                + "   -7.0093    2.2501    0.0000 C   0  0  0  0  0  0\n"
                + "    3.3588    0.7598    0.0000 C   0  0  0  0  0  0\n"
                + "    0.7628    0.7598    0.0000 C   0  0  0  0  0  0\n"
                + "   -1.8172    0.7598    0.0000 C   0  0  0  0  0  0\n"
                + "   -4.4133    0.7598    0.0000 C   0  0  0  0  0  0\n"
                + "   -7.0093    0.7598    0.0000 C   0  0  0  0  0  0\n"
                + "    2.0608    0.0067    0.0000 C   0  0  0  0  0  0\n"
                + "   -0.5192    0.0067    0.0000 C   0  0  0  0  0  0\n"
                + "   -3.1153    0.0067    0.0000 C   0  0  0  0  0  0\n"
                + "   -5.7113    0.0067    0.0000 C   0  0  0  0  0  0\n"
                + "    3.3647   -2.2427    0.0000 C   0  0  0  0  0  0\n"
                + "    4.6612   -1.4882    0.0000 C   0  0  0  0  0  0\n"
                + "    5.9628   -2.2337    0.0000 C   0  0  0  0  0  0\n"
                + "    4.6716   -4.4882    0.0000 C   0  0  0  0  0  0\n"
                + "    5.9680   -3.7337    0.0000 C   0  0  0  0  0  0\n"
                + "   -5.7144   -1.4941    0.0000 O   0  0  0  0  0  0\n"
                + "   -6.7545   -2.0926    0.0000 C   0  0  0  0  0  0\n"
                + "    7.0093   -4.3301    0.0000 O   0  0  0  0  0  0\n"
                + "   -0.5160   -1.1933    0.0000 O   0  0  0  0  0  0\n"
                + "   -0.5160    4.2193    0.0000 O   0  0  0  0  0  0\n"
                + "    4.6757   -5.6882    0.0000 C   0  0  0  0  0  0\n"
                + "    7.0000   -1.6301    0.0000 N   0  0  0  0  0  0\n"
                + "    4.3791    1.6186    0.0000 O   0  0  0  0  0  0\n"
                + "    2.0414    5.6882    0.0000 O   0  0  0  0  0  0\n"
                + "    4.3727    4.3239    0.0000 O   0  0  0  0  0  0\n"
                + "  1 232  0     0  0\n"
                + "  2 122  0     0  0\n"
                + "  3 251  0     0  0\n"
                + "  3 281  0     0  0\n"
                + " 21  41  6     0  0\n"
                + " 25  41  0     0  0\n"
                + "  9  51  1     0  0\n"
                + "  5 392  0     0  0\n"
                + "  5  61  0     0  0\n"
                + "  6 381  0     0  0\n"
                + "  7  82  0     0  0\n"
                + "  7 111  0     0  0\n"
                + "  7 341  0     0  0\n"
                + "  8 101  0     0  0\n"
                + "  8 171  0     0  0\n"
                + "  9 101  0     0  0\n"
                + "  9 161  0     0  0\n"
                + "  9 371  6     0  0\n"
                + " 11 182  0     0  0\n"
                + " 11 121  0     0  0\n"
                + " 12 131  0     0  0\n"
                + " 13 192  0     0  0\n"
                + " 13 141  0     0  0\n"
                + " 14 152  0     0  0\n"
                + " 15 201  0     0  0\n"
                + " 16 211  0     0  0\n"
                + " 17 222  0     0  0\n"
                + " 17 211  0     0  0\n"
                + " 18 221  0     0  0\n"
                + " 18 231  0     0  0\n"
                + " 19 231  0     0  0\n"
                + " 19 241  0     0  0\n"
                + " 20 242  0     0  0\n"
                + " 22 331  0     0  0\n"
                + " 24 301  0     0  0\n"
                + " 25 261  0     0  0\n"
                + " 26 271  0     0  0\n"
                + " 27 291  0     0  0\n"
                + " 27 361  0     0  0\n"
                + " 28 291  0     0  0\n"
                + " 28 351  0     0  0\n"
                + " 29 321  0     0  0\n"
                + " 30 311  0     0  0\n"
                + "M  END";

        System.out.println("fromDb:");
        try {
            IAtomContainer rtn = MoleculeWrangling.fromCtabStringMWK(fromDb);
        } catch (Exception e) {
            System.out.println(e);
            //e.printStackTrace();
        }

        System.out.println("fromChemDoodle:");
        try {
            IAtomContainer rtn = MoleculeWrangling.fromCtabStringMWK(fromChemDoodle);
        } catch (Exception e) {
            System.out.println(e);
            //e.printStackTrace();
        }

    }

    public static void testCtabParseFromDb() {

        CmpdVO cmpd = HelperCmpd.getSingleCmpdByNsc(743380, "DTP_kunkelm");

        String ctabFromDb = cmpd.getParentFragment().getCmpdFragmentStructure().getCtab();

        // System.out.println(ctabFromDb);
//        String ctabFromDb = "  SciTegic01131513202D\n"
//                + "\n"
//                + " 19 21  0  0  0  0            999 V2000\n"
//                + "   -2.3155    0.7475    0.0000 C   0  0\n"
//                + "   -2.3155   -0.7475    0.0000 C   0  0\n"
//                + "   -1.0028   -1.5132    0.0000 C   0  0\n"
//                + "    0.2917   -0.7475    0.0000 C   0  0\n"
//                + "    0.2917    0.7475    0.0000 C   0  0\n"
//                + "   -1.0028    1.5132    0.0000 C   0  0\n"
//                + "    1.7138   -1.2033    0.0000 N   0  0\n"
//                + "    2.5889    0.0182    0.0000 C   0  0\n"
//                + "    1.7138    1.2033    0.0000 C   0  0\n"
//                + "    2.1812    2.6271    0.0000 C   0  0\n"
//                + "    2.1855   -2.6254    0.0000 C   0  0\n"
//                + "    3.6552   -2.9294    0.0000 C   0  0\n"
//                + "    4.1295   -4.3524    0.0000 C   0  0\n"
//                + "    5.5991   -4.6533    0.0000 C   0  0\n"
//                + "    6.5944   -3.5310    0.0000 C   0  0\n"
//                + "    6.1201   -2.1080    0.0000 C   0  0\n"
//                + "    4.6506   -1.8071    0.0000 C   0  0\n"
//                + "    6.9164   -1.2102    0.0000 Cl  0  0\n"
//                + "    3.3556    2.8737    0.0000 O   0  0\n"
//                + "  1  2  2  0\n"
//                + "  1  6  1  0\n"
//                + "  2  3  1  0\n"
//                + "  3  4  2  0\n"
//                + "  4  5  1  0\n"
//                + "  4  7  1  0\n"
//                + "  5  6  2  0\n"
//                + "  5  9  1  0\n"
//                + "  7  8  1  0\n"
//                + "  7 11  1  0\n"
//                + "  8  9  2  0\n"
//                + "  9 10  1  0\n"
//                + " 10 19  1  0\n"
//                + " 11 12  1  0\n"
//                + " 12 13  2  0\n"
//                + " 12 17  1  0\n"
//                + " 13 14  1  0\n"
//                + " 14 15  2  0\n"
//                + " 15 16  1  0\n"
//                + " 16 17  2  0\n"
//                + " 16 18  1  0\n"
//                + "M  STY  1   1 SUP\n"
//                + "M  SLB  1   1   1\n"
//                + "M  SAL   1  1  19\n"
//                + "M  SBL   1  1  13\n"
//                + "M  SMT   1 OH\n"
//                + "M  SBV   1  13   -0.9399    0.0000\n"
//                + "M  END\n"
//                + "$$$$";
        String ctabFromChemDoodle = "'Molecule from ChemDoodle Web Components\n" +
"\n" +
"http://www.ichemlabs.com\n" +
" 17 19  0  0  0  0            999 V2000\n" +
"   -4.6160    2.3176    0.0000 C   0  0  0  0  0  0\n" +
"   -4.6160    0.8225    0.0000 C   0  0  0  0  0  0\n" +
"   -3.3033    0.0568    0.0000 C   0  0  0  0  0  0\n" +
"   -2.0088    0.8225    0.0000 C   0  0  0  0  0  0\n" +
"   -2.0088    2.3176    0.0000 C   0  0  0  0  0  0\n" +
"   -3.3033    3.0833    0.0000 C   0  0  0  0  0  0\n" +
"   -0.5867    0.3668    0.0000 N   0  0  0  0  0  0\n" +
"    0.2884    1.5883    0.0000 C   0  0  0  0  0  0\n" +
"   -0.5867    2.7733    0.0000 C   0  0  0  0  0  0\n" +
"   -0.1150   -1.0553    0.0000 C   0  0  0  0  0  0\n" +
"    1.3547   -1.3593    0.0000 C   0  0  0  0  0  0\n" +
"    1.8290   -2.7824    0.0000 C   0  0  0  0  0  0\n" +
"    3.2986   -3.0832    0.0000 C   0  0  0  0  0  0\n" +
"    4.2940   -1.9609    0.0000 C   0  0  0  0  0  0\n" +
"    3.8197   -0.5379    0.0000 C   0  0  0  0  0  0\n" +
"    2.3502   -0.2371    0.0000 C   0  0  0  0  0  0\n" +
"    4.6160    0.3598    0.0000 Cl  0  0  0  0  0  0\n" +
"  1  22  0     0  0\n" +
"  1  61  0     0  0\n" +
"  2  31  0     0  0\n" +
"  3  42  0     0  0\n" +
"  4  51  0     0  0\n" +
"  4  71  0     0  0\n" +
"  5  62  0     0  0\n" +
"  5  91  0     0  0\n" +
"  7  81  0     0  0\n" +
"  7 101  0     0  0\n" +
"  8  92  0     0  0\n" +
" 10 111  0     0  0\n" +
" 11 122  0     0  0\n" +
" 11 161  0     0  0\n" +
" 12 131  0     0  0\n" +
" 13 142  0     0  0\n" +
" 14 151  0     0  0\n" +
" 15 162  0     0  0\n" +
" 15 171  0     0  0\n" +
"M  END";
        System.out.println("fromDb:");
        try {
            
            IAtomContainer rtn = MoleculeWrangling.fromCtabStringMWK(ctabFromDb);
            System.out.println("fromDb: atomCount: " + rtn.getAtomCount() + " bondCount: " + rtn.getBondCount());

            
            
            
            String fromDbSmiles = MoleculeWrangling.toSmiles(rtn, false);
            System.out.println("smiles: " + fromDbSmiles);
            fromDbSmiles = MoleculeWrangling.toSmiles(rtn, true);
            System.out.println("aromatic smiles: " + fromDbSmiles);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("fromChemDoodle:");
        try {
            IAtomContainer rtn = MoleculeWrangling.fromCtabStringMWK(ctabFromChemDoodle);
            System.out.println("fromChemDoodle: atomCount: " + rtn.getAtomCount() + " bondCount: " + rtn.getBondCount());

            String fromChemDoodleSmiles = MoleculeWrangling.toSmiles(rtn, false);
            System.out.println("smiles: " + fromChemDoodleSmiles);
            fromChemDoodleSmiles = MoleculeWrangling.toSmiles(rtn, true);
            System.out.println("aromatic smiles: " + fromChemDoodleSmiles);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static CmpdLegacyCmpdVO insertLegacyCmpds() {

        CmpdLegacyCmpdVO rtn = new CmpdLegacyCmpdVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            List<Integer> nscIntList = Arrays.asList(new Integer[]{740, 3053, 123127, 163027, 401005, 705701, 743380});

            for (Integer nsc : nscIntList) {

                CmpdLegacyCmpd clc = new CmpdLegacyCmpdImpl();

                clc.setId(nsc.longValue());
                clc.setMolecularFormula("NSC" + nsc);
                clc.setMolecularWeight(-10101d);

                byte[] img = getStructureImage(null, nsc, 512, "NSC" + nsc);

                clc.setJpg512(img);

                session.save(clc);

            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

    public static byte[] getStructureImage(String smiles, Integer nsc, Integer structureDim, String title) throws Exception {

        java.net.URL servletURL = null;

        java.net.HttpURLConnection servletConn = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            servletURL = new java.net.URL("http://localhost:8080/datasystem/StructureServlet");

            servletConn = (java.net.HttpURLConnection) servletURL.openConnection();
            servletConn.setDoInput(true);
            servletConn.setDoOutput(true);
            servletConn.setUseCaches(false);
            servletConn.setRequestMethod("POST");
            servletConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            java.io.DataOutputStream outStream = new java.io.DataOutputStream(servletConn.getOutputStream());

            if (smiles != null) {
                outStream.writeBytes("smiles=" + URLEncoder.encode(smiles, "UTF-8"));
            }

            if (nsc != null) {
                outStream.writeBytes("nsc=" + URLEncoder.encode(nsc.toString(), "UTF-8"));
            }

            if (title != null) {
                outStream.writeBytes("&title=" + URLEncoder.encode(title, "UTF-8"));
            }

            if (structureDim != null) {
                outStream.writeBytes("&structureDim=" + URLEncoder.encode(structureDim.toString(), "UTF-8"));
            }

            outStream.flush();
            outStream.close();

            if (servletConn.getResponseCode() != servletConn.HTTP_OK) {
                throw new Exception("Exception from StructureServlet in getStructureImage in ListManagerController: " + servletConn.getResponseMessage());
            }

//      String tempString = new String();
//      java.io.BufferedReader theReader = new java.io.BufferedReader(new InputStreamReader(servletConn.getInputStream()));
//      while ((tempString = theReader.readLine()) != null) {
//        returnString += tempString;
//      }
            InputStream is = servletConn.getInputStream();

            byte[] buf = new byte[1000];
            for (int nChunk = is.read(buf); nChunk != -1; nChunk = is.read(buf)) {
                baos.write(buf, 0, nChunk);
            }

        } catch (Exception e) {
            System.out.println("Exception in getStructureImage in ListManagerController " + e);
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            servletConn.disconnect();
            servletConn = null;
        }

        baos.flush();
        return baos.toByteArray();

    }

}
