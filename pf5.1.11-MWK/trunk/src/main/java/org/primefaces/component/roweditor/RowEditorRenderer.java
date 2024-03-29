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
package org.primefaces.component.roweditor;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.renderkit.CoreRenderer;

public class RowEditorRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        RowEditor rowEditor = (RowEditor) component;
        String style = rowEditor.getStyle();
        String styleClass = rowEditor.getStyleClass();
        styleClass = (styleClass == null) ? DataTable.ROW_EDITOR_CLASS : DataTable.ROW_EDITOR_CLASS + " " + styleClass;

        writer.startElement("div", null);
        writer.writeAttribute("id", component.getClientId(context), null);
        writer.writeAttribute("class", styleClass, null);
        if(style != null) {
            writer.writeAttribute("style", style, null);
        }

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-icon ui-icon-pencil", null);
        writer.endElement("span");

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-icon ui-icon-check", null);
        writer.endElement("span");

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-icon ui-icon-close", null);
        writer.endElement("span");

        writer.endElement("div");
    }
}
