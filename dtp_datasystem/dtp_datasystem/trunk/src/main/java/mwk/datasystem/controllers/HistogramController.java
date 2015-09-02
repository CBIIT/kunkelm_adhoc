package mwk.datasystem.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import mwk.datasystem.util.TemplPropUtil;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class HistogramController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;
    
    private String json;

    // reach-through to sessionController
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    private List<String> intProps;
    private List<String> dblProps;
    private List<String> intParams;
    private List<String> dblParams;

    private List<CmpdListMemberVO> cmpdListMembers;
    private List<CmpdListMemberVO> selectedCmpdListMembers;

    private TemplPropUtil propUtil;

    public HistogramController() {
        init();
    }

    // don't do this for now 'cuz calls it too often... @PostConstruct
    public void init() {

          ArrayList<String> ignList = new ArrayList<String>();
        ignList.add("id");
        ignList.add("serialVersionUID");

        ArrayList<String> reqList = new ArrayList<String>();
        reqList.add("name");
        reqList.add("cmpdFragmentPChem");

        this.propUtil = new TemplPropUtil<CmpdListMemberVO>(new CmpdListMemberVO(), ignList, reqList);
        this.intProps = this.propUtil.getIntProps();
        this.dblProps = this.propUtil.getDblProps();

        this.intParams = new ArrayList<String>(Arrays.asList(new String[]{
            "countHydBondAcceptors",
            "countHydBondDonors"}));

        this.dblParams = new ArrayList<String>(Arrays.asList(new String[]{
            "molecularWeight",
            "surfaceArea"}));

        this.cmpdListMembers = new ArrayList<CmpdListMemberVO>();
        this.selectedCmpdListMembers = new ArrayList<CmpdListMemberVO>();

    }

    public void handleLoadActiveList() {

        String rtn = performLoadActiveList();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();
        String url = extCtx.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/webpages/d3/activeListHistoScat.xhtml"));

        try {
            extCtx.redirect(url);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }

    }

    public String performLoadActiveList() {

        this.cmpdListMembers = new ArrayList<CmpdListMemberVO>(listManagerController.getListManagerBean().activeList.getCmpdListMembers());
        this.selectedCmpdListMembers = new ArrayList<CmpdListMemberVO>();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.cmpdListMembers);
        try {
            File f = new File("/tmp/cmpdListMembers.json");
            FileWriter fw = new FileWriter(f);
            fw.write(json);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.json = json;

        return "/webpages/d3/activeListHistoScat?faces-redirect=true";
    }

    public String performClearSelections() {

        for (CmpdListMemberVO clmVO : this.cmpdListMembers) {
            clmVO.setIsSelected(Boolean.FALSE);
        }

        this.selectedCmpdListMembers.clear();

        return "/webpages/activeListHistograms?faces-redirect=true";
    }

  
    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
    
    public List<String> getIntProps() {
        return intProps;
    }

    public void setIntProps(List<String> intProps) {
        this.intProps = intProps;
    }

    public List<String> getDblProps() {
        return dblProps;
    }

    public void setDblProps(List<String> dblProps) {
        this.dblProps = dblProps;
    }

    public List<String> getIntParams() {
        return intParams;
    }

    public void setIntParams(List<String> intParams) {
        this.intParams = intParams;
    }

    public List<String> getDblParams() {
        return dblParams;
    }

    public void setDblParams(List<String> dblParams) {
        this.dblParams = dblParams;
    }

    public List<String> getCombinedParams() {
        ArrayList<String> rtn = new ArrayList<String>();

        rtn.addAll(this.getIntParams());
        rtn.addAll(this.getDblParams());

        return rtn;
    }

    public List<CmpdListMemberVO> getCmpdListMembers() {
        return cmpdListMembers;
    }

    public void setCmpdListMembers(List<CmpdListMemberVO> cmpdListMembers) {
        this.cmpdListMembers = cmpdListMembers;
    }

    public List<CmpdListMemberVO> getSelectedCmpdListMembers() {
        return selectedCmpdListMembers;
    }

    public void setSelectedCmpdListMembers(List<CmpdListMemberVO> selectedCmpdListMembers) {
        this.selectedCmpdListMembers = selectedCmpdListMembers;
    }

      // </editor-fold>
}
