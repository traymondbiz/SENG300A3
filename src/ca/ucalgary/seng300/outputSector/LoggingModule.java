package ca.ucalgary.seng300.outputSector;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class LoggingModule {
	private static Writer output;

	/**
	 * Software Engineering 300 - Group Assignment 2
	 * LoggingModule.java
	 * 
	 * Records messages that are handed to it into a text file.
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
	
	
	/* Reference material
	 * https://howtodoinjava.com/core-java/io/how-to-create-a-new-file-in-java/
	 * https://stackoverflow.com/questions/11496700/how-to-use-printwriter-and-file-classes-in-java
	 * https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
	 */

	/**
	 * Self-referential variable. (Singleton)
	 */
	private static LoggingModule loggingModule;

	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static InfoSector mgr;
	
	/**
	 * Private constructor to prevent additional creations. (Singleton)
	 */
	private LoggingModule() {}
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(InfoSector manager) {
		if (manager != null) {
			mgr = manager;
			loggingModule = new LoggingModule();
		}
	}

	/**
	 * Provides access to the singleton instance for package-internal classes. (Singleton)
	 * @return The single instance of the LoggingModule.
	 */
	public static LoggingModule getInstance() {
		return loggingModule;
	}

	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize() {
			loggingModule = new LoggingModule();
	}

	/**
	 * Records a given message into the text file.
	 * @param msg			The message to be written.
	 * @throws IOException	If an unknown I/O error occurs.
	 */
	public void logMessage(String msg) throws IOException {
		loggingModule.printToFile(msg);
	}

	/**
	 * Creates a new file, or continues editing the currently existing one.
	 * 
	 * @param messageToLog	The message to be written. (Timestamped)
	 * @throws IOException	if an unknown I/O error occurs.
	 */
	private void printToFile(String messageToLog) throws IOException {
		Date currentDate = new Date();
		File currentFileDir = new File("event log.txt");
		
		if(!currentFileDir.isFile()) {
			currentFileDir.createNewFile(); 
		}
		
		output = new BufferedWriter(new FileWriter("event log.txt", true));
		output.append(currentDate.toString() + ":" + messageToLog + "\n");
		output.close();
	}

}