/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newstructureservlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.vo.CmpdVO;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.isomorphism.Mappings;
import org.openscience.cdk.isomorphism.Pattern;
import org.openscience.cdk.renderer.SymbolVisibility;
import org.openscience.cdk.renderer.color.CDK2DAtomColors;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.standard.StandardGenerator;
import static org.openscience.cdk.renderer.generators.standard.StandardGenerator.ANNOTATION_LABEL;
import static org.openscience.cdk.renderer.generators.standard.StandardGenerator.HIGHLIGHT_COLOR;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.smarts.SmartsPattern;

/**
 *
 * @author mwkunkel based on examples from John May's blog:
 * http://efficientbits.blogspot.com/
 */
public class StructureServlet extends HttpServlet {

  private byte[] getTextImage(String incomingText) {
    byte[] rtn = new byte[0];
    int referenceSize = 20;
    int strLength = incomingText.length() * referenceSize;
    try {
      BufferedImage bufferedImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2 = bufferedImg.createGraphics();
      g2.setPaint(Color.GRAY);
      g2.fill(new Rectangle.Double(0, 0, 200, 200));
      Font font = new Font("sans-serif", Font.PLAIN, referenceSize);
      g2.setFont(font);
      g2.setPaint(Color.RED);
      g2.drawString(incomingText, referenceSize, referenceSize);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(bufferedImg, "png", baos);
      rtn = baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return rtn;
  }
  
//   private byte[] renderImage(String incomingTitleString, String smiles, String querySmiles, Integer structureDim) throws Exception {
//
//        byte[] byteArray = new byte[0];
//
//        try {
//
//            // parse mol
//            IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
//            SmilesParser sp = new SmilesParser(builder);
//
//            IMolecule theMol = sp.parseSmiles(smiles);
//
//            // try to deduce bonds for ring systems
//            // flag is false by default, only changed if deduce is successful
//            // used below to determine generator for image (BondGen if successful, otherwise RingGen
//            //boolean ableToDeduceBonds = false;
//            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(theMol);
//
//            FixBondOrdersTool fbot = new FixBondOrdersTool();
//            theMol = fbot.kekuliseAromaticRings(theMol);
//
//            // generate coordinates
//            StructureDiagramGenerator sdg = new StructureDiagramGenerator();
//            sdg.setMolecule(theMol);
//
//            // NEED TO CATCH org.openscience.cdk.exception.CDKException: Molecule not connected.      
//            try {
//                sdg.generateCoordinates();
//            } catch (Exception e) {
//                System.out.println(e);
//                System.out.println(smiles);
//                throw e;
//            }
//
//            theMol = sdg.getMolecule();
//
//            // set up for rendering
//            Rectangle drawArea = new Rectangle(structureDim, structureDim);
//            Image image = new BufferedImage(structureDim, structureDim, BufferedImage.TYPE_INT_RGB);
//
//            List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
//            
//            generators.add(new BasicSceneGenerator());            
//            generators.add(new ExternalHighlightGenerator());
//            generators.add(new BasicAtomGenerator());
//            generators.add(new BasicBondGenerator());
//
////            // only add RingGenerator if deduceBonds was unsuccessful
////            if (!ableToDeduceBonds) {
////                //generators.add(new RingGenerator());
////            }
//
//            AtomContainerRenderer renderer = new AtomContainerRenderer(generators, new AWTFontManager());
//            
//            renderer.setup(theMol, drawArea);
//
//            IAtomContainer selection = new AtomContainer();
//
//            if (querySmiles != null && querySmiles.length() > 0) {
//
//                SMARTSQueryTool queryTool = new SMARTSQueryTool(querySmiles);
//                queryTool.matches(theMol);
//
//                if (queryTool.countMatches() > 0) {
//                    List<List<Integer>> mappings = queryTool.getUniqueMatchingAtoms();
//                    for (List<Integer> mapping : mappings) {
//                        for (Integer i : mapping) {
//                            IAtom theAtom = theMol.getAtom(i);
//                            selection.addAtom(theAtom);
//                        }
//                    }
//
//                    // go through theMol and find the bonds that include other selection atoms
//                    for (int bondCnt = 0; bondCnt <= theMol.getBondCount() - 1; bondCnt++) {
//                        IBond b = theMol.getBond(bondCnt);
//                        IAtom a1 = b.getAtom(0);
//                        IAtom a2 = b.getAtom(1);
//                        if (selection.contains(a1) && selection.contains(a2)) {
//                            selection.addBond(b);
//                        }
//                    }
//                }
//            }
//
//            // calculate zoom to fit
//            Rectangle diagram = renderer.calculateDiagramBounds(theMol);
//            renderer.setZoomToFit(drawArea.width, drawArea.height, diagram.width, diagram.height);
//
////            RendererModel model = renderer.getRenderer2DModel();
////
////            if (!selection.isEmpty()) {
////                model.set(ExternalHighlightGenerator.ExternalHighlightDistance.class, (double) 12);
////                model.set(RendererModel.ExternalHighlightColor.class, Color.PINK);
////                model.setExternalSelectedPart(selection);
////            }
//
//            Graphics2D g2 = (Graphics2D) image.getGraphics();
//
//            g2.setColor(Color.WHITE);
//
//            g2.fillRect(0, 0, structureDim, structureDim);
//
//            // render title if specified      
//            if (incomingTitleString != null && incomingTitleString.length() > 0) {
//
//                String[] titleArray;
//
//                if (incomingTitleString.contains("xxx")) {
//                    titleArray = incomingTitleString.split("xxx");
//                } else {
//                    titleArray = new String[1];
//                    titleArray[0] = incomingTitleString;
//                }
//
//                int referenceSize = Math.round(structureDim / 10);
//
//                Font font = new Font("sans-serif", Font.PLAIN, referenceSize);
//
//                for (int tCnt = 0; tCnt < titleArray.length; tCnt++) {
//
//                    String curTitle = titleArray[tCnt];
//
//                    int titleLength = curTitle.length() * referenceSize;
//
//                    //g2.setPaint(Color.pink);
//                    //g2.fill(new Rectangle.Double(0, tCnt * referenceSize, titleLength, referenceSize));
//                    g2.setFont(font);
//                    g2.setPaint(Color.LIGHT_GRAY);
//
//                    g2.drawString(curTitle, 0, (tCnt + 1) * referenceSize);
//
//                }
//
//            }
//
//            renderer.paint(theMol, new AWTDrawVisitor(g2));
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            ImageIO.write((RenderedImage) image, "png", baos);
//
//            baos.flush();
//            baos.close();
//
//            byteArray = baos.toByteArray();
//
//        } catch (Exception e) {
//
//            // if ANYTHING goes wrong, just return simple message
//            byteArray = getTextImage("Unable to render structure image.");
//
//            //e.printStackTrace();
//            throw new Exception("Exception in renderImage: " + e);
//
//        }
//
//        return byteArray;
//
//    }

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   *   
* @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String title = null;
    String smiles = null;
    String querySmiles = null;
    Integer structureDim = null;
    List<String> smilesList = null;

    if (request.getParameter("title") != null) {
      title = request.getParameter("title");
    }

    if (request.getParameter("smiles") != null) {
      smiles = request.getParameter("smiles");
    }

    if (request.getParameter("querySmiles") != null) {
      querySmiles = request.getParameter("querySmiles");
    }

    if (request.getParameter("structureDim") != null) {
      String structureDimString = request.getParameter("structureDim");
      structureDim = Integer.valueOf(structureDimString);
    }

    if (structureDim == null) {
      structureDim = 200;
    }

    if (request.getParameterValues("smilesArray") != null) {
      smilesList = new ArrayList<String>();
      String[] smiStrArr = request.getParameterValues("smilesArray");
      for (String smi : smiStrArr) {
        smilesList.add(smi);
      }
      for (String s : smilesList) {
        System.out.println("smilesList: " + s);
      }
    }

// intercept possible nsc specification here
// this will NOT override an existing smiles    
    if (request.getParameter("nsc") != null && smiles == null) {
      Integer nscInt = Integer.valueOf(request.getParameter("nsc"));
      CmpdVO cVO = null;
      if (nscInt != null) {
        cVO = HelperCmpd.getSingleCmpdByNsc(nscInt, "PUBLIC");
        if (cVO != null && cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi() != null) {
          smiles = cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi();
        }
      }
    }

    if (smiles != null && smiles.length() > 0) {

      IAtomContainer container = MolInput.fromSmiles(smiles);

      // configure font
      final Font font = new Font("Verdana", Font.PLAIN, 18);

      final DepictionGenerator generator = new DepictionGenerator(
              Arrays.asList(
                      new BasicSceneGenerator(),
                      new StandardGenerator(font)));

      if (request.getParameter("kekule") != null) {
        generator.getRendererModel2D()
                .set(StandardGenerator.Visibility.class, SymbolVisibility.all());
      }
      if (request.getParameter("color-atoms") != null) {
        generator.getRendererModel2D()
                .set(StandardGenerator.AtomColor.class, new CDK2DAtomColors());
      }

//                generator.getRendererModel2D()
//                        .set(StandardGenerator.StrokeRatio.class, Double.parseDouble(cmdln.getOptionValue("sts", "1")));
      generator.getRendererModel2D()
              .set(StandardGenerator.StrokeRatio.class, 1d);

      // highlight smarts pattern?
      if (querySmiles != null) {
        highlightSmartsPattern(container, Color.RED, querySmiles);
      }

      if (request.getParameter("atom-numbers") != null) {
        for (int i = 0; i < container.getAtomCount(); i++) {
          container.getAtom(i).setProperty(ANNOTATION_LABEL,
                  Integer.toString(1 + i));
        }
      }

      // do the depiction generation
      final Depiction depiction = generator.generate(container);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(depiction.toBufferedImage(structureDim, structureDim), "png", baos);
      baos.flush();
      baos.close();
      byte[] byteArray = baos.toByteArray();

      response.setContentType("image/png");
      response.setContentLength(byteArray.length);
      response.setHeader("Content-Disposition", "inline; filename=structure.png");
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.getOutputStream().write(byteArray);
      response.getOutputStream().flush();
      response.getOutputStream().close();

    } else {

      byte[] byteArray = getTextImage("NSC or SMILES must be specified");

      response.setContentType("image/png");
      response.setContentLength(byteArray.length);
      response.setHeader("Content-Disposition", "inline; filename=structure.png");
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.getOutputStream().write(byteArray);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    }
  }

