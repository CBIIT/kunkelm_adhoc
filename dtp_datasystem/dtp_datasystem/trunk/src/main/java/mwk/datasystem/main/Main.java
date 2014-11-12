/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.controllers.QueryObject;
import mwk.datasystem.controllers.StructureSearchController;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.util.TransformAndroToVO;
import mwk.datasystem.vo.CmpdListVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;
import org.openscience.cdk.tautomers.InChITautomerGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static void main(String[] args) {
    testCtabFromSmiles();
  }
  
  public static void testCtabFromSmiles(){
    
    StructureSearchController scl = new StructureSearchController();
    
    String str743380 = "OCc1cn(Cc2cccc(Cl)c2)c2ccccc12";
    
    String ctab = scl.ctabFromSmiles(str743380);
    
    System.out.print(ctab);
    
  }

  public static void testReplace() {
    String str401005 = "Molecule from ChemDoodle Web Components\n" +
"\n" +
"http://www.ichemlabs.com\n" +
" 18 21  0  0  0  0            999 V2000\n" +
"   -1.7321   -0.5000    0.0000 C   0  0  0  0  0  0\n" +
"   -0.8660   -1.0000    0.0000 C   0  0  0  0  0  0\n" +
"   -0.8660   -2.0000    0.0000 C   0  0  0  0  0  0\n" +
"   -1.7321   -2.5000    0.0000 C   0  0  0  0  0  0\n" +
"   -2.5981   -2.0000    0.0000 C   0  0  0  0  0  0\n" +
"   -2.5981   -1.0000    0.0000 C   0  0  0  0  0  0\n" +
"    0.0000   -0.5000    0.0000 C   0  0  0  0  0  0\n" +
"    0.8660   -1.0000    0.0000 C   0  0  0  0  0  0\n" +
"    0.8660   -2.0000    0.0000 C   0  0  0  0  0  0\n" +
"    0.0000   -2.5000    0.0000 C   0  0  0  0  0  0\n" +
"    0.0000    0.5000    0.0000 C   0  0  0  0  0  0\n" +
"    0.8660    1.0000    0.0000 C   0  0  0  0  0  0\n" +
"    1.7321    0.5000    0.0000 C   0  0  0  0  0  0\n" +
"    1.7321   -0.5000    0.0000 C   0  0  0  0  0  0\n" +
"    0.8660    2.0000    0.0000 C   0  0  0  0  0  0\n" +
"    1.7321    2.5000    0.0000 C   0  0  0  0  0  0\n" +
"    2.5981    2.0000    0.0000 C   0  0  0  0  0  0\n" +
"    2.5981    1.0000    0.0000 C   0  0  0  0  0  0\n" +
"  1  21  0     0  0\n" +
"  2  32  0     0  0\n" +
"  3  41  0     0  0\n" +
"  4  52  0     0  0\n" +
"  5  61  0     0  0\n" +
"  6  12  0     0  0\n" +
"  2  71  0     0  0\n" +
"  7  82  0     0  0\n" +
"  8  91  0     0  0\n" +
"  9 102  0     0  0\n" +
" 10  31  0     0  0\n" +
"  7 111  0     0  0\n" +
" 11 122  0     0  0\n" +
" 12 131  0     0  0\n" +
" 13 142  0     0  0\n" +
" 14  81  0     0  0\n" +
" 12 151  0     0  0\n" +
" 15 162  0     0  0\n" +
" 16 171  0     0  0\n" +
" 17 182  0     0  0\n" +
" 18 131  0     0  0\n" +
"M  END";

    System.out.print(str401005);
    
    String crStrip = str401005.replaceAll("\\r\\n|\\r|\\n", "\\\\n");
    
    System.out.print(crStrip);
    
  }

  public static void testHibernateCriteria() {

    HelperCmpd cmpdHelper = new HelperCmpd();

    QueryObject lcb = new QueryObject();
    lcb.getProjectCodes().add("SL-102");
    lcb.getProjectCodes().add("DTP-114");

    Long cmpdListId = cmpdHelper.createCmpdListByListContentBean("TEST", lcb, null, "kunkelm");

    System.out.println("cmpdListId: " + cmpdListId);

  }

  public static List<CmpdListVO> showAvailableCmpdLists(String currentUser) {

    System.out.println("In showAvailableCmpdLists.");

    List<CmpdListVO> voList = new ArrayList<CmpdListVO>();
    List<CmpdList> entityList = null;

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();
      Criteria c = session.createCriteria(CmpdList.class);
      c.add(Restrictions.disjunction()
              .add(Restrictions.eq("listOwner", currentUser))
              .add(Restrictions.eq("shareWith", "PUBLIC")));
      entityList = (List<CmpdList>) c.list();

      voList = TransformAndroToVO.translateCmpdLists(entityList, Boolean.FALSE);

      tx.commit();

    } catch (Exception e) {
      tx.rollback();
      e.printStackTrace();
    } finally {
      if (session != null) {
        session.close();
      }
    }

    return voList;

  }

  public static void testSmilesParsing() {

    File smilesFile = new File("/home/mwkunkel/tautomers2.smi");

    MoleculeParser mp = new MoleculeParser();
    mp.parseSMILESFile(smilesFile);

  }

  public static void testCdk() {

    try {

      IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
      SmilesParser sp = new SmilesParser(builder);

      SmilesGenerator sg = new SmilesGenerator();

      InChIGeneratorFactory igf = InChIGeneratorFactory.getInstance();

      // NSC3124 Oc1ccc(cc1)N=O
      IAtomContainer NSC3124 = sp.parseSmiles("O=Nc1ccc(O)cc1");
      IAtomContainer NSC36938 = sp.parseSmiles("ON=C1C=CC(=O)C=C1");

      // after CDK kekulization
      // NSC3124: O=NC1=CC=C(O)C=C1
      // NSC36938: ON=C1C=CC(=O)C=C1
      System.out.println("NSC3124: " + sg.createSMILES(NSC3124));
      System.out.println("NSC36938: " + sg.createSMILES(NSC36938));

      System.out.println("NSC3124: " + igf.getInChIGenerator(NSC3124).getInchi());
      System.out.println("NSC36938: " + igf.getInChIGenerator(NSC36938).getInchi());

// configure atom types
      AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(NSC3124);
      CDKHydrogenAdder.getInstance(builder).addImplicitHydrogens(NSC3124);
      System.out.println("NSC3124: " + sg.createSMILES(NSC3124));
      System.out.println("NSC3124: " + igf.getInChIGenerator(NSC3124).getInchi());

      AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(NSC36938);
      CDKHydrogenAdder.getInstance(builder).addImplicitHydrogens(NSC36938);
      System.out.println("NSC36938: " + sg.createSMILES(NSC36938));
      System.out.println("NSC36938: " + igf.getInChIGenerator(NSC36938).getInchi());

      org.openscience.cdk.tautomers.InChITautomerGenerator tautGen = new InChITautomerGenerator();
      List<IAtomContainer> tauts = tautGen.getTautomers(NSC3124);
      for (IAtomContainer iac : tauts) {
        System.out.println(sg.createSMILES(iac));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void tautomerTestIhnfeldSMARTS() {

    String ruleDesc = "";
    String ruleSMARTS = "";

    try {

      String[] smirksArray = new String[]{
        "Rule 1: 1,3 (thio)keto/(thio)enol",
        "[O,S,Se,Te;X1:1]=[C;z{1-2}:2][CX4R{0-2}:3][#1:4]",
        ">>",
        "[#1:4][O,S,Se,Te;X2:1][#6:2]=[C,cz{0-1}R{0-1}:3]",
        "Rule 2: 1,5 (thio)keto/(thio)enol",
        "[O,S,Se,Te;X1:1]=[Cz1H0:2][C:5]=[C:6][CX4z0,NX3:3][#1:4]",
        ">>",
        "[#1:4][O,S,Se,Te;X2:1][Cz1:2]=[C:5][C:6]=[Cz0,N:3]",
        "Rule 3: simple (aliphatic) imine",
        "[#1,a:5][NX2:1]=[Cz1:2][CX4R{0-2}:3][#1:4]",
        ">>",
        "[#1,a:5][NX3:1]([#1:4])[Cz1,Cz2:2]=[C:3]",
        "Rule 4: special imine",
        "[Cz0R0X3:1]([C:5])=[C:2][Nz0:3][#1:4]",
        ">>",
        "[#1:4][Cz0R0X4:1]([C:5])[c:2]=[nz0:3]",
        "Rule 5: 1,3 aromatic heteroatom H shift",
        "[#1:4][N:1][C;e6:2]=[O,NX2:3]",
        ">>",
        "[NX2,nX2:1]=[C,c;e6:2][O,N:3][#1:4]",
        "Rule 6: 1,3 heteroatom H shift",
        "[N,n,S,s,O,o,Se,Te:1]=[NX2,nX2,C,c,P,p:2][N,n,S,O,Se,Te:3][#1:4]",
        ">>",
        "[#1:4][N,n,S,O,Se,Te:1][NX2,nX2,C,c,P,p:2]=[N,n,S,s,O,o,Se,Te:3]",
        "Rule 7: 1,5 (aromatic) heteroatom H shift (1)",
        "[nX2,NX2,S,O,Se,Te:1]=[C,c,nX2,NX2:6][C,c:5]=[C,c,nX2:2][N,n,S,s,O,o,Se,Te:3][#1:4]",
        ">>",
        "[#1:4][N,n,S,O,Se,Te:1][C,c,nX2,NX2:6]=[C,c:5][C,c,nX2:2]=[NX2,S,O,Se,Te:3]",
        "Rule 8: 1,5 aromatic heteroatom H shift (2)",
        "[n,s,o:1]=[c,n:6][c:5]=[c,n:2][n,s,o:3][#1:4]",
        ">>",
        "[#1:4][n,s,o:1][c,n:6]=[c:5][c,n:2]=[n,s,o:3]",
        "Rule 9: 1,7 (aromatic) heteroatom H shift",
        "[nX2,NX2,S,O,Se,Te,Cz0X3:1]=[c,C,NX2,nX2:6][C,c:5]=[C,c,NX2,nX2:2][C,c,NX2,nX2:7]=[C,c,NX2,nX2:8][N,n,S,s,O,o,Se,Te:3][#1:4]",
        ">>",
        "[#1:4][N,n,S,O,Se,Te,Cz0X4:1][C,c,NX2,nX2:6]=[C,c:5][C,c,NX2,nX2:2]=[C,c,NX2,nX2:7][C,c,NX2,nX2:8]=[NX2,S,O,Se,Te:3][C,c,NX2,nX2:8]=[NX2,S,O,Se,Te:3]",
        "Rule 10: 1,9 (aromatic) heteroatom H shift",
        "[#1:1][n,N,O:2][c,nX2,C:3]=[c,nX2,C:4][c,nX2:5]=[c,nX2:6][c,nX2:7]=[c,nX2:8][c,nX2,C:9]=[n,N,O:10]",
        ">>",
        "[N,n,O:2]=[C,c,nX2:3][c,nX2:4]=[c,nX2:5][c,nX2:6]=[c,nX2:7][c,nX2:8]=[c,nX2:9][n,O:10][#1:1]",
        "Rule 11: 1,11 (aromatic) heteroatom H shift",
        "[#1:1][n,N,O:2][c,nX2,C:3]=[c,nX2,C:4][c,nX2:5]=[c,C,nX2:6][c,C,nX2:7]=[c,C,nX2:8][c,nX2,C:9]=[c,C,nX2:10][c,C,nX2:11]=[nX2,NX2,O:12]",
        ">>",
        "[NX2,nX2,O:2]=[C,c,nX2:3][c,C,nX2:4]=[c,C,nX2:5][c,C,nX2:6]=[c,C,nX2:7][c,C,nX2:8]=[c,C,nX2:9][c,C,nX2:10]=[c,C,nX2:11][nX2,O:12][#1:1]",
        "Rule 12: furanones",
        "[#1:1][O,S,N:2][c,C;z2;r5:3]=[C,c;r5:4][c,C;r5:5]",
        ">>",
        "[O,S,N:2]=[Cz2r5:3][C&r5R{0-2}:4]([#1:1])[C,c;r5:5]",
        "Rule 13: keten/ynol exchange",
        "[O,S,Se,Te;X1:1]=[C:2]=[C:3][#1:4]",
        ">>",
        "[#1:4][O,S,Se,Te;X2:1][C:2]#[C:3]",
        "Rule 14: ionic nitro/aci-nitro",
        "[#1:1][C:2][N?:3]([O–:5])=[O:4]",
        ">>",
        "[C:2]=[N?:3]([O-:5])[O:4][#1:1] checkcharges",
        "Rule 15: pentavalent nitro/aci-nitro",
        "[#1:1][C:2][N:3](=[O:5])=[O:4]",
        ">>",
        "[C:2]=[N:3](=[O:5])[O:4][#1:1]",
        "Rule 16: oxim/nitroso",
        "[#1:1][O:2][Nz1:3]=[C:4]",
        ">>",
        "[O:2]=[Nz1:3][C:4][#1:1]",
        "Rule 17: oxim/nitroso via phenol",
        "[#1:1][O:2][N:3]=[C:4][C:5]=[C:6][C:7]=[O:8]",
        ">>",
        "[O:2]=[N:3][c:4]=[c:5][c:6]=[c:7][O:8][#1:1]",
        "Rule 18: cyanic/iso-cyanic acids",
        "[#1:1][O:2][C:3]#[N:4]",
        ">>",
        "[O:2]=[C:3]=[N:4][#1:1]",
        "Rule 19: formamidinesulfinic acids",
        "[#1:1][O,N:2][C:3]=[S,Se,Te:4]=[O:5]",
        ">>",
        "[O,N:2]=[C:3][S,Se,Te:4][O:5][#1:1]",
        "Rule 20: isocyanides",
        "[#1:1][C0:2]#[N0:3]",
        ">>",
        "[C–:2]#[N?:3][#1:1] checkcharges checkaro",
        "Rule 21: phosphonic acids",
        "[#1:1][O:2][P:3]",
        ">>",
        "[O:2]=[P:3][#1:1] "
      };

      IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
      SmilesParser sp = new SmilesParser(builder);

      IAtomContainer target = sp.parseSmiles("ON=C1C=CC(=O)C=C1");

      SMARTSQueryTool sqt = new SMARTSQueryTool("CCCC");

      for (int i = 0; i < smirksArray.length; i = i + 4) {

        ruleDesc = smirksArray[i + 0] + " LHS";
        String ruleLHS = smirksArray[i + 1];
        ruleSMARTS = ruleLHS;
        sqt.setSmarts(ruleLHS);

        ruleDesc = smirksArray[i + 0] + " RHS";
        String ruleRHS = smirksArray[i + 3];
        ruleSMARTS = ruleRHS;
        sqt.setSmarts(ruleRHS);

      }

    } catch (Exception e) {
      System.out.println("SMARTS error: " + e);
      System.out.println("rule: " + ruleDesc + " rule: " + ruleSMARTS);
    } catch (org.openscience.cdk.smiles.smarts.parser.TokenMgrError tme) {
      System.out.println("SMARTS TokenMgrError: " + tme);
      System.out.println("rule: " + ruleDesc + " rule: " + ruleSMARTS);
    }

  }

  public static void tautomerMattSwainSMARTS() {

    String ruleDesc = "";
    String ruleSMARTS = "";

    try {

      String[] smirksArray = new String[]{
        "1,3 (thio)keto/enol forward", "[CX4!H0][C]=[O,S,Se,Te;X1]",
        "1,3 (thio)keto/enol reverse", "[O,S,Se,Te;X2!H0][C]=[C]",
        "1,5 (thio)keto/enol forward", "[CX4,NX3;!H0][C]=[C][CH0]=[O,S,Se,Te;X1]",
        "1,5 (thio)keto/enol reverse", "[O,S,Se,Te;X2!H0][CH0]=,:[C][C]=,:[C,N]",
        "aliphatic imine forward", "[CX4!H0][C]=[NX2]",
        "aliphatic imine reverse", "[NX3!H0][C]=[CX3]",
        "special imine forward", "[N!H0][C]=[CX3R0]",
        "special imine reverse", "[CX4!H0][c]=,:[n]",
        "1,3 aromatic heteroatom H shift forward", "[#7!H0][#6R1]=[O,#7X2]",
        "1,3 aromatic heteroatom H shift reverse", "[O,#7;!H0][#6R1]=,:[#7X2]",
        "1,3 heteroatom H shift", "[#7,S,O,Se,Te;!H0][#7X2,#6,#15]=[#7,#16,#8,Se,Te]",
        "1,5 aromatic heteroatom H shift", "[n,s,o;!H0]:[c,n]:[c]:[c,n]:[n,s,o;H0]",
        "1,5 aromatic heteroatom H shift forward", "[#7,#16,#8,Se,Te;!H0][#6,nX2]=,:[#6,nX2][#6,#7X2]=,:[#7X2,S,O,Se,Te]",
        "1,5 aromatic heteroatom H shift reverse", "[#7,S,O,Se,Te;!H0][#6,#7X2]=,:[#6,nX2][#6,nX2]=,:[#7,#16,#8,Se,Te]",
        "1,7 aromatic heteroatom H shift forward", "[#7,#8,#16,Se,Te;!H0][#6,#7X2]=,:[#6,#7X2][#6,#7X2]=,:[#6][#6,#7X2]=,:[#7X2,S,O,Se,Te,CX3]",
        "1,7 aromatic heteroatom H shift reverse", "[#7,S,O,Se,Te,CX4;!H0][#6,#7X2]=,:[#6][#6,#7X2]=,:[#6,#7X2][#6,#7X2]=,:[NX2,S,O,Se,Te]",
        "1,9 aromatic heteroatom H shift forward", "[#7,O;!H0][#6,#7X2]=,:[#6,#7X2][#6,#7X2]=,:[#6,#7X2][#6,#7X2]=,:[#6,#7X2][#6,#7X2]=,:[#7,O]",
        "1,11 aromatic heteroatom H shift forward", "[#7,O;!H0][#6,nX2]=,:[#6,nX2][#6,nX2]=,:[#6,nX2][#6,nX2]=,:[#6,nX2][#6,nX2]=,:[#6,nX2][#6,nX2]=,:[#7X2,O]",
        "furanone forward", "[O,S,N;!H0][#6X3r5;$([#6][!#6])]=,:[#6X3r5]",
        "furanone reverse", "[#6r5!H0][#6X3r5;$([#6][!#6])]=[O,S,N]",
        "keten/ynol forward", "[C!H0]=[C]=[O,S,Se,Te;X1]",
        "keten/ynol reverse", "[O,S,Se,Te;!H0X2][C]#[C]",
        "ionic nitro/aci-nitro forward", "[C!H0][N+;$([N][O-])]=[O]",
        "ionic nitro/aci-nitro reverse", "[O!H0][N+;$([N][O-])]=[C]",
        "oxim/nitroso forward", "[O!H0][N]=[C]",
        "oxim/nitroso reverse", "[C!H0][N]=[O]",
        "oxim/nitroso via phenol forward", "[O!H0][N]=[C][C]=[C][C]=[OH0]",
        "oxim/nitroso via phenol reverse", "[O!H0][c]:[c]:[c]:[c][N]=[OH0]",
        "cyano/iso-cyanic acid forward", "[O!H0][C]#[N]",
        "cyano/iso-cyanic acid reverse", "[N!H0]=[C]=[O]",
        "formamidinesulfinic acid forward", "[O,N;!H0][C][S,Se,Te]=[O]",
        "formamidinesulfinic acid reverse", "[O!H0][S,Se,Te][C]=[O,N]",
        "isocyanide forward", "[C-0!H0]#[N+0]",
        "isocyanide reverse", "[N+!H0]#[C-]",
        "phosphonic acid forward", "[OH][PH0]",
        "phosphonic acid reverse", "[PH]=[O]"
      };

      IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
      SmilesParser sp = new SmilesParser(builder);

      String smi = "O=NC1=CC=C(O)C=C1"; // NSC3124: 
      //String smi = "ON=C1C=CC(=O)C=C1"; // NSC36938: 

      IAtomContainer target = sp.parseSmiles(smi);

      SMARTSQueryTool sqt = new SMARTSQueryTool("CCCC");

      for (int i = 0; i < smirksArray.length; i = i + 2) {

        // System.out.println(ruleDesc + " " + ruleSMARTS);
        ruleDesc = smirksArray[i + 0];
        ruleSMARTS = smirksArray[i + 1];
        sqt.setSmarts(ruleSMARTS);

        if (sqt.matches(target)) {
          System.out.println("RULE MATCHES! rule: " + ruleDesc + " rule: " + ruleSMARTS);
        }

      }

    } catch (Exception e) {
      System.out.println("SMARTS error: " + e);
      System.out.println("rule: " + ruleDesc + " rule: " + ruleSMARTS);
    } catch (org.openscience.cdk.smiles.smarts.parser.TokenMgrError tme) {
      System.out.println("SMARTS TokenMgrError: " + tme);
      System.out.println("rule: " + ruleDesc + " rule: " + ruleSMARTS);
    }

  }
}
