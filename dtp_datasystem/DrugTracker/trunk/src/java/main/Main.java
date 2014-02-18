/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import jpa.entity.DrugTracker;
import jpa.entity.DrugTracker_;
import jpa.entity.NscDrugTracker;
import jpa.entity.NscDrugTracker_;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static void main(String[] args) {

    //testDrugTrackerSearch();

    testEntitySearch();


  }

  public static void testEntitySearch() {

//        Context initCtx = new InitialContext();
//
//        //for deployment to Tomcat
//        Context envCtx = (Context) initCtx.lookup("java:comp/env");
//        DataSource ds = (DataSource) envCtx.lookup("jdbc/compareDataSource");
//
//        //jndi named data source for deployment to WebLogic
//        //DataSource ds = (DataSource) initCtx.lookup("jdbc/publicCompareDataSource");

    EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("DrugTrackerPU");
    EntityManager em = emf.createEntityManager();

    //em.getTransaction().begin();

    try {

      //fetch dt by dt criteria
//            DrugTracker dt = (DrugTracker) em.createNamedQuery("DrugTracker.findByAgent").setParameter("agent", "Perifosine").getSingleResult();
//            NscDrugTracker nscDt = dt.getNscDrugTracker();
//            System.out.println("DrugTracker id: " + dt.getId() + " agent: " + dt.getAgent() + " NSC: " + nscDt.getNsc());
//            
//            //fetch dt by nsc criteria            
//            nscDt = (NscDrugTracker) em.createNamedQuery("NscDrugTracker.findByNsc").setParameter("nsc", 123127).getSingleResult();
//            dt = nscDt.getDrugTracker();            
//            System.out.println("DrugTracker id: " + dt.getId() + " agent: " + dt.getAgent() + " NSC: " + nscDt.getNsc());

      //em.getTransaction().commit();

      ArrayList<String> cases = new ArrayList<String>();
      cases.add("1001264-89-6");

      ArrayList<String> agents = new ArrayList<String>();
      agents.add("ABT-199");

      ArrayList<String> originators = new ArrayList<String>();
      originators.add("Abbott");

      CriteriaBuilder critBuilder = em.getCriteriaBuilder();
      CriteriaQuery<DrugTracker> critQuery = critBuilder.createQuery(DrugTracker.class);
      Root<DrugTracker> rootDrugTracker = critQuery.from(DrugTracker.class);

      List<Predicate> predList = new ArrayList<Predicate>();

      if (!cases.isEmpty()) {
        predList.add(rootDrugTracker.get("cas").in(cases));
      };
      //
      if (!agents.isEmpty()) {
        predList.add(rootDrugTracker.get("agent").in(agents));
      };
      //      
      if (!originators.isEmpty()) {
        predList.add(rootDrugTracker.get("originator").in(originators));
      };

//      for (Predicate p : predList) {
//        critQuery.where(p);
//        TypedQuery<DrugTracker> q = em.createQuery(critQuery);
//        List<DrugTracker> resultList = q.getResultList();
//        System.out.println(" resultList contains: " + resultList.size() + " DrugTrackers");
//        for (DrugTracker dt : resultList) {
//          System.out.println(dt.getCas() + " " + dt.getAgent() + " " + dt.getOriginator());
//        }
//      }
      

      //nsc            
      Join<DrugTracker, NscDrugTracker> nscDrugT = rootDrugTracker.join(DrugTracker_.nscDrugTracker);
      critQuery.where(critBuilder.equal(nscDrugT.get(NscDrugTracker_.nsc), 740));








      TypedQuery<DrugTracker> q = em.createQuery(critQuery);
      List<DrugTracker> resultList = q.getResultList();
      System.out.println(" resultList contains: " + resultList.size() + " DrugTrackers");
      for (DrugTracker dt : resultList) {
        System.out.println(dt.getCas() + " " + dt.getAgent() + " " + dt.getOriginator());
      }




    } catch (Exception e) {
      e.printStackTrace();
      //em.getTransaction().rollback();
    } finally {
      em.close();
    }

  }
}
