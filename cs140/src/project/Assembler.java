package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * This code was taken from Piazza as the working Assembler.
 * I assume that since it was provided we are free to use it, and figured that I would rather
 * write my error-handling into an Assembler that I knew worked rather than my own, which although scored 10/10 could still have holes.
 * My own assembler was handed in as a part of Assignment 9, so I am assuming that it is not an issue to use this code to ensure the rest of my program functions
 * I do not take credit for writing the code in this file, aside from the error handling parts defined in the spec
 * 
 * Also, the main method and following helper method were provided to me by Daniel Hintz, which he posted on Piazza and showed me in class
 * as a way to test the instruction files without booting up the GUI every single time
 * I am assuming that since the code he gave me for the main method does quite literally nothing but save me a few minutes for each test
 * that it is a non-issue for me to use it, provided giving him proper credit.
 */

public class Assembler {

	public static String assemble(File input, File output) {
		String returnVal = "success";
		ArrayList<String> code = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();
		ArrayList<String> inText = new ArrayList<>();
		ArrayList<String> outText = new ArrayList<>();
		boolean incode = true;
		try (Scanner inp = new Scanner(input)) {
			while (inp.hasNextLine()) {
				String line = inp.nextLine();
				inText.add(line);
			}
		} catch (FileNotFoundException e) {
			returnVal = "Error: the source file " + input + " is missing";
		}

		// TEST FIRST ERROR
		int lineNum = inText.size();
		boolean failed = false; // failure flag for tests
		for (int i = 0; i < lineNum && !failed; i++) {
			String line = inText.get(i);
			if (line.trim().length() == 0) {
				lineNum = i + 1;
				returnVal = "Error: line " + (lineNum) + " is a blank line";
				failed = true; // will break out of loop and check for other
								// errors prior to this
			}
		}

		boolean failed_2 = false; // another failure flag;
		for (int i = 0; i < lineNum && !failed_2; i++) { // stops iterating when
															// it finds an error
															// 'failed_2' will
															// be true
			String line = inText.get(i);
			if (line.length() > 0) { // Checks for an empty
										// string
				char c = line.charAt(0);
				if (c == ' ' || c == '\t') {
					lineNum = i + 1;
					returnVal = "Error: line " + (i + 1) + " starts with white space";
					
					failed_2 = true; // will break out of loop and check for
										// other errors prior to this
				}
			}
		}

		// TEST THIRD ERROR
		boolean failed_3 = false; // yet another failure flag for breaking out
									// of the loop
		for (int i = 0; i < lineNum && !failed_3; i++) {
			String line = inText.get(i);
			if (line.trim().toUpperCase().equals("DATA")) {
				if (!(line.trim().equals("DATA"))) {
					lineNum = i + 1;
					returnVal = "Error: line " + (i + 1) + " does not have DATA in upper case";
					
					failed_3 = true; // will break out of loop and check for
										// other errors prior to this
				}
			}
		}

		// COPY inText to code/data
		// Up to failure line
		// Adds to code up until it reaches "DATA", then takes us out of code
		// and writes the rest of the text do data
		for (int i = 0; i < lineNum; i++) {
			String line = inText.get(i).trim();
			if (incode && line.trim().toUpperCase().equals("DATA")) {
				incode = false; // Takes us out of code and ready to place
								// remaining data into 'data' as expected
								// output
			} else if (incode) {
				code.add(line.trim());
			} else {
				data.add(line.trim());
			}
		}

		// Instruction Error handling
		
		for(int i = 0; i < lineNum - 1 && i < code.size(); i++) {
			
			String line = code.get(i);
			String[] parts = line.trim().split("\\s+");
			if (InstructionMap.sourceCodes.contains(parts[0].toUpperCase())
					&& !InstructionMap.sourceCodes.contains(parts[0])) {
				
				lineNum = i + 1;
				returnVal = "Error: line " + (lineNum) + " does not have the instruction mnemonic in upper case";
				

			} else if (InstructionMap.noArgument.contains(parts[0])) {
				if (parts.length != 1) {
					lineNum = i + 1;
					returnVal = "Error: line " + lineNum + " has an illegal argument, length != 1";
					
				} else {
					int op = InstructionMap.opcode.get(parts[0]);
					outText.add(Integer.toHexString(op).toUpperCase());
				}
			} else if (!InstructionMap.noArgument.contains(parts[0])) {
				if (parts.length <= 1) {
					lineNum = i + 1;
					returnVal = "Error: line " + lineNum + " is missing an argument, parts.length is 1";
				}
				if (parts.length >= 3) {
					lineNum = i + 1;
					returnVal = "Error: line " + lineNum + " has more than one argument";
					
				}
				if (parts.length == 2) {
					if (parts[1].startsWith("#")) {
						if (!InstructionMap.immediateOK.contains((parts[0]))) {
							lineNum = i + 1;
							returnVal = "Error: line " + lineNum + " starts with an incorrect instruction mnemonic";
						} else {
							// Copied from lower down in the code, has been
							// commented out
							parts[0] = parts[0] + "I";
							parts[1] = parts[1].substring(1, parts[1].length()); // Removes #
							if (parts[0].equals("JUMPI")){
								parts[0] = "JMPI";
							}
								
							if (parts[0].equals("JMPZI")){
								parts[0] = "JMZI";
							}
								
							// From Piazza
							try {
								Integer.parseInt(parts[1], 16); // <<<<<
																// CORRECTION
							} catch (NumberFormatException e) {
								lineNum = i + 1;
								returnVal = "Error: line " + lineNum + " does not have a numberic argument";

							}
						}
					}
					// CHECKS START WITH &
					if (parts[1].startsWith("&")) {
						if (!InstructionMap.indirectOK.contains(parts[0])) {
							lineNum = i + 1;
							returnVal = "Error: line " + lineNum + " starts with &";
						} else {
							// Also copied from lower existing code
							parts[0] = parts[0] + "N";
							parts[1] = parts[1].substring(1, parts[1].length()); // Remove
																					// '&'
							if (parts[0].equals("JUMPN")){
								parts[0] = "JMPN";
							}
							try {
								Integer.parseInt(parts[1], 16); // <<<<<
																// CORRECTION
							} catch (NumberFormatException e) {
								lineNum = i + 1;
								returnVal = "Error: line " + lineNum + " starts with an incorrect instruction mnemonic xD xD";
							}
						}
					}
					try {
						outText.add(Integer.toHexString(InstructionMap.opcode.get(parts[0])).toUpperCase() + " "
								+ parts[1]);
					} catch (NullPointerException e) {
						lineNum = i + 1;
						returnVal = "Error: line " + lineNum + " has an invalid argument";
					}

				}
			}
		}

		/*for (int i = 0; i < lineNum - 1 && i < code.size(); i++) {
			String line = code.get(i);
			String[] parts = line.trim().split("\\s+");
			if (InstructionMap.sourceCodes.contains(parts[0].toUpperCase())
					&& !InstructionMap.sourceCodes.contains(parts[0])) {

				returnVal = "Error: line " + (i + 1) + " does not have the instruction mnemonic in upper case";
				lineNum = i + 1;

			} else if (InstructionMap.noArgument.contains(parts[0])) {
				if (parts.length != 1) {

					returnVal = "Error: line " + (i + 1) + " has an illegal argument, length != 1";
					lineNum = i + 1;
				} else {
					int op = InstructionMap.opcode.get(parts[0]);
					outText.add(Integer.toHexString(op).toUpperCase());
				}

			} else if (!InstructionMap.noArgument.contains(parts[0])) {
				if (parts.length <= 1) {
					lineNum = i + 1;
					returnVal = "Error: line " + lineNum + " is missing an argument, parts.length is 1";
					if (parts.length >= 3) {
						lineNum = i + 1;
						returnVal = "Error: line " + lineNum + " has more than one argument, parts.length is 3 or more";
					}
					if (parts.length == 2) {
						// CHECKS START WITH #
						if (parts[1].startsWith("#")) {
							if (!InstructionMap.immediateOK.contains((parts[0]))) {
								lineNum = i + 1;
								returnVal = "Error: line " + lineNum + " starts with #";
							} else {
								// Copied from lower down in the code, has been
								// commented out
								parts[0] = parts[0] + "I";
								parts[1] = parts[1].substring(0, parts[1].length()); // Removes
																						// the
																						// #
								if (parts[0].equals("JUMPI"))
									parts[0] = "JMPI";
								if (parts[0].equals("JMPZI"))
									parts[0] = "JMZI";
								// From Piazza
								try {
									Integer.parseInt(parts[1], 16); // <<<<<
																	// CORRECTION
								} catch (NumberFormatException e) {
									lineNum = i + 1;
									returnVal = "Error: line " + lineNum + " does not have a numberic argument";

								}
							}
						}
						// CHECKS START WITH &
						if (parts[1].startsWith("&")) {
							if (!InstructionMap.indirectOK.contains(parts[0])) {
								lineNum = i + 1;
								returnVal = "Error: line " + lineNum + " starts with &";
							} else {
								// Also copied from lower existing code
								parts[0] = parts[0] + "N";
								parts[1] = parts[1].substring(0, parts[1].length()); // Remove
																						// '&'
								if (parts[0].equals("JUMPN"))
									parts[0] = "JMPN";
								try {
									Integer.parseInt(parts[1], 16); // <<<<<
																	// CORRECTION
								} catch (NumberFormatException e) {
									lineNum = i + 1;
									returnVal = "Error: line " + lineNum + " does not have a numberic argument";
								}
							}
						}
						try {
							outText.add(Integer.toHexString(InstructionMap.opcode.get(parts[0])).toUpperCase() + " "
									+ parts[1]);
						} catch (NullPointerException e) {
							lineNum = i + 1;
							returnVal = "Error: line " + (i + 1) + " has an invalid argument";
						}

					}
				}
			}
		}*/

		int dat = data.size();
		int cod = code.size();
		for (int i = 0; i < dat && i < lineNum - cod; i++) {
			String line = data.get(i);
			String[] parts = line.trim().split("\\s+");
			if (parts.length != 2) {
				lineNum = i + cod;
				lineNum += 1;
				returnVal = "Error: line " + lineNum + " has the wrong number of arguments, parts.length != 2";
			} else {

				// Parses first from 0
				// This was taken from piazza
				try {
					Integer.parseInt(parts[0], 16); // <<<<< CORRECTION
				} catch (NumberFormatException e) {
					lineNum = i + cod;
					lineNum += 2;
					returnVal = "Error: line " + lineNum + " does not have a valid numberic argument failed parseInt from [0], 16";
				}

				// After [0], parses from [1]
				try {
					Integer.parseInt(parts[1], 16); // <<<<< CORRECTION
				} catch (NumberFormatException e) {
					lineNum = i + cod;
					lineNum += 2;
					returnVal = "Error: line " + lineNum + " does not have a valid numberic argument, failed parseInt from [1], 16";
				}

				
				//This was all moved up further, still has the same functionality
				/*
				 * if (parts[1].startsWith("#")) { parts[0] = parts[0] + "I";
				 * parts[1] = parts[1].substring(1); if
				 * (parts[0].equals("JUMPI")) parts[0] = "JMPI"; if
				 * (parts[0].equals("JMPZI")) parts[0] = "JMZI"; } else if
				 * (parts[1].startsWith("&")) { parts[0] = parts[0] + "N";
				 * parts[1] = parts[1].substring(1); if
				 * (parts[0].equals("JUMPN")) parts[0] = "JMPN"; } int opcode =
				 * InstructionMap.opcode.get(parts[0]);
				 * outText.add(Integer.toHexString(opcode).toUpperCase() + " " +
				 * parts[1]);
				 */
			}
		}

		outText.add("-1");
		outText.addAll(data);

		if (returnVal.equals("success")) {
			try (PrintWriter outp = new PrintWriter(output)) {
				for (String str : outText) {
					outp.println(str);
				}
				outp.close();
			} catch (FileNotFoundException e) {
				returnVal = "Error: unable to open " + output;
			}

		}
		return returnVal;

	}

	// Main method and helper getLastLine provided by Daniel Hintz for purpose
	// of testing files without using the GUI every time.
	public static void main(String[] args) {
		int start = 3;
		int end = 26;
		for (int i = start; i <= end; i++) {
			String s = "tests/";
			if (i < 10) {
				s += "0";
			}
			s += i + "e";
			String l = getLastLine(s + ".pasm");
			System.out.println(s + " " + l + " --- " + assemble(new File(s + ".pasm"), new File(s + ".pexe")));
		}
	}

	private static String getLastLine(String string) {
		try {
			Scanner s = new Scanner(new FileInputStream(new File(string)));
			String last = "";
			while (s.hasNextLine()) {
				String line = s.nextLine();
				if (line.trim().length() > 0)
					last = line.trim();
			}
			s.close();
			return last;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}