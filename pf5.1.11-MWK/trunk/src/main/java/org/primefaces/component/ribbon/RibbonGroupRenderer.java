/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under PrimeFaces Commercial License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.primefaces.org/elite/license.xhtml
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.component.ribbon;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;

public class RibbonGroupRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        RibbonGroup group = (RibbonGroup) component;
        String label = group.getLabel();
        String groupClass = group.getStyleClass();
        groupClass = (groupClass == null) ? Ribbon.GROUP_CLASS : Ribbon.GROUP_CLASS + " " + groupClass;
        String style = group.getStyle();
        
        writer.startElement("li", null);
        writer.writeAttribute("class", groupClass, null);
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }
        
        writer.startElement("div", null);
        writer.writeAttribute("class", Ribbon.GROUP_CONTENT_CLASS, null);
        renderChildren(context, group);
        writer.endElement("div");
        
        writer.startElement("div", null);
        writer.writeAttribute("class", Ribbon.GROUP_LABEL_CLASS, null);
        if(label != null) {
            writer.writeText(label, null);
        }
        writer.endElement("div");
        
        writer.endElement("li");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
