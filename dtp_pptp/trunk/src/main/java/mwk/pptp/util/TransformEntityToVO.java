/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mwk.pptp.androdomain.CellLineGroup;
import mwk.pptp.androdomain.Mouse;
import mwk.pptp.androdomain.MouseCrosstab;
import mwk.pptp.androdomain.MouseGroup;
import mwk.pptp.androdomain.Summary;
import mwk.pptp.vo.CellLineGroupVO;
import mwk.pptp.vo.CellLineGroupWithMouseGroupsWithMouseCrosstabVO;
import mwk.pptp.vo.CellLineGroupWithMouseGroupsWithMousesVO;
import mwk.pptp.vo.CellLineGroupWithSummariesVO;
import mwk.pptp.vo.MouseCrosstabVO;
import mwk.pptp.vo.MouseDataShuttleVO;
import mwk.pptp.vo.MouseGraphShuttleVO;
import mwk.pptp.vo.MouseGroupVO;
import mwk.pptp.vo.MouseGroupWithMouseCrosstabVO;
import mwk.pptp.vo.MouseGroupWithMousesVO;
import mwk.pptp.vo.MouseRTVShuttleVO;
import mwk.pptp.vo.MouseSurvivalShuttleVO;
import mwk.pptp.vo.MouseVO;
import mwk.pptp.vo.SummaryShuttleVO;
import mwk.pptp.vo.SummaryVO;

/**
 *
 * @author mwkunkel
 */
public class TransformEntityToVO {

//
//
//
//
//
//
//
//
//
    /**
     * Description of the Method
     *
     * @param m Description of the Parameter
     * @return Description of the Return Value
     */
    public static Double doMedian(ArrayList<Double> l) {

        Collections.sort(l);

        int middle = l.size() / 2;

        double theMedian = -10101;

        if (l.size() == 0) {
            theMedian = -10101;
        } else if (l.size() == 1) {
            theMedian = l.get(0);
        } else if (l.size() == 2) {
            theMedian = (l.get(0) + l.get(1)) / 2.0;
        } else if (l.size() % 2 == 1) {
            theMedian = l.get(middle);
        } else {
            theMedian = (l.get(middle - 1) + l.get(middle)) / 2.0;
        }

        return theMedian;
    }

    /**
     * Description of the Class
     *
     * @author mwkunkel
     * @created April 18, 2008
     */
    public static class TempMouse {

        /**
         * Description of the Field
         */
        public int mouseNumber;
        /**
         * Description of the Field
         */
        public double value;
        /**
         * Description of the Field
         */
        public double ratio;

        /**
         * Constructor for the TempMouse object
         *
         * @param m Description of the Parameter
         * @param v Description of the Parameter
         */
        TempMouse(int m, double v) {
            this.mouseNumber = m;
            this.value = v;
            ratio = 0;
        }

