/*
 * Copyright 2009-2015 PrimeTek.
 *
 * Licensed under PrimeFaces Commercial License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.model.chart;

public enum LegendPlacement {
    
    OUTSIDE("outside"),
    OUTSIDEGRID("outsideGrid"),
    INSIDE("inside");
    
    private final String text;
    
    LegendPlacement(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
