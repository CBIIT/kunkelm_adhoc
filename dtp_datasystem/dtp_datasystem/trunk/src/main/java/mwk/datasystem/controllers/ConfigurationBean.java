/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
public class ConfigurationBean implements Serializable {

    static final long serialVersionUID = -8653468634048142855l;

//       _       _           _               _   _                 
//  __ _| | ___ | |__   __ _| |   ___  _ __ | |_(_) ___  _ __  ___ 
// / _` | |/ _ \| '_ \ / _` | |  / _ \| '_ \| __| |/ _ \| '_ \/ __|
//| (_| | | (_) | |_) | (_| | | | (_) | |_) | |_| | (_) | | | \__ \
// \__, |_|\___/|_.__/ \__,_|_|  \___/| .__/ \__|_|\___/|_| |_|___/
// |___/                              |_|                          
    protected Boolean showFrags;
    protected Boolean showAnchor;
    protected Boolean showQc;

//     _                   _                  
// ___| |_ _ __ _   _  ___| |_ _   _ _ __ ___ 
/// __| __| '__| | | |/ __| __| | | | '__/ _ \
//\__ \ |_| |  | |_| | (__| |_| |_| | | |  __/
//|___/\__|_|   \__,_|\___|\__|\__,_|_|  \___|
//                                            
//                    _           _             
// _ __ ___ _ __   __| | ___ _ __(_)_ __   __ _ 
//| '__/ _ \ '_ \ / _` |/ _ \ '__| | '_ \ / _` |
//| | |  __/ | | | (_| |  __/ |  | | | | | (_| |
//|_|  \___|_| |_|\__,_|\___|_|  |_|_| |_|\__, |
//                                        |___/ 
    protected List<String> selectedStrcOptions;
    protected String selectedStrcSize;
    protected Integer strcDim;

//     _                             _      
//  __| |_   _ _ __   __ _ _ __ ___ (_) ___ 
// / _` | | | | '_ \ / _` | '_ ` _ \| |/ __|
//| (_| | |_| | | | | (_| | | | | | | | (__ 
// \__,_|\__, |_| |_|\__,_|_| |_| |_|_|\___|
//       |___/                              
//           _                           
//  ___ ___ | |_   _ _ __ ___  _ __  ___ 
// / __/ _ \| | | | | '_ ` _ \| '_ \/ __|
//| (_| (_) | | |_| | | | | | | | | \__ \
// \___\___/|_|\__,_|_| |_| |_|_| |_|___/
    protected List<String> availablePChemParameters;
    protected List<String> selectedPChemParameters;
    protected static HashMap<String, String> valid_physchem_keys;
    protected List<ColumnModel> physChemColumns;

    protected List<String> availableStructureParameters;
    protected List<String> selectedStructureParameters;
    protected static HashMap<String, String> valid_strc_keys;
    protected List<ColumnModel> structureColumns;

    protected List<String> availableCmpdParameters;
    protected List<String> selectedCmpdParameters;
    protected static HashMap<String, String> valid_cmpd_keys;
    protected List<ColumnModel> cmpdColumns;

    protected List<String> availableBioDataParameters;
    protected List<String> selectedBioDataParameters;
    protected static HashMap<String, String> valid_bio_keys;
    protected List<ColumnModel> biodataColumns;

