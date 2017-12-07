package ca.ucalgary.seng300.a3.configuration;



import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.enums.OutputMethod;
import ca.ucalgary.seng300.a3.information.DisplayModule;


public class ConfigurationAlpha {
private static VendingManager mgr;
	

	private static ConfigurationAlpha configurationAlpha;

	
	private static ConfigurationModule configurationModule;
	
	
	
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
		
		
		ConfigurationModule.initialize(this);
		
		configurationModule = ConfigurationModule.getInstance();
		
		
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

	public void changePopPrice(int slotNumber, int newPrice) {
		mgr.changePopPrice(slotNumber, newPrice);
		
	}

	public boolean checkPopRackExist(int slotNumber) {


		return mgr.checkPopRackExist(slotNumber);
		
	}

	public void updateExactChangeLightState() {
		configurationModule.updateExactChangeLight();
		
	}

	public int getNumberOfConfigButtons() {


		return configurationModule.getNumberOfConfigButtons();
	}

	public boolean getMode() {


		return configurationModule.getMode();
	}

	public void startConfigPanel() {


		configurationModule.startConfigPanel();
	}

	public void clearConfigPanel() {


		configurationModule.clearConfigPanel();
	}

	public void enterChar(int index) {
		configurationModule.enterChar(index);
		
	}

	public void pressedEnter() {
		try {
			configurationModule.pressedEnter();
		} catch (InterruptedException e) {


			e.printStackTrace();
		}
		
	}
	
	
}
