package ca.ucalgary.seng300.a3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lsmr.vending.hardware.*;

/**
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
 * @author Raymond Tran (30028473)
 * @author Thomas Coderre (10169277)
 * @author Thobthai Chulpongsatorn (30005238)
 * 
 */
public class VendingManager {
	private static VendingManager mgr;
	private static VendingListener listener;
	private static ChangeModule changeModule;
	private static LoggingModule loggingModule;
	private static VendingMachine vm;
	private static DisplayModule displayModule;
	private static TransactionModule transactionModule;
	//added by XM
	private static ConfigurationModule configurationModule;
	private static List <String> newPopList = new ArrayList<String>();
	private static List <Integer> newPriceList = new ArrayList<Integer>();
	//
	private static Thread noCreditThread2;
	private int credit = 0;
	
	/**
	 * Singleton constructor. Initializes and stores the singleton instance
	 * of VendingListener.
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		ChangeModule.initialize(this);
		DisplayModule.initialize(this);
		TransactionModule.initialize(this);
		ConfigurationModule.initialize(this);
    LoggingModule.initialize(this);
    
		
		listener = VendingListener.getInstance();
		changeModule = ChangeModule.getInstance();
		displayModule = DisplayModule.getInstance();
		transactionModule = TransactionModule.getInstance();
    configurationModule = ConfigurationModule.getInstance();
		loggingModule = LoggingModule.getInstance();
	}
	
	/**
	 * Replaces the existing singleton instances (if any) for the entire 
	 * the Vending logic package. Registers the VendingListener(s) with the
	 * appropriate hardware.
	 * @param host The VendingMachine which the VendingManager is intended to manage.
	 */
	public static void initialize(VendingMachine host){
		mgr = new VendingManager(); 
		vm = host;

		mgr.registerListeners();
		noCreditThread2 = new Thread(DisplayModule.getInstance());

		displayModule.addLoopMessage("Hi there!",5000) ;
		displayModule.addLoopMessage("",10000) ;
		
		noCreditThread2.start();
		mgr.setModule();
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
		//added by XM
		getConfigPanel().getDisplay().register(listener);
		registerConfigButtonListener(listener);
		//end
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
	
	//added by XM
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
	//end
	
	/**
	 * Produces an array of valid coin denominations accepted by the machine.
	 * @return	An array of valid coin denomination.
	 */
	public int[] getValidCoinsArray() {
		int i = vm.getNumberOfCoinRacks();
		int[] inValidCoins = new int[i];
		for (int x = 0; x < i; x++) {
			inValidCoins[x] = vm.getCoinKindForCoinRack(x);
		}
		return inValidCoins;
	}
	
	/**
	 * Produces an array representing the number of each coin available in the machine corresponding to its valid denominations.
	 * @return	An array representing the number of each particular coin accepted by the machine.
	 */
	public int[] getCoinCount() {
		int j = vm.getNumberOfCoinRacks();
		int[] inCoinCount = new int[j];
		for (int x = 0; x < j; x++) {
			CoinRack tempRack = vm.getCoinRack(x);
			inCoinCount[x] = tempRack.size();
		}
		return inCoinCount;
		
	}
	
	/**
	 * Produces an array representing each pop price in the machine.
	 * @return	An array of each pop price.
	 */
	public int[] getPopPrices() {
		int k = vm.getNumberOfPopCanRacks();
		int[] inPopPrices = new int[k];
		for (int x = 0; x < k; x++) {
			inPopPrices[x] = vm.getPopKindCost(x);
		}
		return inPopPrices;
	}
	private void setModule() {

		ChangeModule.setCoins(getValidCoinsArray(), getCoinCount());
		ChangeModule.setPopPrices(getPopPrices());
		
	}

	// General Purpose Accessor Methods
	public Thread getLoopingThread2(){
		return (noCreditThread2);
	}
	void enableSafety(){
		vm.enableSafety();
	}
	void disableSafety(){
		vm.disableSafety();
	}
	boolean isSafetyEnabled(){
		return vm.isSafetyEnabled();
	}
	IndicatorLight getExactChangeLight(){
		return vm.getExactChangeLight();
	}
	IndicatorLight getOutOfOrderLight(){
		return vm.getOutOfOrderLight();
	}
	int getNumberOfSelectionButtons(){
		return vm.getNumberOfSelectionButtons();
	}
	PushButton getSelectionButton(int index){
		return vm.getSelectionButton(index);
	}
	CoinSlot getCoinSlot(){
		return vm.getCoinSlot(); 
	}
	CoinReceptacle getCoinReceptacle(){
		return vm.getCoinReceptacle(); 
	}
	
	DeliveryChute getDeliveryChute(){
		return vm.getDeliveryChute(); 
	}
	int getNumberOfCoinRacks(){
		return vm.getNumberOfCoinRacks();
	}
	CoinRack getCoinRack(int index){
		return vm.getCoinRack(index); 
	}
	CoinRack getCoinRackForCoinKind(int value){
		return vm.getCoinRackForCoinKind(value); 
	}
	Integer getCoinKindForCoinRack(int index){
		return vm.getCoinKindForCoinRack(index); 
	}
	int getNumberOfPopCanRacks(){
		return vm.getNumberOfPopCanRacks(); 
	}
	String getPopKindName(int index){
		return vm.getPopKindName(index); 
	}
	int getPopKindCost(int index){
		return vm.getPopKindCost(index); 
	}
	PopCanRack getPopCanRack(int index){
		return vm.getPopCanRack(index); 
	}
	int getPopCanCount(int index){
		return vm.getPopCanRack(index).size(); 
	}
	void dispensePopCanRack(int index) throws DisabledException, EmptyException, CapacityExceededException {
		getPopCanRack(index).dispensePopCan();
	
	}
	void storeCoinsInStorage() throws CapacityExceededException, DisabledException {
		getCoinReceptacle().storeCoins(); 
		
	}
	Display getDisplay(){
		return vm.getDisplay();
	}
	void ReduceCredit(int cost) {
		
		credit -=cost;
	}
	CoinReturn getCoinReturn() {
		return vm.getCoinReturn();
	}
	
	//added by XM
	ConfigurationPanel getConfigPanel() {
		return vm.getConfigurationPanel();
	}
	//end

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
	 * Gets the credit available for purchases, in cents. 
	 * Public access for testing and external access. 
	 * It is assumed to not be a security vulnerability.
	 * @return The stored credit, in cents.
	 */
	public int getCredit(){
		return credit;
	}
	
	/**
	 * Sets the credit of the current machine.
	 * @param temp	The new credit value to be represented.
	 */
	public void setCredit(int temp){
		credit = temp;
	}
	
	/**
	 * Displays a string message.
	 * @param str	Message to be displayed.
	 */
	public void displayMessage(String str){
		vm.getDisplay().display(str);
		
	}
	
	/**
	 * Adds a message to be displayed.
	 * @param str
	 */
	public void addMessage(String str) {
		displayModule.addMessage(str);
	}

    /**
     * Adds value to the tracked credit.
     * @param added The credit to add, in cents.
     */
	void resetDisplay() {
        noCreditThread2 = new Thread(DisplayModule.getInstance());
        noCreditThread2.start();     //Starts the looping display message when vm is turned on (created)
	}
	
	//New code by Christopher (not really testable until ConfigurationModule is finished)
	//Will implement after ConfigurationModule gets merged
	public void activateCofigPanel() {
		//configPanel.restartConfigDisplay();
	}

	//Will implement after ConfigurationModule gets merged
	public void deactivateCofigPanel() {
		//configPanel.clearConfigData()
	}
	
	//To be put in ConfigurationModule after it is merged
	public void clearConfigPanel(){
//		enteredKey = "";
//		priceChangeMode = false;
//		vm.displayMessageConfig("");
	}
	
	//To be put in ConfigurationModule after it is merged
	public void restartConfigDisplay(){
//		vm.displayMessageConfig("Pop Slot: ");
	}
	//End of new code (Chris)
  
	//added by XM
	/**
	 * Displays message in Configuration Panel's display
	 * 
	 * @param str	String displayed
	 */
	public void displayMessageConfig(String str){
		getConfigPanel().getDisplay().display(str);
		
	}
	
	//End of new code (XM)
	
//vvv=======================VENDING LOGIC START=======================vvv	

	/**
	 * Adds credit to the current amount in the machine.
	 * @param added	Credit to be added.
	 */
    public void addCredit(int added){
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
	 */
	public void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException {
		transactionModule.buy(popIndex);
	}
	
	/**
	 * Add a message to be logged.
	 * @param msg	Message to be logged.
	 */
	public void addLog(String msg) {
		try {
			loggingModule.logMessage(msg);
		}catch(IOException e){
			mgr.setOutOfOrder();
		}
	}
	
	/**
	 * wrapper method for change module so other modules can interact with it
	 */
	public ArrayList<Integer> getCoinsToReturn(int remaining) {
		return changeModule.getCoinsToReturn(remaining, getValidCoinsArray(), getCoinCount());
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
		changeModule.updateExactChangeLight();
	}
	
	/**
	 * Activates the Out Of Order light when necessary.
	 */
	public void setOutOfOrder() {
		getOutOfOrderLight().activate();
	}
	
	//added by XM
	
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
		try {
			configurationModule.pressedEnter();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if pop rack exists
	 * 
	 * @param index		index of the pop rack in the vending machine
	 * @return			boolean value of whether pop rack exists
	 */
	public boolean checkPopRackExist(int index) {
		try {
			String a = vm.getPopKindName(index);
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
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
			if(i == index) {
				newPopList.add(vm.getPopKindName(index));
				newPriceList.add(newPrice);
			}else {
				newPopList.add(vm.getPopKindName(index));
				newPriceList.add(vm.getPopKindCost(index));
			}
		}
		
		vm.configure(newPopList, newPriceList);
		newPopList.clear();
		newPriceList.clear();
	}
	//end
}
