package ca.ucalgary.seng300.a3.configuration;


import java.io.IOException;

import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.enums.OutputMethod;


public class ConfigurationAlpha {
private static VendingManager mgr;
	

	private static ConfigurationAlpha configurationAlpha;


//	private static LoggingModule loggingModule;
	
	
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(VendingManager host){
		configurationAlpha = new ConfigurationAlpha(host);
		
		
	}
	
	/**
	 * Assigns mgr.java a (the) manager.
	 * 
	 * @param host	The VendingManager to call upon for hardware interactions.
	 */
	private  ConfigurationAlpha(VendingManager host){		
		
		
		mgr = host;
		
		
//		LoggingModule.initialize();
		//
	//	loggingModule = LoggingModule.getInstance();
		
		
	}
	
	
	public static ConfigurationAlpha getInstance(){
		return configurationAlpha;
	}
	
	public void setStartingState() {
		
		setStartingOutputDistribution();
		setStartingMessages();
		
		
		
	}
	public void setStartingMessages() {
		
		mgr.addMessage("Hi there!", OutputDataType.WELCOME_MESSAGE_TEXT  ,5000);
		mgr.addMessage("", OutputDataType.WELCOME_MESSAGE_TEXT  ,10000);
		
		
	}
	public void setStartingOutputDistribution() {
		mgr.setOutputMap(OutputDataType.CREDIT_INFO, OutputMethod.DISPLAY, true);
		
		mgr.setOutputMap(OutputDataType.WELCOME_MESSAGE_TEXT, OutputMethod.LOOPING_MESSAGE, true);
		
		mgr.setOutputMap(OutputDataType.BUTTON_PRESSED, OutputMethod.TEXT_LOG, true);
		mgr.setOutputMap(OutputDataType.EXCEPTION_HANDLING, OutputMethod.TEXT_LOG, true);
		mgr.setOutputMap(OutputDataType.VALID_COIN_INSERTED, OutputMethod.TEXT_LOG, true);
		mgr.setOutputMap(OutputDataType.COIN_REFUNDED, OutputMethod.TEXT_LOG, true);
		mgr.setOutputMap(OutputDataType.CREDIT_INFO, OutputMethod.TEXT_LOG, true);
		
		mgr.setOutputMap(OutputDataType.CONFIG_PANEL_MESSAGE, OutputMethod.CONFIG_PANEL_DISPLAY, true);
		
	}
	
	public void addMessage(String str, OutputDataType dataType, int time) {

		mgr.addMessage( str, dataType, time);

		
		
	}
	
	
}
