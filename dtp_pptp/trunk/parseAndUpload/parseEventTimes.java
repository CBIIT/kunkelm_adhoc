import java.io.*;
import java.io.File.*;
/**
 *  opens a 05*.stats file and writes a 05*.stats.processed file converts tabs
 *  to commas
 *
 * @author     mkunkel
 * @created    July 23, 2006
 */
public class parseEventTimes {

	/**
	 *  Description of the Method
	 *
	 * @param  args  The command line arguments
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage: java parseEventTimes compound (0501 or 0503, etc.)");
			return;
		}

		int i = 0;
		int lineCounter = 0;
		FileReader fr = null;
		FileWriter fw = null;

		String compound;

		try {

			compound = args[0];

			System.out.println("parseEventTimes processing compound: " + compound);

			File inputFile = new File("/home/mwkunkel/PPTP/csvFiles/" + compound + "events.csv");

			StringBuffer outputBuffer = new StringBuffer();

			String currentLine;
			String tempString;
			String[] splitLine = null;
			String delimiters = "[,]";

			String thisCompound = "null";
			String thisCell = "null";
			int thisTestNumber = 0;
			String thisStage = "null";
			String thisGroupRole = "null";

			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			currentLine = br.readLine();
			lineCounter++;

			/*
			 *  if (!checkHeaders(currentLine)) {
			 *  return;
			 *  }
			 */
			while ((currentLine = br.readLine()) != null) {

				lineCounter++;
				i = 0;

				if (currentLine.length() < 1) {
					System.out.println("-->Line " + lineCounter + "- found zero-length line");
				} else if (currentLine.charAt(0) == '#') {
				} else {

					splitLine = currentLine.split(",");

					tempString = splitLine[i];
					i++;

					String[] innerStrings = tempString.split("-");

//compound
					thisCompound = innerStrings[0];
//cell
					thisCell = innerStrings[1];

					//check for retest designation

					if (thisCell.endsWith("R1")) {
						//System.out.println("Retest found in " + thisCell);
						thisCell = thisCell.substring(0, thisCell.length() - 2);
						thisTestNumber = 2;
						//System.out.println("Cell line: " + thisCell + " test number: " + thisTestNumber);
					} else if (thisCell.endsWith("R2")) {
						//System.out.println("Retest found in " + thisCell);
						thisCell = thisCell.substring(0, thisCell.length() - 2);
						thisTestNumber = 3;
						//System.out.println("Cell line: " + thisCell + " test number: " + thisTestNumber);
					} else if (thisCell.endsWith("R3")) {
						//System.out.println("Retest found in " + thisCell);
						thisCell = thisCell.substring(0, thisCell.length() - 2);
						thisTestNumber = 4;
						//System.out.println("Cell line: " + thisCell + " test number: " + thisTestNumber);
					} else {
						thisTestNumber = 1;
					}

//stage
					thisStage = innerStrings[2];

//groupRole
					tempString = splitLine[i];
					i++;
					if (tempString.equals("A")) {
						thisGroupRole = "CONTROL";
					} else if (tempString.equals("B")) {
						thisGroupRole = "TREATED";
					} else {
						throw new Exception("Unrecognized groupRole.");
					}

//mouses 1 - 10
//fields all have possibility of a flag

//to force this into the Mouse data model, the day is set to -10101 and the time to event is repeated

					for (int m = 1; m < 11; m++) {
						tempString = splitLine[i];
						//System.out.println("Parsing mouse " + m + " " + tempString);
						i++;
						if (tempString.equals(".")) {
							//do nothing, mouse was missing
						} else if (tempString.equals("-1")) {
							//do nothing, tox mouse
						} else if (tempString.equals("-2")) {
							//do nothing, sac mouse
						} else {
							if (tempString.charAt(0) == '<') {
								outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + ",-10101," + m + "," + tempString.substring(1) + ",-10101\n");
							} else if (tempString.charAt(0) == '>') {
								outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + ",-10101," + m + "," + tempString.substring(1) + ",-10101\n");
							} else {
								outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + ",-10101," + m + "," + tempString + ",-10101\n");
							}
						}
					}
				}
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

