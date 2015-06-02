/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heatmap;

import com.google.gson.Gson;
import controllers.ColorWranglingController;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.PassageDataSetVO;
import mwk.microxeno.vo.PassageAggregateVO;
import mwk.microxeno.vo.TumorVO;
import utils.Comparators;
import utils.CrosstabModel;
import utils.TwoDHashMap;

/**
 *
 * @author mwkunkel
 */
public class HeatMapUtil {

    private static NumberFormat nf2;

    static {
        nf2 = DecimalFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
    }

    public static String makeGson(HeatMap hmm) {
        String rtn = "";
        Gson gson = new Gson();
        String json = gson.toJson(hmm);
        rtn = json;
        return rtn;
    }

    public static HeatMapHeader makeHeatMapHeader(TumorVO tVO) {
        HeatMapHeader hmh = new HeatMapHeader(tVO);
        hmh.setLabel1(tVO.getTumorName());
        hmh.setLabel2(tVO.getTumorType());
        hmh.setIdentString(tVO.getTumorType() + " " + tVO.getTumorName());

        // MWK TODO assign colors?
        // hmh.setRgbColor(ColorWranglingController.colorRgbByTumorType(tVO.getTumorType()));
        hmh.setRgbColor("white");
        return hmh;
    }

    public static HeatMapHeader makeHeatMapHeader(PassageIdentifierVO piVO) {
        HeatMapHeader hmh = null;
        try {
            hmh = new HeatMapHeader(piVO);
            hmh.setLabel1(piVO.getAffymetrixIdentifier().getGenecard() + " " + piVO.getPassage());
            hmh.setLabel2(piVO.getAffymetrixIdentifier().getProbeSetId() + " " + piVO.getPassage());
            hmh.setIdentString("AFFY_PROBE_SET_ID_" + piVO.getAffymetrixIdentifier().getProbeSetId() + " " + "_PASSAGE_" + piVO.getPassage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hmh;
    }

    public static HeatMapCell makeHeatMapCell(PassageAggregateVO paVO) {
        HeatMapCell rtn = new HeatMapCell();
        if (paVO != null && paVO.getMean() != null) {
            rtn.isNullValue = Boolean.FALSE;
            rtn.valueLabel = "level";
            rtn.valueValue = paVO.getMean();
            // for sarcoma data min=3, max=13
            // for sclc data min=3, max= 13
            // use 3, 8, 13 for low, mid, highCut
            rtn.valueColor = ColorWranglingController.colorByExpressionValue(rtn.valueValue.toString(), 0, 7, 14);
            rtn.unitDeltaLabel = "unitDelta level";
            rtn.unitDeltaValue = paVO.getUnitDeltaValue();
            rtn.unitDeltaColor = ColorWranglingController.colorByExpressionUnitDelta(rtn.unitDeltaValue.toString());
        } else {
            rtn.isNullValue = Boolean.TRUE;
            rtn.valueColor = "black";
            rtn.unitDeltaColor = "black";
        }
        return rtn;
    }

    public static HeatMap heatMapFromCrosstabModel(CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO> incoming) {

        HeatMap htMp = new HeatMap();

        htMp.title1 = "Heat Map Image";
        htMp.title2 = "This is the upper left corner.";
        htMp.title3 = "Scroll Down and/or Over to View";

        htMp.greenValue = 3d;
        htMp.yellowValue = 8d;
        htMp.redValue = 13d;
        htMp.greenUnitDeltaValue = -1d;
        htMp.yellowUnitDeltaValue = 0d;
        htMp.redUnitDeltaValue = 1d;
        htMp.dataType1 = "level";
        htMp.dataType2 = "unit Delta level";

        // Heat Map Headers from the CrosstabModel
        htMp.gridXheaderList = new ArrayList<HeatMapHeader>(incoming.getGridXheaders().size());
        htMp.gridYheaderList = new ArrayList<HeatMapHeader>(incoming.getGridYheaders().size());
        htMp.gridXY = new HeatMapCell[incoming.getGridXheaders().size()][incoming.getGridXheaders().size()];
        // running track 
        htMp.maxLenXhead = 0;
        htMp.maxLenYhead = 0;

        for (PassageIdentifierVO piVO : incoming.getGridXheaders()) {
            HeatMapHeader hmh = makeHeatMapHeader(piVO);
            htMp.gridXheaderList.add(hmh);
            if (hmh.getLabelLength() > htMp.maxLenXhead) {
                htMp.maxLenXhead = hmh.getLabelLength();
            }
        }
        for (TumorVO tVO : incoming.getGridYheaders()) {
            HeatMapHeader hmh = makeHeatMapHeader(tVO);
            htMp.gridYheaderList.add(hmh);
            if (hmh.getLabelLength() > htMp.maxLenYhead) {
                htMp.maxLenYhead = hmh.getLabelLength();
            }
        }

        for (int y = 0; y < incoming.getGridYheaders().size(); y++) {
            for (int x = 0; x < incoming.getGridXheaders().size(); x++) {
                HeatMapCell hmc = makeHeatMapCell(incoming.getGridXY()[x][y]);
                htMp.getGridXY()[x][y] = hmc;
            }
        }
        
        String gson = makeGson(htMp);
        
        htMp.setGson(gson);

        return htMp;

    }
   
}
