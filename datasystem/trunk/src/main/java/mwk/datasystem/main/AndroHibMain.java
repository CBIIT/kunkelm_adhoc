/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.androdomain.AdHocCmpd;
import mwk.datasystem.androdomain.AdHocCmpdFragment;
import mwk.datasystem.androdomain.Cmpd;
import mwk.datasystem.androdomain.CmpdList;
import mwk.datasystem.androdomain.CmpdListMember;
import mwk.datasystem.androdomain.NscCmpd;
import mwk.datasystem.androdomain.NscCmpdImpl;
import mwk.datasystem.util.Comparators;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperCmpdView;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.util.TransformAndroToVO;
import mwk.datasystem.util.TransformCmpdViewToVO;
import mwk.datasystem.util.TransformXMLGregorianCalendar;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.io.MDLV2000Writer;

import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;

/**
 *
 * @author mwkunkel
 */
public class AndroHibMain {

  public static void main(String[] args) {
    //androFindByNsc();
    //fetchCmpdById();
    //findCmpdsByNsc();    
    //createList();

    //uploadSDFile();
    //showAvailableLists();

    //findCmpdViewByNsc();

    //testGetList();

    //deleteList();

    //substructureSearch();

    ctabFromSmiles();

  }

  private static void ctabFromSmiles() {

    try {

      File inputFile = new File("/tmp/idWithSmiles.csv");
      FileReader fr = new FileReader(inputFile);
      BufferedReader br = new BufferedReader(fr);


      SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

      String curLine;
      String[] splitLine;

      // skipt the first line
      br.readLine();

      while ((curLine = br.readLine()) != null) {

        //System.out.println("Processing: " + curLine);
        
        splitLine = curLine.split(",");
        
        //System.out.println("id is: " + splitLine[0] + " and SMILES is: " + splitLine[1]);

        Molecule molecule = (Molecule) sp.parseSmiles(splitLine[1]);

        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(molecule);
        try {
          sdg.generateCoordinates();
        } catch (Exception ex) {
          ex.printStackTrace();
        }

        Molecule fixedMol = (Molecule) sdg.getMolecule();
        fixedMol.setProperty("id", splitLine[1]);

        File outputFile = new File("/tmp/" + splitLine[0] + ".mol");
        FileOutputStream fos = new FileOutputStream(outputFile);
        MDLV2000Writer ctabWriter = new MDLV2000Writer(fos);

        ctabWriter.write(fixedMol);

        ctabWriter.close();
        fos.close();

      }

      br.close();
      fr.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void substructureSearch() {

    HelperStructure helper = new HelperStructure();
    //743380
    String smiles = "OCc1cn(Cc2cccc(Cl)c2)c2ccccc12";

    List<Integer> nscList = new ArrayList<Integer>(helper.findNSCsBySmilesSubstructure(smiles));

    for (Integer i : nscList) {
      System.out.println("NSC: " + i);
    }


  }

  private static void deleteList() {

    Long cmpdListId = Long.valueOf(4911779948046178266l);

    Session session = HibernateUtil.getSessionFactory().getCurrentSession();

    Transaction tx = session.beginTransaction();

    Criteria clCriteria = session.createCriteria(CmpdList.class);
    clCriteria.add(Restrictions.eq("cmpdListId", cmpdListId));
    CmpdList cl = (CmpdList) clCriteria.uniqueResult();

    System.out.println("CmpdList: " + cl.getCmpdListId() + " " + cl.getListName() + " " + cl.getListOwner());

    //cascade="delete" is enabled from cl->clm
    //so the clm will be deleted,
    //but I set changed to cascade="none" on Cmpd

    //have to programatically determine whether any ad_hoc_cmpds
    //and delete those


    List<Long> idListForDelete = new ArrayList<Long>();
    Long cmpdId = null;

    for (CmpdListMember clm : cl.getCmpdListMembers()) {
      cmpdId = (Long) session.getIdentifier(clm.getCmpd());
      idListForDelete.add(cmpdId);
    }

    System.out.println("Size of idListForDelete is: " + idListForDelete.size());

    // delete the CmpdList        
    session.delete(cl);

    // find and delete those cmpd with join to ahc
    Criteria ahcCriteria = session.createCriteria(AdHocCmpd.class);
    ahcCriteria.add(Restrictions.in("id", idListForDelete));

    List<AdHocCmpd> ahcList = (List<AdHocCmpd>) ahcCriteria.list();

    System.out.println("Size of ahcList is: " + ahcList.size());

    for (AdHocCmpd ahc : ahcList) {
      System.out.println(ahc.getId() + " " + ahc.getName() + " " + ahc.getAdHocCmpdId());
      session.delete(ahc);
    }

    tx.commit();
  }

  public static void testGetList() {

    HelperCmpdList helper = new HelperCmpdList();
    Long cmpdListId = Long.valueOf(6901399848382838719l);
    CmpdListVO clVO = helper.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE);

    System.out.println(clVO.getCmpdListId() + " " + clVO.getListName() + " " + clVO.getListOwner());

    for (CmpdListMemberVO clmVO : clVO.getCmpdListMembers()) {
      CmpdVO cVO = clmVO.getCmpd();
      System.out.println(cVO.getId() + " " + cVO.getName() + " " + cVO.getNsc() + " " + cVO.getParentFragment().getCmpdFragmentPChem().getMf() + " " + cVO.getParentFragment().getCmpdFragmentStructure().getSmiles());
    }

  }

  public static List<CmpdVO> findCmpdViewByNsc() {

    List<CmpdVO> cvoList = new ArrayList<CmpdVO>();

    try {

//            List<Integer> nscIntList = Arrays.asList(new Integer[]{14229, 763579, 763832, 740, 34462, 755980, 765898, 750, 752, 755, 762, 1390, 3053, 3088, 6396, 8806, 9706, 13875, 18509, 19893, 24559, 25154, 26271, 26980, 27640, 32065, 38721, 45388, 45923, 49842, 63878, 66847, 67574, 71423, 77213, 79037, 733504, 82151, 85998, 92859, 102816, 105014, 109724, 226080, 122758, 122819, 123127, 125066, 125973, 127716, 138783, 141540, 169780, 180973, 218321, 241240, 246131, 266046, 279836, 296961, 312887, 362856, 369100, 409962, 606869, 608210, 609699, 613327, 616348, 628503, 683864, 701852, 702294, 712807, 713563, 715055, 718781, 719276, 719344, 719345, 719627, 721517, 732517, 737754, 743414, 745750, 747599, 747971, 747972, 747973, 747974, 749226, 750690, 754143, 754230, 755384, 755605, 761068, 755985, 755986, 756645, 756655, 757441, 758252, 758253, 758487, 760766, 761388, 761431, 761432, 763932, 765694, 757443, 755764, 756661, 170984, 95100, 755403, 766118, 16895, 633782, 755767, 773149, 759178, 757296, 620634, 757043, 759887, 759254, 758654, 755843, 759323, 757851, 758627, 757105, 764778, 759823, 757363, 765948, 755388, 755402, 755404, 754771, 744009, 763371, 28841, 33832, 59813, 157387, 754346, 686288, 766888, 639966, 756656, 761387, 764039, 766991, 764040, 764041, 765262, 757438, 764134, 764765, 758248, 754364, 756663, 758873, 759659, 766270, 727859, 761188, 763444, 764659, 761193, 754345, 758006, 765775, 639186, 637037, 761690, 758484, 756642, 758249, 758774, 759657, 759905, 760143, 761190, 762153, 764136, 62343, 756879, 330507, 707545, 755981, 759674, 766821, 755773, 755386, 763564, 765673, 762419, 763094, 763527, 754358, 754360, 756875, 756876, 761192, 760442, 761189, 756653, 766820, 752493, 754355, 767470, 764579, 758254, 766907, 764334, 754349, 755765, 756654, 760843, 764821, 765632, 765865, 766824, 756649, 765776, 758489, 756660, 756641, 756874, 761070, 761693, 757444, 758245, 758250, 759094, 759498, 759677, 761069, 761691, 762151, 763930, 765889, 759675, 763929, 760144, 754357, 754362, 767898, 768070, 768071, 771650, 683863, 771649, 118218, 764043, 755984, 761910, 756647, 760841, 766887, 758242, 758244, 758591, 759095, 761692, 763931, 764090, 766271, 766288, 765672, 755775, 764582, 764609, 764488, 766811, 760842, 757437, 649890, 758247, 764036, 767048, 762673, 757148, 758257, 710464, 678515, 750691, 755927, 756662, 764135, 761191, 757439, 758005, 764823, 765888, 765949, 760765, 764517, 764581, 765436, 754356, 760444, 764755, 764756, 754361, 764487, 766908, 764580, 764239, 761879, 142784, 754348, 755926, 755982, 756650, 761217, 762672, 762829, 755762, 761194, 765395, 765435, 755772, 756652, 758243, 761760, 761931, 766255, 767006, 764037, 762152, 721782, 758490, 759096, 756648, 764820, 756877, 756878, 763443, 679828, 741078, 755763, 758246, 761215, 762521, 764042, 765695, 765866, 755770, 764608, 751249, 757440, 758256, 758871, 759660, 764515, 764658, 764091, 772887, 752840, 753686, 756644, 765396, 767125, 767533, 763526, 764237, 756657, 756658, 758485, 759658, 221019, 722134, 754351, 754353, 755385, 755769, 759224, 759676, 760443, 760536, 762382, 764092, 764516, 765974, 767124, 757333, 755766, 36405, 755774, 766819, 754344, 754354, 755983, 757149, 761759, 762154, 762418, 766906, 755771, 763525, 703081, 754350, 758486, 761385, 761386, 763758, 767123, 767126, 756643, 758872, 755389, 755401, 757334, 755387, 766992, 761216, 761266, 764764, 755761, 673596, 754365, 754366, 754367, 698215, 758251, 760764, 758488, 758773, 762598, 754347, 755606, 758007, 772254, 759661, 762381, 764481, 756659, 754352, 756646, 754359, 764757, 755400, 768536, 767599, 767954, 771652, 772197, 755892, 701554, 766254, 768073, 773094, 767953, 741899, 772595, 771750, 767897, 771651, 772196, 767600, 767745, 772290, 772291, 764238, 772198, 767952, 771751, 772351, 761390, 768509, 772885, 767128, 772868, 768539, 772564, 773096, 767746, 767951, 767601, 767598, 772920, 772255, 771644, 773174, 771531, 768068, 768069, 772563, 767189, 771532, 768593, 773230, 768100, 768508, 772469, 773095, 772991, 768072, 751297, 768011, 767896, 771858, 772498, 772992, 768112, 772886, 772499});

      List<Integer> nscIntList = new ArrayList<Integer>();
      nscIntList.add(Integer.valueOf(163027));
      nscIntList.add(Integer.valueOf(401005));
      nscIntList.add(Integer.valueOf(705701));
      nscIntList.add(Integer.valueOf(743380));

      HelperCmpdView helper = new HelperCmpdView();

      cvoList = helper.getCmpdViewsByNsc(nscIntList);

      for (CmpdVO cmpdVO : cvoList) {
        System.out.println(cmpdVO.getId() + " " + cmpdVO.getName() + " " + cmpdVO.getNsc() + " " + cmpdVO.getParentFragment().getCmpdFragmentPChem().getMf() + " " + cmpdVO.getParentFragment().getCmpdFragmentStructure().getSmiles());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return cvoList;

  }

  private static void showAvailableLists() {

    List<CmpdList> availableLists = new ArrayList<CmpdList>();

    try {

      Session session = HibernateUtil.getSessionFactory().getCurrentSession();

      Transaction tx = session.beginTransaction();
      Criteria c = session.createCriteria(CmpdList.class);
      c.add(Restrictions.disjunction()
              .add(Restrictions.eq("listOwner", "kunkelm"))
              .add(Restrictions.eq("shareWith", "PUBLIC")));
      availableLists = (List<CmpdList>) c.list();

      tx.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<CmpdListVO> voList = TransformAndroToVO.translateCmpdLists(availableLists, Boolean.FALSE);

    for (CmpdListVO clVO : voList) {
      System.out.println(clVO.getDateCreated() + " " + clVO.getListOwner() + clVO.getListName());
    }

  }

  private static void uploadSDFile() {

    try {

      Random randomGenerator = new Random();

      MoleculeParser mp = new MoleculeParser();

      File sdFile = new File("/home/mwkunkel/tiny.sdf");

      ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSDF(sdFile);

      // have to persist the compounds
      // then associate with listMembers
      // then create list

      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();

      Date now = new Date();

      String listName = "test upload SDF File " + now.toString();
      String listOwner = "kunkelm";
      String shareWith = "kunkelm";

      ArrayList<Cmpd> entityCmpdList = new ArrayList<Cmpd>();

      for (AdHocCmpd ahc : adHocCmpdList) {

        // come up with a unique adHocListId
        //do {      
        long randomId = randomGenerator.nextLong();
        if (randomId < 0) {
          randomId = -1 * randomId;
        }
        Long adHocCmpdId = new Long(randomId);
        //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

        ahc.setAdHocCmpdId(adHocCmpdId);
        ahc.setCmpdOwner(listOwner);
        ahc.setName(listName);

        session.persist("Cmpd", ahc);

        for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {
          // persist the struc and pchem
          session.persist(ahcf.getAdHocCmpdFragmentPChem());
          session.persist(ahcf.getAdHocCmpdFragmentStructure());
          // persist the fragment
          session.persist(ahcf);
        }

        // if only one fragment, make it the parent, otherwise sort by size

        ArrayList<AdHocCmpdFragment> fragList = new ArrayList<AdHocCmpdFragment>(ahc.getAdHocCmpdFragments());
        Collections.sort(fragList, new Comparators.AdHocCmpdFragmentSizeComparator());
        Collections.reverse(fragList);
        ahc.setAdHocCmpdParentFragment(fragList.get(0));

        // track the cmpds for addition to CmpdList
        entityCmpdList.add(ahc);

      }

      // create a new list

      //do {    
      long randomId = randomGenerator.nextLong();
      if (randomId < 0) {
        randomId = -1 * randomId;
      }
      Long cmpdListId = new Long(randomId);
      //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

      CmpdList cl = CmpdList.Factory.newInstance();

      cl.setCmpdListId(cmpdListId);
      cl.setListName(listName);
      cl.setDateCreated(now);
      cl.setListOwner(listOwner);
      cl.setShareWith(shareWith);
      cl.setCountListMembers(entityCmpdList.size());

      // id from GenerateSequence in Entity class
      session.persist(cl);

      //Set<CmpdListMember> listMemberSet = new HashSet<CmpdListMember>();

      for (Cmpd c : entityCmpdList) {
        CmpdListMember clm = CmpdListMember.Factory.newInstance();
        clm.setCmpd(c);
        clm.setCmpdList(cl);
        session.persist(clm);
      }

      // add the members to the list

      //cl.setCmpdListMembers(listMemberSet);
      //cl.setCountListMembers(listMemberSet.size());

      // IS THIS NEEDED?
      // session.merge(cl);

      session.getTransaction().commit();

      CmpdListVO clVO = TransformCmpdViewToVO.toCmpdListVO(cl, Boolean.TRUE);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static List<Cmpd> findCmpdsByNsc() {

    List<Integer> nscList = new ArrayList<Integer>();
    nscList.add(Integer.valueOf(163027));
    nscList.add(Integer.valueOf(401005));
    nscList.add(Integer.valueOf(705701));
    nscList.add(Integer.valueOf(743380));

    List<Long> adHocCmpdIdList = new ArrayList<Long>();
    adHocCmpdIdList.add(Long.valueOf(2150925853906353151l));
    adHocCmpdIdList.add(Long.valueOf(3967084847705459749l));
    adHocCmpdIdList.add(Long.valueOf(818203042234661984l));
    adHocCmpdIdList.add(Long.valueOf(6319529189351633246l));
    adHocCmpdIdList.add(Long.valueOf(6875922166410226743l));

    List<String> projectCodeList = new ArrayList<String>();
    projectCodeList.add("FAKE1");
    projectCodeList.add("FAKE2");

    Session session = HibernateUtil.getSessionFactory().getCurrentSession();

    session.beginTransaction();


//        Criteria criteria = session.createCriteria(Category.class);
//        criteria.add(Restrictions.eq("active", Boolean.TRUE));
//        criteria.add(Subqueries.propertyEq("parent", subCriteria));

    Criteria c = session.createCriteria(Cmpd.class);

    c.add(Restrictions.disjunction()
            .add(Restrictions.in("nsc", nscList))
            .add(Restrictions.in("adHocCmpdId", adHocCmpdIdList)));

    List<Cmpd> cmpdList = c.list();
    for (Cmpd cmpd : cmpdList) {

      if (cmpd instanceof NscCmpdImpl) {
        NscCmpd n = (NscCmpd) cmpd;
        System.out.println(cmpd.getId() + " " + cmpd.getClass() + " " + n.getName() + " " + n.getNsc());
      } else {
        AdHocCmpd ahc = (AdHocCmpd) cmpd;
        System.out.println(cmpd.getId() + " " + ahc.getClass() + " " + ahc.getName());
      }
    }

    session.getTransaction().commit();

    return cmpdList;
  }

  public static void createList() {

    Date now = new Date();
    XMLGregorianCalendar xmlNow = TransformXMLGregorianCalendar.asXMLGregorianCalendar(now);

    String listName = "testList" + now.toString();
    String listOwner = "kunkelm";
    String shareWith = "kunkelm";

    // call testFindCmpds() which will populate ENTITY_CMPD_LIST

    List<Cmpd> cmpdList = findCmpdsByNsc();

    // come up with a unique ListId

    Long novumListId = null;

    //do {

    java.util.Random generator = new Random();
    long randomId = generator.nextLong();

    if (randomId < 0) {
      randomId = -1 * randomId;
    }

    novumListId = new Long(randomId);

    //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    org.hibernate.Transaction tx = session.beginTransaction();

    // create a new list

    CmpdList cl = CmpdList.Factory.newInstance();

    cl.setCmpdListId(novumListId);
    cl.setListName(listName);
    cl.setDateCreated(now);
    cl.setListOwner(listOwner);
    cl.setShareWith(shareWith);
    cl.setCountListMembers(cmpdList.size());

    session.persist(cl);

    for (Cmpd c : cmpdList) {
      CmpdListMember clm = CmpdListMember.Factory.newInstance();
      clm.setCmpd(c);
      clm.setCmpdList(cl);
      session.persist(clm);
      // not needed for persistence, added for quick(er) conversion to VO
      cl.getCmpdListMembers().add(clm);
    }

    session.getTransaction().commit();

  }
}
