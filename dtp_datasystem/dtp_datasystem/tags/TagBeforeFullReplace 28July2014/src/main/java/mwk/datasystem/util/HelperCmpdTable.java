/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdTableVO;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdTable {

    public static final Boolean DEBUG = Boolean.FALSE;
    
    public static List<CmpdVO> adHocCmpdsToCmpdTable(List<AdHocCmpd> entityList) {

        List<CmpdVO> rtn = new ArrayList<CmpdVO>();

        Session session = null;
        Transaction tx = null;

        try {

            List<CmpdTable> ctList = new ArrayList<CmpdTable>();

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            for (AdHocCmpd ahc : entityList) {

                CmpdTable ct = CmpdTable.Factory.newInstance();

                AdHocCmpdFragmentPChem ahcfpc = ahc.getAdHocCmpdParentFragment().getAdHocCmpdFragmentPChem();

                ct.setMw(ahcfpc.getMw());
                ct.setMf(ahcfpc.getMf());
                ct.setAlogp(ahcfpc.getAlogp());
                ct.setLogd(ahcfpc.getLogd());
                ct.setHba(ahcfpc.getHba());
                ct.setHbd(ahcfpc.getHbd());
                ct.setSa(ahcfpc.getSa());
                ct.setPsa(ahcfpc.getPsa());

                AdHocCmpdFragmentStructure ahcfps = ahc.getAdHocCmpdParentFragment().getAdHocCmpdFragmentStructure();

                ct.setSmiles(ahcfps.getSmiles());
                ct.setInchi(ahcfps.getInchi());
                ct.setMol(ahcfps.getMol());
                ct.setInchiAux(ahcfps.getInchiAux());
                ct.setName(ahc.getName());

                // nulls for AdHocCmpds

//            cv.setNscCmpdId(ahc.getNscCmpdId());
//            cv.setPrefix(ahc.getPrefix());
//            cv.setNsc(ahc.getNsc());
//            cv.setConf(ahc.getConf());
//            cv.setDistribution(ahc.getDistribution());
//            cv.setCas(ahc.getCas());
//            cv.setNci60(ahc.getNci60());
//            cv.setHf(ahc.getHf());
//            cv.setXeno(ahc.getXeno());

                ct.setCmpdOwner(ahc.getCmpdOwner());
                ct.setAdHocCmpdId(ahc.getAdHocCmpdId());

                if (DEBUG) {
                    System.out.println("In HelperCmpdTable.adHocCmpdsToCmpdTable. Setting originalAdHocCmpdId to: " + ahc.getOriginalAdHocCmpdId());
                }
                ct.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());

//            cv.setTargets(ahc.getTargets());
//            cv.setSets(ahc.getSets());
//            cv.setProjects(ahc.getProjects());
//            cv.setPlates(ahc.getPlates());
//            cv.setAliases(ahc.getAliases());

                ct.setId(ahc.getId());

//            cv.setInventory(ahc.getInventory());

                session.merge(ct);

                ctList.add(ct);

            }

            tx.commit();

            for (CmpdTable ct : ctList) {

                CmpdVO ctVO = TransformCmpdTableToVO.toCmpdVO(ct);

                rtn.add(ctVO);

//                System.out.println(ctVO.toString());

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

            }

        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return rtn;

    }
}