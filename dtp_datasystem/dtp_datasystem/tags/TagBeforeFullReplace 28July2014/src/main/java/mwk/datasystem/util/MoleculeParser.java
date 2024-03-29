/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.Atom;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.io.iterator.IteratingSMILESReader;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.ALOGPDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondAcceptorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondDonorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.TPSADescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.WeightDescriptor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.SaturationChecker;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 *
 * @author mwkunkel
 */
public class MoleculeParser {

    // InChIGeneratorFactory INCHI_FACTORY;
    SmilesGenerator SMILES_GENERATOR;
    MDLV2000Writer MDL_WRITER;
    SaturationChecker SATURATION_CHECKER;

    public MoleculeParser() {

        try {

            // INCHI_FACTORY = InChIGeneratorFactory.getInstance();
            SMILES_GENERATOR = new SmilesGenerator();
            MDL_WRITER = new MDLV2000Writer();
            SATURATION_CHECKER = new SaturationChecker();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<AdHocCmpd> parseSMILESFile(File smilesFile) {

        ArrayList<AdHocCmpd> cmpdList = new ArrayList<AdHocCmpd>();

        try {

            FileInputStream fis = new FileInputStream(smilesFile);

            IteratingSMILESReader reader = new IteratingSMILESReader(fis, DefaultChemObjectBuilder.getInstance());

            reader.setReaderMode(IChemObjectReader.Mode.STRICT);

            int countMol = 0;

            while (reader.hasNext()) {

                IMolecule mol = (IMolecule) reader.next();
                countMol++;

                AdHocCmpd ahc = this.moleculeToAdHocCmpd(mol, smilesFile.getName(), countMol);

                cmpdList.add(ahc);

            }

            System.out.println("Processed: " + countMol + " molecules");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cmpdList;
    }

    public ArrayList<AdHocCmpd> parseSDF(File sdFile) {

        ArrayList<AdHocCmpd> cmpdList = new ArrayList<AdHocCmpd>();

        try {

            FileInputStream fis = new FileInputStream(sdFile);

            IteratingMDLReader reader = new IteratingMDLReader(fis, DefaultChemObjectBuilder.getInstance());

            reader.setReaderMode(IChemObjectReader.Mode.STRICT);
            reader.setSkip(true);

            Integer countMol = Integer.valueOf(0);

            while (reader.hasNext()) {

                IMolecule mol = (IMolecule) reader.next();
                countMol++;

                AdHocCmpd ahc = this.moleculeToAdHocCmpd(mol, sdFile.getName(), countMol);

                cmpdList.add(ahc);

            }

            System.out.println("Processed: " + countMol.toString() + " molecules");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cmpdList;
    }

    private AdHocCmpd moleculeToAdHocCmpd(IMolecule mol, String fileName, int countMol) {

        AdHocCmpd ahc = AdHocCmpd.Factory.newInstance();

        try {

            ArrayList<IMolecule> cdkFragList = new ArrayList<IMolecule>();

            if (ConnectivityChecker.isConnected(mol)) {
                if (doCheckForPseudoAtoms(mol)) {
                } else {
                    cdkFragList.add(mol);
                }
            } else {
                IMoleculeSet setOfFragments = ConnectivityChecker.partitionIntoMolecules(mol);
                for (int fragCnt = 0; fragCnt < setOfFragments.getMoleculeCount(); fragCnt++) {
                    if (doCheckForPseudoAtoms(setOfFragments.getMolecule(fragCnt))) {
                    } else {
                        cdkFragList.add(setOfFragments.getMolecule(fragCnt));
                    }
                }
            }

            // set name if found, not zero-length and not placeholder (".")

//            System.out.println("In moleculeToAdHocCmpd");
//            System.out.println(mol.toString());

            Object nameObj = mol.getProperty("Name");
            Object nameDbObj = mol.getProperty("SMIdbNAME");
            Object cmpdIdObj = mol.getProperty("Compound ID");

            System.out.println("CDKConstants.TITLE: " + CDKConstants.TITLE);

            Object titleObj = mol.getProperty(CDKConstants.TITLE);

            String nameStr = null;

            if (nameObj != null) {
                nameStr = nameObj.toString();
            } else if (nameDbObj != null) {
                nameStr = nameDbObj.toString();
            } else if (cmpdIdObj != null) {
                nameStr = cmpdIdObj.toString();
            } else if (titleObj != null) {
                nameStr = titleObj.toString();
            }

            if (nameStr == null || nameStr.length() == 0 || nameStr.equals(".")) {
                nameStr = fileName + countMol;
            }

            System.out.println("Setting adHocCmpd name to: " + nameStr);
            ahc.setName(nameStr);

            Set<AdHocCmpdFragment> fragSet = new HashSet<AdHocCmpdFragment>();

            for (IMolecule iMol : cdkFragList) {

                //System.out.println("At start: total charge: " + AtomContainerManipulator.getTotalCharge(iMol) + " formalCharge: " + AtomContainerManipulator.getTotalFormalCharge(iMol));
                //System.out.println("At start: " + sg.createSMILES(iMol));

                // CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(iMol.getBuilder());
                // hAdder.addImplicitHydrogens(iMol);

                //System.out.println("At start: total charge: " + AtomContainerManipulator.getTotalCharge(iMol) + " formalCharge: " + AtomContainerManipulator.getTotalFormalCharge(iMol));
                //System.out.println("At start: " + sg.createSMILES(iMol));

                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(iMol);
                AtomContainerManipulator.convertImplicitToExplicitHydrogens(iMol);

                SATURATION_CHECKER.saturate(iMol);

//                System.out.println("At end: total charge: " + AtomContainerManipulator.getTotalCharge(iMol) + " formalCharge: " + AtomContainerManipulator.getTotalFormalCharge(iMol));
//                System.out.println("At end: " + sg.createSMILES(iMol));
//                System.out.println();
//
//                CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(iMol.getBuilder());
//                Iterator<IAtom> atoms = iMol.atoms().iterator();
//                while (atoms.hasNext()) {
//                    IAtom atom = atoms.next();
//                    IAtomType type = matcher.findMatchingAtomType(iMol, atom);
//                    AtomTypeManipulator.configure(atom, type);
//                }
//                CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(iMol.getBuilder());
//                hAdder.addImplicitHydrogens(iMol);
//                AtomContainerManipulator.convertImplicitToExplicitHydrogens(iMol);

                AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();

                AdHocCmpdFragmentStructure cfs = doStructureStrings(iMol);
                frag.setAdHocCmpdFragmentStructure(cfs);

                AdHocCmpdFragmentPChem cfpc = doCalcs(iMol);
                frag.setAdHocCmpdFragmentPChem(cfpc);

                fragSet.add(frag);

            }

            ahc.setAdHocCmpdFragments(fragSet);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ahc;

    }

    private AdHocCmpdFragmentPChem doCalcs(IMolecule im) throws Exception {

        AdHocCmpdFragmentPChem pchemRtn = AdHocCmpdFragmentPChem.Factory.newInstance();

        DescriptorValue result = null;
        String paramName = null;
        String parsedResult = null;

        ALOGPDescriptor the_ALOGPDescriptor = new ALOGPDescriptor();
        result = the_ALOGPDescriptor.calculate(im);
        paramName = "ALogP";
        parsedResult = parseDescriptorValue(result, paramName);
        pchemRtn.setAlogp(Double.valueOf(parsedResult));

        HBondAcceptorCountDescriptor the_HBondAcceptorCountDescriptor = new HBondAcceptorCountDescriptor();
        result = the_HBondAcceptorCountDescriptor.calculate(im);
        paramName = "nHBAcc";
        parsedResult = parseDescriptorValue(result, paramName);
        pchemRtn.setHba(Integer.parseInt(parsedResult));

        HBondDonorCountDescriptor the_HBondDonorCountDescriptor = new HBondDonorCountDescriptor();
        result = the_HBondDonorCountDescriptor.calculate(im);
        paramName = "nHBDon";
        parsedResult = parseDescriptorValue(result, paramName);
        pchemRtn.setHbd(Integer.parseInt(parsedResult));

        TPSADescriptor the_TPSADescriptor = new TPSADescriptor();
        result = the_TPSADescriptor.calculate(im);
        paramName = "TopoPSA";
        parsedResult = parseDescriptorValue(result, paramName);
        pchemRtn.setPsa(Double.valueOf(parsedResult));

        IMolecularFormula formula = im.getBuilder().newInstance(IMolecularFormula.class);
        MolecularFormulaManipulator.getMolecularFormula(im, formula);
        pchemRtn.setMf(MolecularFormulaManipulator.getString(formula).trim());

        WeightDescriptor the_WeightDescriptor = new WeightDescriptor();
        result = the_WeightDescriptor.calculate(im);
        paramName = "MW";
        parsedResult = parseDescriptorValue(result, paramName);
        pchemRtn.setMw(Double.valueOf(parsedResult));

//System.out.println("Value of alogp is:" + rtn.alogp);
//System.out.println("Value of hba is:" + rtn.hba);
//System.out.println("Value of hbd is:" + rtn.hbd);
//System.out.println("Value of psa is:" + rtn.psa);
//System.out.println("Value of mf is:" + rtn.mf);
//System.out.println("Value of mw is:" + rtn.mw);

        return pchemRtn;

    }

    /**
     *
     * @param im
     * @return
     */
    private static boolean doCheckForPseudoAtoms(IMolecule im) {
        boolean rtn = false;
        for (int atomCnt = 0; atomCnt
                < im.getAtomCount(); atomCnt++) {
            Atom thisAtom = (Atom) im.getAtom(atomCnt);
            if (thisAtom instanceof PseudoAtom) {
                rtn = true;
            }
        }
        return rtn;
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
    public AdHocCmpdFragmentStructure doStructureStrings(IMolecule im) throws Exception {

        AdHocCmpdFragmentStructure rtn = AdHocCmpdFragmentStructure.Factory.newInstance();

        rtn.setSmiles(SMILES_GENERATOR.createSMILES(im));

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MDL_WRITER.setWriter(baos);
            MDL_WRITER.writeMolecule(im);
            rtn.setCtab(baos.toString());
        } catch (Exception e) {
//intended to silence any exception
//caused by R groups and/or PseudoAtoms
        }
        
//        try {
//            InChIGenerator gen = INCHI_FACTORY.getInChIGenerator(im);
//            INCHI_RET ret = gen.getReturnStatus();
//            if (ret == INCHI_RET.WARNING) {
//                System.out.println("InChI warning: " + gen.getMessage());
//            } else if (ret != INCHI_RET.OKAY) {
//                throw new CDKException("InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]");
//            }
//            rtn.setInchi(gen.getInchi());
//            rtn.setInchiAux(gen.getAuxInfo());
//        } catch (Exception e) {
////intended to silence any Inchi-related exceptions
////caused by R groups and/or PseudoAtoms
//        }

        System.out.println("Value of ctab is:" + rtn.getCtab());
        System.out.println("Value of smiles is:" + rtn.getSmiles());
//        System.out.println("Value of inchi is:" + rtn.getInchi());
        System.out.println("Value of inchiAuxInfo is:" + rtn.getInchiAux());

        return rtn;
    }
}