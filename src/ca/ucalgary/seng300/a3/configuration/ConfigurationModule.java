package ca.ucalgary.seng300.a3.configuration;

/**
 * Software Engineering 300 - Group Assignment 3
 * ConfigurationAlpha.java
 * 
 * This class is the master of the configuration module.
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

import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.enums.OutputMethod;


public class ConfigurationModule {
private static VendingManager mgr;
	

	private static ConfigurationModule configurationModule;

	
	private static TechnicianModule technicianModule;
	
	
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(VendingManager host){
		configurationModule = new ConfigurationModule(host);
		
		
	}
	
	/**
	 * Assigns mgr.java a (the) manager.
	 * 
	 * @param host	The VendingManager to call upon for hardware interactions.
	 */
	private  ConfigurationModule(VendingManager host){		
		
		
		mgr = host;
		
		
		TechnicianModule.initialize(this);
		
		technicianModule = TechnicianModule.getInstance();
		
		
	}
	
	
	public static ConfigurationModule getInstance(){
		return configurationModule;
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

	public void changePopPrice(int slotNumber, int newPrice) {
		mgr.changePopPrice(slotNumber, newPrice);
		
	}

	public boolean checkPopRackExist(int slotNumber) {


		return mgr.checkPopRackExist(slotNumber);
		
	}

	public void updateExactChangeLightState() {
		technicianModule.updateExactChangeLight();
		
	}

	public int getNumberOfConfigButtons() {


		return technicianModule.getNumberOfConfigButtons();
	}

	public boolean getMode() {


		return technicianModule.getMode();
	}

	public void startConfigPanel() {


		technicianModule.startConfigPanel();
	}

	public void clearConfigPanel() {


		technicianModule.clearConfigPanel();
	}

	public void enterChar(int index) {
		technicianModule.enterChar(index);
		
	}

	public void pressedEnter() {
		try {
			technicianModule.pressedEnter();
		} catch (InterruptedException e) {


			e.printStackTrace();
		}
		
	}
	
	
}
