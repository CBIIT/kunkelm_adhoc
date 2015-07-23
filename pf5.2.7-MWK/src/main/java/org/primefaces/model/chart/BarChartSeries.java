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
package org.primefaces.model.chart;

import java.io.IOException;
import java.io.Writer;

public class BarChartSeries extends ChartSeries {

    private boolean disableStack;
    
    public BarChartSeries() {}

    public boolean isDisableStack() {
        return disableStack;
    }

    public void setDisableStack(boolean disableStack) {
        this.disableStack = disableStack;
    }

    @Override
    public String getRenderer() {
        return "BarRenderer";
    }
    
    @Override
    public void encode(Writer writer) throws IOException {
        writer.write("{");
        writer.write("label:'" + this.getLabel() + "'");

        writer.write(",renderer: $.jqplot." + this.getRenderer());
        
        if(disableStack) writer.write(",disableStack:true");

        writer.write("}");
    }
}