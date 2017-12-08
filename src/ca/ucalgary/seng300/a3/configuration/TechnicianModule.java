package ca.ucalgary.seng300.a3.configuration;

/**
 * Software Engineering 300 - Group Assignment 3
 * ConfigurationModule.java
 * 
 * This class is used to configure the hardware tech panel. 
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

import ca.ucalgary.seng300.a3.enums.OutputDataType;

public class TechnicianModule{
	//
	private static TechnicianModule technicianModule;
	private static ConfigurationModule configurationModule;
	
	private static char [] numericValue = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static String enteredKey = "";
	private static boolean priceChangeMode = false;
	private static int slotNumber;
	private static int newPrice;

	/**
	 * Private constructor to prevent additional creations. (Singleton)
	 */
	private TechnicianModule() {}
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param mgr	The VendingManager assigning itself this class.
	 */
	public static void initialize(ConfigurationModule mgr) {
		if (mgr != null) {
			configurationModule = mgr;
			technicianModule = new TechnicianModule();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static TechnicianModule getInstance() {
		return technicianModule;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getMode()
	{
		return priceChangeMode;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfConfigButtons() {
		return numericValue.length;
	}
	
	/**Gets the index of the button that was pushed and adds a char value from buttonValue[index] to enteredKey
	 * Depending on the mode, the panel displays to the user what mode, slot choosing mode or price change mode, they are in.
	 * Also shows what values they entered.
	 * 
	 * @param index		The index of the button pushed
	 */
	public void enterChar(int index) {
		enteredKey += numericValue[index];
		if(!priceChangeMode) {
			configurationModule.addMessage("Pop Slot: " + enteredKey, OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		}else {
			configurationModule.addMessage("New Price: " + enteredKey, OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		}
	}
	
	/**When enter is pressed, if Configuration Panel is still in slot selection mode, gets the slot number from enteredKey and goes to price change mode.
	 * If in price change mode, gets the new price from enteredKey and changes the price of the previously entered pop slot.
	 * Unless, enteredKey is just 0, enteredKey always filters out it's leftmost 0's.
	 * If user entered an invalid entry, reset the panel state and start at slot selection mode.
	 */
	public void pressedEnter() throws InterruptedException {
		if(priceChangeMode) {
			try {
				enteredKey.replaceFirst("^0+(?!$)", "");
				newPrice = Integer.parseInt(enteredKey);
				enteredKey = "";
				configurationModule.changePopPrice(slotNumber, newPrice);
				newPrice = 0;
				priceChangeMode = false;
				configurationModule.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
			}catch (NumberFormatException e) {
				configurationModule.addMessage("Invalid entry. Returning to pop slot selection", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				Thread.sleep(5000);
				configurationModule.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				newPrice = 0;
				priceChangeMode = false;
				enteredKey = "";
			}
		}else {
			try {
				enteredKey.replaceFirst("^0+(?!$)", "");
				slotNumber = Integer.parseInt(enteredKey);
				if (configurationModule.checkPopRackExist(slotNumber)) {
					priceChangeMode = true;
					configurationModule.addMessage("New Price: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				}else {
					configurationModule.addMessage("No such slot exists. Returning to pop slot selection", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
					Thread.sleep(5000);
					configurationModule.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				}
				enteredKey = "";
			}catch (NumberFormatException e) {	
				
				Thread.sleep(5000);
				configurationModule.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				enteredKey = "";
			}
		}
	}
	
	/**
	 * When the vending machine is unlocked, this is called to create the first display menu.
	 */
	public void startConfigPanel(){
		configurationModule.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		}
	
	/**When the vending machine is locked, it resets all the variables to empty and
	 * resets to selection menu back to pop select.
	 */
	public void clearConfigPanel(){
		enteredKey = "";
		priceChangeMode = false;
		configurationModule.addMessage("", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		slotNumber = -1;
		newPrice = 0;
		}
	
	/**
	 * 
	 */
	public void updateExactChangeLight() {
		configurationModule.updateExactChangeLightState();
	}
	
	
}