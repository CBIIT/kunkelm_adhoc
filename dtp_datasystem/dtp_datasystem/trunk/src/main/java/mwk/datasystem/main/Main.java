/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.controllers.SearchCriteriaBean;
import mwk.datasystem.controllers.SessionController;
import mwk.datasystem.controllers.StructureSearchController;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdImpl;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpdImpl;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.util.TransformXMLGregorianCalendar;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import newstructureservlet.MoleculeWrangling;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static void main(String[] args) {

        testCtabFromSmiles();

    }

    public static void testCtabFromSmiles() {

        String smiles = "CCCCCC";
        String ctab = MoleculeWrangling.toCtabFromSmiles(smiles);
        System.out.println(ctab);

    }

}
