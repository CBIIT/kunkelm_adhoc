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
package org.primefaces.event;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import org.primefaces.component.api.AutoUpdatable;
import org.primefaces.context.RequestContext;

/**
 * Registers components to auto update before rendering
 */
public class AutoUpdateComponentListener implements SystemEventListener {

    public void processEvent(SystemEvent cse) throws AbortProcessingException {
        AutoUpdatable component = (AutoUpdatable) cse.getSource();
        FacesContext context = FacesContext.getCurrentInstance();

        if(component.isAutoUpdate() && context.isPostback()) {
            
            if (!RequestContext.getCurrentInstance().isIgnoreAutoUpdate()) {
        		context.getPartialViewContext().getRenderIds().add(component.getClientId(context));
        	}
        }
    }

    public boolean isListenerForSource(Object o) {
        return true;
    }
}