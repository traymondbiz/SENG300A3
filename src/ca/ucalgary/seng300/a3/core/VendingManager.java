package ca.ucalgary.seng300.a3.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a3.configuration.ConfigurationModule;
import ca.ucalgary.seng300.a3.configuration.TechnicianModule;
import ca.ucalgary.seng300.a3.enums.DisplayType;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.enums.OutputMethod;
import ca.ucalgary.seng300.a3.exceptions.InsufficientFundsException;
import ca.ucalgary.seng300.a3.finance.TransactionModule;
import ca.ucalgary.seng300.a3.information.OutputModule;

/**
 * Software Engineering 300 - Group Assignment 3
 * VendingManager.java
 * 
 * 
 * VendingManager is the primary access-point for the logic controlling the
 * VendingMachine hardware. It is associated with VendingListener, which listens
 * for event notifications from the hardware classes.
 * 
 * USAGE: Pass VendingMachine to static method initialize(), then use getInstance()
 * to get the singleton VendingManager object. Listeners are registered automatically. 
 * 
 * DESIGN: All logic classes are designed as singletons. Currently, the only public-access methods are for initialization
 * and to get a VendingManager instance. All other functionality is restricted
 * to package access. 
 * 
 * TESTING: Due to the near-total encapsulation, VendingManager and VendingListener
 * must be tested along with a VendingMachine. Although a "stub" VendingMachine
 * *could* be created, doing so would be extremely inefficient. 
 * We have been instructed that the VendingMachine and other hardware classes
 * are known-good, so integration testing will be sufficient.
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
public class VendingManager {
	private static VendingManager mgr;
	private static VendingListener listener;
	private static OutputModule outputModule;
	private static ConfigurationModule configurationModule;
	private static VendingMachine vendingMachine;
	private static TransactionModule transactionModule;
	private static int credit;
	private static List <String> newPopList = new ArrayList<String>();
	private static List <Integer> newPriceList = new ArrayList<Integer>();
	
	/**
	 * Singleton constructor. Initializes and stores the singleton instance
	 * of VendingListener.
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		
		OutputModule.initialize(this);
		ConfigurationModule.initialize(this);
		
		outputModule = OutputModule.getInstance();
		configurationModule = ConfigurationModule.getInstance();
		listener = VendingListener.getInstance();

		TransactionModule.initialize(this);
		transactionModule = TransactionModule.getInstance();
		
	}
	
	/**
	 * Replaces the existing singleton instances (if any) for the entire 
	 * the Vending logic package. Registers the VendingListener(s) with the
	 * appropriate hardware.
	 * @param host The VendingMachine which the VendingManager is intended to manage.
	 * @throws IOException 
	 */
	public static void initialize(VendingMachine host) {
		mgr = new VendingManager(); 
		vendingMachine = host;
		mgr.registerListeners();	
		configurationModule.setStartingState();		
	}
	
	public void setOutputMap(OutputDataType outputDataType, OutputMethod outputMethod, boolean value ) {
		
		outputModule.setOutputMap(outputDataType, outputMethod, value);
	
	}
		
	/**
	 * Provides public access to the VendingManager singleton.
	 * @return The singleton VendingManager instance  
	 */
	public static VendingManager getInstance(){
		return mgr;
	}
	
	/**
	 * Registers the previously instantiated listener(s) with the 
	 * appropriate hardware.
	 */
	private void registerListeners(){
		getCoinSlot().register(listener);
		getDisplay().register(listener);
		registerButtonListener(listener);
		getConfigPanel().getDisplay().register(listener);
		registerConfigButtonListener(listener);
		getLock().register(listener);
	}
	
	
	/**
	 * Iterates through all selection buttons in the VendingMachine and
	 * registers a single listener with each.
	 * @param listener The listener that will handle SelectionButton events.
	 */
	private void registerButtonListener(PushButtonListener listener){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i< buttonCount; i++){
			getSelectionButton(i).register(listener);;
		}		
	}
	
	/**
	 * Register the buttons of the Configuration Panel.
	 * The amount of buttons registered depends on the array of characters in ConfigurationModule, buttonValue.
	 * 
	 * @param listener
	 */
	private void registerConfigButtonListener(PushButtonListener listener) {
		try {
			int configButtonCount = configurationModule.getNumberOfConfigButtons();
			for(int i = 0; i < configButtonCount; i++) {
				getConfigPanel().getButton(i).register(listener);
			}
			getConfigPanel().getEnterButton().register(listener);
		}catch (NullPointerException e) {
			System.out.println("buttonValue too big for 37 buttons. Limit the size to 37.");
		}
	}
	/**
	 * @return	Number of Configuration Panel buttons that are registered.
	 */
	public int getNumberOfConfigButtons() {
		return configurationModule.getNumberOfConfigButtons();
	}
	
	/**
	 * Produces an array of valid coin denominations accepted by the machine.
	 * @return	An array of valid coin denomination.
	 */
	public int[] getValidCoinsArray() {
		int i = vendingMachine.getNumberOfCoinRacks();
		int[] inValidCoins = new int[i];
		for (int x = 0; x < i; x++) {
			inValidCoins[x] = vendingMachine.getCoinKindForCoinRack(x);
		}
		return inValidCoins;
	}
	
	/**
	 * Produces an array representing the number of each coin available in the machine corresponding to its valid denominations.
	 * @return	An array representing the number of each particular coin accepted by the machine.
	 */
	public int[] getCoinCount() {
		int j = vendingMachine.getNumberOfCoinRacks();
		int[] inCoinCount = new int[j];
		for (int x = 0; x < j; x++) {
			CoinRack tempRack = vendingMachine.getCoinRack(x);
			inCoinCount[x] = tempRack.size();
		}
		return inCoinCount;
		
	}
	
	/**
	 * Produces an array representing each pop price in the machine.
	 * @return	An array of each pop price.
	 */
	public int[] getPopPrices() {
		int k = vendingMachine.getNumberOfPopCanRacks();
		int[] inPopPrices = new int[k];
		for (int x = 0; x < k; x++) {
			inPopPrices[x] = vendingMachine.getPopKindCost(x);
		}
		return inPopPrices;
	}
	

	// General Purpose Accessor Methods
	public int getCredit() {
		return credit;	
	}
	void enableSafety(){
		vendingMachine.enableSafety();
	}
	void disableSafety(){
		vendingMachine.disableSafety();
	}
	boolean isSafetyEnabled(){
		return vendingMachine.isSafetyEnabled();
	}
	IndicatorLight getExactChangeLight(){
		return vendingMachine.getExactChangeLight();
	}
	IndicatorLight getOutOfOrderLight(){
		return vendingMachine.getOutOfOrderLight();
	}
	int getNumberOfSelectionButtons(){
		return vendingMachine.getNumberOfSelectionButtons();
	}
	PushButton getSelectionButton(int index){
		return vendingMachine.getSelectionButton(index);
	}
	CoinSlot getCoinSlot(){
		return vendingMachine.getCoinSlot(); 
	}
	CoinReceptacle getCoinReceptacle(){
		return vendingMachine.getCoinReceptacle(); 
	}
	
	DeliveryChute getDeliveryChute(){
		return vendingMachine.getDeliveryChute(); 
	}
	int getNumberOfCoinRacks(){
		return vendingMachine.getNumberOfCoinRacks();
	}
	CoinRack getCoinRack(int index){
		return vendingMachine.getCoinRack(index); 
	}
	CoinRack getCoinRackForCoinKind(int value){
		return vendingMachine.getCoinRackForCoinKind(value); 
	}
	Integer getCoinKindForCoinRack(int index){
		return vendingMachine.getCoinKindForCoinRack(index); 
	}
	public int getNumberOfPopCanRacks(){
		return vendingMachine.getNumberOfPopCanRacks(); 
	}
	public String getPopKindName(int index){
		return vendingMachine.getPopKindName(index); 
	}
	public int getPopKindCost(int index){
		return vendingMachine.getPopKindCost(index); 
	}
	PopCanRack getPopCanRack(int index){
		return vendingMachine.getPopCanRack(index); 
	}
	public int getPopCanCount(int index){
		return vendingMachine.getPopCanRack(index).size(); 
	}
	public void dispensePopCanRack(int index) throws DisabledException, EmptyException, CapacityExceededException {
		getPopCanRack(index).dispensePopCan();
	
	}
	public void storeCoinsInStorage() throws CapacityExceededException, DisabledException {
		getCoinReceptacle().storeCoins(); 
		
	}
	Display getDisplay(){
		return vendingMachine.getDisplay();
	}
	
	CoinReturn getCoinReturn() {
		return vendingMachine.getCoinReturn();
	}
	
	Lock getLock() {
		return vendingMachine.getLock();
	}
	
	/**
	 * returns the mode of the configurationModule
	 */
	public boolean getConfigMode()
	{
		return configurationModule.getMode();
	}
	
	ConfigurationPanel getConfigPanel() {
		return vendingMachine.getConfigurationPanel();
	}

	/**
	 * Returns the index of the given SelectionButton,
	 * which implies the index of the associated PopRack.
	 * @param button The button of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getButtonIndex(PushButton button){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i< buttonCount; i++){
			if (getSelectionButton(i) == button){
				return i;
			}
		}	
		return -1;
	}
	
	
	/**
	 * Displays a string message.
	 * @param str	Message to be displayed.
	 */
	public void displayMessage(String str, boolean locked){
		// originally !locked
		if (locked)
			vendingMachine.getDisplay().display(str);
		else
			getConfigPanel().getDisplay().display(str);
	}
	
	public DisplayType getDisplayType( Display display) {
		
		if(display == vendingMachine.getDisplay()) return DisplayType.FRONT_DISPLAY;
		
		if(display == getConfigPanel().getDisplay()) return DisplayType.BACK_PANEL_DISPKAY;
		
		return DisplayType.UNKNOWN_DISPLAY;	
		
	}
	
	
	/**
	 * Adds a message to be displayed.
	 * @param str
	 * @throws IOException 
	 */
	public void addMessage(String str, OutputDataType dataType, int time) {
		try {
			outputModule.sendOutput( str, dataType, time);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

    /**
     * Adds value to the tracked credit.
     * @param added The credit to add, in cents.
     */
	public void resetDisplay() {
        outputModule.resetDisplay();
	}
	
	/**
	 * Restarts configurationModule's display
	 */
	public void activateCofigPanel() {
		configurationModule.startConfigPanel();
	}
	
	/**
	 * Clears configurationModule's variables
	 */
	public void deactivateConfigPanel() {
		configurationModule.clearConfigPanel();
	}

	/**
	 * Displays message in Configuration Panel's display
	 * 
	 * @param str	String displayed
	 */
	public void displayMessageConfig(String str){
		getConfigPanel().getDisplay().display(str);
		
	}
	
	
//vvv=======================VENDING LOGIC START=======================vvv	


	/**
	 * Adds credit to the current amount in the machine.
	 * @param added	Credit to be added.
	 * @throws IOException 
	 */
    public void addCredit(int added) throws IOException{
    	transactionModule.addCredit(added);
    }
    
	/**
	 * Handles a pop purchase. Checks if the pop rack has pop, confirms funds available,  
	 *  dispenses the pop, reduces available funds and deposits the added coins into storage. 
	 * @param popIndex The index of the selected pop rack. 	 
	 * @throws InsufficientFundsException Thrown if credit < cost.
	 * @throws EmptyException Thrown if the selected pop rack is empty.
	 * @throws DisabledException Thrown if the pop rack or delivery chute is disabled.
	 * @throws CapacityExceededException Thrown if the delivery chute is full.
	 * @throws IOException 
	 */
	public void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException, IOException {
		transactionModule.buy(popIndex);
	}
	
	/**
	 * Add a message to be logged.
	 * @param msg	Message to be logged.
	 * @throws IOException 
	 */
	public void addLog(String msg, OutputDataType dataType, int time) throws IOException {
		mgr.addMessage(msg, dataType  ,time);
		
	}
	
	/**
	 * Dispense the specified coin.
	 */
	public void dispenseCoin(int value) {
		try {
		CoinRack temp = getCoinRackForCoinKind(value);
		temp.releaseCoin();
		}
		catch(CapacityExceededException e) {
			mgr.setOutOfOrder(); //capacity exceed cannot recover 
		}
		catch(EmptyException e) {
			//can recover from here since it tried to release no coins
		}
		catch(DisabledException e) {
			mgr.setOutOfOrder();
		}
	}
	
	/**
	 * Updates the state of the Exact Change Light, in different fashions.
	 */
	public void activateExactChangeLight() {
		getExactChangeLight().activate();
	}
	public void deactivateExactChangeLight() {
		getExactChangeLight().deactivate();
	}
	public void updateExactChangeLightState() {
		transactionModule.updateExactChangeLight();
	}
	public void interruptDisplay(){
		outputModule.interruptLoopingThread();

	}
	/**
	 * Activates the Out Of Order light when necessary.
	 */
	public void setOutOfOrder() {
		getOutOfOrderLight().activate();
		//disable what the user can interact with
		mgr.getDeliveryChute().disable();
		mgr.getCoinSlot().disable();
		//set a message that cannot be changed
		mgr.displayMessage("OUT OF ORDER", true);
	}
	
	
	/**
	 * Send the index of the configuration panel button that was pushed.
	 * @param index		Index of the configuration panel button
	 */
	public void pressConfigButton (int index) {
		configurationModule.enterChar(index);
	}
	
	/**
	 * Configuration panel's enter button was pushed.
	 */
	public void pressedConfigEnterButton() {
		
		configurationModule.pressedEnter();
		
	}
	
	/**
	 * Check if pop rack exists
	 * 
	 * @param index		index of the pop rack in the vending machine
	 * @return			boolean value of whether pop rack exists
	 */
	public boolean checkPopRackExist(int index) {
		try {
			String a = vendingMachine.getPopKindName(index);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Changes price of a pop slot
	 * Creates a new list of pop names and pop prices. All pop that has no relation to the pop slot being changed will retain their original values
	 * The pop slot that corresponds to the index will have it's price changed.
	 * Clears the lists after configuring Vending Machine.
	 * 
	 * @param index		Pop slot to be changed
	 * @param newPrice	New price to be changed to
	 */
	public void changePopPrice(int index, int newPrice) {
		for (int i = 0; i < vendingMachine.getNumberOfSelectionButtons(); i++) {
			if(i == index) {
				newPopList.add(vendingMachine.getPopKindName(i));
				newPriceList.add(newPrice);
			}
			else {
				newPopList.add(vendingMachine.getPopKindName(i));
				newPriceList.add(vendingMachine.getPopKindCost(i));
			}
		}
		
		vendingMachine.configure(newPopList, newPriceList);
		newPopList.clear();
		newPriceList.clear();
	}

	public void setCredit(int newCredit) {
		credit = newCredit;
	}

}