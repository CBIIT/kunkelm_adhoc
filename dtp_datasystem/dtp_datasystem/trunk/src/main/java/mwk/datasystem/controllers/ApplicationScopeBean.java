/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import mwk.datasystem.domain.CmpdAlias;
import mwk.datasystem.domain.CmpdPlate;
import mwk.datasystem.domain.CmpdProject;
import mwk.datasystem.domain.CmpdSet;
import mwk.datasystem.domain.CmpdTarget;
import mwk.datasystem.domain.NscCmpd;
import mwk.datasystem.util.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openscience.cdk.inchi.InChIGeneratorFactory;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ApplicationScopeBean implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;    
    //
    private String versionAndBuildTime;
    //
    private List<SelectItem> drugNameItems;
    private List<SelectItem> aliasItems;
    private List<SelectItem> casItems;
    private List<SelectItem> cmpdSetItems;
    private List<SelectItem> nscItems;
    private List<SelectItem> projectCodeItems;
    private List<SelectItem> originatorItems;
    private List<SelectItem> plateItems;
    private List<SelectItem> targetItems;

    public ApplicationScopeBean() {

        Properties props = new Properties();
        try {
            
            InputStream is = this.getClass().getResourceAsStream("/deployment.properties");
            props.load(is);

            this.versionAndBuildTime = props.getProperty("pom.version.and.build.time");

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.drugNameItems = new ArrayList<SelectItem>();
        this.aliasItems = new ArrayList<SelectItem>();
        this.casItems = new ArrayList<SelectItem>();
        this.cmpdSetItems = new ArrayList<SelectItem>();
        this.nscItems = new ArrayList<SelectItem>();
        this.projectCodeItems = new ArrayList<SelectItem>();
        this.originatorItems = new ArrayList<SelectItem>();
        this.plateItems = new ArrayList<SelectItem>();
        this.targetItems = new ArrayList<SelectItem>();

        // createItemSelects();

        // for test
        this.drugNameItems.add(new SelectItem("drugNameItemsTest", "drugNameItemsTest"));
        this.aliasItems.add(new SelectItem("aliasItemsTest", "aliasItemsTest"));
        this.casItems.add(new SelectItem("casItemsTest", "casItemsTest"));
        this.cmpdSetItems.add(new SelectItem("cmpdSetItemsTest", "cmpdSetItemsTest"));
        this.nscItems.add(new SelectItem("123456", "123456"));
        this.projectCodeItems.add(new SelectItem("projectCodeItemsTest", "projectCodeItemsTest"));
        this.originatorItems.add(new SelectItem("originatorItemsTest", "originatorItemsTest"));
        this.plateItems.add(new SelectItem("plateItemsTest", "plateItemsTest"));
        this.targetItems.add(new SelectItem("targetItemsTest", "targetItemsTest"));


    }

    private void createItemSelects() {

        // gigantic PITB to harvest appropriate information from the database

        HashSet<String> drugNameSet = new HashSet<String>();
        HashSet<String> aliasSet = new HashSet<String>();
        HashSet<String> casSet = new HashSet<String>();
        HashSet<String> cmpdSetSet = new HashSet<String>();
        HashSet<Integer> nscSet = new HashSet<Integer>();
        HashSet<String> projectCodeSet = new HashSet<String>();
        HashSet<String> originatorSet = new HashSet<String>();
        HashSet<String> plateSetSet = new HashSet<String>();
        HashSet<String> targetSet = new HashSet<String>();


        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

            Criteria cmpdCrit = session.createCriteria(NscCmpd.class);
            List<NscCmpd> cmpdResultList = (List<NscCmpd>) cmpdCrit.list();

            for (NscCmpd nc : cmpdResultList) {

                //drugName
                if (nc.getName() != null) {
                    drugNameSet.add(nc.getName());
                }
                //cas
                if (nc.getCas() != null) {
                    casSet.add(nc.getCas());
                }

                //nsc
                if (nc.getNsc() != null) {
                    nscSet.add(nc.getNsc());
                }



            }


            //alias
            Criteria aliasCrit = session.createCriteria(CmpdAlias.class);
            List<CmpdAlias> aliasResultList = (List<CmpdAlias>) aliasCrit.list();
            for (CmpdAlias al : aliasResultList) {
                aliasSet.add(al.getAlias());
            }

            //cmpdSet
            Criteria cmpdSetCrit = session.createCriteria(CmpdSet.class);
            List<CmpdSet> cmpdSetResultList = (List<CmpdSet>) cmpdSetCrit.list();
            for (CmpdSet cs : cmpdSetResultList) {
                cmpdSetSet.add(cs.getSetName());
            }

            //projectCode
            Criteria cmpdProjectCrit = session.createCriteria(CmpdProject.class);
            List<CmpdProject> cmpdProjectResultList = (List<CmpdProject>) cmpdProjectCrit.list();
            for (CmpdProject cp : cmpdProjectResultList) {
                projectCodeSet.add(cp.getProjectCode());
            }

            //originator                
            // THIS NEEDS TO BE DONE

            //plateSet
            Criteria cmpdPlateCrit = session.createCriteria(CmpdPlate.class);
            List<CmpdPlate> cmpdPlateResultList = (List<CmpdPlate>) cmpdPlateCrit.list();
            for (CmpdPlate cp : cmpdPlateResultList) {
                cmpdSetSet.add(cp.getPlateName());
            }

            //target
            Criteria cmpdTargetCrit = session.createCriteria(CmpdTarget.class);
            List<CmpdTarget> cmpdTargetResultList = (List<CmpdTarget>) cmpdTargetCrit.list();
            for (CmpdTarget ct : cmpdTargetResultList) {
                targetSet.add(ct.getTarget());
            }

            //
            //
            // Create ItemSelect
            //
            //

            //drugName
            ArrayList<String> drugNameList = new ArrayList<String>(drugNameSet);
            Collections.sort(drugNameList);
            for (String s : drugNameList) {
                this.drugNameItems.add(new SelectItem(s, s));
            }
//alias
            ArrayList<String> aliasList = new ArrayList<String>(aliasSet);
            Collections.sort(aliasList);
            for (String s : aliasList) {
                this.aliasItems.add(new SelectItem(s, s));
            }
//cas
            ArrayList<String> casList = new ArrayList<String>(casSet);
            Collections.sort(casList);
            for (String s : casList) {
                this.casItems.add(new SelectItem(s, s));
            }
//cmpdSet
            ArrayList<String> cmpdSetList = new ArrayList<String>(cmpdSetSet);
            Collections.sort(cmpdSetList);
            for (String s : cmpdSetList) {
                this.cmpdSetItems.add(new SelectItem(s, s));
            }
//nsc
            ArrayList<Integer> nscList = new ArrayList<Integer>(nscSet);
            Collections.sort(nscList);
            for (Integer i : nscList) {
                this.nscItems.add(new SelectItem(i.toString(), i.toString()));
            }
//projectCode
            ArrayList<String> projectCodeList = new ArrayList<String>(projectCodeSet);
            Collections.sort(projectCodeList);
            for (String s : projectCodeList) {
                this.projectCodeItems.add(new SelectItem(s, s));
            }
//originator
            ArrayList<String> originatorList = new ArrayList<String>(originatorSet);
            Collections.sort(originatorList);
            for (String s : originatorList) {
                this.originatorItems.add(new SelectItem(s, s));
            }
//plate
            ArrayList<String> plateList = new ArrayList<String>(plateSetSet);
            Collections.sort(plateList);
            for (String s : plateList) {
                this.plateItems.add(new SelectItem(s, s));
            }
//target
            ArrayList<String> targetList = new ArrayList<String>(targetSet);
            Collections.sort(targetList);
            for (String s : targetList) {
                this.targetItems.add(new SelectItem(s, s));
            }

         tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();            
        } finally {
            session.close();
        }

    }

    public static String urlEncode(String in) {
        String rtn = "";
        try {
            rtn = URLEncoder.encode(in, "UTF-8");
        } catch (Exception e) {
        }
        return rtn;
    }

    public List<String> completedrugName(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.drugNameItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completealias(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.aliasItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completecas(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.casItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completecmpdSet(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.cmpdSetItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completensc(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.nscItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completeprojectCode(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.projectCodeItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completeoriginator(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.originatorItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completeplate(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.plateItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completetarget(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.targetItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public String getVersionAndBuildTime() {
        return versionAndBuildTime;
    }

    public void setVersionAndBuildTime(String versionAndBuildTime) {
        this.versionAndBuildTime = versionAndBuildTime;
    }

    public List<SelectItem> getDrugNameItems() {
        return drugNameItems;
    }

    public List<SelectItem> getAliasItems() {
        return aliasItems;
    }

    public List<SelectItem> getCasItems() {
        return casItems;
    }

    public List<SelectItem> getCmpdSetItems() {
        return cmpdSetItems;
    }

    public List<SelectItem> getNscItems() {
        return nscItems;
    }

    public List<SelectItem> getProjectCodeItems() {
        return projectCodeItems;
    }

    public List<SelectItem> getOriginatorItems() {
        return originatorItems;
    }

    public List<SelectItem> getPlateItems() {
        return plateItems;
    }

    public List<SelectItem> getTargetItems() {
        return targetItems;
    }
    // </editor-fold>
}
