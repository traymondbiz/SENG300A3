package ca.ucalgary.seng300.a3.configuration;

import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.enums.OutputMethod;

public class ConfigurationModule{
	//
	private static ConfigurationModule cm;
	private static VendingManager vmgr;
	
	private static char [] numericValue = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static String enteredKey = "";
	private static boolean priceChangeMode = false;
	private static int slotNumber;
	private static int newPrice;

	/**
	 * Private constructor to prevent additional creations. (Singleton)
	 */
	private ConfigurationModule() {}
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param mgr	The VendingManager assigning itself this class.
	 */
	public static void initialize(VendingManager mgr) {
		if (mgr != null) {
			vmgr = mgr;
			cm = new ConfigurationModule();
		}
	}
	
	public static ConfigurationModule getInstance() {
		return cm;
	}
	
	public boolean getMode()
	{
		return priceChangeMode;
	}
	
	public int getNumberOfConfigButtons() {
		return numericValue.length;
	}
	
	/*Gets the index of the button that was pushed and adds a char value from buttonValue[index] to enteredKey
	 * Depending on the mode, the panel displays to the user what mode, slot choosing mode or price change mode, they are in.
	 * Also shows what values they entered.
	 * 
	 * @param index		The index of the button pushed
	 */
	public void enterChar(int index) {
		enteredKey += numericValue[index];
		if(!priceChangeMode) {
			vmgr.addMessage("Pop Slot: " + enteredKey, OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		}else {
			vmgr.addMessage("New Price: " + enteredKey, OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		}
	}
	
	/*When enter is pressed, if Configuration Panel is still in slot selection mode, gets the slot number from enteredKey and goes to price change mode.
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
				vmgr.changePopPrice(slotNumber, newPrice);
				newPrice = 0;
				priceChangeMode = false;
				//ca.addMessage("Pop Slot: ");
				vmgr.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
			}catch (NumberFormatException e) {
				//ca.addMessage("Invalid entry. Returning to pop slot selection");
				vmgr.addMessage("Invalid entry. Returning to pop slot selection", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				Thread.sleep(5000);
				//ca.addMessage("Pop Slot: ");
				vmgr.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				newPrice = 0;
				priceChangeMode = false;
				enteredKey = "";
			}
		}else {
			try {
				enteredKey.replaceFirst("^0+(?!$)", "");
				slotNumber = Integer.parseInt(enteredKey);
				if (vmgr.checkPopRackExist(slotNumber)) {
					priceChangeMode = true;
					vmgr.addMessage("New Price: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				}else {
					vmgr.addMessage("No such slot exists. Returning to pop slot selection", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
					Thread.sleep(5000);
					vmgr.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				}
				enteredKey = "";
			}catch (NumberFormatException e) {
			//	ca.addMessage("Invalid entry. Returning to pop slot selection");
				
				
				Thread.sleep(5000);
				vmgr.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
				enteredKey = "";
			}
		}
	}
	
	//New code by Christopher
	//When the vending machine is unlocked, this is called to create the first display menu.
	public void startConfigPanel(){
		vmgr.addMessage("Pop Slot: ", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		}
	
	/*When the vending machine is locked, it resets all the variables to empty and
	 * resets to selection menu back to pop select.
	 */
	public void clearConfigPanel(){
		enteredKey = "";
		priceChangeMode = false;
		vmgr.addMessage("", OutputDataType.CONFIG_PANEL_MESSAGE, 0);
		slotNumber = -1;
		newPrice = 0;
		}
		//End of new code (Chris)
	
	public void updateExactChangeLight() {
		vmgr.updateExactChangeLightState();
	}
	
	
}