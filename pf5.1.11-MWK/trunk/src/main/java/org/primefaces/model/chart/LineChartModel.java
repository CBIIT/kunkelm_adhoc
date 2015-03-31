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
package org.primefaces.model.chart;

public class LineChartModel extends CartesianChartModel {
 
    private boolean stacked = false;
    private boolean breakOnNull = false;
    
    public boolean isStacked() {
        return stacked;
    }
    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public boolean isBreakOnNull() {
        return breakOnNull;
    }
    public void setBreakOnNull(boolean breakOnNull) {
        this.breakOnNull = breakOnNull;
    }
}
