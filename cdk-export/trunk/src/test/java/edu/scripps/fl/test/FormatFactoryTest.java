package edu.scripps.fl.test;

import java.util.Arrays;

import org.openscience.cdk.io.FormatFactory;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.io.formats.IChemFormatMatcher;

public class FormatFactoryTest {

    public static void main(String[] args) {
        FormatFactory factory = new FormatFactory();
        for (IChemFormatMatcher matcher : factory.getFormats()) {
            if (matcher instanceof IChemFormat) {
                IChemFormat format = (IChemFormat) matcher;
//    			if ( null != format.getWriterClassName() ) { 
                String line = String.format("%s\t%s\t%s\t%s", format.getFormatName(), format.getPreferredNameExtension(), format.getWriterClassName(), Arrays.asList(format.getNameExtensions()));
                System.out.println(line);
//    			}
            }
        }
    }
}
