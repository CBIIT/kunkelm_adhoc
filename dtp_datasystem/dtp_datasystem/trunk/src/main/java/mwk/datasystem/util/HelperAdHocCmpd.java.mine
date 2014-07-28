/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
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
public class HelperAdHocCmpd {
    
    public static void replicatePchem(AdHocCmpdFragmentPChem cur, AdHocCmpdFragmentPChem nw){        
        nw.setAlogp(cur.getAlogp());
        nw.setHba(cur.getHba());
        nw.setHbd(cur.getHbd());
        nw.setLogd(cur.getLogd());
        nw.setMf(cur.getMf());
        nw.setPsa(cur.getPsa());
        nw.setSa(cur.getSa());        
    }
    
    public static void replicateStructure(AdHocCmpdFragmentStructure cur, AdHocCmpdFragmentStructure nw){        
        nw.setCtab(cur.getCtab());
        nw.setInchi(cur.getInchi());
        nw.setInchiAux(cur.getInchiAux());
        nw.setMol(cur.getMol());
        nw.setSmiles(cur.getSmiles());
    }
    
    public static AdHocCmpd replicateAdHocCmpdObject(AdHocCmpd ahc, String currentUser) {

        Random randomGenerator = new Random();
        
        AdHocCmpd rtn = AdHocCmpd.Factory.newInstance();

        try {

            long randomId = randomGenerator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            Long newRandomId = new Long(randomId);

            rtn.setAdHocCmpdId(newRandomId);
            rtn.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());
            rtn.setName(ahc.getName());            

            for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {
                
                AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();

                AdHocCmpdFragmentPChem pchem = AdHocCmpdFragmentPChem.Factory.newInstance();                
                replicatePchem(ahcf.getAdHocCmpdFragmentPChem(), pchem);  
                frag.setAdHocCmpdFragmentPChem(pchem);
                                
                AdHocCmpdFragmentStructure struc = AdHocCmpdFragmentStructure.Factory.newInstance();
                replicateStructure(ahcf.getAdHocCmpdFragmentStructure(), struc);
                frag.setAdHocCmpdFragmentStructure(struc);
                
                if (ahc.getAdHocCmpdParentFragment().equals(ahcf)){
                    rtn.setAdHocCmpdParentFragment(frag);
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }

        return rtn;

    }
    
  public static AdHocCmpd createNewAdHocCmpd(AdHocCmpd ahc, String currentUser) {

        Random randomGenerator = new Random();

        Session session = null;
        Transaction tx = null;

        AdHocCmpd rtn = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            long randomId = randomGenerator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            Long newRandomId = new Long(randomId);

            tx = session.beginTransaction();

            rtn = AdHocCmpd.Factory.newInstance();
            
            rtn.setAdHocCmpdId(newRandomId);
            rtn.setOriginalAdHocCmpdId(newRandomId);
            rtn.setName(ahc.getName());
            
            for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {
                
                AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();

                AdHocCmpdFragmentPChem pchem = AdHocCmpdFragmentPChem.Factory.newInstance();                
                replicatePchem(ahcf.getAdHocCmpdFragmentPChem(), pchem);                
                session.persist(pchem);
                
                frag.setAdHocCmpdFragmentPChem(pchem);
                                
                AdHocCmpdFragmentStructure struc = AdHocCmpdFragmentStructure.Factory.newInstance();
                replicateStructure(ahcf.getAdHocCmpdFragmentStructure(), struc);
                session.persist(struc);
                
                frag.setAdHocCmpdFragmentStructure(struc);
                
                // persist the fragment                
                session.persist(frag);
                
                // check whether this is parent, and set that while we're at it
                
                if (ahc.getAdHocCmpdParentFragment().equals(ahcf)){
                    rtn.setAdHocCmpdParentFragment(frag);
                }
                
            }
            
            session.persist("Cmpd", rtn);
            
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();            
        } finally {
            session.close();
        }

        return rtn;

    }

}