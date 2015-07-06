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
package org.primefaces.component.media.player;

public class PDFPlayer implements MediaPlayer {

	private final static String[] supportedTypes = new String[]{"pdf"};
	
	public String getClassId() {
		return null;
	}

	public String getCodebase() {
		return null;
	}
	
	public String getSourceParam() {
		return null;
	}

	public String getType() {
		return "application/pdf";
	}

    public String[] getSupportedTypes() {
        return supportedTypes;
    }
}