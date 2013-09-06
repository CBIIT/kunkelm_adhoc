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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.*;

import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;

@SuppressWarnings("serial")
public class CDKExportServlet extends HttpServlet {

    private ChemicalMIME mime;
    private CDKExport exporter;

    public CDKExportServlet() {
        super();
        exporter = new CDKExport();
        exporter.addExportFormatHandler(new FreeHEPExportFormatHandler());
        mime = new ChemicalMIME();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private String toHTMLTable(Map map, String caption, String keyHeader, String valueHeader) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table><caption>").append(caption).append("</caption><thead><tr><td>").append(keyHeader)
                .append("</td><td>").append(valueHeader)
                .append("</td></tr></thead><tbody>");
        for (Object key : map.keySet()) {
            sb.append("<tr><td>")
                    .append(key)
                    .append("</td><td>")
                    .append(map.get(key))
                    .append("</td></tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameterMap().size() == 0) {
            resp.setContentType("text/html");
            String realPath = getServletContext().getRealPath("index.html");
            IOUtils.copy(new FileInputStream(realPath), resp.getOutputStream());
            return;
        }

        String parameters = req.getParameter("parameters");
        if (null != parameters && !"".equals(parameters)) {
            CDKExportFormat format = new CDKExportFormat();
            List generators = new ArrayList();
            generators.add(new BasicSceneGenerator());
            generators.add(new BasicBondGenerator());
            generators.add(new BasicAtomGenerator());
            AtomContainerRenderer renderer = new AtomContainerRenderer(generators, new AWTFontManager());
            String build = format.build(renderer, false);
            resp.setContentType("text/html");
            resp.getWriter().write(toHTMLTable(format.getOptions(), "Supported parameters", "Parameter Name", "Default Value"));
            return;
        }

        try {
            IMolecule mol = null;

            // is there a substructure to highlight?
            IMolecule highlightMol = null;
            String highlight = req.getParameter("highlight");
            if (highlight != null && !highlight.equals("")) {
                highlightMol = CDKFiles.getMolecule(highlight);
            }

            // is there a molecule to to convert
            String struct = req.getParameter("structure");
            if (struct != null) {
                mol = CDKFiles.getMolecule(struct);
            } else {
                String id = req.getParameter("id");
                String type = req.getParameter("idtype");
                if (null != id & ("cid".equalsIgnoreCase(type) || "sid".equalsIgnoreCase(type))) {
                    mol = CDKFiles.getPubChemMolecule(id, type);
                } else {
                    return;
                }
            }

            CDKExportFormat fBuilder;
            // what format is the output?
            String export = req.getParameter("export");
            if (export == null || export.equals("")) {
                fBuilder = new CDKExportFormat();
            } else {
                fBuilder = new CDKExportFormat(export);
            }

            String filename = req.getParameter("filename");
            if (null != filename && !"".equals(filename)) {
                String mimeType = mime.getMime(fBuilder.getOption("type"));
                if (mimeType != null) {
                    resp.setContentType(mimeType);
                }

//				String ext = mime.getExtension(fBuilder.getOption("type"));
//				if (ext != null) {
//					String filenamename = "cdkexport" + ext;
                resp.addHeader("Content-Disposition", "attachment; filename=" + filename);
//				}
            }

            exporter.write(resp.getOutputStream(), mol, highlightMol, fBuilder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}