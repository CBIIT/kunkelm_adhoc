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
package org.primefaces.component.chart.renderer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.chart.Chart;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.util.ComponentUtils;

public class DonutRenderer extends BasePlotRenderer {
    
    @Override
    protected void encodeData(FacesContext context, Chart chart) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        DonutChartModel model = (DonutChartModel) chart.getModel();
        List data = model.getData();
        
        writer.write(",data:[");
        for(int i = 0; i < data.size() ; i++) {
            if(i != 0) {
                writer.write(",");
            }
            
            writer.write("[");
            Map<String, Number> map = (Map) data.get(i);
            for(Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                Number value = map.get(key);

                writer.write("[\"" + ComponentUtils.escapeText(key) + "\"," + value + "]");

                if(it.hasNext()) {
                    writer.write(",");
                }
            }
            writer.write("]");
        }
        
        writer.write("]");
    }
    
    @Override
    protected void encodeOptions(FacesContext context, Chart chart) throws IOException {
        super.encodeOptions(context, chart);
        
        ResponseWriter writer = context.getResponseWriter();
        DonutChartModel model = (DonutChartModel) chart.getModel();
        int sliceMargin = model.getSliceMargin();
        boolean fill = model.isFill();
        boolean showDataLabels = model.isShowDataLabels();
        String dataFormat = model.getDataFormat();
		
        if(sliceMargin != 0) writer.write(",sliceMargin:" + sliceMargin);
        if(!fill) writer.write(",fill:false");
        if(showDataLabels) writer.write(",showDataLabels:true");
        if(dataFormat != null) writer.write(",dataFormat:\"" + dataFormat + "\"");
    }
}
