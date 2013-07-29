/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.util;

import java.util.ArrayList;
import java.util.List;
import mwk.pptp.androdomain.CellLineGroup;
import mwk.pptp.vo.CellLineGroupVO;
import mwk.pptp.vo.CellLineGroupWithMouseGroupsWithMousesVO;
import mwk.pptp.vo.MouseGraphShuttleVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class HelperCellLineGroup {

  Session session = null;

  public HelperCellLineGroup() {
    this.session = HibernateUtil.getSessionFactory().getCurrentSession();
  }

  public List<MouseGraphShuttleVO> getGraphs(List<Long> cellLineGroupIds) {

    List<MouseGraphShuttleVO> rtnList = new ArrayList<MouseGraphShuttleVO>();

    try {

      Transaction tx = session.beginTransaction();

      Criteria crit = session.createCriteria(CellLineGroup.class);

      crit.add(Restrictions.in("id", cellLineGroupIds));

      List<CellLineGroup> entityList = (List<CellLineGroup>) crit.list();
      List<CellLineGroupWithMouseGroupsWithMousesVO> voList = TransformEntityToVO.toCellLineGroupWithMouseGroupsWithMousesVOs(entityList);
      rtnList = TransformEntityToVO.toMouseGraphShuttleVOs(voList);
      tx.commit();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return rtnList;

  }

  public List<CellLineGroupVO> searchCellLineGroups(
          List<String> pptpIdentifierList,
          List<String> drugNameList,
          List<String> cellNameList,
          List<String> cellTypeList) {

    List<CellLineGroupVO> rtnList = new ArrayList<CellLineGroupVO>();

    // CATCH NULLS!
    
    if (pptpIdentifierList == null) {
      pptpIdentifierList = new ArrayList<String>();
    }
    if (drugNameList == null) {
      drugNameList = new ArrayList<String>();
    }
    if (cellNameList == null) {
      cellNameList = new ArrayList<String>();
    }
    if (cellTypeList == null) {
      cellTypeList = new ArrayList<String>();
    }

    try {

      Transaction tx = session.beginTransaction();

      Criteria crit = session.createCriteria(CellLineGroup.class)
              .createAlias("cellLineType", "clt")
              .createAlias("cellLineType.cellType", "ct")
              .createAlias("mtdStudy", "mtds")
              .createAlias("mtds.compoundType", "cmpd");

      if (!pptpIdentifierList.isEmpty() && !drugNameList.isEmpty()) {
        crit.add(Restrictions.disjunction()
                .add(Restrictions.in("cmpd.pptpIdentifier", pptpIdentifierList))
                .add(Restrictions.in("cmpd.name", drugNameList)));
      } else if (!pptpIdentifierList.isEmpty()) {
        crit.add(Restrictions.in("cmpd.pptpIdentifier", pptpIdentifierList));
      } else if (!drugNameList.isEmpty()) {
        crit.add(Restrictions.in("cmpd.name", drugNameList));
      }

      if (!cellNameList.isEmpty() && !cellTypeList.isEmpty()) {
        crit.add(Restrictions.disjunction()
                .add(Restrictions.in("clt.pptpIdentifier", cellNameList))
                .add(Restrictions.in("ct.displayName", cellTypeList)));
      } else if (!cellNameList.isEmpty()) {
        crit.add(Restrictions.in("clt.pptpIdentifier", cellNameList));
      } else if (!cellTypeList.isEmpty()) {
        crit.add(Restrictions.in("ct.displayName", cellTypeList));
      }

      crit.addOrder(org.hibernate.criterion.Order.asc("cmpd.name"));
      crit.addOrder(org.hibernate.criterion.Order.asc("clt.name"));

      //crit.setMaxResults(200);

      List<CellLineGroup> cellLineGroupList = (List<CellLineGroup>) crit.list();

      rtnList = TransformEntityToVO.toCellLineGroupVOs(cellLineGroupList);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return rtnList;

  }
}