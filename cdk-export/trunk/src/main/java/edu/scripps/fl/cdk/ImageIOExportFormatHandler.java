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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;

public class ImageIOExportFormatHandler implements ExportFormatHandler {

    public String[] getHandledFormats() {
        return new String[]{"jpeg", "png", "bmp", "wbmp", "gif"};
    }

    public void export(CDKExport exporter, CDKExportFormat format, IAtomContainer molecule, IAtomContainer highlight, OutputStream os) throws Exception {

        AtomContainerRenderer renderer = exporter.buildRenderer(format, molecule, highlight);
        
        int width = format.getIntOption("width");
        int height = format.getIntOption("height");

        RendererModel model = renderer.getRenderer2DModel();

        Image image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(model.getParameter(BasicSceneGenerator.BackgroundColor.class).getValue());
        g2.fillRect(0, 0, width, height);
        renderer.paint(molecule, new AWTDrawVisitor(g2));
        g2.dispose();
        String type = format.getOption("type");
        ImageIO.write((RenderedImage) image, type, os);
    }
}