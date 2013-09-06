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

import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.mcss.RMap;
import org.openscience.cdk.layout.StructureDiagramGenerator;
//import org.openscience.cdk.renderer.Renderer;

import org.openscience.cdk.renderer.AtomContainerRenderer;

import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.RendererModel.ExternalHighlightColor;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class CDKExport {

    public Map<String, ExportFormatHandler> handlers = new HashMap<String, ExportFormatHandler>();

    public CDKExport() {
        addExportFormatHandler(new ImageIOExportFormatHandler());
        addExportFormatHandler(new MoleculeFileExportFormatHandler());
    }

    public void addExportFormatHandler(ExportFormatHandler handler) {
        for (String format : handler.getHandledFormats()) {
            handlers.put(format, handler);
        }
    }

    public void removeExportFormatHandler(ExportFormatHandler handler) {
        for (String format : handler.getHandledFormats()) {
            handlers.remove(format);
        }
    }

//    public Renderer buildRenderer(CDKExportFormat format, IAtomContainer molecule, IAtomContainer highlight) throws Exception {
//        List generators = new ArrayList();
//        generators.add(new BasicSceneGenerator());
//        generators.add(new BasicBondGenerator());
//        generators.add(new BasicAtomGenerator());
//
//        Renderer renderer = new Renderer(generators, new AWTFontManager());
//
//        format.configure(renderer);
//
//        if (format.getBooleanOption("ShowExplicitHydrogens")) {
//            AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
//        }
//
//        int width = format.getIntOption("width");
//        int height = format.getIntOption("height");
//        Rectangle drawArea = new Rectangle(width, height);
//        renderer.setup(molecule, drawArea);
//
//        if (format.getBooleanOption("ZoomToFit")) {
//            Rectangle diagram = renderer.calculateDiagramBounds(molecule);
//            renderer.setZoomToFit(drawArea.width, drawArea.height, diagram.width, diagram.height);
//        }
//
//        RendererModel model = renderer.getRenderer2DModel();
//
//        if (highlight != null) {
////			model.getParameter(RendererModel.ExternalHighlightColor.class).setValue(Color.RED);
////			model.setExternalSelectedPart(highlight);
////			this code is a duplicate of what is in the Renderer but it doesn't seem to set the colorHash there?
//            Map<IChemObject, Color> colorHash = new HashMap();
//            for (int i = 0; i < highlight.getAtomCount(); i++) {
//                colorHash.put(highlight.getAtom(i), model.getParameter(ExternalHighlightColor.class).getValue());
//            }
//            Iterator<IBond> bonds = highlight.bonds().iterator();
//            while (bonds.hasNext()) {
//                colorHash.put(bonds.next(), model.getParameter(ExternalHighlightColor.class).getValue());
//            }
//            model.getParameter(RendererModel.ColorHash.class).setValue(colorHash);
//        }
//
//        return renderer;
//    }
    public AtomContainerRenderer buildRenderer(CDKExportFormat format, IAtomContainer molecule, IAtomContainer highlight) throws Exception {
        
        List generators = new ArrayList();
        generators.add(new BasicSceneGenerator());
        generators.add(new BasicBondGenerator());
        generators.add(new BasicAtomGenerator());

        AtomContainerRenderer renderer = new AtomContainerRenderer(generators, new AWTFontManager());

        format.configure(renderer);

        if (format.getBooleanOption("ShowExplicitHydrogens")) {
            AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
        }

        int width = format.getIntOption("width");
        int height = format.getIntOption("height");
        
        Rectangle drawArea = new Rectangle(width, height);
        renderer.setup(molecule, drawArea);

        if (format.getBooleanOption("ZoomToFit")) {
            Rectangle diagram = renderer.calculateDiagramBounds(molecule);
            renderer.setZoomToFit(drawArea.width, drawArea.height, diagram.width, diagram.height);
        }

        RendererModel model = renderer.getRenderer2DModel();

        if (highlight != null) {
//			model.getParameter(RendererModel.ExternalHighlightColor.class).setValue(Color.RED);
//			model.setExternalSelectedPart(highlight);
//			this code is a duplicate of what is in the Renderer but it doesn't seem to set the colorHash there?
            Map<IChemObject, Color> colorHash = new HashMap();
            for (int i = 0; i < highlight.getAtomCount(); i++) {
                colorHash.put(highlight.getAtom(i), model.getParameter(ExternalHighlightColor.class).getValue());
            }
            Iterator<IBond> bonds = highlight.bonds().iterator();
            while (bonds.hasNext()) {
                colorHash.put(bonds.next(), model.getParameter(ExternalHighlightColor.class).getValue());
            }
            model.getParameter(RendererModel.ColorHash.class).setValue(colorHash);
        }

        return renderer;
    }

    protected IAtomContainer getOverlaps(IAtomContainer a, IAtomContainer q) throws CDKException {
        
        IAtomContainer needle = DefaultChemObjectBuilder.getInstance().newInstance(AtomContainer.class);
        Vector idlist = new Vector();

        List l = UniversalIsomorphismTester.getSubgraphMaps(a, q);
        List maplist = (List) l.get(0);
        for (Iterator i = maplist.iterator(); i.hasNext();) {
            RMap rmap = (RMap) i.next();
            idlist.add(new Integer(rmap.getId1()));
        }

        HashSet hs = new HashSet(idlist);
        for (Iterator i = hs.iterator(); i.hasNext();) {
            needle.addBond(a.getBond(((Integer) i.next()).intValue()));
        }
        return needle;
    }

    public void write(String filename, String molecule, String format) throws Exception, CDKException, IOException {
        IMolecule mol = CDKFiles.getMolecule(molecule);
        write(new File(filename), mol, null, new CDKExportFormat(format));
    }

    public void write(String filename, String molecule, String highlightMolecule, String format) throws Exception, CDKException, IOException {
        IMolecule mol = CDKFiles.getMolecule(molecule);
        IMolecule highlightMol = null;
        if (highlightMolecule != null && !"".equals(highlightMolecule)) {
            highlightMol = CDKFiles.getMolecule(highlightMolecule);
        }
        write(new File(filename), mol, highlightMol, new CDKExportFormat(format));
    }

    public void write(File file, IMolecule molecule, String format) throws Exception, CDKException, IOException {
        write(file, molecule, null, new CDKExportFormat(format));
    }

    public void write(File file, IMolecule molecule, CDKExportFormat exportFormat) throws Exception, CDKException, IOException {
        write(file, molecule, null, exportFormat);
    }

    public void write(File file, IMolecule molecule, IMolecule highlightMolecule, String format) throws Exception, CDKException, IOException {
        write(file, molecule, highlightMolecule, new CDKExportFormat(format));
    }

    public void write(File file, IMolecule molecule, IMolecule highlightMolecule, CDKExportFormat format) throws Exception, CDKException, IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        write(os, molecule, highlightMolecule, format);
        os.flush();
        os.close();
    }

    public void write(OutputStream os, IMolecule molecule, String format) throws Exception, CDKException, IOException {
        write(os, molecule, null, new CDKExportFormat(format));
    }

    public void write(OutputStream os, IMolecule molecule, CDKExportFormat exportFormat) throws Exception, CDKException, IOException {
        write(os, molecule, null, exportFormat);
    }

    public void write(OutputStream os, IMolecule molecule, IMolecule highlightMolecule, String format) throws Exception, CDKException, IOException {
        write(os, molecule, highlightMolecule, new CDKExportFormat(format));
    }

    public void write(OutputStream os, IMolecule molecule, IMolecule highlightMolecule, CDKExportFormat format) throws Exception, CDKException, IOException {
        
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(molecule);
        sdg.generateCoordinates();
        molecule = sdg.getMolecule();

        IAtomContainer highlight = null;
        if (highlightMolecule != null) {
            highlight = getOverlaps(molecule, highlightMolecule);
        }

        String type = format.getOption("type");
        if (type.equals("")) {
            type = "jpeg";
        }
        ExportFormatHandler handler = handlers.get(type);
        if (handler == null) {
            throw new IllegalArgumentException(String.format("Unrecognized image format '%s'", type));
        } else {
            handler.export(this, format, molecule, highlight, os);
        }
    }
}