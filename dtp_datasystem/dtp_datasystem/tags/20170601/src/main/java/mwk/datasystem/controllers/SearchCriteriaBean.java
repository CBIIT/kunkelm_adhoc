/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class SearchCriteriaBean implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    public static final Boolean DEBUG = Boolean.TRUE;

    private List<String> aliases;
    private List<String> cases;
    private List<String> cmpdNamedSets;
    private List<String> drugNames;
    private List<String> mtxtPieces;
    private List<String> nscs;
    private List<String> plates;
    private List<String> projectCodes;
    private List<String> targets;
    private List<String> pseudoAtomsPieces;

    private List<String> aliasesFromAutoComp;
    private List<String> casesFromAutoComp;
    private List<String> cmpdNamedSetsFromAutoComp;
    private List<String> drugNamesFromAutoComp;
    private List<String> mtxtPiecesFromAutoComp;
    private List<String> nscsFromAutoComp;
    private List<String> platesFromAutoComp;
    private List<String> projectCodesFromAutoComp;
    private List<String> targetsFromAutoComp;
    private List<String> pseudoAtomsPiecesFromAutoComp;

    private String aliasTextArea;
    private String casTextArea;
    private String cmpdNamedSetTextArea;
    private String drugNameTextArea;
    private String mtxtTextArea;
    private String nscTextArea;
    private String plateTextArea;
    private String projectCodeTextArea;
    private String pseudoAtomsTextArea;
    private String targetTextArea;

    // molecular properties
    public Double min_molecularWeight;
    public Double max_molecularWeight;
    public String molecularFormula;
    public Double min_logD;
    public Double max_logD;
    public Integer min_countHydBondAcceptors;
    public Integer max_countHydBondAcceptors;
    public Integer min_countHydBondDonors;
    public Integer max_countHydBondDonors;
    public Double min_surfaceArea;
    public Double max_surfaceArea;
    public Double min_solubility;
    public Double max_solubility;
    public Integer min_countRings;
    public Integer max_countRings;
    public Integer min_countAtoms;
    public Integer max_countAtoms;
    public Integer min_countBonds;
    public Integer max_countBonds;
    public Integer min_countSingleBonds;
    public Integer max_countSingleBonds;
    public Integer min_countDoubleBonds;
    public Integer max_countDoubleBonds;
    public Integer min_countTripleBonds;
    public Integer max_countTripleBonds;
    public Integer min_countRotatableBonds;
    public Integer max_countRotatableBonds;
    public Integer min_countHydrogenAtoms;
    public Integer max_countHydrogenAtoms;
    public Integer min_countMetalAtoms;
    public Integer max_countMetalAtoms;
    public Integer min_countHeavyAtoms;
    public Integer max_countHeavyAtoms;
    public Integer min_countPositiveAtoms;
    public Integer max_countPositiveAtoms;
    public Integer min_countNegativeAtoms;
    public Integer max_countNegativeAtoms;
    public Integer min_countRingBonds;
    public Integer max_countRingBonds;
    public Integer min_countStereoAtoms;
    public Integer max_countStereoAtoms;
    public Integer min_countStereoBonds;
    public Integer max_countStereoBonds;
    public Integer min_countRingAssemblies;
    public Integer max_countRingAssemblies;
    public Integer min_countAromaticBonds;
    public Integer max_countAromaticBonds;
    public Integer min_countAromaticRings;
    public Integer max_countAromaticRings;
    public Integer min_formalCharge;
    public Integer max_formalCharge;
    public Double min_theALogP;
    public Double max_theALogP;

    public SearchCriteriaBean() {
        this.aliases = new ArrayList<String>();
        this.cases = new ArrayList<String>();
        this.cmpdNamedSets = new ArrayList<String>();
        this.drugNames = new ArrayList<String>();
        this.mtxtPieces = new ArrayList<String>();
        this.nscs = new ArrayList<String>();
        this.plates = new ArrayList<String>();
        this.projectCodes = new ArrayList<String>();
        this.targets = new ArrayList<String>();
        this.pseudoAtomsPieces = new ArrayList<String>();
    }

    @PostConstruct
    public void init() {
        this.aliasTextArea = "";
        this.casTextArea = "";
        this.cmpdNamedSetTextArea = "";
        this.drugNameTextArea = "";
        this.mtxtTextArea = "";
        this.nscTextArea = "";
        this.plateTextArea = "";
        this.projectCodeTextArea = "";
        this.pseudoAtomsTextArea = "";
        this.targetTextArea = "";

        this.aliases = new ArrayList<String>();
        this.cases = new ArrayList<String>();
        this.cmpdNamedSets = new ArrayList<String>();
        this.drugNames = new ArrayList<String>();
        this.mtxtPieces = new ArrayList<String>();
        this.nscs = new ArrayList<String>();
        this.plates = new ArrayList<String>();
        this.projectCodes = new ArrayList<String>();
        this.targets = new ArrayList<String>();
        this.pseudoAtomsPieces = new ArrayList<String>();

        this.aliasesFromAutoComp = new ArrayList<String>();
        this.casesFromAutoComp = new ArrayList<String>();
        this.cmpdNamedSetsFromAutoComp = new ArrayList<String>();
        this.drugNamesFromAutoComp = new ArrayList<String>();
        this.mtxtPiecesFromAutoComp = new ArrayList<String>();
        this.nscsFromAutoComp = new ArrayList<String>();
        this.platesFromAutoComp = new ArrayList<String>();
        this.projectCodesFromAutoComp = new ArrayList<String>();
        this.targetsFromAutoComp = new ArrayList<String>();
        this.pseudoAtomsPiecesFromAutoComp = new ArrayList<String>();
    }

    /**
     *
     */
    public void newSearch() {

//        this.aliasTextArea = "";
//        this.casTextArea = "";
//        this.cmpdNamedSetTextArea = "";
//        this.drugNameTextArea = "";
//        this.mtxtTextArea = "";
//        this.nscTextArea = "";
//        this.plateTextArea = "";
//        this.projectCodeTextArea = "";
//        this.pseudoAtomsTextArea = "";
//        this.targetTextArea = "";
        this.aliases = new ArrayList<String>();
        this.cases = new ArrayList<String>();
        this.cmpdNamedSets = new ArrayList<String>();
        this.drugNames = new ArrayList<String>();
        this.mtxtPieces = new ArrayList<String>();
        this.nscs = new ArrayList<String>();
        this.plates = new ArrayList<String>();
        this.projectCodes = new ArrayList<String>();
        this.targets = new ArrayList<String>();
        this.pseudoAtomsPieces = new ArrayList<String>();

//        this.aliasesFromAutoComp = new ArrayList<String>();
//        this.casesFromAutoComp = new ArrayList<String>();
//        this.cmpdNamedSetsFromAutoComp = new ArrayList<String>();
//        this.drugNamesFromAutoComp = new ArrayList<String>();
//        this.mtxtPiecesFromAutoComp = new ArrayList<String>();
//        this.nscsFromAutoComp = new ArrayList<String>();
//        this.platesFromAutoComp = new ArrayList<String>();
//        this.projectCodesFromAutoComp = new ArrayList<String>();
//        this.targetsFromAutoComp = new ArrayList<String>();
//        this.pseudoAtomsPiecesFromAutoComp = new ArrayList<String>();
    }

    public void reset() {

        this.aliasTextArea = "";
        this.casTextArea = "";
        this.cmpdNamedSetTextArea = "";
        this.drugNameTextArea = "";
        this.mtxtTextArea = "";
        this.nscTextArea = "";
        this.plateTextArea = "";
        this.projectCodeTextArea = "";
        this.pseudoAtomsTextArea = "";
        this.targetTextArea = "";

        this.aliases = new ArrayList<String>();
        this.cases = new ArrayList<String>();
        this.cmpdNamedSets = new ArrayList<String>();
        this.drugNames = new ArrayList<String>();
        this.mtxtPieces = new ArrayList<String>();
        this.nscs = new ArrayList<String>();
        this.plates = new ArrayList<String>();
        this.projectCodes = new ArrayList<String>();
        this.targets = new ArrayList<String>();
        this.pseudoAtomsPieces = new ArrayList<String>();

        this.aliasesFromAutoComp = new ArrayList<String>();
        this.casesFromAutoComp = new ArrayList<String>();
        this.cmpdNamedSetsFromAutoComp = new ArrayList<String>();
        this.drugNamesFromAutoComp = new ArrayList<String>();
        this.mtxtPiecesFromAutoComp = new ArrayList<String>();
        this.nscsFromAutoComp = new ArrayList<String>();
        this.platesFromAutoComp = new ArrayList<String>();
        this.projectCodesFromAutoComp = new ArrayList<String>();
        this.targetsFromAutoComp = new ArrayList<String>();
        this.pseudoAtomsPiecesFromAutoComp = new ArrayList<String>();

    }

    public void printCriteriaLists() {

        System.out.println("In SearchCriteriaBean.printCriteriaLists()");
        
        try {

            Class thisClass = this.getClass();

            Field[] fieldArr = thisClass.getDeclaredFields();

            for (Field f : fieldArr) {
                f.setAccessible(true);
                System.out.println("fieldName: " + f.getName() + " fieldType: " + f.getType().getName());
                if (Collection.class.isAssignableFrom(f.getType())) {
                    if (f.get(this) != null) {
                        for (Object o : (Collection) f.get(this)) {
                            System.out.println("----" + o.toString());
                        }
                    }
                } else {
                    Object o = f.get(this);
                    if (o == null) {
                        System.out.println("----null");
                    } else {
                        System.out.println("----" + o.toString());
                    }
                }
            }

        } catch (IllegalArgumentException argx) {
            argx.printStackTrace();
        } catch (IllegalAccessException accx) {
            accx.printStackTrace();
        }
    }

    public void appendToaliasesFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to aliasesFromAutoComp");
            }
            this.aliasesFromAutoComp.add(str);
        }
    }

    public void appendTocasesFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to casesFromAutoComp");
            }
            this.casesFromAutoComp.add(str);
        }
    }

    public void appendTocmpdNamedSetsFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to cmpdNamedSetsFromAutoComp");
            }
            this.cmpdNamedSetsFromAutoComp.add(str);
        }
    }

    public void appendTodrugNamesFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to drugNamesFromAutoComp");
            }
            this.drugNamesFromAutoComp.add(str);
        }
    }

    public void appendTomtxtPiecesFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to mtxtPiecesFromAutoComp");
            }
            this.mtxtPiecesFromAutoComp.add(str);
        }
    }

    public void appendTonscsFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to nscsFromAutoComp");
            }
            this.nscsFromAutoComp.add(str);
        }
    }

    public void appendToplatesFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to platesFromAutoComp");
            }
            this.platesFromAutoComp.add(str);
        }
    }

    public void appendToprojectCodesFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to projectCodesFromAutoComp");
            }
            this.projectCodesFromAutoComp.add(str);
        }
    }

    public void appendTotargetsFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to targetsFromAutoComp");
            }
            this.targetsFromAutoComp.add(str);
        }
    }

    public void appendTopseudoAtomsPiecesFromAutoComp(SelectEvent event) {
        Object item = event.getObject();
        if (item != null) {
            String str = item.toString();
            if (DEBUG) {
                System.out.println("Adding: " + str + " to pseudoAtomsPiecesFromAutoComp");
            }
            this.pseudoAtomsPiecesFromAutoComp.add(str);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getCases() {
        return cases;
    }

    public void setCases(List<String> cases) {
        this.cases = cases;
    }

    public List<String> getCmpdNamedSets() {
        return cmpdNamedSets;
    }

    public void setCmpdNamedSets(List<String> cmpdNamedSets) {
        this.cmpdNamedSets = cmpdNamedSets;
    }

    public List<String> getDrugNames() {
        return drugNames;
    }

    public void setDrugNames(List<String> drugNames) {
        this.drugNames = drugNames;
    }

    public List<String> getMtxtPieces() {
        return mtxtPieces;
    }

    public void setMtxtPieces(List<String> mtxtPieces) {
        this.mtxtPieces = mtxtPieces;
    }

    public List<String> getNscs() {
        return nscs;
    }

    public void setNscs(List<String> nscs) {
        this.nscs = nscs;
    }

    public List<String> getPlates() {
        return plates;
    }

    public void setPlates(List<String> plates) {
        this.plates = plates;
    }

    public List<String> getProjectCodes() {
        return projectCodes;
    }

    public void setProjectCodes(List<String> projectCodes) {
        this.projectCodes = projectCodes;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public List<String> getPseudoAtomsPieces() {
        return pseudoAtomsPieces;
    }

    public void setPseudoAtomsPieces(List<String> pseudoAtomsPieces) {
        this.pseudoAtomsPieces = pseudoAtomsPieces;
    }

    public List<String> getAliasesFromAutoComp() {
        return aliasesFromAutoComp;
    }

    public void setAliasesFromAutoComp(List<String> aliasesFromAutoComp) {
        this.aliasesFromAutoComp = aliasesFromAutoComp;
    }

    public List<String> getCasesFromAutoComp() {
        return casesFromAutoComp;
    }

    public void setCasesFromAutoComp(List<String> casesFromAutoComp) {
        this.casesFromAutoComp = casesFromAutoComp;
    }

    public List<String> getCmpdNamedSetsFromAutoComp() {
        return cmpdNamedSetsFromAutoComp;
    }

    public void setCmpdNamedSetsFromAutoComp(List<String> cmpdNamedSetsFromAutoComp) {
        this.cmpdNamedSetsFromAutoComp = cmpdNamedSetsFromAutoComp;
    }

    public List<String> getDrugNamesFromAutoComp() {
        return drugNamesFromAutoComp;
    }

    public void setDrugNamesFromAutoComp(List<String> drugNamesFromAutoComp) {
        this.drugNamesFromAutoComp = drugNamesFromAutoComp;
    }

    public List<String> getMtxtPiecesFromAutoComp() {
        return mtxtPiecesFromAutoComp;
    }

    public void setMtxtPiecesFromAutoComp(List<String> mtxtPiecesFromAutoComp) {
        this.mtxtPiecesFromAutoComp = mtxtPiecesFromAutoComp;
    }

    public List<String> getNscsFromAutoComp() {
        return nscsFromAutoComp;
    }

    public void setNscsFromAutoComp(List<String> nscsFromAutoComp) {
        this.nscsFromAutoComp = nscsFromAutoComp;
    }

    public List<String> getPlatesFromAutoComp() {
        return platesFromAutoComp;
    }

    public void setPlatesFromAutoComp(List<String> platesFromAutoComp) {
        this.platesFromAutoComp = platesFromAutoComp;
    }

    public List<String> getProjectCodesFromAutoComp() {
        return projectCodesFromAutoComp;
    }

    public void setProjectCodesFromAutoComp(List<String> projectCodesFromAutoComp) {
        this.projectCodesFromAutoComp = projectCodesFromAutoComp;
    }

    public List<String> getTargetsFromAutoComp() {
        return targetsFromAutoComp;
    }

    public void setTargetsFromAutoComp(List<String> targetsFromAutoComp) {
        this.targetsFromAutoComp = targetsFromAutoComp;
    }

    public List<String> getPseudoAtomsPiecesFromAutoComp() {
        return pseudoAtomsPiecesFromAutoComp;
    }

    public void setPseudoAtomsPiecesFromAutoComp(List<String> pseudoAtomsPiecesFromAutoComp) {
        this.pseudoAtomsPiecesFromAutoComp = pseudoAtomsPiecesFromAutoComp;
    }

    public String getAliasTextArea() {
        return aliasTextArea;
    }

    public void setAliasTextArea(String aliasTextArea) {
        this.aliasTextArea = aliasTextArea;
    }

    public String getCasTextArea() {
        return casTextArea;
    }

    public void setCasTextArea(String casTextArea) {
        this.casTextArea = casTextArea;
    }

    public String getCmpdNamedSetTextArea() {
        return cmpdNamedSetTextArea;
    }

    public void setCmpdNamedSetTextArea(String cmpdNamedSetTextArea) {
        this.cmpdNamedSetTextArea = cmpdNamedSetTextArea;
    }

    public String getDrugNameTextArea() {
        return drugNameTextArea;
    }

    public void setDrugNameTextArea(String drugNameTextArea) {
        this.drugNameTextArea = drugNameTextArea;
    }

    public String getMtxtTextArea() {
        return mtxtTextArea;
    }

    public void setMtxtTextArea(String mtxtTextArea) {
        this.mtxtTextArea = mtxtTextArea;
    }

    public String getNscTextArea() {
        return nscTextArea;
    }

    public void setNscTextArea(String nscTextArea) {
        this.nscTextArea = nscTextArea;
    }

    public String getPlateTextArea() {
        return plateTextArea;
    }

    public void setPlateTextArea(String plateTextArea) {
        this.plateTextArea = plateTextArea;
    }

    public String getProjectCodeTextArea() {
        return projectCodeTextArea;
    }

    public void setProjectCodeTextArea(String projectCodeTextArea) {
        this.projectCodeTextArea = projectCodeTextArea;
    }

    public String getPseudoAtomsTextArea() {
        return pseudoAtomsTextArea;
    }

    public void setPseudoAtomsTextArea(String pseudoAtomsTextArea) {
        this.pseudoAtomsTextArea = pseudoAtomsTextArea;
    }

    public String getTargetTextArea() {
        return targetTextArea;
    }

    public void setTargetTextArea(String targetTextArea) {
        this.targetTextArea = targetTextArea;
    }

    public Double getMin_molecularWeight() {
        return min_molecularWeight;
    }

    public void setMin_molecularWeight(Double min_molecularWeight) {
        this.min_molecularWeight = min_molecularWeight;
    }

    public Double getMax_molecularWeight() {
        return max_molecularWeight;
    }

    public void setMax_molecularWeight(Double max_molecularWeight) {
        this.max_molecularWeight = max_molecularWeight;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public Double getMin_logD() {
        return min_logD;
    }

    public void setMin_logD(Double min_logD) {
        this.min_logD = min_logD;
    }

    public Double getMax_logD() {
        return max_logD;
    }

    public void setMax_logD(Double max_logD) {
        this.max_logD = max_logD;
    }

    public Integer getMin_countHydBondAcceptors() {
        return min_countHydBondAcceptors;
    }

    public void setMin_countHydBondAcceptors(Integer min_countHydBondAcceptors) {
        this.min_countHydBondAcceptors = min_countHydBondAcceptors;
    }

    public Integer getMax_countHydBondAcceptors() {
        return max_countHydBondAcceptors;
    }

    public void setMax_countHydBondAcceptors(Integer max_countHydBondAcceptors) {
        this.max_countHydBondAcceptors = max_countHydBondAcceptors;
    }

    public Integer getMin_countHydBondDonors() {
        return min_countHydBondDonors;
    }

    public void setMin_countHydBondDonors(Integer min_countHydBondDonors) {
        this.min_countHydBondDonors = min_countHydBondDonors;
    }

    public Integer getMax_countHydBondDonors() {
        return max_countHydBondDonors;
    }

    public void setMax_countHydBondDonors(Integer max_countHydBondDonors) {
        this.max_countHydBondDonors = max_countHydBondDonors;
    }

    public Double getMin_surfaceArea() {
        return min_surfaceArea;
    }

    public void setMin_surfaceArea(Double min_surfaceArea) {
        this.min_surfaceArea = min_surfaceArea;
    }

    public Double getMax_surfaceArea() {
        return max_surfaceArea;
    }

    public void setMax_surfaceArea(Double max_surfaceArea) {
        this.max_surfaceArea = max_surfaceArea;
    }

    public Double getMin_solubility() {
        return min_solubility;
    }

    public void setMin_solubility(Double min_solubility) {
        this.min_solubility = min_solubility;
    }

    public Double getMax_solubility() {
        return max_solubility;
    }

    public void setMax_solubility(Double max_solubility) {
        this.max_solubility = max_solubility;
    }

    public Integer getMin_countRings() {
        return min_countRings;
    }

    public void setMin_countRings(Integer min_countRings) {
        this.min_countRings = min_countRings;
    }

    public Integer getMax_countRings() {
        return max_countRings;
    }

    public void setMax_countRings(Integer max_countRings) {
        this.max_countRings = max_countRings;
    }

    public Integer getMin_countAtoms() {
        return min_countAtoms;
    }

    public void setMin_countAtoms(Integer min_countAtoms) {
        this.min_countAtoms = min_countAtoms;
    }

    public Integer getMax_countAtoms() {
        return max_countAtoms;
    }

    public void setMax_countAtoms(Integer max_countAtoms) {
        this.max_countAtoms = max_countAtoms;
    }

    public Integer getMin_countBonds() {
        return min_countBonds;
    }

    public void setMin_countBonds(Integer min_countBonds) {
        this.min_countBonds = min_countBonds;
    }

    public Integer getMax_countBonds() {
        return max_countBonds;
    }

    public void setMax_countBonds(Integer max_countBonds) {
        this.max_countBonds = max_countBonds;
    }

    public Integer getMin_countSingleBonds() {
        return min_countSingleBonds;
    }

    public void setMin_countSingleBonds(Integer min_countSingleBonds) {
        this.min_countSingleBonds = min_countSingleBonds;
    }

    public Integer getMax_countSingleBonds() {
        return max_countSingleBonds;
    }

    public void setMax_countSingleBonds(Integer max_countSingleBonds) {
        this.max_countSingleBonds = max_countSingleBonds;
    }

    public Integer getMin_countDoubleBonds() {
        return min_countDoubleBonds;
    }

    public void setMin_countDoubleBonds(Integer min_countDoubleBonds) {
        this.min_countDoubleBonds = min_countDoubleBonds;
    }

    public Integer getMax_countDoubleBonds() {
        return max_countDoubleBonds;
    }

    public void setMax_countDoubleBonds(Integer max_countDoubleBonds) {
        this.max_countDoubleBonds = max_countDoubleBonds;
    }

    public Integer getMin_countTripleBonds() {
        return min_countTripleBonds;
    }

    public void setMin_countTripleBonds(Integer min_countTripleBonds) {
        this.min_countTripleBonds = min_countTripleBonds;
    }

    public Integer getMax_countTripleBonds() {
        return max_countTripleBonds;
    }

    public void setMax_countTripleBonds(Integer max_countTripleBonds) {
        this.max_countTripleBonds = max_countTripleBonds;
    }

    public Integer getMin_countRotatableBonds() {
        return min_countRotatableBonds;
    }

    public void setMin_countRotatableBonds(Integer min_countRotatableBonds) {
        this.min_countRotatableBonds = min_countRotatableBonds;
    }

    public Integer getMax_countRotatableBonds() {
        return max_countRotatableBonds;
    }

    public void setMax_countRotatableBonds(Integer max_countRotatableBonds) {
        this.max_countRotatableBonds = max_countRotatableBonds;
    }

    public Integer getMin_countHydrogenAtoms() {
        return min_countHydrogenAtoms;
    }

    public void setMin_countHydrogenAtoms(Integer min_countHydrogenAtoms) {
        this.min_countHydrogenAtoms = min_countHydrogenAtoms;
    }

    public Integer getMax_countHydrogenAtoms() {
        return max_countHydrogenAtoms;
    }

    public void setMax_countHydrogenAtoms(Integer max_countHydrogenAtoms) {
        this.max_countHydrogenAtoms = max_countHydrogenAtoms;
    }

    public Integer getMin_countMetalAtoms() {
        return min_countMetalAtoms;
    }

    public void setMin_countMetalAtoms(Integer min_countMetalAtoms) {
        this.min_countMetalAtoms = min_countMetalAtoms;
    }

    public Integer getMax_countMetalAtoms() {
        return max_countMetalAtoms;
    }

    public void setMax_countMetalAtoms(Integer max_countMetalAtoms) {
        this.max_countMetalAtoms = max_countMetalAtoms;
    }

    public Integer getMin_countHeavyAtoms() {
        return min_countHeavyAtoms;
    }

    public void setMin_countHeavyAtoms(Integer min_countHeavyAtoms) {
        this.min_countHeavyAtoms = min_countHeavyAtoms;
    }

    public Integer getMax_countHeavyAtoms() {
        return max_countHeavyAtoms;
    }

    public void setMax_countHeavyAtoms(Integer max_countHeavyAtoms) {
        this.max_countHeavyAtoms = max_countHeavyAtoms;
    }

    public Integer getMin_countPositiveAtoms() {
        return min_countPositiveAtoms;
    }

    public void setMin_countPositiveAtoms(Integer min_countPositiveAtoms) {
        this.min_countPositiveAtoms = min_countPositiveAtoms;
    }

    public Integer getMax_countPositiveAtoms() {
        return max_countPositiveAtoms;
    }

    public void setMax_countPositiveAtoms(Integer max_countPositiveAtoms) {
        this.max_countPositiveAtoms = max_countPositiveAtoms;
    }

    public Integer getMin_countNegativeAtoms() {
        return min_countNegativeAtoms;
    }

    public void setMin_countNegativeAtoms(Integer min_countNegativeAtoms) {
        this.min_countNegativeAtoms = min_countNegativeAtoms;
    }

    public Integer getMax_countNegativeAtoms() {
        return max_countNegativeAtoms;
    }

    public void setMax_countNegativeAtoms(Integer max_countNegativeAtoms) {
        this.max_countNegativeAtoms = max_countNegativeAtoms;
    }

    public Integer getMin_countRingBonds() {
        return min_countRingBonds;
    }

    public void setMin_countRingBonds(Integer min_countRingBonds) {
        this.min_countRingBonds = min_countRingBonds;
    }

    public Integer getMax_countRingBonds() {
        return max_countRingBonds;
    }

    public void setMax_countRingBonds(Integer max_countRingBonds) {
        this.max_countRingBonds = max_countRingBonds;
    }

    public Integer getMin_countStereoAtoms() {
        return min_countStereoAtoms;
    }

    public void setMin_countStereoAtoms(Integer min_countStereoAtoms) {
        this.min_countStereoAtoms = min_countStereoAtoms;
    }

    public Integer getMax_countStereoAtoms() {
        return max_countStereoAtoms;
    }

    public void setMax_countStereoAtoms(Integer max_countStereoAtoms) {
        this.max_countStereoAtoms = max_countStereoAtoms;
    }

    public Integer getMin_countStereoBonds() {
        return min_countStereoBonds;
    }

    public void setMin_countStereoBonds(Integer min_countStereoBonds) {
        this.min_countStereoBonds = min_countStereoBonds;
    }

    public Integer getMax_countStereoBonds() {
        return max_countStereoBonds;
    }

    public void setMax_countStereoBonds(Integer max_countStereoBonds) {
        this.max_countStereoBonds = max_countStereoBonds;
    }

    public Integer getMin_countRingAssemblies() {
        return min_countRingAssemblies;
    }

    public void setMin_countRingAssemblies(Integer min_countRingAssemblies) {
        this.min_countRingAssemblies = min_countRingAssemblies;
    }

    public Integer getMax_countRingAssemblies() {
        return max_countRingAssemblies;
    }

    public void setMax_countRingAssemblies(Integer max_countRingAssemblies) {
        this.max_countRingAssemblies = max_countRingAssemblies;
    }

    public Integer getMin_countAromaticBonds() {
        return min_countAromaticBonds;
    }

    public void setMin_countAromaticBonds(Integer min_countAromaticBonds) {
        this.min_countAromaticBonds = min_countAromaticBonds;
    }

    public Integer getMax_countAromaticBonds() {
        return max_countAromaticBonds;
    }

    public void setMax_countAromaticBonds(Integer max_countAromaticBonds) {
        this.max_countAromaticBonds = max_countAromaticBonds;
    }

    public Integer getMin_countAromaticRings() {
        return min_countAromaticRings;
    }

    public void setMin_countAromaticRings(Integer min_countAromaticRings) {
        this.min_countAromaticRings = min_countAromaticRings;
    }

    public Integer getMax_countAromaticRings() {
        return max_countAromaticRings;
    }

    public void setMax_countAromaticRings(Integer max_countAromaticRings) {
        this.max_countAromaticRings = max_countAromaticRings;
    }

    public Integer getMin_formalCharge() {
        return min_formalCharge;
    }

    public void setMin_formalCharge(Integer min_formalCharge) {
        this.min_formalCharge = min_formalCharge;
    }

    public Integer getMax_formalCharge() {
        return max_formalCharge;
    }

    public void setMax_formalCharge(Integer max_formalCharge) {
        this.max_formalCharge = max_formalCharge;
    }

    public Double getMin_theALogP() {
        return min_theALogP;
    }

    public void setMin_theALogP(Double min_theALogP) {
        this.min_theALogP = min_theALogP;
    }

    public Double getMax_theALogP() {
        return max_theALogP;
    }

    public void setMax_theALogP(Double max_theALogP) {
        this.max_theALogP = max_theALogP;
    }

    // </editor-fold>
}
