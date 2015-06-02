/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import heatmap.HeatMap;
import heatmap.HeatMapHeader;
import heatmap.HeatMapUtil;
import java.util.Arrays;
import java.util.List;
import mwk.microxeno.vo.AffymetrixDataVO;
import mwk.microxeno.vo.PassageAggregateVO;
import mwk.microxeno.vo.PassageDataSetVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.TumorVO;
import utils.CollateDataSetUtil;
import utils.CrosstabModel;
import utils.CrosstabUtil;
import utils.HelperFlatData;
import utils.SerializeDeSerialize;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static void main(String[] args) {

        //testPassageAggregate();
        //testDataFetch();
        
        testFlatDataFetch();

    }

    public static void testPassageAggregate() {

        SerializeDeSerialize<List<AffymetrixDataVO>> sd = new SerializeDeSerialize<List<AffymetrixDataVO>>("/tmp/dataList.ser");

        List<AffymetrixDataVO> dataList = sd.deserialize();

        List<PassageAggregateVO> paList = CollateDataSetUtil.aggregateByPassage(dataList);

        List<PassageDataSetVO> dataSetList = CollateDataSetUtil.collateDataSet(paList);

        CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO> ctm = CrosstabUtil.makeCrosstabModel(paList);

        HeatMap heatMap = HeatMapUtil.heatMapFromCrosstabModel(ctm);

        System.out.println("Size of dataList: " + dataList.size());
        System.out.println("Size of paList: " + paList.size());
        System.out.println("Size of dataSetList: " + dataSetList.size());

        for (PassageDataSetVO dsVO : dataSetList) {
            System.out.println("dataSetList: " + dsVO.getPassageIdentifier().getAffymetrixIdentifier().getProbeSetId() + " " + dsVO.getPassageIdentifier().getPassage());
        }

        System.out.println("Size of crosstabModel: X: " + ctm.getGridXheaders().size());
        System.out.println("Size of crosstabModel: Y: " + ctm.getGridYheaders().size());

        for (PassageIdentifierVO piVO : ctm.getGridXheaders()) {
            System.out.println("crossTabModel: " + piVO.getAffymetrixIdentifier().getProbeSetId() + " " + piVO.getPassage());
        }

        System.out.println("Size of heatMap: X: " + heatMap.getGridXheaderList().size());
        System.out.println("Size of heatMap: Y: " + heatMap.getGridYheaderList().size());

        for (HeatMapHeader hmh : heatMap.getGridXheaderList()) {
            System.out.println("heatMap: " + hmh.getIdentString());
        }

    }

    public static void testFlatDataFetch() {

        String[] geneArray4 = new String[]{
            "P2RX1",
            "P2RX2",
            "P2RX3",
            "P2RX4",
            "P2RX5",
            "P2RX5-TAX1BP3",
            "P2RX6",
            "P2RX7",
            "P2RY1",
            "P2RY10",
            "P2RY11",
            "P2RY12",
            "P2RY13",
            "P2RY14",
            "P2RY2",
            "P2RY4",
            "P2RY6",
            "P2RY8",
            "P4HA1",
            "P4HA2",
            "P4HA3",
            "P4HB",
            "P4HTM",
            "PA2G4",
            "PAAF1",
            "PABPC1",
            "PABPC1L",
            "PABPC1L2A",
            "PABPC1",
            "PABPC3",
            "PABPC4",
            "PABPC4L",
            "PABPC5",
            "PABPN1",
            "PACRG"
        };

        String[] geneArray3 = new String[]{
            "ZACN",
            "ZADH2",
            "ZAK",
            "ZAN",
            "ZAP70",
            "ZAR1",
            "ZBBX",
            "ZBED1",
            "ZBED2",
            "ZBED3",
            "ZBED3-AS1",
            "ZBED4",
            "ZBED5",
            "ZBED6",
            "ZBP1",
            "ZBTB1",
            "ZBTB10",
            "ZBTB11",
            "ZBTB12",
            "ZBTB16",
            "ZBTB17",
            "ZBTB2",
            "ZBTB20",
            "ZBTB20-AS1",
            "ZBTB22",
            "ZBTB24",
            "ZBTB25",
            "ZBTB26",
            "ZBTB3",
            "ZBTB32",
            "ZBTB33",
            "ZBTB34",
            "ZBTB37",
            "ZBTB38",
            "ZBTB39"
        };

        String[] geneArray2 = new String[]{
            "XAB2",
            "XAF1",
            "XAGE1A",
            "XAGE2",
            "XAGE3",
            "XAGE-4",
            "XBP1",
            "XCL1",
            "XCL1",
            "XCR1",
            "XDH",
            "XG",
            "XG",
            "XIAP",
            "XIRP1",
            "XIRP2",
            "XIST",
            "XK",
            "XKR4",
            "XKR6",
            "XKR8",
            "XKRX",
            "XKRY",
            "XPA",
            "XPC",
            "XPNPEP1",
            "XPNPEP2",
            "XPNPEP3",
            "XPO1",
            "XPO4",
            "XPO5",
            "XPO6",
            "XPO7",
            "XPOT",
            "XPR1"
        };

        String[] geneArray = new String[]{
            "A1BG",
            "A1BG-AS1",
            "A1CF",
            "A2LD1",
            "A2M",
            "A2M-AS1",
            "A2ML1",
            "A2MP1",
            "A4GALT",
            "A4GNT",
            "AA06",
            "AAAS",
            "AACS",
            "AACSP1",
            "AADAC",
            "AADACL2",
            "AADAT",
            "AAED1",
            "AAGAB",
            "AAK1",
            "AAMP",
            "AANAT",
            "AARS",
            "AARS2",
            "AARSD1",
            "PTGES3L",
            "AASDH",
            "AASDHPPT",
            "AASS",
            "AATF"
        };

        String[] tumorArray = new String[]{
            "OVCAR-5",
            "RXF 393",
            "SW-620",
            "HL-60(TB)",
            "HOP-92",
            "HuH-7",
            "SN12C",
            "EKVX",
            "MOLT-4",
            "NCI-H460",
            "COLO 205",
            "HT-29",
            "SF-268",
            "CA46",
            "MDA-N",
            "NCI-H23",
            "HCC-2998",
            "U251",
            "HCT-116",
            "U251",
            "COLO 829",
            "AS283",
            "MCF7",
            "CP70",
            "MALME-3M",
            "CAKI-1",
            "SR",
            "CCRF-CEM",
            "MDA-MB-435",
            "NCI-H226"
        };

        List<String> geneList = Arrays.asList(geneArray);
        List<String> tumorList = Arrays.asList(tumorArray);

        HelperFlatData helper = new HelperFlatData();
        List<AffymetrixDataVO> dataList = helper.searchAffymetrixData(geneList, tumorList);

        System.out.println("Size of dataList: " + dataList.size());

//        for (AffymetrixDataVO adVO : dataList) {
//            System.out.println(adVO.getAffymetrixIdentifier().getGenecard() + " " + adVO.getTumor().getTumorName() + " " + adVO.getValue());
//        }
    }

}
