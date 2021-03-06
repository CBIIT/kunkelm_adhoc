/*
 * Copyright 2014 jagatai.
 *
 * Licensed under PrimeFaces Commercial License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.primefaces.org/elite/license.xhtml
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.model;

import javax.el.ValueExpression;
import org.primefaces.component.api.UIColumn;

public class FilterMeta {

    private UIColumn column;
    private ValueExpression filterByVE;
    private Object filterValue;

    public FilterMeta(UIColumn column, ValueExpression filterByVE, Object filterValue) {
        this.column = column;
        this.filterByVE = filterByVE;
        this.filterValue = filterValue;
    }

    public UIColumn getColumn() {
        return column;
    }

    public ValueExpression getFilterByVE() {
        return filterByVE;
    }

    public Object getFilterValue() {
        return filterValue;
    }

}
