/*
*  Copyright (C) 2010 Mark Southern <southern at scripps dot edu>
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public License
*  as published by the Free Software Foundation; either version 2.1
*  of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program; if not, write to the Free Software
*  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package edu.scripps.fl.cdk;

import java.awt.Color;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.openscience.cdk.renderer.IRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;

/**
 * A class to handle format strings that map to Renderer2DModel properties.
 */
public class CDKExportFormat {

    private String type;
    private Map<String, String> options = new LinkedHashMap<String,String>();;

    public CDKExportFormat() {
        this("");
    }

    public CDKExportFormat(String format) {
    	setOption("type", "jpeg");
    	setOption("width", 350);
    	setOption("height", 350);
    	setOption("ExternalHighlightColor", "255-0-0");
    	setOption("ShowExplicitHydrogends", "false");
    	setOption("ZoomToFit", "true");
        parse(format);
    }
    
    public TreeMap getOptions() {
    	return new TreeMap(options);
    }

    public void setOption(String name, Object value) {
        options.put(name.toLowerCase(), value.toString());
    }

    public String getOption(String name) {
        String val = options.get(name.toLowerCase());
        return val == null ? "" : val;
    }
    
    public int getIntOption(String name) {
    	String val = options.get(name.toLowerCase());
    	return Integer.parseInt(val);
    }
    
    public double getDoubleOption(String name) {
    	String val = options.get(name.toLowerCase());
    	return Double.parseDouble(val);
    }
    
    public boolean getBooleanOption(String name) {
    	String val = options.get(name.toLowerCase());
    	return Boolean.parseBoolean(val);
    }
    
//    public int getIntOption(String name, int defaultValue) {
//    	String val = options.get(name.toLowerCase());
//    	int ii = val == null ? defaultValue : Integer.parseInt(val);
//    	return ii;
//    }
    
    public void clearOptions() {
        options.clear();
    }    
    
    public String build(IRenderer renderer, boolean overwriteExisting) {
    	RendererModel model = renderer.getRenderer2DModel();
    	Map<String, IGeneratorParameter> modelOptions = new HashMap();
    	for(IGeneratorParameter param: model.getRenderingParameters()) {
    		String name = param.getClass().getName().toLowerCase();
    		name = name.replaceAll("[^\\$]+\\$", "");
    		if( options.containsKey(name) & ! overwriteExisting )
    			continue;
    		
    		Object value = param.getValue();

    		try {
	    		ParameterizedType superclass = (ParameterizedType) param.getClass().getGenericSuperclass();
				Class aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
				if(Color.class.isAssignableFrom(aClass) ) {
					Color color = (Color) param.getValue();
					value = String.format("%s-%s-%s", color.getRed(), color.getGreen(), color.getBlue());
				}
    		}
    		catch(Exception ex) {
    			
    		}
			
    		setOption(name, value);
    	}
    	return build();
    }
    
    public void configure(IRenderer renderer) throws Exception {
    	RendererModel model = renderer.getRenderer2DModel();
    	Map<String, IGeneratorParameter> modelOptions = new HashMap();
    	for(IGeneratorParameter param: model.getRenderingParameters()) {
    		String name = param.getClass().getName();
    		name = name.replaceAll("[^\\$]+\\$", "");
    		modelOptions.put(name.toLowerCase(), param);
    	}
    	for(String name: options.keySet()) {
    		if( modelOptions.containsKey(name) ) {
    			IGeneratorParameter param = modelOptions.get(name);
    			
    			String value = getOption(name);
    			
    			ParameterizedType superclass = (ParameterizedType) param.getClass().getGenericSuperclass();
    			Class aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
    			
    			Object obj = null;
    			if(Color.class.isAssignableFrom(aClass) ) {
    				obj = parseColor(value);
    			}
    			else {
    				obj = ConvertUtils.convert(value, aClass);
    			}
    			
    			param.setValue(obj);
    		}
    	}
    }

    protected Color parseColor(String value) {
        Color color = null;
        if (value.startsWith("#"))
            color = Color.decode(value.substring(2));
        else if (value.matches("^\\d+$"))
            color = new Color(Integer.parseInt(value));
        else {
            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(value);
            int[] rgb = new int[3];
            for (int i = 0; matcher.find() & i < 3; i++)
                rgb[i] = Integer.parseInt(matcher.group(1));
            color = new Color(rgb[0], rgb[1], rgb[2]);
        }
        return color;
    }

    public void parse(String format) {
        List<String> words = parseWords("[\\s+,:]",false,format);
        if( words.size() % 2 != 0 )
            words.add("");
        for(int i = 0; i < words.size(); i+=2)
            setOption(words.get(i), words.get(i+1));
    }
    
    public String build() {
        StringBuffer sb = new StringBuffer();
        for (String key : options.keySet()) {
            String value = options.get(key).toString();
            sb.append(key).append(":");
            if (value.matches("[\\s,]"))
                sb.append("\"").append(value).append("\"");
            else
                sb.append(value);
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    protected String delimiterRegex(String delimiter) {
        String regex = "^((?:\\\\.|[^\\\\\"'])*?)" + // an unquoted text
                "(\\Z(?!\\n)|(?:" + delimiter + ")|(?!^)(?=[\\\"']))" + // plus EOL, delimiter, or quote
                "([\\0000-\\0377]*)" // the rest
        ;
        return regex;
    }

    protected static final Pattern quotePattern = Pattern.compile("^([\"'])" + // a quote
            "((?:\\\\.|(?!\\1)[^\\\\])*)" + // quoted text
            "\\1" + // followed by the same quote
            "([\\0000-\\0377]*)" // and the rest
    );

    protected List<String> parseWords(String delimiter, boolean keep, String line) {
        List<String> pieces = new ArrayList<String>();
        Pattern delimPattern = Pattern.compile(delimiterRegex(delimiter));
        while (line.length() > 0) {
            Matcher m = quotePattern.matcher(line);
            if (m.matches()) {
                String quoted = m.group(2);
                String quote = m.group(1);
                if (keep) {
                    quoted = quote + quoted + quote;
                }
                pieces.add(quoted);
                line = line.substring(m.end(2) + quote.length());
            }
            else {
                m = delimPattern.matcher(line);
                if (m.matches()) {
                    line = line.substring(m.end(2), line.length());
                    String unquoted = m.group(1);
                    if (unquoted.length() > 0) {
                        pieces.add(unquoted);
                    }
                }
                else {
                    return pieces;
                }
            }
        }
        return pieces;
    }
}