/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.ALOGPDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondAcceptorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondDonorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.TPSADescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.WeightDescriptor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 *
 * @author mwkunkel
 */
public class ParseAndFragment {

    static int BATCH_FETCH_SIZE = 1000;

    /**
     *
     * @param postgresConn
     * @param postgresInsertConn
     * @throws Exception
     */
    public static void processFragments(Connection postgresConn, Connection postgresInsertConn) throws Exception {

        // sql for set SHOULD BE being passed in from elsewhere, but this is MONOLITHIC
        //
        // a set of specific nsc, mix pseudo atoms, metals, 2-fragment, and problems
        // String sqlString = "select nsc, mf, strc from prod_src where nsc in (3311, 123127, 163027, 240824, 253968, 305308, 401005, 691910, 705701, 670072, 688088, 709767) order by nsc";
        //
        // everything, or with LIMIT 
        // String sqlString = "select nsc, mf, strc from prod_src";
        //
        //FAILURES in APPROVEDONCOLOGY
        // String sqlString = "select nsc, mf, strc from prod_src where nsc in (744009, 757043, 755402, 16895, 757296, 157387, 757105, 33832)";
        //
        // TESTOSTERONE
        String sqlString = "select nsc, mf, strc from prod_src where nsc in (9700,26499,50917,523833,755838,764937,2509410) order by nsc";

        Statement postgresStmt = null;
        PreparedStatement postgresPrepStmt = null;
        ResultSet rs = null;

        ArrayList<String> warningList = new ArrayList<String>();

        PreparedStatement molFragPrepStmt = null;
        PreparedStatement molNoFragPrepStmt = null;
        PreparedStatement molCanNotParsePrepStmt = null;
        PreparedStatement molFragRgroupPrepStmt = null;
        PreparedStatement pseudoAtomPrepStmt = null;
        PreparedStatement nullAtomTypePrepStmt = null;

        //String prefix = "";
        int nsc = 0;
        //int structureId = 0;
        String mf = "";
        String strc = "";

        try {

            postgresStmt = postgresConn.createStatement();
            postgresStmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            postgresStmt.setFetchSize(BATCH_FETCH_SIZE);
            String[] sqlArray = new String[]{
                "drop table if exists mol_cannot_parse",
                "create table mol_cannot_parse(nsc int, mf varchar(1024), message varchar(1024))",
                "drop table if exists mol_frag",
                "create table mol_frag(nsc int, frag_id int, ctab text, smiles text, inchi text, inchi_aux text, alogp double precision, hba int, hbd int, psa double precIsion, mf varchar(1024), mw double precision)",
                "drop table if exists mol_no_frag",
                "create table mol_no_frag(nsc int, strc_id int, mf varchar(1024))",
                "drop table if exists mol_frag_r_group",
                "create table mol_frag_r_group(nsc int, frag_id int, mf varchar(1024))",
                "drop table if exists pseudo_atom",
                "create table pseudo_atom(nsc int, frag_id int, mf varchar(1024), atom_label varchar(1024))",
                "drop table if exists null_atom_type",
                "create table null_atom_type(nsc int, frag_id int, mf varchar(1024), atom_label varchar(1024))"
            };

            for (int i = 0; i < sqlArray.length; i++) {
                String sqlStr = sqlArray[i];
                System.out.println(sqlStr);
                postgresStmt.execute(sqlStr);
                postgresConn.commit();
            }

            String molCanNotParseStmtString = "insert into  mol_cannot_parse(nsc, mf, message) values (?,?,?)";
            molCanNotParsePrepStmt = postgresInsertConn.prepareStatement(molCanNotParseStmtString);

            String molFragStmtString = "insert into mol_frag(nsc, frag_id, ctab, smiles, inchi, inchi_aux, alogp, hba, hbd, psa , mf , mw)  values(?,?,?,?,?,?,?,?,?,?,?,?)";
            molFragPrepStmt = postgresInsertConn.prepareStatement(molFragStmtString);

            String molNoFragStmtString = "insert into mol_no_frag(nsc, mf) values(?,?)";
            molNoFragPrepStmt = postgresInsertConn.prepareStatement(molNoFragStmtString);

            String molFragRgroupStmtString = "insert into mol_frag_r_group(nsc, frag_id, mf)  values(?,?,?)";
            molFragRgroupPrepStmt = postgresInsertConn.prepareStatement(molFragRgroupStmtString);

            String pseudoAtomStmtString = "insert into pseudo_atom(nsc, frag_id, mf, atom_label)  values(?,?,?,?)";
            pseudoAtomPrepStmt = postgresInsertConn.prepareStatement(pseudoAtomStmtString);

            String nullAtomTypeStmtString = "insert into null_atom_type(nsc, frag_id, mf, atom_label)  values(?,?,?,?)";
            nullAtomTypePrepStmt = postgresInsertConn.prepareStatement(nullAtomTypeStmtString);

            int molCnt = 0;
            int fragmentCnt = 0;

            InChIGeneratorFactory inchiFactory = InChIGeneratorFactory.getInstance();
            SmilesGenerator smilesGenerator = new SmilesGenerator();
            MDLV2000Writer mdlWriter = new MDLV2000Writer();
            MDLV2000Reader mdlReader = new MDLV2000Reader();

            rs = postgresStmt.executeQuery(sqlString);

            rs.setFetchDirection(ResultSet.FETCH_FORWARD);
            rs.setFetchSize(BATCH_FETCH_SIZE);
            ArrayList<IMolecule> fragmentList = new ArrayList<IMolecule>();

            while (rs.next()) {

                //prefix = rs.getString("prefix");
                nsc = rs.getInt("nsc");
                //structureId = rs.getInt("strc_id");
                strc = rs.getString("strc");
                mf = rs.getString("mf");

                System.out.println("molCnt: " + molCnt++ + " nsc: " + nsc);

                Molecule theMolecule = new Molecule();

                fragmentList.clear();

                try {
                    mdlReader.setReader(new ByteArrayInputStream(strc.getBytes()));
                    mdlReader.read(theMolecule);
                } catch (Exception e) {

                    theMolecule = null;

                    Writer w = new StringWriter();
                    PrintWriter pw = new PrintWriter(w);

                    e.printStackTrace(pw);
                    String message = w.toString();

                    molCanNotParsePrepStmt.setInt(1, nsc);
                    molCanNotParsePrepStmt.setString(2, mf);
                    molCanNotParsePrepStmt.setString(3, message);
                    molCanNotParsePrepStmt.execute();
                    postgresInsertConn.commit();

                }

                // if the molecule is NOT null
                if (theMolecule != null) {

                    //THIS FAILS FOR sdf with query bonds
                    if (ConnectivityChecker.isConnected(theMolecule)) {
                        fragmentList.add(theMolecule);
                    } else {
                        IMoleculeSet fragmentSet = ConnectivityChecker.partitionIntoMolecules(theMolecule);
                        for (int fragCnt = 0; fragCnt < fragmentSet.getMoleculeCount(); fragCnt++) {
                            fragmentList.add(fragmentSet.getMolecule(fragCnt));
                        }
                    }

//if fragmentList is empty
                    if (fragmentList.isEmpty()) {

                        molNoFragPrepStmt.setInt(1, nsc);
                        molNoFragPrepStmt.setString(2, mf);
                        molNoFragPrepStmt.execute();
                        postgresInsertConn.commit();

                    } else {

                        int hErrorCnt = 0;

                        for (IMolecule im : fragmentList) {

                            fragmentCnt++;

                            // check for pseudo and null atoms and R groups
                            String pseudoCheckReturnString = doCheckForPseudoAtoms(im);

                            if (pseudoCheckReturnString.length() > 0) {
                                String[] splitPseudo = pseudoCheckReturnString.split("-endLabel-");
                                for (int i = 0; i < splitPseudo.length; i++) {
                                    // track pseudoAtoms
                                    pseudoAtomPrepStmt.setInt(1, nsc);
                                    pseudoAtomPrepStmt.setInt(2, fragmentCnt);
                                    pseudoAtomPrepStmt.setString(3, mf);
                                    pseudoAtomPrepStmt.setString(4, splitPseudo[i]);
                                    pseudoAtomPrepStmt.execute();
                                    postgresInsertConn.commit();
                                }
                            }

                            String nullAtomTypeCheckReturnString = doCheckForNullTypeAtoms(im);

                            if (nullAtomTypeCheckReturnString.length() > 0) {
                                String[] splitPseudo = nullAtomTypeCheckReturnString.split("-endLabel-");
                                for (int i = 0; i < splitPseudo.length; i++) {
                                    // track pseudoAtoms
                                    nullAtomTypePrepStmt.setInt(1, nsc);
                                    nullAtomTypePrepStmt.setInt(2, fragmentCnt);
                                    nullAtomTypePrepStmt.setString(3, mf);
                                    nullAtomTypePrepStmt.setString(4, splitPseudo[i]);
                                    nullAtomTypePrepStmt.execute();
                                    postgresInsertConn.commit();
                                }
                            }

                            boolean hasRgroups = doCheckForRgroups(im);
                            if (hasRgroups) {
                                molFragRgroupPrepStmt.setInt(1, nsc);
                                molFragRgroupPrepStmt.setInt(2, fragmentCnt);
                                molFragRgroupPrepStmt.setString(3, mf);
                                molFragRgroupPrepStmt.execute();
                                postgresInsertConn.commit();
                            }

                            // remove the pseudo atoms, process any legitimate stuff left over
                            if (pseudoCheckReturnString.length() > 0) {
                                doStripPseudoAtoms(im);
                            }

                            if (nullAtomTypeCheckReturnString.length() > 0 || hasRgroups) {
                            } else {

                                try {
                                    CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(im.getBuilder());
                                    Iterator<IAtom> atoms = im.atoms().iterator();
                                    while (atoms.hasNext()) {
                                        IAtom atom = atoms.next();
                                        IAtomType type = matcher.findMatchingAtomType(im, atom);
                                        AtomTypeManipulator.configure(atom, type);
                                    }
                                    CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(im.getBuilder());
                                    hAdder.addImplicitHydrogens(im);
                                    AtomContainerManipulator.convertImplicitToExplicitHydrogens(im);
                                } catch (Exception e) {
                                    hErrorCnt++;
                                    warningList.add(e.toString());
                                }

//pass this off to doCalcs()
                                CalcContainer calcRtn = doCalcs(im);
                                StructureContainer strucRtn = doStructureStrings(im, inchiFactory, smilesGenerator, mdlWriter);
                                molFragPrepStmt.setInt(1, nsc);
                                molFragPrepStmt.setInt(2, fragmentCnt);
                                molFragPrepStmt.setString(3, strucRtn.ctab);
                                molFragPrepStmt.setString(4, strucRtn.smiles);
                                molFragPrepStmt.setString(5, strucRtn.inchi);
                                molFragPrepStmt.setString(6, strucRtn.inchiAuxInfo);
                                molFragPrepStmt.setDouble(7, calcRtn.alogp);
                                molFragPrepStmt.setInt(8, calcRtn.hba);
                                molFragPrepStmt.setInt(9, calcRtn.hbd);
                                molFragPrepStmt.setDouble(10, calcRtn.psa);
                                molFragPrepStmt.setString(11, calcRtn.mf);
                                molFragPrepStmt.setDouble(12, calcRtn.mw);
                                molFragPrepStmt.execute();
                                postgresInsertConn.commit();

                            } // if no null atoms 
                        } // for each fragment 
                    } // if fragList is not empty
                } // if theMolecule is not null
            } // while (rs.next())

        } catch (Exception e) {

            System.out.println("Caught outer exception while processing nsc: " + nsc + " " + e);
            e.printStackTrace();

        } finally {

            postgresConn.commit();
            postgresInsertConn.commit();

            if (postgresStmt != null) {
                try {
                    postgresStmt.close();
                    postgresStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing postgresStmt");
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing rs");
                }
            }

        }

    }

