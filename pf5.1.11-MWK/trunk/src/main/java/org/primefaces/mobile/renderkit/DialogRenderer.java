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
package org.primefaces.mobile.renderkit;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.util.WidgetBuilder;

public class DialogRenderer extends org.primefaces.component.dialog.DialogRenderer {
    
    @Override
    protected void encodeScript(FacesContext context, Dialog dialog) throws IOException {
        String clientId = dialog.getClientId(context);
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("Dialog", dialog.resolveWidgetVar(), clientId);

        wb.attr("visible", dialog.isVisible(), false)
            .attr("modal", dialog.isModal(), false)
            .attr("dynamic", dialog.isDynamic(), false)
            .attr("showEffect", dialog.getShowEffect(), null)
            .attr("closeOnEscape", dialog.isCloseOnEscape(), false)  
            .callback("onHide", "function()", dialog.getOnHide())
            .callback("onShow", "function()", dialog.getOnShow());
        
        String focusExpressions = SearchExpressionFacade.resolveComponentsForClient(
        		context, dialog, dialog.getFocus());
        if (focusExpressions != null) {
        	wb.attr("focus", focusExpressions);
        }

        encodeClientBehaviors(context, dialog);
         
        wb.finish();
    }

    @Override
    protected void encodeMarkup(FacesContext context, Dialog dialog) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = dialog.getClientId(context);
        String style = dialog.getStyle();
        String styleClass = dialog.getStyleClass();
        styleClass = (styleClass == null) ? Dialog.MOBILE_CONTAINER_CLASS : Dialog.MOBILE_CONTAINER_CLASS + " " + styleClass;

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }
        
        if(dialog.isShowHeader()) {
            encodeHeader(context, dialog);
        }
        
        encodeContent(context, dialog);

        writer.endElement("div");
    }
    
    @Override
    protected void encodeHeader(FacesContext context, Dialog dialog) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String header = dialog.getHeader();
        UIComponent headerFacet = dialog.getFacet("header");
        
        writer.startElement("div", null);
        writer.writeAttribute("class", Dialog.MOBILE_TITLE_BAR_CLASS, null);
        
        //close
        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("class", Dialog.MOBILE_CLOSE_ICON_CLASS, null);
        writer.endElement("a");
        
        //title
        writer.startElement("h1", null);
        writer.writeAttribute("class", Dialog.MOBILE_TITLE_CLASS, null);
        writer.writeAttribute("role", "heading", null);
        
        if(headerFacet != null)
            headerFacet.encodeAll(context);
        else if(header != null)
            writer.write(header);
        
        writer.endElement("h1");
        
        writer.endElement("div");
    }
    
    @Override
    protected void encodeContent(FacesContext context, Dialog dialog) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-content", null);
        writer.writeAttribute("role", "main", null);
        
        if(!dialog.isDynamic()) {
            renderChildren(context, dialog);
        }
        
        writer.endElement("div");
    }
}