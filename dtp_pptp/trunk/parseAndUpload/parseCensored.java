import java.io.*;
import java.io.File.*;

/**
 *  Description of the Class
 *
 *@author     mkunkel
 *@created    July 23, 2006
 */

public class parseCensored {

	/**
	 *  Description of the Method
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage: java parseCensored compound (0501 or 0503, etc.)");
			return;
		}

		int i = 0;

		int lineCounter = 0;
		FileReader fr = null;
		FileWriter fw = null;

		try {

			String compound = args[0];

			System.out.println("parseCensored processing compound from " + compound);

			File inputFile = new File("/home/mwkunkel/PPTP/csvFiles/" + compound + "censored.csv");

			StringBuffer outputBuffer = new StringBuffer();

			String currentLine;
			String tempString;
			String[] splitLine = null;
			String delimiters = "[,]";

			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);

			currentLine = br.readLine();

			//System.out.println("First line for " + compound + "," + currentLine);

			while ((currentLine = br.readLine()) != null) {

				lineCounter++;
				i = 0;

				if (currentLine.length() < 1) {
					//System.out.println("-->Line " + lineCounter + "- found zero-length line");
				} else if (currentLine.charAt(0) == '#') {

				} else {

					splitLine = currentLine.split(delimiters);

//write the compound to the beginning of the row

					outputBuffer.append(compound + ",");

//Panel IGNORED
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing panel IGNORED from " + tempString);

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

//Line Description IGNORED
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing lineDescription IGNORED from " + tempString);

//Treatment Group
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing treatment group from " + tempString);
					if (tempString.equals("A")) {
						outputBuffer.append("CONTROL,");
					} else if (tempString.equals("B")) {
						outputBuffer.append("TREATED,");
					} else {
						//System.out.println("Unrecognized treatment group in line" + lineCounter);
					}

//N1
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing N1 from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//D1
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing D1 from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//E1
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing E1 from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//N2
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing N2 from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//SURVIVOR %
					if (compound.equals("0501")) {
						outputBuffer.append("null,");
					} else {
						tempString = splitLine[i];
						tempString.trim();
						i++;
						//System.out.println("Parsing percent survivor from " + tempString);
						if (tempString.charAt(tempString.length() - 1) == '%') {
							String newString = tempString.substring(0, tempString.length() - 1);
							//System.out.println("Setting percent survivors to " + newString);
							outputBuffer.append(Integer.parseInt(newString) + ",");
						} else {
							//System.out.println("Setting percent survivors to " + tempString);
							outputBuffer.append(Integer.parseInt(tempString) + ",");
						}
					}

//# of Mice with Events
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing # of Mice with Events from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//KM Estimate of Median Days to Event
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing KM Estimate of Median Days to Event from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(tempString + ",");
					}

//p-value
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing p-value flag from " + tempString);
					//System.out.println("Parsing p-value from " + tempString);

					if (tempString.equals(".")) {
						outputBuffer.append("null,null,");
					} else {
						if (tempString.charAt(0) == '<') {
							outputBuffer.append("LESS_THAN," + tempString.substring(1) + ",");
						} else if (tempString.charAt(0) == '>') {
							outputBuffer.append("GREATER_THAN," + tempString.substring(1) + ",");
						} else {
							outputBuffer.append("null," + tempString + ",");
						}
					}

//EFS T/C
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing EFS T/C flag from " + tempString);
					//System.out.println("Parsing EFS T/C from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,null,");
					} else {
						if (tempString.charAt(0) == '<') {
							outputBuffer.append("LESS_THAN," + tempString.substring(1) + ",");
						} else if (tempString.charAt(0) == '>') {
							outputBuffer.append("GREATER_THAN," + tempString.substring(1) + ",");
						} else {
							outputBuffer.append("null," + tempString + ",");
						}
					}

//Median RTV
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing Median RTV flag from " + tempString);
					//System.out.println("Parsing Median RTV from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,null,");
					} else {
						if (tempString.charAt(0) == '<') {
							outputBuffer.append("LESS_THAN," + tempString.substring(1) + ",");
						} else if (tempString.charAt(0) == '>') {
							outputBuffer.append("GREATER_THAN," + tempString.substring(1) + ",");
						} else {
							outputBuffer.append("null," + tempString + ",");
						}
					}

//Average RTV at T/C assessment day
					if (compound.equals("0501")
							 || compound.equals("0502")
							 || compound.equals("0503")
							 || compound.equals("0504")
							 || compound.equals("0505")
							 || compound.equals("0507")
							 || compound.equals("0509")) {

						//System.out.println("Skipping parse of Average RTV at T/C assessment day.");
						outputBuffer.append("null,");

					} else {
						tempString = splitLine[i];
						tempString.trim();
						i++;
						//System.out.println("Parsing Average RTV from " + tempString);
						if (tempString.equals(".")) {
							outputBuffer.append("null,");
						} else {
							outputBuffer.append(tempString + ",");
						}
					}

//T/C
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing T/C from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(tempString + ",");
					}

//T/C Assessment Day
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing T/C Assessment day from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(tempString + ",");
					}

					if (compound.equals("0501")
							 || compound.equals("0502")
							 || compound.equals("0503")
							 || compound.equals("0504")
							 || compound.equals("0505")
							 || compound.equals("0507")
							 || compound.equals("0509")) {

						//System.out.println("Skipping parse of T/C p-value");
						outputBuffer.append("null,null,");

					} else {

//p-value
						tempString = splitLine[i];
						tempString.trim();
						i++;
						//System.out.println("Parsing T/C p-value flag from " + tempString);
						//System.out.println("Parsing T/C p-value from " + tempString);
						if (tempString.equals(".")) {
							outputBuffer.append("null,null,");
						} else {
							if (tempString.charAt(0) == '<') {
								outputBuffer.append("LESS_THAN," + tempString.substring(1) + ",");
							} else if (tempString.charAt(0) == '>') {
								outputBuffer.append("GREATER_THAN," + tempString.substring(1) + ",");
							} else {
								outputBuffer.append("null," + tempString + ",");
							}
						}
					}

//# PD
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing #PD from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//# PD1
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing #PD1 from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//# PD2
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing #PD2 from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//# SD
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing #SD from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//# PR
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing #PR from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//# CR
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing #CR from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//# MCR
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing #MCR from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(Integer.parseInt(tempString) + ",");
					}

//Response (Median Score)
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing Response (Median Scrore) from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null,");
					} else {
						outputBuffer.append(tempString + ",");
					}

//Overall Median Response for Group
					tempString = splitLine[i];
					tempString.trim();
					i++;
					//System.out.println("Parsing Overall Median Response  for Group from " + tempString);
					if (tempString.equals(".")) {
						outputBuffer.append("null");
					} else {
						outputBuffer.append(tempString);
					}

					outputBuffer.append("\n");

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

