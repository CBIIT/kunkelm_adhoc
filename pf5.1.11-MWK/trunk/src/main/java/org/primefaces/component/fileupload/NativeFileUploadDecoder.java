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
package org.primefaces.component.fileupload;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.NativeUploadedFile;

public class NativeFileUploadDecoder {

    public static void decode(FacesContext context, FileUpload fileUpload) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            if(fileUpload.getMode().equals("simple")) {
                decodeSimple(context, fileUpload, request);
            }
            else {
                decodeAdvanced(context, fileUpload, request);
            }
        } 
        catch (IOException ioe) {
            throw new FacesException(ioe);
        } 
        catch (ServletException se) {
            throw new FacesException(se);
        }
    }
    
    private static void decodeSimple(FacesContext context, FileUpload fileUpload, HttpServletRequest request) throws IOException, ServletException {
        Part part = request.getPart(fileUpload.getSimpleInputDecodeId(context));
        
        if(part != null) {
            fileUpload.setTransient(true);
            fileUpload.setSubmittedValue(new NativeUploadedFile(part));
        }
        else {
            fileUpload.setSubmittedValue("");
        }
	}
    
    private static void decodeAdvanced(FacesContext context, FileUpload fileUpload, HttpServletRequest request) throws IOException, ServletException {
        String clientId = fileUpload.getClientId(context);
        Part part = request.getPart(clientId);

        if(part != null) {
            fileUpload.setTransient(true);
            fileUpload.queueEvent(new FileUploadEvent(fileUpload, new NativeUploadedFile(part)));
        }
	}
    
}
