/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.util.ArrayList;
import java.util.Collection;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.util.HelperCmpdList;
import static mwk.datasystem.util.HelperCmpdList.DEBUG;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.Unproxy;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static void main(String[] args) {

    HelperCmpdList.deleteCmpdListByCmpdListId(2013862676340669978l, "kunkelm");

//    CmpdListVO clVO = new CmpdListVO();
//    clVO.setCmpdListId(7139189152107868606l);
//
//    ArrayList<CmpdListMemberVO> clmVOlist = new ArrayList<CmpdListMemberVO>();
//    CmpdListMemberVO clmVO = new CmpdListMemberVO();
//    clmVO.setId(120024l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120025l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120026l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120027l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120028l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120029l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120030l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120031l);
//    clmVOlist.add(clmVO);
//    clmVO = new CmpdListMemberVO();
//    clmVO.setId(120032l);
//    clmVOlist.add(clmVO);

  }
 
}
