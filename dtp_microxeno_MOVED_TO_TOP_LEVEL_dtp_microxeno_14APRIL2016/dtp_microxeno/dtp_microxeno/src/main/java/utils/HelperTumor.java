/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import mwk.microxeno.domain.Tumor;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import mwk.microxeno.vo.TumorVO;

/**
 *
 * @author mwkunkel
 */
public class HelperTumor {

    public static List<TumorVO> fetchTumors() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<TumorVO> voList = new ArrayList<TumorVO>();

        try {

            Criteria c = session.createCriteria(Tumor.class);

            List<Tumor> entityList = (List<Tumor>) c.list();

            for (Tumor t : entityList) {
                TumorVO aiVO = TranslateAndroToVO.translateTumor(t);
                voList.add(aiVO);
            }

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
