/*
 * To change this template, choose Tools | Templates
 
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
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;
import mwk.datasystem.domain.CmpdPlate;
import mwk.datasystem.domain.CmpdProject;
import mwk.datasystem.domain.CmpdNamedSet;
import mwk.datasystem.domain.CmpdTarget;
import mwk.datasystem.util.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ApplicationScopeBean implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;
    //
    private String projectTitle;
    private String versionAndBuildTime;
    private String landingCompareUrl;
    private String landingSpotfireUrl;
    //
    private List<SelectItem> drugNameItems;
    private List<SelectItem> aliasItems;
    private List<SelectItem> casItems;
    private List<SelectItem> cmpdNamedSetItems;
    private List<SelectItem> nscItems;
    private List<SelectItem> projectCodeItems;
    private List<SelectItem> plateItems;
    private List<SelectItem> targetItems;

    public ApplicationScopeBean() {
        init();
    }

    @PostConstruct
    public void init() {

        Properties props = new Properties();

        try {

            InputStream is = this.getClass().getResourceAsStream("/deployment.properties");
            props.load(is);

            this.versionAndBuildTime = props.getProperty("pom.version.and.build.time");

            if (this.versionAndBuildTime.startsWith("datasystem")) {
                
                this.projectTitle = "Data System Project";
                
            } else if (this.versionAndBuildTime.startsWith("oncologydrugs")) {
                
                this.projectTitle = "Oncology Drugs Project";
                                
            }

            String rawLandingCompareUrl = props.getProperty("compare.application.landing.url");

            // make sure there aren't any concatenated //
            // protect the http: header
            String str1 = rawLandingCompareUrl.replaceAll("http:\\/\\/", "httpLeader");
            String str2 = str1.replaceAll("\\/\\/", "\\/");
            String str3 = str2.replaceAll("httpLeader", "http:\\/\\/");

            this.landingCompareUrl = str3;

            String rawLandingSpotfireUrl = props.getProperty("compare.application.landing.spotfire.url");

            // make sure there aren't any concatenated //
            // protect the http: header
            str1 = rawLandingSpotfireUrl.replaceAll("http:\\/\\/", "httpLeader");
            str2 = str1.replaceAll("\\/\\/", "\\/");
            str3 = str2.replaceAll("httpLeader", "http:\\/\\/");

            this.landingSpotfireUrl = str3;

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.drugNameItems = new ArrayList<SelectItem>();
        this.aliasItems = new ArrayList<SelectItem>();
        this.casItems = new ArrayList<SelectItem>();
        this.cmpdNamedSetItems = new ArrayList<SelectItem>();
        this.nscItems = new ArrayList<SelectItem>();
        this.projectCodeItems = new ArrayList<SelectItem>();
        this.plateItems = new ArrayList<SelectItem>();
        this.targetItems = new ArrayList<SelectItem>();

        // createItemSelects();
        // for test
        this.drugNameItems.add(new SelectItem("drugNameItemsTest", "drugNameItemsTest"));
        this.aliasItems.add(new SelectItem("aliasItemsTest", "aliasItemsTest"));
        this.casItems.add(new SelectItem("casItemsTest", "casItemsTest"));
        this.cmpdNamedSetItems.add(new SelectItem("cmpdNamedSetItemsTest", "cmpdNamedSetItemsTest"));
        this.nscItems.add(new SelectItem("123456", "123456"));
        this.projectCodeItems.add(new SelectItem("projectCodeItemsTest", "projectCodeItemsTest"));
        this.plateItems.add(new SelectItem("plateItemsTest", "plateItemsTest"));
        this.targetItems.add(new SelectItem("targetItemsTest", "targetItemsTest"));

        createItemSelects();

        System.out.println("Size of drugNameItems:" + drugNameItems.size());
        System.out.println("Size of aliasItems:" + aliasItems.size());
        System.out.println("Size of casItems:" + casItems.size());
        System.out.println("Size of cmpdNamedSetItems:" + cmpdNamedSetItems.size());
        System.out.println("Size of nscItems:" + nscItems.size());
        System.out.println("Size of projectCodeItems:" + projectCodeItems.size());
        System.out.println("Size of plateItems:" + plateItems.size());
        System.out.println("Size of targetItems:" + targetItems.size());

    }

    private void createItemSelects() {

        // PITB harvest appropriate information from the database
        // don't do compounds for now because of size
//    HashSet<Integer> nscSet = new HashSet<Integer>();
//    HashSet<String> drugNameSet = new HashSet<String>();
//    HashSet<String> casSet = new HashSet<String>();
        HashSet<String> aliasSet = new HashSet<String>();
        HashSet<String> cmpdNamedSetSet = new HashSet<String>();
        HashSet<String> projectCodeSet = new HashSet<String>();
        HashSet<String> plateSetSet = new HashSet<String>();
        HashSet<String> targetSet = new HashSet<String>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

//      Criteria cmpdCrit = session.createCriteria(NscCmpd.class);
//      List<NscCmpd> cmpdResultList = (List<NscCmpd>) cmpdCrit.list();
//
//      for (NscCmpd nc : cmpdResultList) {
//
//        //drugName
//        if (nc.getName() != null) {
//          drugNameSet.add(nc.getName());
//        }
//        //cas
//        if (nc.getCas() != null) {
//          casSet.add(nc.getCas());
//        }
//
//        //nsc
//        if (nc.getNsc() != null) {
//          nscSet.add(nc.getNsc());
//        }
//
//      }
//      //alias
//      Criteria aliasCrit = session.createCriteria(CmpdAlias.class);
//      List<CmpdAlias> aliasResultList = (List<CmpdAlias>) aliasCrit.list();
//      for (CmpdAlias al : aliasResultList) {
//        aliasSet.add(al.getAlias());
//      }
            //cmpdNamedSet
            Criteria cmpdNamedSetCrit = session.createCriteria(CmpdNamedSet.class);
            List<CmpdNamedSet> cmpdNamedSetResultList = (List<CmpdNamedSet>) cmpdNamedSetCrit.list();
            for (CmpdNamedSet cns : cmpdNamedSetResultList) {
                cmpdNamedSetSet.add(cns.getSetName());
            }

            //projectCode
            Criteria cmpdProjectCrit = session.createCriteria(CmpdProject.class);
            List<CmpdProject> cmpdProjectResultList = (List<CmpdProject>) cmpdProjectCrit.list();
            for (CmpdProject cp : cmpdProjectResultList) {
                projectCodeSet.add(cp.getProjectCode());
            }

            //plateSet
            Criteria cmpdPlateCrit = session.createCriteria(CmpdPlate.class);
            List<CmpdPlate> cmpdPlateResultList = (List<CmpdPlate>) cmpdPlateCrit.list();
            for (CmpdPlate cp : cmpdPlateResultList) {
                cmpdNamedSetSet.add(cp.getPlateName());
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
//      ArrayList<String> drugNameList = new ArrayList<String>(drugNameSet);
//      Collections.sort(drugNameList);
//      for (String s : drugNameList) {
//        this.drugNameItems.add(new SelectItem(s, s));
//      }
//alias
//      ArrayList<String> aliasList = new ArrayList<String>(aliasSet);
//      Collections.sort(aliasList);
//      for (String s : aliasList) {
//        this.aliasItems.add(new SelectItem(s, s));
//      }
//cas
//      ArrayList<String> casList = new ArrayList<String>(casSet);
//      Collections.sort(casList);
//      for (String s : casList) {
//        this.casItems.add(new SelectItem(s, s));
//      }
//cmpdNamedSet
            ArrayList<String> cmpdNamedSetList = new ArrayList<String>(cmpdNamedSetSet);
            Collections.sort(cmpdNamedSetList);
            for (String s : cmpdNamedSetList) {
                this.cmpdNamedSetItems.add(new SelectItem(s, s));
            }

//nsc
//      ArrayList<Integer> nscList = new ArrayList<Integer>(nscSet);
//      Collections.sort(nscList);
//      for (Integer i : nscList) {
//        this.nscItems.add(new SelectItem(i.toString(), i.toString()));
//      }
//projectCode
            ArrayList<String> projectCodeList = new ArrayList<String>(projectCodeSet);
            Collections.sort(projectCodeList);
            for (String s : projectCodeList) {
                this.projectCodeItems.add(new SelectItem(s, s));
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

    public List<String> completecmpdNamedSet(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : this.cmpdNamedSetItems) {
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
    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getVersionAndBuildTime() {
        return versionAndBuildTime;
    }

    public void setVersionAndBuildTime(String versionAndBuildTime) {
        this.versionAndBuildTime = versionAndBuildTime;
    }

    public String getLandingCompareUrl() {
        return landingCompareUrl;
    }

    public void setLandingCompareUrl(String landingCompareUrl) {
        this.landingCompareUrl = landingCompareUrl;
    }

    public String getLandingSpotfireUrl() {
        return landingSpotfireUrl;
    }

    public void setLandingSpotfireUrl(String landingSpotfireUrl) {
        this.landingSpotfireUrl = landingSpotfireUrl;
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

    public List<SelectItem> getCmpdNamedSetItems() {
        return cmpdNamedSetItems;
    }

    public List<SelectItem> getNscItems() {
        return nscItems;
    }

    public List<SelectItem> getProjectCodeItems() {
        return projectCodeItems;
    }

    public List<SelectItem> getPlateItems() {
        return plateItems;
    }

    public List<SelectItem> getTargetItems() {
        return targetItems;
    }
    // </editor-fold>
}
