package ca.ucalgary.seng300.a3.information;

import java.io.IOException;

import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.enums.OutputMethod;

/**
 * Software Engineering 300 - Group Assignment 3
 * InfoSector.java
 * 
 * This class controls the flow of information going to the display hardware or the log file 
 * 
 * Id Input/Output Technology and Solutions (Group 2)
 * @author Raymond Tran 			(30028473)
 * @author Hooman Khosravi 			(30044760)
 * @author Christopher Smith 		(10140988)
 * @author Mengxi Cheng 			(10151992)
 * @author Zachary Metz 			(30001506)
 * @author Abdul Basit 				(30033896)
 * @author Elodie Boudes			(10171818)
 * @author Michael De Grood			(10134884)
 * @author Tae Chyung				(10139101)		
 * @author Xian-Meng Low			(10127970)			
 *   
 * @version	2.0
 * @since	2.0
 */

public class OutputModule {

	
private static OutputModule outputModule;
	
	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static VendingManager mgr;
	
	private static LoggingModule loggingModule;
	private static DisplayModule displayModule;
	
	private static Thread noCreditThread2;
	
	private boolean[][] outputMap = new boolean[OutputDataType.values().length][OutputMethod.values().length];

	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(VendingManager host){
		outputModule = new OutputModule(host);
		noCreditThread2.start();
		
	}
	
	/**
	 * Assigns InfoSector.java a (the) manager.
	 * 
	 * @param host	The VendingManager to call upon for hardware interactions.
	 */
	private OutputModule(VendingManager host){		
		
		resetOutputMap( );
			
		mgr = host;		
		
		DisplayModule.initialize(this);
		LoggingModule.initialize();
		
		displayModule =DisplayModule.getInstance();
		loggingModule = LoggingModule.getInstance();
		
		
		noCreditThread2 = new Thread(DisplayModule.getInstance());

	}
	
	public Thread getMessageThread() {
		
		return noCreditThread2;
	}
	
	/**
	 * Provides access to the singleton instance for package-internal classes. (Singleton)
	 * @return The single instance of InfoSector.
	 */
	public static OutputModule getInstance(){
		return outputModule;
	}
	
	/**
	 * 
	 */
	public void resetOutputMap( ) {
		for( int i = 0; i < OutputDataType.values().length; i++)
			for( int j = 0; j < OutputMethod.values().length; j++)
				outputMap[i][j] = false;
		
		
	}
	
	/**
	 * 
	 * @param outputDataType
	 * @param outputMethod
	 * @param value
	 */
	public void setOutputMap(OutputDataType outputDataType, OutputMethod outputMethod, boolean value ) {		
		outputMap[outputDataType.getValue()][outputMethod.getValue()] = value;
		
		
	}
	
	/**
	 * 
	 * @param Str
	 * @param outputType
	 * @param time
	 * @throws IOException
	 */
	public void sendOutput(String Str, OutputDataType outputType, int time) throws IOException {

		OutputMethod temp;

		
		temp = OutputMethod.CONFIG_PANEL_DISPLAY;
		if (outputMap[outputType.getValue()][temp.getValue()]){
			
			displayModule.addMessage(Str, false);
		}
		
		temp = OutputMethod.TEXT_LOG;
		if (outputMap[outputType.getValue()][temp.getValue()]){
			
			loggingModule.logMessage(Str);
			
		}
		
		temp = OutputMethod.DISPLAY;
		if (outputMap[outputType.getValue()][temp.getValue()]){
			
			displayModule.addMessage(Str, true);
		}
		
		temp = OutputMethod.LOOPING_MESSAGE;
		if (outputMap[outputType.getValue()][temp.getValue()]){
			displayModule.addLoopMessage(Str, time);
	
		}
	
	}
	
	/**
	 * 
	 * @param str
	 * @param locked
	 */
	public void displayMessage(String str, boolean locked) {
		
		mgr.displayMessage(str, locked);
	}
	
	/**
	 * 
	 */
	public void resetDisplay() {
		noCreditThread2 = new Thread(DisplayModule.getInstance());
        noCreditThread2.start();     //Starts the looping display message when vm is turned on (created)
	
	}
	
	/**
	 * 
	 */
	public void interruptLoopingThread() {
		noCreditThread2.interrupt();
	}
}
