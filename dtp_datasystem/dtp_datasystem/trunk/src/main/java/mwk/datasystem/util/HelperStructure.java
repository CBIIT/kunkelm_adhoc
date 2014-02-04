/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.androdomain.Cmpd;
import mwk.datasystem.androdomain.CmpdList;
import mwk.datasystem.androdomain.CmpdListMember;
import mwk.datasystem.androdomain.CmpdView;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
public class HelperStructure {

  Session session = null;

  public HelperStructure() {
    this.session = HibernateUtil.getSessionFactory().getCurrentSession();
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