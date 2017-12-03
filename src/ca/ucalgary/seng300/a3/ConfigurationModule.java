package ca.ucalgary.seng300.a3;

public class ConfigurationModule{
	
	private static ConfigurationModule cm;
	private VendingManager vm;
	private static char [] buttonValue = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static String enteredKey = "";
	private static boolean priceChangeMode = false;
	private static int slotNumber;
	private static int newPrice;

	public static void initialize(VendingManager vm) {
		cm = new ConfigurationModule(vm);
	}
	
	public ConfigurationModule(VendingManager vm) {
		this.vm = vm;
	}
	
	public static ConfigurationModule getInstance() {
		return cm;
	}
	
	public int getNumberOfConfigButtons() {
		return buttonValue.length;
	}
	
	/*Gets the index of the button that was pushed and adds a char value from buttonValue[index] to enteredKey
	 * Depending on the mode, the panel displays to the user what mode, slot choosing mode or price change mode, they are in.
	 * Also shows what values they entered.
	 * 
	 * @param index		The index of the button pushed
	 */
	public void enterChar(int index) {
		enteredKey += buttonValue[index];
		if(!priceChangeMode) {
			vm.displayMessageConfig("Pop Slot: " + enteredKey);
		}else {
			vm.displayMessageConfig("New Price: " + enteredKey);
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
				vm.changePopPrice(slotNumber, newPrice);
				newPrice = 0;
				priceChangeMode = false;
				vm.displayMessageConfig("Pop Slot: ");
			}catch (NumberFormatException e) {
				vm.displayMessageConfig("Invalid entry. Returning to pop slot selection");
				Thread.sleep(5000);
				vm.displayMessageConfig("Pop Slot: ");
				newPrice = 0;
				priceChangeMode = false;
				enteredKey = "";
			}
		}else {
			try {
				enteredKey.replaceFirst("^0+(?!$)", "");
				slotNumber = Integer.parseInt(enteredKey);
				if (vm.checkPopRackExist(slotNumber)) {
					priceChangeMode = true;
					vm.displayMessageConfig("New Price: ");
				}else {
					vm.displayMessageConfig("No such slot exists. Returning to pop slot selection");
					Thread.sleep(5000);
					vm.displayMessageConfig("Pop Slot: ");
				}
				enteredKey = "";
			}catch (NumberFormatException e) {
				vm.displayMessageConfig("Invalid entry. Returning to pop slot selection");
				Thread.sleep(5000);
				vm.displayMessageConfig("Pop Slot: ");
				enteredKey = "";
			}
		}
	}
	
}
