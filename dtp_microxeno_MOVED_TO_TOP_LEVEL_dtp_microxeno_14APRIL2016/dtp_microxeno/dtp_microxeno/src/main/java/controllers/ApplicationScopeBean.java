/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.TumorVO;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import utils.Comparators;
import utils.HelperAffymetrixIdentifier;
import utils.HelperTumor;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ApplicationScopeBean implements Serializable {

    private static final long serialVersionUID = 4105761574565272777L;

    private String versionAndBuildTime;

    private List<AffymetrixIdentifierVO> affyIdentList;
    private List<TumorVO> tumorList;

    public ApplicationScopeBean() {
    }

    @PostConstruct
    public void init() {

        Properties props = new Properties();

        try {

            InputStream is = this.getClass().getResourceAsStream("/deployment.properties");
            props.load(is);

            this.versionAndBuildTime = props.getProperty("pom.version.and.build.time");

        } catch (Exception e) {
            e.printStackTrace();
        }

        createItemSelects();
    }

    private void createItemSelects() {

        affyIdentList = new ArrayList<AffymetrixIdentifierVO>();
        tumorList = new ArrayList<TumorVO>();

        try {

            List<AffymetrixIdentifierVO> aiList = HelperAffymetrixIdentifier.fetchAffymetrixIdentifiers();

            System.out.println("Size of aiList: " + aiList.size());

            for (AffymetrixIdentifierVO aiVO : aiList) {
                affyIdentList.add(aiVO);
            }

            List<TumorVO> tList = HelperTumor.fetchTumors();

            System.out.println("Size of tList: " + tList.size());

            for (TumorVO tVO : tList) {
                tumorList.add(tVO);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    public StreamedContent getTextImage() {

        int referenceSize = 20;

        StreamedContent rtn = new DefaultStreamedContent();

        FacesContext context = FacesContext.getCurrentInstance();

        System.out.println("In getTextImage with text");

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {

            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();

        } else {

            String columnHeaderText = context.getExternalContext().getRequestParameterMap().get("columnHeaderText");

            System.out.println("columnHeaderText is: " + columnHeaderText);

            int strLength = columnHeaderText.length() * referenceSize;

            try {

                BufferedImage bufferedImg = new BufferedImage(referenceSize, strLength, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = bufferedImg.createGraphics();

                g2.setPaint(Color.white);
                g2.fill(new Rectangle2D.Double(0, 0, referenceSize, strLength));

                Font font = new Font("sans-serif", Font.PLAIN, referenceSize);
                g2.setFont(font);
                g2.setPaint(Color.black);

                g2.translate(referenceSize, strLength);
                g2.rotate(-Math.PI / 2.0);

                g2.drawString(columnHeaderText, 0, 0);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImg, "png", baos);

                //FileOutputStream fos = new FileOutputStream("/tmp/image" + columnHeaderText + ".png");
                //fos.write(baos.toByteArray());
                //fos.flush();
                //fos.close();
                rtn = new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()), "image/png", columnHeaderText + ".png");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return rtn;

    }

//    
//    
// object-based autoComplete methods
//    
//    
    public List<AffymetrixIdentifierVO> compProbeSetId(String query) {        
        List<AffymetrixIdentifierVO> suggestions = new ArrayList<AffymetrixIdentifierVO>();
        for (AffymetrixIdentifierVO aiVO : affyIdentList) {
            if (StringUtils.containsIgnoreCase(aiVO.getProbeSetId(), query.trim())) {
                suggestions.add(aiVO);
            }
        }
        Collections.sort(suggestions, new Comparators.AffymetrixIdentifierComparator());
        return suggestions;
    }

    public List<AffymetrixIdentifierVO> compGeneSymbol(String query) {
        List<AffymetrixIdentifierVO> suggestions = new ArrayList<AffymetrixIdentifierVO>();
        for (AffymetrixIdentifierVO aiVO : affyIdentList) {
            if (StringUtils.containsIgnoreCase(aiVO.getGeneSymbol(), query.trim())) {
                suggestions.add(aiVO);
            }
        }
        Collections.sort(suggestions, new Comparators.AffymetrixIdentifierComparator());
        return suggestions;
    }

    public List<AffymetrixIdentifierVO> compGeneTitle(String query) {
        List<AffymetrixIdentifierVO> suggestions = new ArrayList<AffymetrixIdentifierVO>();
        for (AffymetrixIdentifierVO aiVO : affyIdentList) {
            if (StringUtils.containsIgnoreCase(aiVO.getGeneTitle(), query.trim())) {
                suggestions.add(aiVO);
            }
        }
        Collections.sort(suggestions, new Comparators.AffymetrixIdentifierComparator());
        return suggestions;
    }

    public List<TumorVO> compTumorName(String query) {
        List<TumorVO> suggestions = new ArrayList<TumorVO>();
        for (TumorVO tVO : tumorList) {
            if (StringUtils.containsIgnoreCase(tVO.getTumorName(), query.trim())) {
                suggestions.add(tVO);
            }
        }
        Collections.sort(suggestions, new Comparators.TumorComparator());
        return suggestions;
    }

    public List<TumorVO> compTumorType(String query) {
        List<TumorVO> suggestions = new ArrayList<TumorVO>();
        for (TumorVO tVO : tumorList) {
            if (StringUtils.containsIgnoreCase(tVO.getTumorType(), query.trim())) {
                suggestions.add(tVO);
            }
        }
        Collections.sort(suggestions, new Comparators.TumorComparator());
        return suggestions;
    }

// <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS">
    public String getVersionAndBuildTime() {
        return versionAndBuildTime;
    }

    public void setVersionAndBuildTime(String versionAndBuildTime) {
        this.versionAndBuildTime = versionAndBuildTime;
    }

    public List<AffymetrixIdentifierVO> getAffyIdentList() {
        return affyIdentList;
    }

    public void setAffyIdentList(List<AffymetrixIdentifierVO> affyIdentList) {
        this.affyIdentList = affyIdentList;
    }

    public List<TumorVO> getTumorList() {
        return tumorList;
    }

    public void setTumorList(List<TumorVO> tumorList) {
        this.tumorList = tumorList;
    }

    // </editor-fold>
}