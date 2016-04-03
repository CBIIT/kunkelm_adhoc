/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import mwk.microxeno.domain.AffymetrixData;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import mwk.microxeno.vo.PassageVO;

/**
 *
 * @author mwkunkel
 */
public class HelperAffymetrixData {
    
    long startTime = 0;
    long fetchTime = 0;
    long fetchElapsed = 0;
    long translateTime = 0;
    long translateElapsed = 0;

    public List<PassageVO> searchAffymetrixData(
            List<String> geneList,
            List<String> tumorList) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<PassageVO> voList = new ArrayList<PassageVO>();

        System.out.println("Starting query in HelperAffymetrixData");
        startTime = System.currentTimeMillis();
        
        try {

            Criteria c = session.createCriteria(AffymetrixData.class)
                    .createAlias("affymetrixIdentifier", "ai")
                    .createAlias("tumor", "tum");

            if (!tumorList.isEmpty()) {
                c.add(Restrictions.in("tum.tumorName", tumorList));
            }
            if (!geneList.isEmpty()) {
                c.add(Restrictions.in("ai.geneSymbol", geneList));
            }

            List<AffymetrixData> entityList = (List<AffymetrixData>) c.list();
            
            fetchTime = System.currentTimeMillis();
            fetchElapsed = fetchTime - startTime;
            
            System.out.println("Finished query in HelperAffymetrixData in: " + fetchElapsed + " msec");
            
            System.out.println("Size of c.list(): " + entityList.size());
            
            System.out.println("Starting translate in HelperAffymetrixData");
            
            voList = TranslateAndroToVO.translateAffymetrixDatas(entityList);
            
            translateTime = System.currentTimeMillis();
            translateElapsed = translateTime - fetchTime;
            System.out.println("Finished translate in HelperAffymetrixData in: " + translateElapsed + " msec");
            
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
