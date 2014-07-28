/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.DataSystemUser;
import mwk.datasystem.vo.DataSystemUserVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class HelperDataSystemUser {
  
    public DataSystemUserVO getDataSystemUserByName(String userName) {

        DataSystemUserVO rtn = new DataSystemUserVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria dsuCrit = session.createCriteria(DataSystemUser.class);
            dsuCrit.add(Restrictions.eq("userName", userName));
            DataSystemUser dsuEntity = (DataSystemUser) dsuCrit.uniqueResult();

            rtn = TransformAndroToVO.translateDataSystemUser(dsuEntity);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }
    
}