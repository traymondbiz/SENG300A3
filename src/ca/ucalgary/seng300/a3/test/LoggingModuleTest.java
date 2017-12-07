package ca.ucalgary.seng300.a3.test;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

import ca.ucalgary.seng300.a3.core.*;
import ca.ucalgary.seng300.a3.information.LoggingModule;
import ca.ucalgary.seng300.a3.*;

/**
 * Software Engineering 300 - Group Assignment 2
 * LoggingModuleTest.java
 * 
 * This class is used to test the functionality of the LoggingModule class.
 * 
 * 100% code coverage was achieved in LoggingModule.
 * 
 * Id Input/Output Technology and Solutions (Group 2)
 * @author Raymond Tran 			(30028473)
 * @author Hooman Khosravi 			(30044760)
 * @author Christopher Smith 		(10140988)
 * @author Mengxi Cheng 			(10151992)
 * @author Zachary Metz 			(30001506)
 * @author Abdul Basit 				(30033896)
 * 
 * @version	2.0
 * @since	2.0
 */
public class LoggingModuleTest {
	private LoggingModule logging;

	/**
	 * A method to initialize the LoggingModule class
	 * and retrieve a singleton LoggingModule instance.
	 */
	@Before
	public void setup(){
		LoggingModule.initialize();
		logging = LoggingModule.getInstance();
	}
	
	/* Reference material for reading from a file
	 * https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
	 */
	/**
	 * Ensure the the event log file is created with correct contents.
	 * 
	 * @throws IOException Thrown when the I/O exceptions are failed or interrupted.
	 */
	@Test
	public void testLog() throws IOException{
		logging.logMessage("this");
		
		// The name of the file to open.
		String fileName = "Log.txt";
		
		// This will reference one line at a time
		String line = null;
		
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);
			
			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String lastLine = "";
			
			while ((line = bufferedReader.readLine()) != null) { //get the last line in the file
				lastLine = line;
			}
			String[] string = lastLine.split(":");
			assertEquals(" this", string[3]);
			
			// Always. Close. Files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			assertTrue(false);
			System.out.println("Unable to open file '" + fileName + "'");	// For debugging purpose
		} catch (IOException ex) {
			assertTrue(false);
			System.out.println("Error reading file '" + fileName + "'");	// For debugging purpose
		}
	}
	
	/**
	 * Method to destroy the Logging Module instance after each test in order to not affect the following test.
	 */
	@After
	public void tearDown(){
		logging = null;
	}
}
