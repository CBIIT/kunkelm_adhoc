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
package org.primefaces.component.inputtextarea;

import java.util.List;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;

import org.primefaces.facelets.MethodRule;

public class InputTextareaHandler extends ComponentHandler {

	private static final MetaRule COMPLETE_METHOD =
			new MethodRule("completeMethod", List.class, new Class[]{String.class});
	
	public InputTextareaHandler(ComponentConfig config) {
		super(config);
	}
	
	@SuppressWarnings("unchecked")
	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type); 
		
		metaRuleset.addRule(COMPLETE_METHOD);
		
		return metaRuleset; 
	} 
}
