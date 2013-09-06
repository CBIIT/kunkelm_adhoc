package edu.scripps.fl.test;

import java.io.File;

import org.openscience.cdk.interfaces.IMolecule;

import edu.scripps.fl.cdk.CDKExport;
import edu.scripps.fl.cdk.CDKExportFormat;
import edu.scripps.fl.cdk.CDKFiles;
import edu.scripps.fl.cdk.MoleculeFileExportFormatHandler;

public class MoleculeFileFormatTest {
	
	public static void main(String[] args) throws Exception {

		IMolecule mol = CDKFiles.getMolecule("Brc1c(NC2=NCCN2)ccc3nccnc13");
		
		CDKExport exporter = new CDKExport();
		MoleculeFileExportFormatHandler mf = new MoleculeFileExportFormatHandler();
		
		for(String format: mf.getHandledFormats() ) {
			File file = File.createTempFile("cmp-", "." + format);
			System.out.println("writing to file " + file);
			try {
				exporter.write(file, mol, null, new CDKExportFormat("type:" + format));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}		
	}
}