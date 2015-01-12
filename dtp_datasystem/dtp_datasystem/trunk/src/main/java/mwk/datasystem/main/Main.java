/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.controllers.QueryObject;
import mwk.datasystem.controllers.StructureSearchController;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdTable;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.util.TransformAndroToVO;
import mwk.datasystem.util.TransformCmpdTableToVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.ExtendedAtomGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;
import org.openscience.cdk.tautomers.InChITautomerGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static void main(String[] args) {
    
    List<Integer> nscIntList = new ArrayList<Integer>();
    nscIntList.add(740);
    nscIntList.add(3053);
    nscIntList.add(123127);
    
    
    getCmpdsByNsc(nscIntList, "PUBLIC");
    
  }

  public static List<CmpdVO> getCmpdsByNsc(
          List<Integer> nscIntList,
          String currentUser) {

    List<CmpdVO> rtnList = new ArrayList<CmpdVO>();

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();
      Criteria cmpdCrit = session.createCriteria(Cmpd.class)
              .add(Restrictions.in("nsc", nscIntList));

      List<Cmpd> cmpdList = (List<Cmpd>) cmpdCrit.list();

      rtnList= TransformAndroToVO.translateListOfCmpds(cmpdList);
      
      System.out.println("Size of rtnList is: " + rtnList.size());

      tx.commit();

    } catch (Exception e) {
      tx.rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }

    return rtnList;

  }

}