    public ConfigurationBean() {

        reset();

        showFrags = Boolean.FALSE;
        showAnchor = Boolean.FALSE;
        showQc = Boolean.FALSE;

        selectedStrcOptions = new ArrayList<String>();
        selectedStrcOptions.add("TTL");
        selectedStrcOptions.add("CLR");
        selectedStrcOptions.add("HLT");
        selectedStrcSize = "MED";
        strcDim = Integer.valueOf(200);

        // cmpd
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("Prefix", "prefix");
        hm.put("NSC", "nsc");
        hm.put("Conf", "conf");
        hm.put("Distribution", "distribution");
        hm.put("CAS", "cas");
        hm.put("Name", "name");
        hm.put("adHocCmpdId", "adHocCmpdId");
        hm.put("originalAdHocCmpdId", "originalAdHocCmpdId");
        hm.put("nscCmpdId", "nscCmpdId");
        hm.put("Inventory", "inventory");
        hm.put("Aliases", "aliases");
        hm.put("Projects", "projects");
        hm.put("Plates", "plates");
        hm.put("Count Fragments", "countCmpdFragments");

        this.valid_cmpd_keys = hm;

        this.availableCmpdParameters = new ArrayList<String>(hm.keySet());
        // Collections.sort(this.availableCmpdParameters, null);

        // pChem
        hm = new HashMap<String, String>();
        hm.put("Molecular Weight", "molecularWeight");
        hm.put("Molecular Formula", "molecularFormula");
        hm.put("Formal Charge", "formalCharge");
        hm.put("aLogP", "theALogP");
        hm.put("logD", "logD");
        hm.put("Count H Bond Acceptors", "countHydBondAcceptors");
        hm.put("Count H Bond Donors", "countHydBondDonors");
        hm.put("SurfaceArea", "surfaceArea");
        hm.put("Solubility", "solubility");
        hm.put("Count Rings", "countRings");
        hm.put("Count Atoms", "countAtoms");
        hm.put("Count Bonds", "countBonds");
        hm.put("Count Single Bonds", "countSingleBonds");
        hm.put("Count Double Bonds", "countDoubleBonds");
        hm.put("Count Triple Bonds", "countTripleBonds");
        hm.put("Count Rotatable Bonds", "countRotatableBonds");
        hm.put("Count Hydrogen Atoms", "countHydrogenAtoms");
        hm.put("Count Metal Atoms", "countMetalAtoms");
        hm.put("Count Heavy Atoms", "countHeavyAtoms");
        hm.put("Count Positive Atoms", "countPositiveAtoms");
        hm.put("Count Negative Atoms", "countNegativeAtoms");
        hm.put("Count Ring Bonds", "countRingBonds");
        hm.put("Count Stereo Atoms", "countStereoAtoms");
        hm.put("Count Stereo Bonds", "countStereoBonds");
        hm.put("Count Ring Assemblies", "countRingAssemblies");
        hm.put("Count Aromatic Bonds", "countAromaticBonds");
        hm.put("Count Aromatic Rings", "countAromaticRings");

        this.valid_physchem_keys = hm;

        this.availablePChemParameters = new ArrayList<String>(hm.keySet());
        // Collections.sort(this.availablePChemParameters, null);

        // Structure
        hm = new HashMap<String, String>();
        hm.put("Canonical Smiles", "canSmi");
        hm.put("Canonical Tautomer", "canTaut");
        hm.put("Canonical Tautomer, Strip Stereo", "canTautStripStero");
        hm.put("InChI", "inchi");
        hm.put("InChI Auxilliary", "inchiAux");

        this.valid_strc_keys = hm;

        this.availableStructureParameters = new ArrayList<String>(hm.keySet());
        // Collections.sort(this.availableStructureParameters, null);

        // biodata
        hm = new HashMap<String, String>();
        hm.put("NCI60", "nci60");
        hm.put("HF", "hf");
        hm.put("XENO", "xeno");

        this.valid_bio_keys = hm;

        this.availableBioDataParameters = new ArrayList<String>(hm.keySet());
        // Collections.sort(this.availableBioDataParameters, null);

        // defaults
        selectedCmpdParameters = new ArrayList<String>();
        String[] initArr = new String[]{"NSC", "Inventory"};
        selectedCmpdParameters.addAll(Arrays.asList(initArr));

        selectedPChemParameters = new ArrayList<String>();
        initArr = new String[]{"Molecular Weight", "Molecular Formula"};
        selectedPChemParameters.addAll(Arrays.asList(initArr));

        // default is no structures 
        selectedBioDataParameters = new ArrayList<String>();
        initArr = new String[]{"NCI60"};
        selectedBioDataParameters.addAll(Arrays.asList(initArr));

    }

