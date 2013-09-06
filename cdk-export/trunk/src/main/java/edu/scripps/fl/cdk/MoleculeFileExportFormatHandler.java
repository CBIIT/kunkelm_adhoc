/*
 *  Copyright (C) 2010 Mark Southern <southern at scripps dot edu>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package edu.scripps.fl.cdk;

import java.io.OutputStream;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.CDKSourceCodeWriter;
import org.openscience.cdk.io.CMLWriter;
import org.openscience.cdk.io.CrystClustWriter;
import org.openscience.cdk.io.HINWriter;
import org.openscience.cdk.io.IChemObjectWriter;
import org.openscience.cdk.io.MDLRXNWriter;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.Mol2Writer;
import org.openscience.cdk.io.PDBWriter;
import org.openscience.cdk.io.RGroupQueryWriter;
import org.openscience.cdk.io.RssWriter;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.SMILESWriter;
import org.openscience.cdk.io.ShelXWriter;
import org.openscience.cdk.io.XYZWriter;

public class MoleculeFileExportFormatHandler implements ExportFormatHandler {

    public String[] getHandledFormats() {
        return new String[]{"cdk", "cml", "crystclust", "hin", "mdlrxn", "mol", "mol2", "pdb", "rgfile", "rss", "sdf", "ins", "smi", "smiles", "xyz"};
    }

    public void export(CDKExport exporter, CDKExportFormat format, IAtomContainer molecule, IAtomContainer highlight, OutputStream os) throws Exception {
        String type = format.getOption("type");
        IChemObjectWriter writer = null;
        if ("cdk".equalsIgnoreCase(type)) {
            writer = new CDKSourceCodeWriter(os);
        } else if ("cml".equalsIgnoreCase(type)) {
            writer = new CMLWriter(os);
        } else if ("crystclust".equalsIgnoreCase(type)) {
            writer = new CrystClustWriter(os);
        } else if ("hin".equalsIgnoreCase(type)) {
            writer = new HINWriter(os);
        } else if ("mdlrxn".equalsIgnoreCase(type)) {
            writer = new MDLRXNWriter(os);
        } else if ("mol".equalsIgnoreCase(type)) {
            writer = new MDLV2000Writer(os);
        } else if ("mol2".equalsIgnoreCase(type)) {
            writer = new Mol2Writer(os);
        } else if ("pdb".equalsIgnoreCase(type)) {
            writer = new PDBWriter(os);
        } else if ("rgfile".equalsIgnoreCase(type)) {
            writer = new RGroupQueryWriter();
        } else if ("rss".equalsIgnoreCase(type)) {
            writer = new RssWriter();
        } else if ("sdf".equalsIgnoreCase(type)) {
            writer = new SDFWriter(os);
        } else if ("ins".equalsIgnoreCase(type)) {
            writer = new ShelXWriter(os);
        } else if ("smi".equalsIgnoreCase(type) || "smiles".equalsIgnoreCase(type)) {
            writer = new SMILESWriter();
        } else if ("xyz".equalsIgnoreCase(type)) {
            writer = new XYZWriter(os);
        }
        writer.write(molecule);
    }
}