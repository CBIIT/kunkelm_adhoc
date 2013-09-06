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

import java.awt.Dimension;
import java.io.OutputStream;

import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.emf.EMFGraphics2D;
import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.graphicsio.svg.SVGGraphics2D;
import org.freehep.graphicsio.swf.SWFGraphics2D;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;

public class FreeHEPExportFormatHandler implements ExportFormatHandler {

    public String[] getHandledFormats() {
        return new String[]{"emf", "pdf", "svg", "swf", "ps"};
    }

    public void export(CDKExport exporter, CDKExportFormat format, IAtomContainer molecule, IAtomContainer highlight, OutputStream os) throws Exception {

        String type = format.getOption("type");
        int width = format.getIntOption("width");
        int height = format.getIntOption("height");
        Dimension dim = new Dimension(width, height);

        AbstractVectorGraphicsIO g2 = null;
        if ("emf".equalsIgnoreCase(type)) {
            g2 = new EMFGraphics2D(os, dim);
        } else if ("pdf".equalsIgnoreCase(type)) {
            g2 = new PDFGraphics2D(os, dim);
        } else if ("svg".equalsIgnoreCase(type)) {
            g2 = new SVGGraphics2D(os, dim);
        } else if ("swf".equalsIgnoreCase(type)) {
            g2 = new SWFGraphics2D(os, dim);
        } else if ("ps".equalsIgnoreCase(type)) {
            g2 = new PSGraphics2D(os, dim);
        } else {
            throw new IllegalArgumentException(String.format("Class %s does not handle exports of type %s", getClass().getName(), type));
        }

        AtomContainerRenderer renderer = exporter.buildRenderer(format, molecule, highlight);
        RendererModel model = renderer.getRenderer2DModel();

        g2.setDeviceIndependent(true);
        g2.startExport();
        g2.setColor(model.getParameter(BasicSceneGenerator.BackgroundColor.class).getValue());
        g2.fillRect(0, 0, width, height);
        renderer.paint(molecule, new AWTDrawVisitor(g2));
        g2.endExport();
        g2.dispose();
    }
}
