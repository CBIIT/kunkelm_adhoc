/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import mwk.d3.Info;
import mwk.d3.Node;
import mwk.d3.TanimotoForceGraph;
import mwk.d3.TanimotoLink;
import mwk.datasystem.domain.TanimotoScores;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import mwk.datasystem.vo.TanimotoScoresVO;

/**
 *
 * @author mwkunkel
 */
public class HelperTanimotoScores {

  /*
   insert into tanimoto_scores 
   select 
   nextval('tan_seq'), uno, duo, atompairbv_fp_tanimoto, featmorganbv_fp_tanimoto, layered_fp_tanimoto, maccs_fp_tanimoto, morganbv_fp_tanimoto, rdkit_fp_tanimoto, torsionbv_fp_tanimoto
   from temp_tanimoto_collate
   where uno < duo;
   */
  public static ArrayList<TanimotoScoresVO> fetch() {

    System.out.println("In fetch in HelperTanimotoScores.");

    ArrayList<TanimotoScoresVO> rtnList = new ArrayList<TanimotoScoresVO>();

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();

      Criteria crit = session.createCriteria(TanimotoScores.class);

      List<TanimotoScores> entityList = (List<TanimotoScores>) crit.list();

      if (!entityList.isEmpty()) {

        for (TanimotoScores t : entityList) {
          rtnList.add(TransformAndroToVO.translateTanimotoScores(t));
        }

      }

    } catch (Exception e) {
      tx.rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }

    return rtnList;

  }

}