    public void reset() {
        this.showFrags = Boolean.FALSE;
        this.showAnchor = Boolean.FALSE;
        this.showQc = Boolean.FALSE;
        this.selectedStrcOptions = new ArrayList<String>();
        this.selectedStrcSize = selectedStrcSize;
        this.strcDim = Integer.valueOf(200);
        this.availablePChemParameters = new ArrayList<String>();
        this.selectedPChemParameters = new ArrayList<String>();
        this.physChemColumns = new ArrayList<ColumnModel>();
        this.availableStructureParameters = new ArrayList<String>();
        this.selectedStructureParameters = new ArrayList<String>();
        this.structureColumns = new ArrayList<ColumnModel>();
        this.availableCmpdParameters = new ArrayList<String>();
        this.selectedCmpdParameters = new ArrayList<String>();
        this.cmpdColumns = new ArrayList<ColumnModel>();
        this.availableBioDataParameters = new ArrayList<String>();
        this.selectedBioDataParameters = new ArrayList<String>();
        this.biodataColumns = new ArrayList<ColumnModel>();
    }

    private void createDynamicColumns() {

        this.physChemColumns = new ArrayList<ColumnModel>();
        for (String columnKey : this.selectedPChemParameters) {
            String key = columnKey.trim();
            if (valid_physchem_keys.containsKey(key)) {
                this.physChemColumns.add(new ColumnModel(key, valid_physchem_keys.get(key)));
            }
        }

        this.structureColumns = new ArrayList<ColumnModel>();
        for (String columnKey : this.selectedStructureParameters) {
            String key = columnKey.trim();
            if (valid_strc_keys.containsKey(key)) {
                this.structureColumns.add(new ColumnModel(key, valid_strc_keys.get(key)));
            }
        }

        this.cmpdColumns = new ArrayList<ColumnModel>();
        for (String columnKey : this.selectedCmpdParameters) {
            String key = columnKey.trim();
            if (valid_cmpd_keys.containsKey(key)) {
                this.cmpdColumns.add(new ColumnModel(key, valid_cmpd_keys.get(key)));
            }
        }

        this.biodataColumns = new ArrayList<ColumnModel>();
        for (String columnKey : this.selectedBioDataParameters) {
            String key = columnKey.trim();
            if (valid_bio_keys.containsKey(key)) {
                this.biodataColumns.add(new ColumnModel(key, valid_bio_keys.get(key)));
            }
        }

    }

    public String performUpdateColumns() {

        try {
            createDynamicColumns();
        } catch (Exception e) {
            System.out.println("Exception in performUpdateColumns");
            e.printStackTrace();
        }

        return "/webpages/activeListTable.xhtml?faces-redirect=true";

    }

    static public class ColumnModel implements Serializable {

