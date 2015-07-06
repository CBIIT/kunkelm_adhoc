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
package org.primefaces.model.diagram.connector;

import java.io.Serializable;

public abstract class Connector implements Serializable {
    
    private String paintStyle;
    
    private String hoverPaintStyle;

    public Connector() {
    }

    public String getPaintStyle() {
        return paintStyle;
    }

    public void setPaintStyle(String paintStyle) {
        this.paintStyle = paintStyle;
    }

    public String getHoverPaintStyle() {
        return hoverPaintStyle;
    }

    public void setHoverPaintStyle(String hoverPaintStyle) {
        this.hoverPaintStyle = hoverPaintStyle;
    }
    
    public abstract String getType();
    
    public abstract String toJS(StringBuilder sb);
}