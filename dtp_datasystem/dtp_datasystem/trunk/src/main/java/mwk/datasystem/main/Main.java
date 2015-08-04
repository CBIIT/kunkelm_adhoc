/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
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

        // testCtabFromSmiles();
        testReflection();

    }

    public static void testCtabFromSmiles() {

        String smiles = "CCCCCC";
        String ctab = MoleculeWrangling.toCtabFromSmiles(smiles);
        System.out.println(ctab);

    }

    public static void testReflection() {

        String[] textAreaFieldNames = new String[]{
            "drugNameTextArea",
            "aliasTextArea",
            "casTextArea",
            "cmpdNamedSetTextArea",
            "nscTextArea",
            "projectCodeTextArea",
            "plateTextArea",
            "targetTextArea",
            "mtxtTextArea",
            "pseudoAtomsTextArea"
        };

        String[] listFieldNames = new String[]{
            "drugNames",
            "aliases",
            "cases",
            "cmpdNamedSets",
            "nscs",
            "projectCodes",
            "plates",
            "targets",
            "mtxtPieces",
            "pseudoAtomsPieces"
        };

        SearchCriteriaBean scb = new SearchCriteriaBean();
        scb.setNscTextArea("740 123127 401005 705701 743380");

        try {

            Class scbClass = scb.getClass();

            
            for (String textAreaFieldName : Arrays.asList(textAreaFieldNames)) {

                Field f = scbClass.getDeclaredField(textAreaFieldName);
                f.setAccessible(true);

                System.out.println();
                System.out.println("fieldName: " + f.getName());
                System.out.println("fieldType: " + f.getType().getName());

                String str = (String) f.get(scb);

                System.out.println(textAreaFieldName);
                System.out.println(str);
            }
            
            
            for (String textAreaFieldName : Arrays.asList(textAreaFieldNames)) {

                Field f = scbClass.getDeclaredField(textAreaFieldName);
                f.setAccessible(true);

                System.out.println();
                System.out.println("fieldName: " + f.getName());
                System.out.println("fieldType: " + f.getType().getName());

                String str = (String) f.get(scb);

                System.out.println(textAreaFieldName);
                System.out.println(str);
            }

            for (String listFieldName : Arrays.asList(listFieldNames)) {

                Field f = scbClass.getDeclaredField(listFieldName);
                f.setAccessible(true);

                System.out.println();
                System.out.println("fieldName: " + f.getName());
                System.out.println("fieldType: " + f.getType().getName());

                ArrayList<String> al = (ArrayList< String>) f.get(scb);
                System.out.println(listFieldName);
                for (String s : al) {
                    System.out.println(s);
                }
            }

            // production code should handle these exceptions more gracefully
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }

    }

}
