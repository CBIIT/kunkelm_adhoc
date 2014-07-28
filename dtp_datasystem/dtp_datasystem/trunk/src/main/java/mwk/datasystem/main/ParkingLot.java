/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdImpl;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import mwk.datasystem.domain.NscCmpd;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.util.Comparators;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.util.TransformAndroToVO;
import mwk.datasystem.util.TransformCmpdTableToVO;
import mwk.datasystem.util.TransformXMLGregorianCalendar;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdTableVO;
import mwk.datasystem.vo.CmpdVO;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

/**
 *
 * @author mwkunkel
 */
public class ParkingLot {

        public static void adHocCmpdsToCmpdTable() {

        Session session = null;
        Transaction tx = null;
        Transaction tx2 = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria crit = session.createCriteria(AdHocCmpd.class);
            List<AdHocCmpd> cmpdList = crit.list();

            ArrayList<CmpdTable> l = new ArrayList<CmpdTable>();

            for (AdHocCmpd ahc : cmpdList) {

                CmpdTable ct = CmpdTable.Factory.newInstance();

                AdHocCmpdFragmentPChem ahcfpc = ahc.getAdHocCmpdParentFragment().getAdHocCmpdFragmentPChem();
                AdHocCmpdFragmentStructure ahcfps = ahc.getAdHocCmpdParentFragment().getAdHocCmpdFragmentStructure();

                ct.setMw(ahcfpc.getMw());
                ct.setMf(ahcfpc.getMf());
                ct.setAlogp(ahcfpc.getAlogp());
                ct.setLogd(ahcfpc.getLogd());
                ct.setHba(ahcfpc.getHba());
                ct.setHbd(ahcfpc.getHbd());
                ct.setSa(ahcfpc.getSa());
                ct.setPsa(ahcfpc.getPsa());
                ct.setSmiles(ahcfps.getSmiles());
                ct.setInchi(ahcfps.getInchi());
                ct.setMol(ahcfps.getMol());
                ct.setInchiAux(ahcfps.getInchiAux());
                ct.setName(ahc.getName());

                // nulls 

//            cv.setNscCmpdId(ahc.getNscCmpdId());
//            cv.setPrefix(ahc.getPrefix());
//            cv.setNsc(ahc.getNsc());
//            cv.setConf(ahc.getConf());
//            cv.setDistribution(ahc.getDistribution());
//            cv.setCas(ahc.getCas());
//            cv.setNci60(ahc.getNci60());
//            cv.setHf(ahc.getHf());
//            cv.setXeno(ahc.getXeno());

                ct.setAdHocCmpdId(ahc.getAdHocCmpdId());
                ct.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());

//            cv.setTargets(ahc.getTargets());
//            cv.setSets(ahc.getSets());
//            cv.setProjects(ahc.getProjects());
//            cv.setPlates(ahc.getPlates());
//            cv.setAliases(ahc.getAliases());

                ct.setId(ahc.getId());

//            cv.setInventory(ahc.getInventory());

                l.add(ct);

            }

            tx.commit();

//            for (CmpdTable ct : l) {
//
//                CmpdVO ctVO = TransformCmpdTableToVO.toCmpdVO(ct);
//
//                System.out.println(ctVO.toString());
//
//                System.out.println(ct.getMw());
//                System.out.println(ct.getMf());
//                System.out.println(ct.getAlogp());
//                System.out.println(ct.getLogd());
//                System.out.println(ct.getHba());
//                System.out.println(ct.getHbd());
//                System.out.println(ct.getSa());
//                System.out.println(ct.getPsa());
//                System.out.println(ct.getSmiles());
//                System.out.println(ct.getInchi());
//                System.out.println(ct.getMol());
//                System.out.println(ct.getInchiAux());
//                System.out.println(ct.getName());
//                System.out.println("--------------------------------------------------------------");
//
//            }

            tx2 = session.beginTransaction();

            for (CmpdTable ct : l) {
                System.out.println("ct.getId(): " + ct.getId());
                session.merge(ct);
            }

            tx2.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (tx2.isActive()) {
                tx2.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public static String performListLogicSql() {

        Long listAid = 9129646950110277617l;
        Long listBid = 943327792639172795l;

        ArrayList<Integer> AnotBintList = doSingleQuery("except", listAid, listBid);
        ArrayList<Integer> AandBintList = doSingleQuery("intersect", listAid, listBid);
        ArrayList<Integer> AorBintList = doSingleQuery("union", listAid, listBid);

        HelperCmpd helper = new HelperCmpd();

//        this.cmpdsListAnotListB = 
        helper.getCmpdsByNsc(AnotBintList, "kunkelm");
//        this.cmpdsListAorListB = 
        helper.getCmpdsByNsc(AorBintList, "kunkelm");
//        this.cmpdsListAandListB = 
        helper.getCmpdsByNsc(AandBintList, "kunkelm");

        return "/webpages/listLogic?faces-redirect=true";

    }

    private static ArrayList<Integer> doSingleQuery(
            String keyword,
            Long aId,
            Long bId) {

        ArrayList<Integer> nscIntList = new ArrayList<Integer>();

        String templatedQuery = " select n.nsc "
                + " from cmpd_list cl, cmpd_list_member clm, nsc_cmpd n "
                + " where cl.cmpd_list_id = :aId "
                + " and clm.cmpd_list_fk = cl.id "
                + " and clm.cmpd_fk = n.id "
                + " " + keyword + " "
                + " select n.nsc "
                + " from cmpd_list cl, cmpd_list_member clm, nsc_cmpd n "
                + " where cl.cmpd_list_id = :bId "
                + " and clm.cmpd_list_fk = cl.id "
                + " and clm.cmpd_fk = n.id ";


        Session s = null;
        Transaction t = null;

        try {

            s = HibernateUtil.getSessionFactory().openSession();

            t = s.beginTransaction();

            Query q = s.createSQLQuery(templatedQuery);
            q.setParameter("aId", aId);
            q.setParameter("bId", bId);

            List results = q.list();

            int cntNsc = 0;
            for (Iterator itr = results.iterator(); itr.hasNext();) {
                cntNsc++;
                Integer nsc = (Integer) itr.next();
                nscIntList.add(nsc);
            }

            System.out.println("cntNsc: " + cntNsc);

            t.commit();
            s.close();

        } catch (Exception e) {
            t.rollback();
            s.close();
            e.printStackTrace();
        }

        return nscIntList;

    }

    public static void appendCmpdListMembers(CmpdListVO targetList, List<CmpdListMemberVO> forAppend, String currentUser) {

        // targetList

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        org.hibernate.Transaction tx = session.beginTransaction();

        Criteria clCrit = session.createCriteria(CmpdList.class);
        clCrit.add(Restrictions.eq("cmpdListId", targetList.getCmpdListId()));
        clCrit.add(Restrictions.eq("listOwner", currentUser));

        CmpdList target = (CmpdList) clCrit.uniqueResult();

        // members for appending

        ArrayList<Long> idsForAppend = new ArrayList<Long>();

        for (CmpdListMemberVO clmVO : forAppend) {
            idsForAppend.add(clmVO.getId());
        }

        Criteria clmCriteria = session.createCriteria(CmpdListMember.class);
        clmCriteria.add(Restrictions.in("id", idsForAppend));

        List<CmpdListMember> clml = clmCriteria.list();

        for (CmpdListMember clm : clml) {

            // check ownership
            if (clm.getCmpdList().getListOwner().equals(currentUser)) {

                CmpdListMember newClm = CmpdListMember.Factory.newInstance();

                newClm.setCmpd(clm.getCmpd());
                newClm.setListMemberComment(clm.getListMemberComment());
                clm.setCmpdList(target);
                session.persist(clm);

            } else {
                System.out.println(currentUser + " doesn't own list containing listMember: " + clm.getId());
            }

        }

        tx.commit();

    }

    public static void deleteCmpdListMembers(List<CmpdListMemberVO> forDelete, String currentUser) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        ArrayList<Long> idsForDelete = new ArrayList<Long>();

        for (CmpdListMemberVO clmVO : forDelete) {
            idsForDelete.add(clmVO.getId());
        }

        try {

            Transaction tx = session.beginTransaction();

            Criteria clCriteria = session.createCriteria(CmpdListMember.class);
            clCriteria.add(Restrictions.in("id", idsForDelete));

            List<CmpdListMember> clml = clCriteria.list();

            //have to programatically determine whether any ad_hoc_cmpds
            //and delete those

            Long cmpdId;
            ArrayList<Long> cmpdIdsForDelete = new ArrayList<Long>();

            for (CmpdListMember clm : clml) {

                // check ownership

                if (clm.getCmpdList().getListOwner().equals(currentUser)) {

                    cmpdId = (Long) session.getIdentifier(clm.getCmpd());
                    cmpdIdsForDelete.add(cmpdId);
                    // delete the CmpdListMember        
                    session.delete(clm);

                } else {
                    System.out.println(currentUser + " doesn't own list containing listMember: " + clm.getId());
                }

            }


            // MWK TODO handle ownership of AdHocCmpds?

            if (cmpdIdsForDelete.size() > 0) {

                // find and delete those cmpd with join to ahc
                Criteria ahcCriteria = session.createCriteria(AdHocCmpd.class);
                ahcCriteria.add(Restrictions.in("id", cmpdIdsForDelete));

                List<AdHocCmpd> ahcList = (List<AdHocCmpd>) ahcCriteria.list();

                for (AdHocCmpd ahc : ahcList) {
                    session.delete(ahc);
                }

            }

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static CmpdListVO getCmpdListByCmpdListId(Long cmpdListId, Boolean includeListMembers) {

        CmpdListVO rtnVO = new CmpdListVO();
        CmpdList entityCL = null;

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            org.hibernate.Transaction tx = session.beginTransaction();

            Criteria clCrit = session.createCriteria(CmpdList.class);
            clCrit.add(Restrictions.eq("cmpdListId", cmpdListId));
            clCrit.createAlias(("cmpdListMembers"), "m");
            clCrit.createAlias(("m.cmpd"), "c");

            if (includeListMembers) {

                clCrit.setFetchMode("cmpdListMembers", FetchMode.JOIN);

                clCrit.setFetchMode("c.cmpdFragmentStructureImpl", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdFragmentPChemImpl", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdBioAssay", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdInventory", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdAliases", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdProjects", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdPlates", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdPubChemSids", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdSets", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdFragments", FetchMode.JOIN);
                clCrit.setFetchMode("c.cmpdTargets", FetchMode.JOIN);

            }


            entityCL = (CmpdList) clCrit.uniqueResult();

            rtnVO = TransformAndroToVO.toCmpdListVO(entityCL, includeListMembers);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtnVO;

    }

    public static void adHocNscQuery() {

        List<Integer> nscIntList = new ArrayList<Integer>();

        nscIntList.add(123127);
        nscIntList.add(401005);
        nscIntList.add(705701);
        nscIntList.add(743380);

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.beginTransaction();

        Transaction tx = session.beginTransaction();

        // try native query to get around all of the joins from Cmpd to its compositie aggregations

        SQLQuery q = session.createSQLQuery("select nsc, id from nsc_cmpd where nsc in (:nscs)");
        q.setParameterList("nscs", nscIntList);

        List results = q.list();
        for (Iterator itr = results.iterator(); itr.hasNext();) {
            Object[] thisResult = (Object[]) itr.next();
            Integer nsc = (Integer) thisResult[0];
            Long id = ((BigInteger) thisResult[1]).longValue();
            System.out.println("nsc: " + nsc + " id: " + id);
        }
//        Criteria c = session.createCriteria(Cmpd.class);
//        c.add(Restrictions.in("nsc", nscIntList));
//        c.setFetchMode("cmpdFragmentStructureImpl", FetchMode.JOIN);
//        c.setFetchMode("cmpdFragmentPChemImpl", FetchMode.JOIN);
//        c.setFetchMode("cmpdBioAssay", FetchMode.JOIN);
//        c.setFetchMode("cmpdInventory", FetchMode.JOIN);
//        c.setFetchMode("cmpdAliases", FetchMode.JOIN);
//        c.setFetchMode("cmpdProjects", FetchMode.JOIN);
//        c.setFetchMode("cmpdPlates", FetchMode.JOIN);
//        c.setFetchMode("cmpdPubChemSids", FetchMode.JOIN);
//        c.setFetchMode("cmpdSets", FetchMode.JOIN);
//        c.setFetchMode("cmpdFragments", FetchMode.JOIN);
//        c.setFetchMode("cmpdTargets", FetchMode.JOIN);
//        List<Cmpd> cmpdList = (List<Cmpd>) c.list();

        session.get(CmpdImpl.class, 123127l);



        session.getTransaction().commit();

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
        CmpdListVO clVO = helper.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, "kunkelm");

        System.out.println(clVO.getCmpdListId() + " " + clVO.getListName() + " " + clVO.getListOwner());

        for (CmpdListMemberVO clmVO : clVO.getCmpdListMembers()) {
            CmpdVO cVO = clmVO.getCmpd();
            System.out.println(cVO.getId() + " " + cVO.getName() + " " + cVO.getNsc() + " " + cVO.getParentFragment().getCmpdFragmentPChem().getMf() + " " + cVO.getParentFragment().getCmpdFragmentStructure().getSmiles());
        }

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

            CmpdListVO clVO = TransformCmpdTableToVO.toCmpdListVO(cl, Boolean.TRUE);

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

    private void testExcelImages() throws Exception {

        try {

//create a new workbook
            Workbook wb = new XSSFWorkbook(); //or new HSSFWorkbook();

            //add picture data to this workbook.
            InputStream is = new FileInputStream("image1.jpeg");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            is.close();

            CreationHelper helper = wb.getCreationHelper();

            //create sheet
            Sheet sheet = wb.createSheet();

            // Create the drawing patriarch.  This is the top level container for all shapes. 
            Drawing drawing = sheet.createDrawingPatriarch();

            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(3);
            anchor.setRow1(2);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            //auto-size picture relative to its top-left corner
            pict.resize();

            //save workbook
            String file = "picture.xls";
            if (wb instanceof XSSFWorkbook) {
                file += "x";
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static void processPains() {

        //NSC641245
        //O=C(CC(=O)c1ccccc1NC1=Cc2ccccc2C(=O)C1=O)C(=O)NC1=C(Cl)C(=O)c2ccccc2C1=O

        //NSC700002
        //Cn1cc2c3c1C(=O)C(=O)C(Cl)=C3NCC2

        try {

            String[] curArray = SMILES_ARRAY;

            // parse mol
            SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

            SMARTSQueryTool queryTool = new SMARTSQueryTool("O=CO");

            for (int smilesCnt = 0; smilesCnt < curArray.length && smilesCnt < 50; smilesCnt++) {

                Molecule theMol = (Molecule) sp.parseSmiles(curArray[smilesCnt]);

                System.out.println(smilesCnt + " theMol: " + curArray[smilesCnt]);

                for (int j = 0; j < PAINS_ARRAY.length; j++) {

                    if (j % 2 == 1) {

                        queryTool.setSmarts(PAINS_ARRAY[j]);

                        if (queryTool.matches(theMol)) {

                            System.out.println("----------------------------Match: " + PAINS_ARRAY[j - 1] + " " + PAINS_ARRAY[j]);

//            int nmatch = querytool.countMatches();
//            List mappings = querytool.getMatchingAtoms();
//            for (int i = 0; i < nmatch; i++) {
//              List atomIndices = (List) mappings.get(i);
//            }

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void processPainsCached() {

        //NSC641245
        //O=C(CC(=O)c1ccccc1NC1=Cc2ccccc2C(=O)C1=O)C(=O)NC1=C(Cl)C(=O)c2ccccc2C1=O

        //NSC700002
        //Cn1cc2c3c1C(=O)C(=O)C(Cl)=C3NCC2

        try {

            String[] curArray = HITS_ARRAY;

            // ArrayList of queries

            ArrayList<SMARTSQueryTool> queryToolList = new ArrayList<SMARTSQueryTool>();
            System.out.println("START creating array of smartQueries.");
            for (int j = 0; j < PAINS_ARRAY.length; j++) {
                if (j % 2 == 1) {
                    SMARTSQueryTool sqt = new SMARTSQueryTool(PAINS_ARRAY[j]);
                    queryToolList.add(sqt);
                }
            }
            System.out.println("FINISHED creating array of smartQueries.");

            SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

            for (int smilesCnt = 0; smilesCnt < curArray.length && smilesCnt < 50; smilesCnt++) {

                Molecule theMol = (Molecule) sp.parseSmiles(curArray[smilesCnt]);

                System.out.println("theMol: " + curArray[smilesCnt]);

                for (SMARTSQueryTool queryTool : queryToolList) {

                    System.out.print(".");

                    if (queryTool.matches(theMol)) {

                        System.out.println();
                        System.out.println("Match: " + queryTool.getSmarts());

//            int nmatch = querytool.countMatches();
//            List mappings = querytool.getMatchingAtoms();
//            for (int i = 0; i < nmatch; i++) {
//              List atomIndices = (List) mappings.get(i);
//            }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String[] PAINS_ARRAY = {
        "thiaz_ene_A(128)", "[#6]-1(=[#6](-[$([#1]),$([#6](-[#1])-[#1]),$([#6]=[#8])])-[#16]-[#6](-[#7]-1-[$([#1]),$([#6]-[#1]),$([#6]:[#6])])=[#7;!R])-[$([#6](-[#1])-[#1]),$([#6]:[#6])]",
        "pyrrole_A(118)", "n2(-[#6]:1:[!#1]:[#6]:[#6]:[#6]:[#6]:1)c(cc(c2-[#6;X4])-[#1])-[#6;X4]",
        "catechol_A(92)", "c:1:c:c(:c(:c:c:1)-[#8]-[#1])-[#8]-[#1]",
        "ene_five_het_B(90)", "[#6]-1(=[#6])-[#6](-[#7]=[#6]-[#16]-1)=[#8]",
        "imine_one_fives(89)", "[#6]-1=[!#1]-[!#6&!#1]-[#6](-[#6]-1=[!#6&!#1;!R])=[#8]",
        "ene_five_het_C(85)", "[#6]-1(-[#6](-[#6]=[#6]-[!#6&!#1]-1)=[#6])=[!#6&!#1]",
        "hzone_pipzn(79)", "[#6]-[#7]-1-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])(-[#1])-[#6]-1(-[#1])-[#1])-[#7]=[#6](-[#1])-[#6]:[!#1]",
        "keto_keto_beta_A(68)", "c:1-2:c(:c:c:c:c:1)-[#6](=[#8])-[#6;X4]-[#6]-2=[#8]",
        "hzone_pyrrol(64)", "n1(-[#6])c(c(-[#1])c(c1-[#6]=[#7]-[#7])-[#1])-[#1]",
        "ene_one_ene_A(57)", "[#6]=!@[#6](-[!#1])-@[#6](=!@[!#6&!#1])-@[#6](=!@[#6])-[!#1]",
        "cyano_ene_amine_A(56)", "[#6](-[#6]#[#7])(-[#6]#[#7])-[#6](-[#7](-[#1])-[#1])=[#6]-[#6]#[#7]",
        "ene_five_one_A(55)", "c:1-2:c(:c:c:c:c:1)-[#6](=[#8])-[#6](=[#6])-[#6]-2=[#8]",
        "cyano_pyridone_A(54)", "[#6]-1(=[!#1]-[!#1]=[!#1]-[#7](-[#6]-1=[#16])-[#1])-[#6]#[#7]",
        "anil_alk_ene(51)", "c:1:c:c-2:c(:c:c:1)-[#6]-3-[#6](-[#6]-[#7]-2)-[#6]-[#6]=[#6]-3",
        "hzone_anil_di_alk(35)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])=[#7]-[#7]-[$([#6](=[#8])-[#6](-[#1])(-[#1])-[#16]-[#6]:[#7]),$([#6](=[#8])-[#6](-[#1])(-[#1])-[!#1]:[!#1]:[#7]),$([#6](=[#8])-[#6]:[#6]-[#8]-[#1]),$([#6]:[#7]),$([#6](-[#1])(-[#1])-[#6](-[#1])-[#8]-[#1])])-[#1])-[#1]",
        "rhod_sat_A(33)", "[#7]-1-[#6](=[#16])-[#16]-[#6;X4]-[#6]-1=[#8]",
        "amino_acridine_A(46)", "c:1:c:2:c(:c:c:c:1):n:c:3:c(:c:2-[#7]):c:c:c:c:3",
        "ene_five_het_D(46)", "[#6]-1(=[#6])-[#6](=[#8])-[#7]-[#7]-[#6]-1=[#8]",
        "thiophene_amino_Aa(45)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:s:1)-[!#1])-[!#1])-[#6]=[#8]",
        "ene_five_het_E(44)", "[#7]-[#6]=!@[#6]-2-[#6](=[#8])-c:1:c:c:c:c:c:1-[!#6&!#1]-2",
        "sulfonamide_A(43)", "c:1(:c(:c(:c(:c(:c:1-[#8]-[#1])-[F,Cl,Br,I])-[#1])-[F,Cl,Br,I])-[#1])-[#16](=[#8])(=[#8])-[#7]",
        "thio_ketone(43)", "[#6]-[#6](=[#16])-[#6]",
        "sulfonamide_B(41)", "c:1:c:c(:c:c:c:1-[#8]-[#1])-[#7](-[#1])-[#16](=[#8])=[#8]",
        "anil_OH_alk_A(8)", "c:1:c(:c:c:c:c:1)-[#6](-[#1])(-[#1])-[#7](-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#1])-[#1])-[#1]",
        "het_thio_656c(1)", "c:1:c(:c:c:c:c:1)-[#6]-4=[#7]-[#7]:2:[#6](:[#7+]:c:3:c:2:c:c:c:c:3)-[#16]-[#6;X4]-4",
        "anil_no_alk(40)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[$([#8]),$([#7]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#7](-[#1])-[#1]",
        "thiophene_amino_Ab(40)", "[$([#1]),$([#6](-[#1])-[#1]),$([#6]:[#6])]-c:1:c(:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-[#6])-[#6](=[#8])-[#8])-[$([#6]:1:[#6]:[#6]:[#6]:[#6]:[#6]:1),$([#6]:1:[#16]:[#6]:[#6]:[#6]:1)]",
        "het_pyridiniums_A(39)", "[#7+]:1(:[#6]:[#6]:[!#1]:c:2:c:1:c(:c(-[$([#1]),$([#7])]):c:c:2)-[#1])-[$([#6](-[#1])(-[#1])-[#1]),$([#8;X1]),$([#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1]),$([#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#8]-[#1]),$([#6](-[#1])(-[#1])-[#6](=[#8])-[#6]),$([#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6]),$([#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#1])]",
        "anthranil_one_A(38)", "c:1:c:c:c:c(:c:1-[#7](-[#1])-[!$([#6]=[#8])])-[#6](-[#6]:[#6])=[#8]",
        "cyano_imine_A(37)", "[#7](-[#1])-[#7]=[#6](-[#6]#[#7])-[#6]=[!#6&!#1;!R]",
        "diazox_sulfon_A(36)", "[#7](-c:1:c:c:c:c:c:1)-[#16](=[#8])(=[#8])-[#6]:2:[#6]:[#6]:[#6]:[#6]:3:[#7]:[$([#8]),$([#16])]:[#7]:[#6]:2:3",
        "het_5_ene(1)", "[#6]-2(=[#8])-[#6](=[#6](-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#7]=[#6](-c:1:c:c:c:c:c:1)-[#8]-2",
        "hzone_enamin(30)", "[#7](-[#1])-[#7]=[#6]-[#6](-[$([#1]),$([#6])])=[#6](-[#6])-!@[$([#7]),$([#8]-[#1])]",
        "pyrrole_B(29)", "n2(-[#6]:1:[!#1]:[#6]:[#6]:[#6]:[#6]:1)c(cc(c2-[#6]:[#6])-[#1])-[#6;X4]",
        "thiophene_hydroxy(28)", "s1ccc(c1)-[#8]-[#1]",
        "cyano_pyridone_B(27)", "[#6]-1(=[#6](-[#6](=[#8])-[#7]-[#6](=[#7]-1)-[!#6&!#1])-[#6]#[#7])-[#6]",
        "imine_one_sixes(27)", "[#6]-1(-[#6](=[#8])-[#7]-[#6](=[#8])-[#7]-[#6]-1=[#8])=[#7]",
        "dyes5A(27)", "[#6](-[#1])(-[#1])-[#7]([#6]:[#6])~[#6][#6]=,:[#6]-[#6]~[#6][#7]",
        "naphth_amino_A(25)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7]-[#6]=[#7]-3",
        "naphth_amino_B(25)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7](-[#6;X4]-[#7]-3-[#1])-[#1]",
        "ene_one_ester(24)", "[#6]-[#6](=[#8])-[#6](-[#1])=[#6](-[#7](-[#1])-[#6])-[#6](=[#8])-[#8]-[#6]",
        "thio_dibenzo(23)", "[#16]=[#6]-1-[#6]=,:[#6]-[!#6&!#1]-[#6]=,:[#6]-1",
        "cyano_cyano_A(23)", "[#6](-[#6]#[#7])(-[#6]#[#7])-[#6](-[$([#6]#[#7]),$([#6]=[#7])])-[#6]#[#7]",
        "hzone_acyl_naphthol(22)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(:c(:c:2-[#1])-[#8]-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6])-[#1])-[#1])-[#1])-[#1])-[#1]",
        "het_65_A(21)", "[#8]=[#6]-c2c1nc(-[#6](-[#1])-[#1])cc(-[#8]-[#1])n1nc2",
        "imidazole_A(19)", "n:1:c(:n(:c(:c:1-c:2:c:c:c:c:c:2)-c:3:c:c:c:c:c:3)-[#1])-[#6]:[!#1]",
        "anil_OC_alk_D(2)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c:c:c:2-[$([#6](-[#1])-[#1]),$([#8]-[#6](-[#1])-[#1])]",
        "ene_cyano_A(19)", "[#6](-[#6]#[#7])(-[#6]#[#7])=[#6]-c:1:c:c:c:c:c:1",
        "anthranil_acid_A(19)", "c:1(:c:c:c:c:c:1-[#7](-[#1])-[#7]=[#6])-[#6](=[#8])-[#8]-[#1]",
        "dyes3A(19)", "[#7+]([#6]:[#6])=,:[#6]-[#6](-[#1])=[#6]-[#7](-[#6;X4])-[#6]",
        "dhp_bis_amino_CN(19)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6](-[#7](-[#1])-[#1])-[#16]-1)-[#6]#[#7]",
        "het_6_tetrazine(18)", "[#7]~[#6]:1:[#7]:[#7]:[#6](:[$([#7]),$([#6]-[#1]),$([#6]-[#7]-[#1])]:[$([#7]),$([#6]-[#7])]:1)-[$([#7]-[#1]),$([#8]-[#6](-[#1])-[#1])]",
        "ene_one_hal(17)", "[#6]-[#6]=[#6](-[F,Cl,Br,I])-[#6](=[#8])-[#6]",
        "cyano_imine_B(17)", "[#6](-[#6]#[#7])(-[#6]#[#7])=[#7]-[#7](-[#1])-c:1:c:c:c:c:c:1",
        "thiaz_ene_B(17)", "[#6]-1(=[#6](-!@[#6](=[#8])-[#7]-[#6](-[#1])-[#1])-[#16]-[#6](-[#7]-1-[$([#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1]),$([#6]:[#6])])=[#16])-[$([#7]-[#6](=[#8])-[#6]:[#6]),$([#7](-[#1])-[#1])]",
        "het_65_B(7)", "[!#1]:1:[!#1]-2:[!#1](:[!#1]:[!#1]:[!#1]:1)-[#7](-[#1])-[#7](-[#6]-2=[#8])-[#6]",
        "keto_keto_beta_C(7)", "c:1:c:c-2:c(:c:c:1)-[#6](=[#6](-[#6]-2=[#8])-[#6])-[#8]-[#1]",
        "ene_rhod_B(16)", "[#16]-1-[#6](=[#8])-[#7]-[#6](=[#8])-[#6]-1=[#6](-[#1])-[$([#6]-[#35]),$([#6]:[#6](-[#1]):[#6](-[F,Cl,Br,I]):[#6]:[#6]-[F,Cl,Br,I]),$([#6]:[#6](-[#1]):[#6](-[#1]):[#6]-[#16]-[#6](-[#1])-[#1]),$([#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]-[#8]-[#6](-[#1])-[#1]),$([#6]:1:[#6](-[#6](-[#1])-[#1]):[#7](-[#6](-[#1])-[#1]):[#6](-[#6](-[#1])-[#1]):[#6]:1)]",
        "thio_carbonate_A(15)", "[#8]-1-[#6](-[#16]-c:2:c-1:c:c:c(:c:2)-[$([#7]),$([#8])])=[$([#8]),$([#16])]",
        "cyano_pyridone_C(11)", "[#6]-1(-[#6](=[#6](-[#6]#[#7])-[#6](~[#8])~[#7]~[#6]-1~[#8])-[#6](-[#1])-[#1])=[#6](-[#1])-[#6]:[#6]",
        "anil_di_alk_furan_A(15)", "[#7](-[#6](-[#1])-[#1])(-[#6](-[#1])-[#1])-c:1:c(:c(:c(:o:1)-[#6]=[#7]-[#7](-[#1])-[#6]=[!#6&!#1])-[#1])-[#1]",
        "ene_five_het_F(15)", "c:1(:c:c:c:c:c:1)-[#6](-[#1])=!@[#6]-3-[#6](=[#8])-c:2:c:c:c:c:c:2-[#16]-3",
        "anil_di_alk_F(14)", "c:1:c:c(:c:c:c:1-[#6;X4]-c:2:c:c:c(:c:c:2)-[#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])])-[#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])]",
        "hzone_anil(14)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#1])-[#1])-[#1])-[#6]=[#7]-[#7]-[#1]",
        "het_5_pyrazole_OH(14)", "c1(nn(c(c1-[$([#1]),$([#6]-[#1])])-[#8]-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#1])-[#1])-[#1])-[#6;X4]",
        "het_thio_666_A(13)", "c:2(:c:1-[#16]-c:3:c(-[#7](-c:1:c(:c(:c:2-[#1])-[#1])-[#1])-[$([#1]),$([#6](-[#1])(-[#1])-[#1]),$([#6](-[#1])(-[#1])-[#6]-[#1])]):c(:c(~[$([#1]),$([#6]:[#6])]):c(:c:3-[#1])-[$([#1]),$([#7](-[#1])-[#1]),$([#8]-[#6;X4])])~[$([#1]),$([#7](-[#1])-[#6;X4]),$([#6]:[#6])])-[#1]",
        "ene_rhod_D(8)", "[#16]-1-[#6](=!@[#7]-[$([#1]),$([#7](-[#1])-[#6]:[#6])])-[#7](-[$([#1]),$([#6]:[#7]:[#6]:[#6]:[#16])])-[#6](=[#8])-[#6]-1=[#6](-[#1])-[#6]:[#6]-[$([#17]),$([#8]-[#6]-[#1])]",
        "styrene_A(13)", "[#6]-2-[#6]-c:1:c(:c:c:c:c:1)-[#6](-c:3:c:c:c:c:c-2:3)=[#6]-[#6]",
        "ene_rhod_C(13)", "[#16]-1-[#6](=[#7]-[#6]:[#6])-[#7](-[$([#1]),$([#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#8]),$([#6]:[#6])])-[#6](=[#8])-[#6]-1=[#6](-[#1])-[$([#6]:[#6]:[#6]-[#17]),$([#6]:[!#6&!#1])]",
        "dhp_amino_CN_A(13)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6](-[#6]=[#6])-[#8]-1)-[#6](-[#1])-[#1]",
        "cyano_imine_C(12)", "[#8]=[#16](=[#8])-[#6](-[#6]#[#7])=[#7]-[#7]-[#1]",
        "thio_urea_A(12)", "c:1:c:c:c:c:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2",
        "thiophene_amino_B(12)", "c:1:c(:c:c:c:c:1)-[#7](-[#1])-c:2:c(:c(:c(:s:2)-[$([#6]=[#8]),$([#6]#[#7]),$([#6](-[#8]-[#1])=[#6])])-[#7])-[$([#6]#[#7]),$([#6](:[#7]):[#7])]",
        "keto_keto_beta_B(12)", "[#6;X4]-1-[#6](=[#8])-[#7]-[#7]-[#6]-1=[#8]",
        "keto_phenone_A(11)", "c:1:c-3:c(:c:c:c:1)-[#6]:2:[#7]:[!#1]:[#6]:[#6]:[#6]:2-[#6]-3=[#8]",
        "thiaz_ene_C(11)", "[#6]-1(=[#6](-!@[#6]=[#7])-[#16]-[#6](-[#7]-1)=[#8])-[$([F,Cl,Br,I]),$([#7+](:[#6]):[#6])]",
        "hzone_thiophene_A(11)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1]):[!#6&!#1]:[#6](:[#6]:2-[#6](-[#1])=[#7]-[#7](-[#1])-[$([#6]:1:[#7]:[#6]:[#6](-[#1]):[#16]:1),$([#6]:[#6](-[#1]):[#6]-[#1]),$([#6]:[#7]:[#6]:[#7]:[#6]:[#7]),$([#6]:[#7]:[#7]:[#7]:[#7])])-[$([#1]),$([#8]-[#1]),$([#6](-[#1])-[#1])]",
        "ene_quin_methide(10)", "[!#1]:[!#1]-[#6](-[$([#1]),$([#6]#[#7])])=[#6]-1-[#6]=,:[#6]-[#6](=[$([#8]),$([#7;!R])])-[#6]=,:[#6]-1",
        "het_thio_676_A(10)", "c:1:c:c-2:c(:c:c:1)-[#6]-[#6](-c:3:c(-[#16]-2):c(:c(-[#1]):c(:c:3-[#1])-[$([#1]),$([#8]),$([#16;X2]),$([#6;X4]),$([#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])])])-[#1])-[#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])]",
        "ene_five_het_G(10)", "[#6]-1(=[#8])-[#6](=[#6](-[#1])-[$([#6]:1:[#6]:[#6]:[#6]:[#6]:[#6]:1),$([#6]:1:[#6]:[#6]:[#6]:[!#6&!#1]:1)])-[#7]=[#6](-[!#1]:[!#1]:[!#1])-[$([#16]),$([#7]-[!#1]:[!#1])]-1",
        "acyl_het_A(9)", "[#7+](:[!#1]:[!#1]:[!#1])-[!#1]=[#8]",
        "anil_di_alk_G(9)", "[#6;X4]-[#7](-[#6;X4])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6]2=,:[#7][#6]:[#6]:[!#1]2)-[#1])-[#1]",
        "thio_imide_A(1)", "c:1:c(:c:c:c:c:1)-[#7]-2-[#6](=[#8])-[#6](=[#6](-[#1])-[#6]-2=[#8])-[#16]-c:3:c:c:c:c:c:3",
        "dhp_keto_A(9)", "[#7]-1(-[$([#6;X4]),$([#1])])-[#6]=,:[#6](-[#6](=[#8])-[#6]:[#6]:[#6])-[#6](-[#6])-[#6](=[#6]-1-[#6](-[#1])(-[#1])-[#1])-[$([#6]=[#8]),$([#6]#[#7])]",
        "thio_urea_B(9)", "c:1:c:c:c:c:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2",
        "anil_alk_bim(9)", "c:1:3:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c:c:c:2)-[#1]):n:c(-[#1]):n:3-[#6]",
        "imine_imine_A(9)", "c:1:c:c-2:c(:c:c:1)-[#7]=[#6]-[#6]-2=[#7;!R]",
        "thio_urea_C(9)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-[#6](=[#8])-[#6]-2:[!#1]:[!#6&!#1]:[#6]:[#6]-2",
        "imine_one_fives_B(9)", "[#7;!R]=[#6]-2-[#6](=[#8])-c:1:c:c:c:c:c:1-[#16]-2",
        "ene_rhod_E(8)", "[#16]-1-[#6](=[#8])-[#7]-[#6](=[#16])-[#6]-1=[#6](-[#1])-[#6]:[#6]",
        "dhp_amino_CN_B(9)", "[$([#7](-[#1])-[#1]),$([#8]-[#1])]-[#6]-2=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-c:1:c(:n(-[#6]):n:c:1)-[#8]-2",
        "anil_OC_no_alk_A(8)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:n:c:1-[#1])-[#8]-c:2:c:c:c:c:c:2)-[#1])-[#1]",
        "het_thio_66_one(8)", "[#6](=[#8])-[#6]-1=[#6]-[#7]-c:2:c(-[#16]-1):c:c:c:c:2",
        "styrene_B(8)", "c:1:c:c-2:c(:c:c:1)-[#6](-c:3:c(-[$([#16;X2]),$([#6;X4])]-2):c:c:c(:c:3)-[$([#1]),$([#17]),$([#6;X4])])=[#6]-[#6]",
        "het_thio_5_A(8)", "[#6](-[#1])(-[#1])-[#16;X2]-c:1:n:c(:c(:n:1-!@[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2)-[#1]",
        "anil_di_alk_ene_A(8)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6]-2=[#6](-[#1])-c:1:c(:c:c:c:c:1)-[#16;X2]-c:3:c-2:c:c:c:c:3",
        "pyrrole_C(8)", "n1(-[#6;X4])c(c(-[#1])c(c1-[#6]:[#6])-[#1])-[#6](-[#1])-[#1]",
        "thio_urea_D(8)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-c:2:c:c:c:c:c:2",
        "thiaz_ene_D(8)", "[#7](-c:1:c:c:c:c:c:1)-c2[n+]c(cs2)-c:3:c:c:c:c:c:3",
        "ene_rhod_F(8)", "n:1:c:c:c(:c:1-[#6](-[#1])-[#1])-[#6](-[#1])=[#6]-2-[#6](=[#8])-[#7]-[#6](=[!#6&!#1])-[#7]-2",
        "thiaz_ene_E(8)", "[#6]-1(=[#6](-[#6](-[#1])(-[#6])-[#6])-[#16]-[#6](-[#7]-1-[$([#1]),$([#6](-[#1])-[#1])])=[#8])-[#16]-[#6;R]",
        "keto_keto_gamma(5)", "[#8]=[#6]-1-[#6;X4]-[#6]-[#6](=[#8])-c:2:c:c:c:c:c-1:2",
        "het_66_A(7)", "c:2:c:c:1:n:n:c(:n:c:1:c:c:2)-[#6](-[#1])(-[#1])-[#6]=[#8]",
        "thio_urea_E(7)", "c:1:c:c:c:c:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:n:c:c:c:c:2",
        "thiophene_amino_C(7)", "[#6](-[#1])-[#6](-[#1])(-[#1])-c:1:c(:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-[#6]-[#6]-[#6]=[#8])-[$([#6](=[#8])-[#8]),$([#6]#[#7])])-[#6](-[#1])-[#1]",
        "hzone_phenone(7)", "[#6](-c:1:c(:c(:c(:c:c:1-[#1])-[$([#6;X4]),$([#1])])-[#1])-[#1])(-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[$([#1]),$([#17])])-[#1])-[#1])=[$([#7]-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]),$([#7]-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]),$([#7]-[#7](-[#1])-[#6](=[#7]-[#1])-[#7](-[#1])-[#1]),$([#6](-[#1])-[#7])]",
        "ene_rhod_G(7)", "[#8](-[#1])-[#6](=[#8])-c:1:c:c(:c:c:c:1)-[#6]:[!#1]:[#6]-[#6](-[#1])=[#6]-2-[#6](=[!#6&!#1])-[#7]-[#6](=[!#6&!#1])-[!#6&!#1]-2",
        "ene_cyano_B(7)", "[#6]-1(=[#6]-[#6](-c:2:c:c(:c(:n:c-1:2)-[#7](-[#1])-[#1])-[#6]#[#7])=[#6])-[#6]#[#7]",
        "dhp_amino_CN_C(7)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6](-[#6]:[#6])-[#8]-1)-[#6]#[#7]",
        "quinone_B(5)", "c:1:c:c-2:c(:c:c:1)-[#6](-c3cccc4noc-2c34)=[#8]",
        "het_5_A(7)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#6]=[#8])-[#6;X4]-[#6]-2=[#8]",
        "ene_five_het_H(6)", "[#7]-1=[#6]-[#6](-[#6](-[#7]-1)=[#16])=[#6]",
        "thio_amide_A(6)", "c1(coc(c1-[#1])-[#6](=[#16])-[#7]-2-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[!#1]-[#6](-[#1])(-[#1])-[#6]-2(-[#1])-[#1])-[#1]",
        "ene_cyano_C(6)", "[#6]=[#6](-[#6]#[#7])-[#6](=[#7]-[#1])-[#7]-[#7]",
        "hzone_furan_A(6)", "c:1(:c(:c(:c(:o:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](-[$([#1]),$([#6](-[#1])-[#1])])=[#7]-[#7](-[#1])-c:2:n:c:c:s:2",
        "anil_di_alk_H(6)", "c:1(:c(:c(:c(:c(:c:1-[#7](-[#1])-[#16](=[#8])(=[#8])-[#6]:2:[#6]:[!#1]:[#6]:[#6]:[#6]:2)-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
        "het_65_C(6)", "n2c1ccccn1c(c2-[$([#6](-[!#1])=[#6](-[#1])-[#6]:[#6]),$([#6]:[#8]:[#6])])-[#7]-[#6]:[#6]",
        "thio_urea_F(6)", "[#6]-1-[#7](-[#1])-[#7](-[#1])-[#6](=[#16])-[#7]-[#7]-1-[#1]",
        "ene_five_het_I(6)", "c:1(:c:c:c:o:1)-[#6](-[#1])=!@[#6]-3-[#6](=[#8])-c:2:c:c:c:c:c:2-[!#6&!#1]-3",
        "het_6_pyridone_OH(5)", "[#8](-[#1])-c:1:n:c(:c:c:c:1)-[#8]-[#1]",
        "hzone_naphth_A(5)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(:c(:c:2-[#1])-[#1])-[#6]=[#7]-[#7](-[#1])-[$([#6]:[#6]),$([#6]=[#16])])-[#1])-[#1])-[#1])-[#1])-[#1]",
        "thio_ester_A(5)", "[#6]-1=[#6](-[#16]-[#6](-[#6]=[#6]-1)=[#16])-[#7]",
        "ene_misc_A(5)", "[#6]-1=[#6]-[#6](-[#8]-[#6]-1-[#8])(-[#8])-[#6]",
        "cyano_pyridone_D(5)", "[#8]=[#6]-1-[#6](=[#6]-[#6](=[#7]-[#7]-1)-[#6]=[#8])-[#6]#[#7]",
        "het_65_Db(5)", "c3cn1c(nc(c1-[#7]-[#6])-c:2:c:c:c:c:n:2)cc3",
        "het_666_A(5)", "[#7]-2-c:1:c:c:c:c:c:1-[#6](=[#7])-c:3:c-2:c:c:c:c:3",
        "diazox_sulfon_B(5)", "c:1:c(:c:c:c:c:1)-[#7]-2-[#6](-[#1])-[#6](-[#1])-[#7](-[#6](-[#1])-[#6]-2-[#1])-[#16](=[#8])(=[#8])-c:3:c:c:c:c:4:n:s:n:c:3:4",
        "dhp_amino_CN_D(5)", "[#7](-[#1])(-[#1])-[#6]-2=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-c:1:c(:c:c:s:1)-[#8]-2",
        "anil_NH_alk_A(5)", "c:1(:c(:c-2:c(:c(:c:1-[#1])-[#1])-[#7](-[#6](-[#7]-2-[#1])=[#8])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])-[#1]",
        "sulfonamide_C(5)", "c:1(:c(:c-3:c(:c(:c:1-[#7](-[#1])-[#16](=[#8])(=[#8])-c:2:c:c:c(:c:c:2)-[!#6&!#1])-[#1])-[#8]-[#6](-[#8]-3)(-[#1])-[#1])-[#1])-[#1]",
        "het_thio_N_55(5)", "[#6](-[#1])-[#6]:2:[#7]:[#7](-c:1:c:c:c:c:c:1):[#16]:3:[!#6&!#1]:[!#1]:[#6]:[#6]:2:3",
        "keto_keto_beta_D(5)", "[#8]=[#6]-[#6]=[#6](-[#1])-[#8]-[#1]",
        "ene_rhod_H(5)", "[#7]-1-2-[#6](=[#7]-[#6](=[#8])-[#6](=[#7]-1)-[#6](-[#1])-[#1])-[#16]-[#6](=[#6](-[#1])-[#6]:[#6])-[#6]-2=[#8]",
        "imine_ene_A(5)", "[#6]:[#6]-[#6](-[#1])=[#6](-[#1])-[#6](-[#1])=[#7]-[#7](-[#6;X4])-[#6;X4]",
        "het_thio_656a(5)", "c:1:3:c(:c:c:c:c:1):c:2:n:n:c(-[#16]-[#6](-[#1])(-[#1])-[#6]=[#8]):n:c:2:n:3-[#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1]",
        "tert_butyl_A(2)", "[#6](-[#1])(-[#1])(-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-c:1:c(:c:c(:c(:c:1-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-[#6](-[#1])(-[#1])-[#1])-[#8]-[#6](-[#1])-[#7])-[#1]",
        "pyrrole_D(5)", "n1(-[#6])c(c(-[#1])c(c1-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](=[#16])-[#7]-[#1])-[#1])-[#1]",
        "pyrrole_E(5)", "n2(-[#6]:1:[!#1]:[!#6&!#1]:[!#1]:[#6]:1-[#1])c(c(-[#1])c(c2-[#6;X4])-[#1])-[#6;X4]",
        "thio_urea_G(5)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-[#6]([#7;R])[#7;R]",
        "anisol_A(5)", "c:1(:c(:c(:c(:c(:c:1-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#8]-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[$([#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]),$([#6](-[#1])(-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](=[#16])-[#7]-[#1])])-[#1])-[#8]-[#6](-[#1])-[#1]",
        "pyrrole_F(5)", "n2(-[#6]:1:[#6](-[#6]#[#7]):[#6]:[#6]:[!#6&!#1]:1)c(c(-[#1])c(c2)-[#1])-[#1]",
        "coumarin_D(1)", "c:1:c:c(:c:c-2:c:1-[#6](=[#6](-[#1])-[#6](=[#8])-[#8]-2)-c:3:c:c:c:c:c:3)-[#8]-[#6](-[#1])(-[#1])-[#6]:[#8]:[#6]",
        "thiazole_amine_A(4)", "[#7](-[#1])-c:1:n:c(:c:s:1)-c:2:c:n:c(-[#7](-[#1])-[#1]):s:2",
        "het_6_imidate_A(4)", "[#7]=[#6]-1-[#7](-[#1])-[#6](=[#6](-[#7]-[#1])-[#7]=[#7]-1)-[#7]-[#1]",
        "anil_OC_no_alk_B(4)", "c:1:c(:c:2:c(:c:c:1):c:c:c:c:2)-[#8]-c:3:c(:c(:c(:c(:c:3-[#1])-[#1])-[#7]-[#1])-[#1])-[#1]",
        "styrene_C(4)", "c:1:c:c-2:c(:c:c:1)-[#6]-[#16]-c3c(-[#6]-2=[#6])ccs3",
        "azulene(4)", "c:2:c:c:c:1:c(:c:c:c:1):c:c:2",
        "furan_acid_A(4)", "c:1(:c(:c(:c(:o:1)-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#8]-[#6]:[#6])-[#1])-[#6](=[#8])-[#8]-[#1]",
        "cyano_pyridone_E(4)", "[!#1]:[#6]-[#6]-1=[#6](-[#1])-[#6](=[#6](-[#6]#[#7])-[#6](=[#8])-[#7]-1-[#1])-[#6]:[#8]",
        "anil_alk_thio(4)", "[#6]-1-3=[#6](-[#6](-[#7]-c:2:c:c:c:c:c-1:2)(-[#6])-[#6])-[#16]-[#16]-[#6]-3=[!#1]",
        "anil_di_alk_I(4)", "c:1(:c(:c(:c(:c(:c:1-[#7](-[#1])-[#6](=[#8])-c:2:c:c:c:c:c:2)-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
        "het_thio_6_furan(4)", "[#6](-[#1])(-[#1])-[#16;X2]-c:1:n:n:c(:c(:n:1)-c:2:c(:c(:c(:o:2)-[#1])-[#1])-[#1])-c:3:c(:c(:c(:o:3)-[#1])-[#1])-[#1]",
        "het_thio_N_65A(3)", "[#7]-2-[#16]-[#6]-1=[#6](-[#6]:[#6]-[#7]-[#6]-1)-[#6]-2=[#16]",
        "anil_di_alk_ene_B(4)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6]-2=[#6]-c:1:c(:c:c:c:c:1)-[#6]-2(-[#1])-[#1]",
        "thio_ester_B(4)", "[#6]:[#6]-[#6](=[#16;X1])-[#16;X2]-[#6](-[#1])-[$([#6](-[#1])-[#1]),$([#6]:[#6])]",
        "anil_di_alk_M(1)", "c:1(:c:4:c(:n:c(:c:1-[#6](-[#1])(-[#1])-[#7]-3-c:2:c(:c(:c(:c(:c:2-[#6](-[#1])(-[#1])-[#6]-3(-[#1])-[#1])-[#1])-[#1])-[#1])-[#1])-[#1]):c(:c(:c(:c:4-[#1])-[#1])-[#1])-[#1])-[#1]",
        "imine_one_B(4)", "[#7](-[#1])(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#6](=[#8])-[#6](-[#1])-[#1])-[#7](-[#1])-[$([#7]-[#1]),$([#6]:[#6])]",
        "anil_OC_alk_A(4)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1]):o:c:3:c(-[#1]):c(:c(-[#8]-[#6](-[#1])-[#1]):c(:c:2:3)-[#1])-[#7](-[#1])-[#6](-[#1])-[#1]",
        "ene_five_het_J(4)", "[#16]=[#6]-1-[#7](-[#1])-[#6]=[#6]-[#6]-2=[#6]-1-[#6](=[#8])-[#8]-[#6]-2=[#6]-[#1]",
        "pyrrole_G(4)", "n2(-c:1:c(:c:c(:c(:c:1)-[#1])-[$([#7](-[#1])-[#1]),$([#6]:[#7])])-[#1])c(c(-[#1])c(c2-[#1])-[#1])-[#1]",
        "ene_five_het_K(4)", "n1(-[#6])c(c(-[#1])c(c1-[#6](-[#1])=[#6]-2-[#6](=[#8])-[!#6&!#1]-[#6]=,:[!#1]-2)-[#1])-[#1]",
        "cyano_ene_amine_B(4)", "[#6]=[#6]-[#6](-[#6]#[#7])(-[#6]#[#7])-[#6](-[#6]#[#7])=[#6]-[#7](-[#1])-[#1]",
        "colchicine_A(3)", "[#6]-1(-[#6](=[#6]-[#6]=[#6]-[#6]=[#6]-1)-[#7]-[#1])=[#7]-[#6]",
        "ene_five_het_L(4)", "[#8]=[#6]-3-[#6](=!@[#6](-[#1])-c:1:c:n:c:c:1)-c:2:c:c:c:c:c:2-[#7]-3",
        "hzone_thiophene_B(4)", "c:1(:c(:c(:c(:s:1)-[#1])-[#1])-[$([#1]),$([#6](-[#1])-[#1])])-[#6](-[#1])=[#7]-[#7](-[#1])-c:2:c:c:c:c:c:2",
        "dhp_amino_CN_E(4)", "[#6](-[#1])(-[#1])-[#16;X2]-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](-[#6]#[#7])-[#6](=[#8])-[#7]-1",
        "het_5_B(4)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#7](-[#1])-[#6]=[#8])-[#6](-[#1])(-[#1])-[#6]-2=[#8]",
        "imine_imine_B(3)", "[#6]:[#6]-[#6](-[#1])=[#6](-[#1])-[#6](-[#1])=[#7]-[#7]=[#6]",
        "thiazole_amine_B(3)", "c:1(:c:c:c(:c:c:1)-[#6](-[#1])-[#1])-c:2:c(:s:c(:n:2)-[#7](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#1]",
        "pyrrole_H(3)", "n1-2cccc1-[#6]=[#7](-[#6])-[#6]-[#6]-2",
        "imine_ene_one_A(3)", "[#6]-2(-[#6]=[#7]-c:1:c:c:c:c:c:1-[#7]-2)=[#6](-[#1])-[#6]=[#8]",
        "diazox_A(3)", "[#8](-c:1:c:c:c:c:c:1)-c:3:c:c:2:n:o:n:c:2:c:c:3",
        "ene_one_A(3)", "[!#1]:1:[!#1]:[!#1]:[!#1](:[!#1]:[!#1]:1)-[#6](-[#1])=[#6](-[#1])-[#6](-[#7]-c:2:c:c:c:3:c(:c:2):c:c:c(:n:3)-[#7](-[#6])-[#6])=[#8]",
        "anil_OC_no_alk_C(3)", "[#7](-[#1])(-[#1])-c:1:c(:c:c:c:n:1)-[#8]-[#6](-[#1])(-[#1])-[#6]:[#6]",
        "thiazol_SC_A(3)", "[#6]-[#16;X2]-c:1:n:c(:c:s:1)-[#1]",
        "het_666_B(3)", "c:1:c-3:c(:c:c:c:1)-[#7](-c:2:c:c:c:c:c:2-[#8]-3)-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
        "furan_A(3)", "c:1(:c(:c(:c(:o:1)-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#8]-[#1])-[#6]#[#6]-[#6;X4]",
        "thiophene_C(3)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])=[#6]-[#6](=[#8])-c:1:c(-[#16;X2]):s:c(:c:1)-[$([#6]#[#7]),$([#6]=[#8])]",
        "anil_OC_alk_B(3)", "c:1:3:c(:c:c:c:c:1)-[#7]-2-[#6](=[#8])-[#6](=[#6](-[F,Cl,Br,I])-[#6]-2=[#8])-[#7](-[#1])-[#6]:[#6]:[#6]:[#6](-[#8]-[#6](-[#1])-[#1]):[#6]:[#6]:3",
        "het_thio_66_A(3)", "c:1-2:c(:c:c:c:c:1)-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]=[#6]-2-[#16;X2]-[#6](-[#1])(-[#1])-[#6](=[#8])-c:3:c:c:c:c:c:3",
        "rhod_sat_B(3)", "[#7]-2(-c:1:c:c:c:c:c:1-[#6](-[#1])-[#1])-[#6](=[#16])-[#7](-[#6](-[#1])(-[#1])-[!#1]:[!#1]:[!#1]:[!#1]:[!#1])-[#6](-[#1])(-[#1])-[#6]-2=[#8]",
        "ene_rhod_I(3)", "[#7]-2(-[#6](-[#1])-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](=[#6](-[#1])-c:1:c:c:c:c(:c:1)-[Br])-[#6]-2=[#8]",
        "keto_thiophene(3)", "c:1(:c(:c:2:c(:s:1):c:c:c:c:2)-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
        "ene_cyano_D(3)", "[#6](-[#6]#[#7])(-[#6]#[#7])=[#6](-[#16])-[#16]",
        "imine_imine_C(3)", "[#7](-[#6](-[#1])-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])=[#7]-[#6](-[#6](-[#1])-[#1])=[#7]-[#7](-[#6](-[#1])-[#1])-[#6]:[#6]",
        "het_65_pyridone_A(3)", "[#6]:2(:[#6](-[#6](-[#1])-[#1]):[#6]-1:[#6](-[#7]=[#6](-[#7](-[#6]-1=[!#6&!#1;X1])-[#6](-[#1])-[$([#6](=[#8])-[#8]),$([#6]:[#6])])-[$([#1]),$([#16]-[#6](-[#1])-[#1])]):[!#6&!#1;X2]:2)-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
        "thiazole_amine_C(3)", "c:1(:n:c(:c(-[#1]):s:1)-[!#1]:[!#1]:[!#1](-[$([#8]-[#6](-[#1])-[#1]),$([#6](-[#1])-[#1])]):[!#1]:[!#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c(-[#1]):c(:c(-[#1]):o:2)-[#1]",
        "het_thio_pyr_A(3)", "n:1:c(:c(:c(:c(:c:1-[#16]-[#6]-[#1])-[#6]#[#7])-c:2:c:c:c(:c:c:2)-[#8]-[#6](-[#1])-[#1])-[#1])-[#6]:[#6]",
        "melamine_A(3)", "c:1:4:c(:n:c(:n:c:1-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c(:c(:c(:o:2)-[#1])-[#1])-[#1])-[#7](-[#1])-c:3:c:c(:c(:c:c:3-[$([#1]),$([#6](-[#1])-[#1]),$([#16;X2]),$([#8]-[#6]-[#1]),$([#7;X3])])-[$([#1]),$([#6](-[#1])-[#1]),$([#16;X2]),$([#8]-[#6]-[#1]),$([#7;X3])])-[$([#1]),$([#6](-[#1])-[#1]),$([#16;X2]),$([#8]-[#6]-[#1]),$([#7;X3])]):c:c:c:c:4",
        "anil_NH_alk_B(3)", "[#7](-[#1])(-[#6]:1:[#6]:[#6]:[!#1]:[#6]:[#6]:1)-c:2:c:c:c(:c:c:2)-[#7](-[#1])-[#6]-[#1]",
        "rhod_sat_C(3)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#7]-[#6]=[#8])-[#16]-[#6](-[#1])(-[#1])-[#6]-2=[#8]",
        "thiophene_amino_D(3)", "[#6]=[#6]-[#6](=[#8])-[#7]-c:1:c(:c(:c(:s:1)-[#6](=[#8])-[#8])-[#6]-[#1])-[#6]#[#7]",
        "anil_OC_alk_C(3)", "[$([#1]),$([#6](-[#1])-[#1])]-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:n:c:c:n:2",
        "het_thio_65_A(3)", "[#6](-[#1])(-[#1])-[#16;X2]-c3nc1c(n(nc1-[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2)nn3",
        "het_thio_656b(3)", "[#6]-[#6](=[#8])-[#6](-[#1])(-[#1])-[#16;X2]-c:3:n:n:c:2:c:1:c(:c(:c(:c(:c:1:n(:c:2:n:3)-[#1])-[#1])-[#1])-[#1])-[#1]",
        "thiazole_amine_D(3)", "s:1:c(:[n+](-[#6](-[#1])-[#1]):c(:c:1-[#1])-[#6])-[#7](-[#1])-c:2:c:c:c:c:c:2[$([#6](-[#1])-[#1]),$([#6]:[#6])]",
        "thio_urea_H(3)", "[#6]-2(=[#16])-[#7](-[#6](-[#1])(-[#1])-c:1:c:c:c:o:1)-[#6](=[#7]-[#7]-2-[#1])-[#6]:[#6]",
        "cyano_pyridone_F(3)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#6](=[#6]-[#6](=[#7]-2)-[#6]#[#7])-[#6]#[#7]",
        "cyano_cyano_B(3)", "[#6]-1(-[#6]#[#7])(-[#6]#[#7])-[#6](-[#1])(-[#6](=[#8])-[#6])-[#6]-1-[#1]",
        "rhod_sat_D(3)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#16]-[#6](-[#1])(-[#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6])-[#6]-2=[#8]",
        "ene_rhod_J(3)", "[#6](-[#1])(-[#1])-[#7]-2-[#6](=[$([#16]),$([#7])])-[!#6&!#1]-[#6](=[#6]-1-[#6](=[#6](-[#1])-[#6]:[#6]-[#7]-1-[#6](-[#1])-[#1])-[#1])-[#6]-2=[#8]",
        "imine_phenol_A(3)", "[#6]=[#7;!R]-c:1:c:c:c:c:c:1-[#8]-[#1]",
        "thio_carbonate_B(3)", "[#8]=[#6]-2-[#16]-c:1:c(:c(:c:c:c:1)-[#8]-[#6](-[#1])-[#1])-[#8]-2",
        "het_thio_N_5A(3)", "[#7]=[#6]-1-[#7]=[#6]-[#7]-[#16]-1",
        "anil_di_alk_J(3)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])=[#7]-[#7]=[#6](-[#6])-[#6]:[#6])-[#1])-[#1]",
        "thio_aldehyd_A(3)", "[#6]-[#6](=[#16])-[#1]",
        "ene_five_het_M(3)", "[#6]-1=,:[#6]-[#6](-[#6](-[$([#8]),$([#16])]-1)=[#6]-[#6]=[#8])=[#8]",
        "cyano_ene_amine_C(3)", "[#6]:[#6]-[#6](=[#8])-[#7](-[#1])-[#6](=[#8])-[#6](-[#6]#[#7])=[#6](-[#1])-[#7](-[#1])-[#6]:[#6]",
        "thio_urea_I(3)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#7]=[#6]-c:2:c:n:c:c:2",
        "dhp_amino_CN_F(3)", "[#7](-[#1])(-[#1])-[#6]-2=[#6](-[#6]#[#7])-[#6](-[#1])(-c:1:c:c:c:s:1)-[#6](=[#6](-[#6](-[#1])-[#1])-[#8]-2)-[#6](=[#8])-[#8]-[#6]",
        "anthranil_acid_B(3)", "c:1:c-3:c(:c:c(:c:1)-[#6](=[#8])-[#7](-[#1])-c:2:c(:c:c:c:c:2)-[#6](=[#8])-[#8]-[#1])-[#6](-[#7](-[#6]-3=[#8])-[#6](-[#1])-[#1])=[#8]",
        "diazox_B(3)", "[Cl]-c:2:c:c:1:n:o:n:c:1:c:c:2",
        "thio_amide_B(2)", "[#6;X4]-[#7](-[#1])-[#6](-[#6]:[#6])=[#6](-[#1])-[#6](=[#16])-[#7](-[#1])-c:1:c:c:c:c:c:1",
        "imidazole_B(2)", "[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#16]-[#6](-[#1])(-[#1])-c1cn(cn1)-[#1]",
        "thiazole_amine_E(2)", "[#8]=[#6]-[#7](-[#1])-c:1:c(-[#6]:[#6]):n:c(-[#6](-[#1])(-[#1])-[#6]#[#7]):s:1",
        "thiazole_amine_F(2)", "[#6](-[#1])-[#7](-[#1])-c:1:n:c(:c:s:1)-c2cnc3n2ccs3",
        "thio_ester_C(2)", "[#7]-1-[#6](=[#8])-[#6](=[#6](-[#6])-[#16]-[#6]-1=[#16])-[#1]",
        "ene_one_B(2)", "[#6](-[#16])(-[#7])=[#6](-[#1])-[#6]=[#6](-[#1])-[#6]=[#8]",
        "quinone_C(2)", "[#8]=[#6]-3-c:1:c(:c:c:c:c:1)-[#6]-2=[#6](-[#8]-[#1])-[#6](=[#8])-[#7]-c:4:c-2:c-3:c:c:c:4",
        "hzide_naphth(2)", "c:2(:c:1:c(:c(:c(:c(:c:1:c(:c(:c:2-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#7](-[#1])-[#6]=[#8])-[#1])-[#1])-[#1]",
        "keto_naphthol_A(2)", "c:1:2:c:c:c:c(:c:1:c(:c:c:c:2)-[$([#8]-[#1]),$([#7](-[#1])-[#1])])-[#6](-[#6])=[#8]",
        "thio_amide_C(2)", "[#6](-[#1])(-c:1:c:c:c:c:c:1)(-c:2:c:c:c:c:c:2)-[#6](=[#16])-[#7]-[#1]",
        "phthalimide_misc(2)", "[#7]-2(-[#6](=[#8])-c:1:c(:c(:c(:c(:c:1-[#1])-[#6](=[#8])-[#8]-[#1])-[#1])-[#1])-[#6]-2=[#8])-c:3:c(:c:c(:c(:c:3)-[#1])-[#8])-[#1]",
        "sulfonamide_D(2)", "c:1:c:c(:c:c:c:1-[#7](-[#1])-[#16](=[#8])=[#8])-[#7](-[#1])-[#16](=[#8])=[#8]",
        "anil_NH_alk_C(2)", "[#6](-[#1])-[#7](-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6]-[#1]",
        "het_65_E(2)", "s1c(c(c-2c1-[#7](-[#1])-[#6](-[#6](=[#6]-2-[#1])-[#6](=[#8])-[#8]-[#1])=[#8])-[#7](-[#1])-[#1])-[#6](=[#8])-[#7]-[#1]",
        "anisol_B(2)", "[#6](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6;X4])-[#1]",
        "thio_carbam_ene(2)", "[#6]-1=[#6]-[#7]-[#6](-[#16]-[#6;X4]-1)=[#16]",
        "thio_amide_D(2)", "[#6](-[#7](-[#6]-[#1])-[#6]-[#1]):[#6]-[#7](-[#1])-[#6](=[#16])-[#6]-[#1]",
        "het_65_Da(2)", "n2nc(c1cccc1c2-[#6])-[#6]",
        "thiophene_D(2)", "s:1:c(:c(-[#1]):c(:c:1-[#6](=[#8])-[#7](-[#1])-[#7]-[#1])-[#8]-[#6](-[#1])-[#1])-[#1]",
        "het_thio_6_ene(2)", "[#6]-1:[#6]-[#7]=[#6]-[#6](=[#6]-[#7]-[#6])-[#16]-1",
        "het_565_indole(1)", "c2(c(-[#1])n(-[#6](-[#1])-[#1])c:3:c(:c(:c:1n(c(c(c:1:c2:3)-[#1])-[#1])-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1]",
        "cyano_keto_A(2)", "[#6](-[#1])(-[#1])-[#6](-[#1])(-[#6]#[#7])-[#6](=[#8])-[#6]",
        "anthranil_acid_C(2)", "c2(c(-[#7](-[#1])-[#1])n(-c:1:c:c:c:c:c:1-[#6](=[#8])-[#8]-[#1])nc2-[#6]=[#8])-[$([#6]#[#7]),$([#6]=[#16])]",
        "naphth_amino_C(2)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7](-[#7]=[#6]-3)-[#1]",
        "naphth_amino_D(2)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7]-[#7]=[#7]-3",
        "thiazole_amine_G(2)", "c1csc(n1)-[#7]-[#7]-[#16](=[#8])=[#8]",
        "het_66_B(2)", "c:1:c:c:c:2:c(:c:1):n:c(:n:c:2)-[#7](-[#1])-[#6]-3=[#7]-[#6](-[#6]=[#6]-[#7]-3-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
        "coumarin_A(2)", "c:1-3:c(:c(:c(:c(:c:1)-[#8]-[#6]-[#1])-[#1])-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#8])-[#8]-3",
        "het_thio_5_B(2)", "[#6]-1(-[#6]=[#8])(-[#6]:[#6])-[#16;X2]-[#6]=[#7]-[#7]-1-[#1]",
        "thiophene_amino_F(2)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-c:2:c:c:c:c:c:2)-[#6]#[#7])-[#6]:3:[!#1]:[!#1]:[!#1]:[!#1]:[!#1]:3",
        "anthranil_acid_D(2)", "c:12:c(:c:c:c:n:1)c(c(-[#6](=[#8])~[#8;X1])s2)-[#7](-[#1])-[#1]",
        "het_66_C(2)", "c:1:2:n:c(:c(:n:c:1:[#6]:[#6]:[#6]:[!#1]:2)-[#6](-[#1])=[#6](-[#8]-[#1])-[#6])-[#6](-[#1])=[#6](-[#8]-[#1])-[#6]",
        "thiophene_amino_E(2)", "c1csc(c1-[#7](-[#1])-[#1])-[#6](-[#1])=[#6](-[#1])-c2cccs2",
        "het_6666_A(2)", "c:2:c:c:1:n:c:3:c(:n:c:1:c:c:2):c:c:c:4:c:3:c:c:c:c:4",
        "sulfonamide_E(2)", "[#6]:[#6]-[#7](-[#1])-[#16](=[#8])(=[#8])-[#7](-[#1])-[#6]:[#6]",
        "anil_di_alk_K(2)", "c:1:c:c(:c:c:c:1-[#7](-[#1])-[#1])-[#7](-[#6;X3])-[#6;X3]",
        "het_5_C(2)", "[#7]-2=[#6](-c:1:c:c:c:c:c:1)-[#6](-[#1])(-[#1])-[#6](-[#8]-[#1])(-[#6](-[#9])(-[#9])-[#9])-[#7]-2-[$([#6]:[#6]:[#6]:[#6]:[#6]:[#6]),$([#6](=[#16])-[#6]:[#6]:[#6]:[#6]:[#6]:[#6])]",
        "ene_six_het_B(2)", "c:1:c(:c:c:c:c:1)-[#6](=[#8])-[#6](-[#1])=[#6]-3-[#6](=[#8])-[#7](-[#1])-[#6](=[#8])-[#6](=[#6](-[#1])-c:2:c:c:c:c:c:2)-[#7]-3-[#1]",
        "steroid_A(2)", "[#8]=[#6]-4-[#6]-[#6]-[#6]-3-[#6]-2-[#6](=[#8])-[#6]-[#6]-1-[#6]-[#6]-[#6]-[#6]-1-[#6]-2-[#6]-[#6]-[#6]-3=[#6]-4",
        "het_565_A(2)", "c:1:2:c:3:c(:c(-[#8]-[#1]):c(:c:1:c(:c:n:2-[#6])-[#6]=[#8])-[#1]):n:c:n:3",
        "thio_imine_ium(2)", "[#6;X4]-[#7+](-[#6;X4]-[#8]-[#1])=[#6]-[#16]-[#6]-[#1]",
        "anthranil_acid_E(2)", "[#6]-3(=[#8])-[#6](=[#6](-[#1])-[#7](-[#1])-c:1:c:c:c:c:c:1-[#6](=[#8])-[#8]-[#1])-[#7]=[#6](-c:2:c:c:c:c:c:2)-[#8]-3",
        "hzone_furan_B(2)", "c:1(:c(:c(:c(:o:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](-[$([#1]),$([#6](-[#1])-[#1])])=[#7]-[#7](-[#1])-c:2:c:c:n:c:c:2",
        "thiophene_E(2)", "c:1(:c(:c(:c(:s:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](-[$([#1]),$([#6](-[#1])-[#1])])-[#6](=[#8])-[#7](-[#1])-c:2:n:c:c:s:2",
        "ene_misc_B(2)", "[#6]:[#6]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#6]=[#8])-[#7]-2-[#6](=[#8])-[#6]-1(-[#1])-[#6](-[#1])(-[#1])-[#6]=[#6]-[#6](-[#1])(-[#1])-[#6]-1(-[#1])-[#6]-2=[#8]",
        "thio_urea_J(2)", "c:1(:c(:o:c:c:1)-[#6]-[#1])-[#6]=[#7]-[#7](-[#1])-[#6](=[#16])-[#7]-[#1]",
        "het_thio_65_B(2)", "[#7](-[#1])-c1nc(nc2nnc(n12)-[#16]-[#6])-[#7](-[#1])-[#6]",
        "coumarin_B(2)", "c:1-2:c(:c:c:c:c:1-[#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1])-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-[#6]:[#6])-[#6](=[#8])-[#8]-2)-[#1]",
        "thio_urea_K(2)", "[#6]-2(=[#16])-[#7]-1-[#6]:[#6]-[#7]=[#7]-[#6]-1=[#7]-[#7]-2-[#1]",
        "thiophene_amino_G(2)", "[#6]:[#6]:[#6]:[#6]:[#6]:[#6]-c:1:c:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-[#6])-[#6](=[#8])-[#8]-[#1]",
        "pyrrole_I(2)", "n2(-[#6](-[#1])-[#1])c-1c(-[#6]:[#6]-[#6]-1=[#8])cc2-[#6](-[#1])-[#1]",
        "anil_NH_alk_D(2)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c:c:1-[#7](-[#1])-[#6](-[#1])(-[#6])-[#6](-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
        "het_thio_5_C(2)", "[#16]=[#6]-2-[#7](-[#1])-[#7]=[#6](-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#8]-2",
        "thio_keto_het(2)", "[#16]=[#6]-c:1:c:c:c:2:c:c:c:c:n:1:2",
        "het_thio_N_5B(2)", "[#6]~1~[#6](~[#7]~[#7]~[#6](~[#6](-[#1])-[#1])~[#6](-[#1])-[#1])~[#7]~[#16]~[#6]~1",
        "quinone_D(2)", "[#6]-1(-[#6]=,:[#6]-[#6]=,:[#6]-[#6]-1=[!#6&!#1])=[!#6&!#1]",
        "misc_trityl_A(1)", "[#6](-[#6]:[#6])(-[#6]:[#6])(-[#6]:[#6])-[#16]-[#6]:[#6]-[#6](=[#8])-[#8]-[#1]",
        "anil_di_alk_furan_B(2)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(-[#1]):c(:c(:o:1)-[#6](-[#1])=[#6]-[#6]#[#7])-[#1]",
        "ene_six_het_C(2)", "[#8]=[#6]-1-[#6]:[#6]-[#6](-[#1])(-[#1])-[#7]-[#6]-1=[#6]-[#1]",
        "het_55_A(2)", "[#6]:[#6]-[#7]:2:[#7]:[#6]:1-[#6](-[#1])(-[#1])-[#16;X2]-[#6](-[#1])(-[#1])-[#6]:1-[#6]:2-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])=[#6]-[#1]",
        "het_thio_65_C(2)", "n:1:c(:n(:c:2:c:1:c:c:c:c:2)-[#6](-[#1])-[#1])-[#16]-[#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6](-[#1])-[#6](-[#1])=[#6]-[#1]",
        "hydroquin_A(2)", "c:1(:c:c(:c(:c:c:1)-[#8]-[#1])-[#6](=!@[#6]-[#7])-[#6]=[#8])-[#8]-[#1]",
        "anthranil_acid_F(2)", "c:1(:c:c(:c(:c:c:1)-[#7](-[#1])-[#6](=[#8])-[#6]:[#6])-[#6](=[#8])-[#8]-[#1])-[#8]-[#1]",
        "thiophene_amino_H(2)", "[#6](-[#1])-[#7](-[#1])-c:1:c(:c(:c(:s:1)-[#6]-[#1])-[#6]-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6]",
        "imine_one_fives_C(2)", "[#6]:[#6]-[#7;!R]=[#6]-2-[#6](=[!#6&!#1])-c:1:c:c:c:c:c:1-[#7]-2",
        "keto_phenone_zone_A(2)", "c:1:c:c:c:c:c:1-[#6](=[#8])-[#7](-[#1])-[#7]=[#6]-3-c:2:c:c:c:c:c:2-c:4:c:c:c:c:c-3:4",
        "dyes7A(2)", "c:1:c(:c:c:c:c:1)-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])=[#6](-[#1])-[#6]=!@[#6](-[#1])-[#6](-[#1])=[#6]-[#6]=@[#7]-c:2:c:c:c:c:c:2",
        "het_pyridiniums_B(2)", "[#6]:1:2:[!#1]:[#7+](:[!#1]:[#6](:[!#1]:1:[#6]:[#6]:[#6]:[#6]:2)-[*])~[#6]:[#6]",
        "het_5_D(2)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#16]-[#6])-[#6]-2=[#8]",
        "thiazole_amine_H(1)", "c:1:c:c:c(:c:c:1-[#7](-[#1])-c2nc(c(-[#1])s2)-c:3:c:c:c(:c:c:3)-[#6](-[#1])(-[#6]-[#1])-[#6]-[#1])-[#6](=[#8])-[#8]-[#1]",
        "misc_pyridine_OC(1)", "[#8]=[#6](-c:1:c(:c(:n:c(:c:1-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
        "thiazole_amine_I(1)", "[#6](-[#1])(-[#1])-[#7](-[#1])-[#6]=[#7]-[#7](-[#1])-c1nc(c(-[#1])s1)-[#6]:[#6]",
        "het_thio_N_5C(1)", "[#6]:[#6]-[#7](-[#1])-[#6](=[#8])-c1c(snn1)-[#7](-[#1])-[#6]:[#6]",
        "sulfonamide_F(1)", "[#8]=[#16](=[#8])(-[#6]:[#6])-[#7](-[#1])-c1nc(cs1)-[#6]:[#6]",
        "thiazole_amine_J(1)", "[#8]=[#16](=[#8])(-[#6]:[#6])-[#7](-[#1])-[#7](-[#1])-c1nc(cs1)-[#6]:[#6]",
        "het_65_F(1)", "s2c:1:n:c:n:c(:c:1c(c2-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#7]-[#7]=[#6]-c3ccco3",
        "keto_keto_beta_E(1)", "[#6](=[#8])-[#6](-[#1])=[#6](-[#8]-[#1])-[#6](-[#8]-[#1])=[#6](-[#1])-[#6](=[#8])-[#6]",
        "ene_five_one_B(1)", "c:2(:c:1-[#6](-[#6](-[#6](-c:1:c(:c(:c:2-[#1])-[#1])-[#1])(-[#1])-[#1])=[#8])=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1]",
        "keto_keto_beta_zone(1)", "[#6]:[#6]-[#7](-[#1])-[#7]=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#6](-[#1])-[#1])=[#7]-[#7](-[#1])-[#6]:[#6]",
        "thio_urea_L(1)", "[#6;X4]-[#16;X2]-[#6](=[#7]-[!#1]:[!#1]:[!#1]:[!#1])-[#7](-[#1])-[#7]=[#6]",
        "het_thio_urea_ene(1)", "[#6]-1(=[#7]-[#7](-[#6](-[#16]-1)=[#6](-[#1])-[#6]:[#6])-[#6]:[#6])-[#6]=[#8]",
        "cyano_amino_het_A(1)", "c:1(:c(:c:2:c(:n:c:1-[#7](-[#1])-[#1]):c:c:c(:c:2-[#7](-[#1])-[#1])-[#6]#[#7])-[#6]#[#7])-[#6]#[#7]",
        "tetrazole_hzide(1)", "[!#1]:1:[!#1]:[!#1]:[!#1](:[!#1]:[!#1]:1)-[#6](-[#1])=[#6](-[#1])-[#6](-[#7](-[#1])-[#7](-[#1])-c2nnnn2-[#6])=[#8]",
        "imine_naphthol_A(1)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(:c(:c:2-[#1])-[#1])-[#6](=[#7]-[#6]:[#6])-[#6](-[#1])-[#1])-[#8]-[#1])-[#1])-[#1])-[#1])-[#1]",
        "misc_anisole_A(1)", "c:1(:c(:c:2:c(:c(:c:1-[#8]-[#6](-[#1])-[#1])-[#1]):c(:c(:c(:c:2-[#7](-[#1])-[#6](-[#1])(-[#1])-[#1])-[#1])-c:3:c(:c(:c(:c(:c:3-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#1])-[#8]-[#6](-[#1])-[#1]",
        "het_thio_665(1)", "c:1:c:c-2:c(:c:c:1)-[#16]-c3c(-[#7]-2)cc(s3)-[#6](-[#1])-[#1]",
        "anil_di_alk_L(1)", "c:1:c:c:c-2:c(:c:1)-[#6](-[#6](-[#7]-2-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]-4-[#6](-c:3:c:c:c:c:c:3-[#6]-4=[#8])=[#8])(-[#1])-[#1])(-[#1])-[#1]",
        "colchicine_B(1)", "c:1(:c:c:c(:c:c:1)-[#6]-3=[#6]-[#6](-c2cocc2-[#6](=[#6]-3)-[#8]-[#1])=[#8])-[#16]-[#6](-[#1])-[#1]",
        "misc_aminoacid_A(1)", "[#6;X4]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#16]-[#6](-[#1])(-[#1])-[#1])-[#6](=[#8])-[#8]-[#1])-[#1])-[#1]",
        "imidazole_amino_A(1)", "n:1:c(:n(:c(:c:1-c:2:c:c:c:c:c:2)-c:3:c:c:c:c:c:3)-[#7]=!@[#6])-[#7](-[#1])-[#1]",
        "phenol_sulfite_A(1)", "[#6](-c:1:c:c:c(:c:c:1)-[#8]-[#1])(-c:2:c:c:c(:c:c:2)-[#8]-[#1])-[#8]-[#16](=[#8])=[#8]",
        "het_66_D(1)", "c:2:c:c:1:n:c(:c(:n:c:1:c:c:2)-[#6](-[#1])(-[#1])-[#6](=[#8])-[#6]:[#6])-[#6](-[#1])(-[#1])-[#6](=[#8])-[#6]:[#6]",
        "misc_anisole_B(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c(-[#6](-[#1])-[#1])c:c:2",
        "tetrazole_A(1)", "[#6](-[#1])(-[#1])-c1nnnn1-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#1])-[#1]",
        "het_65_G(1)", "[#6]-2(=[#7]-c1c(c(nn1-[#6](-[#6]-2(-[#1])-[#1])=[#8])-[#7](-[#1])-[#1])-[#7](-[#1])-[#1])-[#6]",
        "het_6_hydropyridone(1)", "[#7]-1=[#6](-[#7](-[#6](-[#6](-[#6]-1(-[#1])-[#6]:[#6])(-[#1])-[#1])=[#8])-[#1])-[#7]-[#1]",
        "misc_stilbene(1)", "[#6]-1(=[#6](-[#6](-[#6](-[#6](-[#6]-1(-[#1])-[#1])(-[#1])-[#6](=[#8])-[#6])(-[#1])-[#6](=[#8])-[#8]-[#1])(-[#1])-[#1])-[#6]:[#6])-[#6]:[#6]",
        "misc_imidazole(1)", "[#6](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[Cl])-[#1])-[#1])(-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[Cl])-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-c3nc(c(n3-[#6](-[#1])(-[#1])-[#1])-[#1])-[#1]",
        "anil_NH_no_alk_A(1)", "n:1:c(:c(:c(:c(:c:1-[#1])-[#7](-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6]:[#6]",
        "het_6_imidate_B(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#8]-[#1])-[#6]-2=[#6](-[#8]-[#6](-[#7]=[#7]-2)=[#7])-[#7](-[#1])-[#1]",
        "anil_alk_B(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1]",
        "anthranil_acid_G(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#8])-[#8]-[#1])-[#7](-[#1])-[#6]:[#6]",
        "styrene_anil_A(1)", "c:1:c:c-3:c(:c:c:1)-c:2:c:c:c(:c:c:2-[#6]-3=[#6](-[#1])-[#6])-[#7](-[#1])-[#1]",
        "misc_aminal_acid(1)", "c:1:c:c-2:c(:c:c:1)-[#7](-[#6](-[#8]-[#6]-2)(-[#6](=[#8])-[#8]-[#1])-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](-[#1])-[#1]",
        "anil_no_alk_D(1)", "n:1:c(:c(:c(:c(:c:1-[#7](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#1])-[#1]",
        "anil_alk_C(1)", "[#7](-[#1])(-c:1:c:c:c:c:c:1)-[#6](-[#6])(-[#6])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1]",
        "misc_anisole_C(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#8]-[#6]-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])(-[#1])-[#1])-[#6]:[#6]",
        "het_465_misc(1)", "c:1-2:c:c-3:c(:c:c:1-[#8]-[#6]-[#8]-2)-[#6]-[#6]-3",
        "anthranil_acid_H(1)", "c:1:c(:c2:c(:c:c:1)c(c(n2-[#1])-[#6]:[#6])-[#6]:[#6])-[#6](=[#8])-[#8]-[#1]",
        "thio_urea_M(1)", "[#6]:[#6]-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-c:1:c(:c(:c(:c(:c:1-[F,Cl,Br,I])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1]",
        "thiazole_amine_K(1)", "n:1:c3:c(:c:c2:c:1nc(s2)-[#7])sc(n3)-[#7]",
        "het_thio_5_imine_A(1)", "[#7]=[#6]-1-[#16]-[#6](=[#7])-[#7]=[#6]-1",
        "thio_amide_E(1)", "c:1:c(:n:c:c:c:1)-[#6](=[#16])-[#7](-[#1])-c:2:c(:c:c:c:c:2)-[#8]-[#6](-[#1])-[#1]",
        "het_thio_676_B(1)", "c:1-2:c(:c(:c(:c(:c:1-[#6](-c:3:c(-[#16]-[#6]-2(-[#1])-[#1]):c(:c(-[#1]):c(:c:3-[#1])-[#1])-[#1])-[#8]-[#6]:[#6])-[#1])-[#1])-[#1])-[#1]",
        "sulfonamide_G(1)", "[#6](-[#1])(-[#1])(-[#1])-c:1:c(:c(:c(:c(:n:1)-[#7](-[#1])-[#16](-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])(=[#8])=[#8])-[#1])-[#1])-[#1]",
        "thio_thiomorph_Z(1)", "[#6](=[#8])(-[#7]-1-[#6]-[#6]-[#16]-[#6]-[#6]-1)-c:2:c(:c(:c(:c(:c:2-[#16]-[#6](-[#1])-[#1])-[#1])-[#1])-[#1])-[#1]",
        "naphth_ene_one_A(1)", "c:1:c:c:3:c:2:c(:c:1)-[#6](-[#6]=[#6](-c:2:c:c:c:3)-[#8]-[#6](-[#1])-[#1])=[#8]",
        "naphth_ene_one_B(1)", "c:1-3:c:2:c(:c(:c:c:1)-[#7]):c:c:c:c:2-[#6](-[#6]=[#6]-3-[#6](-[F])(-[F])-[F])=[#8]",
        "amino_acridine_A(1)", "c:1:c:c:c:c:2:c:1:c:c:3:c(:n:2):n:c:4:c(:c:3-[#7]):c:c:c:c:4",
        "keto_phenone_B(1)", "c:1:c-3:c(:c:c:c:1)-[#6]-2=[#7]-[!#1]=[#6]-[#6]-[#6]-2-[#6]-3=[#8]",
        "hzone_acid_A(1)", "c:1-3:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#7]-[#7](-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#6](=[#8])-[#8]-[#1])-[#1])-[#1])-c:4:c-3:c(:c(:c(:c:4-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1]",
        "sulfonamide_H(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#1])-[#1])-[#1])-[#16](=[#8])(=[#8])-[#7](-[#1])-c:2:n:n:c(:c(:c:2-[#1])-[#1])-[#1]",
        "pyrrole_J(1)", "c1(c-2c(c(n1-[#6](-[#8])=[#8])-[#6](-[#1])-[#1])-[#16]-[#6](-[#1])(-[#1])-[#16]-2)-[#6](-[#1])-[#1]",
        "pyrazole_amino_B(1)", "s1ccnc1-c2c(n(nc2-[#1])-[#1])-[#7](-[#1])-[#1]",
        "pyrrole_K(1)", "c1(c(c(c(n1-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#6](=[#8])-[#8]-[#1]",
        "anthranil_acid_I(1)", "c:1:2(:c(:c(:c(:o:1)-[#6])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6](-[#1]):[#6](-[#1]):[#6](-[#1]):[#6](-[#1]):[#6]:2-[#6](=[#8])-[#8]-[#1]",
        "thio_amide_F(1)", "[!#1]:[#6]-[#6](=[#16])-[#7](-[#1])-[#7](-[#1])-[#6]:[!#1]",
        "ene_one_C(1)", "[#6]-1(=[#8])-[#6](-[#6](-[#6]#[#7])=[#6](-[#1])-[#7])-[#6](-[#7])-[#6]=[#6]-1",
        "het_65_H(1)", "c2(c-1n(-[#6](-[#6]=[#6]-[#7]-1)=[#8])nc2-c3cccn3)-[#6]#[#7]",
        "cyano_imine_D(1)", "[#8]=[#6]-1-[#6](=[#7]-[#7]-[#6]-[#6]-1)-[#6]#[#7]",
        "cyano_misc_A(1)", "c:2(:c:1:c:c:c:c:c:1:n:n:c:2)-[#6](-[#6]:[#6])-[#6]#[#7]",
        "ene_misc_C(1)", "c:1:c:c-2:c(:c:c:1)-[#6]=[#6]-[#6](-[#7]-2-[#6](=[#8])-[#7](-[#1])-c:3:c:c(:c(:c:c:3)-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
        "het_66_E(1)", "c:2:c:c:1:n:c(:c(:n:c:1:c:c:2)-c:3:c:c:c:c:c:3)-c:4:c:c:c:c:c:4-[#8]-[#1]",
        "keto_keto_beta_F(1)", "[#6](-[#1])(-[#1])-[#6](-[#8]-[#1])=[#6](-[#6](=[#8])-[#6](-[#1])-[#1])-[#6](-[#1])-[#6]#[#6]",
        "misc_naphthimidazole(1)", "c:1:c:4:c(:c:c2:c:1nc(n2-[#1])-[#6]-[#8]-[#6](=[#8])-c:3:c:c(:c:c(:c:3)-[#7](-[#1])-[#1])-[#7](-[#1])-[#1]):c:c:c:c:4",
        "naphth_ene_one_C(1)", "c:2(:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#6]=[#6]-[#6]-3=[#7])-[#7]",
        "keto_phenone_C(1)", "c:2(:c:1:c:c:c:c:c:1:c-3:c(:c:2)-[#6](-c:4:c:c:c:c:c-3:4)=[#8])-[#8]-[#1]",
        "coumarin_C(1)", "[#6]-2(-[#6]=[#7]-c:1:c:c(:c:c:c:1-[#8]-2)-[Cl])=[#8]",
        "thio_est_cyano_A(1)", "[#6]-1=[#6]-[#7](-[#6](-c:2:c-1:c:c:c:c:2)(-[#6]#[#7])-[#6](=[#16])-[#16])-[#6]=[#8]",
        "het_65_imidazole(1)", "c2(nc:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])n2-[#6])-[#7](-[#1])-[#6](-[#7](-[#1])-c:3:c(:c:c:c:c:3-[#1])-[#1])=[#8]",
        "anthranil_acid_J(1)", "[#7](-[#1])(-[#6]:[#6])-c:1:c(-[#6](=[#8])-[#8]-[#1]):c:c:c(:n:1)-[#6]:[#6]",
        "colchicine_het(1)", "c:1-3:c(:c:c:c:c:1)-[#16]-[#6](=[#7]-[#7]=[#6]-2-[#6]=[#6]-[#6]=[#6]-[#6]=[#6]-2)-[#7]-3-[#6](-[#1])-[#1]",
        "ene_misc_D(1)", "c:1-2:c(:c(:c(:c(:c:1-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#6](-[#6])-[#16]-[#6]-2(-[#1])-[#1])-[#6]",
        "indole_3yl_alk_B(1)", "c:12:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])c(c(-[#6]:[#6])n2-!@[#6]:[#6])-[#6](-[#1])-[#1]",
        "anil_OH_no_alk_A(1)", "[#7](-[#1])(-[#1])-c:1:c:c:c(:c:c:1-[#8]-[#1])-[#16](=[#8])(=[#8])-[#8]-[#1]",
        "thiazole_amine_L(1)", "s:1:c:c:c(:c:1-[#1])-c:2:c:s:c(:n:2)-[#7](-[#1])-[#1]",
        "pyrazole_amino_A(1)", "c1c(-[#7](-[#1])-[#1])nnc1-c2c(-[#6](-[#1])-[#1])oc(c2-[#1])-[#1]",
        "het_thio_N_5D(1)", "n1nscc1-c2nc(no2)-[#6]:[#6]",
        "anil_alk_indane(1)", "c:1(:c:c-3:c(:c:c:1)-[#7]-[#6]-4-c:2:c:c:c:c:c:2-[#6]-[#6]-3-4)-[#6;X4]",
        "anil_di_alk_N(1)", "c:1-2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#6](=[#6](-[#1])-[#6]-3-[#6](-[#6]#[#7])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#7]-2-3)-[#1]",
        "het_666_C(1)", "c:2-3:c(:c:c:1:c:c:c:c:c:1:c:2)-[#7](-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](=[#7]-3)-[#6]:[#6]-[#7](-[#1])-[#6](-[#1])-[#1]",
        "ene_one_D(1)", "[#6](-[#8]-[#1]):[#6]-[#6](=[#8])-[#6](-[#1])=[#6](-[#6])-[#6]",
        "anil_di_alk_indol(1)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1]):c(:c(-[#1]):n:2-[#1])-[#16](=[#8])=[#8]",
        "anil_no_alk_indol_A(1)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#1])-[#1]):c(:c(-[#1]):n:2-[#6](-[#1])-[#1])-[#1]",
        "dhp_amino_CN_G(1)", "[#16;X2]-1-[#6]=[#6](-[#6]#[#7])-[#6](-[#6])(-[#6]=[#8])-[#6](=[#6]-1-[#7](-[#1])-[#1])-[$([#6]=[#8]),$([#6]#[#7])]",
        "anil_di_alk_dhp(1)", "[#7]-2-[#6]=[#6](-[#6]=[#8])-[#6](-c:1:c:c:c(:c:c:1)-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#6]~3=[#6]-2~[#7]~[#6](~[#16])~[#7]~[#6]~3~[#7]",
        "anthranil_amide_A(1)", "c:1:c(:c:c:c:c:1)-[#6](=[#8])-[#7](-[#1])-c:2:c(:c:c:c:c:2)-[#6](=[#8])-[#7](-[#1])-[#7](-[#1])-c:3:n:c:c:s:3",
        "hzone_anthran_Z(1)", "c:1:c:2:c(:c:c:c:1):c(:c:3:c(:c:2):c:c:c:c:3)-[#6]=[#7]-[#7](-[#1])-c:4:c:c:c:c:c:4",
        "ene_one_amide_A(1)", "c:1:c(:c:c:c:c:1)-[#6](-[#1])-[#7]-[#6](=[#8])-[#6](-[#7](-[#1])-[#6](-[#1])-[#1])=[#6](-[#1])-[#6](=[#8])-c:2:c:c:c(:c:c:2)-[#8]-[#6](-[#1])-[#1]",
        "het_76_A(1)", "s:1:c(:c(-[#1]):c(:c:1-[#6]-3=[#7]-c:2:c:c:c:c:c:2-[#6](=[#7]-[#7]-3-[#1])-c:4:c:c:n:c:c:4)-[#1])-[#1]",
        "thio_urea_N(1)", "o:1:c(:c(-[#1]):c(:c:1-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](=[#16])-[#7](-[#6]-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c:c:c:2)-[#1])-[#1]",
        "anil_di_alk_coum(1)", "c:1:c(:c:c:c:c:1)-[#7](-[#6]-[#1])-[#6](-[#1])-[#6](-[#1])-[#6](-[#1])-[#7](-[#1])-[#6](=[#8])-[#6]-2=[#6](-[#8]-[#6](-[#6](=[#6]-2-[#6](-[#1])-[#1])-[#1])=[#8])-[#6](-[#1])-[#1]",
        "ene_one_amide_B(1)", "c2-3:c:c:c:1:c:c:c:c:c:1:c2-[#6](-[#1])-[#6;X4]-[#7]-[#6]-3=[#6](-[#1])-[#6](=[#8])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
        "dhp_amidine_A(1)", "[#7]-1(-[#1])-[#7]=[#6](-[#7]-[#1])-[#16]-[#6](=[#6]-1-[#6]:[#6])-[#6]:[#6]",
        "thio_urea_O(1)", "c:1(:c(:c-3:c(:c(:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])-c:2:c(:c(:c(:o:2)-[#6]-[#1])-[#1])-[#1])-[#1])-[#8]-[#6](-[#8]-3)(-[#1])-[#1])-[#1])-[#1]",
        "anil_di_alk_O(1)", "c:1(:c(:c(:c(:c(:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-c:2:c:c:c:c:c:2)-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
        "thio_urea_P(1)", "[#8]=[#6]-!@n:1:c:c:c-2:c:1-[#7](-[#1])-[#6](=[#16])-[#7]-2-[#1]",
        "het_pyraz_misc(1)", "[#6](-[F])(-[F])-[#6](=[#8])-[#7](-[#1])-c:1:c(-[#1]):n(-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#8]-[#6](-[#1])(-[#1])-[#6]:[#6]):n:c:1-[#1]",
        "diazox_C(1)", "[#7]-2=[#7]-[#6]:1:[#7]:[!#6&!#1]:[#7]:[#6]:1-[#7]=[#7]-[#6]:[#6]-2",
        "diazox_D(1)", "[#6]-2(-[#1])(-[#8]-[#1])-[#6]:1:[#7]:[!#6&!#1]:[#7]:[#6]:1-[#6](-[#1])(-[#8]-[#1])-[#6]=[#6]-2",
        "misc_cyclopropane(1)", "[#6]-1(-[#6](-[#1])(-[#1])-[#6]-1(-[#1])-[#1])(-[#6](=[#8])-[#7](-[#1])-c:2:c:c:c(:c:c:2)-[#8]-[#6](-[#1])(-[#1])-[#8])-[#16](=[#8])(=[#8])-[#6]:[#6]",
        "imine_ene_one_B(1)", "[#6]-1:[#6]-[#6](=[#8])-[#6]=[#6]-1-[#7]=[#6](-[#1])-[#7](-[#6;X4])-[#6;X4]",
        "misc_furan_A(1)", "c:1:c(:o:c(:c:1-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#7]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#8]-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#8]-c:2:c:c-3:c(:c:c:2)-[#8]-[#6](-[#8]-3)(-[#1])-[#1]",
        "rhod_sat_E(1)", "[#7]-4(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#16]-[#6](-[#1])(-[#7](-[#1])-c:2:c:c:c:c:3:c:c:c:c:c:2:3)-[#6]-4=[#8]",
        "rhod_sat_imine_A(1)", "[#7]-3(-[#6](=[#8])-c:1:c:c:c:c:c:1)-[#6](=[#7]-c:2:c:c:c:c:c:2)-[#16]-[#6](-[#1])(-[#1])-[#6]-3=[#8]",
        "rhod_sat_F(1)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#16]-[#6](-[#1])(-[#1])-[#6]-2=[#16]",
        "het_thio_5_imine_B(1)", "[#7]-1(-[#6](-[#1])-[#1])-[#6](=[#16])-[#7](-[#6]:[#6])-[#6](=[#7]-[#6]:[#6])-[#6]-1=[#7]-[#6]:[#6]",
        "het_thio_5_imine_C(1)", "[#16]-1-[#6](=[#7]-[#7]-[#1])-[#16]-[#6](=[#7]-[#6]:[#6])-[#6]-1=[#7]-[#6]:[#6]",
        "ene_five_het_N(1)", "[#6]-2(=[#8])-[#6](=[#6](-[#1])-c:1:c(:c:c:c(:c:1)-[F,Cl,Br,I])-[#8]-[#6](-[#1])-[#1])-[#7]=[#6](-[#16]-[#6](-[#1])-[#1])-[#16]-2",
        "thio_carbam_A(1)", "[#6](-[#1])(-[#1])-[#16]-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]",
        "misc_anilide_A(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6])-[#1])-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]",
        "misc_anilide_B(1)", "c:1(:c(:c(:c(:c(:c:1-[#6](-[#1])-[#1])-[#1])-[Br])-[#1])-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
        "mannich_B(1)", "c:1-2:c(:c:c:c(:c:1-[#8]-[#6](-[#1])(-[#1])-[#7](-[#6]:[#6]-[#8]-[#6](-[#1])-[#1])-[#6]-2(-[#1])-[#1])-[#1])-[#1]",
        "mannich_catechol_A(1)", "c:1-2:c(:c(:c(:c(:c:1-[#8]-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6]-2(-[#1])-[#1])-[#1])-[#8])-[#8])-[#1]",
        "anil_alk_D(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
        "het_65_I(1)", "n:1:2:c:c:c(:c:c:1:c:c(:c:2-[#6](=[#8])-[#6]:[#6])-[#6]:[#6])-[#6](~[#8])~[#8]",
        "misc_urea_A(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#6](=[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#6](-[#6;X4])(-[#6;X4])-[#7](-[#1])-[#6](=[#8])-[#7](-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]",
        "imidazole_C(1)", "[#6]-3(-[#1])(-n:1:c(:n:c(:c:1-[#1])-[#1])-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[Br])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-c:4:c-3:c(:c(:c(:c:4-[#1])-[#1])-[#1])-[#1]",
        "styrene_imidazole_A(1)", "[#6](=[#6](-[#1])-[#6](-[#1])(-[#1])-n:1:c(:n:c(:c:1-[#1])-[#1])-[#1])(-[#6]:[#6])-[#6]:[#6]",
        "thiazole_amine_M(1)", "c:1(:n:c(:c(-[#1]):s:1)-c:2:c:c:n:c:c:2)-[#7](-[#1])-[#6]:[#6]-[#6](-[#1])-[#1]",
        "misc_pyrrole_thiaz(1)", "c:1(:n:c(:c(-[#1]):s:1)-c:2:c:c:c:c:c:2)-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]-[#6](-[#1])(-[#1])-c:3:c:c:c:n:3-[#1]",
        "pyrrole_L(1)", "n:1(-[#1]):c(:c(-[#6](-[#1])-[#1]):c(:c:1-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#6](=[#8])-[#8]-[#6](-[#1])-[#1]",
        "het_thio_65_D(1)", "c:2(:n:c:1:c(:c(:c:c(:c:1-[#1])-[F,Cl,Br,I])-[#1]):n:2-[#1])-[#16]-[#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6]",
        "ene_misc_E(1)", "c:1(:c(:c-2:c(:c(:c:1-[#8]-[#6](-[#1])-[#1])-[#1])-[#6]=[#6]-[#6](-[#1])-[#16]-2)-[#1])-[#8]-[#6](-[#1])-[#1]",
        "anil_OC_alk_E(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#8]-[#1])-[#6](-[#1])-[#1]",
        "melamine_B(1)", "n:1:c(:n:c(:n:c:1-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#6]-[#1])-[#6]=[#8]",
        "thio_cyano_A(1)", "[#7]-1(-[#1])-[#6](=[#16])-[#6](-[#1])(-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6]-1-[#6]:[#6])-[#1]",
        "cyano_amino_het_B(1)", "n:1:c(:c(:c(:c(:c:1-[#16;X2]-c:2:c:c:c:c:c:2-[#7](-[#1])-[#1])-[#6]#[#7])-c:3:c:c:c:c:c:3)-[#6]#[#7])-[#7](-[#1])-[#1]",
        "cyano_pyridone_G(1)", "[#7]-2(-c:1:c:c:c(:c:c:1)-[#8]-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](=[#6]-[#6](=[#7]-2)-n:3:c:n:c:c:3)-[#6]#[#7]",
        "het_65_J(1)", "o:1:c(:c:c:2:c:1:c(:c(:c(:c:2-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](~[#8])~[#8]",
        "ene_one_yne_A(1)", "[#6]#[#6]-[#6](=[#8])-[#6]#[#6]",
        "anil_OH_no_alk_B(1)", "c:2(:c:1:c(:c(:c(:c(:c:1:c(:c(:c:2-[#8]-[#1])-[#6]=[#8])-[#1])-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#1]",
        "hzone_acyl_misc_A(1)", "c:1(:c(:c(:c(:o:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6](-[$([#1]),$([#6](-[#1])-[#1])])-c:2:c:c:c:c(:c:2)-[*]-[*]-[*]-c:3:c:c:c:o:3",
        "thiophene_F(1)", "[#16](=[#8])(=[#8])-[#7](-[#1])-c:1:c(:c(:c(:s:1)-[#6]-[#1])-[#6]-[#1])-[#6](=[#8])-[#7]-[#1]",
        "anil_OC_alk_F(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#6]=[#8])-[#16]",
        "het_65_K(1)", "n1nnnc2cccc12",
        "het_65_L(1)", "c:1-2:c(-[#1]):s:c(:c:1-[#6](=[#8])-[#7]-[#7]=[#6]-2-[#7](-[#1])-[#1])-[#6]=[#8]",
        "coumarin_E(1)", "c:1-3:c(:c:2:c(:c:c:1-[Br]):o:c:c:2)-[#6](=[#6]-[#6](=[#8])-[#8]-3)-[#1]",
        "coumarin_F(1)", "c:1-3:c(:c:c:c:c:1)-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-c:2:n:o:c:c:2-[Br])-[#6](=[#8])-[#8]-3)-[#1]",
        "coumarin_G(1)", "c:1-2:c(:c:c(:c:c:1-[F,Cl,Br,I])-[F,Cl,Br,I])-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-[#1])-[#6](=[#7]-[#1])-[#8]-2)-[#1]",
        "coumarin_H(1)", "c:1-3:c(:c:c:c:c:1)-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-c:2:n:c(:c:s:2)-[#6]:[#16]:[#6]-[#1])-[#6](=[#8])-[#8]-3)-[#1]",
        "het_thio_67_A(1)", "[#6](-[#1])(-[#1])-[#16;X2]-c:2:n:n:c:1-[#6]:[#6]-[#7]=[#6]-[#8]-c:1:n:2",
        "sulfonamide_I(1)", "[#16](=[#8])(=[#8])(-c:1:c:n(-[#6](-[#1])-[#1]):c:n:1)-[#7](-[#1])-c:2:c:n(:n:c:2)-[#6](-[#1])(-[#1])-[#6]:[#6]-[#8]-[#6](-[#1])-[#1]",
        "het_65_mannich(1)", "c:1-2:c(:c(:c(:c(:c:1-[#8]-[#6](-[#1])(-[#1])-[#8]-2)-[#6](-[#1])(-[#1])-[#7]-3-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]-3)-[#1])-[#1])-[#1]",
        "anil_alk_A(1)", "[#6](-[#1])(-[#1])-[#8]-[#6]:[#6]-[#6](-[#1])(-[#1])-[#7](-[#1])-c:2:c(:c(:c:1:n(:c(:n:c:1:c:2-[#1])-[#1])-[#6]-[#1])-[#1])-[#1]",
        "het_5_inium(1)", "[#7]-4(-c:1:c:c:c:c:c:1)-[#6](=[#7+](-c:2:c:c:c:c:c:2)-[#6](=[#7]-c:3:c:c:c:c:c:3)-[#7]-4)-[#1]",
        "anil_di_alk_P(1)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c:1:s:c(:n:c:1:c:2)-[#16]-[#6](-[#1])-[#1]",
        "thio_urea_Q(1)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(-[#1]):c(:c:2-[#1])-[#1])-[#6](-[#6](-[#1])-[#1])=[#7]-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6]:[#6]:[#6])-[#1])-[#1])-[#1])-[#1]",
        "thio_pyridine_A(1)", "[#6]:1(:[#7]:[#6](:[#7]:[!#1]:[#7]:1)-c:2:c(:c(:c(:o:2)-[#1])-[#1])-[#1])-[#16]-[#6;X4]",
        "misc_phthal_thio_N(1)", "c:1(:n:s:c(:n:1)-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]-[#6](=[#8])-c:2:c:c:c:c:c:2-[#6](=[#8])-[#8]-[#1])-c:3:c:c:c:c:c:3",
        "hzone_acyl_misc_B(1)", "n:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6](-[#1])-c:2:c:c:c:c:c:2-[#8]-[#6](-[#1])(-[#1])-[#6](=[#8])-[#8]-[#1]",
        "tert_butyl_B(1)", "[#6](-[#1])(-[#1])(-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#8]-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-[#6](-[#1])(-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c(:c(:c:2-[#1])-[#1])-[#8]-[#1])-[#1]",
        "diazox_E(1)", "[#7](-[#1])(-[#1])-c:1:c(-[#7](-[#1])-[#1]):c(:c(-[#1]):c:2:n:o:n:c:1:2)-[#1]",
        "anil_NH_no_alk_B(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#7](-[#1])-[#16](=[#8])=[#8])-[#1])-[#7](-[#1])-[#6](-[#1])-[#1])-[F,Cl,Br,I])-[#1]",
        "anil_no_alk_A(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#7]=[#6]-2-[#6](=[#6]~[#6]~[#6]=[#6]-2)-[#1])-[#1])-[#1])-[#1])-[#1]",
        "anil_no_alk_B(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-n:2:c:c:c:c:2)-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1]",
        "thio_ene_amine_A(1)", "[#16]=[#6]-[#6](-[#6](-[#1])-[#1])=[#6](-[#6](-[#1])-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
        "het_55_B(1)", "[#6]-1:[#6]-[#8]-[#6]-2-[#6](-[#1])(-[#1])-[#6](=[#8])-[#8]-[#6]-1-2",
        "cyanamide_A(1)", "[#8]-[#6](=[#8])-[#6](-[#1])(-[#1])-[#16;X2]-[#6](=[#7]-[#6]#[#7])-[#7](-[#1])-c:1:c:c:c:c:c:1",
        "ene_one_one_A(1)", "[#8]=[#6]-[#6]-1=[#6](-[#16]-[#6](=[#6](-[#1])-[#6])-[#16]-1)-[#6]=[#8]",
        "ene_six_het_D(1)", "[#8]=[#6]-1-[#7]-[#7]-[#6](=[#7]-[#6]-1=[#6]-[#1])-[!#1]:[!#1]",
        "ene_cyano_E(1)", "[#8]=[#6]-[#6](-[#1])=[#6](-[#6]#[#7])-[#6]",
        "ene_cyano_F(1)", "[#8](-[#1])-[#6](=[#8])-c:1:c(:c(:c(:c(:c:1-[#8]-[#1])-[#1])-c:2:c(-[#1]):c(:c(:o:2)-[#6](-[#1])=[#6](-[#6]#[#7])-c:3:n:c:c:n:3)-[#1])-[#1])-[#1]",
        "hzone_furan_C(1)", "c:1:c(:c:c:c:c:1)-[#7](-c:2:c:c:c:c:c:2)-[#7]=[#6](-[#1])-[#6]:3:[#6](:[#6](:[#6](:[!#1]:3)-c:4:c:c:c:c(:c:4)-[#6](=[#8])-[#8]-[#1])-[#1])-[#1]",
        "anil_no_alk_C(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-c:2:c(-[#1]):c(:c(-[#6](-[#1])-[#1]):o:2)-[#6]=[#8])-[#1])-[#1]",
        "hzone_acid_D(1)", "[#8](-[#1])-[#6](=[#8])-c:1:c:c:c(:c:c:1)-[#7]-[#7]=[#6](-[#1])-[#6]:2:[#6](:[#6](:[#6](:[!#1]:2)-c:3:c:c:c:c:c:3)-[#1])-[#1]",
        "ene_cyano_G(1)", "n1(-[#6])c(c(-[#1])c(c1-[#6](-[#1])=[#6](-[#6]#[#7])-c:2:n:c:c:s:2)-[#1])-[#1]",
        "hzone_furan_E(1)", "[#8](-[#1])-[#6](=[#8])-c:1:c:c:c:c(:c:1)-[#6]:[!#1]:[#6]-[#6]=[#7]-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#8]",
        "het_6_pyridone_NH2(1)", "[#8](-[#1])-[#6]:1:[#6](:[#6]:[!#1]:[#6](:[#7]:1)-[#7](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](=[#8])-[#8]",
        "imine_one_fives_D(1)", "[#6]-1(=[!#6&!#1])-[#6](-[#7]=[#6]-[#16]-1)=[#8]",
        "pyrrole_M(1)", "n2(-c:1:c:c:c:c:c:1)c(c(-[#1])c(c2-[#6]=[#7]-[#8]-[#1])-[#1])-[#1]",
        "pyrrole_N(1)", "n2(-[#6](-[#1])-c:1:c(:c(:c:c(:c:1-[#1])-[#1])-[#1])-[#1])c(c(-[#1])c(c2-[#6]-[#1])-[#1])-[#6]-[#1]",
        "pyrrole_O(1)", "n1(-[#6](-[#1])-[#1])c(c(-[#6](=[#8])-[#6])c(c1-[#6]:[#6])-[#6])-[#6](-[#1])-[#1]",
        "sulfonamide_J(1)", "n3(-c:1:c:c:c:c:c:1-[#7](-[#1])-[#16](=[#8])(=[#8])-c:2:c:c:c:s:2)c(c(-[#1])c(c3-[#1])-[#1])-[#1]",
        "misc_pyrrole_benz(1)", "n2(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#8]-[#6]:[#6])c(c(-[#1])c(c2-[#1])-[#1])-[#1]",
        "thio_urea_R(1)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-[#6](-[#1])=[#6](-[#1])-[#6]=[#8]",
        "ene_one_one_B(1)", "[#6]-1(-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6]-[#6](-[#1])(-[#1])-[#6]-1=[#8])=[#6](-[#7]-[#1])-[#6]=[#8]",
        "dhp_amino_CN_H(1)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#16]-[#6;X4]-[#16]-1",
        "het_66_anisole(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-c:2:c:c:n:c:3:c(:c:c:c(:c:2:3)-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1]",
        "thiazole_amine_N(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#7](-[#1])-c:2:n:c(:c:s:2)-c:3:c:c:c(:c:c:3)-[#8]-[#6](-[#1])-[#1]",
        "het_pyridiniums_C(1)", "[#6]~1~3~[#7](-[#6]:[#6])~[#6]~[#6]~[#6]~[#6]~1~[#6]~2~[#7]~[#6]~[#6]~[#6]~[#7+]~2~[#7]~3",
        "het_5_E(1)", "[#7]-3(-c:2:c:1:c:c:c:c:c:1:c:c:c:2)-[#7]=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6]-3=[#8]",
        "ene_six_het_A(483)", "[#6]-1(-[#6](~[!#6&!#1]~[#6]-[!#6&!#1]-[#6]-1=[!#6&!#1])~[!#6&!#1])=[#6;!R]-[#1]",
        "hzone_phenol_A(479)", "c:1:c:c(:c(:c:c:1)-[#6]=[#7]-[#7])-[#8]-[#1]",
        "anil_di_alk_A(478)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c:c(:c(:c(:c:1)-[$([#1]),$([#6](-[#1])-[#1]),$([#8]-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])])-[#7])-[#1]",
        "indol_3yl_alk(461)", "n:1(c(c(c:2:c:1:c:c:c:c:2-[#1])-[#6;X4]-[#1])-[$([#6](-[#1])-[#1]),$([#6]=,:[!#6&!#1]),$([#6](-[#1])-[#7]),$([#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](-[#1])-[#1])])-[$([#1]),$([#6](-[#1])-[#1])]",
        "quinone_A(370)", "[!#6&!#1]=[#6]-1-[#6]=,:[#6]-[#6](=[!#6&!#1])-[#6]=,:[#6]-1",
        "azo_A(324)", "[#7;!R]=[#7]",
        "imine_one_A(321)", "[#6]-[#6](=[!#6&!#1;!R])-[#6](=[!#6&!#1;!R])-[$([#6]),$([#16](=[#8])=[#8])]",
        "mannich_A(296)", "[#7]-[#6;X4]-c:1:c:c:c:c:c:1-[#8]-[#1]",
        "anil_di_alk_B(251)", "c:1:c:c(:c:c:c:1-[#7](-[#6;X4])-[#6;X4])-[#6]=[#6]",
        "anil_di_alk_C(246)", "c:1:c:c(:c:c:c:1-[#8]-[#6;X4])-[#7](-[#6;X4])-[$([#1]),$([#6;X4])]",
        "ene_rhod_A(235)", "[#7]-1-[#6](=[#16])-[#16]-[#6](=[#6])-[#6]-1=[#8]",
        "hzone_phenol_B(215)", "c:1(:c:c:c(:c:c:1)-[#6]=[#7]-[#7])-[#8]-[#1]",
        "ene_five_het_A(201)", "[#6]-1(=[#6])-[#6]=[#7]-[!#6&!#1]-[#6]-1=[#8]",
        "anil_di_alk_D(198)", "c:1:c:c(:c:c:c:1-[#7](-[#6;X4])-[#6;X4])-[#6;X4]-[$([#8]-[#1]),$([#6]=[#6]-[#1]),$([#7]-[#6;X4])]",
        "imine_one_isatin(189)", "[#8]=[#6]-2-[#6](=!@[#7]-[#7])-c:1:c:c:c:c:c:1-[#7]-2",
        "anil_di_alk_E(186)", "[#6](-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[$([#1]),$([#6](-[#1])-[#1])])-[#6](-[#1])-[$([#1]),$([#6]-[#1])])-[#1])-[#1]"
    };
    public static String[] HITS_ARRAY = {
        "O=C1C(=O)C(Br)=C(Br)C(Br)=C1Br",
        "[N-]=[N+]=NC1=C(Cl)C(=O)C(Cl)=C(Cl)C1=O",
        "[N-]=[N+]=NC1=C(Cl)C(=O)C(N=[N+]=[N-])=C(Cl)C1=O",
        "[N-]=[N+]=NC1=C(Cl)C(=O)c2ncccc2C1=O",
        "O=C1C(=O)C(Cl)=C(Cl)c2ncccc21",
        "CN(C)C1=C(Cl)C(=O)C(=O)c2cccnc21",
        "[N-]=[N+]=C1C(=O)C=Cc2nc3ccccc3nc21",
        "O=C1C(=O)C(Cl)=C(NCc2ccccc2)c2ncccc21",
        "CCN(CC)C1=C(Cl)C(=O)C(=O)c2cccnc21",
        "O=C1C(=O)C(Cl)=C(NC2CC3CCC2C3)c2ncccc21",
        "CCCCCCCCCCCCNC1=C(Cl)C(=O)C(=O)c2cccnc21",
        "CN(C)c1ccc(C(=CC2=CC(=O)C(=O)c3ccccc32)c2ccc(N(C)C)cc2)cc1",
        "C[C@]12CC[C@@H]3C4=CC(=[N+]=[N-])C(=O)C=C4CC[C@H]3[C@@H]1CCC2=O",
        "O=C1C(=O)C(F)=C(F)C(F)=C1F",
        "CC1=C(C(=O)Nc2ccccc2)SCCO1.O=C1C(=O)C(Cl)=C(Cl)C(Cl)=C1Cl",
        "CC1=CSCCO1.O=C1C(=O)C(Cl)=C(Cl)C(Cl)=C1Cl",
        "O=C1C(Br)=CC(Br)=C/C1=N\\Br",
        "O=C1C(Cl)=CC(Cl)=C/C1=N/Cl",
        "[N-]=[N+]=NC1=C(Cl)C(=O)c2ccccc2C1=O",
        "CC(C)(C)C1=CC(C(C)(C)C)=C(Cl)C(=O)C1=O",
        "CC12CCC3C4=C(CCC3C1CCC2=O)C(=[N+]=[N-])C(=O)C=C4",
        "[N-]=[N+]=C1C=Cc2c(cccc2S(=O)(=O)Nc2ccc(-c3ccc(NS(=O)(=O)c4cccc5c4C=CC(=[N+]=[N-])C5=O)cc3)cc2)C1=O",
        "NS(=O)(=O)c1ccc(N=Nc2nc(CC(NC3=CC(=O)C(=O)c4ccccc43)C(=O)O)c[nH]2)cc1",
        "NS(=O)(=O)c1ccc(N=Nc2nc(CCNC3=CC(=O)C(=O)c4ccccc43)c[nH]2)cc1",
        "CC(C)C1CCC2C(CCC3C(C)(CNS(=O)(=O)c4cccc5c4C=CC(=[N+]=[N-])C5=O)CCCC23C)C1",
        "COC1=C(Cl)C(=O)C(=O)c2ccccc21",
        "[N-]=[N+]=C1C=Cc2c(cccc2S(=O)(=O)Oc2ccc(C(=O)c3ccccc3)c(OS(=O)(=O)c3cccc4c3C=CC(=[N+]=[N-])C4=O)c2)C1=O",
        "N#CC1=C(Cl)C(C#N)=C(Cl)C(=[N+]=[N-])C1=O",
        "Cc1ccc(C)c2c1c(NS(=O)(=O)c1cccc3c1C=CC(=[N+]=[N-])C3=O)cc1cccc(C(C)C)c12",
        "[N-]=[N+]=C1C=Cc2c(cccc2S(=O)(=O)N2CCCC2)C1=O",
        "CN(C)c1ccc(C2=C(Cl)C(=O)C(c3ccc(N(C)C)cc3)=C(Cl)C2=O)cc1",
        "O=C1C(=O)C(Br)=Cc2ccccc21",
        "NC1=C(Br)C(=O)C(=O)c2ccccc21",
        "O=C1C(=O)C(Cl)=C(Cl)C(Cl)=C1Cl",
        "[N-]=[N+]=C1c2ccccc2-c2ccccc2C1=O",
        "[N-]=[N+]=C1C(=O)C=Cc2ccccc21",
        "O=C(CC(=O)c1ccccc1NC1=Cc2ccccc2C(=O)C1=O)C(=O)NC1=C(Cl)C(=O)c2ccccc2C1=O",
        "CSc1c2c3c(n1C)C(=O)C(=O)C(Cl)=C3NCC2",
        "O=C1C(=S)C=C(Nc2ccccc2S)c2ccccc21",
        "Cn1cc2c3c1C(=O)C(=O)C(Cl)=C3NCC2",
        "O=C1C=C(CCCCCCCCc2ccc(Nc3c4ccccc4nc4ccccc43)cc2)c2ccccc2C1=O",
        "O=C1c2[nH]cc3c2C(=C(Cl)C1=O)NCC3",
        "C=C1CC[C@H](Cl)C(C)(C)[C@@H]1C[C@@]12OC(C)(C)[C@H](Cl)C[C@]1(Cl)C(=O)C1=C(C2=O)C(=[N+]=[N-])C(=O)C(C)=C1O"
    };
    public static String[] SMILES_ARRAY = {
        "CN(C)CCNC(=O)c1ccc(NC(=O)c2nc([N+](=O)[O-])cn2C)cc1",
        "CC1(C2Cc3c(O)c4c(c(O)c3CO2)C(=O)c2ccccc2C4=O)OCCCO1",
        "C=C1CC[C@H](OC(=O)/C=C(C)/C=C/C=C(C)/C=C/C2=C(C)CCCC2(C)C)C/C1=C/C=C1\\CCC[C@@]2(C)C1CCC2[C@H](C)/C=C/[C@H](C)C(C)C",
        "CC(=O)C1Cc2c(c(NCCNCCO)c3c(c2NCCNCCO)C(=O)c2ccccc2C3=O)CO1",
        "COC(=O)C(C(=O)C(=O)Nc1ccccc1C#N)c1nc2ccc([N+](=O)[O-])cc2nc1O",
        "CC(C)CCCC(C)[C@H]1CCC2[C@]1(C)CCC1C23C=CC2(C[C@@H](O)C4OC4[C@]12C)n1c(=O)n(-c2ccccc2)c(=O)n13",
        "O=C([CH-]C(=O)c1ccccc1)CCCCCCC(=O)[CH-]C(=O)c1ccccc1.O=C([CH-]C(=O)c1ccccc1)CCCCCCC(=O)[CH-]C(=O)c1ccccc1.O=C([CH-]C(=O)c1ccccc1)CCCCCCC(=O)[CH-]C(=O)c1ccccc1",
        "COc1cc(CNC2CCc3cc(OC)c(OC)c(OC)c3-c3ccc(SC)c(=O)cc32)cc(OC)c1",
        "Oc1nc2cc(Cl)ccc2nc1/C(=N\\Nc1ccccc1)C(O)c1ccc(Cl)c(Cl)c1",
        "CCOC(=O)C(C)(C)Oc1ccc2c(c1)oc(=O)c(-c1ccc(Cl)cc1)c2-c1ccc(Cl)cc1",
        "C=C1CN(S(=O)(=O)c2ccc(C)cc2)CCCN(Cc2ccccc2)CCCN(S(=O)(=O)c2ccc(C)cc2)C1",
        "C[N+](C)(C)CC(=O)N/N=C(/CC1OC(=O)c2ccccc21)c1ccccc1O",
        "O=C(O)CSCc1cc2c(O)c(c1)Cc1cc(CSCC(=O)O)cc(c1O)Cc1cc(CSCC(=O)O)cc(c1O)Cc1cc(CSCC(=O)O)cc(c1O)C2",
        "CCCCCCCCCCCCCCCCCCOCC(COP(=O)([O-])OC(CC(=O)O)C[N+](C)(C)C)OC",
        "C/C(=N/N(C=O)C[n+]1ccccc1)C(CN(CC(C)C)CC(C)C)C(=O)Nc1ccc(C)cc1C",
        "CCN(CC)C(=O)COc1c2cc(Br)cc1Cc1cc(Br)cc(c1OCC(=O)N(CC)CC)Cc1cc(Br)cc(c1OCC(=O)N(CC)CC)Cc1cc(Br)cc(c1OCC(=O)N(CC)CC)C2",
        "NC(=O)N/N=C(\\Cc1nc2ccc(Cl)cc2nc1O)C(=O)Nc1ccc(Cl)cc1Cl",
        "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCC=C(c1cc(Cl)c(O)c(C(=O)O)c1)c1cc(Cl)c(O)c(C(=O)O)c1",
        "O=C(CC(=O)N1N=C(c2ccccc2)C(N=Nc2ccc(Cl)cc2)C1=O)Nc1ccccc1Cl",
        "COCOCC(=O)C1CC2=C(CO1)C(=O)c1c(cc3ccccc3c1OC(C)=O)C2=O.COCOCC(=O)C1CC2=C(CO1)C(=O)c1cc3ccccc3c(OC(C)=O)c1C2=O",
        "COC(=O)C(=O)C(C(=O)C(=O)Nc1cc(C)ccc1C)c1nc2ccc(C(=O)c3ccccc3)cc2nc1O",
        "COC(=O)C(C(=O)C(=O)Nc1ccccc1C)c1nc2ccc([N+](=O)[O-])cc2nc1O",
        "CCOc1ccc(N=Nc2ccc3oc(=O)c(/C(C)=N\\NC(=O)CC(=O)Nc4ccc(Cl)cc4)cc3c2)cc1",
        "O=C(O)c1ccc(N=Nc2ccc(/C=C/c3ccc(N=Nc4ccc(C(=O)O)cc4O)cc3S(=O)(=O)O)c(S(=O)(=O)O)c2)c(O)c1",
        "CC[N+]1(C)CCC(O)(c2ccc(OC)cc2)C(C(=O)c2ccc(OC)cc2)C1",
        "C=C(C=O)[C@@H]1C2OC(=O)C(=C)[C@@H]2CC(O)[C@@]1(C)CC",
        "O=C(/C=C/c1ccc(OCC(=O)N2CCN(c3ccccc3)CC2)cc1)c1ccc(Cl)cc1",
        "Cc1ccc(S(=O)(=O)N/N=C(\\C(=O)Nc2cccc([N+](=O)[O-])c2)c2nc3ccc([N+](=O)[O-])cc3nc2O)cc1",
        "CCCCCCCCCCCCCC(=O)OC1C(O)C(CO)OC(OC)C1NC(=O)N(CCCl)N=O",
        "CCCNS(=O)(=O)OCCC(=O)N1CCN(C(=O)CCOS(=O)(=O)NCCC)CC1",
        "O=C(Nc1ccc(Cl)c(Cl)c1)C(=O)C(C(=O)c1ccccc1-c1ccccc1)C1OC(=O)c2ccccc21",
        "COc1cc([C@@H]2c3cc4c(cc3[C@@H](NC(=O)c3ccccc3)C3COC(=O)[C@@H]32)OCO4)cc(OC)c1O",
        "CN(C)CCNC(=O)c1nc(NC(=O)c2nc(NC(=O)CCCCCCC(=O)Nc3cn(C)c(C(=O)Nc4cn(C)c(C(=O)NCCN(C)C)n4)n3)cn2C)cn1C",
        "NS(=O)(=O)c1ccc(Nc2c3ccccc3nc3c(C(=O)N4CCN(CCO)CC4)ccc(Cl)c32)cc1",
        "COc1ccc(NC(=O)CC(=O)n2nc(-c3ccccc3)c(N=Nc3ccccc3C(=O)O)c2-c2ccccc2)cc1",
        "CCc1ccc(N2C(=N/C(F)(F)F)/C(=N\\C(F)(F)F)S/C2=N\\C)cc1",
        "CC(=O)NNc1nc(C)c(C(/C=C/c2ccc3c(c2)OCO3)=N/Nc2ccccc2)s1",
        "O=C(Nc1cccc(NC(=O)c2cc(=O)c3ccccc3o2)n1)c1cc(=O)c2ccccc2o1",
        "CC(C)NC(=O)OCc1nc(SCCO[Si](C)(C)C(C)(C)C)n(C)c1COC(=O)NC(C)C",
        "CC(C)(C)c1cc2c(OCC(=O)O)c(c1)COCc1cc(C(C)(C)C)cc(c1OCC(=O)O)COCc1cc(C(C)(C)C)cc(c1OCC(=O)O)COC2",
        "COCOc1c(OC)ccc(COCSC)c1[C@H]1C(O[Si](C)(C)C(C)(C)C)CCCC1S(=O)(=O)C(C)(C)C",
        "CCN(CC)CCNc1ccc2nnn3c2c1c(=O)c1cc([N+](=O)[O-])ccc13",
        "Cc1ccc(S(=O)(=O)ON/N=C2/C3OC3[C@@H]3CCCC[C@H]23)cc1",
        "COC(=O)c1ccccc1NC(=O)C(=O)CC(=O)CC(c1ccccc1)c1c(O)c2ccccc2oc1=O",
        "O=C(Cc1nc2ccc([N+](=O)[O-])cc2nc1O)C(=O)Nc1ccccc1[N+](=O)[O-]",
        "CSc1nc2c(c(NC3OCC(OC(C)=O)C(OC(C)=O)C3OC(C)=O)n1)C(=O)CO2",
        "CN(C)/N=C(/c1nc2ccc(Cl)cc2nc1O)C(O)c1ccc(C(O)/C(=N\\N(C)C)c2nc3ccc(Cl)cc3nc2O)cc1",
        "C=Cc1cc2c(cc1[C@@H]1C(O[Si](C)(C)C(C)(C)C)CCC1S(=O)(=O)C(C)(C)C)OCO2",
        "COc1ccc(-c2onc(-c3ccc([N+](=O)[O-])cc3)c2C2=NCCCN2)cc1",
        "c1nc2nc(-c3ccccc3)c3ccccc3n2n1.c1nnc2nc(-c3ccccc3)c3ccccc3n12",
        "O=C(O[C@@H]1[C@@H]2O[C@]2(CO)[C@@H]2[C@H]1C=CO[C@H]2OC1OC(CO)C(O)C(O)C1O)c1ccc(O)c(O)c1",
        "COC1=C(C)C(=O)C2=C(C1=O)[C@@H](COC(N)=O)C1(OC)[C@@H]3[C@H](CN21)N3C(=O)OCc1ccc(SSc2ccccn2)cc1",
        "COc1cc2cnc3c4ccccc4ccc3c2cc1OC.COc1cc2c[n+](C)c3c4ccccc4ccc3c2cc1OC",
        "CC(CCC(=O)O)[C@H]1CCC2[C@@H]3C(O)C[C@@H]4CC(O)CC[C@]4(C)C3CC(O)[C@@]21C",
        "CC(=O)OCC1([C@@H]2CC3=C(C(=O)c4c(cc5ccccc5c4OC(C)=O)C3=O)C(OC3CC(NC(=O)C(F)(F)F)C(O)C(C)O3)O2)OCCCO1.CC(=O)OCC1([C@@H]2CC3=C(C(=O)c4cc5ccccc5c(OC(C)=O)c4C3=O)C(OC3CC(NC(=O)C(F)(F)F)C(O)C(C)O3)O2)OCCCO1",
        "COc1ccc(CCN(C)C2CCC(C#N)(c3ccc(OC)c(OC)c3)CC2)cc1OC",
        "Cc1ccc([N+](=O)[O-])c2c(NCCNC(=O)Cn3ccnc3[N+](=O)[O-])ccnc12",
        "CC(CCCO)C1CCC2[C@@H]3C(O)CC4CC(O)CC[C@]4(C)C3CC[C@]12C",
        "N=C(N)NS(=O)(=O)c1ccc(NC(=O)c2cccc3cc4ccccc4nc32)cc1",
        "CC(=O)O.CC12CCC3(C1)[C@H](CC2=O)C[C@@H](OC(=O)c1ccccc1)C1C(C)(CO)CCC[C@@]13C",
        "COC(=O)C(=O)C(C(=O)C(=O)Nc1ccc([N+](=O)[O-])cc1C#N)c1nc2ccc([N+](=O)[O-])cc2nc1O",
        "Cn1cc(NC(=O)c2cc(NC(=O)c3cc(NC(=O)CCC(=O)Nc4cc(C(=O)Nc5cc(C(=O)Nc6cc(C(=O)NCCC(=N)N)n(C)c6)n(C)c5)n(C)c4)cn3C)cn2C)cc1C(=O)NCCC(=N)N",
        "CCOC(=O)c1cc(NCCCN)c2cccc(-c3cccc4c3ncc(C(=O)OCC)c4NCCCN)c2n1",
        "COC(=O)C(CC(=O)c1cc2c(cc1I)OCO2)Cc1cc(OC)c(OC)c(OC)c1I",
        "C=CCN(CC=C)CC(C(C)=O)C(c1ccccc1)c1c(O)c2ccccc2oc1=O",
        "O=C(C=Cc1ccccc1)C(C(=O)C(=O)Nc1ccc(Cl)c(C(F)(F)F)c1)C1OC(=O)c2ccccc21",
        "CC(=O)O[C@H]1CCC2[C@@H]3CCC4c5nonc5CC[C@]4(C)C3CC[C@@]21C",
        "COC(=O)Oc1cc(NC(=O)/C(C)=C(/C)C(=O)Nc2cc(OC(=O)OC)c(Cl)cc2F)c(F)cc1Cl",
        "C=CCN(N=O)C(=O)N(CCCC(NC(C)=O)C(=O)NCc1ccccc1)Cc1ccccc1",
        "[N-]=[N+]=NC1CC(n2cc(COCC(F)(F)F)c(=O)[nH]c2=O)OC1CO",
        "COC(=O)C(=O)C(C(=O)C(=O)Nc1cccc(C(C)=O)c1)c1nc2ccc([N+](=O)[O-])cc2nc1O",
        "C/C(=N\\N)C(CN1CCCCC1)C(c1ccccc1)c1c(O)c2ccccc2oc1=O",
        "COC(=O)CC(NC(=O)OCc1ccccc1)C(C(C)=O)C(=O)OCc1ccccc1",
        "O=c1[nH]c(=O)n(C2CC(O)C(CO)O2)cc1C(OCC(F)(F)F)OCC(F)(F)F",
        "CC(C)OC(=O)c1cc(NC(=S)c2ccccc2)cc(NC(=S)c2ccccc2)c1",
        "C=CCCCCCCCCCO[C@H]1CC[C@@]2(C)C(=CC=C3C4CCC([C@H](C)/C=C/[C@H](C)C(C)C)[C@@]4(C)CCC32)C1",
        "CCCN(CCC)S(=O)(=O)c1cc(S(=O)(=O)NC(=O)c2ccccc2)c(N)c(Cl)c1Cl",
        "COC(=O)C(/C(=N/N)C(=O)Nc1ccc(C)c(C)c1)c1nc2ccc(Cl)cc2nc1O",
        "COC(=O)Cn1c(=O)oc2ccc(OC(=O)Nc3cccc(C(F)(F)F)c3)cc21",
        "CC(C)(C)c1cc2c(OCCO)c(c1)Cc1cc(C(C)(C)C)cc(c1OCCO)Cc1cc(C(C)(C)C)cc(c1OCCO)Cc1cc(C(C)(C)C)cc(c1OCCO)Cc1cc(C(C)(C)C)cc(c1OCCO)Cc1cc(C(C)(C)C)cc(c1OCCO)Cc1cc(C(C)(C)C)cc(c1OCCO)Cc1cc(C(C)(C)C)cc(c1OCCO)C2",
        "Cc1cc2nc(/C(C#N)=C\\c3ccc(OCc4ccc(F)cc4)cc3)[nH]c2cc1C",
        "COc1ccc(-n2c(COc3ccc(Cl)cc3)nnc2SCC(=O)C(C)(C)C)cc1",
        "Cc1cc(C)c(S(=O)(=O)N2CCOC23CCN(C(=O)CC(C)C)CC3)c(C)c1",
        "COc1ccc2c(c1)OCCN(c1ccc(NC(=O)c3cccc(C)c3)cc1C(=O)O)C2",
        "Cc1cccc(-c2c3c(c4c(C)cc(C)nn24)n(C)c(=O)n(C)c3=O)c1",
        "Cc1ccc(NC(=O)COc2ccc(/C=C(/C#N)C(=O)N3CCOCC3)cc2)cc1",
        "O=C(N/N=C/c1ccc(OCc2ccccc2)cc1)c1ccc(CSCc2ccccc2)cc1",
        "CSc1ccc(-c2c/c(=N\\c3nc(-c4ccccc4)c(C)s3)c3ccccc3o2)cc1",
        "CCNc1ncc(C(=O)Nc2ccc(S(=O)(=O)N3CCOCC3)cc2)c(N[C@H]2CC[C@H](O)CC2)n1",
        "CCCCNc1ncc(C(=O)N2CCC(NC(=O)OC(C)(C)C)CC2)c(N[C@H]2CC[C@H](O)CC2)n1",
        "O=C(Nc1ccc(S(=O)(=O)N2CCOCC2)cc1)c1cnc(NC2CCOCC2)nc1N[C@H]1CC[C@H](O)CC1",
        "CCCCNc1ncc(C(=O)Nc2ccc(S(=O)(=O)N3CCOCC3)cc2)c(NCC2CC2)n1",
        "CCCCNc1ncc2c(-c3ccc(S(=O)(=O)O)cc3)nn(C[C@H]3CC[C@H](O)CC3)c2n1",
        "CCCCNc1ncc2c(-c3ccc(S(=O)(=O)N(C)c4ccc(C(=O)NCCOCC)cc4)cc3)nn([C@H]3CC[C@@H](O)CC3)c2n1",
        "O=C1c2ccccc2-c2c1c1ccc([N+](=O)[O-])cc1c(=O)n2C[C@@H](O)CO",
        "CCCCNc1ncc(C(=O)Nc2ccc(S(=O)(=O)N3CCOCC3)cc2)c(NCCC2CCOCC2)n1",
        "CCCCNc1ncc(C(=O)Nc2ccc(S(=O)(=O)N3CCOCC3)cc2)c(NCCN(CC)CC)n1",
        "FC(F)(F)c1ccc(-c2cnc(-c3ccc(C(F)(F)F)cc3)nc2N2CCC3(CC2)OCCO3)cc1",
        "CCCCNc1ncc2c(-c3ccc(C4NC(=O)NC(C)=C4C(C)=O)cc3)nn([C@H]3CC[C@@H](O)CC3)c2n1",
        "COc1cc2cc3c(nc2cc1OC)CCN3Cc1ccc(NC(=O)Nc2ccccc2)cc1",
        "Cc1ccc([C@H]2Nc3c(c(C(C)(C)C)nn3-c3ccccc3)C(=O)[C@]23CCCC3=O)cc1",
        "COc1nc(OC)nc(-c2cc(C(=O)n3ncc4ccccc43)n3ccc(C)cc23)n1",
        "COc1cc(-c2nc(-c3ccccc3)c(-c3ccccc3)n2C2CCCCC2)ccc1OC1CCCCC1",
        "CC(=O)c1c(C)c2cnc(Nc3ccc(N4CCNCC4)cn3)nc2n(C2CCCC2)c1=O",
        "C#CCn1c(-c2ccc(OC)c(OC3CCCCC3)c2)nc(-c2ccccc2)c1-c1ccccc1",
        "CCOC(=O)c1c2c(sc1NC(=O)c1cc(Cl)nc(Cl)c1)CN(C(=O)OC(C)(C)C)CC2",
        "Cc1ccc(/C=C/C(=O)NCC(C)(C)CNC(=O)/C=C/c2ccc(C)o2)o1",
        "Cn1c(=O)c(S(=O)(=O)c2ccccc2)cc2ccc3c(cnn3-c3ccc(Cl)cc3)c21",
        "CCc1nc(C)cn1CC1C(=O)O[C@@H]2[C@@H]3O[C@]3(C)CC/C=C(\\C)CC[C@@H]12",
        "COc1ccc(C(=O)/C=C/c2ccc(/C=C/c3cc(OC)ccc3OC)cc2)cc1",
        "CCN(CC)P(=O)(N(CC)CC)N(CCCSC/C=C(\\C)CC/C=C(\\C)CCC=C(C)C)CCCN1CCN(C)CC1",
        "Cc1nc2cc(Nc3nc(N4CCCC4)nc(N4CCOCC4)n3)ccc2n1Cc1ccccc1",
        "C/C1=C\\CC[C@@]2(C)O[C@H]2[C@H]2OC(=O)C(Cn3ccnc3I)[C@@H]2CC1",
        "COC(C(C)O)C(C)[C@H]1O[C@]1(C)CC(C)/C=C/C=C(\\C)C1OC(CC(=O)O)CCC1C",
        "CCc1ccc(-c2nc(CN3CCC(C(=O)N4[C@H](C)CN(c5ccccc5)C[C@@H]4C)CC3)c(C)o2)cc1",
        "CCc1ccc(-c2nc(CN3CCC(C(=O)N4CCN(c5ccccc5)CC4)CC3)c(C)s2)cc1",
        "COC(=O)[C@]12C3C(=O)N(c4ccc(C)cc4)C(=O)C3C(c3cn(-c4ccccc4)nc3-c3ccccc3)N1[C@H](c1cn(-c3ccccc3)nc1-c1ccccc1)[C@@H]1C(=O)N(c3ccc(C)cc3)C(=O)[C@@H]12",
        "COc1ccc(/C=C/c2cc(OC)cc(OC)c2/C=C(\\C#N)c2ccc(OC)cc2)cc1",
        "COc1cc2c3c(n(CCNCCNCCn4c5c(c6cc(OC)c(OC)cc6c4=O)C(=O)c4cc6c(cc4-5)OCO6)c(=O)c2cc1OC)-c1cc2c(cc1C3=O)OCO2",
        "O=C(CN1CCN(c2nnc(-c3ccccc3)c3ccccc32)CC1)Nc1nc(-c2ccc(Cl)cc2)cs1",
        "COc1cc2c3c(n(CCCN(CCOC(=O)c4cccnc4)C(=O)OC(C)(C)C)c(=O)c2cc1OC)-c1cc2c(cc1C3=O)OCO2",
        "COc1cc2c(cc1NC(=O)CCCN1C(=O)c3ccccc3C1=O)oc1ccccc12",
        "C[C@H]1O[C@@H](Oc2ccc3ccccc3c2)[C@H](O)[C@@H](O)[C@@H]1O",
        "NCCCn1c2c(c3ccc([N+](=O)[O-])cc3c1=O)C(=O)c1ccc(O)cc1-2",
        "COc1ccc(/C=C(/C(=O)c2ccc(OC)cc2)S(=O)(=O)c2ccc(C)cc2)cc1",
        "Cc1nc(Nc2nc(=O)c(Cc3ccccc3)c(C)[nH]2)nc2cc3c(cc12)OCO3",
        "COC(COS(=O)(=O)O)C(=O)N[C@@H]1C(=O)N[C@H](CCCNC(=N)N)C(=O)N[C@H]2CCC(O)N(C2=O)[C@H](Cc2ccccc2)C(=O)N(C)[C@H](CCc2ccc(O)cc2)C(=O)N[C@H](C(C)C)C(=O)O[C@@H]1C",
        "COc1cc2nc(-c3ccccc3)n(-c3nnc(-c4ccccc4)s3)c(=O)c2cc1OC",
        "COc1cc(-c2c3sc(=O)[nH]c3nc3c2c(=O)[nH]n3C(C)C)cc2c1OCO2",
        "Cc1oc(-c2ccc(F)cc2)nc1CN1CCC(C(=O)N2CCN(c3ccc(F)cc3)CC2)CC1",
        "Cn1c(=O)c(S(=O)(=O)c2ccccc2)cc2c1-c1cnn(-c3cccc(Cl)c3)c1CC2",
        "CCCCCCCCCCCCCCNc1nc(-c2ccc(C(F)(F)F)cc2)ncc1-c1ccc(C(F)(F)F)cc1",
        "CCc1nc(C)cn1CC1C(=O)O[C@@H]2[C@@H]3O[C@]3(C)CC/C=C(/CO)CC[C@@H]12",
        "N#Cc1ccccc1S(=O)(=O)N1CCN(C(=O)COc2ccc(Cl)cc2Br)CC1",
        "CCOC(=O)c1c(-c2ccc(C)o2)csc1NC(=O)CN1CCC(C(N)=O)CC1",
        "COc1ccc(OC)c(-n2c(=O)c(-c3ccc(Cl)cc3)cc3c2CC(C)(C)CC3=O)c1",
        "O=S(=O)(c1cccs1)N1CCOC12CCN(S(=O)(=O)c1ccc(F)cc1)CC2",
        "Cc1cccc(N2C(=O)c3ccccc3/C(=C/c3ccc(Cl)c([N+](=O)[O-])c3)C2=O)c1",
        "O=C(c1n[nH]c2ccccc21)N1CCN(S(=O)(=O)c2ccc(Br)cc2)CC1",
        "O=S(=O)(Nc1cc(S(=O)(=O)N2CCOCC2)ccc1O)c1ccc2c(c1)OCCCO2",
        "N[C@H]1CCCNc2nc(ncc2C(=O)NCc2ccc(F)cc2)NCCCCCCNC1=O",
        "CS(=O)(=O)c1ccc(C(=O)OCc2cc(=O)oc3cc(O)ccc23)cc1[N+](=O)[O-]",
        "O=C(COc1cccc(C(F)(F)F)c1)N1N=C(c2ccc(Cl)cc2)CC1c1ccco1",
        "COc1ccc(OC)c(-n2c(=O)c(-c3nccs3)cc3c2CC(C)(C)CC3=O)c1",
        "Cc1ccc(C2=NN(C(=O)COc3ccc(C)cc3[N+](=O)[O-])C(c3ccco3)C2)cc1",
        "CCc1ccc(-c2nc(C(=O)N3CCN(S(=O)(=O)c4ccc(F)cc4F)CC3)cs2)cc1",
        "COc1ccc(C2=NN(C(=O)COc3ccc(C(C)=O)cc3)C(c3ccco3)C2)cc1",
        "CNC(=O)C1CN(CC(=O)N2N=C(c3ccc(C)cc3)CC2c2ccco2)c2ccccc2O1",
        "CS(=O)(=O)Nc1ccc(CNC(=O)c2sccc2Nc2ccnc(C(F)(F)F)c2)cc1",
        "CC(C)[C@H](CO)Nc1nc2c(ncn2C(C)C)c(Nc2ccc(C(=O)NCCNS(=O)(=O)c3cccc4c3cccc4N(C)C)c(Cl)c2)n1",
        "Cc1cccc(C(C)C)c1NC(=O)COC(=O)c1cc(-c2ccco2)nc2ccccc21",
        "CCOC(=O)C1CCN(S(=O)(=O)c2ccc3c4c2CC(=O)N4CC(C)S3)CC1",
        "Cc1cc2c3c(c1)C(C)=CC(C)(C)N3C(=O)C21N=C(N)NC(Nc2ccccc2)=N1",
        "O=C(c1c[nH]c(=S)n1-c1ccccc1)N1CCN(S(=O)(=O)c2ccccc2F)CC1",
        "O=C(COC(=O)c1cc(-c2ccco2)nc2ccccc21)Nc1ccc([N+](=O)[O-])cc1C(F)(F)F",
        "COC(=O)CCC(=O)Nc1cc(S(=O)(=O)Nc2ccc(OC)cc2)ccc1N1CCCC1",
        "COc1ccccc1NS(=O)(=O)c1cc(NC(=O)C2CCCC2)ccc1N1CCOCC1",
        "O=C(CSc1nnc2c3cc(Br)ccc3[nH]c2n1)N1N=C(c2ccccc2)CC1c1ccc(Cl)cc1",
        "CC(C)(C)NC(=O)NC(=O)COC(=O)c1c2ccccc2nc2c1CCC/C2=C/c1ccco1",
        "O=C(NCc1ccc(N2CCCC2=O)nc1)C(Oc1ccc(Cl)cc1Br)c1ccccc1",
        "O=C(CN(Cc1ccco1)C(=O)C(=O)Nc1ccc2c(c1)OCCO2)NC1CCCC1",
        "Cc1cc(C#N)c(C)n1-c1cc(S(=O)(=O)N2CCOCC2)ccc1N1CCCC1",
        "C/C(=N\\Nc1ccc(S(N)(=O)=O)cc1[N+](=O)[O-])c1ccc(O)cc1",
        "COc1ccc(N2CC(c3nc4ccccc4n3CC(=O)N(c3ccccc3)C(C)C)CC2=O)cc1",
        "Cc1c(C(=O)O)sc2nc(/C(Cl)=C/c3cccc(C)c3)[nH]c(=O)c12",
        "Cc1ccc(-c2cc(C(=O)OCC(=O)Nc3ccc(C#N)cc3)c3ccccc3n2)o1",
        "N#Cc1cc([N+](=O)[O-])ccc1NC(=O)COC(=O)c1cc(-c2ccco2)nc2ccccc21",
        "O=C(CSc1ccccc1)Nc1cc(S(=O)(=O)N2CCOCC2)ccc1N1CCOCC1",
        "CNS(=O)(=O)Cc1ccc(CNC(=O)c2c[nH]c(=S)n2-c2ccc(F)cc2)cc1",
        "Cc1nc(-c2ccc(CN(C(=O)COc3ccc(Cl)cc3Br)c3cccc(F)c3)o2)cs1",
        "COc1ccc(-c2nnc(SCC(=O)N3CCN(S(=O)(=O)c4ccccc4)CC3)n2N)cc1",
        "FC(F)(F)c1cc(-c2cnc(Cl)nc2NC2CCCCCC2)cc(C(F)(F)F)c1",
        "COc1ccc(C2=NN(C(=O)CNc3cccc(C#N)c3)C(c3ccco3)C2)cc1",
        "COc1cc(F)ccc1-c1nc2c(ncc(Br)c2NCCCNC(=O)C2CCC2)[nH]1",
        "CCCCNc1ncc(-c2ccc(CN3CCOCC3)cn2)c(N[C@H]2CC[C@H](O)CC2)n1",
        "CCCCNc1ncc2c(-c3ccc(S(=O)(=O)NCC4CCNCC4)cc3)nn([C@H]3CC[C@@H](O)CC3)c2n1",
        "CCCCNc1ncc2c(-c3ccc(N4CCOCC4)cc3)nn(C3CCC(O)CC3)c2n1",
        "Fc1ccc(Oc2ncnc3c(NCCCc4ccccc4)cn(CCC4CCNCC4)c32)cc1",
        "COc1ccc2sc(NC(=O)CN3CCN(c4[nH]c(=O)[nH]c(=N)c4C#N)CC3)nc2c1",
        "CCN1CCC(CCc2ccc(NC(=O)c3cccc4c3C(=O)N(C)C4)cc2)C(C)C1",
        "CCCCNc1ncc2c(-c3ccc(S(=O)(=O)N4CCN(CC)CC4)cc3)nn([C@H]3CC[C@H](O)CC3)c2n1",
        "CCCCNc1ncnc2c(N(C(=O)C3CCCCC3)C(=O)C3CCCCC3)c[nH]c21",
        "CCCCNc1ncc2c(-c3ccc(F)cc3)nn(C[C@H]3CC[C@@H](O)CC3)c2n1",
        "CCCCNc1ncc(C(=O)Nc2ccc(S(=O)(=O)N3CCOCC3)cc2)c(NCCc2ccc(F)cc2)n1",
        "CCCCNc1ncc2c(-c3ccc(S(=O)(=O)N4CCCCC4)cc3)nn([C@H]3CC[C@H](O)CC3)c2n1",
        "CCCCNc1ncc2c(-c3ccc(S(=O)(=O)NC)cc3)nn(CCC[C@H]3CC[C@H](O)CC3)c2n1",
        "N#C/C(=C\\c1c(OCc2ccccc2)ccc2ccccc21)c1nc2ccccc2[nH]1",
        "O=C(Cn1cnc2c(nnn2Cc2ccc(F)cc2)c1=O)Nc1ccc2c(c1)OCO2",
        "COc1ccc(CCNC(=O)CS(=O)(=O)Cc2nc(-c3ccccc3Cl)oc2C)cc1OC",
        "CC(=O)Nc1ccc(S(=O)(=O)Nc2c(=O)oc3ccccc3c2NC2CCCCC2)cc1",
        "CC(C)(C)c1cc(Cc2c(F)c(F)c(F)c(F)c2F)cc(C(C)(C)C)c1O",
        "CCCn1nc(C(=O)Nc2ccc(S(=O)(=O)NC(C)C)cc2)c2ccccc2c1=O",
        "CN(CC(=O)NCc1nc(-c2ccccc2)no1)S(=O)(=O)c1ccc(Cl)cc1",
        "CCCCNS(=O)(=O)c1ccc2nc(-c3cccnc3)cc(C(=O)NC(C)C)c2c1",
        "O=C1CN(CCC2=CCCCC2)[C@H]2CS(=O)(=O)C[C@H]2N1c1ccccc1",
        "CCOC(=O)C1=C(C)NC(=O)C1(NC(=O)c1ccc(OC)c(OC)c1)C(F)(F)F",
        "CC(C)C[C@H](NC(=O)N1CCn2c3ccccc3nc21)C(=O)N[C@@H](Cc1ccccc1)C(=O)O",
        "COc1cc(C(=O)Nc2ccc(N3CCCS3(=O)=O)cc2)c([N+](=O)[O-])cc1OC",
        "COc1ccc(CNC(=O)Cn2c3ccc(S(=O)(=O)N4CCCC4)cc3oc2=O)cc1",
        "CCCCN(C(=O)C1CCN(C(=O)c2ccccc2C)CC1)c1c(N)n(CCCC)c(=O)[nH]c1=O",
        "O=C(CNc1cc([N+](=O)[O-])ccc1NCCO)Nc1cccc(S(=O)(=O)N2CCCCC2)c1",
        "C[N+](CCCl)(CCCl)Cc1ccc(B(O)O)c(C[N+](C)(CCCl)CCCl)c1",
        "CSCCC1NC(=O)N(CC(=O)N2N=C(c3ccc(Cl)cc3)CC2c2ccco2)C1=O",
        "CC(=O)N1CCN(S(=O)(=O)c2cccc(C(=O)N(C)Cc3ccc(C)o3)c2)CC1",
        "Cc1cc(/C=C2/C(=N)N3C(=NC2=O)SN=C3S(C)(=O)=O)c(C)n1-c1ccc(F)cc1",
        "COc1cc(-c2nnc(COC(=O)CSCC(=O)Nc3cc(C)on3)o2)cc(OC)c1OC",
        "CC(Sc1nnc2n1CCCCC2)C(=O)Nc1cccc(S(=O)(=O)N2CCOCC2)c1",
        "Cc1cc(=O)oc2c3c(cc(OCC(=O)N4CCC(C(=O)O)CC4)c12)OC(C)(C)CC3",
        "O=C(Nc1ccccc1)N1CCC(C(=O)N2CCN(S(=O)(=O)c3cccc(F)c3)CC2)CC1",
        "CC(C)[C@H](NC(=O)N1CCn2c3ccccc3nc21)C(=O)N[C@H](C(=O)O)c1ccccc1",
        "O=C(CCC(=O)N1C[C@H]2C[C@@H](C1)c1cccc(=O)n1C2)NCc1ccccc1C(F)(F)F",
        "Cc1nn(S(=O)(=O)c2ccc(Cl)cc2)c(C)c1S(=O)(=O)N1CCOCC1",
        "CC(C)(C)c1ccc(C(=O)N2CCN(c3ccc(N4CCOCC4)nn3)CC2)cc1",
        "Cc1cccc(NC(=O)CN2CCN(C(=O)CN3C(=O)NC4(CCCCC4C)C3=O)CC2)c1C",
        "Cn1cc(C2=C(c3cn(CCCN/C(N)=N\\[N+](=O)[O-])c4ccccc34)C(=O)NC2=O)c2ccccc21",
        "CCOC(=O)C1CCN(C(=O)Nc2ccc(NC(=O)C3CCN(C(=O)OCC)CC3)cc2)CC1",
        "CN(Cc1ccco1)C(=O)Cn1c(=O)oc2cc(S(=O)(=O)NCc3ccccc3)ccc21",
        "Cc1noc(/C=C/c2ccccc2F)c1S(=O)(=O)N1CCC(C(=O)N2CCCCC2)CC1",
        "N#Cc1ccc(CN2CCN(C(=O)CNS(=O)(=O)c3ccc4c(c3)OCCCO4)CC2)cc1",
        "CC(=O)c1ccc(S(=O)(=O)N2CCN(CC(=O)NC(=O)NC3CCCCC3)CC2)cc1",
        "CCCCc1cc(=O)oc2cc(OCC(=O)N3C[C@H]4C[C@@H](C3)c3cccc(=O)n3C4)ccc12",
        "Cc1oc2c(cc3c(C)c(CCC(=O)N4C[C@H]5C[C@@H](C4)c4cccc(=O)n4C5)c(=O)oc3c2C)c1C",
        "CC(=O)c1ccc(NC(=O)CSC2=C(C#N)C(c3ccco3)C3=C(O)CCCC3=N2)cc1",
        "COc1ccccc1N1C(=O)[C@H]2NN=C(C(=O)CN3C(=O)c4ccccc4C3=O)[C@H]2C1=O",
        "COc1ccccc1NC(=O)C1CCN(S(=O)(=O)c2ccc3c(c2)NC(=O)CO3)CC1",
        "Cc1c2ccccc2oc1C(=O)Nc1cc(S(=O)(=O)N2CCOCC2)ccc1N1CCOCC1",
        "Cc1cc(C(=O)OCC(=O)Nc2ccc(S(=O)(=O)N3CCOCC3)cc2)c(C)o1",
        "O=C(c1ccc(S(=O)(=O)N2CCOCC2)cc1)N(c1ccc(F)cc1)C1C=CS(=O)(=O)C1",
        "O=C(CNC(=O)c1ccc(F)cc1)NCc1ccc(S(=O)(=O)N2CCOCC2)cc1",
        "CCOc1ccc(NC(=O)CSc2nnc(C)n2C2CC2)cc1S(=O)(=O)N1CCCC1",
        "CC1CN(S(=O)(=O)c2cccc(C(=O)Nc3ccccc3N3CCOCC3)c2)CC(C)O1",
        "Cc1coc2c1c(C)cc1oc(=O)c(CC(=O)NCC(=O)NCC(=O)O)c(C)c21",
        "O=C(c1ccccc1)N1CCN(c2ccc([N+](=O)[O-])c(NCc3cccnc3)c2)CC1",
        "Cc1c(NS(=O)(=O)c2ccc(N3CCN(C)CC3)c([N+](=O)[O-])c2)c(=O)n(-c2ccccc2)n1C",
        "CCN(C(=O)CN1CCN(c2ccccc2O)CC1)c1c(N)n(Cc2ccccc2)c(=O)[nH]c1=O",
        "O=S(=O)(c1ccc(/N=C(\\S)N2CCN(c3ccc(O)cc3)CC2)cc1)N1CCOCC1",
        "Cn1c2ncn(CC(=O)OCC(=O)NC3CCCc4ccccc43)c2c(=O)n(C)c1=O",
        "CSc1cccc(NC(=O)CN2C(=O)COc3ccc(S(=O)(=O)N4CCOCC4)cc32)c1",
        "COC(=O)C(CCCN1C(=O)c2ccccc2C1=O)(NC(C)=O)c1nnn(C)n1",
        "Cc1cc(C)cc(NC(=O)CN2CCN(CC/N=C/C3=C(O)CC(C)(C)CC3=O)CC2)c1",
        "CCOc1ccc(N2CC(C(=O)Nc3ccccc3C(=O)N3CCOCC3)CC2=O)cc1",
        "COc1ccccc1NS(=O)(=O)c1cc(NC(=O)CCNC(C)=O)ccc1N1CCCCC1",
        "CCS(=O)(=O)N1CCC(NC(=O)C(Cc2ccc(OC)c(OC)c2)NC(C)=O)CC1",
        "O=C(CCC(=O)N1CCc2ccccc2C1)N1CCN(S(=O)(=O)c2ccccc2)CC1",
        "CC1CCCCC12NC(=O)N(CC(=O)NC(=O)Nc1ccc3c(c1)OCCO3)C2=O",
        "Cc1ccc(C)c(NC(=O)CCNC(=O)N2C[C@H]3C[C@@H](C2)c2cccc(=O)n2C3)c1",
        "CCN(C(=O)CN1CCN(c2ccccc2OC)CC1)c1c(N)n(Cc2ccccc2)c(=O)[nH]c1=O",
        "Cc1c2c(nn1-c1ccc(C)cc1)c(=O)n(CCCC(=O)N1CCC(C)CC1)nc2C",
        "COc1ccc(-c2[nH]c(C(=O)NCC3CCCO3)cc3c4ccccc4nc2-3)cc1",
        "Cc1c2ccc(OCC(=O)NC(Cc3ccc(Cl)cc3)C(=O)O)cc2oc(=O)c1Cc1ccccc1",
        "O=C(CNc1cc(S(=O)(=O)N2CCOCC2)ccc1NCC1CCCO1)Nc1cccc(F)c1",
        "CC(C)[C@H](NC(=O)C1CCN(S(=O)(=O)c2ccc(F)cc2)CC1)C(=O)N1CCCC1",
        "COc1cc(OC)cc(C(=O)NC(=S)N2C[C@H]3C[C@H](C2)c2cccc(=O)n2C3)c1",
        "Cc1nn(CC(=O)Nc2ccc(C(=O)N3CCCCC3)cc2)c(=O)c2ccccc12",
        "CS(=O)(=O)c1nc(S(=O)(=O)c2ccc(Cl)cc2)c(N2CCC(O)CC2)s1",
        "O=C(Nc1nnc(C2=COCCO2)o1)c1ccc(S(=O)(=O)N2CCOCC2)cc1",
        "Cc1c(C)c2c(OCC(=O)NCc3cccnc3)cc3c(c2oc1=O)CCC(C)(C)O3",
        "CC(=O)Nc1ccc(S(=O)(=O)N2CCC(Oc3ccc(-n4cnnn4)cc3)CC2)cc1",
        "CCOc1cccc(-c2c(C(=O)N/N=C/c3ccncc3)nnn2-c2nonc2N)c1",
        "CCn1cc(C(=O)N[C@@H](CCSC)C(=O)NC(C)(C)CO)c(=O)c2cc3c(cc21)OCO3",
        "O=C(NCC1COc2ccccc2O1)c1ccc(F)c(S(=O)(=O)N2CCOCC2)c1",
        "CCCCN1C(=O)C(=O)N(CC(=O)Nc2cc(S(=O)(=O)N(C)C)ccc2C)C1=O",
        "CCOC(=O)c1[nH]c(C)c(C(=O)C2=C(O)C(=O)N(CCCN3CCOCC3)C2c2cccnc2)c1C",
        "CCc1nn(C(CC)C(=O)NCCc2ccc(OC)cc2OC)c(=O)c2cc3occc3n21",
        "COc1cc(C(c2sc3nc(C)nn3c2O)N2CC(C)OC(C)C2)cc(OC)c1OC",
        "CCc1nc(SCC(=O)NCc2ccc(F)cc2)c2c(=O)n(C)c(=O)n(C)c2n1",
        "CC(C)Cn1cc(C(=O)N2CCC3(CC2)OCCO3)c2c3ccccc3n(C)c2c1=O",
        "COc1ccc(CC(=O)N(C)CC(=O)Nc2ccc(F)cc2)cc1S(=O)(=O)N1CCOCC1",
        "CCOC(=O)N1CCN(C(=O)CCS(=O)(=O)c2cc3c(cc2Cl)NC(=O)CO3)CC1",
        "CC(NCC(=O)Nc1ccccc1C(=O)NC1CC1)c1ccc(S(N)(=O)=O)cc1",
        "COC(=O)CC1C(=O)NCCN1C(=O)c1ccc(S(=O)(=O)N2CCOCC2)cc1",
        "COc1ccc2c(C)c(CC(=O)N3C[C@H]4C[C@@H](C3)c3cccc(=O)n3C4)c(=O)oc2c1C",
        "COCCN(C(=O)C1CCN(C(=O)c2ccccc2C)CC1)c1c(N)n(CC(C)C)c(=O)[nH]c1=O",
        "CCCn1c(O)nnc1SCC(=O)Nc1ccc(C)c(S(=O)(=O)N2CCOCC2)c1",
        "Cc1ccccc1-n1c(SCC(=O)N2CCN(c3ccccn3)CC2)nnc1N1CCOCC1",
        "CN1CCN(S(=O)(=O)c2ccc(N3CCN(c4ccccc4O)CC3)c([N+](=O)[O-])c2)CC1",
        "CCCCn1c(N)c(N(CCC(C)C)C(=O)CSc2nncn2-c2ccccc2)c(=O)[nH]c1=O",
        "CCN(CC)S(=O)(=O)c1cccc(C(=O)OCC(=O)NCCN2C(=O)CSC2=O)c1",
        "CCc1cc(=O)oc2cc(C)cc(OC(C)C(=O)NC(Cc3c[nH]c4ccc(O)cc34)C(=O)O)c12",
        "CCCc1cc(=O)oc2c3c(cc(OCC(=O)NCc4ccccn4)c12)OC(C)(C)CC3",
        "Cn1cc(C2=C(c3c4ccccc4n4c3CN(C(N)=S)CC4)C(=O)NC2=O)c2ccccc21",
        "CN1CCN(C(=O)Cn2c3ccc(S(=O)(=O)NCc4ccccc4)cc3oc2=O)CC1",
        "COCCn1c(CN2CCN(C(=O)c3ccco3)CC2)nc2c1c(=O)n(C)c(=O)n2C",
        "COc1cc(C2C(C#N)=C(N)OC3=C2C(=O)CC(C)(C)C3)ccc1OCCN1CCOCC1",
        "Cc1ccc(NC(=O)C2CCN(S(=O)(=O)c3ccc4c(c3)NC(=O)CO4)CC2)cc1C",
        "O=C(CCc1ccc(S(=O)(=O)N2CCOCC2)cc1)Nc1ccc(N2CCOCC2)cc1",
        "COc1cc2c(cc1OCCCN)c1c(n(CCCN3CCOCC3)c2=O)-c2cc3c(cc2C1=O)OCO3",
        "COc1cccc(-n2c(C)cc(/C=C3/C(=N)N4C(=NC3=O)SN=C4S(C)(=O)=O)c2C)c1",
        "CCC(C)N(C(=O)COC(=O)c1cc([N+](=O)[O-])ccc1N)C1CCS(=O)(=O)C1",
        "COc1ccc(S(=O)(=O)N2CCC(C(=O)Nc3ccccc3N3CCCC3)CC2)cc1OC",
        "CC(C)(C)NC(=O)COc1coc(CN2CCN(C(=O)c3ccco3)CC2)cc1=O",
        "CN(C)S(=O)(=O)c1ccc(C(=O)Oc2coc(CSc3ncccn3)cc2=O)cc1",
        "O=[N+]([O-])c1cccc(S(=O)(=O)Nc2ccc(-c3ccc(N4CCOCC4)nn3)cc2)c1",
        "O=C(COc1ccc(Cl)cc1)NCc1nnc(SCC(=O)Nc2ccc3c(c2)OCCO3)o1",
        "COc1ccc(C2c3c(cc(C)n(CCN4CCOCC4)c3=O)OC(N)=C2C#N)cc1OC",
        "COc1ccc(C(=O)OCC(=O)Nc2ccc3c(c2)OCO3)cc1S(=O)(=O)N1CCOCC1",
        "Cc1cc(C)n(-c2ccc(C(=O)OCC(=O)c3c(N)n(C)c(=O)n(C)c3=O)cc2)n1",
        "Cn1c2ccc(S(=O)(=O)N3CCC(N4CCCCC4)CC3)cc2n(C)c(=O)c1=O",
        "O=C1C2CC=CCC2C(=O)N1c1ccc(N2CCN(c3ccccn3)CC2)c([N+](=O)[O-])c1",
        "COc1ccc(CN(C)C(=O)Cc2ccc(S(=O)(=O)N3CCOCC3)s2)cc1OC",
        "CCCCn1c(N)c(N(CCOC)C(=O)C2CC(=O)N(c3cccc(SC)c3)C2)c(=O)[nH]c1=O",
        "CN(C)S(=O)(=O)c1cccc(NC(=O)c2ccc(S(=O)(=O)N3CCCC3)cc2)c1",
        "CCCn1c(N)c(C(=O)CN(C)CC(=O)Nc2cc(C)ccc2C)c(=O)[nH]c1=O",
        "COc1cc(-c2nnc(NC(=O)CCS(=O)(=O)c3ccc(F)cc3)o2)cc(OC)c1OC",
        "COc1ccc(NC(=O)CSc2nnc(C3CC3)n2C)cc1S(=O)(=O)N1CCOCC1",
        "CNC(=O)C1CCN(S(=O)(=O)c2c(C)noc2/C=C/c2ccc(OC)cc2)CC1",
        "Cc1nn(CC(=O)N(C)c2c(N)n(Cc3ccccc3)c(=O)[nH]c2=O)c(C)c1[N+](=O)[O-]",
        "CCN(C(=O)COC(=O)c1cccc(S(=O)(=O)N2CCOCC2)c1)C1CCS(=O)(=O)C1",
        "COc1ccc(S(=O)(=O)N2CCOC23CCN(S(=O)(=O)c2ccc(F)cc2C)CC3)cc1",
        "Cc1ncc(CCCN(C)C)cc1Nc1ncc2c(n1)-c1ccc(C(F)(F)F)cc1NC(=S)C2",
        "Cc1cc(S(=O)(=O)NC(=N)n2nccc2-c2ccccc2)c(SCc2cc3c(cc2Cl)OCO3)cc1Cl",
        "COc1ccc(N(C)C(=O)c2cnc(N3CCN(c4ncccn4)CC3)c3ccccc23)cc1",
        "CC(=O)OCC(OC(C)=O)C(OC(C)=O)C(OC(C)=O)C(=O)Nc1cccc(C)c1",
        "Cc1ccc(S(=O)(=O)N2CCC(C(=O)N3CCC(C(=O)NCc4cccnc4)CC3)CC2)cc1",
        "COc1cc(NC(=O)CSc2nc3c(c(=O)n(C)c(=O)n3C)n2C)cc(OC)c1",
        "CC(=O)c1ccc(-n2nnnc2SCC(=O)N2CCN(C(=O)c3ccco3)CC2)cc1",
        "CC(OC(=O)CCNC1=NS(=O)(=O)c2ccccc21)C(=O)Nc1ccc(S(N)(=O)=O)cc1",
        "Cc1ccnc(NS(=O)(=O)c2ccc(NC(=O)CN3C(=O)CCC3=O)cc2)n1",
        "CCOC(=O)N1CCC(=NNc2ccc(S(=O)(=O)N3CCOCC3)cc2[N+](=O)[O-])CC1",
        "COc1cc(S(=O)(=O)N2CCOCC2)cc(C(=O)OCC(=O)NC2CCCCC2)c1OC",
        "O=C(CN1CCN(CN2C(=O)NC3(CCCCCC3)C2=O)CC1)Nc1ccc(F)cc1",
        "N#Cc1ccccc1S(=O)(=O)N1CCN(C(=O)[C@H](Cc2ccccc2)NC(=O)c2ccco2)CC1",
        "O=C(COC(=O)c1ccc(NS(=O)(=O)c2ccc3c(c2)OCCO3)cc1)NC1CC1",
        "CC(=O)Nc1ccc(S(=O)(=O)NC2(C(F)(F)F)NC(=O)N(C3CCCC3)C2=O)cc1",
        "CCOc1ccc(S(=O)(=O)N(CC(=O)N2CCC(C(N)=O)CC2)c2ccc(F)cc2)cc1",
        "COC(=O)Nc1ccc(S(=O)(=O)N2CCC(C(=O)N(C)C3CCCCC3)CC2)cc1",
        "Cc1noc(C)c1S(=O)(=O)N1CCC(C(=O)N2CCN(c3cc(C)ccc3C)CC2)CC1",
        "C/C(=N\\NC(=O)c1nnn(-c2nonc2N)c1CN(C)c1ccccc1)c1cccnc1",
        "CC(C)C[C@@H](C(=O)NCc1ccco1)N1C(=O)N2CCc3c4ccccc4[nH]c3[C@@]2(C)C1=O",
        "COc1ccc(CN2/C(=N/C(=O)C3CCCO3)SC3CS(=O)(=O)CC32)cc1OC",
        "O=c1cc(CN2CCN(S(=O)(=O)c3ccc4ccccc4c3)CC2)nc2sccn21",
        "COc1ccc(-c2nc(CS(=O)(=O)CC(=O)NCc3cccc(F)c3)c(C)o2)cc1",
        "COc1ccccc1NS(=O)(=O)c1cc(C(=O)N2CCN(C(=O)c3ccco3)CC2)ccc1F",
        "CNS(=O)(=O)c1cccc(C(=O)OCC(=O)N2c3ccccc3NC(=O)C2(C)C)c1",
        "Cc1ccc(CNC(=O)CC2C(=O)Nc3c2c(=O)n(C)c(=O)n3Cc2ccccc2)cc1",
        "CC(C)n1c(CN2CCN(C(=O)c3ccco3)CC2)nc2c1c(=O)n(C)c(=O)n2C",
        "Cc1c(Br)c([N+](=O)[O-])nn1CC(=O)Nc1c(C)n(C)n(-c2ccccc2)c1=O",
        "CCN(CC)S(=O)(=O)c1ccc(C(=O)OCC(=O)c2ccc3c(c2)NC(=O)CO3)cc1",
        "O=C1CN(CCO)C(=O)[C@@H]2Cc3c4ccccc4[nH]c3C(c3ccc(Cl)cc3)N12",
        "Cc1c(N/C=C2\\C(=O)NC(=O)N(Cc3ccco3)C2=O)c(=O)n(-c2ccccc2)n1C",
        "COc1ccc(NC(=O)N2C[C@@H]3C[C@@H](C2)c2cccc(=O)n2C3)c(OC)c1",
        "CC1CCN(C(=O)CC2C(=O)Nc3c2c(=O)n(C)c(=O)n3Cc2ccccc2)CC1",
        "CCC(=O)c1ccc(OCC(O)CN2CCN(S(=O)(=O)N3CCCCC3)CC2)cc1",
        "O=C(CSc1nc(N2CCOCC2)nc(N2CCOCC2)n1)Nc1cccc(C(F)(F)F)c1",
        "COc1cc(S(=O)(=O)N2CCOCC2)ccc1NC(=O)COC(=O)COc1ccccc1",
        "C=CCN(CC=C)C(=O)C1CCN(S(=O)(=O)c2ccc(-n3cnnn3)cc2)CC1",
        "CCOC(=O)c1c(C)[nH]c(C)c1S(=O)(=O)N(C)CC(=O)Nc1ccc(C)cn1",
        "CCN(C(=O)CN1C(=O)COc2ccc(S(=O)(=O)N3CCOCC3)cc21)c1ccc(OC)cc1",
        "CCOC(=O)C1=C(COC(=O)c2cc(S(=O)(=O)N3CCOCC3)ccc2F)NC(=O)NC1C",
        "CCC(C(=O)NCc1ccco1)n1nc(C)c2c(C)n(-c3ccccc3)nc2c1=O",
        "Cc1ccc(S(=O)(=O)N2CCOCC2)cc1NC(=O)CN(C)C1CCS(=O)(=O)C1",
        "CC(=O)Nc1ccc(S(=O)(=O)Nc2c(C)n(C)n(-c3ccccc3)c2=O)cc1",
        "CC(=O)c1cccc(NC(=O)CSc2nnc(-c3ccc(S(=O)(=O)N4CCCC4)cc3)o2)c1",
        "CCCCN(C(=O)c1cccc(S(=O)(=O)N2CCOCC2)c1)c1c(N)n(CCC)c(=O)[nH]c1=O",
        "CC(=O)Nc1ccc(/C=N/NC(=O)c2cccc(S(=O)(=O)N3CCOCC3)c2)cc1",
        "COc1ccc(S(=O)(=O)N2CCN(C(=O)CNC(=O)c3ccc4c(c3)OCO4)CC2)cc1",
        "O=C(NCCCN1CCCC1=O)c1cccc(S(=O)(=O)N2CCN(c3ccccc3)CC2)c1",
        "Cc1ccc(S(=O)(=O)N2CCN(CC(=O)N/N=C/c3ccc(O)c(O)c3)CC2)cc1",
        "COc1cccc(NC(=O)CN2C(=O)COc3ccc(S(=O)(=O)N4CCOCC4)cc32)c1",
        "O=C(CCN1C(=O)NC2(CCCC2)C1=O)Nc1cccc(S(=O)(=O)N2CCOCC2)c1",
        "COc1cc(-c2nnc(SCC(=O)N(C)C3CCS(=O)(=O)C3)o2)cc(OC)c1OC",
        "O=C(c1ccc(S(=O)(=O)N2CCC(c3nc4ccccc4[nH]3)CC2)cc1)N1CCOCC1",
        "COc1ccc(OC)c(CN2CCN(c3ccc(S(=O)(=O)N4CCCC4)cn3)CC2)c1",
        "CCOC(=O)c1c(C)[nH]c(C(=O)CSc2nnc(-c3ccncc3)n2CC)c1C",
        "CN(C)S(=O)(=O)c1ccc(N2CCCC2)c(C(=O)N2CCC(C(N)=O)CC2)c1",
        "Cc1cc(C)n(-c2nc3c(c(=O)[nH]c(=O)n3C)n2Cc2cccc(Br)c2)n1",
        "COC(=O)C1CCN(C(c2sc3nc(C)nn3c2O)c2ccc(OC)c(OC)c2)CC1",
        "CC1CCN(C(=O)CSc2nnc(-c3ccc(S(=O)(=O)N4CCOCC4)cc3)n2C)CC1",
        "Cc1cc(NC(=O)C2CCN(S(=O)(=O)Cc3ccccc3)CC2)ccc1-n1cnnn1",
        "COCCNC(=O)CN(C(=O)c1ccc(N2CCCC2)nc1)c1ccc(F)c(Cl)c1",
        "O=C(O)CSc1nnc(-c2ccc(S(=O)(=O)N3CCCC3)cc2)n1Cc1ccco1",
        "CCOC(=O)C1CCN(C(=O)COc2ccc(S(=O)(=O)NC(C)C)cc2Cl)CC1",
        "CC(c1nnc(SCC(=O)N2CC(=O)Nc3ccccc32)n1Cc1ccccc1)N(C)C",
        "Cc1ccc(NC(=O)COC(=O)C2CCC(=O)N2)c(S(=O)(=O)N2CCOCC2)c1",
        "O=C(Cn1c(=O)cnc2ccccc21)N1CCN(S(=O)(=O)c2cccc([N+](=O)[O-])c2)CC1",
        "COc1cc(C(=O)N(C)CCOc2ccc(C)cc2)ccc1OC1CCN(C(C)=O)CC1",
        "CCc1nn(C(C)C(=O)N2CCN(Cc3ccccc3)CC2)c(=O)c2cc3occc3n21",
        "OCC1=C[C@@H](O)[C@@H]2C=CO[C@@H](OC3OC(CO)C(O)C(O)C3O)[C@H]12",
        "Cc1ccc(-c2nnn(CC(=O)N(CCO)C(C(=O)NC3CCCCC3)c3ccncc3)n2)o1",
        "COc1ccc(N(CC(=O)NCc2ccc3c(c2)OCO3)C(=O)CCC(=O)Nc2ccccn2)cc1",
        "CCN(C(=O)COC(=O)c1ccccc1SCC(=O)N1CCCC1)C1CCS(=O)(=O)C1",
        "Cc1ccc(C)c(N2CCN(C(=O)CCS(=O)(=O)c3ccc4c(c3)NC(=O)CO4)CC2)c1",
        "COCCN(C(=O)CN1CCCc2ccccc21)c1c(N)n(Cc2ccccc2)c(=O)[nH]c1=O",
        "COC(=O)c1[nH]c2ccccc2c1NC(=O)CN1CCN(C(=O)c2ccco2)CC1",
        "CC(C)CN(C(=O)COC(=O)c1cnccn1)c1c(N)n(Cc2ccccc2)c(=O)[nH]c1=O",
        "CCOC(=O)c1ccc(NC(=O)CC2SC(NNC3=NC(=O)CS3)=NC2=O)cc1",
        "COc1ccc(NC(=O)c2[nH]c(C)c(C(C)=O)c2C)cc1S(=O)(=O)N1CCOCC1",
        "COCCn1c(C)cc(C2=NN=C(Nc3ccc(S(=O)(=O)N4CCOCC4)cc3)SC2)c1C",
        "CCOC(=O)C1CCN(C(=O)CN(c2cccc(C(F)(F)F)c2)S(C)(=O)=O)CC1",
        "CCCCN(C(=O)C1=COCCO1)c1c(N)n(Cc2ccccc2)c(=O)[nH]c1=O",
        "O=C(CCCNC1=NS(=O)(=O)c2ccccc21)OCC(=O)N1CCc2ccccc21",
        "CCCn1c(N2CCN(CC(=O)c3c[nH]c4ccccc43)CC2)nc2c1c(=O)[nH]c(=O)n2C",
        "Cn1c(N)c(C(=O)CSc2nnc(C3CC3)n2-c2ccccc2)c(=O)n(C)c1=O",
        "CCN1C(=O)NC(=O)/C(=C\\c2cn(CC(=O)N3CCCCC3)c3ccccc23)C1=O",
        "Cc1ccccc1C(=O)NCc1nnc(SCC(=O)N2CCN(c3ccccc3)CC2)n1C",
        "CC(C)N(C(=O)COC(=O)[C@@H]1CC(O)CN1S(=O)(=O)c1ccccc1[N+](=O)[O-])C(C)C",
        "COCCN1C(=O)N(c2ccccc2)C(=O)C1CC(=O)Nc1cc(OC)cc(OC)c1",
        "COc1ccc2[nH]c3c(c2c1)CCN1C(=O)N([C@@H](C)C(=O)NCC2CCCO2)C(=O)[C@]31C",
        "COc1cc2[nH]c(=O)n(CCCCCC(=O)N3CCN(c4ccccn4)CC3)c(=O)c2cc1OC",
        "Cc1nc(C)c(C(=O)C2=C(O)C(=O)N(CCN3CCOCC3)C2c2ccco2)s1",
        "CN(Cc1nccn1C)C(=O)CC1C(=O)NCCN1CC(c1ccccc1)c1ccccc1",
        "CCOC(=O)N1CCN(C(=O)c2cn(C)c3ccc(S(=O)(=O)N(CC)CC)cc3c2=O)CC1",
        "CCOC(=O)c1cnc(SC2CC(=O)N(c3ccc(OC)c(OC)c3)C2=O)nc1N",
        "CC(=O)NS(=O)(=O)c1ccc(NCCc2c(C)nc3scc(-c4ccccc4)n3c2=O)cc1",
        "CCN(CC)C(=O)COC(=O)C1CCN(S(=O)(=O)c2ccc3c(c2)OCCO3)CC1",
        "CCCCn1c(N)c(N(CCOC)C(=O)c2ccc(N3CCCC3)c([N+](=O)[O-])c2)c(=O)[nH]c1=O",
        "CC(=O)Nc1ccc(C(=O)OCC(=O)N(c2ccccc2)C2CCS(=O)(=O)C2)cc1",
        "COc1ccccc1N1CCN(C(=O)Cn2cnc3c2c(=O)n(C)c(=O)n3C)CC1",
        "CCN(CC)S(=O)(=O)c1ccc2c(c1)N(CC(=O)N1CCC(C(N)=O)CC1)C(=O)CO2",
        "Cc1sc2ncn(CC(=O)NCCCC(=O)N3CCN(c4ncccn4)CC3)c(=O)c2c1C",
        "CCOc1ccccc1N(C)C(=O)CCS(=O)(=O)c1ccc2c(c1)NC(=O)CO2",
        "CCc1ccc(-c2nnn(CC(=O)N(C3CCCC3)C3CCS(=O)(=O)C3)n2)cc1",
        "O=C(CN1C(=O)NC2(CCSC2)C1=O)N(Cc1ccccc1)C1CCS(=O)(=O)C1",
        "CC(=O)OC[C@H]1O[C@@H](n2cc(Cn3cc([Si](C)(C)C)nn3)c(=O)[nH]c2=O)C[C@H]1OC(C)=O",
        "CC(C)(O)c1ccc(N2CCN(c3nnc(Cc4ccccc4)c4ccccc43)CC2)nc1",
        "COc1ccc(CNC(=O)COc2cc(O)c3c(c2)OC(C)(C)CC3=O)c(OC)c1",
        "CCCc1cc(=O)oc2c3c(cc(OCC(=O)NC[C@H]4CC[C@H](C(=O)O)CC4)c12)OC(C)(C)CC3",
        "O=C(COc1ccc2c(c1)oc(=O)c1c2CCCC1)N1C[C@H]2C[C@@H](C1)c1cccc(=O)n1C2",
        "CCN1CCN(C(=O)C2CCN(S(=O)(=O)N3CCC4(CC3)OCCO4)CC2)CC1",
        "COc1ccc(CN(C)C(=O)Cc2ccc(OC)c(S(=O)(=O)N3CCOCC3)c2)cc1OC",
        "COc1ccc(CNC(=O)C2CCCN(S(=O)(=O)c3c(C)noc3/C=C/N(C)C)C2)cc1",
        "COc1cccc(CNC(=O)C2CCCN(S(=O)(=O)c3c(C)noc3/C=C/N(C)C)C2)c1",
        "CCOc1ccccc1N1CCN(CC(=O)N(C2CCCC2)C2CCS(=O)(=O)C2)CC1",
        "CCOC(=O)N1CCN(c2c(=O)c(=O)c2N2CCN(c3ccccc3F)CC2)CC1",
        "CCCCCNC(=O)[C@@H]1CCCN2C(=O)CC[C@H](NC(=O)[C@H](Cc3ccc(OP(=O)(O)O)cc3)NC(C)=O)C(=O)N12",
        "CCOC(=O)C1CCN(C(=O)CCS(=O)(=O)c2ccc3c(c2)CCN3C(=O)CC)CC1",
        "Cc1noc(/C=C/N(C)C)c1S(=O)(=O)N1CCC(C(=O)Nc2ccc(Cl)cc2)CC1",
        "COc1ccc(N2CCN(C(=O)CCS(=O)(=O)c3ccc4c(c3)NC(=O)CO4)CC2)cc1",
        "COc1ccc(S(N)(=O)=O)cc1C(=O)N(C)CC(=O)Nc1ccc(N2CCOCC2)cc1",
        "COc1ccccc1C(=O)NC(=O)COC(=O)Cn1cnc2c1c(=O)n(C)c(=O)n2C",
        "Cc1cc(C(=O)CN2CCN(C3CCS(=O)(=O)C3)CC2)c(C)n1CCc1ccccc1",
        "CCN(CC)S(=O)(=O)c1ccc2c(c1)nc(CCC(=O)Nc1ccc3c(c1)OCO3)n2C",
        "O=C(CN1CCN(C(=O)CNC(=O)c2ccc3c(c2)OCO3)CC1)Nc1ccc(F)cc1",
        "CCOc1ccc(NC(=O)CN2CCN(CC(=O)Nc3ccc4c(c3)OCO4)CC2)cc1",
        "COc1ccccc1N1CCN(C(=O)c2cccc(NC3=NC4CS(=O)(=O)CC4S3)c2)CC1",
        "CCN(C(=O)c1ccc(S(=O)(=O)N2CCC(C)CC2)cc1)C1CCS(=O)(=O)C1",
        "O=S(=O)(O)c1cc(N=Nc2cc(S(=O)(=O)O)c3cccnc3c2O)ccc1/C=C/c1ccc(N=Nc2cc(S(=O)(=O)O)c3cccnc3c2O)cc1S(=O)(=O)O",
        "CC[C@H](/C=C/[C@@H](C)C1CCC2[C@@H]3CC=C4C[C@@H](OC(=O)/C=C(C)/C=C/C=C(C)/C=C/C5=C(C)CCCC5(C)C)CC[C@]4(C)C3CC[C@]12C)C(C)C",
        "C/C(=N\\NS(=O)(=O)c1ccc(C)cc1)C(CN1CCCCC1)C(c1ccccc1)c1c(O)c2ccccc2oc1=O",
        "NC(=O)c1cc(N(CCI)CCI)c([N+](=O)[O-])cc1[N+](=O)[O-]",
        "CC(C)N(C(=O)C12C3C4C1C1(C(=O)O)C2C3C41C(=O)N(C(C)C)C(C)C)C(C)C",
        "C[C@]12CCC3[C@@H](CCC45OC4C(=O)C(S(C)(=O)=O)C[C@]35C)C1CC[C@@H]2O",
        "COc1ccc(C(=O)ONC(=O)c2cc(OCC(F)(F)F)ccc2OCC(F)(F)F)cc1",
        "CCOc1ccc2nc(NC(=O)/C(CC(=O)CC(c3ccccc3)C3=C(O)c4ccccc4CC3=O)=N\\O)sc2c1",
        "CCCCCCCC(=O)NC(CC(C)C)C(=O)NC(CC(C)C)C(=O)NC(CCCCN)C(=O)NC(CCCCN)C(=O)NC(CC(C)C)C(=O)NC(CCCCN)C(=O)NC(CCCCCN)C(=O)NC(CC(C)C)C(=O)NC(CC(C)C)C(=O)NC(CCCCC)C(=O)NC(CCCCN)C(=O)NC(CC(C)C)C(N)=O",
        "O=C(Nc1cccc(NC(=O)C(=O)C2CCC3=C(Sc4ccccc4N3)C2=O)n1)C(=O)C1CCC2=C(Sc3ccccc3N2)C1=O",
        "COc1cc(/C=C2/C(=O)N(C(=O)c3ccccc3O)N=C2C)cc(OC)c1OC",
        "COc1cccc(N=Nc2ccc(N=Nc3sc4cc(Cl)cc(C)c4c3O)c(OC)c2)c1",
        "O=C(Nc1ccc(Cl)cc1)c1cc(Cl)c(=O)n(Cc2cccc(C(F)(F)F)c2)c1",
        "O=c1c([N+](=O)[O-])cc(C(F)(F)F)cn1Cc1ccc(Cl)c(Cl)c1",
        "CC(C)C1=CC23CCC4C(C)(C(=O)O)CCCC4(C)C2CC1C1C(=O)N(c2ccccc2N2C(=O)C4C(C2=O)C25C=C(C(C)C)C4CC2C2(C)CCCC(C)(C(=O)O)C2CC5)C(=O)C13",
        "C/C(COc1ccc(Oc2cnc3cc(Cl)ccc3n2)cc1)=N\\Nc1ccc([N+](=O)[O-])cc1[N+](=O)[O-]",
        "COc1c2c(cc3c1C(=O)N[C@@H]1C3=CC(OC(C)=O)[C@H]3OC(C)(C)O[C@H]31)OCO2",
        "C/C=C(\\C)C(=O)C(C)/C=C(\\C)C1OC(O)(C(C)(O)C(=O)NC2C(=O)N3NCCCC3C(=O)N(O)C(COC)C(=O)N(C)C(CC(C)C)C(=O)N3NCCCC3C(=O)NC(C(C)O)C(=O)OC2C(C)C)CCC1C",
        "O=[N+]([O-])c1cccc(/N=C2\\c3ccccc3CN2c2cccc([N+](=O)[O-])c2)c1",
        "Cc1c(/N=C\\c2ccccc2)/c(=N/NC(=O)c2ccccc2Cl)n(-c2ccccc2)n1C",
        "CC(=O)Nc1ccc(-c2nnc(SCC(=O)Nc3ccccc3[N+](=O)[O-])o2)cc1",
        "CC1(C)O[C@H]2[C@@H](O1)[C@H]1C(=CC2OCc2ccccc2)c2cc3c(c(OCc4ccccc4)c2C(=O)N1Cc1ccccc1)OCO3",
        "CN(C)CC(=O)Nc1ccccc1C(=O)Nc1nc2c(ccc(Oc3ccc(Cl)cc3)c2Cl)[nH]1",
        "O=[N+]([O-])c1ccc(C(Nc2ncc([N+](=O)[O-])cn2)Nc2ncc([N+](=O)[O-])cn2)cc1",
        "COc1ccc(NC(=O)C2C(=O)C=C(c3cc(OC)c(OC)c(OC)c3)CC2c2ccc([N+](=O)[O-])cc2)cc1",
        "C/C1=C\\C(O)C[C@@](C)(O)/C=C/C(C(C)C)CC(O)/C(C)=C/CC1",
        "ClC1=C(Cl)C(Cl)(Cl)c2c1c1c(c3c2C(Cl)=C(Cl)C3(Cl)Cl)C(Cl)=C(Cl)C1(Cl)Cl",
        "CN1c2ccc(Cl)cc2C(c2ccccc2Cl)=NC(OC(=O)CCCC(=O)O)C1=O",
        "O=C(CC1C(=O)c2ccccc2C1=C1C(=O)c2ccccc2C1=O)c1ccccc1",
        "CO[C@H](C(C)/C=C/N(C)C=O)C(C)C(=O)CC[C@H](C)C(O)[C@H](C)[C@H]1OC(=O)/C=C/C(C)=C/CC(O)CC2C=CC[C@@H](C[C@H](OC)[C@@H](C)C(OC)C[C@@H](O)[C@@H]1C)O2",
        "NS(=O)(=O)c1ccc(Cl)c(C2=NC(O)C(=O)Nc3ccc(Cl)cc32)c1",
        "CC1=NN(C(=O)c2ccccc2O)C(=O)/C1=C/c1cccc([N+](=O)[O-])c1",
        "COC(=O)C(=O)C(C(=O)C(=O)Nc1ccccc1C(N)=O)c1nc2ccc(Cl)cc2nc1O",
        "CC1(C)CC(=O)C(CC2C(=O)c3ccccc3C2=O)C(=O)C1C(=O)C(=O)Nc1cccc2ccc(O)cc21",
        "O=C(CSc1nc(-c2ccccc2)c(-c2ccccc2)[nH]1)N/N=C/c1ccc(Cl)cc1",
        "CN(C)CCNC(=O)c1nc(NC(=O)c2nc(NC(=O)CCCc3ccc(N(CCCl)CCCl)cc3)cn2C)cn1C",
        "CCCN1CN([C@@H](C(=O)N[C@@H]2C(=O)N3C2SC(C)(C)C3C(=O)O)c2ccc(O)cc2)CSC1=S",
        "O=C1C(=Cc2ccc([N+](=O)[O-])o2)C(=O)N(CCc2ccccc2)S(=O)(=O)N1CCc1ccccc1",
        "COc1cnc(NS(=O)(=O)c2ccc(NC3=C(Cl)C(=O)c4ccccc4C3=O)cc2)nc1",
        "CCOC(=O)N1C=NC23C(c4ccccc4)C(c4ccccc4)C12C(=O)N(C)C(=O)N3C",
        "Cc1ccnc(NS(=O)(=O)c2ccc(/N=C3\\C=C(Nc4ccc(S(=O)(=O)Nc5nccc(C)n5)cc4)C(=O)c4ccccc43)cc2)n1",
        "Cc1c(C)n(-c2ccccc2)c2nc(S(C)(=O)=O)nc(S(C)(=O)=O)c12",
        "Cc1noc(NS(=O)(=O)c2ccc(/N=C3\\C=C(Nc4ccc(S(=O)(=O)Nc5onc(C)c5C)cc4)C(=O)c4ccccc43)cc2)c1C",
        "CC(C)OC(=O)c1cc(N(C(=O)c2cc(Oc3ccc(C(F)(F)F)c(Cl)c3)ccc2[N+](=O)[O-])C(=O)c2cc(Oc3ccc(C(F)(F)F)cc3Cl)ccc2[N+](=O)[O-])ccc1Cl",
        "CCC(COC(=O)C1CC=CCC1C(=O)O)(COC(=O)C1CC=CCC1C(=O)O)COC(=O)C1CC=CCC1C(=O)O",
        "CCOC(=O)C1=C(C)Nc2cc(-c3ccccc3)n(-c3ccccc3OC)c(=O)c2C1c1ccccc1C(F)(F)F",
        "COc1ccc(C2S/C(=C\\c3cc(OC)c(OC)c(OC)c3)C(=O)N2NC(=O)Cc2ccccc2)cc1",
        "COc1cc(/C=C2/C(C)=NN(C(=O)c3ccc(NC(C)=O)cc3)C2=O)ccc1O",
        "COc1ccc(N2C(=O)C3CC(c4ccc(C)cc4)c4c5ccccc5[nH]c4C3C2=O)cc1",
        "CC(C)OP(=O)(OC(C)C)/C(=N/Nc1ccc([N+](=O)[O-])cc1)NNC(=O)c1ccccc1",
        "COc1ccc(S(=O)(=O)N(CCC(=O)NCO)CCC(=O)NCO)cc1N=Nc1c(O)c2c(cc(S(=O)(=O)O)cc2NC(C)=O)cc1S(=O)(=O)O",
        "CC[C@H](C)[C@@H]([C@@H](CC(=O)N1CCC[C@H]1[C@H](OC)[C@@H](C)C(=O)NC(Cc1ccccc1)C(N)=O)OC)N(C)C(=O)C(NC(=O)[C@H](C(C)C)N(C)C)C(C)C",
        "O=C(NCCNC(=O)c1cc(OCC(F)(F)F)ccc1OCC(F)(F)F)Nc1cccc(C(F)(F)F)c1",
        "COc1ccc(/C=c2\\sc3n(c2=O)C(N)=C(C#N)C(c2ccc(OC)cc2)C=3c2nc3ccccc3[nH]2)cc1",
        "CC1(C)CCC(C)(C)c2cc(C(=O)c3ccc4cc(C(=O)O)ccc4c3)ccc21",
        "COC1C(OC(N)=O)CC[C@]2(CO2)C1[C@@]1(C)O[C@@H]1CC=C(C)C",
        "CC(C)CC(N)C(O)C(O)C(=O)NC(CC(C)C)C1Cc2ccccc2C(=O)O1",
        "CCN(CC)C(=O)Oc1ccc(C(C)(C)c2ccc(OC(=O)N(CC)CC)cc2)cc1",
        "CC(C)(C)c1cc2c(OCC(=O)OCc3ccc([N+](=O)[O-])c(O)c3)c(c1)Cc1cc(C(C)(C)C)cc(c1OCC(=O)OCc1ccc([N+](=O)[O-])c(O)c1)Cc1cc(C(C)(C)C)cc(c1OCC(=O)OCc1ccc([N+](=O)[O-])c(O)c1)Cc1cc(C(C)(C)C)cc(c1OCC(=O)OCc1ccc([N+](=O)[O-])c(O)c1)C2",
        "C[N+](C)(Cc1ccc(NC(=O)CCCC(=O)ON2C(=O)CCC2=O)cc1)c1ccccc1",
        "COc1ccc(-c2ccccc2)cc1N(NC(=O)C(F)(F)F)C(=O)C(F)(F)F",
        "CC(=O)ON(C(C)=O)C1C(COC(c2ccccc2)(c2ccccc2)c2ccccc2)OC2OC3(CCCC3)OC21",
        "COC1O[C@@H]2COC(C)(C)O[C@@H]2C(=O)N1Nc1ccc([N+](=O)[O-])cc1",
        "C[C@]12CCC3[C@@H](CCC45OC4C(=O)[C@@](Br)(C#N)C[C@]35C)C1CC[C@@H]2O",
        "CCOC(=O)COc1c2cc(C(C)(C)C)cc1COCc1cc(C(C)(C)C)cc(c1OCC(=O)OCC)COCc1cc(C(C)(C)C)cc(c1OCC(=O)OCC)COC2",
        "C=CC[C@]1(C#N)C[C@H](C[C@@]2(C#N)C[C@H](CCl)OC(C)(C)O2)OC(C)(C)O1",
        "CC(=O)N[C@H](Cc1ccc2ccccc2c1)C(=O)NC(Cc1cccnc1)C(=O)NC(CO)C(=O)N[C@@H](Cc1ccc(O)cc1)C(=O)N[C@H](CCCCNC(=O)C1CNC(=O)N1)C(=O)N[C@@H](CC(C)C)C(=O)NC(CCCNC(=N)N)C(=O)C1CC[C@@H](C(=O)N[C@@H](C)C(N)=O)N1",
        "CCCCCCCCN(CCCCCCCC)C(=O)COc1c2cc(C(C)(C)C)cc1Cc1cc(C(C)(C)C)cc(c1OCC(=O)N(CCCCCCCC)CCCCCCCC)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N(CCCCCCCC)CCCCCCCC)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N(CCCCCCCC)CCCCCCCC)C2",
        "COc1cc2ccccc2cc1C(=O)NNC(=O)Nc1ccc(C)c(NC(=O)NNC(=O)c2cc3ccccc3cc2OC)c1",
        "CC1[C@H]2[C@H](CC3C4CCC5Cc6nc7c(nc6C[C@]5(C)C4C(=O)[C@@H](O)[C@@]32C)CC2CCC3C4CC5O[C@@]6(CC[C@@H](C)CO6)[C@H](C)C5C4(C)C(O)C(=O)C3C2(C)C7)O[C@]12CCC(C)CO2",
        "CC(C)=CC1C(C(=O)OCc2nc3ccccc3n2C(=O)C2C(C=C(C)C)C2(C)C)C1(C)C",
        "CC(C)CC(NC(=O)C(O)C(O)C1CCCN1)C1Cc2cccc(O)c2C(=O)O1",
        "COc1cccc2cc(CNCCN3C(=O)C4CC(C3=O)C3C5C6C7CC5C(C76)C43)oc21",
        "Cc1ccc(N2C(=O)/C(=C3\\SC(C(N)=O)=C(N)N3c3ccccc3)SC2=S)cc1",
        "COc1cc(Cn2c3ccccc3c3cc(C)c4c(c32)C=CC(C)(CCC=C(C)C)O4)cc2c3ccccc3[nH]c21",
        "N#C/C(=C\\c1ccc(O)c(O)c1)C(=O)NC1CCC(CC2CCC(NC(=O)/C(C#N)=C/c3ccc(O)c(O)c3)CC2)CC1",
        "CCCc1c(C(=O)OCC)[nH]c(-c2ccc(-c3[nH]c(C(=O)OCC)c(CCC)c3C(=O)OCC)s2)c1C(=O)OCC",
        "CCc1cc2c(OCC(=O)N(CCOC)CCOC)c(c1)Cc1cc(CC)cc(c1OCC(=O)N(CCOC)CCOC)Cc1cc(CC)cc(c1OCC(=O)N(CCOC)CCOC)Cc1cc(CC)cc(c1OCC(=O)N(CCOC)CCOC)Cc1cc(CC)cc(c1OCC(=O)N(CCOC)CCOC)Cc1cc(CC)c(cc1OCC(=O)N(CCOC)CCOC)Cc1cc(OCC(=O)N(CCOC)CCOC)c(cc1CC)C2",
        "O=c1c2ccccc2c(=O)c2c1ccc1[nH]c3c(ccc4c(=O)c5ccccc5c(=O)c43)[nH]c12",
        "CC(=O)N(C(C)=O)C1=C2c3ccccc3N(S(=O)(=O)c3ccccc3)C2n2c(=O)n(-c3ccccc3)c(=O)n2C1",
        "COc1ccc(-c2ccccc2)cc1N(NC(=O)C(F)(F)F)S(=O)(=O)C(F)(F)F",
        "COc1ccc(-c2ccccc2)cc1N(NC(=O)OC(C)(C)C(Cl)(Cl)Cl)C(=O)C(F)(F)F",
        "CCCCC(O)(c1ccccc1)C(O)(c1ccccc1)c1c(F)c(F)c(F)c(F)c1F",
        "COc1ccc(N2C(=O)C3c4[nH]c5ccccc5c4C4CCC(C(C)(C)C)CC4C3C2=O)c(C)c1",
        "CCN1CCC(O)(/C=C/c2ccc(Cl)cc2)C(C(=O)/C=C/c2ccc(Cl)cc2)C1",
        "O=[N+]([O-])c1cc(-c2cccc(-c3ccc(O)c([N+](=O)[O-])c3)c2)ccc1O",
        "COc1cnc([C@H]2CCC3[C@@H]4CC=C5C[C@@H](OC(=O)CCC(=O)O)CC[C@]5(C)C4CC[C@@]32C)s1",
        "O=C(Nc1cccc(C(F)(F)F)c1)c1cccn(Cc2cccc(C(F)(F)F)c2)c1=O",
        "COC(=O)[C@@H](C)NC(=O)COc1c2cc(C(C)(C)C)cc1Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@@H](C)C(=O)OC)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@@H](C)C(=O)OC)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@@H](C)C(=O)OC)C2",
        "CC(C)(C)NC(=O)/C(=C\\c1ccc(C(F)(F)F)cc1)NC(=O)c1ccccc1",
        "C[N+](C)(C)CC(=O)NC(=O)N/N=C\\C(CC#N)CN(Cc1ccccc1)Cc1ccccc1",
        "CC(=O)O[C@@H]1C2=C(C)[C@@H](OC(=O)C(O)C(NC(=O)c3ccccc3)c3ccc(Cl)cc3)C[C@@](O)(C(OC(=O)c3ccccc3)C3[C@]4(OC(C)=O)CO[C@@H]4C[C@H](O)[C@@]3(C)C1=O)C2(C)C",
        "COc1cc(C)cc2c(-c3c(O)cc(O)c4c3CC(C)N[C@H]4C)cc(-c3cc(-c4c(O)cc(O)c5c4CC(C)N[C@H]5C)c4cc(C)cc(OC)c4c3O)c(O)c12",
        "CC(C)CCCC(C)C1CCC2[C@@H]3CCC4C[C@@H](CCCO)CC[C@]4(C)C3CC[C@]12C",
        "COc1cc(-c2cc(-c3cc([N+](=O)[O-])c(/C=N/NC(=O)c4ccncc4)o3)ccc2OC)ccc1C",
        "O=[N+]([O-])c1c(/C=N/Nc2ccccc2)oc(-c2ccc3c(c2)OCO3)c1-c1ccc2c(c1)OCO2",
        "CCN(CC)[C@@H]1c2cc3c(cc2C(c2cc(OC)c(O)c(OC)c2)C2C(=O)CO[C@@H]21)OCO3",
        "O=S1c2ccc(Cl)cc2CC(SSC2Cc3cc(Cl)ccc3S(=O)c3ccccc32)c2ccccc21",
        "COC(=O)C(=O)C(C(=O)C(=O)Nc1cc(C)ccc1C)c1nc2ccccc2nc1O",
        "COC(=O)C1C=CCON2CCc3c4ccccc4n(S(=O)(=O)c4ccccc4)c3C12",
        "COc1cc(C2c3cc4c(cc3[C@@H](N(C)C)[C@H]3COC(=O)C32)OCO4)cc(OC)c1O",
        "C=CCn1c(=O)n(C2OC(COC(=O)COC)C(OC(=O)COC)C2OC(=O)COC)c2nc(NC(=O)COC)[nH]c(=O)c21",
        "CCCCN(CCCC)C1=S[Zn+2]2(S=C(N(CCCC)CCCC)[SH-]2)[SH-]1",
        "Cc1ccc(S(=O)(=O)N/N=C(\\CCN2CCCC2)CC(c2ccccc2)c2c(O)c3ccccc3oc2=O)cc1",
        "CCN(CC)CCCCC1CCN(CC(=O)N2c3ccccc3NC(=O)c3ccccc32)CC1",
        "O=C(NC1=NC2C=CC([N+](=O)[O-])=CC2S1)C(=O)Nn1c(S)nc2ccccc2c1=O",
        "Cc1nc2nc(C(F)(F)F)nn2c(C)c1CC(=O)NCC1(c2ccccc2)CCC1",
        "COc1ccc(NC(=O)c2cc(C3CCCCN3C(=O)c3ccccc3)on2)c(C)c1",
        "O=C(Nc1ccc2c(c1)OCO2)c1cc(C2CCCCN2C(=O)c2ccccc2)on1",
        "CN(C(=O)c1ccccc1C(=O)OCC(=O)N(Cc1ccccc1F)C1CC1)c1ccccc1",
        "O=C(COc1ccccc1C(=O)NCc1ccccc1)Nc1cc(C(F)(F)F)ccc1Cl",
        "CC(=O)Nc1cccc(NC(=O)CC2(C)N=C(c3cccc(Br)c3)N(C)O2)c1",
        "COc1ccc(S(=O)(=O)N2CCCC(c3cc(C(=O)NCc4cccnc4)no3)C2)cc1",
        "CC(C)(C)c1ccccc1NC(=O)c1csc(N2CCN(Cc3cccc(C(F)(F)F)c3)C2=O)n1",
        "Cc1cccc(NC(=O)c2c(C)cc(C)nc2SCC(=O)Nc2nc3ccccc3s2)c1",
        "Cc1noc(C)c1S(=O)(=O)Nc1ccc2nc(SCC(=O)Nc3ccc(C)cc3)sc2c1",
        "COc1ccc(-c2cc(C(=O)Nc3cccc(C)c3)nn2C2CCS(=O)(=O)C2)cc1",
        "Cn1c(CN(c2ccc(F)cc2)S(C)(=O)=O)nnc1SCC(=O)N1CCc2ccccc21",
        "O=C(Nc1ccc(OCc2ccccc2)cc1)C1=COC2=NC(=O)N(Cc3cccc(Cl)c3)CCN12",
        "CC(C)c1cc(C(=O)NCCc2ccc(S(N)(=O)=O)cc2)nn1C1CCS(=O)(=O)C1",
        "Cn1c(CN(c2ccc(F)cc2)S(C)(=O)=O)nnc1SCC(=O)Nc1ccc2c(c1)OCO2",
        "COc1ccc(-c2nnn3c2[nH]c(=O)c2ccc(C(=O)NC(C)c4ccccc4)cc23)cc1",
        "O=C(Nc1ccc(F)c(Cl)c1)C1=COC2=NC(=O)N(Cc3ccc(F)cc3)CCN12",
        "O=C(Nc1cccc(C(F)(F)F)c1)C1=CN2CCN(Cc3ccccc3C(F)(F)F)C(=O)N=C2O1",
        "O=C(Nc1cccc(OC(F)(F)F)c1)C1=COC2=NC(=O)N(Cc3cccc(Cl)c3)CCN12",
        "COc1ccc(S(=O)(=O)N2CCCCC2c2cc(C(=O)NCc3ccc(F)cc3)no2)cc1",
        "COc1ccc2c(c1)OCC(/C=C1\\N=C(c3ccc(NC(C)=O)cc3)OC1=O)=C2",
        "O=C(COC(=O)c1sccc1-n1cccc1)NC(=O)NC12CC3CC(CC(C3)C1)C2",
        "Cc1nc2ccccc2c(=O)n1-c1ccc(C(=O)N(Cc2ccc(F)cc2)C2CC2)cc1",
        "O=C(Nc1ccc(F)c(Cl)c1)c1cc(C2CCCN(C(=O)c3cccs3)C2)on1",
        "CC(C)c1ccc(C(=O)N2CCCC(c3cc(C(=O)NCc4cccnc4)no3)C2)cc1",
        "CN(C)C(CNC(=O)N1CCN(S(=O)(=O)c2ccc(Cl)cc2)CC1)c1ccsc1",
        "O=C(Nc1ccc2c(c1)OCO2)c1cc(C2CCCN(C(=O)C3CCCCC3)C2)on1",
        "O=C(Nc1ccc2c(c1)OCO2)c1cc(C2CCCCN2S(=O)(=O)c2ccccc2)on1",
        "CC(C)c1ccc(NC(=O)c2cnc(N3CCN(Cc4ccccc4C#N)C3=O)s2)cc1",
        "CC(=O)NC(C)(c1nc(-c2ccc(S(C)(=O)=O)cc2)cs1)c1ccccc1",
        "CNC(=O)c1nnc2n1C(c1ccccc1)CN(C(=O)c1cccc(OC(F)(F)F)c1)C2",
        "CC(=O)Nc1ccc(NC(=O)c2csc(N3CCN(Cc4ccccc4)C3=O)n2)cc1",
        "O=C(Nc1cccc(OC(F)(F)F)c1)c1cnc(N2CCN(Cc3ccccc3)C2=O)s1",
        "COc1ccc(Cl)cc1-c1cc(C(=O)NCc2ccc(C(F)(F)F)cc2)[nH]n1",
        "CC(=O)Nc1ccc(NC(=O)c2csc(N3CCN(CCc4ccccc4)C3=O)n2)cc1",
        "CN(C)C(=O)c1nnc2n1CCN(C(=O)C1(c3ccc(Cl)cc3)CCCC1)C2",
        "O=C(Nc1cccc(C(F)(F)F)c1)C1=CN2CCN(Cc3ccc(F)cc3)C(=O)N=C2O1",
        "COc1ccc(S(=O)(=O)N2CCCCC2c2cc(C(=O)Nc3ccccc3Cl)no2)cc1",
        "CC(=O)NCCNC(=O)c1nnc2n1CCN(C(=O)c1ccccc1Oc1ccccc1)C2",
        "COc1ccc(OC)c(CC(=O)N2CCn3c(nnc3C(=O)NCCNC(C)=O)C2)c1",
        "CC(C)NC(=O)c1cc(C2CCCCN2S(=O)(=O)c2cccc3cccnc32)on1",
        "Cn1c(=O)c2sc(-c3cccc(C(F)(F)F)c3)cc2nc1Oc1cccnc1CN1CCCC1",
        "CCN(CC)C(=O)c1cccc(-c2ccc3c(c2)COCC(=O)N3Cc2ccc(C)cc2)c1",
        "O=C(Nc1ccccc1)C1=COC2=NC(=O)N(Cc3ccccc3C(F)(F)F)CCN12",
        "O=C(NCCCc1ccccc1)C1=CN2CCN(Cc3cccc(Cl)c3)C(=O)N=C2O1",
        "COc1cc(NC(=O)CC2(C)N=C(c3ccc(N(C)C)cc3)N(C)O2)cc(OC)c1",
        "O=C(c1cnc(N2CCN(Cc3cccc(C(F)(F)F)c3)C2=O)o1)N1CCCC1",
        "CC(=O)Nc1ccc(NC(=O)C2=CN3CCN(Cc4c(C)noc4C)C(=O)N=C3O2)cc1",
        "CCN(CC)C(=O)c1cccc(-c2csc(C(C)(O)c3ccc(OC)cc3)n2)c1",
        "CC(=O)Nc1cccc(-c2ccc3c(c2)N(CCc2ccccc2)C(=O)COC3)c1",
        "O=C(NCc1ccc(F)c(F)c1)c1cnc(N2CCN(Cc3ccc(Cl)cc3)C2=O)o1",
        "Cc1ccccc1CN1CCN2C(C(=O)Nc3ccc(C(C)C)cc3)=COC2=NC1=O",
        "O=C(Nc1cccc(Cl)c1)C1=COC2=NC(=O)N(Cc3cccc(Cl)c3)CCN12",
        "CC(=O)Nc1cccc(-c2ccc3c(c2)N(Cc2cc(F)cc(F)c2)C(=O)COC3)c1",
        "O=C(NCCC(F)(F)F)c1cnc(N2CCN(Cc3cccc(C(F)(F)F)c3)C2=O)o1",
        "COc1ccc(-c2cc(CNc3nc4cc(-c5ccccc5C)sc4c(=O)n3C)on2)cc1",
        "O=C(Nc1ccc(F)c(Cl)c1)C1=COC2=NC(=O)N(Cc3ccccc3)CCN12",
        "COc1ccnc(N2CCN(C(=O)c3nn(-c4ccccc4[N+](=O)[O-])c(C)cc3=O)CC2)n1",
        "COC(=O)c1cccc(S(=O)(=O)NC(c2ccccc2)c2ccc(OC(F)F)cc2)c1",
        "COc1ccc(CNC(=O)Nc2ccc3c(c2)c(=O)n(C)n3Cc2cccc(C(F)(F)F)c2)cc1",
        "Cc1nc2nc(C(F)(F)F)nn2c(C)c1CCC(=O)N1CCCCCC1c1cccn1C",
        "O=S(=O)(Nc1ccc(F)c(-c2nnc3n2CCCCC3)c1)c1ccc2c(c1)OCCCO2",
        "CC(=O)c1ccc(N2CCN(C(=O)CCc3c(C)nc4ncnn4c3C)CC2)c(F)c1",
        "CCN(CC(=O)Nc1ccc(NC(C)=O)cc1)CC(=O)N1CCCc2cccc(F)c21",
        "O=C(CSc1nnc(N2CCCC2)n1-c1ccccc1Cl)NCc1ccc2c(c1)OCO2",
        "CC(C)[C@H](CO)Nc1nccc2[nH]c(-c3cccc(C(F)(F)F)c3)nc21",
        "COCCNc1oc2c(c1-c1ccc([N+](=O)[O-])cc1)c(=O)oc1ccccc21",
        "Cc1csc2nc(CSc3nnc(N4CCOCC4)n3-c3cccc(F)c3)cc(=O)n12",
        "Cc1cccc(-n2c(SCC(=O)NCc3ccc(Cl)cc3Cl)nnc2N2CCOCC2)c1",
        "C/C=C/CC(C)CC(C)C(=O)C1=C(O)C(C(O)c2ccc(O)cc2)NC1=O",
        "CC(=O)Nc1ccc(-c2nnc3ccc(-c4ccc(C(=O)N5CCOCC5)cc4)cn32)cc1",
        "C[C@@H]1CC[C@]2(C(=O)O[C@@H]3O[C@H](CO)[C@@H](O)[C@H](O)[C@H]3O)CC[C@]3(C)C(=CCC4[C@@]5(C)C[C@@H](O)[C@H](O)[C@](C)(C(=O)O)C5CC[C@]43C)[C@@H]2[C@]1(C)O",
        "CCC(C)CC(C)CC(CO)CC(C)/C=C(C)/C=C(/CC(C)C(=O)O)C(=O)O",
        "C=CCC1=C[C@]2(OC)[C@H](C)[C@@H](c3ccc(OC)c(OC)c3)O[C@]2(OC)CC1=O",
        "CN(C)CCCOc1ccc(-c2nnc3ccc(-c4ccc(F)c(Cl)c4)cn32)cc1",
        "CN1CCN(C(=O)c2cccc(-c3ccc4nnc(-c5cccc(C#N)c5)n4c3)c2)CC1",
        "COc1cc(-c2[nH]nc3ccnc(-c4ccc(C#N)cc4)c32)cc(OC)c1OC",
        "C/C=C(/C)C(=O)O[C@H]1CC(C)(C)CC2[C@]13[C@H](O)O[C@@]21CCC2[C@@]4(C)CC[C@H](O[C@@H]5O[C@H](C(=O)O)[C@@H](O)[C@H](O[C@@H]6O[C@H](CO)[C@H](O)[C@H](O)[C@H]6O[C@@H]6O[C@@H](C)[C@H](O)[C@@H](O)[C@H]6O[C@@H]6OC[C@](O)(CO)[C@H]6O)[C@H]5O[C@@H]5O[C@H](CO)[C@H](O)[C@H](O)[C@H]5O)C(C)(C)C4CC[C@@]2(C)[C@]1(C)C[C@H]3O",
        "CC1=C[C@H]2O[C@@H]3C[C@H]4OC(=O)/C=C\\C/C=C5\\OCC[C@@]6(O[C@@H]6C(=O)OC[C@@]2(CC1)[C@]4(C)[C@]31CO1)[C@@H]5O",
        "COc1ccc(-c2nnc3ccc(-c4cccc(C(=O)N(C)C)c4)cn32)cc1OC",
        "C/C(=C\\CCC(C)C1CC(O)C2(C)C3CCC4C5(CC35CC(O)C12C)CCC(OC1OCC(O)C(O)C1O)C4(C)C)C(=O)O",
        "Cc1cc2c3ccccc3n3c2c2c1O[C@]1(C)CC[C@@H]([C@@H]2C1)C3(C)C",
        "CC(=O)O[C@H]1CC2C(COC(=O)CC(C)C)=CO[C@@H](OC(=O)CC(C)C)C2[C@@]12CO2",
        "CC(=O)O[C@]1(C)[C@H](OC(=O)C2(C)OC2C)CC[C@]2(C)CC(=O)C(=C(C)C)C[C@H]21",
        "CN(C)CCNC(=O)c1cccc(-c2nccc3[nH]nc(-c4cccc(OC(F)(F)F)c4)c32)c1",
        "Cc1cc(C(=O)COC(=O)c2nc3nc(C)cc(C)n3n2)c(C)n1-c1ccccc1",
        "COc1ccc(-c2cc(CN(C)C(=O)C(NC(=O)C34CC5CC(CC(C5)C3)C4)C(C)C)on2)cc1",
        "Cc1nc2nc(C(F)(F)F)nn2c(C)c1CCC(=O)N1CCCC(C(=O)Nc2ccccc2)C1",
        "O=C(c1cc(C2CC2)nc2c1nc1n2CCCCC1)N(Cc1ccc2c(c1)OCO2)C1CCCC1",
        "COc1cccc(-c2cnc3c(NC(C)=O)cc(-c4ccc(N5CCN(C)CC5)cc4)cn23)c1",
        "CC(C)c1cccc(-c2ccc3nnc(-c4ccc(OCCCN(C)C)cc4)n3c2)c1",
        "CC(=O)c1cccc(NC(=O)C2Cc3c(nccc3-c3ccccc3Oc3ccccc3)O2)c1",
        "CC1(C(=O)Nc2cccc(NS(C)(=O)=O)c2)Cc2c(nccc2-c2ccc(C(N)=O)cc2)O1",
        "CC(=O)N(C)c1cccc(-c2cc3cnc(Nc4cccc(OC(C)C)c4)cc3n2C)c1",
        "CC(=O)Nc1cccc(-c2cc(NC(C)=O)c3ncc(-c4ccccc4)n3c2)c1",
        "COc1cc(-c2ccc3ncc(-c4ccc(NC(C)=O)cc4)n3n2)cc(OC)c1OC",
        "COc1cc(Nc2nccc3c2cc(-c2ccc(N(C)C)cc2)n3C)cc(OC)c1OC",
        "COc1cc(-c2cc(NC=O)c3ncc(-c4ccc(C(C)=O)cc4)n3c2)cc(OC)c1OC",
        "COc1ccc(CNC(=O)C2(C)Cc3c(nccc3-c3ccc4c(c3)OCO4)O2)cc1OC",
        "COc1ccc(-c2ccc(C(=O)Nc3cc(OC)c(OC)c(OC)c3)c3occc32)c(C)c1",
        "CC(=O)Nc1ccc(NC(=O)C2(C)Cc3c(nccc3-c3ccc4c(c3)OCO4)O2)cc1",
        "CC1(C(=O)Nc2cccc(C(F)(F)F)c2)Cc2c(nccc2-c2cccc(C(N)=O)c2)O1",
        "COc1ccc(CNC(=O)C2(C)Cc3c(nccc3-c3cccc(C(N)=O)c3)O2)cc1OC",
        "CC(=O)Nc1cccc(-c2ccnc3c2CC(C)(C(=O)NCCC(F)(F)F)O3)c1",
        "CN(C)C(=O)c1ccc(-c2cc3c(ccnc3Nc3cccc(C(F)(F)F)c3)n2C)cc1",
        "COc1cc(NC(=O)[C@@H](C)N2Cc3ccc(NCC(C)C)nc3C2=O)cc(OC)c1",
        "COc1ccc(NC(=O)C2(C)Cc3c(nccc3-c3ccc4c(c3)OCO4)O2)cc1",
        "CC1(C(=O)NCc2cccc(Cl)c2)Cc2c(nccc2-c2ccc3c(c2)OCO3)O1",
        "CC(=O)Nc1ccc(-c2ccnc3c2CC(C)(C(=O)NCc2ccc(F)c(F)c2)O3)cc1",
        "O=C(Nc1cccc(OC(F)(F)F)c1)C1Cc2c(nccc2-c2ccc(Cl)cc2)O1",
        "COc1ccc(-c2cc(NC=O)c3ncc(-c4cccc(C(=O)N(C)C)c4)n3c2)cn1",
        "CNc1nc2cnc(Nc3ccc(OC)c(OC)c3)cc2n1-c1cccc(C(F)(F)F)c1",
        "CC(=O)Nc1cc(-c2ccc(C(=O)N3CCOCC3)cc2)cn2c(-c3ccc(C(C)=O)cc3)cnc12",
        "CNc1nc2cnc(Nc3ccc(C(=O)N(C)C)cc3)cc2n1-c1cccc(C(F)(F)F)c1",
        "CC(=O)c1ccc(-c2cnc3c(NC=O)cc(-c4ccc(C(=O)N5CCOCC5)cc4)cn23)cc1",
        "COc1ccc(NC(=O)C2Cc3c(nccc3-c3ccccc3Oc3ccccc3)O2)cn1",
        "O=C(NCc1ccc(F)c(F)c1)c1ccc(-c2ccc(F)c(F)c2)c2ccoc21",
        "O=C(Cc1ccccc1[N+](=O)[O-])N1CCN(Cc2ccc3c(c2)OCO3)CC1",
        "O=C(/C=C/c1ccc(O)cc1)O[C@@H]1[C@@H]2O[C@]2(CO)[C@@H]2[C@H]1C=CO[C@H]2O[C@@H]1O[C@H](CO)[C@@H](O)[C@H](O)[C@H]1O",
        "CC[C@H](C)/C=C(C)/C=C/C1=CC2=C(Cl)C(=O)[C@](C)(OC(C)=O)C(=O)C2=CN1CCO",
        "C=C1[C@H]2C[C@@H]3C[C@H](OC(C)=O)C(C)=C([C@@H](OC(C)=O)[C@H](OC(C)=O)[C@]2(C)[C@@H](OC(C)=O)C[C@@H]1OC(C)=O)C3(C)C",
        "CCC1=CC2CN3CCc4c5ccccc5[nH]c4[C@@](C(=O)OC)(C2)[C@@H]13",
        "Cc1cc(C(=O)CSc2nnnn2-c2ccc(O)cc2)c(C)n1Cc1ccc2c(c1)OCO2",
        "CN1CCN(c2ccc(Nc3nccc4[nH]c(-c5cccc(F)c5)cc43)cc2)CC1",
        "C=C[C@H]1[C@H](O[C@@H]2O[C@H](CO)[C@@H](O)[C@H](O)[C@H]2O)OC=C2C(=O)OCC[C@]21O",
        "C=C1C(=O)O[C@@H]2/C=C(/C)CC3C=C(C[C@H](OC(=O)/C(C)=C\\C)[C@@H]12)C(=O)O3",
        "O=C(Nc1ccc2c(c1)OCO2)c1csc(C(O)c2cccc(C(F)(F)F)c2)n1",
        "CSc1ccccc1NC(=O)CN(C)C(C)C(=O)Nc1ccccc1C(=O)c1ccccc1",
        "O=C(CSc1nnc(-c2cccnc2)n1-c1cccc(C(F)(F)F)c1)c1ccc2c(c1)OCO2",
        "CN(C)C(=O)CN(CN1C(=O)NC(c2ccccc2)(c2ccccc2)C1=O)CC(F)(F)F",
        "O=C(NC1CC1)C(Sc1nnc(-c2ccc3c(c2)OCO3)n1-c1ccccc1)c1ccccc1",
        "Cc1cc2nc(N(Cc3ccccc3)C(=O)c3nc4nccc(C)n4n3)sc2c(C)c1",
        "Cc1c(O)ccc2c(CSc3nnc(N4CCOCC4)n3-c3ccccc3)cc(=O)oc12",
        "O=C1/C(=C/c2ccc(O)cc2)N=C(c2ccccc2)N1n1c(=O)c2ccccc2nc1-c1ccccc1",
        "CC(C)(C)c1cc2c(OCC(=O)N[C@H]3CONC3=O)c(c1)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@H]1CONC1=O)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@H]1CONC1=O)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@H]1CONC1=O)C2",
        "CC(C)(C)NC(=S)N/N=C/C1c2ccccc2C(/C=N/NC(=S)NC(C)(C)C)c2ccccc21",
        "COc1cc2c(cc1OC)c(-c1ccc3c(c1)OCO3)c1c(c2OC2OCC(OC3OC(COC(C)=O)C(OC(C)=O)C(OC(C)=O)C3OC(C)=O)C(OC)C2OC)COC1=O",
        "CN(CCCNC(=O)c1ccc(N(CCCl)CCCl)cc1)CCCNC(=O)c1ccc(N(CCCl)CCCl)cc1",
        "COC(=O)[C@@H](C)NC(=O)COc1c2cc(C(C)(C)C)cc1Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@H](C)C(=O)OC)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@@H](C)C(=O)OC)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@H](C)C(=O)OC)C2",
        "C=CCn1c2scc(-c3cccc([N+](=O)[O-])c3)[n+]2c2ccccc2c1=O",
        "Fc1nc(-c2c3ccccc3c3sc4ccccc4n32)nc(N2CCOCCOCCOCCOCC2)n1",
        "O=C(OCCS(=O)(=O)c1ccccc1)N1c2ccc(OCCO)cc2C23OC24CCC[C@H]3C#C/C=C\\C#C[C@H]14",
        "N#C/C(c1nc2ccccc2[nH]1)=c1\\s/c(=C/c2ccccc2)c(=O)n1-c1ccccc1",
        "O=C1CN(c2cccc(Cl)c2)C(=O)CN1CCN1CC(=O)N(c2cccc(Cl)c2)CC1=O",
        "O=C1c2ccccc2/C(=N/c2ccc([N+](=O)[O-])cc2)C=C1Nc1ccc([N+](=O)[O-])cc1",
        "COC(=O)c1sccc1NS(=O)(=O)c1ccc(NC2=C(Cl)C(=O)c3ccccc3C2=O)cc1",
        "CCCCCCCCCCCC(=O)OC[C@@H](C[As](=O)(O)O)OC(=O)CCCCCCCCCCC",
        "COc1ccc(CCN(C)CCCC(C#N)(c2ccc(OC)c(OC)c2)C(C)C)cc1OC",
        "CCN(CC)CCC/N=c1\\c2cc(OC)ccc2[nH]c2c([N+](=O)[O-])ccc(NCCCN(CC)CC)c21",
        "O=[N+]([O-])c1ccc(N2C[N+](c3ccccc3)=NC(c3ccccc3)=N2)cc1",
        "O=C1NC(=S)S/C1=C\\c1ccccc1/C=N/c1ccc([N+](=O)[O-])cc1",
        "COC(=O)c1cc2c(ccc3ccc(OCCOc4ccccc4[N+](=O)[O-])c([N+](=O)[O-])c32)oc1=O",
        "Nc1nc(=O)c2c([nH]1)NCC(CCc1ccc(C(=O)NC(CCC(=O)O)C(=O)O)cc1)C2",
        "CCCCCCCCCCCCC/C=C(/COCC[N+](C)(C)C)OC(C)=O.CCCCCCCCCCCCCC/C(=C/OCC[N+](C)(C)C)OC(C)=O",
        "CCOC(=O)C1=C(C)NC(=O)NC1c1cccc(Oc2cccc(C(F)(F)F)c2)c1",
        "CC1=NN(c2ccccc2)C(=O)C1C(c1ccccc1)(c1ccccc1)c1ccccc1",
        "C=C(C)C1Cc2c(cc(O)c3c2oc2c(c3=O)-c3cc(OC)c(OC)cc3OC2O)O1",
        "O=C(N[C@H]1CCc2cc(O)c(O)c(O)c2-c2ccc(O)c(=O)cc21)c1cc(O)c(O)c(O)c1",
        "O=c1[nH]/c(=C/c2ccc3c(c2)OCO3)c(=O)[nH]/c1=C/c1ccc2c(c1)OCO2",
        "C/C(=N\\NC(=N)N)c1ccc(N2CCN(c3ccc(/C(C)=N/NC(=N)N)cc3)CC2)cc1",
        "O=[N+]([O-])c1ccc2nc(-c3ccc(Cl)cc3)n(OCc3cccc(Cl)c3)c2c1",
        "CC(=O)N(C(=O)CC(=O)N1N=C(c2ccccc2)C(N=Nc2ccccc2[N+](=O)[O-])C1=O)c1ccc(Cl)cc1",
        "NCCCCC(N)C(=O)NC(CC(=O)O)C(=O)NC(CC(=O)O)C(=O)NC(CCCCN)C(=O)N1CCCC1C(=O)O",
        "CC(C)OC(=O)/C(=C/c1cc(C(C)(C)C)c(O)c(C(C)(C)C)c1)P(=O)(OC(C)C)OC(C)C",
        "Cc1cn(C2CC(N=[N+]=[N-])C(COC(=O)CCCCCCCCCCOc3ccccc3)O2)c(=O)[nH]c1=O",
        "CCC(C)[C@@H](NC1=C(Cl)C(=O)c2c(O)ccc(O)c2C1=O)C(=O)OC",
        "Clc1ccc(-c2nnc3c4ccccc4nc(N4CCN(c5ccccc5)CC4)n23)cc1",
        "CCOC(=O)/C(Cc1ccc[nH]1)=N\\Nc1ccc([N+](=O)[O-])cc1[N+](=O)[O-]",
        "O=C1CCC(=O)N1Cc1ccccc1N=[N+]([O-])c1ccccc1CN1C(=O)CCC1=O",
        "O=C(N[C@H]1CCCCC1OC(=O)c1cc(OCc2ccccc2)c(C(=O)c2ccccc2C(=O)OCc2ccccc2)c(OCc2ccccc2)c1)c1ccc(OCc2ccccc2)cc1",
        "COc1ccc(/C=C2/C(=O)N(/C=C3\\C(=O)Oc4ccccc4C3=O)/C(=N/C(C)=O)N2C)cc1OC",
        "COc1ccc(/C=C2/S/C(=N\\c3ccccc3)N(NC(=O)Cc3ccccc3)C2=O)c(OC)c1",
        "CCOC(=O)c1cc(C(=O)OCC)c(N2CCN(c3ccc(OC)cc3)CC2)[nH]c1=O",
        "CCCCCCCCCCCCCCCCOCC(COP(=O)([O-])OCC[n+]1ccc(N(C)C)cc1)OCC",
        "CSCCOC(=O)COc1c2cccc1Cc1cccc(c1OCC(=O)OCCSC)Cc1cccc(c1OCC(=O)OCCSC)Cc1cccc(c1OCC(=O)OCCSC)C2",
        "O=C(NCCCC1N[Cu]2(NC(CCCNC(=O)c3ccc(Cl)cc3)C(=O)O2)OC1=O)c1ccc(Cl)cc1",
        "CC(C)[C@H](CO)NC(=O)COc1c2cc(C(C)(C)C)cc1Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@@H](CO)C(C)C)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@@H](CO)C(C)C)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N[C@@H](CO)C(C)C)C2",
        "Cc1ccc(S(=O)(=O)N/N=C/C=N/NS(=O)(=O)c2ccc(C)cc2)cc1",
        "COc1ccc2c(c1)CC[C@@H]1C2CC[C@]2(C)C(=O)N(C)C(=O)CC12",
        "COc1c(O)cc2c(O)c1CC(C)CC(OC)[C@@H](O)[C@H](C)/C=C(\\C)C(OC(N)=O)C(OC)/C=C\\C=C(/C)C(=O)N2",
        "COc1cc(CO)c(-c2ccc3c(c2O)C(C)N[C@H](C)C3)c2cccc(O)c12",
        "Cc1cc(C)nc(NS(=O)(=O)c2cc(NC3=C(Cl)C(=O)c4ccccc4C3=O)ccc2C)c1",
        "CCCCCCCCC[C@@H]1C[C@H](O)[C@H](Cc2ccccc2)N1C(=O)OC(C)(C)C",
        "CCCCCCC(=O)OC[n+]1cccc(-c2c(COC(=O)NC(C)C)c(COC(=O)NC(C)C)c3n2CCC3)c1",
        "CC(C)N/C(COc1ccc(Oc2cnc3cc(Cl)ccc3n2)cc1)=N/S(=O)(=O)c1ccccc1",
        "CC(=O)Nc1ccc(C(=O)NN2C(=O)CSC2c2ccc([N+](=O)[O-])cc2)cc1",
        "CC(=O)OC1COC(N2c3cc([N+](=O)[O-])c(Br)cc3/C(=N/NC(=N)N)C2=O)C(OC(C)=O)C1OC(C)=O",
        "O=C1Nc2ccccc2/C1=N/Nc1nc(-c2ccc([N+](=O)[O-])cc2)cs1",
        "CC(C)N/C(=N/S(=O)(=O)c1ccccc1)C(C)Oc1ccc(Oc2cnc3cc(Cl)ccc3n2)cc1",
        "COc1cccc2c(-c3ccc4c(c3O)C(C)N[C@H](C)C4)c(C)cc(OC)c12",
        "COc1ccc(-c2ccccc2)cc1N(NC(=O)C(F)(F)C(F)(F)F)C(=O)C(F)(F)C(F)(F)F",
        "COc1ccc(N=Nc2ccc3oc(=O)c(C(=O)Nc4ccc(Cl)cc4)cc3c2)cc1",
        "CCOP(=O)(O)c1cccc2c(NC(=O)c3ccc(C)c(NC(=O)c4cccc(NC(=O)Nc5cccc(C(=O)Nc6cc(C(=O)Nc7cccc8c7cccc8P(=O)(O)OCC)ccc6C)c5)c4)c3)cccc21",
        "COc1cc2c(cc1OC)[C@@](O)(c1ccc3c(c1)OCO3)[C@H]1C(=O)OC[C@H]1C21SCCCS1",
        "CCC(C)c1cc(N=Nc2ccc(S(=O)(=O)Nc3cnc4ccccc4n3)cc2)cc(C(C)CC)c1O",
        "Cc1cc(C)cc(Sc2c(C(C)C)c(=O)[nH]c(=O)n2OCc2ccccc2)c1",
        "CC(C)NC(=O)OCc1c(COC(=O)NC(C)C)c(-c2cc[n+](COC(=O)C3CCCCC3)cc2)n2c1CCC2",
        "O=[N+]([O-])c1ccc2c(c1)/C(=N/N=C1c3ccccc3Cc3ccccc31)c1cc([N+](=O)[O-])cc([N+](=O)[O-])c1-2",
        "C=C1C(OC(C)=O)[C@]23C[C@H]1CC[C@H]2C1(C)CCCC(C)(C(=O)OC)[C@H]1CC3",
        "CCC12C=CCN3CCC4(c5cc([C@@]6(C(=O)OC)C[C@H]7CN(C[C@](O)(CC)C7)CCc7c8ccccc8[nH]c76)c(OC)c(N)c5N(C)[C@H]4[C@@](O)(C(=O)OC)[C@@H]1O)C32",
        "CCOC(=O)C(=O)/C(=C/n1c2cc([N+](=O)[O-])ccc2[nH]c1=S)C(=O)OCC",
        "CCN(CC)CCCNc1ccc([N+](=O)[O-])c2[nH]c3ccccc3c(=N)c12",
        "O=S(=O)(Oc1cccc(Oc2ncc(C(F)(F)F)cc2Cl)c1)c1cccc(C(F)(F)F)c1",
        "O=C1/C(=C\\c2ccc([N+](=O)[O-])cc2)Sc2scc(-c3ccc(O)c(O)c3)[n+]21",
        "CC(C)(C)c1cc2c(OCC(=O)N(C3CCCCC3)C3CCCCC3)c(c1)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N(C1CCCCC1)C1CCCCC1)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N(C1CCCCC1)C1CCCCC1)Cc1cc(C(C)(C)C)cc(c1OCC(=O)N(C1CCCCC1)C1CCCCC1)C2",
        "O=C1C(C2=C3c4ccccc4N=C3/C(=C\\c3ccc(O)cc3)C2=O)=C2c3ccccc3N=C2/C1=C\\c1ccc(O)cc1",
        "COc1ccc2[nH]c3c([N+](=O)[O-])ccc(NCCCN(C)C)c3/c(=N/CCCN(C)C)c2c1",
        "COC(=O)CNC(=O)C(CSC(=O)OCC1c2ccccc2-c2ccccc21)NC(=O)CCC(NC(=O)OCC1c2ccccc2-c2ccccc21)C(=O)OC",
        "CCC(C)C(NC(=O)C1CCCN1C(=O)C(N)C(C)C)C(=O)NC(CCC(=O)O)C(=O)N1CCCC1C(=O)NC(Cc1c[nH]cn1)C(=O)NC(C)C(=O)NC(CC(C)C)C(=O)O",
        "C=CCOc1ccc(C(=O)Nc2ccc(/C=C/c3ccc(NC(=O)c4ccc(OCC=C)cc4OCC=C)cc3S(=O)(=O)O)c(S(=O)(=O)O)c2)c(OCC=C)c1",
        "CCOC(=O)C(=CN1C/C(=C/C(C)=C/c2ccccc2)SC1=S)C(=O)OCC",
        "COC(=O)[C@@]1(C)CCC[C@]2(C)C3(O)C(C(OC(C)=O)C21O)[C@@H](c1ccoc1)OC(=O)C3C",
        "CCCCCCCCCCCCCCCCS(=O)(=O)c1ccc(S(=O)(=O)N/N=c2\\[nH]c3cc(S(=O)(=O)O)ccc3s2)cc1C(=O)O",
        "N#CC1=C2Sc3ccccc3N2C(N)=C(c2nc3ccccc3s2)C1c1ccc(Cl)cc1",
        "O=C(CC(=O)Nc1ccccc1)NNC(Sc1ccccc1)c1cc(N=Nc2ccc(Br)cc2)ccc1O",
        "Nc1nc(N)c(N=Nc2ccccc2S(=O)(=O)O)c(Nc2ccc(S(=O)(=O)O)cc2)n1",
        "CC(=O)O[C@@H]1C2=C(C)[C@@H](OC(=O)C(O)C(NC(=O)c3ccccc3)c3ccccc3)C[C@@](O)(C(OC(=O)c3cccc(N=[N+]=[N-])c3)C3C4(OC(C)=O)COC4C[C@H](O)[C@@]3(C)C1=O)C2(C)C",
        "O=C(Nc1ccccc1)C(=O)C(C(=O)c1ccccc1)C(c1ccccc1)c1ccccc1",
        "CCOc1ccc(Nc2nc(C)c(C(=O)CC(=O)C(=O)Nc3ccc(C(C)C)cc3C(C)C)s2)cc1",
        "O=C(CC(=O)Nc1ccccc1)N/N=C/c1cc(N=Nc2ccc(Br)cc2)ccc1O",
        "COc1ccc(NC(=O)CC(=O)NNC(Sc2ccccc2)c2cc(N=Nc3ccc(Br)cc3)ccc2O)cc1",
        "C/C=C(\\C)[C@H]1O[C@@]2(C[C@@H]3C[C@@H](C/C=C(\\C)[C@H](OC(=O)C(C)C)C(C)/C=C/C=C4\\COC5[C@H](O)C(C)=CC(C(=O)O3)C45O)O2)C(O)C(OC(=O)C(C)C)C1C",
        "CN(C)S(=O)(=O)Oc1cccc(C(=O)Nc2cc(C(F)(F)F)cc(C(F)(F)F)c2)c1",
        "CC(=O)[C@@H]1Cc2c(O)c3c(c(O)c2C(OC2CC(N)C(O)C(C)O2)O1)C(=O)c1ccccc1C3=O",
        "CC1=NN(c2nc(N)nc(C(C)Sc3cc(Cl)c(C)cc3S(N)(=O)=O)n2)C(C)(C)C1",
        "O=C(O/N=C1\\C(=O)N(Cc2ccc(Cl)cc2Cl)c2ccccc21)c1cccc(C(F)(F)F)c1",
        "CO[C@@H]1[C@H](O)c2coc3c2[C@@](C)(c2ccc4c(c2C3=O)CCC4=O)[C@@H]1O",
        "CCC12C=CCN3CCC4(c5cc([C@@]6(C(=O)OC)C[C@H]7CN(C[C@](O)(CC)C7)CCc7c8ccccc8[nH]c76)c(OC)c(NC=O)c5N(C)[C@H]4[C@@](O)(C(=O)OC)[C@@H]1OC(C)=O)C32",
        "CC(=O)O[C@@]12CO[C@@H]1C[C@H](OC1OCC(O)OC1O)[C@@]1(C)C(=O)[C@H](O)C3=C(C)[C@@H](OC(=O)C(O)[C@@H](NC(=O)c4ccccc4)c4ccccc4)C[C@@](O)(C(OC(=O)c4ccccc4)C21)C3(C)C",
        "Cn1cc(NC(=O)c2cc(NC(=O)c3cc(C(=O)Nc4ccc(N(CCCl)CCCl)cc4)cn3C)cn2C)cc1C(=O)NCCC(=N)N",
        "N#C/C(=C\\NC(=S)N/N=C1\\C(=O)Nc2ccccc21)c1nc2ccccc2s1",
        "CCCCCCCCCCCCCCOc1ccccc1NC(=O)c1cc(-n2nc3ccccc3n2)c2ccccc2c1O",
        "COc1cc(C2c3cc4c(cc3[C@H](OC3OC(C)C(O)C(O)C3O)[C@@H]3COC(=O)[C@H]32)OCO4)cc(OC)c1O",
        "CC1=CC(=O)/C(=C/N2C(=S)NC(=O)/C2=C\\c2cccc([N+](=O)[O-])c2)C(=O)O1",
        "COc1ccc(C(=O)/C=C/c2ccc(OCCSCCCCCCCCCCSCCOc3ccc(/C=C/C(=O)c4ccc(OC)cc4)cc3)cc2)cc1",
        "O=C(C/N=C(/CN(c1ccccc1)c1ccccc1)Oc1c(Cl)cc(Cl)cc1Cl)n1cnc2ccccc21",
        "CC(C)(O)C(OC(=O)N1C(C)(C)COC1(C)C)[C@@H]1CCCN1Cc1ccccc1",
        "C=C1C(=O)OC2/C=C(\\C)CC/C=C(\\C)CC(OC(=O)/C(=C/CO)CO)[C@@H]12",
        "CSCCCNC(=O)c1csc(-c2c[s+]c(CCNC(=O)CNC(=O)CNC(=O)CNC(=O)OC(C)(C)C)[nH]2)n1",
        "COc1cc(Cn2c3ccccc3c3cc(C)cc(OC)c32)cc2c3ccccc3[nH]c21",
        "Cc1cc(NC(=O)NC(=O)c2ccccc2NC(=O)CN(C)C)ccc1Oc1ncc(Br)cn1",
        "NS(=O)(=O)c1ccc(NC(=O)c2ccccc2SSc2ccccc2C(=O)Nc2ccc(S(N)(=O)=O)cc2)cc1",
        "COc1ccc(/C=C(\\NC(=O)c2ccccc2)C(=O)NN(C)c2ccnc3cc(Cl)ccc32)cc1OC",
        "COc1ccc(-c2ccccc2)cc1N(NC(=O)OC(C)(C)C)C(=O)C(F)(F)F",
        "CC(=C\\c1ccccc1)/C=C1/SC(=S)N(C=C2C(=O)NC(=O)NC2=O)C1=O",
        "C=CCNC(=S)NC=Cc1c(C(=O)O)cc([N+](=O)[O-])cc1[N+](=O)[O-]",
        "COc1cc(NC(=O)c2cccn(Cc3ccc(Cl)cc3Cl)c2=O)cc(OC)c1OC",
        "CN(C1CCCCC1)S(=O)(=O)c1cc(-n2c(=O)cc(C(F)(F)F)n(C)c2=O)c(F)cc1Cl",
        "O=C1CN(c2ccc(Cl)cc2)C(=O)CN1NCNN1CC(=O)N(c2ccc(Cl)cc2)CC1=O",
        "CCOC(=O)/C(C#N)=C/C(C(=O)OCC)=C(/N)N1CCN(c2ccccn2)CC1",
        "Cc1nc(Nc2c(C)cccc2C)sc1C(=O)CC(=O)C(=O)Nc1c(C)cccc1C",
        "CN(C)CCNC(=O)c1cc(NC(=O)c2cc(NC(=O)c3ccc(N(CCCl)CCCl)cc3)cn2C)cn1C",
        "Cc1c(/N=C\\c2ccc(Cl)cc2)/c(=N/NC(=O)c2ccccc2)n(-c2ccccc2)n1C",
        "CCOC(=O)C1=C(C)N(CC(=O)Nc2ccc(F)cc2)c2ncnn2C1c1ccc(F)cc1",
        "COc1ccc(-c2nnn3c2[nH]c(=O)c2ccc(C(=O)NCc4ccc(C(C)C)cc4)cc23)cc1",
        "CC(=O)N1CCc2cc(S(=O)(=O)N3CCc4cc5cccc(C)c5nc43)ccc21",
        "COc1ccc(CNC(=O)c2ccc3c(=O)[nH]c4c(-c5ccccc5F)nnn4c3c2)cc1OC",
        "CCn1c(CN(c2ccc(F)cc2)S(C)(=O)=O)nnc1SCC(=O)Nc1ccc(C)c(F)c1",
        "CC(=O)Nc1ccc(NC(=O)C2=CN3CCN(Cc4cscn4)C(=O)N=C3O2)cc1",
        "Cn1c2cc(NS(=O)(=O)c3cccc4nsnc43)c(Sc3ccccc3)cc2n(C)c1=O",
        "Cn1c2ccc(S(=O)(=O)N3CCCC3c3ccccc3Cl)cc2n(C)c(=O)c1=O",
        "COc1ccc(NC(=O)c2nnn(-c3ccc4noc(-c5ccc(Cl)cc5)c4c3)c2C)c(OC)c1",
        "CCOC(=O)C1=C(C)N(CC(=O)Nc2ccc(C)c(C)c2)c2ncnn2C1c1ccccc1",
        "CCOC(=O)C1=C(C)N(CC(=O)Nc2ccc(F)cc2F)c2ncnn2C1c1ccc(F)cc1",
        "CS(=O)(=O)N(Cc1nnc(SCC(=O)c2ccccc2)n1-c1ccccc1)c1ccc(F)cc1",
        "CC1(c2ccc3c(c2)OCO3)NC(=O)N(CC(=O)N2CCCc3ccccc32)C1=O",
        "Cc1nc2nc(C(F)(F)F)nn2c(C)c1CCC(=O)N1CCn2c3ccccc3nc21",
        "CC(Sc1nnc(N2CCOCC2)n1Cc1ccccc1)C(=O)Nc1ccc2c(c1)OCO2",
        "COc1ccc(N2CCN(C(=O)c3ccc(Cn4c(=O)c5cccn5c5cccnc54)cc3)CC2)cc1",
        "O=C(Nc1ccc(F)c(Cl)c1)C1=COC2=NC(=O)N(Cc3ccccc3C(F)(F)F)CCN12",
        "Cc1cccc(-c2nnn3c2[nH]c(=O)c2ccc(C(=O)NCc4cccnc4)cc23)c1",
        "Cc1nnc2n1c1cc(C(=O)NCc3ccc(Cl)cc3)ccc1c(=O)n2-c1cccc(F)c1",
        "Cc1cccc(C)c1NC(=O)CSc1nnc(CN(c2ccc(F)cc2)S(C)(=O)=O)n1C",
        "Cc1ccc(C)c(NC(=O)CSc2nnc(CN(c3ccc(F)cc3)S(C)(=O)=O)n2C)c1",
        "O=C(NCc1ccccc1Cl)c1ccc2c(=O)[nH]c3c(-c4ccccc4F)nnn3c2c1",
        "COc1ccc(CCNC(=O)c2ccc3c(=O)[nH]c4c(-c5ccccc5)nnn4c3c2)cc1OC",
        "COc1cccc(CN2CCN3C(C(=O)NCc4ccc(N(C)C)cc4)=COC3=NC2=O)c1",
        "Cc1ccc(NC(=O)c2c(C)cc(C)nc2SCC(=O)Nc2nc3ccccc3s2)cc1",
        "CCCCCNC(=O)c1ccc2c(=O)[nH]c3c(-c4ccc(OC)cc4)nnn3c2c1",
        "Cc1cccc(C)c1NC(=O)CN(C)C(C)C(=O)NCC(=O)Nc1ccc(F)c(F)c1F",
        "Cc1nn(C)c(C)c1S(=O)(=O)N(CC(=O)Nc1cccc(Cl)c1C)c1ccc(C)cc1",
        "Cc1nn(C)c(C)c1S(=O)(=O)N(CC(=O)NC1CC1)c1cc(C)cc(C)c1",
        "Cc1ccc(N(Cc2ccc3c(c2)CN(C(=O)NCCc2ccccc2)CCO3)C(=O)C(C)C)cc1",
        "O=C(NCc1ccccc1F)c1ccc2c(c1)CN(S(=O)(=O)c1ccc(F)cc1F)CCO2",
        "Cc1cc(C)cc(S(=O)(=O)N2CCOc3ccc(C(=O)NCc4ccccc4F)cc3C2)c1",
        "Cc1nc2ncnn2c(C)c1CCC(=O)N(Cc1cccs1)c1ccc2c(c1)OCCO2",
        "O=C(CSc1nnc2n1c1ccccc1c(=O)n2Cc1ccc(F)cc1)Nc1cc(F)ccc1F",
        "O=c1ccc(N2CCCN(S(=O)(=O)c3ccccc3)CC2)nn1-c1ccc(F)cc1",
        "Cc1ccccc1C(=O)Nc1cc2c(cc1N1CCC(C)CC1)n(C)c(=O)c(=O)n2C",
        "CNC(=O)N1c2ccccc2NC2=C(C(=O)CC(C)(C)C2)C1c1ccc(OC)cc1",
        "CCn1c2ccccc2n(Cc2ccc(C(=O)NCCN3CCCCC3C)cc2)c(=O)c1=O",
        "O=S(=O)(c1ccc(F)c(Cl)c1)N1CCOc2ccc(COCc3ccccc3F)cc2C1",
        "CCc1ccc(NC(=O)CSc2nnc3n2c2ccccc2c(=O)n3CCCOC(C)C)cc1",
        "CC(C)CNC(=O)c1ccc2c(c1)n1c(SCC(N)=O)nnc1n(CC(C)C)c2=O",
        "Cc1ccc(-n2nc(N3CCCN(S(=O)(=O)c4ccc(Cl)cc4)CC3)ccc2=O)cc1C",
        "CCCn1c2nnc(SCC(=O)N3CCN(c4ccccc4F)CC3)n2c2ccc(F)cc2c1=O",
        "COc1ccc(C(=O)Nc2ccc(-c3nc(-c4ccc(C)cc4)co3)cc2)cc1OC",
        "Cc1ccc(NS(=O)(=O)c2ccc3c(c2)NC(=O)C(CC(=O)N2CCCC2)CS3)cc1C",
        "CC(C)NC(=O)CSc1nnc2n1c1ccccc1c(=O)n2CCC(=O)NC1CCCCC1",
        "CC(=O)NC(Cc1c[nH]c2ccccc12)C(=O)Nc1cccc(-c2nnc3n2CCCCC3)c1",
        "CCCn1c2nnc(SCC(=O)Nc3cccc(C)c3)n2c2cc(C(=O)NC(C)C)ccc2c1=O",
        "C=CCn1c(=O)c(=O)n(CC(=O)NCCCN2CCN(c3ccc(F)cc3)CC2)c2ccccc21",
        "COc1ccc(OCC(=O)N2CCOc3ccc(CN(C(=O)C4CC4)c4ccccc4)cc3C2)cc1",
        "COc1ccc(S(=O)(=O)N2CCc3c(c(COCc4ccccc4)nn3C)C2)cc1Cl",
        "COc1cccc(CNS(=O)(=O)c2ccc3c(c2)NC(=O)C(CC(=O)N2CCCC2)CS3)c1",
        "Cc1ccc(NC(=O)CSc2nnc3n2c2ccccc2c(=O)n3CCCOC(C)C)c(Cl)c1",
        "CCOC(=O)C1CCN(S(=O)(=O)c2cc(-c3ccc(=O)[nH]n3)cc(C)c2C)CC1",
        "Cc1ccc(CNC(=O)c2ccc(Cn3c4ccccc4n(C)c(=O)c3=O)cc2)cc1",
        "CCOc1ccccc1NC(=O)Cn1c2cn(CC)nc2c(=O)n(Cc2ccc(F)cc2)c1=O",
        "N#C/C(=C/c1ccc(-c2ccccc2C#N)o1)c1nnc(N2CCOCC2)n1-c1ccccc1",
        "CCN(CC(=O)Nc1ccccc1C(=O)c1ccccc1)CC(=O)Nc1ccccc1C(F)(F)F",
        "CC(C)NC(=O)c1ccc2c(c1)n1c(SCC(=O)NC(C)(C)C)nnc1n(Cc1ccccc1)c2=O",
        "CCc1cccc(NC(=O)Cn2c3cn(CC)nc3c(=O)n(Cc3ccc(F)cc3)c2=O)c1",
        "CC(=O)N(Cc1ccc(F)cc1)Cc1ccc2c(c1)CN(S(=O)(=O)c1cccc(C)c1)CCO2",
        "CCOC(=O)c1nn(-c2ccc(F)cc2)c(=O)cc1NCCC(c1ccccc1)c1ccccc1",
        "COc1cccc(S(=O)(=O)Nc2cc3c(cc2Oc2ccccc2)n(C)c(=O)c(=O)n3C)c1",
        "CCCc1cc(C2CCCN2C(=O)c2cnn3c2nc(-c2ccccc2)cc3C(F)F)no1",
        "Cc1cc(NC(=O)CN2c3ccccc3NC3=C(C(=O)CC(C)(C)C3)C2c2ccccc2)no1",
        "CCn1c(SCC(=O)c2c[nH]c3ccccc32)nnc1-c1ccc2c(c1)OCCCO2",
        "Cc1cc(C)cc(S(=O)(=O)N2CCOc3ccc(COCc4ccc(F)cc4)cc3C2)c1",
        "O=C(CN1C(=O)NC(c2ccccc2)(c2ccccc2)C1=O)NC(=O)NC1CCCC1",
        "COc1cccc(OC)c1C(=O)Nc1cc2c(cc1Oc1ccccc1)n(C)c(=O)c(=O)n2C",
        "CCn1cc2c(n1)c(=O)n(Cc1ccccc1)c(=O)n2CC(=O)N1CCc2ccccc2C1",
        "Cc1cc(C)c(NC(=O)Cn2c3cn(C)nc3c(=O)n(Cc3ccc(F)cc3)c2=O)c(C)c1",
        "CC(=O)N(Cc1ccc2c(c1)CN(S(=O)(=O)c1cc(F)ccc1C)CCO2)c1ccc(C)cc1",
        "CN(CC(=O)N(c1nc2ccccc2s1)c1ccccc1)S(=O)(=O)/C=C/c1ccccc1",
        "O=S(=O)(Nc1ccc(-c2nnc3n2CCCCC3)cc1)c1ccc2c(c1)OCCO2",
        "CS(=O)(=O)Nc1nc2ccc(NC(=O)c3cc(-c4ccc(F)cc4)nc4ccccc43)cc2s1",
        "CCCn1c2nnc(SCC(=O)Nc3ccc4c(c3)OCCO4)n2c2ccc(Cl)cc2c1=O",
        "Cc1ccc(NC(=O)CSc2nnc3n2c2cc(C(=O)NC4CCCCC4)ccc2c(=O)n3C)cc1",
        "CC(=O)N(Cc1ccc2c(c1)CN(S(=O)(=O)c1cccc(C)c1)CCO2)c1ccc(C)cc1",
        "CCOC(=O)c1ccc(NC(=O)Cn2c3cn(CC)nc3c(=O)n(Cc3ccccc3)c2=O)cc1",
        "COc1ccc(S(=O)(=O)N2CCc3c(c(COCc4ccc(C)cc4)nn3C)C2)cc1Cl",
        "CCS(=O)(=O)c1ccc(-c2cccc(NS(=O)(=O)c3ccc(OC)cc3)c2)nn1",
        "Cc1cccc(CSc2nnc3n2c2cc(C(=O)NC4CCCCC4)ccc2c(=O)n3C)c1",
        "Cc1cc(C)n(CCC(=O)N2CCN(c3ccc(=O)n(-c4ccc(C)cc4)n3)CC2)n1",
        "C=CCn1c(=O)c(=O)n(CCC(=O)NCCN2CCN(c3ccccc3F)CC2)c2ccccc21",
        "COC(=O)c1ccc(NC(=O)Cn2c3cn(C)nc3c(=O)n(Cc3ccc(F)cc3)c2=O)cc1",
        "CCOc1ccc(S(=O)(=O)N2CCOc3ccc(C(=O)NCc4ccccc4F)cc3C2)cc1",
        "Cc1ccccc1-n1c2nnc(SCn3nnc4ccccc4c3=O)n2c2ccccc2c1=O",
        "O=C(NCc1ccccc1F)c1ccc2c(c1)CN(S(=O)(=O)Cc1ccccc1Cl)CCO2",
        "O=C1Nc2cc(Cl)ccc2Oc2ccc(S(=O)(=O)N3CCc4ccccc4C3)cc21",
        "Cc1ccc2c(c1)NC(=O)c1cc(S(=O)(=O)N3CCc4ccccc4C3)ccc1O2",
        "Cc1cccc(CN2CC(=O)N(Cc3ccc(Cl)cc3)c3ccccc3S2(=O)=O)c1",
        "CCn1cc2c(n1)c(=O)n(Cc1ccccc1)c(=O)n2CC(=O)Nc1ccc(OC)cc1",
        "O=S(=O)(c1ccc2c(c1)OCCO2)N1CCOc2ccc(COCc3cccc(F)c3)cc2C1",
        "CC(NC(=O)CN1C(=O)NC(c2ccccc2)(c2ccccc2)C1=O)c1ccccc1",
        "CC(C)Cn1c2nnc(SCc3ccccc3Cl)n2c2cc(C(=O)NC(C)C)ccc2c1=O",
        "Cc1ccc(NC(=O)Cn2cc(S(=O)(=O)c3ccc(Cl)cc3)c(=O)c3ccc(C)nc32)c(C)c1",
        "O=C1Nc2ccccc2Oc2cc(Cl)c(S(=O)(=O)N3CCc4ccccc4C3)cc21",
        "O=C1CN(Cc2ccccc2Cl)S(=O)(=O)c2ccccc2N1Cc1ccc(Cl)cc1",
        "CC(C)Cn1c2nnc(SCC(=O)NC3CCCC3)n2c2cc(C(=O)NC(C)C)ccc2c1=O",
        "Cc1cc(C)n(-c2nc3c(c(=O)n2CC(=O)Nc2ccc(Br)cc2)CCCC3)n1",
        "COc1ccc(Oc2cc3c(cc2NC(=O)c2sccc2C)n(C)c(=O)c(=O)n3C)cc1",
        "O=C1CN(Cc2c(F)cccc2Cl)S(=O)(=O)c2ccccc2N1Cc1ccc(Cl)cc1",
        "CCc1nn2c(c1-c1ccc(C)c(S(=O)(=O)N3CCc4ccccc4C3)c1)NC(=O)CCC2=O",
        "O=C(CSc1nnc(N2CCOCC2)n1-c1ccccc1)N(Cc1ccco1)C1=CCCCC1",
        "COc1cccc(CN2CCN3C=C(C(=O)Nc4ccc(NC(C)=O)cc4)OC3=NC2=O)c1",
        "COc1ccc(CNC(=O)C2=COC3=NC(=O)N(Cc4ccccc4C)CCN23)c(OC)c1",
        "O=C(Nc1ccc(-n2cccc2)cc1)C1=COC2=NC(=O)N(Cc3cccc(Cl)c3)CCN12",
        "Cc1ccnc2nc(C(=O)Nc3ncc(Cc4ccc(OC(F)(F)F)cc4)s3)nn21",
        "O=C(c1cc(-c2cccnc2)on1)N1CCN(C(c2ccccc2)c2ccccc2)CC1",
        "COc1ccc(-c2cc(C(=O)Nc3ccccc3C)nn2C2CCS(=O)(=O)C2)cc1",
        "COC(=O)c1ccc2c(c1)N(CC(=O)Nc1ccc(C)c(F)c1)C(=O)c1ccccc1S2(=O)=O",
        "CCOC(=O)c1ccc2c(c1)N(CC(=O)NCc1ccccc1)C(=O)c1ccccc1S2",
        "CCOC(=O)C1=C(C)N(CC(=O)Nc2ccc(CC)cc2)c2ncnn2C1c1ccc(F)cc1",
        "COc1cccc(NC(=O)Cn2nc3c4cc(F)ccc4n(Cc4cccc(C)c4)cc-3c2=O)c1",
        "CC(C)CCNC(=O)C1(c2ccc(NS(=O)(=O)c3ccc(F)cc3)cc2)CC1",
        "COc1ccc(-c2cc(C(=O)NC(C)(C)CC(C)(C)C)nn2C2CCS(=O)(=O)C2)cc1",
        "N#Cc1ccc(/C=N/Nc2ccc(S(=O)(=O)Nc3cccc(Cl)c3)cc2[N+](=O)[O-])cc1",
        "O=[N+]([O-])c1ccc(/C=N/Nc2ccc(S(=O)(=O)Nc3ccccc3)cc2[N+](=O)[O-])o1",
        "COc1cccc(C(=O)Nc2c3c(nn2-c2ccc([N+](=O)[O-])cc2)CS(=O)(=O)C3)c1",
        "CC(=O)N1c2ccc(S(=O)(=O)N(C)CC(=O)NC3CCCCC3C)cc2CC1C",
        "COc1cc2c(cc1NC(=O)Cn1cnc3c1c(=O)n(C)c(=O)n3C)oc1ccccc12",
        "Cc1cccc(OCc2nnc(SCC(=O)c3c(N)n(C4CC4)c(=O)[nH]c3=O)o2)c1",
        "COc1ccc(OC)c(NC(=O)COC(=O)c2ccc(OC)c(S(=O)(=O)N3CCOCC3)c2)c1",
        "O=C(C1CCN(S(=O)(=O)c2c[nH]cn2)CC1)N1CCN(c2ccc([N+](=O)[O-])cc2)CC1",
        "CCc1c(C)c2ccc(OCC(=O)NCC3CCC(C(=O)O)CC3)c(C)c2oc1=O",
        "Cn1c(=O)c2c(SCC(=O)N3CCOCC3)nc(-c3ccccc3F)nc2n(C)c1=O",
        "CC1CN(C(=O)CSc2nnc(CNC(=O)c3ccc(S(=O)(=O)N(C)C)cc3)o2)CC(C)O1",
        "O=C(CN(C(=O)CNC(=O)c1ccco1)c1ccc2c(c1)OCCO2)NC1CCCC1",
        "CN(C)S(=O)(=O)c1ccc(S(=O)(=O)N2CCCC(C(=O)NC3CCCC3)C2)cc1",
        "COC(=O)C1C2c3ccccc3OC1(C)N=c1s/c(=C\\c3cnn(C)c3)c(=O)n12",
        "CCOc1ccc(NC(=O)CCNS(=O)(=O)c2ccc3c(c2)c(=O)n(C)c(=O)n3C)cc1",
        "COc1ccc(C2SCC(=O)N2c2c(C)n(C)n(-c3ccccc3)c2=O)cc1OC",
        "C=CCn1c(SCC(=O)c2c(N)n(C)c(=O)n(C)c2=O)nnc1-c1ccccc1F",
        "COc1ccc(OC)c(/C=C/C(=O)OC2(C(F)(F)F)C(=O)Nc3c2ccc2ccccc32)c1",
        "Cc1ccc(-n2c(-c3ccccc3)cc(/C=N/c3ccc(C)cc3C)c2-c2ccccc2)cc1",
        "O=S(=O)(c1cccs1)[C@H]1CS(=O)(=O)C[C@@H]1N1CCN(Cc2ccc3c(c2)OCO3)CC1",
        "COC(=O)c1ccc(COc2ccc(/C=C(/CC(=O)O)c3nc4ccccc4s3)cc2)cc1",
        "NC(=O)c1ccc(COC(=O)CCC(=O)c2ccc(Cl)cc2)c([N+](=O)[O-])c1",
        "Cc1ccc2[nH]c(/C(C#N)=C\\c3ccc(-c4cc(C(=O)O)cc(C(=O)O)c4)o3)nc2c1",
        "Cc1ccc(S(=O)(=O)Oc2ccc(C3C4=C(CC(C)(C)CC4=O)OC4=C3C(=O)CC(C)(C)C4)cc2)cc1",
        "CCCCn1c(N)c(N(CC)C(=O)Cc2ccc(S(=O)(=O)N3CCOCC3)s2)c(=O)[nH]c1=O",
        "COc1ccc(C)cc1NC(=O)CCNS(=O)(=O)c1ccc2c(c1)CC(C)N2C(C)=O",
        "CN(CC(=O)Nc1ccc(F)cc1)C(=O)COC(=O)CN1C(=O)NC2(CCCC2)C1=O",
        "COc1ccc(C(=O)NNC(=O)CSc2nc3c(c(=O)n(C)c(=O)n3C)n2C)cc1",
        "NS(=O)(=O)c1ccc(CCNC(=O)COC(=O)CCN2C(=O)C3CC=CCC3C2=O)cc1",
        "CN(C)S(=O)(=O)c1ccc(NC(=O)COC(=O)Cn2[nH]c(=O)c3ccccc3c2=O)cc1",
        "CN(C)S(=O)(=O)c1cccc(NC(=O)CN2C(=O)C3C4C=CC(C4)C3C2=O)c1",
        "COc1ccc(/C=C2\\C(=N)N3N=C(CC(=O)N4CCCCC4)SC3=NC2=O)cc1",
        "CC(C)[C@H](NC(=O)[C@@H]1CSC2c3ccccc3C(=O)N21)C(=O)NC1CCCC1",
        "CC(=O)NC1C(OCc2ccccc2)OC(COC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "CCOC(=O)c1[nH]c(C)c(C(=O)OCC(=O)N(C)C2CCS(=O)(=O)C2)c1C",
        "Cc1cc(C(=O)CN2C(=O)C(=O)N(C)C2=O)c(C)n1-c1cccc(S(=O)(=O)N2CCOCC2)c1",
        "Cc1ncc(CO)c(/C=N/NC(=O)c2cccc(S(=O)(=O)N3CCCC3)c2)c1O",
        "CN(C)CCN/C=C1/c2ccccc2C(=O)N(c2ccc(S(=O)(=O)N3CCOCC3)cc2)C1=O",
        "Cc1cc(C)n2nc(SCC(=O)N(CC(C)C)c3c(N)n(CC(C)C)c(=O)[nH]c3=O)nc2n1",
        "COC(=O)c1ccc(NC(=O)CCc2nnc3ccc(N4CCC5(CC4)OCCO5)nn32)cc1",
        "CC(=O)NC(Cc1c[nH]c2ccccc12)C(=O)NNC(=O)CSc1nc2ccccc2s1",
        "CCCCC(=O)Oc1ccc(C2C3=C(CC(C)(C)CC3=O)OC3=C2C(=O)CC(C)(C)C3)cc1OC",
        "N#Cc1ccc(-c2ccc(OC(=O)CSC(c3ccccc3)c3ccccc3)cc2)cc1",
        "O=C(O)CCNS(=O)(=O)c1ccc(C(=O)/C=C/c2ccc(C(=O)O)cc2)cc1",
        "CC(=O)Nc1ccc(NC(=O)C2CCN(c3nc4ccccc4nc3OC(C)C)CC2)cc1",
        "Cc1ccccc1S(=O)(=O)Cc1ccc(C(=O)NCCN(CC(C)C)CC(C)C)o1",
        "COc1ccc(C(=O)Oc2ccc(/C=C(\\C#N)c3nc4cc(C)c(C)cc4[nH]3)cc2)cc1",
        "CCOC(=O)C1=C(COC(=O)c2ccc(OC)c(S(=O)(=O)N3CCOCC3)c2)NC(=O)NC1C",
        "Cc1c(NC(=O)Cn2nc(C(=O)O)c3ccccc3c2=O)c(=O)n(-c2ccccc2)n1C",
        "Cc1c(OC(C)C(=O)NC[C@H]2CC[C@H](C(=O)O)CC2)ccc2c(-c3ccccc3)cc(=O)oc12",
        "O=C(NCCCN1CCOCC1)c1cc2c3ccccc3nc-2c(-c2cccc(Br)c2)[nH]1",
        "COc1ccc(-c2coc3cc(OC4OC(CO)C(O)C(O)C4O)ccc3c2=O)cc1",
        "Cc1csc(NC(=O)CCNS(=O)(=O)c2ccc3c(c2)c(=O)n(C)c(=O)n3C)n1",
        "CCN(CC)S(=O)(=O)c1ccc(-c2nnc(SCC(=O)Nc3nnc(C)s3)n2CC)cc1",
        "Cc1ccc(NC(=O)COC(=O)Cn2cnc3c2c(=O)n(C)c(=O)n3C)cc1Cl",
        "Cn1c(CCC(=O)OCC(=O)NCc2ccc3c(c2)OCO3)nc(=O)c2ccccc21",
        "O=C(/C=C/c1ccc(Cl)cc1)NC(=S)N1C[C@H]2C[C@H](C1)c1cccc(=O)n1C2",
        "COc1ccccc1N1CCN(C(=O)c2ccc3c(c2)[nH]c(=S)n(Cc2ccco2)c3=O)CC1",
        "NC(=O)c1ccc(NC(=O)CCC(=O)N2CCN(S(=O)(=O)c3ccc(Cl)cc3)CC2)cc1",
        "CC(C)Oc1ccc(S(=O)(=O)N2CCOCC2)cc1NC(=O)CN1C(=O)c2ccccc2C1=O",
        "CC1Sc2ccc(S(=O)(=O)CCC(=O)N3CCN(c4ccccc4)CC3)cc2NC1=O",
        "CCN(CCCNC(=O)C1CCCN(S(=O)(=O)c2c[nH]cn2)C1)c1ccccc1",
        "Cc1nn(C)c(C)c1S(=O)(=O)N(CC(=O)NCCN1CCOCC1)c1ccc(C(C)C)cc1",
        "COc1ccc(CCNC(=O)[C@H](C)N2C(=O)N3CCc4c5ccccc5[nH]c4[C@@]3(C)C2=O)cc1OC",
        "CN(C)S(=O)(=O)c1ccc(C(=O)Nc2cc3c(cc2C(=O)O)OCCCO3)cc1",
        "CCCCn1c(N)c(N(C(=O)CSc2nncn2-c2ccccc2)C2CCCC2)c(=O)[nH]c1=O",
        "CC(C)(C)C(=O)/C=c1/[nH]c(=O)/c(=C/c2cccc(C(F)(F)F)c2)s1",
        "O=C(COC(=O)c1ccccc1NC(=O)c1ccc([N+](=O)[O-])cc1Cl)N1CCc2ccccc21",
        "O=C(c1cn(CCCCn2cc(C(=O)C(F)(F)F)c3ccccc32)c2ccccc12)C(F)(F)F",
        "COC(=O)[C@@]1(C)CC[C@]2(C)CC[C@]3(C)C(=CC(=O)C4[C@@]3(C)CCC3C(C)(C)C(=O)CC[C@@]34C)[C@@H]2C1",
        "CC(=O)NC12CC3CC(C1)CC(C(=O)Oc1ccc(-c4ccc(C#N)cc4)cc1)(C3)C2",
        "CCN(CC)c1sc(S(=O)(=O)c2ccc(C)cc2)nc1S(=O)(=O)c1ccc(C)cc1",
        "Cc1ccc(NC(=O)C2=C(C)N=C(S)NC2c2ccc(C(C)C)cc2)c(C)c1",
        "COC(=O)c1ccccc1NC(=O)CN(c1ccnn1C(C)C)S(=O)(=O)c1ccccc1",
        "O=C(NC1C2CC3CC(C2)CC1C3)c1cc(-c2ccncc2)nc2ccc(Br)cc21",
        "CCOC(=O)Cn1c2ccc(S(=O)(=O)N(C)c3ccc(OC)cc3)cc2oc1=O",
        "COc1ccc(OC)c(CNC(=O)CCS(=O)(=O)c2ccc3c(c2)oc(=O)n3C)c1",
        "CC(=O)C(C)n1c(CN2CCN(C(=O)c3ccco3)CC2)nc2c1c(=O)n(C)c(=O)n2C",
        "Nc1nnc(SCC(=O)Nc2cc(S(=O)(=O)N3CCOCC3)ccc2N2CCCC2)s1",
        "Cc1c(C)c2c(OCC(=O)Nc3ccc(CCO)cc3)cc3c(c2oc1=O)CCC(C)(C)O3",
        "O=C(CCC(=O)c1ccc(F)cc1)OCC(=O)c1ccc([N+](=O)[O-])cc1",
        "CSc1ccc(C(C2=C(O)CC(C)(C)CC2=O)C2C(=O)CC(C)(C)CC2=O)cc1",
        "COc1cc(C(=O)OCCCC(=O)c2ccc(Br)cc2)ccc1OCc1ccc(Cl)cc1",
        "Cc1ccccc1-n1c(SCCOc2ccccc2Cl)nnc1-c1ccc(C(C)(C)C)cc1",
        "O=C(O)C1C(c2ccccc2)C(C(=O)Oc2cccc3ccccc32)C1c1ccccc1",
        "COc1cc(/C=C(/C#N)C(=O)c2ccccc2)ccc1OC(=O)c1ccc(F)cc1",
        "COC(=O)C(C(=O)OC)C(c1ccc(Cl)cc1)C(C)C(=O)c1ccc(C)cc1",
        "Cc1ccc2nc(N3CCN(C(=O)CCCC(=O)O)CC3)nc(-c3ccccc3)c2c1",
        "COc1cc(/C=C(\\C#N)c2nc3cc(C)ccc3[nH]2)ccc1OCc1ccc(C)cc1",
        "Cc1c(OCC(=O)N2CCC(C(N)=O)CC2)ccc2c3c(c(=O)oc12)CCCC3",
        "O=C(NCCCN1CCCC1=O)C1CCN(S(=O)(=O)c2c(Cl)cccc2Cl)CC1",
        "CCC(C)NC(=O)C(Cc1ccccc1)NS(=O)(=O)c1ccc2c(c1)oc(=O)n2C",
        "O=C(COC(=O)c1cc([N+](=O)[O-])ccc1N1CCOCC1)Nc1ccc(Cl)cn1",
        "Sc1nnc(-c2[nH]nc3c2CCC3)n1/N=C/c1c2ccccc2cc2ccccc21",
        "COc1cc(NC(=O)CNc2cccc(S(=O)(=O)N3CCOCC3)c2)cc(OC)c1",
        "COc1ccc(NS(=O)(=O)c2ccc(C)c(NC(=O)CNC(=O)c3ccco3)c2)cc1",
        "Cc1cc(C(=O)CSc2nnc(COc3ccc(Cl)cc3)n2C)c(C)n1Cc1cccs1",
        "COc1ccc(C(=O)C(C)OC(=O)C23CC4CC(CC(Cl)(C4)C2)C3)c(OC)c1",
        "O=C(CSc1nc2ccc(NC(=O)c3cccc(F)c3)cc2s1)N1CCCc2ccccc21",
        "COC(=O)c1ccc(COc2ccccc2/C=C/C(=O)c2ccc(Cl)cc2Cl)cc1",
        "O=C(OCc1coc(-c2ccccc2)n1)C1=NN(C2CCS(=O)(=O)C2)C(=O)CC1",
        "COc1ccc(C2c3c(C)n[nH]c3OC(N)=C2C#N)cc1COc1cc(C)c(Cl)c(C)c1",
        "CCOc1cc(C2C3=C(CC(C)(C)CC3=O)OC3=C2C(=O)CC(C)(C)C3)ccc1OC(=O)CC",
        "O=C(C1=C(c2ccccc2)SCCO1)N(Cc1ccccc1)C1CCS(=O)(=O)C1",
        "CCOC(=O)c1cc2n(n1)CC(C)(C(=O)NCCC(C)C)N(c1ccc(OC)cc1)C2=O",
        "COc1ccc(-c2nc(CS(=O)CC(=O)NCc3ccc(C)cc3)c(C)o2)cc1OC",
        "COc1ccccc1N1CCN(C(=O)C2CCN(S(=O)(=O)N3CCOCC3)CC2)CC1",
        "CC1(C)Cc2nc3oc4c(ncn(CC(=O)Nc5ccc(Cl)cc5)c4=O)c3cc2CO1",
        "O=C(Nc1ccc(Br)cc1F)[C@@H]1CCC(=O)N1C1OC(=O)c2ccccc21",
        "Cn1c(SCC(=O)N2CCCCC2)nnc1-c1ccc(S(=O)(=O)N2CCOCC2)cc1",
        "CC(C)(C)c1ccc(S(=O)(=O)NCC(=O)N2CCN(C(=O)c3ccco3)CC2)cc1",
        "CC(NC(=O)C1CCN(S(=O)(=O)N2CCC3(CC2)OCCO3)CC1)c1ccccc1",
        "CCN(CC)S(=O)(=O)c1ccc(N/N=C/c2ccc(C)s2)c([N+](=O)[O-])c1",
        "CCN(CC)S(=O)(=O)c1ccc(C)c(N/C(S)=N/c2ccc(F)c(F)c2F)c1",
        "Cc1ccc(C(=O)/C(=C\\c2ccc(Cl)c(Cl)c2)c2nc3ccccc3o2)cc1",
        "CCCOc1ccc(C(CC(=O)c2ccc(C)cc2)S(=O)(=O)c2ccc(C)cc2)cc1",
        "COc1ccc(OC)c(/C=C2\\Oc3cc(OC(=O)C(C)(C)C)ccc3C2=O)c1",
        "Cc1ccc(/C(=N/O)c2oc3ccc4c(C)cc(=O)oc4c3c2-c2ccccc2)cc1",
        "O=[N+]([O-])c1c(-n2nnc3ccccc32)nn(-c2ccccc2)c1C(Cl)Cl",
        "CNS(=O)(=O)c1cn(CC(=O)NCc2ccc(OC)cc2)cc1S(=O)(=O)NC",
        "CCN(C(=O)COC(=O)c1c(C)c(C)sc1NC(C)=O)C1CCS(=O)(=O)C1",
        "CC(C)(C)NC(=O)NC(=O)COC(=O)c1ccc2c(c1)S(=O)(=O)N=C1CCCCCN21",
        "COc1cccc(-n2c(CN3CCN(C)CC3)nc3ccc([N+](=O)[O-])cc3c2=O)c1",
        "COc1ccc(CN2CCN(C(=O)CN3C(=O)C4C5C=CC(C5)C4C3=O)CC2)c(OC)c1OC",
        "CC(Sc1nc2cc(C(F)(F)F)ccc2n1Cc1ccccc1)C(=O)c1ccc(F)cc1",
        "Clc1ccc(SCc2nc3sc4c(c3c(NC3CCCc5ccccc53)n2)CCC4)cc1",
        "O=C(OCC(=O)C12CC3CC(CC(C3)C1)C2)c1ccc(Cl)c([N+](=O)[O-])c1",
        "O=C(c1ccc(Cl)cc1)C1[C@H]2C(=O)[C@@H]3OC[C@@H](O3)[C@@H]12",
        "CCOC(=O)c1cc(NC(=O)c2cccc(-c3cc4ccccc4oc3=O)c2)ccc1N1CCOCC1",
        "Cc1ccc2[nH]c(/C(C#N)=C/c3ccc(OCc4ccc(Cl)cc4Cl)cc3)nc2c1",
        "COc1ccc(C(=O)Oc2c(C)cc(C(O)(C(F)(F)F)C(F)(F)F)cc2C)cc1",
        "Cc1csc(=O)n1CC(=O)OCC(=O)c1c(N)n(CC(C)C)c(=O)n(C)c1=O",
        "COc1ccc(-c2nnc3c4ccccc4c(C)nn23)cc1S(=O)(=O)N1CCN(CCO)CC1",
        "COc1cccc(/C=C2\\SC(=O)N(CCC(=O)N3CCC(C(=O)O)CC3)C2=O)c1",
        "COc1ccc(C(=O)C2=C(O)C(=O)N(c3ncccn3)C2c2ccc(OC)c(OC)c2)cc1",
        "O=C(NCc1ccc2c(c1)OCO2)c1ccc2c(c1)C(=O)N(Cc1cccnc1)C2=O",
        "CC(=O)Nc1cccc(NC(=O)CSc2nc(=O)n(Cc3ccncc3)c3c2CCC3)c1",
        "CN(C)CCCn1c(SCC(=O)N2CC(=O)Nc3ccccc32)nc2ccccc2c1=O",
        "Cc1c(NC(=O)C(C)Sc2ncnc3ccccc32)c(=O)n(-c2ccccc2)n1C",
        "Cc1ccc(C2c3[nH]c4ccccc4c3C[C@H]3C(=O)N(c4ccccc4C(=O)NCCN4CCOCC4)C(=O)N32)cc1",
        "Nc1nonc1-n1nnc(C(=O)N/N=C/c2ccc(O)cc2)c1COc1ccc(F)cc1",
        "CCOC(=O)C1=C(N)OC(C)=C(C(=O)OCc2ccccc2)C12c1cc(Br)ccc1NC2=O",
        "CC1=C(C(=O)OCc2ccccc2)C2(c3cc(Br)ccc3NC2=O)C(C(=O)OCc2ccccc2)=C(N)O1",
        "CCOC(=O)c1c(C)[nH]c(C)c1S(=O)(=O)N1CCN(C(=O)c2ccco2)CC1",
        "Cc1ccc(S(=O)(=O)NNC(=O)CCCCC(=O)NNS(=O)(=O)c2ccc(C)cc2)cc1",
        "O=C(Nc1ccc2c(c1)OCCO2)C1CCN(S(=O)(=O)c2cccc3nsnc32)CC1",
        "Cc1cc(S(=O)(=O)NC(=N)n2nc(C)c3ccccc3c2=O)c(SCc2cc3c(cc2Cl)OCO3)cc1Cl",
        "O=C(NC(Nc1cccc(C(F)(F)F)c1)C(=O)c1ccccc1)c1ccc(Cl)cc1",
        "CC1=C(C(=O)OCC(Cl)(Cl)Cl)C(c2ccc3c(c2)OCO3)NC(=O)N1",
        "O=C(OCC(=O)C12CC3CC(CC(C3)C1)C2)c1ccc([N+](=O)[O-])cc1Cl",
        "COC(=O)c1ccc(CO/N=C/c2ccc(OCc3ccc(Cl)cc3)c(OC)c2)cc1",
        "O=C(OCC(=O)C12CC3CC(CC(C3)C1)C2)c1ccc(Oc2ccc(Cl)cc2[N+](=O)[O-])cc1",
        "C/C(=N/Nc1ccc(S(=O)(=O)Nc2ccc(Cl)cc2)cc1[N+](=O)[O-])c1cccc([N+](=O)[O-])c1",
        "COc1cccc(OC)c1C(=O)Oc1ccc2c(c1)O/C(=C\\c1ccc(C(C)C)cc1)C2=O",
        "O=[N+]([O-])c1cccc(/C=N/Nc2nc(N3CCOCC3)nc(N3CCOCC3)n2)c1",
        "CCCCn1c(N)c(N(CCOC)C(=O)c2oc3ccc(OC)cc3c2C)c(=O)[nH]c1=O",
        "COc1cccc(C2c3[nH]c4ccccc4c3C[C@H]3C(=O)N(CCCCCO)CC(=O)N23)c1",
        "CC(O)CNc1nc2c(c(=O)n(C)c(=O)n2C)n1Cc1cccc(C(F)(F)F)c1",
        "O=C(COC(=O)c1ccc(F)c(S(=O)(=O)N2CCOCC2)c1)N1CCCC1=O",
        "CCCS(=O)(=O)c1nc(S(=O)(=O)c2ccc(C)cc2)c(N2CCN(CCO)CC2)s1",
        "CCc1cc(=O)oc2c(C)c(OC(C)C(=O)NC[C@H]3CC[C@H](C(=O)O)CC3)ccc12",
        "COc1ccc(C2CC(c3ccc(F)cc3)n3nc(NS(C)(=O)=O)nc3N2)cc1",
        "Cc1ccc(S(=O)(=O)N2CCC(C(=O)N3CCC[C@H]3C(=O)NCc3ccco3)CC2)cc1",
        "CN(Cc1c(C(=O)N/N=C/c2ccc(O)cc2)nnn1-c1nonc1N)C1CCCCC1",
        "Cc1cc(C)n2nc(C(=O)Nc3cccc(S(=O)(=O)N4CCCC4)c3)nc2n1",
        "Cc1ccc(C)c(NC(=O)CN(C)C(=O)C2CCN(C(=O)Nc3ccccc3)CC2)c1",
        "CCOC(=O)Nc1ccc2c(c1)N(C(=O)CCN1CCN(CCO)CC1)c1ccccc1CC2",
        "COc1ccccc1C(=O)N1CCN(Cc2nc3cc([N+](=O)[O-])ccc3n2C)CC1",
        "O=C(COc1ccc(S(=O)(=O)N2CCOCC2)cc1)Nc1ccc2c(c1)OCCO2",
        "COc1cc2c3c(n(C[C@H](O)[C@@H](O)[C@H](O)[C@H](O)CO)c(=O)c2cc1OC)-c1cc2c(cc1C3=O)OCO2",
        "CS(=O)(=O)c1ccc(Cl)c(NC(=O)CSc2nnc(C3CC3)n2C2CC2)c1",
        "CC(NC(=O)c1cc(S(=O)(=O)N2CCOCC2)ccc1F)c1ccc(-n2ccnc2)cc1",
        "CCCCOC(=O)COc1ccc(S(=O)(=O)c2ccc(OCC(=O)OCCCC)cc2)cc1",
        "Cc1ccc(NC(=O)C2=C(c3ccccc3)N=C(S)NC2c2ccc(F)cc2)cc1",
        "CC1(C)O[C@H]2O[C@H](C(=O)Oc3ccc4ccccc4c3)[C@@H]3OC(C)(C)O[C@@H]3[C@H]2O1",
        "CCOC(=O)C1=C(N)OC(c2ccccc2)=C(C(=O)OCC)C12c1cc(Br)ccc1NC2=O",
        "CN(C(=O)CSc1nc(C2CC2)n(-c2ccccc2)n1)C1CCS(=O)(=O)C1",
        "O=C(Cn1nnc2ccccc21)N(c1ccc(F)cc1)C(C(=O)NCc1ccco1)c1cccnc1",
        "O=C(CN(CC1CCCO1)C(=O)CNS(=O)(=O)c1ccccc1)NCc1ccccc1",
        "COc1ccc(OCC(=O)Nc2ccc3c(c2)nc(CN2CCN(C(C)=O)CC2)n3C)cc1",
        "CCCn1c(SCC(=O)Nc2ccc3c(c2)OCO3)nc2c1c(=O)n(C)c(=O)n2C",
        "CCOC(=O)c1ccc2nc(CCC(=O)N[C@H](C(=O)OC)C(C)C)c(=O)n(CC)c2c1",
        "CS(=O)(=O)c1nc(S(=O)(=O)c2ccc(Cl)cc2)c(NCCN2CCOCC2)s1",
        "CCc1noc(C)c1C(=O)N(Cc1ccccc1)c1c(N)n(CCOC)c(=O)[nH]c1=O",
        "CCOC(=O)C1CCN(C(=O)C2CC(=O)N(c3ccc4c(c3)OCCO4)C2)CC1",
        "COc1ccccc1N1CCN(C(=O)C2CCN(S(=O)(=O)c3ccccc3F)CC2)CC1",
        "Cn1c2nc(SCC(=O)Nc3ccccc3)n(Cc3ccccc3)c2c(=O)[nH]c1=O",
        "Cc1c(Br)c([N+](=O)[O-])nn1C(C)C(=O)Nc1c(C)n(C)n(-c2ccccc2)c1=O",
        "Cn1c2ncn(CC(=O)OCC(=O)Nc3ccc(Cl)cc3Cl)c2c(=O)n(C)c1=O",
        "CCC(C)(C)NC(=O)c1ccc2c(c1)N(CC(=O)N1CCCC1)C(=O)C(C)(C)O2",
        "Cc1cc(C(C)(C)c2ccc(OC(=O)c3ccccc3F)c(C)c2)ccc1OC(=O)c1ccccc1F",
        "CC(=O)CC(C)(C)S(=O)(=O)c1ccc(Cc2ccc(S(=O)(=O)C(C)(C)CC(C)=O)cc2)cc1",
        "COc1cc(C)cc2oc(=O)c(CC(=O)N3C[C@H]4C[C@@H](C3)c3cccc(=O)n3C4)c(C)c12",
        "CC(=O)OC1CC2(O)[C@@H]3CCC4CC(OC5CC(O)C(OC6CC(O)C(OC7CC(O)C(O)C(C)O7)C(C)O6)C(C)O5)CCC4(C)[C@H]3CCC2(C)C1C1=CC(=O)OC1",
        "COC(=O)c1ccccc1S(=O)(=O)N1CCC(C(=O)OCC(=O)NCC(F)(F)F)CC1",
        "COc1cc(/C=C(\\C#N)C(=O)Nc2ccccc2C(=O)O)cc(Cl)c1OC(C)C",
        "CCOC(=O)CNC(=O)C(F)(F)C(F)(F)C(F)(F)C(F)(F)C(=O)NCC(=O)OCC",
        "O=C(COC(=O)c1ccc2c(c1)-c1ccccc1C2=O)c1cccc([N+](=O)[O-])c1",
        "CC(O)C(NS(=O)(=O)c1ccc(Cl)cc1)C(=O)OCc1nnc(-c2ccccc2)o1",
        "O=C(CCNC(=O)C1CCN(S(=O)(=O)c2ccc(F)cc2)CC1)Nc1ccccc1",
        "CN(C)S(=O)(=O)c1ccc(C(=O)NCCS(=O)(=O)N2CCN(c3ccc(F)cc3)CC2)cc1",
        "CCC(C)N(C(=O)CSc1nc(C)c(C)c(C)c1C#N)C1CCS(=O)(=O)C1",
        "CN(C(=O)CSc1nc2c(cnn2-c2ccccc2)c(O)n1)C1CCS(=O)(=O)C1",
        "COc1cccc(-n2c(N)cc(=O)nc2SCC(=O)N2CCN(c3ccccc3)CC2)c1",
        "O=C(NCc1cccnc1)C1CCN(S(=O)(=O)c2cccc(C(F)(F)F)c2)CC1",
        "COCCNC(=O)C(c1ccc(C)o1)N(CC1CCCO1)C(=O)Cn1nnc2ccccc21",
        "CCN(C(=O)CSc1nnc(-c2ccco2)n1CC)c1c(N)n(Cc2ccccc2)c(=O)[nH]c1=O",
        "CCCN(C(=O)CSCc1c(C)noc1C)c1c(N)n(Cc2ccccc2)c(=O)[nH]c1=O",
        "Cc1cccc(OCC(O)Cn2c(NCc3cccnc3)nc3c2c(=O)[nH]c(=O)n3C)c1",
        "Cc1ccc(S(=O)(=O)N2CCC3(CC2)OCCN3S(=O)(=O)c2cccs2)cc1",
        "CCN(CC)S(=O)(=O)c1ccc2c(c1)N(CC(=O)NCCN1CCOCC1)C(=O)CO2",
        "C=CCN(CC(=O)NC1CCS(=O)(=O)C1)S(=O)(=O)c1ccccc1C(F)(F)F",
        "Cc1c(S(=O)(=O)N2CCN(c3cccc(Cl)c3)CC2)c(=O)n(C)c(=O)n1C",
        "CCCCN(C(=O)COc1ccc(C)cc1OC)c1c(N)n(CCC)c(=O)[nH]c1=O",
        "COc1ccc(OC)c(NC(=O)C2CCCN(S(=O)(=O)c3ccc(-n4cnnn4)cc3)C2)c1",
        "Cn1c2ncn(CC(=O)N3CCN(S(=O)(=O)c4ccc(Cl)cc4)CC3)c2c(=O)n(C)c1=O",
        "CCOC(=O)N1CCN(C(=O)CCNS(=O)(=O)c2ccc3c(c2)c(=O)n(C)c(=O)n3C)CC1",
        "CCC(C)NC(=O)[C@@H]1CCCN1C(=O)C1CCN(S(=O)(=O)c2ccc(C)cc2)CC1",
        "CCC(c1nnc(SCC(=O)Nc2ccc(C(C)=O)cc2)n1C1CCCCC1)N(C)C",
        "COc1cc(OC)c(C2c3c(cc(C)n(CCCN4CCOCC4)c3=O)OC(N)=C2C#N)cc1OC",
        "COc1cccc(CNC(=O)c2cc(O)nc3ccc(S(=O)(=O)N4CCOCC4)cc32)c1",
        "COc1ccc2c(c1)CCc1c(C(=O)Nc3c(C)n(C)n(-c4ccccc4)c3=O)noc1-2",
        "Cc1c2cc(S(=O)(=O)N3CCC4(CC3)OCCO4)ccc2oc1C(=O)NCc1ccco1",
        "CCN(c1ccc(C(=O)CN2C(=O)NC3(CCOc4ccccc43)C2=O)cc1)S(C)(=O)=O",
        "COc1cc(/C=C(\\C#N)C(=O)Nc2ccc(OC(F)(F)F)cc2)cc(OC)c1OC",
        "O=C(Nc1cc(S(=O)(=O)N2CCOCC2)ccc1N1CCOCC1)c1cn2ccccc2n1",
        "CN(C(=O)CS(=O)(=O)c1cn(CC(=O)N2CCCCC2)c2ccccc12)c1ccccc1",
        "Cc1ccccc1OCC(=O)N(CC(C)C)c1c(N)n(CC(C)C)c(=O)[nH]c1=O",
        "Cc1ccc(S(=O)(=O)NCCC(=O)Nc2ccc(S(=O)(=O)NC(C)C)cc2)cc1",
        "O=C(CSc1nnc(-c2cccc(S(=O)(=O)N3CCCCC3)c2)o1)c1ccc[nH]1",
        "CCOC(=O)c1c(C)[nH]c(C(=O)OCC(=O)N(C)C2CCS(=O)(=O)C2)c1C",
        "O=C(COC(=O)c1ccccc1F)Nc1cc(S(=O)(=O)N2CCOCC2)ccc1N1CCCC1",
        "CCc1nc(N2CCN(C(=O)c3ccccc3)CC2)c(C#N)c2c1COC(C)(C)C2",
        "CCCCc1cc(=O)oc2cc(OCC(=O)NC(Cc3c[nH]c4ccc(O)cc34)C(=O)O)ccc12",
        "O=C(CSc1nnc(NC(=O)C2CC(=O)N(c3ccc(F)cc3)C2)s1)NCc1ccco1",
        "CC1CCCCC12NC(=O)N(CC(=O)N1CCN(C(=O)c3ccco3)CC1)C2=O",
        "CCC(C)NC(=O)C(Cc1ccccc1)NS(=O)(=O)c1ccc2c(c1)CCC(=O)N2",
        "COc1ccc(S(=O)(=O)N2CCN(c3ccc(F)cc3)CC2)cc1C(=O)N1CCOCC1",
        "CC(=O)OCC1=C(C(=O)O)N2C(=O)C(NC(=O)C(C)Cn3cc(Cl)cn3)C2SC1",
        "CN(C)S(=O)(=O)c1cc(NC(=O)COC(=O)C2CSC3(C)CCC(=O)N23)ccc1Cl",
        "Cc1csc(NC(=O)CSc2nnc(-c3ccc(S(=O)(=O)N4CCCC4)cc3)o2)n1",
        "COc1cccc(C2CC(C(F)(F)F)n3ncc(C(=O)NCC4CCCO4)c3N2)c1",
        "CNC(=O)NC(=O)C(C)OC(=O)c1cc(C(F)(F)F)cc(C(F)(F)F)c1",
        "Cc1oc2cc(OCCN3CCN(c4ccccc4)CC3)ccc2c(=O)c1-c1ccccc1",
        "COc1cccc(/C=C2\\Oc3cc(OS(=O)(=O)c4ccccc4)ccc3C2=O)c1OC",
        "COC(=O)CC1=C(C(=O)OC)C(c2ccc(C(=O)OC)cc2)C(C#N)=C(N)O1",
        "COc1ccc(S(=O)(=O)Oc2ccc3c(c2)O/C(=C\\c2ccc(C(C)(C)C)cc2)C3=O)cc1",
        "CCN(CC)S(=O)(=O)c1ccc(-c2nnc(SCC(=O)Nc3ccc4c(c3)OCO4)o2)cc1",
        "Cc1cc(=O)oc2c(C)c(OC(C)C(=O)NC[C@H]3CC[C@H](C(=O)O)CC3)ccc12",
        "Cc1oc2cc3oc(=O)c(CC(=O)N[C@H](C(=O)N[C@@H](C(=O)O)c4ccccc4)C(C)C)c(C)c3cc2c1C",
        "O=C1NC(=O)N(Cc2ccco2)C(=O)/C1=C/NC1CCN(Cc2ccccc2)CC1",
        "CCOc1ccc(NC(=O)c2cccc(NC(C)=O)c2)cc1S(=O)(=O)N1CCCC1",
        "CC(=O)N1CCN(CC(=O)Nc2ccc(Cl)c(S(=O)(=O)N(C)C)c2)CC1",
        "COc1ccc(OCC(=O)Nc2ccc3c(c2)nc(CCN2CCN(C)CC2)n3C)cc1",
        "COC(=O)C1=C2SC(C)C(=O)N2C(N)=C(C#N)C1c1ccc(OC)c(OC)c1",
        "CCC(C)[C@H](NC(=O)N1CC(=O)Nc2ccccc21)C(=O)N[C@@H](C(=O)O)c1ccccc1",
        "O=C(CN(CC1CCCO1)C(=O)CNS(=O)(=O)c1ccc(F)cc1)NCc1ccco1",
        "COc1ccc2[nH]c3c(c2c1)CCN1C(=O)N([C@@H](C)C(=O)NCc2ccc(C)cc2)C(=O)[C@]31C",
        "COc1ccc(/C=C2\\Oc3cc(OCC(=O)N4CCC(C(=O)O)CC4)ccc3C2=O)c(OC)c1OC",
        "CCN(CC)S(=O)(=O)c1ccc(NC(=O)COC(=O)c2cnc(O)nc2O)cc1",
        "COc1cccc(C(=O)C2=C(O)C(=O)N(CCN3CCOCC3)C2c2cccnc2)c1",
        "COC(=O)C1=C(N)OC2=C(C(=O)CC(c3ccccc3)C2)C1c1ccc(C)cc1",
        "CC(CC(=O)Nc1ccc(F)cc1F)S(=O)(=O)c1ccc2c(c1)NC(=O)CO2",
        "COc1ccc(CCNC(=O)c2cc3c(nc4ccccn4c3=O)n(Cc3ccco3)c2=N)cc1",
        "O=C(/C=C/c1ccccc1)N1CCC(C(=O)NCc2ccc(C(F)(F)F)cc2)CC1",
        "O=C(CC12CC3CC(CC(Br)(C3)C1)C2)Oc1ccc([N+](=O)[O-])cc1",
        "N#Cc1c(Cl)nc(N/N=C/c2ccc(Cl)c([N+](=O)[O-])c2)c(Cl)c1Cl",
        "Cc1ccc(CC(C(=O)NCCC23CC4CC(CC(C4)C2)C3)C(C)C(=O)O)cc1",
        "CCOC(=O)c1ccc(Oc2c(=O)c3ccc(OS(C)(=O)=O)cc3oc2C(F)(F)F)cc1",
        "O=C(O)CC/C(=C/c1cccc(Oc2ccc(Cl)cc2)c1)c1nc2ccccc2s1",
        "CC(C(=O)N1CCc2ccccc2C1)N(C)CC(=O)Nc1ccc(Cl)c(C(F)(F)F)c1",
        "N#Cc1ccc(CN2CCCN(CC(=O)N3CCN(C4CCS(=O)(=O)C4)CC3)CC2)cc1",
        "CCc1ccc(/C=C2\\Oc3cc(OS(=O)(=O)c4ccc(OC)cc4)ccc3C2=O)cc1",
        "CC(=O)NC(Cc1cccc(C)c1)C(=O)NC1CCN(S(=O)(=O)c2ccccc2)CC1",
        "C[C@]1(O)CC[C@H]2[C@@H]3CCC4=CC(=O)CC[C@]4(C)[C@H]3CC[C@@]21C",
        "C=CCN1C(=O)c2ccc(C(=O)N(CC(C)C)c3c(N)n(CCCC)c(=O)[nH]c3=O)cc2C1=O",
        "CCOc1ccc(S(=O)(=O)Nc2cccc(C(=O)NCCCN3CCCC3=O)c2)cc1",
        "COc1ccc(NC(=O)Cn2nnc(C(=O)NCc3ccc4c(c3)OCO4)c2N)c(OC)c1",
        "Cn1c(N)c(C(=O)CSc2nnc(-c3cccs3)n2C2CC2)c(=O)[nH]c1=O",
        "CCCN1CCN(C(=O)CS(=O)(=O)Cc2nc(-c3cccc(OC)c3)oc2C)CC1",
        "COc1ccc(C(=O)N2CCN(c3ccc(C(C)=O)cc3F)CC2)cc1S(=O)(=O)NC1CC1",
        "COC(=O)CSc1nnc(CNC(=O)c2ccc(S(=O)(=O)N3CCOCC3)cc2)n1C",
        "Cc1oc2c(cc3c(C)c(CCC(=O)NCCCN4CCOCC4)c(=O)oc3c2C)c1C",
        "Cc1c(OC(C)C(=O)NCc2ccccn2)ccc2c(-c3ccccc3)cc(=O)oc12",
        "CCN(CC)C(=O)c1ccccc1NS(=O)(=O)c1ccc(OC)c(C(=O)N2CCOCC2)c1",
        "N#Cc1ccccc1S(=O)(=O)N1CCN(CC(=O)Nc2ccccc2C(=O)NC2CC2)CC1",
        "Cc1cccc(C(=O)N2CCN(c3ccc([N+](=O)[O-])c(N4CCOCC4)c3)CC2)c1",
        "CCOc1ccc(Oc2ccc(S(=O)(=O)N3CCOCC3)cc2NC(=O)CCNC(C)=O)cc1",
        "CCC(C)N(C(=O)CSc1ncnc2c1cnn2-c1ccccc1)C1CCS(=O)(=O)C1",
        "O=C(CN1C(=O)NC2(CCOc3ccccc32)C1=O)N(Cc1ccccc1)C1CCS(=O)(=O)C1",
        "CCOC(=O)Cn1c(=O)oc2cc(S(=O)(=O)N3CCN(C(=O)c4ccco4)CC3)ccc21",
        "CN(C)c1ccc(CNC(=O)CCS(=O)(=O)c2ccc3c(c2)oc(=O)n3C)cc1",
        "O=C(Nc1ccc(N2CCN(C(=O)c3ccco3)CC2)cc1)c1ccc2c(c1)OCO2",
        "CCN(CC)S(=O)(=O)c1ccc(C)c(NC(=S)Nc2ccccc2C(C)(C)C)c1",
        "Cc1ccc(S(=O)(=O)Oc2ccc3c(c2)O/C(=C\\c2cccc(C)c2)C3=O)cc1",
        "COc1cccc2cc(-c3cc(=O)oc4cc(OCC(=O)OC(C)(C)C)ccc43)c(=O)oc21",
        "O=C(c1ccco1)C1=C(O)C(=O)N(CCCn2ccnc2)C1c1cccc([N+](=O)[O-])c1",
        "N=C1/C(=C/c2c[nH]c3ccccc23)C(=O)N=C2SC(CC(=O)N3CCOCC3)=NN21",
        "CCOc1ccc(S(=O)(=O)Nc2ccc(C(=O)NCCCN3CCCC3=O)cc2)cc1",
        "Cc1cc(C)n(-c2ccc(C(=O)OCC(=O)N(CC(C)C)C3CCS(=O)(=O)C3)cc2)n1",
        "NC(=O)c1nn(CC(=O)Nc2cccc(S(=O)(=O)N3CCCC3)c2)c(=O)c2ccccc21",
        "CC(=O)c1ccc(N(CC(=O)NC2CCCC2)C(=O)CCC(=O)Nc2cc(C)on2)cc1",
        "COc1ccc(OCCC(=O)OCC(=O)Nc2cccc(S(=O)(=O)N3CCOCC3)c2)cc1",
        "COc1ccc(O[C@@H]2O[C@H](COC(C)=O)[C@@H](OC(C)=O)[C@H](OC(C)=O)[C@H]2NC(C)=O)cc1",
        "COc1ccc(NC(=O)CS(=O)(=O)c2cn(CC(=O)N3CCCC3)c3ccccc23)c(OC)c1",
        "COc1ccc(OC)c(N(CC(=O)O)S(=O)(=O)c2ccc(OC)c(OC)c2)c1",
        "CCN(CC)S(=O)(=O)c1ccc2nc(O)cc(C(=O)N(C)C3CCS(=O)(=O)C3)c2c1",
        "COc1cc(OC)cc(C(=O)Nc2ccc(S(=O)(=O)Nc3cc(C)on3)cc2)c1",
        "O=[N+]([O-])c1ccc(N/N=C/C2=C(Sc3nc4ccccc4o3)CCCC2)cc1",
        "O=C(Nc1ccc(N2CCOCC2)cc1)c1ccc2c(c1)S(=O)(=O)c1ccccc1C2=O",
        "COc1ccc(/C=C/C(=O)OCCCC(c2ccc(F)cc2)c2ccc(F)cc2)cc1OC",
        "Cc1cc(C)c(S(=O)(=O)N(C)C(=O)c2cc3c(s2)CCCCCC3)c(C)c1",
        "O=C(/C=C/c1ccco1)Oc1ccc(Sc2ccc(OC(=O)/C=C/c3ccco3)cc2)cc1",
        "COc1ccc(NCC(=O)NNC(=O)C(Cc2ccccc2)NC(=O)c2cccs2)cc1",
        "COc1ccc(/C=C2\\Oc3cc(OS(=O)(=O)c4ccc(C)cc4)ccc3C2=O)c(OC)c1OC",
        "O=C(Nc1ccc(NC(=O)C(F)(F)C(F)(F)OC(F)(F)F)cc1)C(F)(F)C(F)(F)OC(F)(F)F",
        "Cc1ccccc1C(=O)Nc1cccc(NC(=O)CC23CC4CC(CC(C4)C2)C3)c1",
        "Cn1c2ncn(CC(=O)OCC(=O)Nc3cccc(Cl)c3)c2c(=O)n(C)c1=O",
        "COc1ccc(NS(=O)(=O)c2cc(NC(=O)C3CC3)ccc2N2CCOCC2)cc1",
        "CCCc1nc(SCC(=O)N2CCC(C)CC2)c2c(=O)n(C)c(=O)n(C)c2n1",
        "COc1cccc(C2c3[nH]c4ccccc4c3C[C@H]3C(=O)N(c4ccccc4C(=O)NCCCN4CCOCC4)C(=O)N32)c1",
        "Cc1ccc(S(=O)(=O)N2CCC(C(=O)N3CCC[C@H]3C(=O)NCC(C)C)CC2)cc1",
        "COc1ccc(CNC(=O)C2CCCN(S(=O)(=O)c3c(C)n[nH]c3C)C2)cc1",
        "O=C(Nc1c2c(nn1-c1ccc([N+](=O)[O-])cc1)CS(=O)(=O)C2)c1cccs1",
        "CCN(CC)S(=O)(=O)c1cccc(C(=O)OCc2cc(=O)n3nc(C)sc3n2)c1",
        "CCc1ccccc1S(=O)(=O)Cc1ccc(C(=O)N2CCN(c3ccccn3)CC2)o1",
        "O=C(Nc1ccccc1N1CCN(C(=O)c2ccccc2)CC1)c1ccc([N+](=O)[O-])o1",
        "CCOC(=O)Cc1nnc(NC(=O)CSc2nnc3c4ccccc4n(C(C)C)c3n2)s1",
        "CCOc1ccc(NC(=O)C2CCN(S(=O)(=O)c3ccc4c(c3)N(C(C)=O)CCO4)CC2)cc1",
        "CCOc1ccc(NC(=O)C2CCCN(S(=O)(=O)c3c(C)n[nH]c3C)C2)cc1",
        "COC(=O)c1cc(NS(=O)(=O)c2cccs2)cc(NS(=O)(=O)c2cccs2)c1",
        "Cc1ccc(S(=O)(=O)N2CCOCC2)cc1NC(=O)CN1CCN(c2ccc(O)cc2)CC1",
        "CC1CCN(C(=O)CSc2nnc(CNC(=O)c3ccc(S(=O)(=O)N(C)C)cc3)o2)CC1",
        "CCOC(=O)N1CCN(C(=O)CCc2ccc(S(=O)(=O)NCC(C)C)cc2)CC1",
        "NS(=O)(=O)c1ccc(N/N=C/C=C/c2ccccc2[N+](=O)[O-])c([N+](=O)[O-])c1",
        "O=S(=O)(c1ccccc1)c1cnc2cc3c(cc2c1N1CCN(Cc2ccccc2)CC1)OCCO3",
        "Cc1nn(-c2ccc(C(=O)NC3CCCC3)cc2)c(NC(=O)C2CC2)c1-c1ccccc1",
        "CC1Cc2ccccc2N1C(=O)COC(=O)c1ccc(Cl)c([N+](=O)[O-])c1",
        "NS(=O)(=O)c1ccc(N/N=C/c2ccc(Br)cc2)c([N+](=O)[O-])c1",
        "O=C(/C=C/c1ccccc1)N/N=C(\\N=Nc1cccc(Cl)c1)c1ccc([N+](=O)[O-])cc1",
        "Cc1ccc(OCC2OC(n3cnc4nc(Cl)nc(Cl)c43)CC2Oc2ccc(C)cc2)cc1",
        "CCOC(=O)C(NCc1ccc2c(c1)OCO2)(NC(=O)c1ccccc1)C(F)(F)F",
        "COC(CNC(=O)C(NC(=O)OC(C)(C)C(Cl)(Cl)Cl)c1c[nH]c2ccccc21)OC",
        "C(=C/c1ccc2c(c1)OCO2)\\C(/C=C/c1ccc2c(c1)OCO2)=NNC1=NCCN1",
        "O=C(CC[C@H]1CC[C@H](CCC(=O)Nc2ccc(P(=O)(O)O)cc2)CC1)Nc1ccc(P(=O)(O)O)cc1",
        "Cc1ccc(S(=O)(=O)OCC2OC(n3cnc4c3ncnc4NC(=O)c3ccccc3)C3OC(C)(C)OC23)cc1",
        "N#Cc1nc(CCC(c2nc(C#N)c(N)o2)N2C(=O)c3ccccc3C2=O)oc1N",
        "c1ccc(-c2cc(-c3ccccc3)c3c(n2)-c2ccccc2N=C(N2CCCCC2)C3)cc1",
        "CC[C@@H](CO)NC(=O)CCOC(Cc1cnccn1)c1ccc(C(F)(F)F)cc1",
        "Cn1c(=O)n(-c2ccc(Cl)c(/C=N/OC(C)(C)C)c2)c(=O)cc1C(F)(F)F",
        "C=CCOC(=O)C1C2OC3(C(C(=O)OC)=C2C(=O)OC)c2ccccc2CCC13",
        "COC(=O)/C=C/c1ccc(C(=O)NCCCN2CCC(C(O)(c3ccccc3)c3ccccc3)CC2)cc1",
        "CC(C)OP(=O)(OC(C)C)C(=Cc1cc(C(C)(C)C)c(O)c(C(C)(C)C)c1)P(=O)(OC(C)C)OC(C)C",
        "O=C(c1cc(Cl)nc(Cl)c1)N1CC[NH+](C(c2ccccc2)c2ccccc2)CC1",
        "CCOC(=O)CNC(=O)C(C)NC(=O)C(NC(=O)CC(c1ncc[nH]1)c1ncc[nH]1)C(C)CC",
        "CC1OC2OC3C(O)C(O)COC3OC(=O)[C@]34CCC(C)(C)CC3C3=CCC5[C@@]6(C)C[C@H](O)[C@H](OC7OC(CO)C(O)C(O)C7OC7OC(COC(=O)CC(C)(O)CC(=O)OC1C(O)C2O)C(O)C(O)C7O)[C@@](C)(CO)C6CC[C@@]5(C)[C@]3(C)CC4",
        "O=C(N=Nc1ccc([N+](=O)[O-])cc1[N+](=O)[O-])NNc1ccc([N+](=O)[O-])cc1[N+](=O)[O-]",
        "CC(=O)OCC1OC(n2c(-c3ccc(C)cc3)cc(-c3ccccc3)c(C#N)c2=S)C(OC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "CC(=O)O[C@@H]1[C@@H](CN=[N+]=[N-])OCO[C@H](CN=[N+]=[N-])[C@H]1OC(C)=O",
        "CCN1c2cc(C)c(C)cc2O/C1=C/C=C1/SC(/C=C2\\[Se]c3ccccc3N2CCC(=O)O)=[N+](CC)C1=O",
        "ClCCCCCc1c2ccc(n2)c(CCCCCCl)c2ccc([nH]2)c(CCCCCCl)c2ccc(n2)c(CCCCCCl)c2ccc1[nH]2",
        "CCOC(=O)C(NC(=O)c1[nH]c(=O)n(Cc2ccccc2)c1N)C(=O)OCC",
        "CCOC(=O)NC(Nc1c(F)c(F)c(F)c(F)c1F)(C(F)(F)F)C(F)(F)F",
        "C[C@]12CC[C@H]3[C@@H](CC=C4C[C@@H](O)CC[C@@]43C)[C@@H]1C[C@H](N1CCOCC1)C2=O",
        "COc1c2c(c(O)c3c1C(=O)c1c(O)c(C)c(O)cc1C3=O)-c1c(cc3cnn(CC(=O)O)c(=O)c3c1O)CC2",
        "CC(C)CC(NC(=O)C(Cc1ccccc1)NC(=O)C(Cc1ccccc1)NC(=O)OC(C)(C)C)C(=O)OCc1ccccc1",
        "COc1ccc(COC(=O)NCC(=O)NC(C)C(=O)NC(Cc2ccccc2)C(=O)OCc2ccccc2)cc1",
        "O=C(Cc1ccc([N+](=O)[O-])cc1)NN1C(=O)/C(=C/c2cccc(Cl)c2)SC1c1ccccc1Cl",
        "COc1ccc(C2Oc3cc(OC)cc(O)c3C(=O)C2C2C(=O)c3c(O)cc(OC)cc3OC2c2ccc(O)cc2)cc1",
        "CO/C(=C\\C=C\\c1cc2cc(Cl)c(Cl)cc2[nH]1)C(=O)Nc1ccc(OC)nc1",
        "CC[C@]12CCCN3C[C@@H](Br)[C@@]4(c5ccccc5N=C4[C@@](Cl)(C(=O)OC)C1)[C@@H]32",
        "CCCc1cc(=O)oc2c1c(OC)cc1c2C(=O)[C@@H](C)[C@@H](C)O1",
        "CC[C@H](C)[C@@H]([C@@H](CC(=O)N1CCC[C@H]1[C@H](OC)[C@@H](C)C(=O)N[C@@H](Cc1ccccc1)C(=O)Nc1nc(-c2ccccc2)cs1)OC)N(C)C(=O)[C@@H](NC(=O)[C@H](C(C)C)N(C)C)C(C)C",
        "CC(/C=C/C=C(\\C)CCC/C(C)=C/C1OC(=O)C(C)=C1O)=C\\CCc1ccoc1",
        "CS(C)(=O)[Ru](Cl)(Cl)(S(C)(C)=O)(S(C)(C)=O)S(C)(C)=O",
        "CNC(=O)N(C)CCN1CCC(c2cn(-c3ccc(F)cc3)c3cc(Cl)ccc23)CC1",
        "CN(C)c1ccc(/C=C2\\CN(C)C/C(=C\\c3ccc(N(C)C)cc3)C2=O)cc1",
        "CCN1C(=O)S/C(=C\\C2=C([O-])N(CC)/C(=C\\c3sc4cc(OC)ccc4[n+]3CCC(=O)O)S2)C1=O",
        "C/N=c1\\[nH]c(=O)nc(C([N+](=O)[O-])[N+](=O)[O-])[nH]1",
        "CCCC(=O)OC1CC(n2cc(F)c(=O)[nH]c2=O)OC1COP(=O)(OCC(Cl)(Cl)Cl)OCC(Cl)(Cl)Cl",
        "COc1ccc2c(c1)sc(/C=C1\\Sc3cc4c(cc3N1CCC(=O)O)OCO4)[n+]2C",
        "C[n+]1c(/C=C2\\Sc3cc4c(cc3N2Cc2ccc(C(=O)O)cc2)OCO4)sc2ccccc21",
        "CCOC(=O)C(CC(C)C)NC(=O)C(Cc1ccccc1)NC(=O)CNC(=O)OC(C)(C)C",
        "CC(=O)OCC1OC(SC2=NC(=O)CS2)C(OC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "CCOc1cc2c(cc1O)C(=O)C[C@H]1[C@@H]3CC[C@H](O)[C@@]3(C)CC[C@H]21",
        "COP(=O)(/C=C/c1cc(C(=O)OCc2ccccc2)n(S(=O)(=O)c2ccccc2)c1)OC",
        "C=C1C(=O)O[C@@H]2[C@H]1C[C@@H]1O[C@H]2[C@H](C)CCC[C@]2(C)O[C@H]2CC[C@@]1(C)O",
        "CC(C)CC(NS(=O)(=O)c1ccc(F)cc1)C(=O)Nc1ccc(OC(F)(F)F)cc1",
        "CC(=O)OC1CCC2C3CC(OC(C)=O)c4c(C)ccc(OS(=O)(=O)c5ccccc5)c4C3CCC12C",
        "CCCCCCCCCCCCCCCCNc1ccn(C2CC(OP(=O)(O)OCC3OC(n4cc(CC)c(=O)[nH]c4=O)CC3O)C(CO)O2)c(=O)n1",
        "Cc1ccc(S(=O)(=O)Nn2c(-c3ccccc3)c(C#N)c(-c3ccc([N+](=O)[O-])cc3)c(C#N)c2=O)cc1",
        "CC[C@@]1(O)C(=O)OCc2c1cc1n(c2=O)C(O)c2cc3c(O)cccc3nc2-1",
        "COc1ccc(S(=O)(=O)N2CCN(CCCn3c(=O)c4ccccc4c(=O)n3C)CC2)cc1",
        "CC(C)[Si](Oc1cc2c(ccc3c2CCCC3(C)C)c(O)c1[C@@H](C)CO[Si](C)(C)C(C)(C)C)(C(C)C)C(C)C",
        "C[C@]12CC[C@H](OC(=O)c3ccccc3)C[C@]1(O)C(=O)CC1CCCC12",
        "CCC[C@@H](N)CN(Cc1ccccc1)[C@H](C)CN1CCC[C@@H]1CN(Cc1ccccc1)[C@@H](CCCCNCc1ccccc1)CNCc1ccccc1",
        "O=[N+]([O-])c1ccc(N2N=C(c3ccc4ccccc4c3O)CC2c2ccc(Cl)cc2Cl)c([N+](=O)[O-])c1",
        "COc1ccc(-c2c(C#N)c(-c3ccc(C)cc3)n(NS(=O)(=O)c3ccc(C)cc3)c(=O)c2C#N)cc1",
        "CCOC(=O)CC1c2cc3c(cccc3OC)nc2-c2cc3c(c(=O)n21)COC(=O)[C@]3(O)CC",
        "C=C1CC(C)CC2CC=CC(C/C=C\\C(=O)OC3CC(OC3/C=C/C3CC(C)=CCO3)C(O)C(O)C1)O2",
        "COc1ccc2[nH]c(=O)n(CCN3CCC(C(=O)c4ccc(F)cc4)CC3)c(=O)c2c1OC",
        "FC(F)(F)c1cnc(N2CCN(Cc3nnc(S)n3-c3ccccc3)CC2)c(Cl)c1",
        "CC(=O)C1(O)CC(OC2CC(N)C(O)C(C)O2)c2c(O)c3c(c(O)c2[C@H]1F)C(=O)c1ccccc1C3=O",
        "Cc1c(C#N)c(=S)n(C2OC(CO)C(O)C(O)C2O)c(C)c1N=Nc1ccccc1",
        "CC(=O)O[C@@]12CO[C@@H]1C[C@H](O)[C@@]1(C)C(=O)[C@H](O)C3=C(C)[C@@H](OC(=O)[C@H](O)[C@@H](NC(=O)[C@H](Cc4c[nH]c5ccccc45)NC(=O)OC(C)(C)C)c4ccccc4)C[C@@](O)([C@@H](OC(=O)c4ccccc4)[C@H]21)C3(C)C",
        "COc1cc2c(O)c3c(c(-c4cc(OC)c(O)c(OC)c4)c2cc1OC)C(=O)OC3",
        "CC(=O)O[C@@]12OCC1C[C@H](O)[C@@]1(C)C(=O)[C@H](O)C3=C(C)C(O)[C@H](O)[C@@](O)(C(OC(=O)c4ccccc4)C21)C3(C)C",
        "CCCc1cc(=O)oc2c1c1c(c3c2C(O)C(C)=C(C)O3)C=CC(C)(C)O1",
        "CC(C)CC(C(=O)N(C)C(CC(C)C)C(=O)N(C)C(C(=O)OC(C)(C)C)C(C)C)N(C)C(=O)C(CCCNC(=O)OC(C)(C)C)NC(=O)OCC1c2ccccc2-c2ccccc21",
        "C[C@H]1[C@H]2[C@H](C=C3[C@@H]4CC[C@H]5Cc6nc7c(nc6C[C@]5(C)[C@H]4CC(=O)[C@@]32C)C[C@@H]2CC[C@H]3C4=C[C@@H]5O[C@]6(CC[C@@H](C)CO6)[C@@H](C)[C@@H]5[C@@]4(C)[C@H](O)C[C@@H]3[C@@]2(C)C7)O[C@]12CC[C@@H](C)CO2",
        "COC(C/C=C/N(C)C=O)C(C)C(=O)CCC(C)C(OC)C(C)C1OC(=O)/C=C/C=C(\\C)CC(OC)C(OC)C2=CC(=O)O[C@H]([C@H]2O)C(C)C(OC)CC(OC)/C=C/C(C)C(O)CC(OC)/C=C/C1C",
        "C=C1CCC([C@@H](C)C(=O)OC)[C@](C)(CC)C1CCc1ccc2c(c1)C(=O)C=CC2=O",
        "COC(=O)C1(C(=O)OC)Cc2c(OC(=O)c3cccc(OC)c3C#N)ccc(OC(=O)c3cccc(OC)c3C#N)c2C(=O)C1",
        "COc1ccc2c(c1)N(CCC(=O)O)/C(=C/C1=[N+](CC(=O)O)C(=O)/C(=C/C=C3/Oc4ccccc4N3Cc3ccccc3)S1)[Se]2",
        "CCOC(=O)c1ccc(N2C(=S)N(C(=O)c3ccccc3)C(=N/c3ccccc3)/C2=N\\c2ccccc2)cc1",
        "CCOC(=O)NOc1cc2c(cnn2-c2c(Cl)cc(C(F)(F)F)cc2Cl)cc1[N+](=O)[O-]",
        "CCOC(=O)C1C(=O)CC(O)(c2ccccc2)C(C(=O)OCC)C1c1ccc(N(C)C)cc1",
        "COc1ccc(-n2c(C)nc3ccc(NC4OC(CO)C(O)C(O)C4O)cc3c2=O)cc1",
        "CCCc1cc(=O)oc2c1c1c(c3c2[C@@H](O)[C@H](C)[C@@H](C)O3)CCC(C)(C)O1",
        "CCC1(c2ccc(N3C(=O)c4cccc5cc([N+](=O)[O-])cc(c54)C3=O)cc2)CCC(=O)NC1=O",
        "COC(=O)Oc1cc2c(c(O)c1C)C(=O)c1c(OC)c3c(c(O)c1C2=O)-c1c(cc2cnn(-c4ccccc4)c(=O)c2c1O)CC3",
        "COc1ccc2[nH]c(-c3ccco3)c(-c3cc(OC)c(OC)c(OC)c3)c2c1",
        "COC(=O)C1(c2ccc(N(C)C)cc2OC)CCCN(C(=O)Oc2ccccc2[N+](=O)[O-])CCc2c3ccccc3[nH]c21",
        "CN1CCN(CCCNc2ncc3cc(-c4c(Cl)cccc4Cl)c(NC(=O)Nc4ccccc4)nc3n2)CC1",
        "CCCCCCCCCCCCCCCCCCNc1ccn(C2OC(COP(=O)(O)OCCCCCCCCCCCCCCCC)C(O)C2O)c(=O)n1",
        "CC(C)=C1C(=O)C[C@]2(C)C[C@H]3/C(=C(/C)CCC12)CC[C@@]3(C)O",
        "C=C1CC[C@](C)(O)[C@@]2(C)CC[C@H]3C(=C(C)C)C(=O)C[C@]3(C)C[C@H]12",
        "CC(=O)OC1C(O)COC(O[C@H]2CC[C@]34C[C@]35CC[C@]3(C)C([C@@]6(C)CC[C@@H](C(C)(C)O)O6)[C@@H](O)C[C@@]3(C)C5C[C@H](OC3OCC(O)C(O)C3O)[C@H]4C2(C)C)C1OC1OCC(O)C(O)C1O",
        "C=C(C)C(=O)O[C@H]1C/C(CO)=C/CCC(=C)C(OC(C)=O)[C@H]2OC(=O)C(=C)[C@@H]21",
        "COc1ccc(CCC(=O)c2c(O)cc(OC3OC(CO)C(O)C(O)C3OC3OC(C)C(O)C(O)C3O)cc2O)c(O)c1",
        "COc1nc2c(c(=O)[nH]1)N=C(c1ccc(Cl)cc1)CC(c1ccccc1)N2",
        "N#Cc1c(=O)n(NS(=O)(=O)c2ccccc2)c(-c2ccccc2)c(C#N)c1-c1ccc([N+](=O)[O-])cc1",
        "C[C@H](NC1=CC(=O)C(N[C@@H](C)[C@H](O)c2ccccc2)=CC1=O)[C@H](O)c1ccccc1",
        "CC1(C)OP(=O)(O)C=C1Sc1ccc([N+](=O)[O-])cc1[N+](=O)[O-]",
        "C/C(=N/NC(=S)N1CCCCCC1)c1cccc(/C(C)=N\\NC(=S)N2CCCCCC2)n1",
        "COP1(=O)OCC2OC(n3cnc4ncnc(N)c43)C(OS(=O)(=O)c3ccc(C)cc3)C2O1",
        "Cc1cn(C2C[C@@H](/[N+]([O-])=C\\c3ccccc3)C(CO[Si](C)(C)C(C)(C)C)O2)c(=O)[nH]c1=O",
        "Cc1cc(S(=O)(=O)Nc2nc(Nc3ccc(Cl)cc3)n[nH]2)c(S)cc1Cl",
        "Cc1ccc(-n2cc(-c3ccc(Cl)cc3)c3c2N=CN2N=C(c4cccs4)NC32)cc1",
        "O=C(CCN1C(=O)/C(=C\\c2cccc(C(F)(F)F)c2)SC1=S)OCC(F)(F)F",
        "CC1(C)SC2C(N)C(=O)C2C1C(=O)OCc1ccc([N+](=O)[O-])cc1",
        "CC[C@]1(OC(=O)CN)c2cc3n(c(=O)c2COC1=O)Cc1cc2c(N)cccc2nc1-3",
        "CC[C@]1(OC(=O)CN)c2cc3n(c(=O)c2COC1=O)Cc1cc2cc4c(cc2nc1-3)OCO4",
        "Cn1c(=O)oc2cc(C(=O)CN3CCN(Cc4ccc5c(c4)OCO5)CC3)c(Cl)cc21",
        "COc1cc2oc3cc(OC)c(OC)c(CC=C(C)C)c3c(=O)c2c2c1CCC(C)(C)O2",
        "Cc1ccc2c(c1)CC1(C2=O)[C@H](c2ccccc2)C2c3cc(C)ccc3C(=O)C2[C@H]1c1ccccc1",
        "CC1(C)OC2OC(C(O)CNCCN3CCCCC3)C(NC(=O)Nc3ccccc3)C2O1",
        "O=C(O)c1cc(N=Nc2cc(S(=O)(=O)O)c3cccnc3c2O)ccc1/C=C/c1ccc(N=Nc2cc(S(=O)(=O)O)c3cccnc3c2O)cc1C(=O)O",
        "CC1=C(C(=O)OCCN(Cc2ccccc2)c2ccccc2)C(c2cccc([N+](=O)[O-])c2)C(P2(=O)OCC(C)(C)CO2)=C(C)N1CCN1CCOCC1",
        "C[C@@]12C[C@@H](OC(=O)[C@]1(C)O)[C@@H]([C@]1(O)CC[C@H]3C4C[C@H]5O[C@]56CCCC(=O)[C@]6(C)C4CC[C@@]31C)C2",
        "Cc1cc(N=[N+]([O-])c2cc(C)[n+]([O-])c(C)c2)cc(C)[n+]1[O-]",
        "O=C(Nc1ccc(F)cc1F)c1cccnc1N1CCN(C(c2ccccc2)c2ccccc2)CC1",
        "O=C1O/C(=C\\c2ccc([N+](=O)[O-])cc2)C(c2ccc(Cl)cc2)=C1c1ncc(C(F)(F)F)cc1Cl",
        "CC(=O)OCC1OC(O[C@@H]2OC=C[C@H]3[C@H](OC(=O)/C=C/c4ccc(OC(C)=O)c(OC(C)=O)c4)[C@@H]4O[C@]4(COC(C)=O)[C@H]32)C(OC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "CC(=O)Nc1ccc2cc3c(nc2n1)c1cccnc1n3S(=O)(=O)c1ccccc1",
        "CNS(=O)(=O)c1ccc(NC(=O)c2ccccc2SSc2ccccc2C(=O)Nc2ccc(S(=O)(=O)NC)cc2)cc1",
        "CC(C)=C(C(=O)NCc1ccccc1)N1C(=O)C[C@H]1S(=O)NCc1ccccc1",
        "C#CC1(O)C(CO)OC(n2ccc(=O)[nH]c2=O)C1O[Si](C)(C)C(C)(C)C",
        "O=S(=O)(c1cccc(C(F)(F)F)c1)c1cnc(-c2ccccn2)nc1-c1ccccn1",
        "COc1ccc(C(=O)O[C@H]2C[C@H](n3cc(C)c(=O)[nH]c3=O)O[C@@H]2CN=[N+]=[N-])cc1",
        "O=S(=O)(O)c1cc2cc(S(=O)(=O)O)cc(S(=O)(=O)O)c2c(NC(=S)Nc2cc(S(=O)(=O)O)cc3cc(S(=O)(=O)O)cc(S(=O)(=O)O)c32)c1",
        "CC(=O)c1c(C)nc(SSc2nc(C)c(C(C)=O)c(-c3ccc(Cl)cc3)c2C#N)c(C#N)c1-c1ccc(Cl)cc1",
        "CC(=O)O[C@@]12CO[C@@H]1C[C@H](O)[C@@]1(C)C(=O)[C@H](O)C3=C(C)[C@@H](OC(=O)[C@H](O)[C@@H](NC(=O)[C@@H](NC(=O)OC(C)(C)C)C(C)(C)C)c4ccccc4)C[C@@](O)([C@@H](OC(=O)c4ccccc4)[C@H]21)C3(C)C",
        "CCc1c(C)c(C#N)c(=S)n(C2OC(COC(C)=O)C(OC(C)=O)C(OC(C)=O)C2OC(C)=O)c1C",
        "CC(C)CNC(=O)c1c(NC(=O)c2ccccc2)sc2[nH]c(=O)n(-c3ccccc3)c(=O)c21",
        "CCOC(=O)c1c[nH]c2c(cc(F)cc2N2C(=O)C(C)SC2c2ccccc2)c1=O",
        "COc1cc2c(cc1OC)c(-c1ccc3c(c1)OCO3)c1c(c2SCCCSSCCCSc2c3c(c(-c4ccc5c(c4)OCO5)c4cc(OC)c(OC)cc42)C(=O)OC3)COC1=O",
        "Cc1nc(O)nc(N/N=C/c2ccc(Cl)c(Cl)c2)c1C(=O)Nc1ccc(Cl)c(Cl)c1",
        "CC(=O)c1c(-c2ccco2)c(C#N)c(=S)n(C2OC(CO)C(O)C(O)C2O)c1-c1ccccc1",
        "Cc1ccc(S(=O)(=O)NC2C(C(O)CN3CCCCCC3)OC3OC(C)(C)OC32)cc1",
        "COc1cc2c(cc1OC)C(=O)C1(C2)[C@H](c2ccccc2)C2c3cc(OC)c(OC)cc3C(=O)C2[C@H]1c1ccccc1",
        "O=C(n1c(=O)n(-c2ccc(Cl)cc2)c(=O)n1C(=O)C(Cl)(Cl)Cl)C(Cl)(Cl)Cl",
        "CCOc1ccc(OCC)c(/N=C/c2c[nH]c3nc(N)nc(N)c23)c1.CC(=O)O",
        "CN(CCNC(=O)c1cc(-c2ccccc2)nc2ccccc21)CCNC(=O)c1cc(-c2ccccc2)nc2ccccc21",
        "COc1cc([C@@H]2c3cc4c(cc3[C@@H](OC3OC5COC(C)OC5C(OC=O)C3OC=O)[C@H]3COC(=O)[C@@H]32)OCO4)cc(OC(C)=O)c1O",
        "Cc1cc2oc3cc(C)c4c(c3c(=O)c2c(CCC(C)(C)Cl)c1C)OC(C)(C)CC4",
        "COc1ccc(C(OC(=O)[C@H](O[Si](C)(C)C(C)(C)C)[C@@H](NC(=O)c2ccccc2)c2ccccc2)c2ccc(OC)cc2)cc1",
        "COc1cc(/C=C2\\C(C)=NN(C(=O)/C=C/c3ccccc3)C2=O)cc(OC)c1OC",
        "C[C@@H]1CCC2[C@@H](C)[C@@H](OCCOCCO[C@H]3OC4O[C@@]5(C)CCC6[C@H](C)CCC([C@H]3C)[C@]64OO5)OC3O[C@@]4(C)CCC1[C@@]23OO4",
        "O[C@]1(c2c(F)c(F)c(F)c(F)c2F)C2C3CC4C5C3C1[C@](O)(C42)[C@H]5Br",
        "O=C1c2ccccc2C(=O)N1CCCCN1c2ccccc2Sc2ccc(C(F)(F)F)cc21",
        "Cc1cc(-c2ccc(C(=N)NC3CCCC3)cc2)oc1-c1ccc(C(=N)NC2CCCC2)cc1",
        "O=C1Nc2ccccc2C(=O)C2C(c3ccc(Cl)cc3)CC(O)(c3ccccc3)C12",
        "COc1cc(OC)c(OC)cc1/C=C/C(=O)/C=C/c1cc(OC)c(OC)cc1OC",
        "COc1ccc(N2C(=O)C3c4[nH]c5ccc(OCc6ccccc6)cc5c4C4CCC(c5ccccc5)CC4C3C2=O)cc1",
        "CC(C)[C@@H]1NC(=O)[C@H]2N=C(O[C@@H]2C)[C@@H]2CCCN2C(=O)[C@H](Cc2ccccc2)NC(=O)[C@@H]2CSC(=N2)[C@@H](Cc2ccccc2)NC(=O)[C@@H]2CSC1=N2",
        "COc1cccc(CCn2c(N)c(-c3ccccc3)c3ccc([N+](=O)[O-])cc3c2=O)c1",
        "CCOC(=O)C/C(=N\\c1cccc([N+](=O)[O-])c1)Nc1cccc([N+](=O)[O-])c1",
        "CC(C)(C)c1ccc(C(=O)Nc2ccc(S(=O)(=O)O)c3cc(S(=O)(=O)O)cc(S(=O)(=O)O)c23)cc1N",
        "Cc1ccc2cc(NC(=O)CN)c3ccc(C)[n+]4c3c2[n+]1[Cu+]41[n+]2c3c4c(ccc(C)[n+]41)c(NC(=O)CN)cc3ccc2C",
        "C[C@H]1[C@@]2(O)[C@H](C=C3[C@@H]4CC[C@H]5Cc6nc7c(nc6C[C@]5(C)[C@H]4C[C@@H](O)[C@@]32C)C[C@@H]2CCC3C4=CC[C@@H]5[C@@H](C)C(O)(C(O)CC(C)(C)O)OC[C@]45C(=O)C[C@]3(O)[C@@]2(C)C7)O[C@]12O[C@](C)(CO)C[C@H]2O",
        "CC(=O)Oc1ccc2c(c1)[C@@]1(C)CCC[C@](C)(C(=O)O)[C@@H]1CC2",
        "CC1=CCC[C@H]2[C@@]1(C)CC[C@@H](C)[C@]2(C)Cc1cc(NC2=CC(=O)C(C[C@]3(C)[C@@H](C)CC[C@]4(C)C(C)=CCC[C@@H]34)=CC2=O)ccc1O",
        "CCCCCCCCCCCCCCCC(=O)OC/C=C(C)/C=C/C=C(C)\\C=C\\C1=C(C)CCCC1(C)C",
        "CC(CCc1nc2ccccc2o1)C1CCC2C3CC[C@@H]4C[C@H](O)CC[C@]4(C)C3C[C@H](O)[C@]12C",
        "CCCCCC(=O)Nc1cc[n+](CC(=O)Nc2ccc(NNC(=O)C(=O)NC)c(C)c2)cc1",
        "O=C(O)COc1c2c(Br)c(c(OCC(=O)O)c1OCC(=O)O)Cc1c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)Cc1c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)Cc1c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)C2",
        "C[C@@]1(O)C2/C(=N/O)C3CC1(c1ccccc1)CC(/C3=N/O)C2c1ccccc1",
        "COc1c2c(nc3ccccc31)-c1cc(C(CC(=O)OC(C)(C)C)C(=O)OC(C)C)c(CO)c(=O)n1C2",
        "CC(C)=CCc1c2c(c(O)c3c1O[C@]14C(=C[C@@H]5C[C@H]1C(C)(C)O[C@@]4(C/C=C(/C)C=O)C5=O)C3=O)C=CC(C)(C)O2",
        "CCOC(=O)C(=O)N(C(=O)C(=O)OCC)c1nc(-c2ccc(O)c(OC)c2)cs1",
        "O=C(N/N=C/c1ccc(Cl)cc1Cl)C1CCN(c2ncc(C(F)(F)F)cc2Cl)CC1",
        "CNC(=O)OCc1nc(SSC(C)(C)C)n(-c2ccc(OC)cc2)c1COC(=O)NC",
        "CCOP(=O)(OCC)/C(C#N)=C/c1ccc(-c2cc(C(F)(F)F)cc(C(F)(F)F)c2)o1",
        "O=C(COc1c2cc([N+](=O)[O-])cc1Cc1cc([N+](=O)[O-])cc(c1OCC(=O)Nc1cc(S(=O)(=O)O)cc3cc(S(=O)(=O)O)cc(O)c31)Cc1cc([N+](=O)[O-])cc(c1OCC(=O)Nc1cc(S(=O)(=O)O)cc3cc(S(=O)(=O)O)cc(O)c31)Cc1cc([N+](=O)[O-])cc(c1OCC(=O)Nc1cc(S(=O)(=O)O)cc3cc(S(=O)(=O)O)cc(O)c31)C2)Nc1cc(S(=O)(=O)O)cc2cc(S(=O)(=O)O)cc(O)c21",
        "CCOC(=O)/C=C/[C@H](O[Si](c1ccccc1)(c1ccccc1)c1ccccc1)C(C)(C)C",
        "Nc1nc(N)c(N=Nc2ccc(S(N)(=O)=O)cc2)c(Nc2cccc3cccc(S(=O)(=O)O)c32)n1",
        "CO[C@H]1O[C@@]2(C)CC[C@H]3CCC[C@@H](CCOC(=O)c4ccc(C(=O)OCC[C@@H]5CCC[C@@H]6CC[C@@]7(C)OO[C@]65[C@@H](OC)O7)cc4)[C@]31OO2",
        "COc1ccc(NC(=O)CC(=O)N2N=C(C)C(N=Nc3ccc(C(=O)O)cc3)C2=O)cc1",
        "CS(=O)(=O)c1ccc(NC(=O)c2ccccc2SSc2ccccc2C(=O)Nc2ccc(S(C)(=O)=O)cc2)cc1",
        "Cc1nn(-c2ccccc2)cc1C(=O)N1CCN(c2ncc(C(F)(F)F)cc2Cl)CC1",
        "CC[C@]1(O)c2cc3n(c(=O)c2COC1=O)Cc1c-3nc2ccccc2c1/C=N/NC(=O)C(N)CCSC",
        "CCCCCCCCCCCCCCCC(=O)Nc1ccn(C2CC(OP(=O)(O)OCC3OC(n4cc(C)c(=O)[nH]c4=O)CC3N=[N+]=[N-])C(CO)O2)c(=O)n1",
        "O=C(OC[C@H]1O[C@@H](n2cc(SCc3ccccc3)c(=S)[nH]c2=O)C[C@@H]1OC(=O)c1ccc(Cl)cc1)c1ccc(Cl)cc1",
        "COC(=O)c1ccccc1NC(=O)Nc1ccc2cc(S(=O)(=O)O)cc(O)c2c1",
        "CC(O)[C@H](CN(Cc1ccccc1)[C@@H](CCCCNCc1ccccc1)CNCc1ccccc1)N(Cc1ccccc1)C[C@H](CCCCNCc1ccccc1)N(CCCCN)Cc1ccccc1",
        "CSc1nc2c(c(=O)[nH]1)N=C(c1ccc([N+](=O)[O-])cc1)CC(c1ccccc1)N2",
        "CCCCCCCCCCCCCCCCCCCCOC(=O)[C@H](C[C@H](OC(C)=O)[C@H](OC(C)=O)[C@H](C)OC(C)=O)c1coc(Cc2cnco2)n1",
        "CC(=O)OCC1OC(n2c3c(c(-c4ccco4)c(C#N)c2=S)CCCC3)C(OC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "O=C1NC(=O)[C@@H](OCC2CCCCC2)[C@@H]2OCC3OC(C(O)C3O)C12",
        "Cc1c[n+]2c3c4c(ccc3c1C)c(C)c(C)c[n+]4[Cu+]21[n+]2cc(C)c(C)c3ccc4c(c32)[n+]1cc(C)c4C",
        "O=C(NCCC(F)(F)C(F)(F)C(F)(F)C(F)(F)F)NC1OC(CO)C(O)C(O)C1O",
        "CC(=O)OCC1OC(n2c(-c3ccccc3)c(C(C)=O)c(-c3ccc(Cl)cc3)c(C#N)c2=S)C(OC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "CCN(CC)CC(=O)Nc1nc2c(cc(-c3cc4sc(NC(=O)CN(CC)CC)nc4c(C)c3)cc2C)s1",
        "CNC(=O)C(=O)NNc1cc(OC)c(NC(=O)Cn2cc[n+](CC(=O)Nc3cc(C)c(NNC(=O)C(=O)NC)cc3OC)c2C)cc1C",
        "O=C(NCCNCCNC(=O)c1cc(-c2ccccc2)nc2ccccc21)c1cc(-c2ccccc2)nc2ccccc21",
        "COC(=O)C(Cc1c[nH]c2ccccc12)NP(=O)(O)OCC1OC(n2ccc(N)nc2=O)C(O)C1O",
        "CCN(CC)c1nc(OC)nc(C(Cl)([N+](=O)[O-])[N+](=O)[O-])n1",
        "Cc1ccc(S(=O)(=O)NC2C(C(O)CNCCN3CCCCC3)OC3OC(C)(C)OC32)cc1",
        "C[C@H]1CC[C@H]2C(CN3CCC(O)(c4ccccc4)CC3)C(=O)O[C@H]2[C@]2(C)C(=O)CC(N3CCC(O)(c4ccccc4)CC3)[C@@H]12",
        "O=C(NCCCN1CCN(CCCNC(=O)c2cc(-c3ccccc3)nc3ccccc32)CC1)c1cc(-c2ccccc2)nc2ccccc21",
        "OCCN1CN(CCO)CN(c2nc(N3CN(CCO)CN(CCO)C3)nc(N3CN(CCO)CN(CCO)C3)n2)C1",
        "COc1cccc2c1C(=O)c1c(O)c3c(c(O)c1C2=O)CC(O)(C(=O)CO)CC3OC1CC(N)C(O)C(C)O1.Cl[Al](Cl)(Cl)Cl",
        "C(CCCCCCNC1C2CC3CC(C2)CC1C3)CCCCCNC1C2CC3CC(C2)CC1C3",
        "CC(C)C[C@H](NC(=O)[C@H](Cc1ccccc1)NC(=O)c1cnccn1)B(O)O",
        "N#Cc1cc2[nH]c3ccccc3c2n(-c2ccc([N+](=O)[O-])cc2)c1=N",
        "CCC(C)C(N)C(=O)NC(CCCCN)C(=O)NC(C(=O)NC(C(=O)NC(C(=O)NC(CCSC)C(=O)NC(CC(C)C)C(=O)NC(C)C(=O)NC(CCCCN)C(=O)NC(CC(C)C)C(=O)NCC(=O)NC(CCCCN)C(=O)NC(C(=O)NC(CC(C)C)C(=O)NC(C)C(=O)NC(Cc1c[nH]cn1)C(=O)NC(C(=O)O)C(C)C)C(C)C)C(C)O)C(C)O)C(C)CC",
        "O=c1cc(N=P(c2ccccc2)(c2ccccc2)c2ccccc2)n(-c2ccccc2)c(=O)n1-c1ccccc1",
        "N#CC(c1ccccc1)N(CCCCCCN(C(=O)c1ccccc1)C(C#N)c1ccccc1)C(=O)c1ccccc1",
        "O=C(OCCN(CCNc1ncc(C(F)(F)F)cc1Cl)c1ncc(C(F)(F)F)cc1Cl)c1ccc([N+](=O)[O-])cc1",
        "C=C(CC)N(CC)c1cccc(-c2ccc3c(n2)n(C)c(=O)n(C)c3=O)c1",
        "CC(NC(=O)CNC(=O)C(N)CO)C(=O)NC(CCC(=O)O)C(=O)NC(CCCCN)C(=O)NC(CO)C(=O)NC(CCCCN)C(=O)NC(CCCCN)C(=O)NC(CCCCN)C(=O)NCC(=O)NC(CO)C(=O)NC(CC(N)=O)C(=O)NC(C)C(=O)NC(Cc1c[nH]cn1)C(=O)NC(CO)C(=O)O",
        "COc1c(C)c(O)c2c(c1O)C1C3CC4=C(C(=O)C(OC)=C(C)C4=O)[C@H](CNC(=O)[C@H](C)N)N3C(C#N)C([C@H]2OC)N1C",
        "CC(C)NCC(O)c1ccc([N+](=O)[O-])c(S(=O)(=O)c2ccccc2)c1",
        "CC(C)C1(C)N=C2C3CCC(C)CC3C(=O)N2C1=O.CC(C)C1(C)N=C2C3CC(C)CCC3C(=O)N2C1=O",
        "COc1cc2c(cc1OC)c(-c1ccc3c(c1)OCO3)c1c(c2OCOc2c3c(c(-c4ccc5c(c4)OCO5)c4cc(OC)c(OC)cc42)C(=O)OC3)COC1=O",
        "CCCCCCCCCCCCOC1OC(C(O)COS(=O)(=O)c2ccc(C)cc2)C2OC(C)(C)OC12",
        "O=C(NC(/C=N/Nc1ccc(Cl)cc1)C(O)C(O)C(O)CO)c1ccc([N+](=O)[O-])cc1",
        "COc1cc2c(cc1OC)c(-c1ccc3c(c1)OCO3)c1c(c2OCCCN2C(=O)c3ccccc3C2=O)COC1=O",
        "CN(C)CCCNc1ccnc2c(COC(=O)N(CCCl)CCCl)ccc([N+](=O)[O-])c12",
        "CC(C)(C)c1ccc(OCOCCN2CCC(C(O)(c3ccccc3)c3ccccc3)CC2)cc1",
        "CC(=O)c1sc2c(c1OC(=O)c1ccco1)c(=O)n(-c1ccccc1)c(=S)n2-c1ccccc1",
        "Brc1ccc(C2CC3(c4ccccc4)ON=C(c4ccccc4)N3c3ccccc3S2)cc1",
        "COc1nc(=[N+]2CCOCC2)nc([C-]([N+](=O)[O-])[N+](=O)[O-])[nH]1",
        "O=C(O)CCC/C=C(\\c1ccc(CNS(=O)(=O)c2ccc(Cl)cc2)cc1)c1cccnc1",
        "CC(=O)O[C@@H]1C2=C(C)[C@@H](OC(=O)[C@H](O)[C@@H](NC(=O)c3ccco3)c3ccccc3)C[C@@](O)([C@@H](OC(=O)c3ccccc3)[C@@H]3[C@]4(OC(C)=O)CO[C@@H]4C[C@H](O)[C@@]3(C)C1=O)C2(C)C",
        "N#Cc1c(N)n(-c2ccccc2)c(-c2cccc([N+](=O)[O-])c2)c1C#N",
        "CC(/C=C/C1CCCCC1(C)C)=C\\C=C\\C(C)=C\\C1c2cc(c(O)c(O)c2O)C(/C=C(C)/C=C/C=C(C)/C=C/C2CCCCC2(C)C)c2cc(c(O)c(O)c2O)C(/C=C(C)/C=C/C=C(C)/C=C/C2CCCCC2(C)C)c2cc(c(O)c(O)c2O)C(/C=C(C)/C=C/C=C(C)/C=C/C2CCCCC2(C)C)c2cc1c(O)c(O)c2O",
        "COc1cc([C@@H]2c3cc4c(cc3[C@@H](OC3OC5COC(C)OC5C(O)C3O)[C@H]3COC(=O)[C@@H]32)OCO4)cc(O)c1OC",
        "C/C(O)=C/C(=O)c1c(C)nn(-c2nc(-c3ccc(Cl)cc3Cl)cs2)c1O",
        "CC1=C2C(=O)C=C(CO)[C@@H]2[C@H]2OC(=O)[C@@H](C)[C@@H]2[C@@H](OC(=O)Cc2ccc(O)cc2)C1",
        "O=C(O)COc1c2cc(c(OCC(=O)O)c1OCC(=O)O)C(C=P(c1ccccc1)(c1ccccc1)c1ccccc1)c1cc(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)C(C=P(c1ccccc1)(c1ccccc1)c1ccccc1)c1cc(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)C(C=P(c1ccccc1)(c1ccccc1)c1ccccc1)c1cc(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)C2C=P(c1ccccc1)(c1ccccc1)c1ccccc1",
        "CC(C)CC1NC(=O)C2CCCN2C(=O)C(C(C)O)NC(=O)C(CC(C)C)NC(=O)C2CCCN2C(=O)C(C(C)C)NC(=O)C(Cc2c[nH]c3ccccc23)NC1=O",
        "CCCN(CCC)c1c([N+](=O)[O-])cc(S(=O)(=O)c2cc([N+](=O)[O-])c(N(CCC)CCC)c([N+](=O)[O-])c2)cc1[N+](=O)[O-]",
        "Cc1ccc(N=N/C(=N/NC(=O)c2ccccc2)c2ccc(N(CCC#N)CCC#N)cc2C)cc1",
        "CC1=NN(C(=O)CC(=O)Nc2ccccc2Cl)C(=O)C1N=Nc1ccc(C(=O)O)cc1",
        "CC1=NN(C(=O)CC(=O)Nc2cccc(Cl)c2)C(=O)C1N=Nc1ccc(C(=O)O)cc1",
        "CCCCCCCCCCOCCC(=O)OCC1OC(n2cc(C)c(=O)[nH]c2=O)CC1N=[N+]=[N-]",
        "COc1ccc(C(=O)/C=C/c2ccc(OCC(=O)N3CCN(c4ccccc4)CC3)cc2)cc1OC",
        "CCCC1c2c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O)C(CCC)c2c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O)C(CCC)c2c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O)C(CCC)c2c(Br)c1c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O",
        "CC(C)NCC(O)c1cc(OCc2ccccc2)c(OCc2ccccc2)cc1NC(=O)CCC(=O)O",
        "C=C(C)[C@@]1(O)O[C@H]2C[C@@]3(C)OC(=CC3=O)/C(C)=C\\[C@@H]3OC(=O)[C@@]1(C)[C@H]32",
        "COC(=O)C(CC(C)C)NC(=O)C(C)NC(=O)C(Cc1ccc(OCc2ccccc2)cc1)NC(=O)C(COCc1ccccc1)NC(=O)OC(C)(C)C",
        "CC1(C)CNC[C@](C)(OCCCO[C@@]2(C)CNCC(C)(C)NC2=O)C(=O)N1",
        "COC(=O)C1OC(O[C@H]2CC[C@@]3(C)[C@@H](CC[C@]4(C)[C@@H]3CC=C3[C@@H]5CC(C)(C)CC[C@]5(C(=O)OC5OCC(O)C(O)C5OC5OC(C)C(OC6OCC(O)C(O)C6O)C(OC6OCC(O)(CO)C6O)C5O)[C@H](O)C[C@]34C)C2(C)C)C(O)C(O)C1O",
        "COc1cccc(NC(=O)CC(=O)N2N=C(C)C(N=Nc3ccc(S(=O)(=O)CCOS(=O)(=O)O)cc3)C2=O)c1",
        "O=C(O)COc1c(OCC(=O)O)c2c(Br)c(c1OCC(=O)O)C(c1cccc(Br)c1)c1c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)C(c1cccc(Br)c1)c1c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)C(c1cccc(Br)c1)c1c(Br)c(c(OCC(=O)O)c(OCC(=O)O)c1OCC(=O)O)C2c1cccc(Br)c1",
        "CC(C)N(C(=O)c1c(-c2ccccc2)noc1C(c1c(F)c(F)c(F)c(F)c1F)c1c(F)c(F)c(F)c(F)c1F)C(C)C",
        "CCCCOC(OCCCC)c1cc[n+](CC(=O)Nc2ccc(NNC(=O)C(=O)NC)c(OC)c2)cc1",
        "O=c1c(-c2ccnn2-c2ncc(C(F)(F)F)cc2Cl)cn(Cc2ccccc2)c(=O)n1Cc1ccc(F)cc1",
        "CC(=O)c1cn(N(C)c2ncc(C(F)(F)F)cc2Cl)c(=O)n(Cc2ccc(F)cc2)c1=O",
        "N=C(N)NC(=N)Nc1cccc2ccc(O)c(N=Nc3ccccc3S(=O)(=O)O)c21",
        "CCCCCCCC(CC(=O)O)OC(=O)CC(CCCCCCC)OC1OC(C)C(O)C(O)C1O",
        "O=S(=O)(N1CCCCN(S(=O)(=O)C(F)(F)F)CCCN(S(=O)(=O)C(F)(F)F)CCCCN(S(=O)(=O)C(F)(F)F)CCCN(S(=O)(=O)C(F)(F)F)CCCCN(S(=O)(=O)C(F)(F)F)CCCN(S(=O)(=O)C(F)(F)F)CCCCN(S(=O)(=O)C(F)(F)F)CCC1)C(F)(F)F",
        "C=C1CN(S(=O)(=O)c2ccc(C)cc2)CCC[N+]([O-])(Cc2ccccc2)CCCN(S(=O)(=O)c2ccc(C)cc2)C1",
        "N#C/C(=C\\c1ccc(O)c(O)c1)C(=O)NCCCCCCNC(=O)/C(C#N)=C/c1ccc(O)c(O)c1",
        "O=c1[nH]nc(Cc2ccc(Cl)cc2)n1/N=C/CC/C=N\\n1c(=O)[nH]nc1Cc1ccc(Cl)cc1",
        "CC(C)(NC(=O)OCc1ccccc1)C(=O)NCC1OC(n2cnc3c2ncnc3N)C(O)C1O",
        "COc1cc([C@@H]2c3cc4c(cc3[C@@H](NC(=O)C(O)C(NC(=O)c3ccccc3)c3ccccc3)[C@H]3COC(=O)[C@@H]32)OCO4)cc(OC)c1OC",
        "CC(C)C[C@H](NC(=O)[C@H](Cc1ccccc1)NC(=O)c1ccccc1)B(O)O",
        "C=CCNC1=C2C[C@@H](C)C[C@@H](OC)[C@H](OC(=O)CCN(C)C)[C@H](C)/C=C(\\C)[C@@H](OC(N)=O)[C@H](OC)/C=C\\C=C(/C)C(=O)NC(=CC1=O)C2=O",
        "O=C1C2=C(CC3=C(CS(=O)(=O)C3)C2)C(=O)C2=C1CC1=C(CS(=O)(=O)C1)C2",
        "COc1cc(C2C3=C(N=C4SC(C(C)=O)=C(C)N42)c2ccccc2CC3)cc(OC)c1OC",
        "COc1cc([C@@H]2c3cc4c(cc3[C@@H](OC(=O)C(O)C(NC(=O)c3ccccc3)c3ccccc3)[C@H]3COC(=O)[C@@H]32)OCO4)cc(OC)c1OC",
        "Cc1ccc(C(=O)Nc2ccc(CP(=O)(O)O)cc2)cc1NC(=O)c1cccc(NC(=O)Nc2cccc(C(=O)Nc3cc(C(=O)Nc4ccc(CP(=O)(O)O)cc4)ccc3C)c2)c1",
        "Nc1nc2[nH]cc(CNc3ccc(C(=O)NC(CCC(=O)O)C(=O)O)cc3)c2c(N)n1",
        "c1ccc(C(OOC(c2ccccc2)(c2ccccc2)c2ccc3ccc4ccccc4c3c2)(c2ccccc2)c2ccc3ccc4ccccc4c3c2)cc1",
        "CC1(C)Oc2[nH]c(=O)n(-c3ccc(Cl)cc3)c(=O)c2C1c1ccccc1",
        "COc1ccc(C(=O)/N=C(\\NC(=O)c2ccccc2)Nc2cc(-c3ccc(Cl)cc3)nn2C)cc1",
        "CCC12CN3C[C@H](C[C@](C(=O)OC)(c4cc5c(c([N+](=O)[O-])c4OC)N(C)[C@@H]4[C@]56CCN5CC=CC(CC)(C56)[C@@H](OC(C)=O)[C@]4(O)C(=O)OC)c4[nH]c5cc([N+](=O)[O-])ccc5c4C3)C1O2",
        "CCSc1[nH]c(Nc2nc(-c3ccccc3)n(-c3ccccc3)n2)c(C(N)=O)c1C(N)=O",
        "CC1Oc2c3c(c4c(-c5ccccc5)cc(=O)oc4c2[C@@H](O)[C@H]1C)OC(C)(C)CC3",
        "CC(=O)N(C(C)=O)c1ncc(-n2nc(-c3ccccc3)cc2-c2ccccc2)c(-c2ccccc2)n1",
        "C1=CN2c3c4c(ccc3=C1)=CC=CN4[Cu+2]21N2C=CC=c3ccc4c(c32)N1C=CC=4",
        "O=C(O)COc1c2cc([N+](=O)[O-])cc1Cc1cc([N+](=O)[O-])cc(c1OCC(=O)O)Cc1cc([N+](=O)[O-])cc(c1OCC(=O)O)Cc1cc([N+](=O)[O-])cc(c1OCC(=O)O)C2",
        "CNC(=O)C(=O)NNc1ccc(NC(=O)C[n+]2ccc(NC(C)=O)cc2)cc1C",
        "O=C(CCCN1C(=O)/C(=C\\c2ccc(F)cc2)SC1=S)ON1C(=O)S/C(=C\\c2ccccc2)C1=O",
        "Cc1cc(C(c2c(F)c(F)c(F)c(F)c2F)c2c(F)c(F)c(F)c(F)c2F)on1",
        "CCCCCCCCCCCCCCCCCC(=O)OC[C@H](COP(=O)(O)OCCNC(=O)CCC(=O)OCCC=C(c1cc(Cl)c(OC)c(C(=O)OC)c1)c1cc(Cl)c(OC)c(C(=O)OC)c1)OC(=O)CCCCCCCCCCCCCCCCC",
        "CC(C)(C)OC(=O)CCC(NC(=O)OCC1c2ccccc2-c2ccccc21)C(=O)NC(Cc1c[nH]c2ccccc12)C(=O)NC(CCCCNC(=O)OC(C)(C)C)C(=O)N1CCCC1C(=O)NCC(N)=O",
        "CC/C(=C\\c1cc(C(C)(C)C)c(O)c(C(C)(C)C)c1)C(=O)CP(=O)(OC)OC",
        "CNC(=O)CCC(NC(=O)OC(C)(C)C)C(=O)NC(Cc1ccc(NC(=O)CCCCC(C)=O)cc1)C(=O)NCC(=O)NC",
        "CC(C)(C)OC(=O)N(CCNS(=O)(=O)c1cccc2cnccc21)CCC(=O)ON1C(=O)CCC1=O",
        "CCOC(=O)C(O)(NC(=O)NC(O)(C(=O)OCC)C(F)(F)F)C(F)(F)F",
        "N#Cc1cn(-c2ccc(Oc3ccc(C(F)(F)F)cn3)c(Cl)c2)c(=O)[nH]c1=O",
        "CSCCC1c2cc(c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O)C(CCSC)c2cc(c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O)C(CCSC)c2cc(c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O)C(CCSC)c2cc1c(OCC(=O)O)c(OCC(=O)O)c2OCC(=O)O",
        "O=C1NCN(c2ccccc2)C12CCN(CCCN1CCC3(CC1)C(=O)NCN3c1ccccc1)CC2",
        "C[C@@H](NC(=O)O[C@H]1CN(C)CCC1c1c(O)cc(O)c2c(=O)cc(-c3ccccc3)oc21)c1ccccc1",
        "COc1cc(C[C@H]2C(=O)OC[C@@H]2Cc2ccc(OC)c(OC)c2)cc(OC)c1",
        "CCCCCCCCCCCCCCCCS(=O)(=O)c1ccc(S(=O)(=O)N/N=c2/sc3ccc(S(=O)(=O)N4CCCCC4)cc3n2C)cc1NC(C)=O",
        "CC(C)=CCC/C(C)=C/C=C(\\C=C(/C)CCC=C(C)C)S(=O)(=O)c1ccccc1",
        "Cc1ccc(S(=O)(=O)Nc2ccc(CC(=O)NC(CC(C)C)C(=O)NC(CC(=O)O)C(=O)NC(C(=O)N3CCCC3C(=O)O)C(C)C)cc2)cc1",
        "CCCOC(=O)c1cc(C(=CCC[C@H]2CC[C@@]3(C)[C@@H](CC[C@H]4[C@@H]5CC[C@H](C(C)CCCC(C)C)[C@@]5(C)CC[C@@H]43)C2)c2cc(Cl)c(O)c(C(=O)OCCC)c2)cc(Cl)c1O",
        "COc1ccc(C2S/C(=C\\c3cc(OC)c(OC)c(OC)c3)C(=O)N2NC(=O)Cc2ccc([N+](=O)[O-])cc2)cc1",
        "CC[C@H]1C2OC(=O)[C@H](C)C2[C@@]2(O)C[C@@H]([C@@H]3C[C@H](C)C(=O)O3)N3CCCC[C@@]12C3=O",
        "COC(=O)c1cc2cc3c(cc2c(-c2cc(OC)c(OC)c(OC)c2)c1C(=O)OC)OCCO3",
        "O=C(O)CN1CCN(CC(=O)O)CC(=O)NCCCCNC(=O)CN(CC(=O)O)CC1",
        "COc1cc([C@@H]2c3cc4c(cc3[C@@H](Nc3ccc(NC(=O)c5cc([N+](=O)[O-])cn5C)cc3)[C@H]3COC(=O)[C@@H]32)OCO4)cc(OC)c1O",
        "O=C(N[C@@H](Cc1ccccc1)[C@@H](O)[C@@H](O)[C@H](Cc1ccccc1)NC(=O)O[C@H]1CCOC1)O[C@H]1CCOC1",
        "CC(C)CC(NC(=O)C(N)Cc1ccc(O)cc1)C(=O)NC(CCC(=O)O)C(=O)N1CCCC1C(=O)NCC(=O)N1CCCC1C(=O)NC(C(=O)NC(C(=O)NC(C(=O)O)C(C)C)C(C)O)C(C)C",
        "CC(=O)O[C@@]12CO[C@@H]1C[C@H](OC(=O)OCC(Cl)(Cl)Cl)[C@@]1(C)C(=O)[C@H](OC(=O)OCC(Cl)(Cl)Cl)C3=C(C)[C@@H](OC(=O)[C@H](O)[C@@H](NC(=O)[C@H](C)N)c4ccccc4)C[C@@](O)([C@@H](OC(=O)c4ccccc4)[C@H]21)C3(C)C",
        "O=c1c2ccccc2nc(N/N=C/c2ccc([N+](=O)[O-])cc2)n1-c1ccccc1",
        "Cn1c(=O)n(-c2cc(S(=O)(=O)n3cccn3)c(F)cc2F)c(=O)cc1C(F)(F)F",
        "CCCCCCCC1C2C(=O)N(c3ccc(OC)cc3)C(=O)C2c2[nH]c3ccccc3c2C1C",
        "CC[C@H](C)[C@@H]([C@@H](CC(=O)N1CCC[C@H]1[C@H](OC)[C@@H](C)C(=O)N1CCC[C@H]1C(=O)Nc1nc2ccccc2s1)OC)N(C)C(=O)[C@@H](NC(=O)[C@H](C(C)C)N(C)C)C(C)C",
        "CCCCOCC(=O)C1CC[C@H]2[C@@H]3CC[C@H](O)[C@@]3(C)CC[C@@H]2[C@H]1C(=O)O",
        "COc1ccc(N2C(=O)C3c4[nH]c5ccc(Br)cc5c4C4CCC(C(C)(C)C)CC4C3C2=O)cc1",
        "C[C@@H](CC(O)C(O)C(C)(C)O)C1CCC23CC12CCC1[C@@]3(C)C(OC2OC(CO)C(O)C(O)C2O)CC2C(C)(C)C(O)CC[C@@]21C",
        "COc1ccc(N2C(=O)C3c4[nH]c5ccc(OCc6ccccc6)cc5c4C4CCC(C(C)(C)C)CC4C3C2=O)cc1",
        "CC(C)(C)OC(=O)NCCCCC(NC(=O)C(Cc1c[nH]c2ccccc12)NC(=O)OCC1c2ccccc2-c2ccccc21)C(=O)N1CCCC1C(=O)NCC(N)=O",
        "CC(C)(C)OC(=O)NC(Cc1c[nH]c2ccccc12)C(=O)OCC[Se]c1ccccc1",
        "CCOP(=O)(OCC)/C(=C/c1cc(C(C)(C)C)c(O)c(C(C)(C)C)c1)C(=O)OCC(C)(C)C",
        "C=C1C(=O)O[C@@H]2[C@H]3O[C@]3(C)CC/C=C(\\C)[C@H](OC(C)=O)CC12",
        "Clc1ccc(-c2c3ccc(n3)c(-c3ccc(Cl)cc3)c3ccc([nH]3)c(-c3ccc(Cl)cc3)c3ccc(n3)c(-c3ccc(Cl)cc3)c3ccc2[nH]3)cc1",
        "CC(=O)OC(C(=O)O)C(OC(C)=O)C(=O)NC12CC3CC(CC(C3)C1)C2",
        "CC(NC(=O)C(Cc1ccc(OC(=O)c2ccccc2)cc1)NC(=O)OC(C)(C)C)C(=O)NCC(=O)NC(Cc1ccccc1)C(=O)NN",
        "O=C(CN1CCN(c2ccccc2)CC1)Nc1nc2cc3nc(NC(=O)CN4CCN(c5ccccc5)CC4)sc3cc2s1",
        "O=C(N/C(=C/c1ccc(Cl)cc1)c1nc2ccccc2c(=O)n1Nc1ccc([N+](=O)[O-])cc1[N+](=O)[O-])c1ccccc1",
        "CCCCOC(=O)NC(Nc1ccc(S(=O)(=O)Nc2ncccn2)cc1)(C(F)(F)F)C(F)(F)F",
        "CC(c1ccccc1F)=[N+]1[N-]C(N)=S[Pt+2]12S=C(N)[N-][N+]2=C(C)c1ccccc1F",
        "CC(=O)OCC1OC(O[C@@H]2C[C@H]3C(C)(C)[C@H](OC(C)=O)CC[C@]3(C)[C@H]3CC[C@@]45C[C@H](O)C[C@H](C)[C@@H]4CC=C5[C@]23C)C(O)C(O)C1O",
        "COCn1c(=O)n(C)c(=O)c2c1nc1c(c(=O)n(C)c(=O)n1COC)[n+]2[O-]",
        "CC(=O)OCC1OC(n2c3c(c(-c4ccc(C)cc4)c(C#N)c2=S)C(=O)CC(C)(C)C3)C(OC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "CCCCC1CC(=O)c2c(O)cc(O)c([C@@H]3CCN(C)C[C@@H]3O)c2O1",
        "CN1CCC(c2c(O)cc(O)c3c(=O)cc(-c4ccccc4F)oc32)[C@H](O)C1",
        "CC(=O)OC(CCOP(=S)(Cl)OCC1OC(n2cc(C)c(=O)[nH]c2=O)CC1N=[N+]=[N-])OC(C)=O",
        "C[C@]12CCC3C(CC=C4[C@@H](O)C=CC(=O)[C@@]43C)[C@]1(O)CC[C@]2(O)[C@@H]1C[C@@]2(C)C(=O)O[C@@H]1C[C@]2(C)O",
        "CC(C)[C@H](NC(=O)[C@H](C(C)C)N(C)C)C(=O)N(C)[C@H](C(=O)N1CCC[C@H]1C(=O)N1CCC[C@H]1C(=O)O[C@H](C(=O)Nc1cnc2ccccc2c1)C(C)C)C(C)C",
        "Cc1cc2c(c(C(=O)NCCC(=O)N[C@@H](C)C(=O)OCc3ccccc3)c1)Oc1c(cc(C)cc1C(=O)NCCC(=O)N[C@@H](C)C(=O)OCc1ccccc1)S2",
        "COC(=O)CCS[C@@H]1CC(=O)O[C@@H](C)CCC/C=C/[C@@H]2C[C@H](O)C[C@H]2[C@@H]1O",
        "CCC(=O)O[C@H]1CC[C@H]2[C@@H]3CCC4=C/C(=N/NC(=O)c5cccc6c(=O)c7ccccc7[nH]c56)CC[C@]4(C)[C@H]3CC[C@]12C",
        "COc1ccc(C2S/C(=C\\C=C\\c3ccccc3)C(=O)N2NC(=O)Cc2ccc([N+](=O)[O-])cc2)cc1",
        "COc1ccc(C(OCC2OC(n3c(=O)cnc4c3nc(N)n(C)c4=O)CC2OP(OCCC#N)N(C(C)C)C(C)C)(c2ccccc2)c2ccc(OC)cc2)cc1",
        "COc1cc(C(=O)/C(=C/c2c[nH]c3cc(C)ccc23)CCC(O)CO)cc(OC)c1OC",
        "CC1(C)CC(=O)C2=C(C1)OC1=C(CC(C)(C)CC1=O)C2c1ccccc1[N+](=O)[O-]",
        "CO[C@]1(c2ccc([N+](=O)[O-])cc2)Oc2ccccc2C(=O)[C@@]1(Nc1ccc(C=O)cc1)OC",
        "CC(C)(C)c1cc(C(c2ccc(-c3c4ccc(n4)c(-c4ccc(C(c5cc(C(C)(C)C)c(O)c(C(C)(C)C)c5)=C5C=C(C(C)(C)C)C(=O)C(C(C)(C)C)=C5)cc4)c4ccc([nH]4)c(-c4ccc(C(c5cc(C(C)(C)C)c(O)c(C(C)(C)C)c5)=C5C=C(C(C)(C)C)C(=O)C(C(C)(C)C)=C5)cc4)c4ccc(n4)c(-c4ccc(C(c5cc(C(C)(C)C)c(O)c(C(C)(C)C)c5)=C5C=C(C(C)(C)C)C(=O)C(C(C)(C)C)=C5)cc4)c4ccc3[nH]4)cc2)=C2C=C(C(C)(C)C)C(=O)C(C(C)(C)C)=C2)cc(C(C)(C)C)c1O",
        "COc1ccc(N(NC(=O)C(F)(F)F)C(=O)C(F)(F)F)cc1-c1ccccc1",
        "CC[C@H](C)[C@@H]([C@@H](CC(=O)N1CCC[C@H]1[C@H](OC)[C@@H](C)C(=O)N[C@@H](Cc1ccccc1)C(=O)Nc1nc2ccc(Cl)cc2s1)OC)N(C)C(=O)[C@@H](NC(=O)[C@H](C(C)C)N(C)C)C(C)C",
        "CCOP(=O)(Cc1cc(C(C)(C)C)c(O)c(C(C)(C)C)c1)N1CCN(c2ccccc2)CC1",
        "COc1ccc(/C=C/C(=O)N[C@@H]2C(=O)N3[C@@H]2SC(C)(C)[C@@H]3C(=O)O)cc1",
        "CC(C)(C)OC(=O)NC(Cc1ccc(OCc2ccccc2)cc1)C(=O)NC(CCCCNC(=O)OCc1ccccc1)C(=O)ON1C(=O)CCC1=O",
        "CC(C)(C)OC(=O)CC(NC(=O)OC(C)(C)C)C(=O)NC(CCCCNC(=O)OCc1ccccc1)C(=O)NC(C)(C)C(=O)NCCC(=O)OCc1ccccc1",
        "COC(=O)C(CCCCNC(=O)CNC(=O)OCc1ccccc1)NC(=O)C(CCCCNC(=O)CNC(=O)OCc1ccccc1)NC(=O)OCc1ccccc1",
        "CC(C)(C)OC(=O)[N+](=N)C(=O)C1CCC(=O)N1C(=O)OCc1ccccc1",
        "COC(=O)/C=C(\\C(=O)OC)c1c(C)c(C)c2c(C)cc(C)cc(C)c1-2",
        "COC1(OC)C[C@H](C)[C@@]23O[C@]24C2=CC(=O)C=CC2=N[C@H]3C#C/C=C\\C#C[C@@]41O",
        "CC(C)CCCC(C)[C@H]1CC[C@H]2[C@H]3CC[C@@H]4C[C@H](CCC=C(c5cc(Cl)c(O)c(C(=O)O)c5)c5cc(Cl)c(O)c(C(=O)O)c5)CC[C@]4(C)[C@H]3CC[C@]12C",
        "O=C1CSC2=NC3=C(CSC/C3=C/c3ccc(Cl)cc3)C(c3ccc(Cl)cc3)N12",
        "C[C@@H]1CC[C@H]2[C@@H](C)C(O)O[C@@H]3O[C@@]4(C)CC[C@@H]1[C@]32OO4",
        "COc1ccc(C(=O)NCCCN(C)C)c2nc3c(C(=O)NCCCN(C)C)c(N)c(=O)c(OC)c-3oc12",
        "CCOC(=O)C(=O)Nc1nc2c(s1)c(Cl)cc(NC(=O)C(=O)OCC)c2Cl",
        "CN1CCC(c2c(O)cc(O)c3c(=O)cc(-c4ccccc4)oc32)[C@@H](OC(=O)NC2CCCCC2)C1",
        "O=C(O)c1ccc(/C=C/c2ccc(N=Nc3c(O)cc(O)cc3C(=O)O)cc2C(=O)O)cc1",
        "N=C(N)NS(=O)(=O)c1ccc(NC(=O)c2ccc3nc4ccccc4c(Nc4ccc(S(N)(=O)=O)cc4)c3c2)cc1",
        "CCOC1=NC(C(F)(F)F)(C(F)(F)F)N=C(/N=C(\\N)N(C)C(C(C)C)P(=O)(OC(C)C)OC(C)C)O1",
        "CC1OC(OC2C(O)C(O)COC2OC(=O)[C@]23CCC(C)(C)C[C@H]2C2=CC[C@@H]4[C@@]5(C)CC[C@H](OC6OC(C(=O)O)C(O)C(O)C6O)C(C)(C)[C@@H]5CC[C@@]4(C)[C@]2(C)C[C@H]3O)C(O)C(O)C1OC1OCC(O)C(O)C1O",
        "CC(=O)OCC(OC(C)=O)C(OC(C)=O)C(OC(C)=O)C(/C=N\\N(C(C)=O)S(=O)(=O)c1ccc(/C=C2\\NC(=O)NC2=O)cc1)OC(C)=O",
        "CN(CCN(C)C1C2CC3CC(C2)CC1C3)CCN(C)C1C2CC3CC(C2)CC1C3",
        "C/C(=C\\CC/C(C=O)=C/CC(O)C1=CC(=O)OC1O)CCC1=C(C)CCCC1(C)C",
        "CN1C[C@H](c2c[nH]c3c2c(O)cc(Br)c3Br)NC[C@H]1c1c[nH]c2cc(Br)ccc21",
        "O=C(n1c(=O)n(-c2ccc([N+](=O)[O-])cc2)c(=O)n1C(=O)C(Cl)(Cl)Cl)C(Cl)(Cl)Cl",
        "COC(=O)c1ccc2oc3ccc(CN4CCC(n5c(=O)[nH]c6ccccc65)CC4)cc3c(=O)c2c1",
        "CO/N=C1\\C=C\\C=C/[C@H]2O[C@@H]2C[C@@H](C)OC(=O)c2c(O)cc(O)c(Cl)c2C1",
        "CC(=O)O[C@@H]1C2=C(C)[C@@H](OC(=O)[C@H](O)[C@@H](NC(=O)CC(C)C)c3ccccc3)C[C@@](O)([C@@H](OC(=O)c3ccccc3)[C@@H]3[C@]4(OC(C)=O)CO[C@@H]4C[C@H](O)[C@@]3(C)C1=O)C2(C)C",
        "Cc1cc(C)cc([Se]c2c(C3CC3)c(=O)[nH]c(=O)n2OCCc2ccccc2)c1",
        "COc1ccc(CCNC(=O)C2(C)CC3(C)CC(C)(C2)C(=O)NC3=O)cc1OC",
        "CC[C@H](C)[C@@H]([C@@H](CC(=O)N[C@@H](CCSC)C(=O)NCCc1ccc(Cl)cc1)OC)N(C)C(=O)[C@@H](NC(=O)[C@H](C(C)C)N(C)C)C(C)C",
        "CCN(CC)CC(=O)Nc1ccc(-c2ccc(NC(=O)CN(CC)CC)c(C)c2)cc1C",
        "C[Si](C)(O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)c1ccc2c(c1)C(=O)OC2=O)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)O[Si](C)(C)c1ccc2c(c1)C(=O)OC2=O",
        "COC(=O)C(Cc1cccc(O)c1)NC(=O)C(CC(=O)OCc1ccccc1)NC(=O)OCc1ccccc1",
        "N#Cc1sc2c(c1OC(=O)c1ccc(C(F)(F)F)cc1)c(=O)n(-c1ccccc1)c(=S)n2-c1ccccc1",
        "O=C(O)COc1c(Br)c2c(OCC(=O)O)c(c1Br)Cc1c(Br)c(OCC(=O)O)c(Br)c(c1OCC(=O)O)Cc1c(Br)c(OCC(=O)O)c(Br)c(c1OCC(=O)O)Cc1c(Br)c(OCC(=O)O)c(Br)c(c1OCC(=O)O)C2",
        "N#Cc1c(-c2ccccc2)cn(Cc2ccc(Cl)cc2Cl)c1/N=C/c1ccc(Cl)cc1",
        "CNC(=O)CCC(NC(=O)OC(C)(C)C)C(=O)NC(Cc1ccc([N+](=O)[O-])cc1)C(=O)NCC(=O)NC",
        "NC(Cc1ccc(OCc2ccccc2)cc1)C(=O)NC(Cc1ccccc1)C(=O)ON1C(=O)CCC1=O",
        "COc1cc(O)c2c(=O)c(OC)c3c4c2c1c1c(OC)cc(O)c2c(=O)c(OC)c(c4c21)C(C(C)O)C3C(C)O",
        "COC(=O)C1(NC(=O)C(CC(=O)OC(C)(C)C)NC(=O)OC(C)(C)C)CC1",
        "CCOC(=O)COc1c2cc(c(OCC(=O)OCC)c1OCC(=O)OCC)Cc1cc(c(OCC(=O)OCC)c(OCC(=O)OCC)c1OCC(=O)OCC)Cc1cc(c(OCC(=O)OCC)c(OCC(=O)OCC)c1OCC(=O)OCC)Cc1cc(c(OCC(=O)OCC)c(OCC(=O)OCC)c1OCC(=O)OCC)C2",
        "CCOP(=O)(CCCC(O)CNc1nc(N)nc(Cl)c1N=Nc1ccc(Cl)cc1)OCC",
        "CC(=O)O[C@H]1CN(C)CC[C@H]1c1c(O)cc(O)c2c(=O)cc(-c3ccccc3)oc21",
        "COc1cc(C2c3cc4c(cc3OC(Nc3ccc(C(=O)O)cc3)C2C)OCO4)cc(OC)c1OC",
        "Clc1ccc(C(c2ccccc2)N2CCN(CCCN3CCN(C(c4ccccc4)c4ccc(Cl)cc4)CC3)CC2)cc1",
        "O=C(c1ccccc1)N1CCN(c2ncnc(N3CCN(c4ncnc(N5CCN(C(=O)c6ccccc6)CC5)c4[N+](=O)[O-])CC3)c2[N+](=O)[O-])CC1",
        "CC(=O)NC(CS)C(=O)NC(CO)C(=O)NC(CO)C(=O)NC(Cc1c[nH]c2ccccc12)C(=O)N1CCCC1C(=O)NC(CO)C(=O)NC(Cc1c[nH]c2ccccc12)C(=O)NC(Cc1c[nH]cn1)C(=O)NC(CO)C(=O)NC(Cc1c[nH]c2ccccc12)C(=O)NCC(=O)NCC(=O)NC(CC(=O)O)C(=O)NC(CCC(N)=O)C(=O)NC(CCCCN)C(=O)NC(Cc1ccccc1)C(=O)NC(CCCNC(=N)N)C(=O)NC(CCCCN)C(N)=O",
        "O=[N+]([O-])C(F)(CCNCC(F)([N+](=O)[O-])[N+](=O)[O-])[N+](=O)[O-]",
        "COc1ccc(/N=C(Nc2ccc(OC)cc2)/C(=N/c2ccc(OC)cc2)Nc2ccc(OC)cc2)cc1",
        "O=C(n1[nH]c(=O)n(-c2ccc([N+](=O)[O-])cc2)c1=O)C(Cl)(Cl)Cl",
        "COc1ccc(N2C[C@@H](c3ccccc3)[C@H]3c4[nH]c5ccccc5c4CCN3C2)cc1",
        "C[N+](C)(C)CCOP(=O)([O-])OC[C@@H](COC(c1ccccc1)(c1ccccc1)c1ccccc1)OC(=O)C1CC1",
        "O=C(Cc1ccc([N+](=O)[O-])cc1)NN1C(=O)/C(=C/c2ccccc2O)SC1/C=C\\c1ccccc1",
        "CN(C)C(=O)OCOP(=O)(OCOC(=O)N(C)C)OCC1CCC(n2ccc(=O)[nH]c2=O)O1",
        "Cc1ccc2c(=O)n(-c3ccccc3)n(Cc3ccc([N+](=O)[O-])cc3)c2n1",
        "CCCc1cc(=O)oc2c1c1c(c3c2C(=O)[C@@H](C)[C@@H](C)O3)CCC(C)(C)O1",
        "O=[N+]([O-])/C(C(Cl)=C(Cl)Cl)=C(/Nc1ccccc1)SCc1ccccc1",
        "C(#CC1(Nc2ccccc2)CCN(Cc2ccccc2)CC1)C1(Nc2ccccc2)CCN(Cc2ccccc2)CC1",
        "CC(C)CC(=O)O/N=C1\\c2cc(Cl)ccc2N(Cc2c(Cl)cccc2Cl)C1=O",
        "Cc1cccc2c(CCNC(=O)c3ccc(C(=O)NCCc4c[nH]c5c4cccc5C)cc3)c[nH]c21",
        "O=C1NC2=NN=C(c3ccc(Br)cc3)CN2NC12c1ccccc1-c1ccccc12",
        "COC(=O)C1=C(C)NC(C)=C(C(=O)OCCCN2CCN(C(c3ccccc3)c3ccccc3)CC2)[C@@H]1c1cccc([N+](=O)[O-])c1",
        "CSC1(C)C(=O)N(CCO)c2ccc(Oc3c(F)cc(C(F)(F)F)cc3Cl)cc21",
        "CC(=O)O[C@@H]1CC[C@]2(C)C3CC[C@]4(C)C(C(C)CCc5nc6ccccc6[nH]5)CCC4C3[C@H](OC(C)=O)C[C@@H]2C1",
        "CCCCC(CCCC)c1cc[n+](CC(=O)Nc2ccc(NNC(=O)C(=O)NC(C)(C)CO)cc2)cc1",
        "CC(C)N(N)CC(O)C(Cc1ccccc1)NC(=O)C(CC(N)=O)NC(=O)c1ccc2ccccc2n1",
        "CC[N+]1=C(/C=C2/SC(c3ccccc3)=C(c3ccccc3)N2CCCS(=O)(=O)[O-])S/C(=C\\C=C2\\Sc3ccc4ccccc4c3N2CCCS(=O)(=O)O)C1=O",
        "NC(CCCCNC(=O)OCc1ccccc1)C(=O)NC(CCCCNC(=O)OCc1ccccc1)C(=O)ON1C(=O)CCC1=O",
        "CCOC(=O)c1sc2c(c1OC(=O)c1ccccc1)c(=O)n(-c1ccccc1)c(=S)n2-c1ccccc1",
        "COC(=O)c1cccc(O)c1C(=O)c1ccc(C(=O)O[C@@H]2CCC[C@H]2NC(=O)c2ccc(O)cc2)cc1O",
        "O[C@H]1CN2C[C@]34CC/C=C\\CCCN5C[C@H](C=C(CC/C=C\\CCCCCC2)[C@H]53)C14",
        "Nc1nc(Cl)c(N=Nc2ccc(Cl)cc2)c(NCC2(CO)CC(OCc3ccccc3)C2)n1",
        "C/C=C/C1OC(OC)(C(C)C(O)C(C)C2OC(=O)/C(OC)=C/C(C)=C/C(C)C(O)C(C)C(O)C(C)C/C(C)=C/C=C/C2OC)CC(OC2CC(O)C(O)C(C)O2)C1C",
        "CCOC(=O)COC(=O)CCN1C(=O)/C(=C/c2cc(C(C)(C)C)c(O)c(C(C)(C)C)c2)SC1=S",
        "C=C(C)C1CC=C(C(=O)OCOC(=O)CCC(=O)OCc2ccc(N=[N+]=[N-])cc2)CC1",
        "C=C1CN(S(=O)(=O)c2ccc(C)cc2)CCCN(Cc2cc([N+](=O)[O-])ccc2O)CCCN(S(=O)(=O)c2ccc(C)cc2)C1",
        "O=C1/C(=C/c2ccc([N+](=O)[O-])cc2)CNC/C1=C\\c1ccc([N+](=O)[O-])cc1",
        "COC1CC(OC2C(C)OC(OC3C(C)OC(OC4C(C)OC(O[C@H]5CC[C@@]6(C)C(=CC[C@]7(O)[C@@H]6C[C@@H](OC(=O)/C=C(\\C)C(C)C)[C@@]6(C)[C@]7(O)CC[C@@]6(O)C(C)=O)C5)CC4OC)CC3OC)CC2OC)OC(C)C1O",
        "COc1ccc(-c2c(C#N)c(S)n(NS(=O)(=O)c3ccccc3)c(=O)c2C#N)cc1",
        "CCCC(=O)NC(Nc1ccc(S(=O)(=O)Nc2cc(C)on2)cc1)(C(=O)OCC)C(F)(F)F",
        "Cc1cccc(C(=O)O)c1NC(=O)c1cccc(NC(=O)Nc2cccc(C(=O)Nc3c(C)cccc3C(=O)O)c2)c1",
        "CCC1(c2ccc(N3C(=O)c4cc(OC)c(OC)cc4C3=O)cc2)CCC(=O)NC1=O",
        "CC(=O)O[C@@H]1C2=C(C)C(OC(=O)[C@H](O)[C@@H](NC(=O)/C(C)=N/OCc3ccccc3)c3ccccc3)C[C@@](O)([C@@H](OC(=O)c3ccccc3)C3[C@]4(OC(C)=O)CO[C@@H]4C[C@H](O)[C@@]3(C)C1=O)C2(C)C",
        "O=C(CNC1CCCCC1)Nc1nc2cc3nc(NC(=O)CNC4CCCCC4)sc3cc2s1",
        "CCOC(=O)C(NC(C)=O)(NC(=O)c1ncn(Cc2ccccc2)c1N)C(=O)OCC",
        "Cc1cn(C2CC(N=[N+]=[N-])C(COC(=O)CCOCCCCCCCCCl)O2)c(=O)[nH]c1=O",
        "CCC(/C=C1/Sc2cc(OC)ccc2N1CC)=C1/SC(/C=C2\\Sc3ccccc3N2CC)=[N+](c2cccc3ccccc32)C1=O",
        "CN(C)c1cccc2c1C(=O)C=CC21CCc2ccc(N(C)C)c3c(N(C)C)ccc1c23",
        "C=C1C2OC2C(O)C(C)C(=O)OC(CCCC)C2CCC(O)C(CC3(O)OC(CCC3O)C/C(C)=C/C(C)C(=O)CC1O)O2",
        "Cc1c2[nH]c3ccccc3c2c(C)c2c1C=CN(C(=O)Oc1ccc([N+](=O)[O-])cc1)C2",
        "CCN(CC)C(=O)c1cc([N+](=O)[O-])c(=O)n2c3ccccc3[nH]c12",
        "CCN1c2ccc(OC)cc2S/C1=C\\C=c1\\s/c(=C/c2sc(C)c(-c3ccccc3)[n+]2C)n(-c2ccccc2)c1=O",
        "COc1ccc(CC2C(=S)NC(C)C(=O)N(C)C3CCc4ccc(cc4)Oc4cc(ccc4OC)CC(C(=O)N[C@H](C)C(=O)NC(C)C(=O)N2C)N(C)C3=O)cc1",
        "CC(C)(C)c1cc(-c2ccc(-c3cc(C(C)(C)C)c(O)c(C(C)(C)C)c3)cc2)cc(C(C)(C)C)c1O",
        "O=S(=O)(Nc1ccc2cnn(-c3ncc(C(F)(F)F)cc3C(F)(F)F)c2c1)C(F)(F)F",
        "Cc1ccc(Nc2ccc(O)c3c2C(=O)c2ccccc2C3=O)cc1S(=O)(=O)O",
        "O=C1NCN(c2ccccc2)C12CCN(CCCn1nc(Cc3ccc(Cl)cc3)c3ccccc3c1=O)CC2",
        "Cc1cc(C)c(C2CNCc3cc4c(cc32)CNCC4c2c(C)cc(C)cc2C)c(C)c1.O=C(O)C(O)c1ccccc1",
        "CC(=O)Oc1cc(OC(C)=O)c2c(c1C)CC[C@H]1[C@@H]3CC[C@H](OC(C)=O)[C@@]3(C)CC[C@H]21",
        "CCOC(=O)NC(Nc1ccc(S(=O)(=O)Nc2ncccn2)cc1)(C(=O)OCC)C(F)(F)F",
        "C=CCCCCCCCCC(=O)OCC(COC(=O)CCCCCCCCC=C)OC(=O)CCCCCCCCC=C",
        "O=[N+]([O-])c1ccc(Sc2cc(Sc3ccc([N+](=O)[O-])cc3)nc(Cl)n2)cc1",
        "CCOC(=O)C(=O)Nc1cc(NC(=O)C(=O)OCC)cc(C(=O)Nc2ncccn2)c1",
        "Cc1ccc(S(=O)(=O)N2C/C=C\\CN(S(=O)(=O)c3ccc(C)cc3)CCCN(Cc3ccccc3)CCC2)cc1",
        "COC1CC(OC2C(C)OC(OC3C(C)OC(OC4C(C)OC(O[C@H]5CC[C@@]6(C)C(=CC[C@]7(O)[C@@H]6C[C@@H](OC(=O)/C=C/c6ccccc6)[C@@]6(C)[C@]7(O)CC[C@@]6(O)C(C)=O)C5)CC4OC)CC3OC)CC2OC)OC(C)C1O",
        "NC(CCC(=O)NC(CSc1c(Cl)c(Cl)c(Cl)c(Cl)c1Cl)C(=O)NCC(=O)O)C(=O)O",
        "CC[C@H](C)[C@@H]([C@@H](CC(=O)N1CCC[C@H]1[C@H](OC)[C@@H](C)C(=O)N[C@@H](CCSC)C(=O)NC1CC2(C)CCC1C2(C)C)OC)N(C)C(=O)[C@@H](NC(=O)[C@H](C(C)C)N(C)C)C(C)C",
        "Nc1nc(Cl)c(N=Nc2ccc(Cl)cc2)c(NCC2(CO)CC(OCc3ccccc3)C2)n1",
        "COc1cc(-c2oc3c(OC)c(OC)c(OC)c(OC)c3c(=O)c2OC)cc(OC)c1OC",
        "CC(C)COP(=O)(OCC(C)C)C(NS(=O)(=O)c1ccccc1)(C(F)(F)F)C(F)(F)F",
        "C[n+]1cn(C2=C([N-]S(=O)(=O)c3ccccc3)C(=O)c3ccccc3C2=O)c2ccccc21",
        "C[n+]1cn(-c2nc3ccccc3nc2[N-]S(=O)(=O)c2ccccc2)c2ccccc21",
        "O=C(NC(Nc1ccc(F)c(Cl)c1)(C(F)(F)F)C(F)(F)F)C(C(F)(F)F)C(F)(F)F",
        "Cc1c(C)c(C)[n+]2c3c4c(ccc31)c(C)c(C)c(C)[n+]4[Cu+]21[n+]2c3c4c(ccc3c(C)c(C)c2C)c(C)c(C)c(C)[n+]41",
        "CCOC(=O)NN(C(=O)OCC)C(C(=O)C1=CCCCC1)N(NC(=O)OCC)C(=O)OCC",
        "CN1CN(c2ccccc2)C2(CCN(CCCSc3ccccc3NC(=O)/C=C/c3ccccc3)CC2)C1=O",
        "CCOC(=O)NNc1c(C(=O)C(F)(F)F)cc(C(=O)C(F)(F)F)c2ccccc21",
        "O=S(=O)(/N=C(\\Sc1nnn(-c2ccccc2)n1)c1ccccc1)c1ccc(Cl)cc1",
        "CC1=NN(c2nc(N)nc(-c3cc(N4CCCC4)c(Cl)c(S(N)(=O)=O)c3)n2)C(C)(C)C1",
        "O=C1c2cc(COCc3cc4c(c(O)c3O)C(=O)c3c(O)cc(O)c(O)c3C4=O)c(O)c(O)c2C(=O)c2c(O)cc(O)c(O)c21",
        "CC(C)(Cl)C(Br)CC/C(CBr)=C(\\Cl)CCl.CC(C)(Cl)C(Br)CC/C(CBr)=C(/Cl)CCl",
        "CN/C(=N\\S(C)(=O)=O)C(C)Oc1ccc(Oc2cnc3cc(Cl)ccc3n2)cc1",
        "O=C(O)c1ccc(-c2c3ccc(n3)c(-c3ccc(C(=O)O)cc3)c3ccc([nH]3)c(-c3ccc(C(=O)O)cc3)c3ccc(n3)c(-c3ccc(C(=O)O)cc3)c3ccc2[nH]3)cc1",
        "Cc1ccc(C(=O)NC2C(=O)NC(C(C)C)C(=O)N3CCCC3C(=O)N(C)CC(=O)N(C)C(C(C)O)C(=O)OC2C)c2nc3c(C(=O)NC4C(=O)NC(C(C)C)C(=O)N5CCCC5C(=O)N(C)CC(=O)N(C)C(C(C)O)C(=O)OC4C)c(N)c(=O)c(C)c-3oc12",
        "CCCCN1C[C@H](OC(=O)CCC)[C@@H](OC(=O)CCC)[C@H](OC(=O)CCC)[C@H]1COC(=O)CCC",
        "CCOC(=O)C(NC(=O)c1ncn(Cc2ccccc2)c1/N=C/c1ccccc1)(OC)C(=O)OCC",
        "CCP(CC)(CC)[Au]SC1OC(COC(C)=O)C(OC(C)=O)C(OC(C)=O)C1OC(C)=O",
        "O=[N+]([O-])/C(C(Cl)=C(Cl)Cl)=C(/Sc1ccccc1)N1CCOCC1",
        "CCn1c(=O)/c(=C\\C2=C([O-])N(CC)/C(=C\\c3sc4cc(OC)ccc4[n+]3CCC(=O)O)S2)s/c1=C/c1sc2cc(OC)ccc2[n+]1CCC(=O)[O-]",
        "CC(/C=C/C=C(C)\\C=C\\C1=C(C)CCCC1(C)C)=C\\C(=O)Nc1ccc2c(c1)OCO2",
        "COC(C/C=C/N(C)C=O)C(C)C(=O)CCC(C)C(OC)C(C)C1OC(=O)/C=C/C=C(\\C)CC(OC)CC2=CC(=O)O[C@H]([C@H]2O)C(C)C(OC)CC(OC)/C=C/C(C)C(OC)CC(O)/C=C/C1C",
        "O=Cc1c(Cl)ncnc1N1CC[N+]2(CC1)CC[N+]1(CCN(c3ncnc(Cl)c3C=O)CC1)CC2",
        "CCOC(=O)C(NC(=O)C1CN(Cc2ccccc2)C=N1)(C(=O)OCC)C(NC(=O)C1N=CN(Cc2ccccc2)C1[N+](=O)[O-])(C(=O)OCC)C(=O)OCC",
        "CCC(/C=C1/Oc2ccccc2N1C)=C1/SC(/C=C2/Sc3ccccc3N2CC)=[N+](c2ccccc2)C1=O",
        "COc1ccc(/C(=N/N=c2\\[nH]c3ccccc3n2Cc2ccccc2)C(=O)O)cc1",
        "CC[C@]12CCc3c(c4ccccc4n3S(=O)(=O)c3ccc(OC)cc3)[C@H]1N(CCSc1ccccc1)C(=S)CC2",
        "O=C1OC2(C34OC(=O)c5cccc(c53)C(=O)c3ccccc34)c3c1cccc3C(=O)c1ccccc12",
        "CN1c2ccccc2C(=O)/C(=C\\Nc2cc(C(F)(F)F)cc(C(F)(F)F)c2)S1(=O)=O",
        "CC[C@@]1(C[C@H]2C[C@@]3(C(=O)OC)c4[nH]c5ccccc5c4CCN3C2=O)COC2(CCCCC2)O1",
        "CC[C@]1(O)c2cc3n(c(=O)c2COC1=O)C(NCCN(C)C)c1cc2c(cccc2OC)nc1-3",
        "NC1=NN(c2ccccc2)C2(O)c3ccncc3C3=NN(c4ccccc4)C(=O)C132",
        "O=C(CCCCCCCCCCC(=O)Nc1ccc(P(=O)(O)O)cc1)Nc1ccc(P(=O)(O)O)cc1",
        "Cc1c([N+](=O)[O-])c2ccc(O)c(CN(CCO)CCO)c2n1-c1ccc(C)cc1",
        "O=C(CCc1ccc(Cc2ccc(CCC(=O)Nc3ccc(P(=O)(O)O)cc3)cc2)cc1)Nc1ccc(P(=O)(O)O)cc1",
        "C[C@H](CCc1nc(C#N)c(N)o1)[C@H]1CC[C@H]2[C@@H]3CCC4C[C@@H](O)CC[C@]4(C)[C@H]3C[C@@H](O)[C@]12C",
        "Cc1cc(O)c(C=O)c2c1C(=O)Oc1c(COCO/C=C/C(=O)O)c(O)c(C(=O)O)c(C)c1O2",
        "O=C(O)c1[nH]c(=O)[nH]c(=O)c1SSc1c(=O)[nH]c(=O)[nH]c1C(=O)O",
        "COc1ccc(NC(=O)CN(C)C(=O)COc2ccc3oc4c(c3c2)CCCC4)cc1",
        "CC(O)C(CO)NC(=O)C1CSSCC(NC(=O)C(N)Cc2ccccc2)C(=O)NC(Cc2ccccc2)C(=O)NC(Cc2c[nH]c3ccccc23)C(=O)NC(CCCCN)C(=O)NC(C(C)O)C(=O)N1",
        "CCCCCCC(=O)NC(CCC(=O)NC(CCCC(N)C(=O)O)C(=O)NC(C)C(=O)O)C(=O)O",
        "Cc1nc2cc(-c3noc(CNS(=O)(=O)c4ccccc4)n3)ccc2n(C2CCCC2)c1=O",
        "CCS(=O)(=O)N1CCCc2ccc(NC(=O)CN3C(=O)c4ccccc4C3=O)cc21",
        "C=CCn1c(=O)c2sc(N(CC)CC)nc2n(CC(=O)Nc2cccc(OC)c2)c1=O",
        "C=CCNC(=O)C1CCN(C(=O)Nc2ccc(NC(=O)Nc3ccc(C)c(C)c3)cc2)CC1",
        "COc1cccc(CNC(=O)N2CCN(Cc3nc4cc(C(=O)NC(C)C)ccc4n3C)CC2)c1",
        "Cc1ccc(NC(=O)Nc2ccc(F)c(-c3nnc(-c4ccccc4)o3)c2)cc1F",
        "O=C(c1cccc(OCC2CCCN(C(=O)C3(c4ccccc4)CC3)C2)c1)N1CCCCC1",
        "O=C(CNC(=O)COC(=O)Cc1n[nH]c(=O)c2ccccc12)Nc1ccc(F)c(F)c1F",
        "CN(C)S(=O)(=O)c1ccc(Cl)c(C(=O)NCc2ccnc(N3CCCC3)c2)c1",
        "CN1CCN(c2ccc(NC(=O)CCc3ncc(-c4ccc(Cl)cc4)o3)cc2)CC1",
        "CCOC(=O)C1CCN(CCC(=O)Nc2c3cc(F)ccc3[nH]c2C(=O)OC)CC1",
        "Cc1nn(C)cc1CNC(=O)c1cc2nc(-c3ccc(Cl)s3)cc(C(F)(F)F)n2n1",
        "Cc1ccccc1NC(=O)CSc1nc2c(c(=O)n(C)c(=O)n2C)n1Cc1ccccc1F",
        "Cc1ccc(Cl)c(OCn2ccc(C(=O)N3CCN(Cc4ccccc4F)CC3)n2)c1",
        "CCC(C)[C@H](NC(=O)[C@@H]1Cc2ccccc2CN1)C(=O)Nc1cccc(C)c1",
        "COC(=O)Cc1cs/c(=N/C(=O)Cc2c(C)c3cc(Cl)c(O)cc3oc2=O)[nH]1",
        "CC(=O)Nc1cccc(NC(=O)C(=O)N2CCN(C(=O)c3cc4ccccc4[nH]3)CC2)c1",
        "CCC(C)[C@H](NC(=O)Nc1ccc2oc(-c3cccnc3)nc2c1)C(=O)OC",
        "COc1cc(OC)c2c(C)c(CC(=O)N3CCC(O)(c4ccc(Cl)cc4)CC3)c(=O)oc2c1",
        "COc1cccc(CNC(=O)c2cc3c(s2)-c2c(C)nn(-c4ccccc4)c2CC3)c1",
        "Cn1c2ccc(NC3=C(C(=O)N4CCOCC4)S(=O)(=O)c4ccccc43)cc2n(C)c1=O",
        "Cc1ccc(C(C)NC(=O)c2cn(-c3ccc(NC(=O)CC(C)C)cn3)cn2)cc1",
        "COc1ccc(NC(=O)Cn2c(=O)cc(C(=O)N3CCc4ccccc4C3)c3ccccc32)cc1OC",
        "COCCNC(=O)C1CCCN1C(=O)c1ccc(NS(=O)(=O)c2ccc(Cl)cc2)cc1",
        "COc1cc(NC(=O)CCc2nc(-c3ccc(N4CCCC4)cc3)no2)cc(OC)c1",
        "Cc1nn(CCCC(=O)NCc2ccc3c(c2)OCO3)c(=O)c2c1nn(-c1ccccc1)c2-n1cccc1",
        "CCCCc1ccc(S(=O)(=O)c2cnc(SCC(=O)Nc3cccc(Cl)c3)[nH]c2=O)cc1",
        "Cc1cc(C)c2c(=O)n(C(C)C(=O)Nc3cc(Cl)ccc3C)c(=O)n(C)c2n1",
        "CCN1CCN(c2ccc(C(=O)OC)cc2NC(=O)c2ccc(OC)c(OC)c2)CC1",
        "COc1ccc(S(=O)(=O)N(C)C)cc1NC(=O)Cc1nn(C)c(=O)c2ccccc12",
        "Cc1cc(C)n(-c2nc3c(c(=O)n(CC(C)C)c(=O)n3C)n2CCc2ccccc2)n1",
        "Cc1cc(NC(=O)CSc2nnnn2-c2ccc(C)c(C)c2)n(-c2ccccc2)n1",
        "CCn1c2c(nc1-n1nc(C)c(C)c1C)n(C)c(=O)n(Cc1c(F)cccc1Cl)c2=O",
        "CCCn1c2c(nc1-n1nc(C)cc1C)n(C)c(=O)n(Cc1cccc(Cl)c1)c2=O",
        "CCC(C(=O)Nc1sc2c(c1C#N)CCCC2)n1cnc2c1c(=O)n(C)c(=O)n2C",
        "COCCN1C(=O)C(O)=C(C(=O)c2ccc(OCc3ccccc3)cc2C)C1c1cccnc1",
        "CC(C)c1nc(N2CCN(C(=O)C(=O)N3CCOCC3)CC2)c(C#N)c2c1CCCC2",
        "Cc1c(C)n2c(nc3c2c(O)nc(=O)n3C)n1CCNc1ccc(Oc2ccccc2)cc1",
        "CCC1(C)Cc2nc3sc4c(ncn(CC(=O)NCc5ccco5)c4=O)c3cc2CO1",
        "Cc1ccc(N2CCN(C(C(=O)O)c3c[nH]c4cc(NC(=O)C(C)C)ccc43)CC2)c(C)c1",
        "CCCS(=O)(=O)NC(=O)C1(C)CCCN(C(=O)c2cc(C(C)(C)C)nn2C)C1",
        "CN(C)c1ccc(CNC(=O)C2CCN(c3ncccc3S(=O)(=O)N3CCOCC3)CC2)cc1",
        "CNC(=O)C1CCCN(C(=O)Nc2ccc(NC(=O)Nc3ccc(OC)cc3)cc2)C1",
        "CCc1ccc(S(=O)(=O)c2cnc(SCC(=O)Nc3ccc4c(c3)OCCO4)[nH]c2=O)cc1",
        "CN(C)S(=O)(=O)N(CC(=O)NCCCN1CCN(c2ccccc2F)CC1)c1ccccc1",
        "O=C(NCc1ccc2c(c1)OCO2)c1cccc(Nc2ncc(-c3ccc(F)cc3)cn2)c1",
        "Cc1ccc2[nH]cc(CCNC(=O)C3CCCN(c4nc5ncccc5n5cccc45)C3)c2c1",
        "COc1ccc(CN(Cc2cc(=O)n3nc(C(C)(C)C)sc3n2)CC(C)C)cc1OC",
        "Cc1nn(-c2ccc3nnc(C)n3n2)c2c1C(c1cc(Br)ccc1F)CC(=O)N2",
        "CC(NC(=O)C1CCN(S(=O)(=O)c2cccc(OC(F)(F)F)c2)CC1)c1cccnc1",
        "Cc1cccc(Cn2c(N3CCN(CC(N)=O)CC3)nc3c2c(=O)[nH]c(=O)n3C)c1",
        "Cc1ccc2nc([NH+]3CCN(C(=O)CCCC(=O)[O-])CC3)nc(-c3ccccc3)c2c1",
        "COc1ccc(C(=O)Cn2c(N3CCCCCC3)nc3c2c(=O)n(C)c(=O)n3C)cc1",
        "CC(=O)N1CCC2(CC1)Oc1ccccc1C1CC(c3cccc(OC(F)F)c3)=NN12",
        "O=C(Nc1oc2ccccc2c(=O)c1-c1ccc(F)cc1)c1ccc([N+](=O)[O-])cc1",
        "COc1ccc(C(=O)Nc2oc3ccc(Cl)cc3c(=O)c2-c2ccccc2)cc1OC",
        "N#Cc1c2c(sc1N1C(=O)CC(c3ccc4c(c3)OCO4)C3=C1CCCC3=O)CCCCC2",
        "CN(Cc1ccccc1)c1nc2c(c(=O)n(Cc3ccccc3Cl)c(=O)n2C)n1C",
        "COc1cccc(CNC(=O)c2ccc(-c3nc(CCN(C)C(=O)c4ccccc4F)no3)cc2)c1",
        "CCOc1cccc(-c2noc(-c3nn(-c4ccc(C)c(C)c4)ccc3=O)n2)c1",
        "CCOC(=O)c1cc(C)nc2c1c(=O)n(Cc1nc(-c3ccccc3C)oc1C)c(=O)n2C",
        "O=C(CN1CCN(C(=O)Nc2ccc(F)cc2)CC1=O)N1CCN(c2ccccn2)CC1",
        "Cc1cccc(-c2csc3c2nc(N2CCN(c4cccc(C)c4)C(C)C2)[nH]c3=O)c1",
        "CC(C)C(NS(=O)(=O)/C=C/c1ccccc1)C(=O)NC1CC(C)(C)NC(C)(C)C1",
        "O=C(c1ccccc1)N1CCc2ccc(C(=O)N3CCN(c4ccc(Cl)cc4)CC3)cc21",
        "COc1ccc(NC(=O)CCCc2nnc3c(=O)n(-c4cc(C)cc(C)c4)ccn23)cc1OC",
        "Cc1ccc(C)c(NS(=O)(=O)c2ccc(N3CC(C(=O)O)CC3=O)cc2)c1",
        "CCc1cc(CC)n(-c2nc3c(c(=O)n(CCN4CCCCC4)c(=O)n3C)n2CC)n1",
        "COc1ccc(-c2cc(C(=O)N3CCN(CC(=O)N(C)C)CC3)c3ccccc3n2)cc1",
        "CC(=O)N1CCN(S(=O)(=O)c2ccc(C(=O)N(C)Cc3ccccc3N3CCCCC3)cc2)CC1",
        "COc1cccc(NC(=O)C2CCCN(C(=O)c3ccc(OC)c4ccccc34)C2)c1",
        "CN(C)S(=O)(=O)c1ccc(N2CCCCC2)c(C(=O)OC(C(=O)NC2CC2)c2ccccc2)c1",
        "CC(C)(C)c1nc2cc(NC(=O)Cc3ccc([N+](=O)[O-])cc3)ccc2o1",
        "Cc1ccc(OCc2nc3c4c(ncn3[nH+]2)Oc2cc([O-])ccc2C4c2ccccc2)cc1",
        "Cc1cc(C)c(O)c(-c2[nH]nc3c2C(c2ccc(Cl)c(Cl)c2)N(CCCO)C3=O)c1",
        "COc1ccccc1-c1nc(S(=O)(=O)c2ccc3c(c2)OCCO3)c(N2CCOCC2)o1",
        "CCOC(=O)C1=C(C)NC(=O)NC1c1cn(-c2ccccc2)nc1-c1ccc(OC(C)C)c(C)c1",
        "Cc1cc(C)c2oc(C(=O)N(Cc3cccc(Cl)c3)C3CCS(=O)(=O)C3)cc(=O)c2c1",
        "COc1cc(OC)c(C2C3=C(CC(C)(C)CC3=O)Nc3ccccc3N2C(C)=O)cc1OC",
        "CCC(=O)N1c2ccccc2NC2=C(C(=O)CC(C)(C)C2)C1c1cc(OC)ccc1OC",
        "CCOC(=O)c1[nH]c2ccc(OC)cc2c1NC(=O)CCN1CCC(C(=O)OCC)CC1",
        "Cc1cc(C(F)F)nn1Cc1nc2c3c4c(sc3ncn2n1)CC(C(C)(C)C)CC4",
        "COc1ccccc1CCN1CNc2c(c(=O)[nH]c(=O)n2-c2ccc(Cl)cc2)C1",
        "Cc1ccc(C(=O)NC2(C(F)(F)F)C(=O)Nc3c2c(=O)[nH]c(=O)n3Cc2ccco2)cc1",
        "COCCNC(=O)C1(C)CCN(C(=O)c2ccc3c(c2)C(C)(C)CCC3(C)C)C1",
        "COc1ccc(-c2nc3ccccc3c3nnc(SCC(=O)N(C)c4ccc(F)cc4)n32)cc1",
        "CCOc1ccc(NC(=O)c2cn(C)c3c(c(C)nn3-c3ccc(F)cc3)c2=O)cc1",
        "CC(=O)Nc1ccc(Cc2nc3ccc(C(=O)N4CCN(c5ccc(Cl)cc5)CC4)cc3[nH]2)cc1",
        "CC(C)n1nnc2cc(S(=O)(=O)N3CCCC(C(=O)NCCCN4CCCCC4)C3)ccc21",
        "COc1ccc(N2CCN(C(=O)c3ccc(-c4nc(-c5cccc(F)c5)no4)cc3)CC2)cc1",
        "CC(=O)Nc1ccc(C)cc1S(=O)(=O)N1CCOc2ccc(C(=O)NCc3ccccc3)cc2C1",
        "O=C(CN1C(=O)COc2ccc(Cl)cc21)Nc1ccc2[nH]c(CCc3ccccc3)nc2c1",
        "Cc1ccc(NC(=O)Cn2ccn3nc(-c4ccc5c(c4)OCO5)cc3c2=O)c(C)c1",
        "Cc1cc(C)cc(NC(=O)CCc2nc(CSc3nc(-c4cccnc4)n[nH]3)no2)c1",
        "CCOC(=O)C1CCCN(C(=O)CSc2nnc3c4ccccc4nc(-c4cccc(C)c4)n23)C1",
        "O=C(CNS(=O)(=O)c1ccc2c(c1)CCN2C(=O)N1CCCC1)N1CCc2ccccc2C1",
        "COC(=O)C(O)(c1ccc(N(C)C(=O)Nc2cccc(Cl)c2)cc1)C(F)(F)F",
        "Cc1ccc(S(=O)(=O)Nc2nc3ccccc3nc2Nc2cccc(C(=O)O)c2)cc1C",
        "Cc1ccccc1C(=O)N1CCN(c2ccc([N+](=O)[O-])c(-n3nc(C)c4ccccc4c3=O)c2)CC1",
        "CC(=O)N1CCN(c2nc3c(c(=O)n(C)c(=O)n3C)n2Cc2ccc(F)cc2)CC1",
        "CN1CC[NH+](Cc2cc(=O)oc3cc(-c4ccccc4)c([O-])cc23)CC1",
        "COC(=O)c1cc(NCc2cc(Cl)c(OCC(N)=O)c(OC)c2)ccc1N1CCOCC1",
        "Cc1cccc(-c2nc3cc(NC(=O)c4cccc([N+](=O)[O-])c4C)ccc3o2)c1",
        "Cc1cc(O)c(-c2cc(C(=O)Nc3cccc(C(F)(F)F)c3)n[nH]2)cc1C",
        "COc1ccc(N(C(=O)CNC(C)=O)C2(C(=O)Nc3c(C)cccc3C)CCCCC2)cc1",
        "Cc1c(C(=O)Nc2cccc(C(F)(F)F)c2)sc2c1C(c1cccc(O)c1)CC(=O)N2",
        "Cc1nn(CC(=O)Nc2ccc3c(c2)nc(CCN2CCOCC2)n3C)c(=O)c2ccccc12",
        "Cc1ccc(S(=O)(=O)N(Cc2cc3ccc(C)cc3[nH]c2=O)CC2CCCO2)cc1",
        "CN(Cc1ccccc1)c1nc2c(c(=O)n(C)c(=O)n2C)n1CC(O)COc1ccccc1",
        "COc1cccc(-c2c(NC(=O)c3ccc([N+](=O)[O-])cc3)oc3ccccc3c2=O)c1",
        "CCN(CC)C(=O)Oc1ccc2c(c1)oc(C(F)(F)F)c(-c1ccccc1)c2=O",
        "CCc1ccc(OCC(=O)Nc2ccc(S(=O)(=O)NC(C)(C)C)cc2)c(Br)c1",
        "Cc1ccc(OCC(=O)Nc2ccc(S(=O)(=O)NC(C)(C)C)cc2)c(Br)c1",
        "COc1ccc(Nc2nc3ccccc3nc2NS(=O)(=O)c2ccc(NC(C)=O)cc2)cc1OC",
        "Cn1c(SCC(=O)NC(c2ccccc2)c2ccccc2)nc2c1c(=O)n(C)c(=O)n2C",
        "CCc1nc2c(OCC(=O)C(C)(C)C)cccn2c1N(C=O)Cc1ccc(OC)cc1",
        "CCc1nc2c(OCc3ccc(C(F)(F)F)cc3)cccn2c1N(C=O)Cc1ccc(OC)cc1",
        "CC1(C(=O)NS(=O)(=O)C2CC2)CCCN(C(=O)c2cccc(C(F)(F)F)c2)C1",
        "COc1ccc(OC)c(CN(C(=O)c2cccc(NC(=O)c3ccc4[nH]cnc4c3)c2)C2CC2)c1",
        "Cc1ccc(C(O)(CC(=O)NCC(=O)Nc2ccc(F)c(F)c2)C(F)(F)F)o1",
        "CCOC(=O)c1oc2cc(OCc3cccc(OC)c3)ccc2c(=O)c1-c1ccc(OC)c(OC)c1",
        "COC(=O)c1ccc(Oc2c(C)oc3cc(OC(=O)c4ccco4)ccc3c2=O)cc1",
        "CCOC(=O)c1ccc(NC(=O)c2c3cc(OC)ccc3oc2-c2ccc(OC)cc2)cc1",
        "COc1ccc(Oc2coc3cc(OCc4ccc([N+](=O)[O-])cc4)ccc3c2=O)cc1",
        "COC(=O)C(C)Oc1ccc2c(C)c(-c3ccc(OC)c(OC)c3)c(=O)oc2c1",
        "Cc1ccc2nc(-c3ccc(C)c(NC(=O)c4ccc5c(c4)OCO5)c3)oc2c1",
        "CCC(C)(C(=O)NC1CCCCC1)N(Cc1cccs1)C(=O)c1ccc2c(c1)OCO2",
        "COc1ccc2oc(=O)c(C(=O)NCCc3c(C)[nH]c4ccc(C)cc43)cc2c1",
        "COc1ccc(CNc2nc(NCc3ccc(OC)cc3)n(S(=O)(=O)c3ccc(C)cc3)n2)cc1",
        "Cc1cc(C)cc(N(CC(=O)Nc2ccccc2C(=O)NCC2CCCO2)S(C)(=O)=O)c1",
        "COc1cc(C2c3c(n[nH]c3-c3cc(Cl)c(C)cc3O)C(=O)N2CCO)ccc1O",
        "Cn1c(NCc2ccccc2)nc2c1c(=O)n(Cc1ccc(Cl)c(Cl)c1)c(=O)n2C",
        "CCN1CCN(c2nc3c(c(=O)n(C)c(=O)n3C)n2CC(=O)c2ccccc2)CC1",
        "Cc1ccc(S(=O)(=O)NC(=O)C2(C)CCN2C(=O)Cc2ccc(-c3ccccc3)cc2)cc1",
        "CCCCNC(=O)C1(C)CCCCN1C(=O)c1ccc2c(c1)C(C)(C)CCC2(C)C",
        "CC1(C(=O)NS(=O)(=O)c2cccs2)CCN1C(=O)CC(c1ccccc1)c1ccccc1",
        "Cc1ccc(C(=O)Nc2cccc(-c3cn4c(C(=O)N(C)C)csc4n3)c2)cc1",
        "Cc1ccc(NC(=O)c2ccc3oc(=O)n(CC(=O)NCc4cccc(Cl)c4)c3c2)cc1",
        "CN(Cc1cn(-c2ccccc2)nc1-c1ccc(-c2ccccc2)cc1)C(=O)/C=C/C(=O)O",
        "Cc1nn(-c2ccc(-c3noc(C4CC4)n3)cc2)cc1C(=O)NCc1ccccc1",
        "CCCc1c(C)nc2ncnn2c1Sc1ccc(NS(=O)(=O)c2ccc(OC)cc2)cc1",
        "COc1cc(NC(=O)c2cccc(Nc3ncc(-c4cccc(F)c4)cn3)c2)cc(OC)c1",
        "Cc1ccc(-c2cc(N3CCOCC3)c3cc(NC(=O)NCCc4ccccc4)ccc3n2)cc1",
        "Cc1ccc(-c2csc(SCc3noc(CCC(=O)Nc4cc(F)cc(F)c4)n3)n2)cc1",
        "COc1ccc2c(c1)c1nc(SCC(=O)Nc3cc(F)ccc3F)n(C)c(=O)c1n2C",
        "CC1Oc2ccccc2N(CC(=O)Nc2ccc3sc(NC(=O)C4CCCCC4)nc3c2)C1=O",
        "COc1cccc(C(=O)Oc2ccc3c(c2)oc(C)c(-c2ccccc2OC)c3=O)c1",
        "COc1ccccc1CNc1nc2ccccc2nc1NS(=O)(=O)c1cccc2c1N=S=N2",
        "COC(=O)C(O)(c1ccc(N(C)C(=O)Nc2ccc(Cl)cc2)cc1)C(F)(F)F",
        "Cn1c2nc(N3CCN(c4ccccc4)CC3)n(Cc3ccc(F)cc3)c2c(=O)n(C)c1=O",
        "COc1cc(C(=O)/C(=C/c2cccnc2)c2nc3ccccc3o2)cc(OC)c1OC",
        "COc1cc(OC)c(Nc2nc3ccccc3nc2NS(=O)(=O)c2ccccc2)cc1Cl",
        "COc1ccc(CNc2nc(NCc3ccc(OC)cc3)n(S(=O)(=O)c3ccccc3)n2)cc1",
        "COc1ccc(-c2c(C)n[nH]c2-c2ccc(OCCN3CCC(C(N)=O)CC3)cc2O)cc1",
        "Cn1c2nc(N3CCN(C(=O)CN4CCCC4)CC3)[nH]c2c(=O)n(C)c1=O",
        "CC1CCN(c2nc3c(c(=O)n(C)c(=O)n3C)n2CC(=O)c2ccc(Br)cc2)CC1",
        "COc1ccccc1N1CC[NH+](Cc2cc(=O)oc3cc([O-])c(Cl)cc23)CC1",
        "COc1ccc(-c2cc(C(=O)NCCC(=O)N3CCOCC3)c3ccccc3n2)cc1OC",
        "COC(=O)C1=C(C)N(c2ccc3c(c2)OCO3)C(=O)NC1c1c(F)cccc1Cl",
        "Cn1c2nc(N3CCN(CC(N)=O)CC3)n(Cc3ccc(Cl)cc3)c2c(=O)[nH]c1=O",
        "CCC(C)Cc1c(C)sc2nc(CNc3ccc(NC(C)=O)c(Cl)c3)[nH]c(=O)c21",
        "CCOc1ccc(NS(=O)(=O)c2cc(NC(=O)Cc3c(F)cccc3Cl)ccc2OC)cc1",
        "COC(=O)C1CCN(c2nc3ccccc3nc2C(C#N)C(=O)NCc2ccccc2)CC1",
        "Cc1ccc(S(=O)(=O)N(CC(=O)Nc2ccccc2C(=O)O)c2cccc(Cl)c2)cc1",
        "CCC1(c2ccc(NC(=O)c3cc(OC)c(OC)c(OC)c3)cc2)CCC(=O)N=C1O",
        "CSCc1nnc2c3ccccc3c(-c3ccc(C)c(S(=O)(=O)N(C)CCO)c3)nn12",
        "COc1cc(OC)c2c(C)c(CC(=O)NCC3(N4CCN(C)CC4)CCCCC3)c(=O)oc2c1",
        "COc1ccc2[nH]cc(C(C(=O)O)N3CCN(Cc4cccc5ccccc54)CC3)c2c1",
        "O=C(O)Cn1cc(C(C(=O)O)N2CCN(Cc3cccnc3)CC2)c2ccc(Br)cc21",
        "COC(=O)CN1CCN(c2nc3c(c(=O)[nH]c(=O)n3C)n2Cc2ccccc2Cl)CC1",
        "Cc1c(C)n(CCCN2CCOCC2)c2ncn3nc(COc4ccc(Cl)cc4)nc3c12",
        "CCc1nc2c(OCc3ccc(C(F)(F)F)cc3)cccn2c1N(C)C(=O)c1ccc(C)cc1",
        "CCc1nc2c(OCc3ccc(C(F)(F)F)cc3)cccn2c1N(C)C(=O)C1CC1",
        "COc1cccc(-c2nn(-c3ccccc3)cc2C(=O)NCC(C)(C)N2CCOCC2)c1",
        "COc1ccc2c(COC(=O)CCCNC3=NS(=O)(=O)c4ccccc43)cc(=O)oc2c1",
        "Cn1c2nc(N3CCN(c4ccccc4)CC3)n(Cc3ccccc3Cl)c2c(=O)n(C)c1=O",
        "CCCCN(C)C(=O)Cn1c2ccccc2nc1C1CC(=O)N(c2cccc(C)c2)C1",
        "O=C(Nc1nc(-c2ccc(F)c(F)c2)cs1)C1CCCN(C(=O)c2ccoc2)C1",
        "O=c1[nH]c(CN(Cc2cccs2)CC2CCCO2)nc2scc(-c3ccccc3)c21",
        "Cc1cccc(OCC(=O)Nc2nc(-c3ccc(S(=O)(=O)N4CCOCC4)cc3)cs2)c1",
        "O=C(CSc1nnc(-c2ccc(S(=O)(=O)N3CCCCCC3)cc2)o1)c1ccc(Cl)cc1",
        "Cc1nn(-c2ncnc3[nH]cnc32)c2c1C(c1c(Cl)cccc1Cl)SCC(O)=N2",
        "O=C(O)C(c1c[nH]c2cc(Br)ccc21)N1CCN(c2ncc(Br)cn2)CC1",
        "COCc1nn2c(c1-c1ccccc1)N=C(O)CC2C(=O)Nc1ccccc1C(F)(F)F",
        "Cc1[nH]n(-c2nc3ccccc3[nH]2)c(=O)c1C1OC(=O)c2ccccc21",
        "CN1C(=O)COc2ccc(NC(=O)C(=O)NC3CC(C)(C)NC(C)(C)C3)cc21",
        "COc1ccc2c(C)c(CC(=O)NC3CC(C)(C)NC(C)(C)C3)c(=O)oc2c1OC",
        "COc1ccccc1CCNS(=O)(=O)c1ccc(N2C(=O)C(C)(C)CS2(=O)=O)cc1",
        "CC(=O)Nc1ccc(NC(=O)CNC(=O)c2ccc3nc(-n4cccc4)sc3c2)cc1",
        "COc1ccc2[nH]cc(C(C(=O)O)N3CCN(C4=Nc5ccccc5Oc5ccccc54)CC3)c2c1",
        "CN(C)CCNC(=O)CN1CCN(C(C(=O)O)c2c[nH]c3cc(Br)ccc32)CC1",
        "Cc1cc(N2CCN(C(C(=O)O)c3c[nH]c4ccc(CO)cc43)CC2)nc2ccccc12",
        "COc1ccc2c(c1)c(C(C(=O)O)N1C[C@H](C)C[C@H](C)C1)cn2CC(=O)O",
        "Cc1cccc(NC(=O)CSc2nc3c(c(=O)[nH]2)CN(Cc2ccccc2)CC3)c1",
        "COc1cc2c(=O)[nH]c3c(C(=O)Nc4ccccc4C)sc(=S)n3c2cc1OC",
        "COc1cc(CNCCc2c[nH]c3ccccc23)ccc1OCc1ccc2c(c1)n(C)c(=O)n2C",
        "COc1cc(C(=O)N2CCN(C(=O)OC(C)(C)C)CC2)ccc1OCc1ccccc1",
        "NC(=O)COc1ccc(NC(=O)c2nn(Cc3ccccc3)c(=O)c3ccccc32)cc1",
        "COc1ccc(C)cc1CN1CCN(C(=O)c2cc(C3CC3)nn2-c2ccccc2)CC1",
        "CCOc1ccc(/C=C/c2nc(C#N)c(N3CCN(Cc4ccccc4)CC3)o2)cc1",
        "CCOc1ccc(N(C(C)C(=O)NCc2ccc(Cl)cc2Cl)S(C)(=O)=O)cc1",
        "COc1ccc(Cc2nc(P(=O)(OC)OC)c(NCc3ccc4c(c3)OCO4)o2)cc1",
        "COc1ccc(-c2coc3cc(OCC(=O)NCc4ccc(F)cc4)ccc3c2=O)cc1OC",
        "COc1ccccc1Oc1c(C)oc2cc(OCC(=O)Nc3ccc(C)cc3C)ccc2c1=O",
        "COc1ccc2c(c1)c(C(C(=O)O)N1CCN(Cc3ccccc3)CC1)cn2CC(=O)O",
        "Cn1c(=O)cc(CNC(=O)C(Cc2ccccc2)NC(=O)Cc2ccc(Cl)cc2)n(C)c1=O",
        "Cc1cc(=O)n2nc(-c3ccccc3Cl)nc2n1CC(=O)N1CCc2ccccc2C1",
        "CC(C)N1CCc2nc(NC(=O)c3ccc(S(=O)(=O)N4CCOCC4)cc3)sc2C1",
        "COc1ccc(-c2nn3c(nc(CNC(=O)c4cccc(Cl)c4)cc3=O)s2)cc1",
        "COc1ccc(Nc2nc(Nc3ccc(OC)cc3OC)nc(N3CCN(C)CC3)n2)c(OC)c1",
        "CCc1cc(CC)n(-c2nc3c(c(=O)[nH]c(=O)n3C)n2Cc2ccccc2C)n1",
        "CCOc1ccc(Cn2c(=O)c3sccc3n(CC(=O)NCc3ccc4c(c3)OCO4)c2=O)cc1",
        "Cc1cc(NC(=O)Cn2c3ccsc3c(=O)n(-c3ccc(C)c(C)c3)c2=O)no1",
        "CCOC(=O)C(NC(=O)c1ccc(F)cc1)c1c(C)nn(-c2ccc(F)cc2)c1C",
        "CC1CCN(S(=O)(=O)c2ccc(C(=O)Nc3nc(-c4ccccc4)cs3)cc2)CC1",
        "CC(C)Oc1ccc(C(=O)N2CCN(c3nc4sc5c(c4c(=O)n3C)CCCC5)CC2)cc1",
        "Cc1oc2cc(OCC(=O)N[C@@H](CC(C)C)C(=O)O)ccc2c(=O)c1-c1ccc(Cl)cc1",
        "Cc1c(C)c(=O)oc2cc(OCC(=O)NCCc3ccc(S(N)(=O)=O)cc3)ccc12",
        "COc1ccc2c(C)c(CC(=O)N3CCN(c4ccccn4)CC3)c(=O)oc2c1OC",
        "CCN(CC)CCCNc1oc(-c2ccco2)nc1S(=O)(=O)c1ccc2c(c1)OCCO2",
        "CC(C)(C)OC(=O)N1CCN(C(C(=O)O)c2c[nH]c3cc(NC(=O)c4ccccc4)ccc32)CC1",
        "COc1ccc2c(C)c(CC(=O)/N=c3\\[nH]nc(C(C)(C)C)s3)c(=O)oc2c1OC",
        "COC(=O)[C@H](CC(C)C)NC(=O)Nc1ccc2oc(-c3cccnc3)nc2c1",
        "CC(=O)c1ccc(NC(=O)[C@@]2(CC(=O)O)CC(c3ccc(F)cc3)=NO2)cc1",
        "COc1cc(OC)c2c(C)c(CC(=O)Nc3nc4ccccc4[nH]3)c(=O)oc2c1",
        "CC(NS(=O)(=O)/C=C/c1ccccc1)C(=O)N(C)Cc1cnn(Cc2ccccc2)c1",
        "COc1ccccc1N1CCN(C(=O)C2CCCN(S(=O)(=O)c3cc4c(cc3C)NC(=O)CO4)C2)CC1",
        "CC(=O)Nc1ccc(C(=O)Nc2nnc(C3CC(=O)N(c4ccc(C)c(Cl)c4)C3)s2)cc1",
        "Cc1ccc2nc(O)c(-c3noc(CCCC(=O)Nc4ccc(C)c(Cl)c4)n3)cc2c1",
        "COc1cc(C(=O)OC(C)C(=O)Nc2c(C)n(C)n(-c3ccccc3)c2=O)cc(Cl)c1OC",
        "COc1ccc(-c2nc3n(n2)C(c2ccc(Cl)cc2)C2=C(CC(C)(C)CC2=O)N3)cc1",
        "CC(NC(=O)CCc1ncc(-c2ccccc2Cl)o1)c1ccc(S(N)(=O)=O)cc1",
        "Cc1cccc(NC(=O)CN2CCN(C(=O)CCNS(=O)(=O)c3ccc(F)cc3)CC2)c1C",
        "Cc1ccc2c(=O)cc(C(=O)Nc3ccc(S(=O)(=O)Nc4nc(C)cc(C)n4)cc3)oc2c1",
        "CCCN1c2ccccc2C2(C1=O)C(C(=O)c1ccc(C)o1)=C(O)C(=O)N2CCN(C)C",
        "Cc1ccc(C(=O)N2CCC(C(=O)N3CCCCCC3)CC2)cc1S(=O)(=O)N1CCCCC1",
        "CC(=O)N(c1nc(CSc2nnc(C3CC3)n2C2CC2)cs1)c1ccc(C)cc1C",
        "N#Cc1nc(-c2ccc(S(=O)(=O)N3CCCC3)cc2)oc1N1CCN(c2cccc(Cl)c2)CC1",
        "CC(=O)c1cccc(Nc2nc3ccccc3nc2NS(=O)(=O)c2cc(C)ccc2C)c1",
        "CC(C)(C)c1ccc(-c2cc(=O)c3cc(NC(=O)c4ccncc4)ccc3o2)cc1",
        "Cc1nn(-c2cc(N3CCN(C(=O)c4ccc(F)cc4)CC3)ccc2[N+](=O)[O-])c(=O)c2ccccc12",
        "Cc1c(C(=O)Nc2ccc3oc(-c4cncc(Br)c4)nc3c2)cccc1[N+](=O)[O-]",
        "Cc1cc(C)c2oc(C(=O)Nc3ccc(S(=O)(=O)Nc4ncccn4)cc3)cc(=O)c2c1",
        "CC(O)CNc1ccc(-c2n[nH]c(=O)c3ccccc32)cc1[N+](=O)[O-]",
        "COc1cc(-c2nc3c(s2)CN(C(C(=O)O)c2c[nH]c4cc(F)ccc42)CC3)cc(OC)c1OC",
        "CCN1CCN(C(C(=O)O)c2c[nH]c3cc(NC(=O)CCC(=O)O)ccc32)CC1",
        "NC(=O)[C@@H]1CCCN1C(C(=O)O)c1cn(CCC(=O)O)c2cc(F)ccc12",
        "COc1ccc2c(c1)c(C(C(=O)O)N1CCN(Cc3cccnc3)CC1)cn2CCC(=O)O",
        "COc1ccc(OC)c(CCNC(=O)c2ccc(Cl)c(N3C(=O)CCS3(=O)=O)c2)c1",
        "COc1ccc2c(c1)c(C(C(=O)O)N1CCN(Cc3ccccc3)CC1)cn2CCC(=O)O",
        "COc1cccc(-c2ccc(=O)n(CC(=O)Nc3cc(Cl)c(OC)cc3OC)n2)c1",
        "CC(C)Cn1c2nn(CC(=O)NC(C)(C)C)c(=O)n2c2cc(C(=O)NC3CCCCC3)ccc2c1=O",
        "CCC(C)NC(=O)c1ccc2c(c1)n1c(=O)n(CC(=O)NCc3ccc(Cl)cc3)nc1n(C)c2=O",
        "CC1(C)CS(=O)(=O)N(c2ccc(C(=O)NCCc3c[nH]c4ccccc34)cc2)C1=O",
        "CCC(C)NC(=O)c1ccc2c(c1)n1c(=O)n(CC(=O)N(C)c3ccccc3)nc1n(C1CC1)c2=O",
        "CC(=O)O.Oc1ccc(-c2nc(-c3ccccc3)c(-c3ccccc3)[nH]2)cc1O",
        "Cc1nn(-c2nc3c(c(=O)n(CC(=O)C(C)(C)C)c(=O)n3C)n2CC(C)C)c(C)c1C",
        "COc1ccc(S(=O)(=O)NC2CC2)cc1NC(=O)c1n[nH]c(=O)c2ccccc21",
        "CCOCCn1c(=O)c2c(nc(-n3nc(C)cc3C)n2Cc2ccc(C)cc2)n(C)c1=O",
        "COc1ccc(C2(C)NC(=O)N(Cc3nc(-c4cccs4)oc3C)C2=O)cc1OC",
        "CCc1nnc2sc(-c3ccc(CNC(=O)C(C)(C)Oc4ccc(Cl)cc4)cc3)nn12",
        "CCCOC(=O)c1ccc(Oc2c(C)oc3cc(OC(=O)C(C)C)ccc3c2=O)cc1",
        "COc1ccc(OC)c(NC(=O)Cn2cccc(-c3nc(-c4ccc(OC)c(OC)c4)no3)c2=O)c1",
        "Cc1ccc(NC(=O)Cc2nnc(SCC(=O)NC34CC5CC(CC(C5)C3)C4)n2C)c(C)c1",
        "CCOc1ccc(-c2n[nH]c3c2C(c2c[nH]nc2-c2ccc(F)cc2)C(C#N)=C(N)O3)cc1",
        "CC1(C)CC(O)=C2C(=Nc3nc(N)[nH]c(=O)c3C2c2ccc(Cl)cc2)C1",
        "COc1ccc(-c2c(NC(=O)c3cc(OC)c(OC)c(OC)c3)oc3ccc(Cl)cc3c2=O)cc1",
        "CC(=O)Nc1ccc(NC(=O)COc2cc(O)c3c(=O)cc(-c4ccccc4)oc3c2)cc1",
        "COc1ccccc1Oc1c(C)oc2cc(OCC(=O)Nc3cccc(C)c3C)ccc2c1=O",
        "COc1cc2cc(C(=O)N[C@@H](C)C(=O)Nc3ccc4c(c3)OCO4)n(C)c2c(OC)c1OC",
        "COc1ccc(-c2nc3c(s2)CN(C(C(=O)O)c2c[nH]c4cc(F)ccc42)CC3)cc1"
    };
    
}
