/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.main;

import mwk.datasystem.util.TemplPropUtil;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.controllers.SearchCriteriaBean;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdImpl;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperTanimotoScores;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.TransformAndroToVO;
import mwk.datasystem.util.TransformXMLGregorianCalendar;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.TanimotoScoresVO;
import newstructureservlet.MoleculeWrangling;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static final Boolean DEBUG = Boolean.TRUE;

  public static void main(String[] args) {
    //testTanimotoScores();
    testSearchCriteriaBean();
  }

  public static void testTanimotoScores() {
    ArrayList<TanimotoScoresVO> scores = HelperTanimotoScores.fetch();
    System.out.println("scores.size(): " + scores.size());
  }

  public static void testSearchCriteriaBean() {

    SearchCriteriaBean scb = new SearchCriteriaBean();
    scb.setProjectCodeTextArea("DTP103 DTP110");

    ArrayList<String> projectList = new ArrayList<String>();
    projectList.add("DTP-103");
    projectList.add("DTP-110");

    scb.setMin_molecularWeight(150d);
    scb.setMax_molecularWeight(250d);

    HelperCmpd.createCmpdListFromSearchCriteriaBean("testList", scb, null, "DTP_kunkelm");

  }

}
