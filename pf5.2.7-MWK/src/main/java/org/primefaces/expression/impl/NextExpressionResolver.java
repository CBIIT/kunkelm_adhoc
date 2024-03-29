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
package org.primefaces.expression.impl;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionResolver;

/**
 * {@link SearchExpressionResolver} for the "@next" keyword.
 */
public class NextExpressionResolver implements SearchExpressionResolver {

	public UIComponent resolveComponent(FacesContext context, UIComponent source, UIComponent last, String expression) {
		UIComponent parent = last.getParent();

		if (parent.getChildCount() > 1) {
			List<UIComponent> children = parent.getChildren();
			int index = children.indexOf(last);

			if (index < parent.getChildCount() - 1)
			{
				return children.get(index + 1);
			}			
		}

		return null;
	}

}
