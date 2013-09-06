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

import java.util.*;

/*
 * List of MIME types obtained from here: http://www.ch.ic.ac.uk/chemime/
 */

public class ChemicalMIME {

	private Map<String,String> mimeTypes;
	private Map<String,String> extensions;

	public ChemicalMIME() {
		mimeTypes = new HashMap<String,String>();
		extensions = new HashMap<String,String>();

		String[] a = new String[] {
		   // image types
		    "emf",    "emf", "application/x-msMetafile"
		   // format string, extension, mime type
		   ,"cml",    "cml", "chemical/x-cml"
		   ,"mol",    "mol", "chemical/x-mdl-molfile"
		   ,"sdf",    "sd", "chemical/x-mdl-sdfile"
		   ,"rxn",    "rxn", "chemical/x-mdl-rxnfile"
		   ,"rdf",    "rd", "chemical/x-mdl-rdfile"
		   // csmol, csrgf, cssdf, csrxn, csrdf,
		   ,"smiles", "smi", "chemical/x-daylight-smiles"
		   ,"smarts", "smi", "chemical/x-daylight-smiles"
		   ,"sybyl",  "mol2", "chemical/x-mol2"
		   ,"mol2",   "mol2", "chemical/x-mol2"
		   ,"pdb",    "pdb", "chemical/x-pdb"
		   ,"xyz",    "xyz", "chemical/x-xyz"
		   ,"cube",   "cub", "chemical/x-gaussian-cube"
//		   ,"inchi"
		};

		for(int ii = 0; ii < a.length - 2; ii+=3) {
			extensions.put(a[ii],a[ii+1]);
			mimeTypes.put(a[ii],a[ii+2]);
		}
	}

	public String getMime(String format) {
		return mimeTypes.get(format);
	}

	public String getExtension(String format) {
		return extensions.get(format);
	}
}