        private String header;
        private String property;

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }

    }

    public void handleStrcOptions() {
//        for (String s : selectedStrcOptions) {
//            System.out.println("selectedStrcOptions includes: " + s);
//        }
//        System.out.println("selectedStrcSize is: " + selectedStrcSize);
//        System.out.println("showFrags is: " + showFrags);
//        System.out.println("showAnchor is: " + showAnchor);
    }

    public String getSmilesStrcUrl(String smiles, String querySmiles, String title) {
        // System.out.println("In getSmilesStrcUrl(smiles, querySmiles, title)");
        // System.out.println("smiles is: " + smiles);
        String rtn = "";
        StringBuilder sb = new StringBuilder();
        strcDim = Integer.valueOf(200);
        if (selectedStrcSize.equals("SM")) {
            strcDim = Integer.valueOf(100);
        } else if (selectedStrcSize.equals("MED")) {
            strcDim = Integer.valueOf(200);
        } else if (selectedStrcSize.equals("LG")) {
            strcDim = Integer.valueOf(400);
        } else if (selectedStrcSize.equals("JUMBO")) {
            strcDim = Integer.valueOf(800);
        }
        sb.append("/StructureServlet?structureDim=");
        sb.append(strcDim);
        if (smiles != null && smiles.length() > 0) {
            sb.append("&smiles=");
            sb.append(SessionController.urlEncode(smiles));
        }
        if (selectedStrcOptions.contains("HLT")) {
            if (querySmiles != null && querySmiles.length() > 0) {
                sb.append("&querySmiles=");
                sb.append(SessionController.urlEncode(querySmiles));
            }
        }
        if (selectedStrcOptions.contains("TTL")) {
            sb.append("&title=");
            sb.append(SessionController.urlEncode(title));
        }
        if (selectedStrcOptions.contains("CLR")) {
            sb.append("&color-atoms=true");
        }
        if (selectedStrcOptions.contains("NUM")) {
            sb.append("&atom-numbers=true");
        }
        if (selectedStrcOptions.contains("KEK")) {
            sb.append("&kekule=true");
        }
        rtn = sb.toString();
        // System.out.println("getSmilesStrcUrl() in SessionController: ");
        // System.out.println("rtn from getSmilesStrcUrl is: " + rtn);
        return rtn;
    }

    public String getSmilesStrcUrl(String smiles) {
        // System.out.println("In getSmilesStrcUrl(smiles)");
        // System.out.println("smiles is: " + smiles);
        return getSmilesStrcUrl(smiles, null, null);
    }

    public String getCmpdStrcUrl(CmpdVO cVO, String querySmiles) {
        String rtn = "";
        if (cVO != null) {
            StringBuilder sb = new StringBuilder();
            strcDim = Integer.valueOf(200);
            if (selectedStrcSize.equals("SM")) {
                strcDim = Integer.valueOf(100);
            } else if (selectedStrcSize.equals("MED")) {
                strcDim = Integer.valueOf(200);
            } else if (selectedStrcSize.equals("LG")) {
                strcDim = Integer.valueOf(400);
            } else if (selectedStrcSize.equals("JUMBO")) {
                strcDim = Integer.valueOf(800);
            }
            sb.append("/StructureServlet?structureDim=");
            sb.append(strcDim);
            if (cVO.getParentFragment() != null && cVO.getParentFragment().getCmpdFragmentStructure() != null && cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi() != null) {
                sb.append("&smiles=");
                sb.append(SessionController.urlEncode(cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi()));
            } else if (cVO.getNsc() != null) {
                sb.append("&nsc=");
                sb.append(cVO.getNsc());
            }
            if (selectedStrcOptions.contains("TTL")) {
                sb.append("&title=");
                if (cVO.getPrefix() != null && cVO.getNsc() != null) {
                    sb.append(SessionController.urlEncode(cVO.getPrefix() + cVO.getNsc()));
                } else if (cVO.getName() != null) {
                    sb.append(SessionController.urlEncode(cVO.getName()));
                } else {
                    sb.append("Can't determine title from cmpdVO.");
                }
            }
            if (selectedStrcOptions.contains("CLR")) {
                sb.append("&color-atoms=true");
            }
            if (selectedStrcOptions.contains("NUM")) {
                sb.append("&atom-numbers=true");
            }
            if (selectedStrcOptions.contains("KEK")) {
                sb.append("&kekule=true");
            }
            if (selectedStrcOptions.contains("HLT")) {
                if (querySmiles != null && querySmiles.length() > 0) {
                    sb.append("&querySmiles=");
                    sb.append(SessionController.urlEncode(querySmiles));
                }
            }
            rtn = sb.toString();
        } else {

            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("CmpdVO is null in getCmpdStrcUrl in SessionController");
            System.out.println("querySmiles is: " + querySmiles);
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");

        }
        return rtn;
    }

    public String getCmpdStrcUrl(CmpdVO cVO) {
        return getCmpdStrcUrl(cVO, null);
    }

    public Boolean getShowFrags() {
        return showFrags;
    }

    public void setShowFrags(Boolean showFrags) {
        this.showFrags = showFrags;
    }

    public Boolean getShowAnchor() {
        return showAnchor;
    }

    public void setShowAnchor(Boolean showAnchor) {
        this.showAnchor = showAnchor;
    }

    public Boolean getShowQc() {
        return showQc;
    }

    public void setShowQc(Boolean showQc) {
        this.showQc = showQc;
    }

    public List<String> getSelectedStrcOptions() {
        return selectedStrcOptions;
    }

    public void setSelectedStrcOptions(List<String> selectedStrcOptions) {
        this.selectedStrcOptions = selectedStrcOptions;
    }

    public String getSelectedStrcSize() {
        return selectedStrcSize;
    }

    public void setSelectedStrcSize(String selectedStrcSize) {
        this.selectedStrcSize = selectedStrcSize;
    }

    public Integer getStrcDim() {
        return strcDim;
    }

    public void setStrcDim(Integer strcDim) {
        this.strcDim = strcDim;
    }

    public List<String> getAvailablePChemParameters() {
        return availablePChemParameters;
    }

    public void setAvailablePChemParameters(List<String> availablePChemParameters) {
        this.availablePChemParameters = availablePChemParameters;
    }

    public List<String> getSelectedPChemParameters() {
        return selectedPChemParameters;
    }

    public void setSelectedPChemParameters(List<String> selectedPChemParameters) {
        this.selectedPChemParameters = selectedPChemParameters;
    }

    public static HashMap<String, String> getValid_physchem_keys() {
        return valid_physchem_keys;
    }

    public static void setValid_physchem_keys(HashMap<String, String> valid_physchem_keys) {
        ConfigurationBean.valid_physchem_keys = valid_physchem_keys;
    }

    public List<ColumnModel> getPhysChemColumns() {
        return physChemColumns;
    }

    public void setPhysChemColumns(List<ColumnModel> physChemColumns) {
        this.physChemColumns = physChemColumns;
    }

    public List<String> getAvailableStructureParameters() {
        return availableStructureParameters;
    }

    public void setAvailableStructureParameters(List<String> availableStructureParameters) {
        this.availableStructureParameters = availableStructureParameters;
    }

    public List<String> getSelectedStructureParameters() {
        return selectedStructureParameters;
    }

    public void setSelectedStructureParameters(List<String> selectedStructureParameters) {
        this.selectedStructureParameters = selectedStructureParameters;
    }

    public static HashMap<String, String> getValid_strc_keys() {
        return valid_strc_keys;
    }

    public static void setValid_strc_keys(HashMap<String, String> valid_strc_keys) {
        ConfigurationBean.valid_strc_keys = valid_strc_keys;
    }

    public List<ColumnModel> getStructureColumns() {
        return structureColumns;
    }

    public void setStructureColumns(List<ColumnModel> structureColumns) {
        this.structureColumns = structureColumns;
    }

    public List<String> getAvailableCmpdParameters() {
        return availableCmpdParameters;
    }

    public void setAvailableCmpdParameters(List<String> availableCmpdParameters) {
        this.availableCmpdParameters = availableCmpdParameters;
    }

    public List<String> getSelectedCmpdParameters() {
        return selectedCmpdParameters;
    }

    public void setSelectedCmpdParameters(List<String> selectedCmpdParameters) {
        this.selectedCmpdParameters = selectedCmpdParameters;
    }

    public static HashMap<String, String> getValid_cmpd_keys() {
        return valid_cmpd_keys;
    }

    public static void setValid_cmpd_keys(HashMap<String, String> valid_cmpd_keys) {
        ConfigurationBean.valid_cmpd_keys = valid_cmpd_keys;
    }

    public List<ColumnModel> getCmpdColumns() {
        return cmpdColumns;
    }

    public void setCmpdColumns(List<ColumnModel> cmpdColumns) {
        this.cmpdColumns = cmpdColumns;
    }

    public List<String> getAvailableBioDataParameters() {
        return availableBioDataParameters;
    }

    public void setAvailableBioDataParameters(List<String> availableBioDataParameters) {
        this.availableBioDataParameters = availableBioDataParameters;
    }

    public List<String> getSelectedBioDataParameters() {
        return selectedBioDataParameters;
    }

    public void setSelectedBioDataParameters(List<String> selectedBioDataParameters) {
        this.selectedBioDataParameters = selectedBioDataParameters;
    }

    public static HashMap<String, String> getValid_bio_keys() {
        return valid_bio_keys;
    }

    public static void setValid_bio_keys(HashMap<String, String> valid_bio_keys) {
        ConfigurationBean.valid_bio_keys = valid_bio_keys;
    }

    public List<ColumnModel> getBiodataColumns() {
        return biodataColumns;
    }

    public void setBiodataColumns(List<ColumnModel> biodataColumns) {
        this.biodataColumns = biodataColumns;
    }

}
