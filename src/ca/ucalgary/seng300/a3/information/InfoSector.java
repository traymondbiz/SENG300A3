package ca.ucalgary.seng300.a3.information;

import java.io.IOException;

import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.enums.OutputMethod;


public class InfoSector {

	
private static InfoSector infoSector;
	
	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static VendingManager mgr;
	
	private static LoggingModule loggingModule;
	private static DisplayModule displayModule;
	
	private static Thread noCreditThread2;
	
	private boolean[][] outputMap = new boolean[OutputDataType.values().length][OutputMethod.values().length];
	
	int a;
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(VendingManager host){
		infoSector = new InfoSector(host);
		noCreditThread2.start();
		
	}
	
	/**
	 * Assigns InfoSector.java a (the) manager.
	 * 
	 * @param host	The VendingManager to call upon for hardware interactions.
	 */
	private InfoSector(VendingManager host){		
		
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
	public static InfoSector getInstance(){
		return infoSector;
	}
	
	public void resetOutputMap( ) {
		for( int i = 0; i < OutputDataType.values().length; i++)
			for( int j = 0; j < OutputMethod.values().length; j++)
				outputMap[i][j] = false;
		
		
	}
	
	public void setOutputMap(OutputDataType outputDataType, OutputMethod outputMethod, boolean value ) {
		int a1 = OutputDataType.values().length;
		int a2 = OutputMethod.values().length;
		
		outputMap[outputDataType.getValue()][outputMethod.getValue()] = value;
		
		
	}
	
	public void sendOutput(String Str, OutputDataType outputType, int time) throws IOException {
		
		OutputMethod temp;

		temp = OutputMethod.TEXT_LOG;
		if (outputMap[outputType.getValue()][temp.getValue()]){
			
			loggingModule.logMessage(Str);
			
		}
		
		temp = OutputMethod.DISPLAY;
		if (outputMap[outputType.getValue()][temp.getValue()]){
			
			displayModule.addMessage(Str);
		}
		
		temp = OutputMethod.LOOPING_MESSAGE;
		if (outputMap[outputType.getValue()][temp.getValue()]){
			displayModule.addLoopMessage(Str, time);
	
		}
	
	}
	public void displayMessage(String str) {
		
		mgr.displayMessage(str);
	}
	
	public void resetDisplay() {
		noCreditThread2 = new Thread(DisplayModule.getInstance());
        noCreditThread2.start();     //Starts the looping display message when vm is turned on (created)
	
	}
	public void interruptLoopingThread() {
		noCreditThread2.interrupt();
	}
}
