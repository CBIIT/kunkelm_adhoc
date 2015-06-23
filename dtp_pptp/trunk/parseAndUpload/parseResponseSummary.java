import java.io.*;
import java.io.File.*;

/**
 *  Description of the Class
 *
 * @author     mkunkel
 * @created    July 23, 2006
 */

public class parseResponseSummary {

	/**
	 *  Description of the Method
	 *
	 * @param  args  The command line arguments
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage: java parseResponseSummary compound (0501 or 0503, etc.)");
			return;
		}

		int i = 0;

		int lineCounter = 0;
		FileReader fr = null;
		FileWriter fw = null;

		try {

			String compound = args[0];

			System.out.println("parseResponseSummary processing compound from " + compound);

			File inputFile = new File("/home/mwkunkel/PPTP/csvFiles/" + compound + "responsesummary.csv");

			StringBuffer outputBuffer = new StringBuffer();

			String currentLine;
			String tempString;
			String[] splitLine = null;
			String delimiters = "[,]";

			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			currentLine = br.readLine();

			while ((currentLine = br.readLine()) != null) {

				lineCounter++;
				i = 0;

				if (currentLine.length() < 1) {
					System.out.println("-->Line " + lineCounter + "- found zero-length line");
				} else if (currentLine.charAt(0) == '#') {
				} else {

					splitLine = currentLine.split(delimiters);

//write the compound to the beginning of the row

					outputBuffer.append(compound + ",");

					//Cell Line

					int thisTestNumber = 0;

					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing cell line from " + tempString);

					if (tempString.equals(".")) {
						throw new Exception("Cell line cannot be null at line " + lineCounter);
					} else {
						//check for retest designation
						if (tempString.endsWith("R1")) {
							//System.out.println("Retest found in " + tempString);
							tempString = tempString.substring(0, tempString.length() - 2);
							thisTestNumber = 2;
							//System.out.println("Cell line: " + tempString + " test number: " + thisTestNumber);
						} else if (tempString.endsWith("R2")) {
							//System.out.println("Retest found in " + tempString);
							tempString = tempString.substring(0, tempString.length() - 2);
							thisTestNumber = 3;
							//System.out.println("Cell line: " + tempString + " test number: " + thisTestNumber);
						} else if (tempString.endsWith("R3")) {
							//System.out.println("Retest found in " + tempString);
							tempString = tempString.substring(0, tempString.length() - 2);
							thisTestNumber = 4;
							//System.out.println("Cell line: " + tempString + " test number: " + thisTestNumber);
						} else {
							thisTestNumber = 1;
						}
						outputBuffer.append(tempString + "," + thisTestNumber + ",");
					}

					//Histology
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("IGNORING histology from " + tempString);

					//KM Estimate of Median Time to Event
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("IGNORING KM estimate from " + tempString);

					//P-value
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("IGNORING pValue from " + tempString);

					//EFS T/C
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("IGNORING EFS T/C from " + tempString);

					//Median Final RTV
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("IGNORING median final RTV from " + tempString);

					//Tumor Volume T/C
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("IGNORING T/C from " + tempString);

					if (compound.equals("0501")
							 || compound.equals("0502")
							 || compound.equals("0503")
							 || compound.equals("0601")
							 || compound.equals("0604")
							 || compound.equals("0613")
							 || compound.equals("0504")
							 || compound.equals("0505")
							 || compound.equals("0507")
							 || compound.equals("0508")
							 || compound.equals("0509")
							) {
					} else {
						//P-value
						tempString = splitLine[i];
						tempString.trim();
						i++;
						//System.out.println("IGNORING pValue from " + tempString);
					}

					//T/C Activity
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing T/C activity from " + tempString);
					outputBuffer.append(tempString + ",");

					//EFS Activity
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing EFS activity from " + tempString);
					outputBuffer.append(tempString + ",");

					//Response Activity
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing ResponseActivity from " + tempString);
					outputBuffer.append(tempString);
				}

				outputBuffer.append("\n");

			}

			fr.close();
			fr = null;

//open and write to output file
			File outputFile = new File("/home/mwkunkel/PPTP/csvFiles/" + inputFile.getName() + ".processed");
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

