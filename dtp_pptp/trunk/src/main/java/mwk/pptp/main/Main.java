/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.main;

import java.util.ArrayList;
import java.util.List;
import mwk.pptp.util.HelperCellLineGroup;
import mwk.pptp.vo.CellLineGroupVO;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static void main(String[] args) {
        testSearchCompoundData();
    }

    public static void testSearchCompoundData() {

        List<String> pptpIdentifierList = new ArrayList<String>();
//        pptpIdentifierList.add("0501");

        List<String> drugNameList = new ArrayList<String>();
        drugNameList.add("Cisplatin");

        List<String> cellNameList = new ArrayList<String>();
//        cellNameList.add("ALL-4");
//        cellNameList.add("ALL-8");

        List<String> cellTypeList = new ArrayList<String>();
        cellTypeList.add("Ewing sarcoma");
        cellTypeList.add("Glioblastoma");

        HelperCellLineGroup helper = new HelperCellLineGroup();

        List<CellLineGroupVO> detList = helper.searchCellLineGroups(pptpIdentifierList, drugNameList, cellNameList, cellTypeList);

        for (CellLineGroupVO sdVO : detList) {
            System.out.println(sdVO.getCompound() + " " + sdVO.getCellLine() + " " + sdVO.getCellTypeName());
        }

    }
}
