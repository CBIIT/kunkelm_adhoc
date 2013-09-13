/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.structureservlet.util;

import mwk.structureservlet.androdomain.Structure;
import mwk.structureservlet.vo.StructureVO;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class HelperStructure {

  Session session = null;

  public HelperStructure() {
    this.session = HibernateUtil.getSessionFactory().getCurrentSession();
  }

  public StructureVO findStructureByNsc(Integer nsc) {

    Transaction tx = null;

    StructureVO rtn = new StructureVO();

    try {

      tx = session.beginTransaction();

      Criteria c = session.createCriteria(Structure.class)
              .add(Restrictions.eq("nsc", nsc));

      Structure entity = (Structure) c.uniqueResult();

      if (entity != null) {
          rtn.setNsc(entity.getNsc());
          rtn.setSmiles(entity.getSmiles());
      }
      
      tx.commit();

    } catch (Exception e) {
      tx.rollback();
      e.printStackTrace();
    }

    return rtn;

  }

  public List<Integer> findNSCsByExactMatch(String smiles) {

    String sqlQuery = "select nsc from rdkit_mol where '" + smiles + "' @= mol";

    Transaction tx = session.beginTransaction();
    Query q = session.createSQLQuery(sqlQuery);
    List resultList = q.list();
    ArrayList<Integer> nscList = new ArrayList<Integer>(resultList);

    tx.commit();

    return nscList;
  }

  public List<Integer> findNSCsBySmilesSubstructure(String substructureSmiles) {

    String sqlQuery = "select nsc from rdkit_mol  where '" + substructureSmiles + "' <@ mol";

    Transaction tx = session.beginTransaction();
    Query q = session.createSQLQuery(sqlQuery);
    List resultList = q.list();
    ArrayList<Integer> nscList = new ArrayList<Integer>(resultList);

    tx.commit();

    return nscList;
  }

  public List<Integer> findNSCsBySmartsSubstructure(String substructureSmiles) {

    String sqlQuery = "select nsc from rdkit_mol  where cast('" + substructureSmiles + "' as qmol) <@ mol";

    Transaction tx = session.beginTransaction();
    Query q = session.createSQLQuery(sqlQuery);
    List resultList = q.list();
    ArrayList<Integer> nscList = new ArrayList<Integer>(resultList);

    tx.commit();

    return nscList;
  }

  public List<Integer> findNSCsByDiceSimilarity(String structureSmiles, Double similarityCutoff) {

    String sqlQuery = "set rdkit.dice_threshold = " + similarityCutoff.toString();

    Transaction tx = session.beginTransaction();
    Query q = session.createSQLQuery(sqlQuery);
    q.executeUpdate();

    sqlQuery = "select nsc from fps, rdkit_mol where morganbv_fp('" + structureSmiles + "',2) # morganbv and fps.id = rdkit_mol.id";

    q = session.createSQLQuery(sqlQuery);
    List resultList = q.list();
    ArrayList<Integer> nscList = new ArrayList<Integer>(resultList);

    tx.commit();

    return nscList;
  }

  public List<Integer> findNSCsByTanimotoSimilarity(String structureSmiles, Double similarityCutoff) {

    String sqlQuery = "set rdkit.tanimoto_threshold = " + similarityCutoff.toString();

    Transaction tx = session.beginTransaction();
    Query q = session.createSQLQuery(sqlQuery);
    q.executeUpdate();

    sqlQuery = "select nsc from fps, rdkit_mol where morganbv_fp('" + structureSmiles + "',2) % morganbv and fps.id = rdkit_mol.id";

    q = session.createSQLQuery(sqlQuery);
    List resultList = q.list();
    ArrayList<Integer> nscList = new ArrayList<Integer>(resultList);

    tx.commit();

    return nscList;
  }
}