/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author mwkunkel
 */
public class HelperStructure {

    public final static String limitClause = " limit 2000";

    public static List<Integer> findNSCsByExactMatch(String smiles) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            
            String sqlQuery = "select nsc from rdkit_mol where mol_from_smiles('" + smiles + "') @= mol " + limitClause;

            tx = session.beginTransaction();
            Query q = session.createSQLQuery(sqlQuery);
            List resultList = q.list();
            nscList = new ArrayList<Integer>(resultList);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return nscList;
    }

    public static List<Integer> findNSCsBySmilesSubstructure(String substructureSmiles) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

//            String sqlQuery = "select nsc from rdkit_mol  where '" + substructureSmiles + "'::mol <@ mol " + limitClause;
            String sqlQuery = "select nsc from rdkit_mol  where substruct(mol, '" + substructureSmiles + "') " + limitClause;

            tx = session.beginTransaction();
            Query q = session.createSQLQuery(sqlQuery);
            List resultList = q.list();
            nscList = new ArrayList<Integer>(resultList);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return nscList;
    }

    public static List<Integer> findNSCsByCtabSubstructure(String substructureCtab) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            String sqlQuery = "select nsc from rdkit_mol  where substruct(mol, mol_from_ctab(cast('" + substructureCtab + "' as cstring)) " + limitClause;
            
            tx = session.beginTransaction();
            Query q = session.createSQLQuery(sqlQuery);
            List resultList = q.list();
            nscList = new ArrayList<Integer>(resultList);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return nscList;
    }

}
