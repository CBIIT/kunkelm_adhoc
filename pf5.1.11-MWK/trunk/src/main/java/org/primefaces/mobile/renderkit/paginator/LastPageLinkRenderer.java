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
package org.primefaces.mobile.renderkit.paginator;

import java.io.IOException;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.UIData;
import org.primefaces.component.paginator.PaginatorElementRenderer;

public class LastPageLinkRenderer extends PageLinkRenderer implements PaginatorElementRenderer {

    public void render(FacesContext context, UIData uidata) throws IOException {
        int currentPage = uidata.getPage();
        int pageCount = uidata.getPageCount();
        
        boolean disabled = (currentPage == (pageCount - 1)) || (currentPage == 0 && pageCount == 0);
        
        super.render(context, uidata, "ui-paginator-last ui-btn ui-btn-icon-notext ui-icon-carat-r", disabled);
    }
    
}
