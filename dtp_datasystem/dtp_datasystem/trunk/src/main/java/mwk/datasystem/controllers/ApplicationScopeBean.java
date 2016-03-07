/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import mwk.datasystem.domain.CmpdAlias;
import mwk.datasystem.domain.CmpdPlate;
import mwk.datasystem.domain.CmpdProject;
import mwk.datasystem.domain.CmpdNamedSet;
import mwk.datasystem.domain.CmpdTarget;
import mwk.datasystem.domain.NscCmpd;
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

    private String compareUrl;
    //
    private List<SelectItem> aliasItems;
    private List<SelectItem> casItems;
    private List<SelectItem> cmpdNamedSetItems;
    private List<SelectItem> drugNameItems;
    private List<SelectItem> nscItems;
    private List<SelectItem> plateItems;
    private List<SelectItem> projectCodeItems;
    private List<SelectItem> targetItems;

    public ApplicationScopeBean() {
        init();
    }

    @PostConstruct
    public void init() {

        Properties props = new Properties();

        try {

            InputStream is = getClass().getResourceAsStream("/deployment.properties");
            props.load(is);

            versionAndBuildTime = props.getProperty("pom.version.and.build.time");

            if (versionAndBuildTime.startsWith("datasystem")) {
                projectTitle = "Data System Project";
            } else if (versionAndBuildTime.startsWith("oncologydrugs")) {
                projectTitle = "Oncology Drugs Project";
            }

            String rawCompareUrl = props.getProperty("compare.application.url");

      // make sure there aren't any concatenated //
            // protect the http: header
            String str1 = rawCompareUrl.replaceAll("http:\\/\\/", "httpLeader");
            String str2 = str1.replaceAll("\\/\\/", "\\/");
            String str3 = str2.replaceAll("httpLeader", "http:\\/\\/");

            compareUrl = str3;

        } catch (Exception e) {
            e.printStackTrace();
        }

        aliasItems = new ArrayList<SelectItem>();
        casItems = new ArrayList<SelectItem>();
        cmpdNamedSetItems = new ArrayList<SelectItem>();
        drugNameItems = new ArrayList<SelectItem>();
        nscItems = new ArrayList<SelectItem>();
        plateItems = new ArrayList<SelectItem>();
        projectCodeItems = new ArrayList<SelectItem>();
        targetItems = new ArrayList<SelectItem>();

        // for large datasets, don't populate drop-down
        if (!versionAndBuildTime.startsWith("oncologydrugs")) {
            nscItems.add(new SelectItem("not avaliable by lookup", "not avaliable by lookup"));
            drugNameItems.add(new SelectItem("not avaliable by lookup", "not avaliable by lookup"));
            aliasItems.add(new SelectItem("not avaliable by lookup", "not avaliable by lookup"));
            casItems.add(new SelectItem("not avaliable by lookup", "not avaliable by lookup"));
        }

//        cmpdNamedSetItems.add(new SelectItem("cmpdNamedSetItemsTest", "cmpdNamedSetItemsTest"));
//        projectCodeItems.add(new SelectItem("projectCodeItemsTest", "projectCodeItemsTest"));
//        plateItems.add(new SelectItem("plateItemsTest", "plateItemsTest"));
//        targetItems.add(new SelectItem("targetItemsTest", "targetItemsTest"));
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

    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd'at'HH:mm:z");
        Date dt = new Date();
        return sdf.format(dt);         
    }

    private void createItemSelects() {

        HashSet<String> aliasSet = new HashSet<String>();
        HashSet<String> casSet = new HashSet<String>();
        HashSet<String> cmpdNamedSetSet = new HashSet<String>();
        HashSet<String> drugNameSet = new HashSet<String>();
        HashSet<Integer> nscSet = new HashSet<Integer>();
        HashSet<String> plateSetSet = new HashSet<String>();
        HashSet<String> projectCodeSet = new HashSet<String>();
        HashSet<String> targetSet = new HashSet<String>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

            if (versionAndBuildTime.startsWith("oncologydrugs")) {

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

            }

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

// _ _     _          __                                _       
//| (_)___| |_ ___   / _|_ __ ___  _ __ ___    ___  ___| |_ ___ 
//| | / __| __/ __| | |_| '__/ _ \| '_ ` _ \  / __|/ _ \ __/ __|
//| | \__ \ |_\__ \ |  _| | | (_) | | | | | | \__ \  __/ |_\__ \
//|_|_|___/\__|___/ |_| |_|  \___/|_| |_| |_| |___/\___|\__|___/
            //drugName
            ArrayList<String> drugNameList = new ArrayList<String>(drugNameSet);
            Collections.sort(drugNameList);
            for (String s : drugNameList) {
                drugNameItems.add(new SelectItem(s, s));
            }

//alias
            ArrayList<String> aliasList = new ArrayList<String>(aliasSet);
            Collections.sort(aliasList);
            for (String s : aliasList) {
                aliasItems.add(new SelectItem(s, s));
            }

//cas
            ArrayList<String> casList = new ArrayList<String>(casSet);
            Collections.sort(casList);
            for (String s : casList) {
                casItems.add(new SelectItem(s, s));
            }

//cmpdNamedSet
            ArrayList<String> cmpdNamedSetList = new ArrayList<String>(cmpdNamedSetSet);
            Collections.sort(cmpdNamedSetList);
            for (String s : cmpdNamedSetList) {
                cmpdNamedSetItems.add(new SelectItem(s, s));
            }

//nsc
            ArrayList<Integer> nscList = new ArrayList<Integer>(nscSet);
            Collections.sort(nscList);
            for (Integer i : nscList) {
                nscItems.add(new SelectItem(i.toString(), i.toString()));
            }

//projectCode
            ArrayList<String> projectCodeList = new ArrayList<String>(projectCodeSet);
            Collections.sort(projectCodeList);
            for (String s : projectCodeList) {
                projectCodeItems.add(new SelectItem(s, s));
            }

//plate
            ArrayList<String> plateList = new ArrayList<String>(plateSetSet);
            Collections.sort(plateList);
            for (String s : plateList) {
                plateItems.add(new SelectItem(s, s));
            }

//target
            ArrayList<String> targetList = new ArrayList<String>(targetSet);
            Collections.sort(targetList);
            for (String s : targetList) {
                targetItems.add(new SelectItem(s, s));
            }

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

    }

    public List<String> completedrugName(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : drugNameItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completealias(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : aliasItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completecas(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : casItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completecmpdNamedSet(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : cmpdNamedSetItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completensc(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : nscItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completeprojectCode(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : projectCodeItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completeplate(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : plateItems) {
            if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
                suggestions.add(si.getLabel());
            }
        }
        return suggestions;
    }

    public List<String> completetarget(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (SelectItem si : targetItems) {
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

    public String getCompareUrl() {
        return compareUrl;
    }

    public void setCompareUrl(String compareUrl) {
        this.compareUrl = compareUrl;
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
