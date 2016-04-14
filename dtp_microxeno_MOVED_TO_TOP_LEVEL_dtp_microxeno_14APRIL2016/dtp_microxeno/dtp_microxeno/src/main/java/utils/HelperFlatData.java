/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import mwk.microxeno.domain.FlatData;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import mwk.microxeno.vo.PassageVO;
import org.hibernate.criterion.Disjunction;

/**
 *
 * @author mwkunkel
 */
public class HelperFlatData {

    long startTime = 0;
    long fetchTime = 0;
    long fetchElapsed = 0;
    long translateTime = 0;
    long translateElapsed = 0;

    public List<PassageVO> searchAffymetrixData(
            List<String> probeSetIdList,
            List<String> geneList,
            List<String> tumorList,
            List<String> passageList) {

        if (probeSetIdList == null) {
            probeSetIdList = new ArrayList<String>();
        }

        if (geneList == null) {
            geneList = new ArrayList<String>();
        }
        if (tumorList == null) {
            tumorList = new ArrayList<String>();
        }
        if (passageList == null) {
            passageList = new ArrayList<String>();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<PassageVO> voList = new ArrayList<PassageVO>();

        System.out.println("Starting query in HelperFlatData");
        startTime = System.currentTimeMillis();

        try {

            Criteria c = session.createCriteria(FlatData.class);
            
            c.setMaxResults(10000);

            if (!probeSetIdList.isEmpty() && !geneList.isEmpty()) {

                Disjunction d = Restrictions.disjunction();
                d.add(Restrictions.in("probeSetId", probeSetIdList));
                d.add(Restrictions.in("geneSymbol", geneList));
                c.add(d);

            } else {

                if (!probeSetIdList.isEmpty()) {
                    c.add(Restrictions.in("probeSetId", probeSetIdList));
                }

                if (!geneList.isEmpty()) {
                    c.add(Restrictions.in("geneSymbol", geneList));
                }

            }

            if (!tumorList.isEmpty()) {
                c.add(Restrictions.in("tumorName", tumorList));
            }
            if (!passageList.isEmpty()) {
                c.add(Restrictions.in("passage", passageList));
            }

            List<FlatData> entityList = (List<FlatData>) c.list();

            fetchTime = System.currentTimeMillis();
            fetchElapsed = fetchTime - startTime;

            System.out.println("Finished query in HelperFlatData in: " + fetchElapsed + " msec");

            System.out.println("Size of c.list(): " + entityList.size());

            System.out.println("Starting translate in HelperFlatData");

            voList = TranslateFlatData.translateFlatDatas(entityList);

            translateTime = System.currentTimeMillis();
            translateElapsed = translateTime - fetchTime;
            System.out.println("Finished translate in HelperFlatData in: " + translateElapsed + " msec");

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return voList;

    }
}
