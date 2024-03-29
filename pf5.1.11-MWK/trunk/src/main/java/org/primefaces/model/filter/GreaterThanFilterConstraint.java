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
package org.primefaces.model.filter;

import java.util.Locale;

public class GreaterThanFilterConstraint implements FilterConstraint {

    public boolean applies(Object value, Object filter, Locale locale) {
        if(filter == null) {
            return true;
        }
                
        if(value instanceof Comparable) {
            int compared = ((Comparable) value).compareTo(filter);
            
            return (compared > 0);
        }
        
        return false;
    }
}
