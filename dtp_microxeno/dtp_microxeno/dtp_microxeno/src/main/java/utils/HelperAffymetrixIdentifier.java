/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import mwk.microxeno.domain.AffymetrixIdentifier;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import mwk.microxeno.vo.AffymetrixIdentifierVO;

/**
 *
 * @author mwkunkel
 */
public class HelperAffymetrixIdentifier {

    public static List<AffymetrixIdentifierVO> fetchAffymetrixIdentifiers() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<AffymetrixIdentifierVO> voList = new ArrayList<AffymetrixIdentifierVO>();

        try {

            Criteria c = session.createCriteria(AffymetrixIdentifier.class);

            List<AffymetrixIdentifier> entityList = (List<AffymetrixIdentifier>) c.list();

            for (AffymetrixIdentifier ai : entityList) {
                AffymetrixIdentifierVO aiVO = TranslateAndroToVO.translateAffymetrixIdentifier(ai);
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
