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

import mwk.microxeno.vo.AffymetrixDataVO;

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

    public List<AffymetrixDataVO> searchAffymetrixData(
            List<String> geneList,
            List<String> tumorList) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<AffymetrixDataVO> voList = new ArrayList<AffymetrixDataVO>();

        System.out.println("Starting query in HelperFlatData");
        startTime = System.currentTimeMillis();
        
        try {

            Criteria c = session.createCriteria(FlatData.class);

            if (!tumorList.isEmpty()) {
                c.add(Restrictions.in("tumorName", tumorList));
            }
            if (!geneList.isEmpty()) {
                c.add(Restrictions.in("genecard", geneList));
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
