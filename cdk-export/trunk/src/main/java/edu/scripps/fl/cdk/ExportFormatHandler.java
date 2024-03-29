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

public interface ExportFormatHandler {
    public String[] getHandledFormats();
    public void export(CDKExport exporter, CDKExportFormat format, IAtomContainer molecule, IAtomContainer highlight, OutputStream os) throws Exception;
}