    /**
     *
     * @param im
     * @return
     * @throws Exception
     */
    private static CalcContainer doCalcs(IMolecule im) {

        CalcContainer rtn = new CalcContainer();
        DescriptorValue result = null;
        String paramName = null;
        String parsedResult = null;

        try {

            ALOGPDescriptor the_ALOGPDescriptor = new ALOGPDescriptor();
            result = the_ALOGPDescriptor.calculate(im);
            paramName = "ALogP";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.alogp = Double.parseDouble(parsedResult);

            HBondAcceptorCountDescriptor the_HBondAcceptorCountDescriptor = new HBondAcceptorCountDescriptor();
            result = the_HBondAcceptorCountDescriptor.calculate(im);
            paramName = "nHBAcc";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.hba = Integer.parseInt(parsedResult);

            HBondDonorCountDescriptor the_HBondDonorCountDescriptor = new HBondDonorCountDescriptor();
            result = the_HBondDonorCountDescriptor.calculate(im);
            paramName = "nHBDon";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.hbd = Integer.parseInt(parsedResult);

            TPSADescriptor the_TPSADescriptor = new TPSADescriptor();
            result = the_TPSADescriptor.calculate(im);
            paramName = "TopoPSA";
            parsedResult = parseDescriptorValue(result, paramName);
//System.out.println(paramName + ": " + parsedResult);
            rtn.psa = Double.parseDouble(parsedResult);

            int calcAtomCount = im.getAtomCount();
//build up the molecular formula
            IMolecularFormula formula = im.getBuilder().newInstance(
                    IMolecularFormula.class);
            MolecularFormulaManipulator.getMolecularFormula(im, formula);
            rtn.mf = MolecularFormulaManipulator.getString(formula).trim();

            WeightDescriptor the_WeightDescriptor = new WeightDescriptor();
            result = the_WeightDescriptor.calculate(im);
            paramName = "MW";
            parsedResult = parseDescriptorValue(result, paramName);
            rtn.mw += Double.valueOf(parsedResult);

//System.out.println("Value of alogp is:" + rtn.alogp);
//System.out.println("Value of hba is:" + rtn.hba);
//System.out.println("Value of hbd is:" + rtn.hbd);
//System.out.println("Value of psa is:" + rtn.psa);
//System.out.println("Value of mf is:" + rtn.mf);
//System.out.println("Value of mw is:" + rtn.mw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }

    private static boolean doCheckForRgroups(IMolecule im) {

        boolean rtn = false;

        for (int atomCnt = 0; atomCnt < im.getAtomCount(); atomCnt++) {
            Atom thisAtom = (Atom) im.getAtom(atomCnt);
            if (thisAtom.getSymbol().equals("R")) {
                rtn = true;
            }
        }

        return rtn;
    }

    /**
     *
     * @param im
     * @return
     */
    private static String doCheckForPseudoAtoms(IMolecule im) {

    //System.out.println("In doCheckForPseudoAtoms");
        //System.out.println("Count of atoms is: " + im.getAtomCount());
        StringBuffer sb = new StringBuffer();

        for (int atomCnt = 0; atomCnt < im.getAtomCount(); atomCnt++) {

            Atom thisAtom = (Atom) im.getAtom(atomCnt);

            if (thisAtom instanceof PseudoAtom) {
                PseudoAtom pa = (PseudoAtom) thisAtom;
                sb.append(pa.getLabel());
                sb.append("-endLabel-");
            }

        }
        //System.out.println("Returning: " + sb.toString());
        return sb.toString();
    }

    protected static void doStripPseudoAtoms(IMolecule theMolecule) {

        try {

            int initialAtomCount = theMolecule.getAtomCount();

            for (int atomCnt = 0; atomCnt < theMolecule.getAtomCount(); atomCnt++) {
                Atom thisAtom = (Atom) theMolecule.getAtom(atomCnt);
                if (thisAtom instanceof PseudoAtom) {
                    theMolecule.removeAtom(thisAtom);
                }
            }

            int finalAtomCount = theMolecule.getAtomCount();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static String doCheckForNullTypeAtoms(IMolecule im) {

        StringBuffer sb = new StringBuffer();

        try {

            CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(im.getBuilder());

            Iterator<IAtom> atoms = im.atoms().iterator();

            while (atoms.hasNext()) {

                IAtom atom = atoms.next();

                IAtomType type = matcher.findMatchingAtomType(im, atom);
// catch nulls
                if (type == null) {
                    sb.append(atom.getSymbol());
                    sb.append("-endLabel-");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Returning: " + sb.toString());
        return sb.toString();

    }

    /**
     *
     * @param dv
     * @param paramName
     * @return
     */
    private static String parseDescriptorValue(DescriptorValue dv, String paramName) {
        int paramIndex = -1;
        String[] names = dv.getNames();
        for (int i = 0; i
                < names.length; i++) {
//System.out.print(names[i] + ", ");
            if (names[i].equals(paramName)) {
                paramIndex = i;
            }
        }
        String resultString = dv.getValue().toString();
        String[] splitString = resultString.split(",");
        if (paramIndex > -1) {
            return splitString[paramIndex];
        } else {
            System.out.println("Could not extract: " + paramName + " in parseDescriptorValue in CompoundRegistrationControllerImpl");
            return "notFound";

        }
    }

    /**
     *
     * @param im
     * @param inchiFactory
     * @param smilesGenerator
     * @param mdlWriter
     * @return
     * @throws Exception
     */
    public static StructureContainer doStructureStrings(
            IMolecule im,
            InChIGeneratorFactory inchiFactory,
            SmilesGenerator smilesGenerator,
            MDLV2000Writer mdlWriter) throws Exception {

        StructureContainer rtn = new StructureContainer();

        rtn.smiles = smilesGenerator.createSMILES(im);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mdlWriter.setWriter(baos);
            mdlWriter.writeMolecule(im);
            rtn.ctab = baos.toString();
        } catch (Exception e) {
//intended to silence any exception
//caused by R groups and/or PseudoAtoms
        }
        try {
            InChIGenerator gen = inchiFactory.getInChIGenerator(im);
            INCHI_RET ret = gen.getReturnStatus();
            if (ret == INCHI_RET.WARNING) {
//System.out.println("InChI warning: " + gen.getMessage());
                rtn.inchi = gen.getInchi();
                rtn.inchiAuxInfo = gen.getAuxInfo() + " InChI warning: " + gen.getMessage();
            } else if (ret != INCHI_RET.OKAY) {
                rtn.inchi = "InChiFailed ";
                rtn.inchiAuxInfo = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
            } else {
                rtn.inchi = gen.getInchi();
                rtn.inchiAuxInfo = gen.getAuxInfo();
            }

        } catch (Exception e) {
//intended to silence any Inchi-related exceptions
//caused by R groups and/or PseudoAtoms
        }
//System.out.println("Value of ctab is:" + rtn.ctab);
//System.out.println("Value of smiles is:" + rtn.smiles);
//System.out.println("Value of inchi is:" + rtn.inchi);
//System.out.println("Value of inchiAuxInfo is:" + rtn.inchiAuxInfo);
        return rtn;
    }
}
