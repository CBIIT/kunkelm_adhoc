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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.io.SMILESReader;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class CDKFiles {

    public static String getSmilesFromCID(String id) throws CDKException, IOException {
        return getSmilesFromPubChem(id,"cid");
    }
    
    public static String getSmilesFromSID(String id) throws CDKException, IOException {
        return getSmilesFromPubChem(id,"sid");
    }
    
    public static String getSmilesFromPubChem(String id, String type) throws CDKException, IOException {
        IMolecule mol = getPubChemMolecule(id,type);
        SmilesGenerator generator = new SmilesGenerator();
        return generator.createSMILES(mol);
    }
    
    public static IMolecule getPubChemMolecule(String id, String type) throws CDKException, IOException {
        IMolecule mol = null;
        URL url = new URL(String.format("http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?%s=%s&disopt=SaveSDF",type,id));
        // can't seem to get this to work with ReaderFactory
        Reader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        IteratingMDLReader mdlReader = new IteratingMDLReader(reader, DefaultChemObjectBuilder.getInstance());
        if( mdlReader.hasNext() ) {
            mol = (IMolecule) mdlReader.next();
            mol = (IMolecule) AtomContainerManipulator.removeHydrogens(mol);
        }
        return mol;
    }
    
    public static IMolecule getMoleculeFromFactory(Reader reader) throws CDKException, IOException {
        IMolecule mol = null;
        ReaderFactory readerFactory = new ReaderFactory();
        ISimpleChemObjectReader chemReader = readerFactory.createReader(reader);
        IChemFile content = (IChemFile) chemReader.read((ChemObject) new ChemFile());
        if (content == null)
            return null;
        Iterator iter = content.chemSequences().iterator();
        if( iter.hasNext() )
            mol = (IMolecule) iter.next();
        return mol;
    }
    
    public static IMolecule getMolecule(String structure) throws CDKException, IOException {
        return getMolecule(structure,true);
    }
    
    public static IMolecule getMolecule(String structure, boolean addImplicitHydrogens) throws CDKException, IOException {
        IMolecule mol = null;
        try {
            SMILESReader sr = new SMILESReader(new StringReader(structure));
            MoleculeSet set = (MoleculeSet) sr.read(new MoleculeSet());
            mol = set.getMolecule(0);
            if( mol.getAtomCount() == 0 )
                mol = getMoleculeFromFactory(new StringReader(structure));
            
        }
        catch (CDKException notSmiles) {
            mol = getMoleculeFromFactory(new StringReader(structure));
        }
        if( addImplicitHydrogens ) {
        	CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
            adder.addImplicitHydrogens(mol);
        }
        return mol;
    }
}
