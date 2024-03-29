/*
 * Copyright 2009-2015 PrimeTek.
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
package org.primefaces.model;

import javax.faces.model.ListDataModel;

public class SelectableDataModelWrapper extends ListDataModel implements SelectableDataModel {

    private SelectableDataModel original;

    public SelectableDataModelWrapper(SelectableDataModel original, Object data) {
        this.original = original;
        setWrappedData(data);
    }

    public Object getRowData(String rowKey) {
        return original.getRowData(rowKey);
    }

    public Object getRowKey(Object object) {
        return original.getRowKey(object);
    }
}
