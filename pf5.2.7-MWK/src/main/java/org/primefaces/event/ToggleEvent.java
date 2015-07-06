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

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

import org.primefaces.model.Visibility;

public class ToggleEvent extends AjaxBehaviorEvent {
	
    /** Visibility status */
	private Visibility visibility;
    
    /** Related data if available */
    private Object data;
	
	public ToggleEvent(UIComponent component, Behavior behavior, Visibility visibility) {
		super(component, behavior);
		this.visibility = visibility;
	}
    
    public ToggleEvent(UIComponent component, Behavior behavior, Visibility visibility, Object data) {
		super(component, behavior);
		this.visibility = visibility;
        this.data = data;
	}

	@Override
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return (faceslistener instanceof AjaxBehaviorListener);
	}

	@Override
	public void processListener(FacesListener faceslistener) {
		((AjaxBehaviorListener) faceslistener).processAjaxBehavior(this);
	}
	
	public Visibility getVisibility() {
		return visibility;
	}

    public Object getData() {
        return data;
    }
}