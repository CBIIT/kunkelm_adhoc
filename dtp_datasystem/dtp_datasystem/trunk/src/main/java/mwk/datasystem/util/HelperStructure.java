/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    public static List<Integer> findNSCsByExactMatch(String smiles) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            String sqlQuery = "select nsc from rdkit_mol where '" + smiles + "' @= mol";

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

            String sqlQuery = "select nsc from rdkit_mol  where '" + substructureSmiles + "' <@ mol";

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

    public static List<Integer> findNSCsByCtabSubstructure(String substrcutureCtab) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

// select rdk.nsc 
// from rdkit_mol rdk, rs3_from_plp_frags frags 
// where frags.nsc = 743380
// --and mol_from_ctab(frags.ctab) <@ rdk.mol;
            
            String sqlQuery = "select nsc from rdkit_mol where mol_from_ctab(" + substrcutureCtab + "::cstring) <@ mol";

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

    
    public static List<Integer> findNSCsBySmartsSubstructure(String substructureSmiles) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            String sqlQuery = "select nsc from rdkit_mol  where cast('" + substructureSmiles + "' as qmol) <@ mol";

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

    public static List<Integer> findNSCsByDiceSimilarity(String structureSmiles, Double similarityCutoff) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            String sqlQuery = "set rdkit.dice_threshold = " + similarityCutoff.toString();

            tx = session.beginTransaction();
            Query q = session.createSQLQuery(sqlQuery);
            q.executeUpdate();

            sqlQuery = "select nsc from fps, rdkit_mol where morganbv_fp('" + structureSmiles + "',2) # morganbv and fps.id = rdkit_mol.id";

            q = session.createSQLQuery(sqlQuery);
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

    public static List<Integer> findNSCsByTanimotoSimilarity(String structureSmiles, Double similarityCutoff) {

        ArrayList<Integer> nscList = new ArrayList<Integer>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();


            String sqlQuery = "set rdkit.tanimoto_threshold = " + similarityCutoff.toString();

            tx = session.beginTransaction();
            Query q = session.createSQLQuery(sqlQuery);
            q.executeUpdate();

            sqlQuery = "select nsc from fps, rdkit_mol where morganbv_fp('" + structureSmiles + "',2) % morganbv and fps.id = rdkit_mol.id";

            q = session.createSQLQuery(sqlQuery);
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