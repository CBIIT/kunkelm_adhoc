import java.io.*;
import java.util.StringTokenizer;
/**
 *  opens a dap*.txt file and writes a dap*.txt.processed file 
 *  converts tabs to commas
 * @author     mkunkel
 * @created    July 23, 2006
 */
public class parseAffy {

	/**
	 *  Description of the Method
	 *
	 * @param  args  The command line arguments
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage: java parseAffy fileName (dap068_u133v2-met.txt, etc.)");
			return;
		}

		int lineCounter = 0;
		FileReader fr = null;
		FileWriter fw = null;

		String fileName;
		String cellLineIdentifier;

		try {

			fileName = args[0];
						
			File inputFile = new File("/home/mwkunkel/PPTP/Expression/TXT/" + fileName);

			StringBuffer outputBuffer = new StringBuffer();

			String currentLine;
			String tempString;

			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

//read the first line to extract the cellLineIdentifier

			currentLine = br.readLine();
			lineCounter++;
			cellLineIdentifier = currentLine.substring(0, 6);

//crank through until the line with the column headings

			while ((currentLine = br.readLine()) != null) {

				lineCounter++;

				if (currentLine.length() > 15 && currentLine.substring(0, 14).equals("Probe Set Name")) {
					break;
				}
			}

// parse out all of the data lines
// tab delimited, no option for missing data (will barf)

			while ((currentLine = br.readLine()) != null) {

				lineCounter++;

				if (currentLine.length() < 1) {
//if line is blank then error
					System.out.println("-->Line " + lineCounter + "- found zero-length line");
				} else if (currentLine.charAt(0) == '#') {
//if line starts with # then ignore
				} else {

					StringTokenizer lineTokenizer = new StringTokenizer(currentLine, "\t");

					outputBuffer.append(cellLineIdentifier + ",");

//Probe Set Name
					tempString = lineTokenizer.nextToken();
					outputBuffer.append(tempString + ",");

//Stat Pairs
					tempString = lineTokenizer.nextToken();
					outputBuffer.append(tempString + ",");

//Stat Pairs Used
					tempString = lineTokenizer.nextToken();
					outputBuffer.append(tempString + ",");

//Signal
					tempString = lineTokenizer.nextToken();
					outputBuffer.append(tempString + ",");

//Detection
					tempString = lineTokenizer.nextToken();
					outputBuffer.append(tempString + ",");

//Detection p-value
					tempString = lineTokenizer.nextToken();
					outputBuffer.append(tempString);

					outputBuffer.append("\n");

				}

			}

			fr.close();
			fr = null;

//open and write to output file
			File outputFile = new File("/home/mwkunkel/PPTP/Expression/TXT/" + inputFile.getName() + ".processed");
			fw = new FileWriter(outputFile);

			fw.write(outputBuffer.toString());

			fw.close();
			fw = null;

		} catch (Exception e) {
			System.out.println("Caught exception " + e + " at line " + lineCounter);
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
					fr = null;
				} catch (Exception e) {
				}
			}
			if (fw != null) {
				try {
					fw.close();
					fw = null;
				} catch (Exception e) {
				}
			}
		}
	}
}

