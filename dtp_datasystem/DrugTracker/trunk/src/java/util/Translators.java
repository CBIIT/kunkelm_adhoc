/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import jpa.entity.AdHocDrugTracker;
import jpa.entity.DrugTracker;
import jpa.entity.DrugTrackerAlias;
import jpa.entity.DrugTrackerPlate;
import jpa.entity.DrugTrackerSet;
import jpa.entity.DrugTrackerTarget;
import jpa.entity.NscDrugTracker;
import jpa.entity.StandardizedSmiles;
import vo.DrugTrackerVO;

/**
 *
 * @author mwkunkel
 */
public class Translators {

  public static DrugTrackerVO toDrugTrackerVO(DrugTracker dt) {

    DrugTrackerVO rtn = new DrugTrackerVO();

    rtn.setId(dt.getId());

    try {

      Collection<DrugTrackerAlias> aliasColl = dt.getDrugTrackerAliasCollection();
      Collection<String> strColl = new ArrayList<String>();
      for (DrugTrackerAlias a : aliasColl) {
        strColl.add(a.getAlias());
      }
      rtn.setDrugTrackerAliases(strColl);

      Collection<DrugTrackerTarget> targetColl = dt.getDrugTrackerTargetCollection();
      strColl = new ArrayList<String>();
      for (DrugTrackerTarget t : targetColl) {
        strColl.add(t.getTarget());
      }
      rtn.setTargets(strColl);

      String drugTrackerSetString = "";
      for (DrugTrackerSet dts : dt.getDrugTrackerSetCollection()) {
        drugTrackerSetString += dts.getSetName() + " ";
      }
      rtn.setDrugTrackerSet(drugTrackerSetString);

      rtn.setOriginator(dt.getOriginator());

      rtn.setAgent(dt.getAgent());

      String drugTrackerPlateString = "";
      for (DrugTrackerPlate dtp : dt.getDrugTrackerPlateCollection()) {
        drugTrackerPlateString += dtp.getPlateName() + " ";
      }
      rtn.setPlate(drugTrackerPlateString);

      if (dt.getNscDrugTracker() != null) {

        NscDrugTracker ndt = dt.getNscDrugTracker();

        // brand new NSC numbers may not have full information

        if (ndt.getStandardizedSmilesFk() != null) {

          StandardizedSmiles ss = ndt.getStandardizedSmilesFk();

          rtn.setCas(ss.getCas());
          rtn.setPrefix(ss.getPrefix());
          rtn.setConf(ss.getConf());
          rtn.setDistribution(ss.getDistribution());

          // have to catch these potential nulls since the hibernate model is using Integer

          if (ss.getNsc() != null) {
            rtn.setNsc(Long.valueOf(ss.getNsc()));
          }

          if (ss.getHf() != null) {
            rtn.setHf(Long.valueOf(ss.getHf()));
          }

          if (ss.getNci60() != null) {
            rtn.setNci60(Long.valueOf(ss.getNci60()));
          }

          if (ss.getXeno() != null) {
            rtn.setXeno(Long.valueOf(ss.getXeno()));
          }

          if (ss.getHba() != null) {
            rtn.setHba(Long.valueOf(ss.getHba()));
          }

          if (ss.getHbd() != null) {
            rtn.setHbd(Long.valueOf(ss.getHbd()));
          }


          rtn.setInventory(ss.getInventory());

          rtn.setParentCanSmi(ss.getParentCanSmi());
          rtn.setProdCanSmi(ss.getProdCanSmi());
          rtn.setRemovedSalts(ss.getRemovedSalts());
          rtn.setSmilesFromProd(ss.getSmilesFromProd());
          rtn.setStructureSource(ss.getStructureSource());
          rtn.setTautCanSmi(ss.getTautCanSmi());
          rtn.setTautNostereoCanSmi(ss.getTautNostereoCanSmi());

          rtn.setMf(ss.getMf());
          rtn.setMw(ss.getMw());
          rtn.setAlogp(ss.getAlogp());
          rtn.setLogd(ss.getLogd());
          rtn.setSa(ss.getSa());
          rtn.setPsa(ss.getPsa());

        }

      } else if (dt.getAdHocDrugTracker() != null) {

        AdHocDrugTracker adt = dt.getAdHocDrugTracker();

        rtn.setParentCanSmi(adt.getSmiles());
        rtn.setRemovedSalts(adt.getRemovedSalts());
        rtn.setStructureSource(adt.getStructureSource());

        rtn.setMf(adt.getMf());
        rtn.setMw(adt.getMw());
        rtn.setAlogp(adt.getAlogp());
        rtn.setLogd(adt.getLogd());
        if (adt.getHba() != null) {
          rtn.setHba(Long.valueOf(adt.getHba()));
        }
        if (adt.getHbd() != null) {
          rtn.setHbd(Long.valueOf(adt.getHbd()));
        }
        rtn.setSa(adt.getSa());
        rtn.setPsa(adt.getPsa());
      }

    } catch (Exception e) {
      System.out.println("Caught exception in toDrugTrackerVO() in DrugTrackerImpl " + e);
      e.printStackTrace();
    }

    return rtn;
  }
}
