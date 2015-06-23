import java.io.*;
import java.io.File.*;

/**
*  Description of the Class
*
* @author     mkunkel
* @created    January 17, 2007
*/
public class parseStats {
	
	/**
	*  Description of the Method
	*
	* @param  args  The command line arguments
	*/
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Usage: java parseStats compound (0501 or 0503, etc.)");
			return;
		}
		
		int i = 0;
		int lineCounter = 0;
		FileReader fr = null;
		FileWriter fw = null;
		
		String compound;
		
		try {
			
			compound = args[0];
			
			System.out.println("parseStats processing compound: " + compound);
			
			File inputFile = new File("/home/mwkunkel/PPTP/csvFiles/" + compound + "stats.csv");
			
			StringBuffer outputBuffer = new StringBuffer();
			
			String currentLine;
			String tempString;
			String[] splitLine = null;
			String delimiters = "[,]";
			
			String thisCompound = null;
			String thisCell = null;
			int thisTestNumber = 0;
			String thisStage = null;
			
			String thisGroupRole = null;
			String thisDay = null;
			
			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);
			
			currentLine = br.readLine();
			lineCounter++;
			
			while ((currentLine = br.readLine()) != null) {
				
				//System.out.println("Fetching new line.");
				
				lineCounter++;
				i = 0;
				
				if (currentLine.length() < 1) {
					System.out.println("-->Line " + lineCounter + "- found zero-length line");
				} else if (currentLine.charAt(0) == '#') {
				} else {
					
					splitLine = currentLine.split(delimiters);
					
					//compound-cell-stage
					
					tempString = splitLine[i];
					i++;
					//System.out.println("parsing compound-cell-stage: " + tempString);
					
					String[] innerStrings = tempString.split("-");
					
					//compound
					thisCompound = innerStrings[0];
					//cell
					thisCell = innerStrings[1];
					
					//check for retest designation
					
					if (thisCell.endsWith("R1")) {
						System.out.println("Retest found in " + thisCell);
						thisCell = thisCell.substring(0, thisCell.length() - 2);
						thisTestNumber = 2;
						//System.out.println("Cell line: " + thisCell + " test number: " + thisTestNumber);
					} else if (thisCell.endsWith("R2")) {
						//System.out.println("Retest found in " + thisCell);
						thisCell = thisCell.substring(0, thisCell.length() - 2);
						thisTestNumber = 3;
						//System.out.println("Cell line: " + thisCell + " test number: " + thisTestNumber);
					} else if (thisCell.endsWith("R3")) {
						System.out.println("Retest found in " + thisCell);
						thisCell = thisCell.substring(0, thisCell.length() - 2);
						thisTestNumber = 4;
						//System.out.println("Cell line: " + thisCell + " test number: " + thisTestNumber);
					} else {
						thisTestNumber = 1;
					}
					
					//stage
					thisStage = innerStrings[2];
					
					//groupRole translated
					tempString = splitLine[i];
					i++;
					//System.out.println("parsing groupRole: " + tempString);
					if (tempString.equals("A")) {
						thisGroupRole = "CONTROL";
					} else if (tempString.equals("B")) {
						thisGroupRole = "TREATED";
					} else {
						throw new Exception("Unrecognized groupRole.");
					}
					//day
					thisDay = splitLine[i];
					
					/*
					if (Integer.valueOf(thisDay).intValue() < 0) {
					break;
					}
					*/
					
					i++;
					//System.out.println("parsing day: " + thisDay);
					
					//mouses 1 - 10
					//handle calculation of logTumorVolume at this point
					//for tumor-volume of 0 and values < 0.001 (likely to actually be 0) log is set at -6
					
					for (int m = 1; m < 11; m++) {
						tempString = splitLine[i];
						i++;
						//System.out.println("parsing mouse: " + m + " from " + tempString);
						if (tempString.equals(".")) {
							outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + "," + thisDay + "," + m + "," + "-3,null\n");
						} else if (Double.valueOf(tempString) < 0) {
							outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + "," + thisDay + "," + m + "," + tempString + ",null\n");
						} else if (Double.valueOf(tempString) == 0) {
							outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + "," + thisDay + "," + m + "," + tempString + ",-6\n");
						} else if (Double.valueOf(tempString) > 0 && Double.valueOf(tempString) < 0.001) {
							outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + "," + thisDay + "," + m + "," + tempString + ",-6\n");
						} else {
							outputBuffer.append(thisCompound + "," + thisCell + "," + thisTestNumber + "," + thisStage + "," + thisGroupRole + "," + thisDay + "," + m + "," + tempString + "," + java.lang.Math.log10(Double.valueOf(tempString)) + "\n");
						}
					}
				}
			}
			//while
			
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