  protected void processRequestExperimental(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    RequestParameters reqParam = new RequestParameters(request);

    String title = reqParam.getParamValue("title", "");
    String smiles = reqParam.getParamValue("smiles", "");
    String querySmiles = reqParam.getParamValue("querySmiles", "");

    int structureDim = 200;
    try {
      String strcDimStr = reqParam.getParamValue("structureDim", "200");
      structureDim = Integer.valueOf(strcDimStr);
    } catch (Exception e) {
      e.printStackTrace();
    }

    int nscInt = -10101;
    try {
      String nscStr = reqParam.getParamValue("nsc", "-10101");
      nscInt = Integer.valueOf(nscStr);
    } catch (Exception e) {
      e.printStackTrace();
    }

// intercept possible nsc specification here
// this will NOT override an existing smiles    
    if (nscInt > 0 && smiles == null) {
      CmpdVO cVO = null;
      cVO = HelperCmpd.getSingleCmpdByNsc(nscInt, "PUBLIC");
      if (cVO != null && cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi() != null) {
        smiles = cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi();
      }
    }

    if (smiles.length() > 0) {

      IAtomContainer container = MolInput.fromSmiles(smiles);

      // configure font
      final Font font = new Font("Verdana", Font.PLAIN, 18);

      final DepictionGenerator generator = new DepictionGenerator(
              Arrays.asList(
                      new BasicSceneGenerator(),
                      new StandardGenerator(font)));

      if (reqParam.hasParam("kekule")) {
        generator.getRendererModel2D()
                .set(StandardGenerator.Visibility.class, SymbolVisibility.all());
      }
      if (reqParam.hasParam("color-atoms")) {
        generator.getRendererModel2D()
                .set(StandardGenerator.AtomColor.class, new CDK2DAtomColors());
      }

//                generator.getRendererModel2D()
//                        .set(StandardGenerator.StrokeRatio.class, Double.parseDouble(cmdln.getOptionValue("sts", "1")));
      generator.getRendererModel2D()
              .set(StandardGenerator.StrokeRatio.class, 1d);

      // highlight smarts pattern?
      if (querySmiles.length() > 0) {
        highlightSmartsPattern(container, Color.RED, querySmiles);
      }

      if (reqParam.hasParam("atom-numbers")) {
        for (int i = 0; i < container.getAtomCount(); i++) {
          container.getAtom(i).setProperty(ANNOTATION_LABEL,
                  Integer.toString(1 + i));
        }
      }

      // do the depiction generation
      final Depiction depiction = generator.generate(container);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(depiction.toBufferedImage(structureDim, structureDim), "png", baos);
      baos.flush();
      baos.close();
      byte[] byteArray = baos.toByteArray();

      response.setContentType("image/png");
      response.setContentLength(byteArray.length);
      response.setHeader("Content-Disposition", "inline; filename=structure.png");
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.getOutputStream().write(byteArray);
      response.getOutputStream().flush();
      response.getOutputStream().close();

    } else {

      byte[] byteArray = getTextImage("NSC invalid or not found or SMILES not specified.");

      response.setContentType("image/png");
      response.setContentLength(byteArray.length);
      response.setHeader("Content-Disposition", "inline; filename=structure.png");
      response.setHeader("Expires", "0");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      response.setHeader("Pragma", "public");
      response.getOutputStream().write(byteArray);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    }
  }

  private static void highlightSmartsPattern(IAtomContainer container, Color highlightColor, String smarts) {
    try {
      Pattern ptrn = SmartsPattern.create(smarts,
              SilentChemObjectBuilder.getInstance());
      Mappings mappings = ptrn.matchAll(container);

      int[] p = mappings.first();
      for (int x : p) {
        container.getAtom(x).setProperty(HIGHLIGHT_COLOR,
                highlightColor);
      }
      for (IBond bond : container.bonds()) {
        if (bond.getAtom(0).getProperty(HIGHLIGHT_COLOR) != null && bond.getAtom(1).getProperty(HIGHLIGHT_COLOR) != null) {
          bond.setProperty(HIGHLIGHT_COLOR,
                  highlightColor);
        }
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   *   
* @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *   
* @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *   
* @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "PNG graphic image, 200X200, for a specified nsc number or smiles string";
  }// </editor-fold>

}
