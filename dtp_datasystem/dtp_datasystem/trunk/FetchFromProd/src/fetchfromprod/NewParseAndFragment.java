/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fetchfromprod;

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
import java.util.Collections;
import java.util.Iterator;
import net.sf.jniinchi.INCHI_OPTION;
import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
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
public class NewParseAndFragment {

    int BATCH_FETCH_SIZE = 1000;
    InChIGeneratorFactory inchiFactory = null;
    SmilesGenerator smilesGenerator = null;
    MDLV2000Writer mdlWriter = null;
    MDLV2000Reader mdlReader = null;

    public NewParseAndFragment() {

        try {

            inchiFactory = InChIGeneratorFactory.getInstance();
            smilesGenerator = new SmilesGenerator();
            mdlWriter = new MDLV2000Writer();
            mdlReader = new MDLV2000Reader();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private AtomContainer parseMolecule(String ctab) {

        AtomContainer rtn = new AtomContainer();

        try {
            mdlReader.setReader(new ByteArrayInputStream(ctab.getBytes()));
            mdlReader.read(rtn);
        } catch (Exception e) {

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            String message = w.toString();
            rtn.setProperty("message", message);

        }

        return rtn;

    }

    private ArrayList<IAtomContainer> fragmentMolecule(AtomContainer theMolecule) {

        ArrayList<IAtomContainer> fragmentList = new ArrayList<IAtomContainer>();

        // if the molecule is NOT null
        if (theMolecule != null) {

            if (ConnectivityChecker.isConnected(theMolecule)) {
                fragmentList.add(theMolecule);
            } else {
                IAtomContainerSet fragmentSet = ConnectivityChecker.partitionIntoMolecules(theMolecule);
                for (int fragCnt = 0; fragCnt < fragmentSet.getAtomContainerCount(); fragCnt++) {
                    fragmentList.add(fragmentSet.getAtomContainer(fragCnt));
                }
            }

        }

        return fragmentList;

    }

    public void processFragments(Connection postgresConn, Connection postgresInsertConn) {

         String sqlString = "select nsc, mf, ctab as strc from prod_raw_cmpd where nsc > 169370 order by nsc";

        Statement postgresStmt = null;
        PreparedStatement postgresPrepStmt = null;
        ResultSet rs = null;

        ArrayList<String> warningList = new ArrayList<String>();

        PreparedStatement molCanNotParsePrepStmt = null;
        PreparedStatement molFragPrepStmt = null;
        PreparedStatement molNoFragPrepStmt = null;
        PreparedStatement molFragRgroupPrepStmt = null;
        PreparedStatement pseudoAtomPrepStmt = null;
        PreparedStatement nullAtomTypePrepStmt = null;

        int nsc = 0;
        String mf = "";
        String strc = "";

        try {

            postgresStmt = postgresConn.createStatement();
            postgresStmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            postgresStmt.setFetchSize(BATCH_FETCH_SIZE);
            String[] sqlArray = new String[]{
                "drop table if exists mol_cannot_parse",
                "create table mol_cannot_parse(nsc int, mf varchar, message varchar)",
                "drop table if exists mol_frag",
                "create table mol_frag(nsc int, frag_id int, count_atoms int, count_pseudo_atoms int, count_bonds int, ctab text, smiles text, inchi text, inchi_aux text, alogp double precision, hba int, hbd int, psa double precIsion, mf varchar, mw double precision, processing_messages varchar)",
                "drop table if exists mol_no_frag",
                "create table mol_no_frag(nsc int, strc_id int, mf varchar)",
                "drop table if exists mol_frag_r_group",
                "create table mol_frag_r_group(nsc int, frag_id int, mf varchar)",
                "drop table if exists mol_frag_pseudo_atom",
                "create table mol_frag_pseudo_atom(nsc int, frag_id int, mf varchar, atom_label varchar)",
                "drop table if exists mol_frag_null_atom_type",
                "create table mol_frag_null_atom_type(nsc int, frag_id int, mf varchar, atom_label varchar)"
            };

//            for (int i = 0; i < sqlArray.length; i++) {
//                String sqlStr = sqlArray[i];
//                System.out.println(sqlStr);
//                postgresStmt.execute(sqlStr);
//                postgresConn.commit();
//            }

            String molCanNotParseStmtString = "insert into  mol_cannot_parse(nsc, mf, message) values (?,?,?)";
            molCanNotParsePrepStmt = postgresInsertConn.prepareStatement(molCanNotParseStmtString);

            String molFragStmtString = "insert into mol_frag(nsc, frag_id, count_atoms, count_pseudo_atoms, count_bonds, ctab, smiles, inchi, inchi_aux, alogp, hba, hbd, psa , mf , mw, processing_messages)  values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            molFragPrepStmt = postgresInsertConn.prepareStatement(molFragStmtString);

            String molNoFragStmtString = "insert into mol_no_frag(nsc, mf) values(?,?)";
            molNoFragPrepStmt = postgresInsertConn.prepareStatement(molNoFragStmtString);

            String molFragRgroupStmtString = "insert into mol_frag_r_group(nsc, frag_id, mf)  values(?,?,?)";
            molFragRgroupPrepStmt = postgresInsertConn.prepareStatement(molFragRgroupStmtString);

            String pseudoAtomStmtString = "insert into mol_frag_pseudo_atom(nsc, frag_id, mf, atom_label)  values(?,?,?,?)";
            pseudoAtomPrepStmt = postgresInsertConn.prepareStatement(pseudoAtomStmtString);

            String nullAtomTypeStmtString = "insert into mol_frag_null_atom_type(nsc, frag_id, mf, atom_label)  values(?,?,?,?)";
            nullAtomTypePrepStmt = postgresInsertConn.prepareStatement(nullAtomTypeStmtString);

            int molCnt = 0;
            int fragmentId = 0;

            rs = postgresStmt.executeQuery(sqlString);

            rs.setFetchDirection(ResultSet.FETCH_FORWARD);
            rs.setFetchSize(BATCH_FETCH_SIZE);

            while (rs.next()) {

                nsc = rs.getInt("nsc");
                strc = rs.getString("strc");
                mf = rs.getString("mf");

                molCnt++;

                System.out.println("molCnt: " + molCnt + " nsc: " + nsc + " mf: " + mf);

                AtomContainer theMolecule = parseMolecule(strc);

                ArrayList<IAtomContainer> fragmentList = new ArrayList<IAtomContainer>();

                // failed molecules return with the "message" property set
                if (theMolecule.getProperty("message") != null) {

                    // HAVE to increment the fragmentId HERE!
                    fragmentId++;

                    String msg = theMolecule.getProperty("message").toString();

                    molCanNotParsePrepStmt.setInt(1, nsc);
                    molCanNotParsePrepStmt.setString(2, mf);
                    molCanNotParsePrepStmt.setString(3, msg);

                    molCanNotParsePrepStmt.execute();
                    postgresInsertConn.commit();

                    // write a place-holder to mol_frag to simplify tracking nsc later...
                    String errString = "ERR cannot parse strc " + msg;

                    molFragPrepStmt.setInt(1, nsc);
                    molFragPrepStmt.setInt(2, fragmentId);
                    // count_atoms
                    molFragPrepStmt.setInt(3, 0);
                    // count_pseudo_atoms
                    molFragPrepStmt.setInt(4, 0);
                    // count_bonds
                    molFragPrepStmt.setInt(5, 0);
                    //ctab
                    molFragPrepStmt.setString(6, strc);
                    // smiles
                    molFragPrepStmt.setString(7, null);
                    // inchi
                    molFragPrepStmt.setString(8, null);
                    molFragPrepStmt.setString(9, null);
                    molFragPrepStmt.setDouble(10, 0);
                    molFragPrepStmt.setInt(11, 0);
                    molFragPrepStmt.setInt(12, 0);
                    molFragPrepStmt.setDouble(13, 0);
                    // mf
                    molFragPrepStmt.setString(14, null);
                    molFragPrepStmt.setDouble(15, 0);
                    // processing_messages
                    molFragPrepStmt.setString(16, errString);
                    molFragPrepStmt.execute();
                    postgresInsertConn.commit();

                } else {

                    fragmentList = fragmentMolecule(theMolecule);

                    if (fragmentList.isEmpty()) {

                        molNoFragPrepStmt.setInt(1, nsc);
                        molNoFragPrepStmt.setString(2, mf);
                        
                        molNoFragPrepStmt.execute();
                        postgresInsertConn.commit();

                    } else {

                        int hErrorCnt = 0;

                        for (IAtomContainer im : fragmentList) {

                            fragmentId++;

                            // use MoleculeStats
                            MoleculeStats molStats = new MoleculeStats(im, nsc);

                            // check (and remove) pseudo atoms, check for null atom types, check for R groups
                           
                            // pseudo atoms
                            if (molStats.countPseudoAtoms > 0) {
                                for (String atomSymbol : molStats.pseudoAtomsList) {
                                    // track pseudoAtoms
                                    pseudoAtomPrepStmt.setInt(1, nsc);
                                    pseudoAtomPrepStmt.setInt(2, fragmentId);
                                    pseudoAtomPrepStmt.setString(3, mf);
                                    pseudoAtomPrepStmt.setString(4, atomSymbol);
                                    
                                    pseudoAtomPrepStmt.execute();
                                    postgresInsertConn.commit();
                                }
                            }

                            if (molStats.countNullAtoms > 0) {
                                for (String atomSymbol : molStats.nullAtomsList) {
                                    // track null atoms
                                    nullAtomTypePrepStmt.setInt(1, nsc);
                                    nullAtomTypePrepStmt.setInt(2, fragmentId);
                                    nullAtomTypePrepStmt.setString(3, mf);
                                    nullAtomTypePrepStmt.setString(4, atomSymbol);
                                    
                                    nullAtomTypePrepStmt.execute();
                                    postgresInsertConn.commit();
                                }
                            }

                            if (molStats.countRgroups > 0) {
                                molFragRgroupPrepStmt.setInt(1, nsc);
                                molFragRgroupPrepStmt.setInt(2, fragmentId);
                                molFragRgroupPrepStmt.setString(3, mf);
                                
                                molFragRgroupPrepStmt.execute();
                                postgresInsertConn.commit();
                            }

                            // if there were any pseudoatoms, SWAP for the molecule with those removed
                            if (molStats.countPseudoAtoms > 0) {
                                im = molStats.moleculeWithoutPseudoAtoms;
                            }

                            // EVERY FRAG should have an entry in mol_frag
                            if (molStats.countNullAtoms > 0 || molStats.countRgroups > 0 || molStats.countAtoms == 0) {

                                StringBuilder sb = new StringBuilder();

                                if (molStats.countPseudoAtoms > 0) {
                                    sb.append("ERR pseudo-atom(s) -->" + makeStringFromList(molStats.pseudoAtomsList) + "<-- ");
                                }

                                if (molStats.countNullAtoms > 0) {
                                    sb.append("ERR null atom type(s) -->" + makeStringFromList(molStats.nullAtomsList) + "<-- ");
                                }

                                if (molStats.countRgroups > 0) {
                                    sb.append("ERR r group(s) -->" + makeStringFromList(molStats.rGroupsList) + "<-- ");
                                }

                                // THIS has to reference the NEW atom since the stats were calculated BEFORE removing pseudoAtoms
                                if (im.getAtomCount() == 0) {
                                    sb.append("ERR no real atoms ");
                                }

                                molFragPrepStmt.setInt(1, nsc);
                                molFragPrepStmt.setInt(2, fragmentId);
                                molFragPrepStmt.setInt(3, molStats.countAtoms);
                                molFragPrepStmt.setInt(4, molStats.countPseudoAtoms);
                                molFragPrepStmt.setInt(5, molStats.countBonds);
                                molFragPrepStmt.setString(6, null);
                                molFragPrepStmt.setString(7, null);
                                molFragPrepStmt.setString(8, null);
                                molFragPrepStmt.setString(9, null);
                                molFragPrepStmt.setDouble(10, 0);
                                molFragPrepStmt.setInt(11, 0);
                                molFragPrepStmt.setInt(12, 0);
                                molFragPrepStmt.setDouble(13, 0);
                                molFragPrepStmt.setString(14, null);
                                molFragPrepStmt.setDouble(15, 0);
                                molFragPrepStmt.setString(16, sb.toString());

                                molFragPrepStmt.execute();
                                postgresInsertConn.commit();

                            } else {

//                try {
//                  FixBondOrdersTool fbt = new FixBondOrdersTool();
//                  IAtomContainer temp = fbt.kekuliseAromaticRings(im);
//                  im = temp;
//                } catch (Exception e) {
//                  e.printStackTrace();
//                  warningList.add(e.toString());
//                }
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

                                    // MWK 11Jan2014, tried keeping H as implicit so that they don't write to the ctab
                                    // but this caused problems with smiles
                                    AtomContainerManipulator.convertImplicitToExplicitHydrogens(im);

                                } catch (Exception e) {
                                    hErrorCnt++;
                                    warningList.add(e.toString());
                                }

                                //pass this off to doCalcs()
                                CalcContainer calcRtn = doCalcs(im);
                                StructureContainer strucRtn = doStructureStrings(im);

                                //                               
                                molFragPrepStmt.setInt(1, nsc);
                                molFragPrepStmt.setInt(2, fragmentId);
                                molFragPrepStmt.setInt(3, molStats.countAtoms);
                                molFragPrepStmt.setInt(4, molStats.countPseudoAtoms);
                                molFragPrepStmt.setInt(5, molStats.countBonds);
                                molFragPrepStmt.setString(6, strucRtn.ctab);
                                molFragPrepStmt.setString(7, strucRtn.smiles);
                                molFragPrepStmt.setString(8, strucRtn.inchi);
                                molFragPrepStmt.setString(9, strucRtn.inchiAuxInfo);
                                molFragPrepStmt.setDouble(10, calcRtn.alogp);
                                molFragPrepStmt.setInt(11, calcRtn.hba);
                                molFragPrepStmt.setInt(12, calcRtn.hbd);
                                molFragPrepStmt.setDouble(13, calcRtn.psa);
                                molFragPrepStmt.setString(14, calcRtn.mf);
                                molFragPrepStmt.setDouble(15, calcRtn.mw);
                                molFragPrepStmt.setString(16, null);
                                
                                molFragPrepStmt.execute();
                                postgresInsertConn.commit();

                            } // if pseudo atoms, null atoms or r groups

                        } // for each fragment 

                    } // if fragList is not empty

                } // if theMolecule is not null

            } // while (rs.next())

        } catch (Exception e) {

            System.out.println("Caught outer exception while processing nsc: " + nsc + " " + e);
            e.printStackTrace();

        } finally {

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
    private CalcContainer doCalcs(IAtomContainer im) {

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
            IMolecularFormula formula = im.getBuilder().newInstance(IMolecularFormula.class);
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

    /**
     *
     * @param dv
     * @param paramName
     * @return
     */
    private String parseDescriptorValue(DescriptorValue dv, String paramName) {
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
     * @return @throws Exception
     */
    private StructureContainer doStructureStrings(IAtomContainer im) {

        StructureContainer rtn = new StructureContainer();

        try {

            rtn.smiles = smilesGenerator.createSMILES(im);

        } catch (Exception e) {

            e.printStackTrace();
            im.toString();

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            rtn.smiles = w.toString();

        }

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mdlWriter.setWriter(baos);
            mdlWriter.writeMolecule(im);
            rtn.ctab = baos.toString();

        } catch (Exception e) {

            e.printStackTrace();

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            rtn.ctab = w.toString();

        }

        try {

            ArrayList<INCHI_OPTION> optionList = new ArrayList<INCHI_OPTION>();
            //optionList.add(INCHI_OPTION.DoNotAddH);
            //optionList.add(INCHI_OPTION.FB);

            InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(im, optionList);

            INCHI_RET ret = gen.getReturnStatus();

            if (ret == INCHI_RET.WARNING) {
                rtn.inchi = gen.getInchi();
                rtn.inchiAuxInfo = gen.getAuxInfo() + " InChI warning: " + gen.getMessage();
            } else if (ret != INCHI_RET.OKAY) {
                rtn.inchi = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
                rtn.inchiAuxInfo = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
            } else {
                rtn.inchi = gen.getInchi();
                rtn.inchiAuxInfo = gen.getAuxInfo();
            }

        } catch (Exception e) {
            e.printStackTrace();

            Writer w = new StringWriter();
            PrintWriter pw = new PrintWriter(w);
            e.printStackTrace(pw);
            rtn.inchi = "InChI failed with exception: " + w.toString();
            rtn.inchiAuxInfo = "InChI failed with exception: " + w.toString();

        }

        // System.out.println("Value of ctab is:" + rtn.ctab);
        // System.out.println("Value of smiles is:" + rtn.smiles);
        // System.out.println("Value of inchi is:" + rtn.inchi);
        // System.out.println("Value of inchiAuxInfo is:" + rtn.inchiAuxInfo);
        return rtn;
    }

    public String makeStringFromList(ArrayList<String> incomingList) {

        Collections.sort(incomingList);

        StringBuilder sb = new StringBuilder();
        for (String s : incomingList) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();

    }

    private class MoleculeStats {

        public IAtomContainer moleculeWithoutPseudoAtoms;
        public int countBonds;
        public int countAtoms;
        public int countPseudoAtoms;
        public int countNullAtoms;
        public int countRgroups;
        public ArrayList<String> atomsList;
        public ArrayList<String> pseudoAtomsList;
        public ArrayList<String> nullAtomsList;
        public ArrayList<String> rGroupsList;

        public MoleculeStats(IAtomContainer incomingMolecule, int nsc) {

            this.countAtoms = incomingMolecule.getAtomCount();
            this.countBonds = incomingMolecule.getBondCount();
            this.countPseudoAtoms = 0;
            this.countNullAtoms = 0;
            this.countRgroups = 0;
            this.atomsList = new ArrayList<String>();
            this.pseudoAtomsList = new ArrayList<String>();
            this.nullAtomsList = new ArrayList<String>();
            this.rGroupsList = new ArrayList<String>();

            try {

                if (incomingMolecule == null) {
                } else {

                    this.moleculeWithoutPseudoAtoms = incomingMolecule;

                    CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(incomingMolecule.getBuilder());

                    Iterator<IAtom> atoms = incomingMolecule.atoms().iterator();

                    while (atoms.hasNext()) {

                        IAtom atom = atoms.next();

                        IAtomType type = null;
                        
                        try {
                            type = matcher.findMatchingAtomType(incomingMolecule, atom);
                        } catch (Exception e) {
                            System.out.println("Processing: " + nsc + " caught Exception in match.findMatchingAtomType: " + atom.getSymbol());
                            e.printStackTrace();
                        }

                        if (type == null) {                            
                            this.countNullAtoms++;
                            this.nullAtomsList.add(atom.toString());
                        }

                        if (atom instanceof PseudoAtom) {
                            this.countPseudoAtoms++;
                            PseudoAtom pa = (PseudoAtom) atom;
                            this.pseudoAtomsList.add(pa.getLabel());
                            this.moleculeWithoutPseudoAtoms.removeAtom(atom);
                        }

                        if (atom.getSymbol().equals("R")) {
                            this.countRgroups++;
                            this.rGroupsList.add(atom.getSymbol());
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("nsc is: " + nsc);
                e.printStackTrace();
            }

        }
    }
}
