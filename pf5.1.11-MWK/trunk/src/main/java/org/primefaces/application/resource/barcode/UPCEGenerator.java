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
package org.primefaces.application.resource.barcode;

import java.io.IOException;
import org.krysalis.barcode4j.impl.upcean.UPCEBean;
import org.krysalis.barcode4j.output.CanvasProvider;

public class UPCEGenerator implements BarcodeGenerator {
    
    public void generate(CanvasProvider canvasProvider, String value) throws IOException {
        UPCEBean bean = new UPCEBean();
        bean.generateBarcode(canvasProvider, value);
    }
}
