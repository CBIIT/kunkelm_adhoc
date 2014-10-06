/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdAlias;
import mwk.datasystem.domain.CmpdFragment;
import mwk.datasystem.domain.CmpdPlate;
import mwk.datasystem.domain.CmpdProject;
import mwk.datasystem.domain.CmpdSet;
import mwk.datasystem.domain.CmpdTarget;
import mwk.datasystem.domain.NscCmpd;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.TransformAndroToVO;
import mwk.datasystem.vo.CmpdAliasVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdPlateVO;
import mwk.datasystem.vo.CmpdProjectVO;
import mwk.datasystem.vo.CmpdSetVO;
import mwk.datasystem.vo.CmpdTargetVO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class SCRIPT7_gsonFragments {

    public static void main(String[] args) {

        makeJsons();

    }

    public static void makeGsonFragments() {

        System.out.println("In makeGsonFragments.");

        // Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new Gson();

        File fragFile = null;
        FileWriter fragWriter = null;

        Session session = null;
        Transaction tx = null;

        try {

            fragFile = new File("/tmp/fragmentJson_part2.tab");
            fragWriter = new FileWriter(fragFile);

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // all cmpds

            Criteria cmpdCrit = session.createCriteria(Cmpd.class);
            cmpdCrit.add(Restrictions.le("nsc", 1000));

            Query q = session.createQuery("from CmpdFragmentImpl where id >= 400000");

            ScrollableResults entities = q
                    .setFetchSize(Integer.valueOf(1000))
                    .setReadOnly(true)
                    .setCacheable(false)
                    .scroll(ScrollMode.FORWARD_ONLY);

            int entCnt = 0;

            while (entities.next()) {

                entCnt++;

                CmpdFragment cf = (CmpdFragment) entities.get()[0];
                CmpdFragmentVO cfVO = TransformAndroToVO.toCmpdFragmentVO(cf);

                String json = gson.toJson(cfVO);

                fragWriter.write(cfVO.getId().toString());
                fragWriter.write("\t");
                fragWriter.write(json);
                fragWriter.write("\n");

                System.out.println("entCnt: " + entCnt + " fragId: " + cfVO.getId());

                if (entCnt % 1000 == 0) {
                    System.out.println("flush()");
                    fragWriter.flush();
                }

            }

            tx.commit();

            fragWriter.close();
            fragWriter = null;

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            if (fragWriter != null) {
                try {
                    fragWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void makeJsons() {

        System.out.println("In makeJsons");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        // Gson gson = new Gson();

        FileWriter fragW = null;
        FileWriter nscFragW = null;
        //
        FileWriter aliasW = null;
        FileWriter targetW = null;
        FileWriter setW = null;
        FileWriter projectW = null;
        FileWriter plateW = null;

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            fragW = new FileWriter("/tmp/fragJson.tab");
            nscFragW = new FileWriter("/tmp/nscFragJson.tab");
            aliasW = new FileWriter("/tmp/aliasJson.tab");
            targetW = new FileWriter("/tmp/targetJson.tab");
            setW = new FileWriter("/tmp/setJson.tab");
            projectW = new FileWriter("/tmp/projectJson.tab");
            plateW = new FileWriter("/tmp/plateJson.tab");

            // all cmpds

            int entCnt = 0;
            String jsonStr;

            ScrollableResults scrollableResults = 
                    session
                    .createQuery("from CmpdImpl")
                    .setReadOnly(true)
                    .setFetchSize(1000)
                    .scroll(ScrollMode.FORWARD_ONLY);

            while (scrollableResults.next()) {

                entCnt++;

                Cmpd cmpd = (Cmpd) scrollableResults.get(0);

                if (cmpd instanceof NscCmpdImpl) {

                    NscCmpd nscc = (NscCmpd) cmpd;

                    System.out.println("NSC is: " + nscc.getNsc());
                    System.out.println("entCnt: " + entCnt + " nsc: " + nscc.getNsc());

                    // fragments, both individual and BY NSC

                    ArrayList<CmpdFragmentVO> cfList = new ArrayList<CmpdFragmentVO>();

                    for (CmpdFragment cf : nscc.getCmpdFragments()) {

                        CmpdFragmentVO cfVO = TransformAndroToVO.toCmpdFragmentVO(cf);
                        cfList.add(cfVO);

                        String json = gson.toJson(cfVO);
                        fragW.write(cfVO.getId().toString());
                        fragW.write("\t");
                        fragW.write(json);
                        fragW.write("\n");

                    }

                    if (!cfList.isEmpty()) {
                        jsonStr = gson.toJson(cfList);
                        nscFragW.write(nscc.getNsc().toString());
                        nscFragW.write("\t");
                        nscFragW.write(jsonStr);
                        nscFragW.write("\n");
                    }

                    // aliases

                    ArrayList<CmpdAliasVO> aliasList = new ArrayList<CmpdAliasVO>();
                    for (CmpdAlias ca : nscc.getCmpdAliases()) {
                        CmpdAliasVO caVO = TransformAndroToVO.toCmpdAliasVO(ca);
                        aliasList.add(caVO);
                    }

                    if (!aliasList.isEmpty()) {
                        jsonStr = gson.toJson(aliasList);
                        aliasW.write(nscc.getNsc().toString());
                        aliasW.write("\t");
                        aliasW.write(jsonStr);
                        aliasW.write("\n");
                    }

                    // targets

                    ArrayList<CmpdTargetVO> targetList = new ArrayList<CmpdTargetVO>();
                    for (CmpdTarget ct : nscc.getCmpdTargets()) {
                        CmpdTargetVO ctVO = TransformAndroToVO.toCmpdTargetVO(ct);
                        targetList.add(ctVO);
                    }

                    if (!targetList.isEmpty()) {
                        jsonStr = gson.toJson(targetList);
                        targetW.write(nscc.getNsc().toString());
                        targetW.write("\t");
                        targetW.write(jsonStr);
                        targetW.write("\n");
                    }

                    // sets

                    ArrayList<CmpdSetVO> setList = new ArrayList<CmpdSetVO>();
                    for (CmpdSet cs : nscc.getCmpdSets()) {
                        CmpdSetVO csVO = TransformAndroToVO.toCmpdSetVO(cs);
                        setList.add(csVO);
                    }

                    if (!setList.isEmpty()) {
                        jsonStr = gson.toJson(setList);
                        setW.write(nscc.getNsc().toString());
                        setW.write("\t");
                        setW.write(jsonStr);
                        setW.write("\n");
                    }

                    // projects

                    ArrayList<CmpdProjectVO> projectList = new ArrayList<CmpdProjectVO>();
                    for (CmpdProject cp : nscc.getCmpdProjects()) {
                        CmpdProjectVO cpVO = TransformAndroToVO.toCmpdProjectVO(cp);
                        projectList.add(cpVO);
                    }

                    if (!projectList.isEmpty()) {
                        jsonStr = gson.toJson(projectList);
                        projectW.write(nscc.getNsc().toString());
                        projectW.write("\t");
                        projectW.write(jsonStr);
                        projectW.write("\n");
                    }

                    // plates

                    ArrayList<CmpdPlateVO> plateList = new ArrayList<CmpdPlateVO>();
                    for (CmpdPlate cp : nscc.getCmpdPlates()) {
                        CmpdPlateVO cpVO = TransformAndroToVO.toCmpdPlateVO(cp);
                        plateList.add(cpVO);
                    }

                    if (!plateList.isEmpty()) {
                        jsonStr = gson.toJson(plateList);
                        plateW.write(nscc.getNsc().toString());
                        plateW.write("\t");
                        plateW.write(jsonStr);
                        plateW.write("\n");
                    }

                }

                session.evict(cmpd);

            }

            tx.commit();

            fragW.close();
            fragW = null;

            nscFragW.close();
            nscFragW = null;

            aliasW.close();
            aliasW = null;

            targetW.close();
            targetW = null;

            setW.close();
            setW = null;

            projectW.close();
            projectW = null;

            plateW.close();
            plateW = null;


        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (fragW != null) {
                    fragW.close();
                }
                if (nscFragW != null) {
                    nscFragW.close();
                }
                if (aliasW != null) {
                    aliasW.close();
                }
                if (targetW != null) {
                    targetW.close();
                }
                if (setW != null) {
                    setW.close();
                }
                if (projectW != null) {
                    projectW.close();
                }
                if (plateW != null) {
                    plateW.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (tx != null) {
                tx.rollback();
            }

        } finally {

            try {
                if (session != null) {
                    session.close();
                }
                if (fragW != null) {
                    fragW.close();
                }
                if (nscFragW != null) {
                    nscFragW.close();
                }
                if (aliasW != null) {
                    aliasW.close();
                }
                if (targetW != null) {
                    targetW.close();
                }
                if (setW != null) {
                    setW.close();
                }
                if (projectW != null) {
                    projectW.close();
                }
                if (plateW != null) {
                    plateW.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