        /**
         * Description of the Method
         *
         * @param o Description of the Parameter
         * @return Description of the Return Value
         */
        public boolean equals(Object o) {
            TempMouse other = (TempMouse) o;
            if (this.mouseNumber == other.mouseNumber) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static class TempHolder {

        /**
         * Description of the Field
         */
        public int day;
        /**
         * Description of the Field
         */
        public String role;
        /**
         * Description of the Field
         */
        public ArrayList mouses;

        /**
         * Constructor for the TempHolder object
         *
         * @param d Description of the Parameter
         * @param r Description of the Parameter
         */
        TempHolder(int d, String r) {
            this.day = d;
            this.role = r;
            this.mouses = new ArrayList();
        }

        /**
         * Description of the Method
         *
         * @param o Description of the Parameter
         * @return Description of the Return Value
         */
        public boolean equals(Object o) {

//equals is not a full comparison to make indexOf easier
//when resolving mouses into their TempHolder

            TempHolder other = (TempHolder) o;
            if (this.day == other.day && this.role.equals(other.role)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static List<MouseGraphShuttleVO> toMouseGraphShuttleVOs(List<CellLineGroupWithMouseGroupsWithMousesVO> listIn) throws Exception {

        List<MouseGraphShuttleVO> rtnList = new ArrayList<MouseGraphShuttleVO>();

        for (CellLineGroupWithMouseGroupsWithMousesVO vo : listIn) {
            rtnList.add(toMouseGraphShuttleVO(vo));
        }

        return rtnList;

    }

    public static MouseGraphShuttleVO toMouseGraphShuttleVO(CellLineGroupWithMouseGroupsWithMousesVO clg) throws Exception {

        try {

            MouseGraphShuttleVO returnVO = new MouseGraphShuttleVO();

            Collection<MouseDataShuttleVO> dataCollection = new ArrayList<MouseDataShuttleVO>();
            Collection<MouseSurvivalShuttleVO> survivalCollection = new ArrayList<MouseSurvivalShuttleVO>();
            Collection<MouseRTVShuttleVO> rtvCollection = new ArrayList<MouseRTVShuttleVO>();

            returnVO.setCompound(clg.getCompound());
            returnVO.setCompoundName(clg.getCompoundName());
            returnVO.setCellLine(clg.getCellLine());
            returnVO.setCellLineName(clg.getCellLineName());
            returnVO.setTestNumber(clg.getTestNumber());
            returnVO.setPanelType(clg.getPanelName());
            returnVO.setSchedule(clg.getSchedule());
            returnVO.setConcentrationUnit(clg.getConcentrationUnit());
            returnVO.setDose(clg.getDose());

//package up the mouse data
//the for loops are separate for now to try to reduce possible confusion
            MouseDataShuttleVO dVO = new MouseDataShuttleVO();
            Collection<MouseGroupWithMousesVO> mgwmColl = clg.getMouseGroupsWithMouses();

            for (MouseGroupWithMousesVO thisMgwm : mgwmColl) {
//only process if day >= 0
                if (thisMgwm.getDay().intValue() >= 0) {
                    Collection<MouseVO> mouses = thisMgwm.getMouses();
                    for (MouseVO thisMouse : mouses) {
//skip any negative tumor volumes
//set the Shuttle with logTumorVolume for solid tumors, tumorVolume (CD45) for ALL
                        if (thisMouse.getTumorVolume().doubleValue() >= 0) {
                            MouseDataShuttleVO mdsvo = new MouseDataShuttleVO();
                            mdsvo.setDay(thisMgwm.getDay());
                            mdsvo.setGroupRole(thisMgwm.getGroupRole());
                            mdsvo.setMouseNumber(thisMouse.getMouseNumber());

                            if (clg.getCellLine().startsWith("G")) {
                                mdsvo.setValue(thisMouse.getTumorVolume());
                            } else {
                                mdsvo.setValue(thisMouse.getLogTumorVolume());
                            }

                            if (mdsvo.getValue() != null) {
                                dataCollection.add(mdsvo);
                            }
                        }
                    }
                }
            }

            /*
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             *  EVENT TIMES
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             */
// event times data has day = -10101
// the tumorVolume is carrying the event time

            for (MouseGroupWithMousesVO thisMgwm : mgwmColl) {

//only process if day >= 0
                if (thisMgwm.getDay().intValue() == -10101) {
                    Collection<MouseVO> mouses = thisMgwm.getMouses();
                    for (MouseVO thisMouse : mouses) {

//skip any negative mice
                        if (thisMouse.getTumorVolume().doubleValue() >= 0) {
                            MouseSurvivalShuttleVO mssvo = new MouseSurvivalShuttleVO();
                            mssvo.setGroupRole(thisMgwm.getGroupRole());
                            mssvo.setTimeToEvent(thisMouse.getTumorVolume());
                            mssvo.setPercentEventFree(new Double(0));
                            survivalCollection.add(mssvo);
                        }
                    }
                }
            }
//determine number of starting mice
            int countControlAtStart = 0;
            int countTreatedAtStart = 0;
            for (MouseSurvivalShuttleVO thisVO : survivalCollection) {
                if (thisVO.getGroupRole().equals("CONTROL")) {
                    countControlAtStart++;
                } else {
                    countTreatedAtStart++;
                }
            }
            //System.out.println(" countControlAtStart: " + countControlAtStart + " countTreatedAtStart: " + countTreatedAtStart);
//for each mouse, determine how many had events >= that mouse
//CONTROL and TREATED are hard-coded
            for (MouseSurvivalShuttleVO refMouse : survivalCollection) {

                if (refMouse.getGroupRole().equals("CONTROL")) {
                    if (countControlAtStart > 0) {
                        int countMatches = 0;
                        for (MouseSurvivalShuttleVO testMouse : survivalCollection) {
                            if (testMouse.getGroupRole().equals("CONTROL")) {
                                if (testMouse.getTimeToEvent().doubleValue() >= refMouse.getTimeToEvent().doubleValue()) {
                                    countMatches++;
                                }
                            }
                        }
//System.out.println(" refMouse: " + refMouse.getGroupRole() + " " + refMouse.getTimeToEvent() + " matches: " + countMatches);
                        double tempDouble = (double) countMatches / (double) countControlAtStart;
                        refMouse.setPercentEventFree(new java.lang.Double(tempDouble));
                    }
                }
                if (refMouse.getGroupRole().equals("TREATED")) {
                    if (countTreatedAtStart > 0) {
                        int countMatches = 0;
                        for (MouseSurvivalShuttleVO testMouse : survivalCollection) {
                            if (testMouse.getGroupRole().equals("TREATED")) {
                                if (testMouse.getTimeToEvent().doubleValue() >= refMouse.getTimeToEvent().doubleValue()) {
                                    countMatches++;
                                }
                            }
                        }
//System.out.println(" refMouse: " + refMouse.getGroupRole() + " " + refMouse.getTimeToEvent() + " matches: " + countMatches);
                        double tempDouble = (double) countMatches / (double) countTreatedAtStart;
                        refMouse.setPercentEventFree(new java.lang.Double(tempDouble));
                    }
                }
            }

            /*
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             *  RTV
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             *  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             */
//THIS SEEMS TO BE INSANELY COMPLICATED TO DO SOMETHING SIMPLE...

//coalesce the mouse data into a series of TempHolder classes to group by day/role

            ArrayList<TempHolder> holderList = new ArrayList<TempHolder>();
            ArrayList dayZeroControl = new ArrayList();
            ArrayList dayZeroTreated = new ArrayList();

            TempHolder tempHolder = null;

            for (MouseGroupWithMousesVO thisMgwm : mgwmColl) {

//skip negative days
                if (thisMgwm.getDay().intValue() >= 0) {

                    //System.out.println("Creating Holder for day: " + thisMgwm.getDay().intValue() + " and role: " + thisMgwm.getGroupRole());
                    tempHolder = new TempHolder(thisMgwm.getDay().intValue(), thisMgwm.getGroupRole());
                    Collection<MouseVO> mouseColl = thisMgwm.getMouses();
                    for (MouseVO thisMouse : mouseColl) {
//process only positive mice or tox mice (tv = -1)
                        if (thisMouse.getTumorVolume().doubleValue() >= 0 || thisMouse.getTumorVolume().doubleValue() == -1) {
                            tempHolder.mouses.add(new TempMouse(thisMouse.getMouseNumber().intValue(), thisMouse.getTumorVolume().doubleValue()));
                        }
                    }

                    if (tempHolder.day == 0) {
                        if (tempHolder.role.equals("CONTROL")) {
                            holderList.add(tempHolder);
                            dayZeroControl = tempHolder.mouses;
                        } else if (tempHolder.role.equals("TREATED")) {
                            holderList.add(tempHolder);
                            dayZeroTreated = tempHolder.mouses;
                        }
                    } else {
                        holderList.add(tempHolder);
                    }
                }
            }

//cycle through holderList and calculate the ratio value for each mouse
//relative to its timeZero mouse
//tox mice are raplaced with 99
//for ALL the value is just the median value

// CATCH SITUATIONS WITH NO ZERO DAY DATA - Bortezomib in ALL-19, for example

            for (TempHolder currentHolder : holderList) {

                ArrayList<TempMouse> mouses = currentHolder.mouses;

                for (TempMouse currentMouse : mouses) {


                    //find the matching mouse at day 0
                    //this works because equals for Mouse is based only on mouseNumber

                    TempMouse zeroMouse = null;

                    if (currentHolder.role.equals("CONTROL")) {
                        if (dayZeroControl.indexOf(currentMouse) > -1) {
                            zeroMouse = (TempMouse) dayZeroControl.get(dayZeroControl.indexOf(currentMouse));
                        }
                    } else if (currentHolder.role.equals("TREATED")) {
                        if (dayZeroTreated.indexOf(currentMouse) > -1) {
                            zeroMouse = (TempMouse) dayZeroTreated.get(dayZeroTreated.indexOf(currentMouse));
                        }
                    }

                    if (currentMouse.value >= -1) {

                        if (currentMouse.value == -1) {
                            //if tox mouse, set ratio value depending on cell type

                            if (clg.getCellLine().startsWith("G")) {
                                currentMouse.ratio = (double) 99;
                            } else {
                                currentMouse.ratio = (double) 99 / zeroMouse.value;
                            }

                        } else {

                            if (clg.getCellLine().startsWith("G")) {
                                currentMouse.ratio = currentMouse.value;
                            } else {

                                if (zeroMouse != null) {
                                    currentMouse.ratio = currentMouse.value / zeroMouse.value;
                                } else {
                                    //will this ever occur for solid tumors?
                                }
                            }
                        }
                    } else {
                        //flag mice that aren't to be used in calculation
                        currentMouse.ratio = (double) -10101;
                    }
                }
            }

            //cycle through the days and determine the median values for all mice

            //flag to track whether we have already gone above the cutoff (RTV > 4 for solids or CD45 > 25% for ALL)
            boolean aboveLimit = false;

            for (TempHolder currentHolder : holderList) {

                ArrayList<TempMouse> mouses = currentHolder.mouses;

                //System.out.println("Number of mouses for median: " + mouses.size());

                ArrayList<Double> goodMouseRatios = new ArrayList<Double>();

                for (TempMouse currentMouse : mouses) {
                    if (currentMouse.ratio != -10101) {
                        goodMouseRatios.add(currentMouse.ratio);
                    }
                }

                double medianValue = doMedian(goodMouseRatios);

                if (!aboveLimit) {
                    if (clg.getCellLine().startsWith("G")) {
                        if (medianValue >= 25) {
                            aboveLimit = true;
                        }
                    } else {
                        if (medianValue >= 4) {
                            aboveLimit = true;
                        }
                    }

                    MouseRTVShuttleVO mrtvsvo = new MouseRTVShuttleVO();

                    mrtvsvo.setDay(currentHolder.day);
                    mrtvsvo.setGroupRole(currentHolder.role);
                    mrtvsvo.setRtv(new Double(medianValue));

                    rtvCollection.add(mrtvsvo);
                }
            }

            returnVO.setDatas(dataCollection);
            returnVO.setSurvivals(survivalCollection);
            returnVO.setRtvs(rtvCollection);

            return returnVO;

        } catch (Exception e) {
            System.out.println("Caught exception " + e + " in toMouseGraphShuttleVO in TransformEntityToVO");
            e.printStackTrace();
            throw new Exception("Caught exception " + e + " in toMouseGraphShuttleVO in TransformEntityToVO");
        }

    }

//
//
//
//
//
//
//
//
//
//
//
//
    public static List<CellLineGroupVO> toCellLineGroupVOs(List<CellLineGroup> clgList) {

        ArrayList<CellLineGroupVO> rtnList = new ArrayList<CellLineGroupVO>();

        for (CellLineGroup clg : clgList) {
            rtnList.add(toCellLineGroupVO(clg));
        }

        return rtnList;

    }

    public static CellLineGroupVO toCellLineGroupVO(CellLineGroup clg) {
        CellLineGroupVO returnVO = new CellLineGroupVO();
        returnVO.setCompound(clg.getMtdStudy().getCompoundType().getPptpIdentifier());
        returnVO.setCompoundName(clg.getMtdStudy().getCompoundType().getName());
        returnVO.setCellLine(clg.getCellLineType().getPptpIdentifier());
        returnVO.setCellLineName(clg.getCellLineType().getName());
        returnVO.setPanelName(clg.getCellLineType().getPanelType().getDisplayName());
        returnVO.setCellTypeName(clg.getCellLineType().getCellType().getDisplayName());
        returnVO.setTestNumber(clg.getTestNumber());
        returnVO.setDose(clg.getDose());
        returnVO.setConcentrationUnit(clg.getConcentrationUnitType().getDisplayName());
        returnVO.setSchedule(clg.getScheduleType().getDisplayName());
        returnVO.setCellLineSortOrder(clg.getCellLineType().getSortOrder());
        returnVO.setCellTypeSortOrder(clg.getCellLineType().getCellType().getSortOrder());
        returnVO.setCellLineGroupId(clg.getId());
        return returnVO;
    }

    public static CellLineGroupWithMouseGroupsWithMouseCrosstabVO toCellLineGroupWithMouseGroupsWithMouseCrosstabVO(CellLineGroup clg) {
        CellLineGroupWithMouseGroupsWithMouseCrosstabVO returnVO = new CellLineGroupWithMouseGroupsWithMouseCrosstabVO();
        returnVO.setCompound(clg.getMtdStudy().getCompoundType().getPptpIdentifier());
        returnVO.setCompoundName(clg.getMtdStudy().getCompoundType().getName());
        returnVO.setCellLine(clg.getCellLineType().getPptpIdentifier());
        returnVO.setCellLineName(clg.getCellLineType().getName());
        returnVO.setTestNumber(clg.getTestNumber());
        returnVO.setDose(clg.getDose());
        returnVO.setConcentrationUnit(clg.getConcentrationUnitType().getDisplayName());
        returnVO.setSchedule(clg.getScheduleType().getDisplayName());
        returnVO.setPanelName(clg.getCellLineType().getPanelType().getDisplayName());
        returnVO.setCellTypeName(clg.getCellLineType().getCellType().getDisplayName());
        returnVO.setCellLineSortOrder(clg.getCellLineType().getSortOrder());
        returnVO.setCellTypeSortOrder(clg.getCellLineType().getCellType().getSortOrder());
        returnVO.setCellLineGroupId(clg.getId());
        Collection mouseGroupValueObjectCollection = new ArrayList();
        Collection<MouseGroup> entityCollection = clg.getMouseGroups();
        for (MouseGroup entityMouseGroup : entityCollection) {
            mouseGroupValueObjectCollection.add(toMouseGroupWithMouseCrosstabVO(entityMouseGroup));
        }
        returnVO.setMouseGroupsWithMouseCrosstabs(mouseGroupValueObjectCollection);
        return returnVO;
    }

    public static List<CellLineGroupWithMouseGroupsWithMousesVO> toCellLineGroupWithMouseGroupsWithMousesVOs(List<CellLineGroup> listIn) {

        List<CellLineGroupWithMouseGroupsWithMousesVO> rtnList = new ArrayList<CellLineGroupWithMouseGroupsWithMousesVO>();

        for (CellLineGroup clg : listIn) {
            rtnList.add(toCellLineGroupWithMouseGroupsWithMousesVO(clg));
        }

        return rtnList;

    }

    public static CellLineGroupWithMouseGroupsWithMousesVO toCellLineGroupWithMouseGroupsWithMousesVO(CellLineGroup clg) {
        CellLineGroupWithMouseGroupsWithMousesVO returnVO = new CellLineGroupWithMouseGroupsWithMousesVO();
        returnVO.setCompound(clg.getMtdStudy().getCompoundType().getPptpIdentifier());
        returnVO.setCompoundName(clg.getMtdStudy().getCompoundType().getName());
        returnVO.setCellLine(clg.getCellLineType().getPptpIdentifier());
        returnVO.setCellLineName(clg.getCellLineType().getName());
        returnVO.setPanelName(clg.getCellLineType().getPanelType().getDisplayName());
        returnVO.setCellTypeName(clg.getCellLineType().getCellType().getDisplayName());
        returnVO.setTestNumber(clg.getTestNumber());
        returnVO.setDose(clg.getDose());
        returnVO.setConcentrationUnit(clg.getConcentrationUnitType().getDisplayName());
        returnVO.setSchedule(clg.getScheduleType().getDisplayName());
        returnVO.setCellLineSortOrder(clg.getCellLineType().getSortOrder());
        returnVO.setCellTypeSortOrder(clg.getCellLineType().getCellType().getSortOrder());
        returnVO.setCellLineGroupId(clg.getId());
// mouses (tumor sizes)
        Collection mouseGroupValueObjectCollection = new ArrayList();
        Collection<MouseGroup> entityCollection = clg.getMouseGroups();
        for (MouseGroup entityMouseGroup : entityCollection) {
            mouseGroupValueObjectCollection.add(toMouseGroupWithMousesVO(entityMouseGroup));
        }
        returnVO.setMouseGroupsWithMouses(mouseGroupValueObjectCollection);
        return returnVO;
    }

    public static CellLineGroupWithSummariesVO toCellLineGroupWithSummariesVO(CellLineGroup clg) {
        CellLineGroupWithSummariesVO returnVO = new CellLineGroupWithSummariesVO();
        returnVO.setCompound(clg.getMtdStudy().getCompoundType().getPptpIdentifier());
        returnVO.setCompoundName(clg.getMtdStudy().getCompoundType().getName());
        returnVO.setCellLine(clg.getCellLineType().getPptpIdentifier());
        returnVO.setCellLineName(clg.getCellLineType().getName());
        returnVO.setPanelName(clg.getCellLineType().getPanelType().getDisplayName());
        returnVO.setCellTypeName(clg.getCellLineType().getCellType().getDisplayName());
        returnVO.setTestNumber(clg.getTestNumber());
        returnVO.setDose(clg.getDose());
        returnVO.setConcentrationUnit(clg.getConcentrationUnitType().getDisplayName());
        returnVO.setSchedule(clg.getScheduleType().getDisplayName());
        returnVO.setCellLineSortOrder(clg.getCellLineType().getSortOrder());
        returnVO.setCellTypeSortOrder(clg.getCellLineType().getCellType().getSortOrder());
        returnVO.setCellLineGroupId(clg.getId());
        Collection summaryCollection = new ArrayList();
        Collection<Summary> entitySummaryCollection = clg.getSummaries();
        for (Summary entitySummary : entitySummaryCollection) {
            summaryCollection.add(toSummaryVO(entitySummary));
        }
        returnVO.setSummaries(summaryCollection);
        return returnVO;
    }
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

    public static MouseGroupVO toMouseGroupVO(MouseGroup mg) {
        MouseGroupVO thisVO = new MouseGroupVO();
        thisVO.setDay(mg.getDay());
        thisVO.setGroupRole(mg.getGroupRoleType().getDisplayName());
        return thisVO;
    }

    /**
     * @return Description of the Return Value
     * @see mwk.pptp.domain.MouseGroup#toMouseGroupWithMousesVO()
     */
    public static MouseGroupWithMousesVO toMouseGroupWithMousesVO(MouseGroup mg) {
        MouseGroupWithMousesVO thisVO = new MouseGroupWithMousesVO();
        thisVO.setDay(mg.getDay());
        thisVO.setGroupRole(mg.getGroupRoleType().getDisplayName());
        Collection<MouseVO> mouseCollection = new ArrayList();
        Collection<Mouse> entityMouseCollection = mg.getMice();
        for (Mouse entityMouse : entityMouseCollection) {
            mouseCollection.add(toMouseVO(entityMouse));
        }
        thisVO.setMouses(mouseCollection);
        return thisVO;
    }

    public static MouseGroupWithMouseCrosstabVO toMouseGroupWithMouseCrosstabVO(MouseGroup mg) {
        MouseGroupWithMouseCrosstabVO thisVO = new MouseGroupWithMouseCrosstabVO();
        thisVO.setDay(mg.getDay());
        thisVO.setGroupRole(mg.getGroupRoleType().getDisplayName());
        MouseCrosstab thisCrosstab = mg.getMouseCrosstab();
        thisVO.setMouseCrosstab(toMouseCrosstabVO(thisCrosstab));
        return thisVO;
    }
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

    public static MouseVO toMouseVO(Mouse m) {
        MouseVO thisVO = new MouseVO();
        thisVO.setMouseNumber(m.getMouseNumber());
        thisVO.setTumorVolume(m.getTumorVolume());
        thisVO.setLogTumorVolume(m.getLogTumorVolume());
        return thisVO;
    }
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

    public static MouseCrosstabVO toMouseCrosstabVO(MouseCrosstab mc) {
        java.text.NumberFormat nf3 = java.text.NumberFormat.getNumberInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);
        nf3.setGroupingUsed(false);
        MouseCrosstabVO thisVO = new MouseCrosstabVO();
        if (mc.getMouse1() != null) {
            thisVO.setMouse1(new Double(nf3.format(mc.getMouse1())));
        }
        if (mc.getMouse2() != null) {
            thisVO.setMouse2(new Double(nf3.format(mc.getMouse2())));
        }
        if (mc.getMouse3() != null) {
            thisVO.setMouse3(new Double(nf3.format(mc.getMouse3())));
        }
        if (mc.getMouse4() != null) {
            thisVO.setMouse4(new Double(nf3.format(mc.getMouse4())));
        }
        if (mc.getMouse5() != null) {
            thisVO.setMouse5(new Double(nf3.format(mc.getMouse5())));
        }
        if (mc.getMouse6() != null) {
            thisVO.setMouse6(new Double(nf3.format(mc.getMouse6())));
        }
        if (mc.getMouse7() != null) {
            thisVO.setMouse7(new Double(nf3.format(mc.getMouse7())));
        }
        if (mc.getMouse8() != null) {
            thisVO.setMouse8(new Double(nf3.format(mc.getMouse8())));
        }
        if (mc.getMouse9() != null) {
            thisVO.setMouse9(new Double(nf3.format(mc.getMouse9())));
        }
        if (mc.getMouse10() != null) {
            thisVO.setMouse10(new Double(nf3.format(mc.getMouse10())));
        }
        return thisVO;
    }
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------

    public static SummaryVO toSummaryVO(Summary s) {
        java.text.NumberFormat nf3 = java.text.NumberFormat.getNumberInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);
        nf3.setGroupingUsed(false);
        SummaryVO returnVO = new SummaryVO();
        returnVO.setTheN1(s.getTheN1());
        returnVO.setTheD1(s.getTheD1());
        returnVO.setTheE1(s.getTheE1());
        returnVO.setTheN2(s.getTheN2());
        returnVO.setSurvivingPercent(s.getSurvivingPercent());
        returnVO.setCountMouseEvents(s.getCountMouseEvents());
        if (s.getMedianDaysToEvent() != null) {
            returnVO.setMedianDaysToEvent(new Double(nf3.format(s.getMedianDaysToEvent())));
        }
        returnVO.setThePValueFlag(s.getThePValueFlag());
        if (s.getThePValue() != null) {
            returnVO.setThePValue(new Double(nf3.format(s.getThePValue())));
        }
        returnVO.setEfsTOverCFlag(s.getEfsTOverCFlag());
        if (s.getEfsTOverC() != null) {
            returnVO.setEfsTOverC(new Double(nf3.format(s.getEfsTOverC())));
        }
        returnVO.setMedianRTVFlag(s.getMedianRTVFlag());
        if (s.getMedianRTV() != null) {
            returnVO.setMedianRTV(new Double(nf3.format(s.getMedianRTV())));
        }
        if (s.getAverageRTVAtDayForTOverC() != null) {
            returnVO.setAverageRTVAtDayForTOverC(new Double(nf3.format(s.getAverageRTVAtDayForTOverC())));
        }
        if (s.getTOverC() != null) {
            returnVO.setTOverC(new Double(nf3.format(s.getTOverC())));
        }
        returnVO.setDayForTOverC(s.getDayForTOverC());
        returnVO.setCountPD(s.getCountPD());
        returnVO.setCountPD2(s.getCountPD2());
        returnVO.setCountPD1(s.getCountPD1());
        returnVO.setCountSD(s.getCountSD());
        returnVO.setCountPR(s.getCountPR());
        returnVO.setCountCR(s.getCountCR());
        returnVO.setCountMCR(s.getCountMCR());
        returnVO.setResponseMedianScore(new Double(nf3.format(s.getResponseMedianScore())));
        returnVO.setOverallGroupMedianResponse(s.getOverallGroupMedianResponse());
        returnVO.setGroupRole(s.getGroupRoleType().getDisplayName());
        return returnVO;
    }

    public static SummaryShuttleVO toSummaryShuttleVO(Summary s) {
        java.text.NumberFormat nf3 = java.text.NumberFormat.getNumberInstance();
        nf3.setMinimumFractionDigits(3);
        nf3.setMaximumFractionDigits(3);
        nf3.setGroupingUsed(false);
        SummaryShuttleVO returnVO = new SummaryShuttleVO();
        returnVO.setTheN1(s.getTheN1());
        returnVO.setTheD1(s.getTheD1());
        returnVO.setTheE1(s.getTheE1());
        returnVO.setTheN2(s.getTheN2());
        returnVO.setSurvivingPercent(s.getSurvivingPercent());
        returnVO.setCountMouseEvents(s.getCountMouseEvents());
        returnVO.setMedianDaysToEvent(new Double(nf3.format(s.getMedianDaysToEvent())));
        returnVO.setThePValueFlag(s.getThePValueFlag());
        if (s.getThePValue() != null) {
            returnVO.setThePValue(new Double(nf3.format(s.getThePValue())));
        }
        returnVO.setEfsTOverCFlag(s.getEfsTOverCFlag());
        if (s.getEfsTOverC() != null) {
            returnVO.setEfsTOverC(new Double(nf3.format(s.getEfsTOverC())));
        }
        returnVO.setMedianRTVFlag(s.getMedianRTVFlag());
        if (s.getMedianRTV() != null) {
            returnVO.setMedianRTV(new Double(nf3.format(s.getMedianRTV())));
        }
        if (s.getAverageRTVAtDayForTOverC() != null) {
            returnVO.setAverageRTVAtDayForTOverC(new Double(nf3.format(s.getAverageRTVAtDayForTOverC())));
        }
        if (s.getTOverC() != null) {
            returnVO.setTOverC(new Double(nf3.format(s.getTOverC())));
        }
        returnVO.setDayForTOverC(s.getDayForTOverC());
        returnVO.setCountPD(s.getCountPD());
        returnVO.setCountPD2(s.getCountPD2());
        returnVO.setCountPD1(s.getCountPD1());
        returnVO.setCountSD(s.getCountSD());
        returnVO.setCountPR(s.getCountPR());
        returnVO.setCountCR(s.getCountCR());
        returnVO.setCountMCR(s.getCountMCR());
        if (s.getResponseMedianScore() != null) {
            returnVO.setResponseMedianScore(new Double(nf3.format(s.getResponseMedianScore())));
        }
        returnVO.setOverallGroupMedianResponse(s.getOverallGroupMedianResponse());
        returnVO.setCompound(s.getCellLineGroup().getMtdStudy().getCompoundType().getPptpIdentifier());
        returnVO.setCompoundName(s.getCellLineGroup().getMtdStudy().getCompoundType().getName());
        returnVO.setCellLine(s.getCellLineGroup().getCellLineType().getPptpIdentifier());
        returnVO.setCellLineName(s.getCellLineGroup().getCellLineType().getName());
        returnVO.setPanelName(s.getCellLineGroup().getCellLineType().getPanelType().getDisplayName());
        returnVO.setCellTypeName(s.getCellLineGroup().getCellLineType().getCellType().getDisplayName());
        returnVO.setGroupRole(s.getGroupRoleType().getDisplayName());
        returnVO.setTestNumber(s.getCellLineGroup().getTestNumber());
        returnVO.setDose(s.getCellLineGroup().getDose());
        returnVO.setConcentrationUnit(s.getCellLineGroup().getConcentrationUnitType().getDisplayName());
        returnVO.setSchedule(s.getCellLineGroup().getScheduleType().getDisplayName());
        returnVO.setCellTypeSortOrder(s.getCellLineGroup().getCellLineType().getCellType().getSortOrder());
        returnVO.setCellLineSortOrder(s.getCellLineGroup().getCellLineType().getSortOrder());
        return returnVO;
    }
}
