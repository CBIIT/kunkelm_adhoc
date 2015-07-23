/*
 * Copyright 2009,2010 Prime Technology.
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

import java.util.List;

public interface DashboardModel {

	public void addColumn(DashboardColumn column);
	
	public List<DashboardColumn> getColumns();
	
	public int getColumnCount();
	
	public DashboardColumn getColumn(int index);
	
	public void transferWidget(DashboardColumn fromColumn, DashboardColumn toColumn, String widgetId, int index);
}