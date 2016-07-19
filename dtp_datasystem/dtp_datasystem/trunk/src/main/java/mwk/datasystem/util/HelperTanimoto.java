/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import mwk.datasystem.domain.TanimotoScores;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.TanimotoScoresVO;
import mwk.datasystem.vo.TanimotoScoresWithCmpdObjectsVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

/**
 *
 * @author mwkunkel
 */
public class HelperTanimoto {

    private static Random randGen;

    static {
        randGen = new Random();
    }

    /*
   insert into tanimoto_scores 
   select 
   nextval('tan_seq'), uno, duo, atompairbv_fp_tanimoto, featmorganbv_fp_tanimoto, layered_fp_tanimoto, maccs_fp_tanimoto, morganbv_fp_tanimoto, rdkit_fp_tanimoto, torsionbv_fp_tanimoto
   from temp_tanimoto_collate
   where uno < duo;
     */
    public static ArrayList<TanimotoScoresVO> fetch() {

        System.out.println("In HelperTanimotoScores.fetch");

        ArrayList<TanimotoScoresVO> rtnList = new ArrayList<TanimotoScoresVO>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria crit = session.createCriteria(TanimotoScores.class);

            List<TanimotoScores> entityList = (List<TanimotoScores>) crit.list();

            if (entityList != null && !entityList.isEmpty()) {
                for (TanimotoScores t : entityList) {
                    TanimotoScoresVO tsVO = TransformAndroToVO.translateTanimotoScores(t);
                    rtnList.add(tsVO);
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

    public static ArrayList<TanimotoScoresWithCmpdObjectsVO> fetch(TreeMap<Integer, CmpdVO> nscCmpdMap) {

        System.out.println("In HelperTanimotoScores.fetch");

        ArrayList<TanimotoScoresWithCmpdObjectsVO> rtnList = new ArrayList<TanimotoScoresWithCmpdObjectsVO>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria crit = session.createCriteria(TanimotoScores.class);

            List<TanimotoScores> entityList = (List<TanimotoScores>) crit.list();

            if (entityList != null && !entityList.isEmpty()) {
                for (TanimotoScores t : entityList) {
                    TanimotoScoresVO tsVO = TransformAndroToVO.translateTanimotoScores(t);

                    TanimotoScoresWithCmpdObjectsVO tswcoVO = new TanimotoScoresWithCmpdObjectsVO();

                    if (null != tsVO) {
                        tswcoVO.setCmpd1(nscCmpdMap.get(tsVO.getNsc1()));
                        tswcoVO.setCmpd2(nscCmpdMap.get(tsVO.getNsc2()));
                        tswcoVO.setAtomPairBv(tsVO.getAtomPairBv());
                        tswcoVO.setFeatMorganBv(tsVO.getFeatMorganBv());
                        tswcoVO.setLayered(tsVO.getLayered());
                        tswcoVO.setMaccs(tsVO.getMaccs());
                        tswcoVO.setMorganBv(tsVO.getMorganBv());
                        tswcoVO.setRdkit(tsVO.getRdkit());
                        tswcoVO.setTorsionBv(tsVO.getTorsionBv());
                    }

                    rtnList.add(tswcoVO);
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

    public static TreeMap<String, String> fpLookup = new TreeMap<String, String>();

    static {
        fpLookup.put("atompairbv_fp", "atomPairBv");
        fpLookup.put("featmorganbv_fp", "featMorganBv");
        fpLookup.put("layered_fp", "layered");
        fpLookup.put("maccs_fp", "maccs");
        fpLookup.put("morganbv_fp", "morganBv");
        fpLookup.put("rdkit_fp", "rdkit");
        fpLookup.put("torsionbv_fp", "torsionBv");
    }

//    public static ArrayList<TanimotoScoresVO> doTanimoto(final ArrayList<Integer> nscIntList) {
//
//        System.out.println("In HelperTanimotoScores.doTanimoto.");
//
//        ArrayList<TanimotoScoresVO> rtnList = new ArrayList<TanimotoScoresVO>();
//
//        Session session = null;
//        Transaction tx = null;
//
//        String sql = "";
//
//        try {
//
//            session = HibernateUtil.getSessionFactory().openSession();
//            tx = session.beginTransaction();
//
//            session.doWork(
//                    new Work() {
//                @Override
//                public void execute(Connection conn) throws SQLException {
//
//                    String sql = "";
//
//                    conn.setAutoCommit(true);
//
//                    Statement stmt = conn.createStatement();
//
//                    // table for nsc
//                    sql = "drop table if exists temp_nsc_for_tanimoto";
//                    System.out.println(sql);
//                    //-------------------------------------stmt.executeUpdate(sql);
//
//                    sql = "create table temp_nsc_for_tanimoto(nsc int)";
//                    System.out.println(sql);
//                    //-------------------------------------stmt.executeUpdate(sql);
//
//                    for (Integer i : nscIntList) {
//                        sql = "insert into temp_nsc_for_tanimoto(nsc) values(" + i + ")";
//                        System.out.println(sql);
//                        //-------------------------------------stmt.executeUpdate(sql);
//                    }
//
//                    // possible pairs
//                    sql = "drop table if exists temp_all_pair_for_tanimoto";
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    sql = "create table temp_all_pair_for_tanimoto "
//                            + " as "
//                            + " select u.nsc as uno, "
//                            + " d.nsc as duo "
//                            + " from temp_nsc_for_tanimoto u, "
//                            + " temp_nsc_for_tanimoto d "
//                            + " where u.nsc < d.nsc";
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    // fingerprints
//                    sql = "drop table if exists temp_fp_for_tanimoto";
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    StringBuilder sb = new StringBuilder();
//
//                    sb.append("create table temp_fp_for_tanimoto as \n");
//                    sb.append(" select temp_nsc_for_tanimoto.nsc\n");
//
//                    for (String fp : fpLookup.keySet()) {
//                        sb.append(", CASE \n");
//                        sb.append(" WHEN mol is null THEN null\n");
//                        sb.append(" WHEN " + fp + "(mol) is null THEN null \n");
//                        sb.append(" ELSE " + fp + "(mol) END \n");
//                        sb.append(" as " + fp + "\n");
//                    }
//
//                    sb.append(" from rdkit_mol, temp_nsc_for_tanimoto \n");
//                    sb.append(" where rdkit_mol.nsc = temp_nsc_for_tanimoto.nsc\n");
//
//                    sql = sb.toString();
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    // run the calculations
//                    sql = "drop table if exists temp_tanimoto";
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    sb = new StringBuilder();
//
//                    sb.append("create table temp_tanimoto as \n");
//                    sb.append(" select uno, duo \n");
//
//                    for (String fp : fpLookup.keySet()) {
//                        sb.append(", CASE \n");
//                        sb.append(" WHEN uno." + fp + " is null THEN null \n");
//                        sb.append(" WHEN duo." + fp + " is null THEN null \n");
//                        sb.append(" WHEN tanimoto_dist(uno." + fp + ", duo." + fp + ") is not null THEN \n");
//                        sb.append(" tanimoto_dist(uno." + fp + ", duo." + fp + ")\n");
//                        sb.append(" ELSE null END \n");
//                        sb.append(" as " + fp + "\n");
//                    }
//
//                    sb.append(" from temp_all_pair_for_tanimoto ap, temp_fp_for_tanimoto uno, temp_fp_for_tanimoto duo \n");
//                    sb.append(" where ap.uno = uno.nsc and ap.duo = duo.nsc\n");
//
//                    sql = sb.toString();
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    stmt.close();
//
//                }
//            });
//
//            // tx.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            tx.rollback();
//        } finally {
//            session.close();
//        }
//
//        return rtnList;
//
//    }
    public static ArrayList<TanimotoScoresWithCmpdObjectsVO> doTanimoto(final ArrayList<CmpdVO> cVOlist) {

        long randL = randGen.nextLong();
        while (randL < 0) {
            randL = randGen.nextLong();
        }
        String randStr = Long.valueOf(randL).toString();

        final String nscTableName = "temp_nsc_for_tanimoto_" + randStr;
        final String allPairTableName = "temp_all_pair_for_tanimoto_" + randStr;
        final String fpTableName = "temp_fp_for_tanimoto_" + randStr;
        final String tanimotoTableName = "temp_tanimoto_" + randStr;

        final ArrayList<TanimotoScoresWithCmpdObjectsVO> rtnList = new ArrayList<TanimotoScoresWithCmpdObjectsVO>();

        System.out.println("In HelperTanimotoScores.improvedDoTanimoto.");

        final TreeMap<Integer, CmpdVO> nscCmpdMap = new TreeMap<Integer, CmpdVO>();

        for (CmpdVO cVO : cVOlist) {
            if (cVO.getNsc() != null) {
                nscCmpdMap.put(cVO.getNsc(), cVO);
            }
        }

        Session session = null;
        Transaction tx = null;

        String sql = "";

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.doWork(
                    new Work() {
                @Override
                public void execute(Connection conn) throws SQLException {

                    String sql = "";

                    conn.setAutoCommit(true);

                    Statement stmt = conn.createStatement();

                    // table for nsc
                    sql = "drop table if exists " + nscTableName;
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    sql = "create table " + nscTableName + "(nsc int)";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    for (Integer i : nscCmpdMap.keySet()) {
                        sql = "insert into " + nscTableName + "(nsc) values(" + i + ")";
                        System.out.println(sql);
                        stmt.executeUpdate(sql);
                    }

                    // possible pairs
                    sql = "drop table if exists " + allPairTableName;
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    sql = "create table " + allPairTableName
                            + " as "
                            + " select u.nsc as uno, "
                            + " d.nsc as duo "
                            + " from " + nscTableName + " u, "
                            + " " + nscTableName + " d "
                            + " where u.nsc < d.nsc";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    // fingerprints
                    sql = "drop table if exists " + fpTableName;
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    StringBuilder sb = new StringBuilder();

                    sb.append("create table " + fpTableName + " as \n");
                    sb.append(" select " + nscTableName + ".nsc\n");

                    for (String fp : fpLookup.keySet()) {
                        sb.append(", CASE \n");
                        sb.append(" WHEN mol is null THEN null\n");
                        sb.append(" WHEN " + fp + "(mol) is null THEN null \n");
                        sb.append(" ELSE " + fp + "(mol) END \n");
                        sb.append(" as " + fp + "\n");
                    }

                    sb.append(" from rdkit_mol, " + nscTableName + "\n");
                    sb.append(" where rdkit_mol.nsc = " + nscTableName + ".nsc\n");

                    sql = sb.toString();
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    // run the calculations
                    sql = "drop table if exists " + tanimotoTableName;
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    sb = new StringBuilder();

                    sb.append("create table " + tanimotoTableName + " as \n");
                    sb.append(" select uno as nsc1, duo as nsc2 \n");

                    for (String fp : fpLookup.keySet()) {
                        sb.append(", CASE \n");
                        sb.append(" WHEN uno." + fp + " is null THEN null \n");
                        sb.append(" WHEN duo." + fp + " is null THEN null \n");
                        sb.append(" WHEN tanimoto_dist(uno." + fp + ", duo." + fp + ") is not null THEN \n");
                        sb.append(" tanimoto_dist(uno." + fp + ", duo." + fp + ")\n");
                        sb.append(" ELSE null END \n");
                        sb.append(" as " + fp + "\n");
                    }

                    sb.append(" from " + allPairTableName + " ap, " + fpTableName + " uno, " + fpTableName + " duo \n");
                    sb.append(" where ap.uno = uno.nsc and ap.duo = duo.nsc\n");

                    sql = sb.toString();
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    sql = "select nsc1 , nsc2 , atompairbv_fp , featmorganbv_fp , layered_fp , maccs_fp , morganbv_fp , rdkit_fp , torsionbv_fp "
                            + "from " + tanimotoTableName;

                    ResultSet rs = stmt.executeQuery(sql);
                    Integer nsc1;
                    Integer nsc2;
                    Double atompairbv_fp;
                    Double featmorganbv_fp;
                    Double layered_fp;
                    Double maccs_fp;
                    Double morganbv_fp;
                    Double rdkit_fp;
                    Double torsionbv_fp;

                    while (rs.next()) {

                        TanimotoScoresWithCmpdObjectsVO tswcoVO = new TanimotoScoresWithCmpdObjectsVO();

                        Integer n1 = rs.getInt("nsc1");
                        Integer n2 = rs.getInt("nsc2");
                        
//                        System.out.println("nsc1: " + n1 + " nsc2: " + n2);
                        
                        CmpdVO cVO1 = nscCmpdMap.get(n1);
                        CmpdVO cVO2 = nscCmpdMap.get(n2);
                        
//                        System.out.println("cmpd1.nsc: " + cVO1.getNsc() + " cmpd2.nsc: " + cVO2.getNsc());
                        
                        tswcoVO.setCmpd1(cVO1);
                        tswcoVO.setCmpd2(cVO2);
                        
                        tswcoVO.setAtomPairBv(rs.getDouble("atompairbv_fp"));
                        tswcoVO.setFeatMorganBv(rs.getDouble("featmorganbv_fp"));
                        tswcoVO.setLayered(rs.getDouble("layered_fp"));
                        tswcoVO.setMaccs(rs.getDouble("maccs_fp"));
                        tswcoVO.setMorganBv(rs.getDouble("morganbv_fp"));
                        tswcoVO.setRdkit(rs.getDouble("rdkit_fp"));
                        tswcoVO.setTorsionBv(rs.getDouble("torsionbv_fp"));

                        rtnList.add(tswcoVO);

                    }
//
//                    
//                  clean up  
//                    
//                    
//                    
                    
//                    sql = "drop table if exists " + nscTableName;
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    sql = "drop table if exists " + allPairTableName;
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    sql = "drop table if exists " + fpTableName;
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);
//
//                    sql = "drop table if exists " + tanimotoTableName;
//                    System.out.println(sql);
//                    stmt.executeUpdate(sql);

                    stmt.close();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtnList;

    }

}
