/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class SessionController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;
    //    
    private String loggedUser;
    //
    private List<String> selectedStrcOptions;
    private String selectedStrcSize;
    //
    private Integer strcDim;
    // 
    private Boolean showFrags;

    @PostConstruct
    public void init() {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
            this.loggedUser = "PUBLIC";
        } else {
            this.loggedUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
            if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("NCI DCTD_PLP_DEVELOPER")) {
                this.loggedUser = "DTP_" + this.loggedUser;
            }
        }

        this.selectedStrcOptions = new ArrayList<String>();
        this.selectedStrcSize = "MED";
        this.strcDim = Integer.valueOf(200);
        this.showFrags = Boolean.TRUE;
    }

    public SessionController() {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
            this.loggedUser = "PUBLIC";
        } else {
            this.loggedUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        }
    }

    public String logout() {
        System.out.println("Now in logout in SessionController");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/webpages/availableLists.xhtml?faces-redirect=true";
    }

    public void handleLogout() {
        String rtn = this.logout();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();
        String url = extCtx.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/webpages/availableLists.xhtml"));
        try {
            extCtx.redirect(url);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
    }

    public void handleStrcOptions() {
        for (String s : this.selectedStrcOptions) {
            System.out.println("selectedStrcOptions includes: " + s);
        }
        System.out.println("selectedStrcSize is: " + this.selectedStrcSize);
        System.out.println("showFrags is: " + this.showFrags);
    }

    public String getCmpdStrcUrl(CmpdVO cVO) {
        return getCmpdStrcUrl(cVO, null);
    }

    public String getCmpdStrcUrl(CmpdVO cVO, String querySmiles) {

        String rtn = "";

        if (cVO != null) {

            StringBuilder sb = new StringBuilder();

            this.strcDim = Integer.valueOf(200);
            if (this.selectedStrcSize.equals("SM")) {
                this.strcDim = Integer.valueOf(100);
            } else if (this.selectedStrcSize.equals("MED")) {
                this.strcDim = Integer.valueOf(200);
            } else if (this.selectedStrcSize.equals("LG")) {
                this.strcDim = Integer.valueOf(400);
            } else if (this.selectedStrcSize.equals("JUMBO")) {
                this.strcDim = Integer.valueOf(800);
            }

            sb.append("/StructureServlet?structureDim=");
            sb.append(this.strcDim);

            if (cVO.getParentFragment() != null && cVO.getParentFragment().getCmpdFragmentStructure() != null && cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi() != null) {
                sb.append("&smiles=");
                sb.append(urlEncode(cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi()));
            } else if (cVO.getNsc() != null) {
                sb.append("&nsc=");
                sb.append(cVO.getNsc());
            }

            if (this.selectedStrcOptions.contains("TTL")) {
                sb.append("&title=");
                sb.append(urlEncode(cVO.getIdentifierString()));
            }

            if (this.selectedStrcOptions.contains("CLR")) {
                sb.append("&color-atoms=true");
            }

            if (this.selectedStrcOptions.contains("NUM")) {
                sb.append("&atom-numbers=true");
            }

            if (this.selectedStrcOptions.contains("KEK")) {
                sb.append("&kekule=true");
            }

            if (this.selectedStrcOptions.contains("HLT")) {
                if (querySmiles != null && querySmiles.length() > 0) {
                    sb.append("&querySmiles=");
                    sb.append(urlEncode(querySmiles));
                }
            }

            rtn = sb.toString();

        } else {
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("CmpdVO is null in getCmpdStrcUrl");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("--------------------------------------------------------------------");
        }

        System.out.println("getCmpdStrcUrl() in SessionController: ");
        System.out.println(rtn);

        return rtn;
    }

    
    
    public String getSmilesStrcUrl(String smiles) {
        return getSmilesStrcUrl(smiles, null, null);
    }

    public String getSmilesStrcUrl(String smiles, String querySmiles, String title) {

        String rtn = "";

        StringBuilder sb = new StringBuilder();

        this.strcDim = Integer.valueOf(200);
        if (this.selectedStrcSize.equals("SM")) {
            this.strcDim = Integer.valueOf(100);
        } else if (this.selectedStrcSize.equals("MED")) {
            this.strcDim = Integer.valueOf(200);
        } else if (this.selectedStrcSize.equals("LG")) {
            this.strcDim = Integer.valueOf(400);
        } else if (this.selectedStrcSize.equals("JUMBO")) {
            this.strcDim = Integer.valueOf(800);
        }

        sb.append("/StructureServlet?structureDim=");
        sb.append(this.strcDim);

        if (smiles != null && smiles.length() > 0) {
            sb.append("&smiles=");
            sb.append(urlEncode(smiles));
        }

        if (this.selectedStrcOptions.contains("HLT")) {
            if (querySmiles != null && querySmiles.length() > 0) {
                sb.append("&querySmiles=");
                sb.append(urlEncode(querySmiles));
            }
        }

        if (this.selectedStrcOptions.contains("TTL")) {
            sb.append("&title=");
            sb.append(urlEncode(title));
        }

        if (this.selectedStrcOptions.contains("CLR")) {
            sb.append("&color-atoms=true");
        }

        if (this.selectedStrcOptions.contains("NUM")) {
            sb.append("&atom-numbers=true");
        }

        if (this.selectedStrcOptions.contains("KEK")) {
            sb.append("&kekule=true");
        }

        rtn = sb.toString();

        System.out.println("getCmpdStrcUrl() in SessionController: ");
        System.out.println(rtn);

        return rtn;
    }

    public static String urlEncode(String in) {
        String rtn = "";
        try {
            rtn = URLEncoder.encode(in, "UTF-8");
        } catch (Exception e) {
        }
        return rtn;
    }

    public String getLoggedUser() {
        return this.loggedUser;
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

    public Boolean getShowFrags() {
        return showFrags;
    }

    public void setShowFrags(Boolean showFrags) {
        this.showFrags = showFrags;
    }

}
