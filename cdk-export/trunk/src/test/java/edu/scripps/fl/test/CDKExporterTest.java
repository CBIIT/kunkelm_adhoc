package edu.scripps.fl.test;

import java.awt.Desktop;
import java.io.File;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.tools.CDKHydrogenAdder;

import edu.scripps.fl.cdk.CDKExportFormat;
import edu.scripps.fl.cdk.CDKExport;
import edu.scripps.fl.cdk.CDKFiles;

public class CDKExporterTest {
	
	public static void main(String[] args) throws Exception {
		IMolecule highlight = null;
		
//		IMolecule mol = MoleculeFactory.make123Triazole();
//		IMolecule mol = CDKFiles.getPubChemMolecule("2519", "cid");
//		IMolecule mol = CDKFiles.getPubChemMolecule("6845", "cid");
//		IMolecule mol = CDKFiles.getPubChemMolecule("241", "cid");
//		IMolecule highlight = CDKFiles.getPubChemMolecule("241", "cid");
		
		IMolecule mol = CDKFiles.getMolecule("Brc1c(NC2=NCCN2)ccc3nccnc13");
		
		highlight = CDKFiles.getMolecule("C1=CC2=C(C=C1)N=CC=N2");
		
//		IMolecule mol = MoleculeFactory.makeBiphenyl();
//		IMolecule highlight = MoleculeFactory.makeBenzene();
		
		File file = File.createTempFile("cmp-", ".png");
		System.out.println("writing to file " + file);
		CDKExport exporter = new CDKExport();		
		exporter.write(file, mol, highlight, new CDKExportFormat("type:png,ExternalHighlightColor:0-255-0"));
		Desktop.getDesktop().open(file);
	}
}