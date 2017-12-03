package ca.ucalgary.seng300.a3;

import java.io.IOException;
import java.util.ArrayList;

import org.lsmr.vending.hardware.*;

import EnumTypes.OutputDataType;
import EnumTypes.OutputMethod;
import ca.ucalgary.seng300.a3.financeSector.FinanceSector;
import ca.ucalgary.seng300.outputSector.InfoSector;

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
	private static InfoSector infoSector;
	private static FinanceSector financeSector;
	private static VendingMachine vm;
	
	/**
	 * Singleton constructor. Initializes and stores the singleton instance
	 * of VendingListener.
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		
		InfoSector.initialize(this);
		FinanceSector.initialize(this);
		
		infoSector = InfoSector.getInstance();
		financeSector = FinanceSector.getInstance();
		listener = VendingListener.getInstance();
		
		
	}
	
	/**
	 * Replaces the existing singleton instances (if any) for the entire 
	 * the Vending logic package. Registers the VendingListener(s) with the
	 * appropriate hardware.
	 * @param host The VendingMachine which the VendingManager is intended to manage.
	 * @throws IOException 
	 */
	public static void initialize(VendingMachine host) throws IOException{
		mgr = new VendingManager(); 
		vm = host;
		mgr.registerListeners();
		
		mgr.setOutputmaps();
		financeSector.setModule();
		
		mgr.addMessage("Hi there!", OutputDataType.WELCOME_MESSAGE_TEXT  ,5000);
		mgr.addMessage("", OutputDataType.WELCOME_MESSAGE_TEXT  ,10000);
		
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
	}
	private void setOutputmaps() {
		
		infoSector.setOutputMap(OutputDataType.CREDIT_INFO, OutputMethod.DISPLAY, true);
		
		infoSector.setOutputMap(OutputDataType.WELCOME_MESSAGE_TEXT, OutputMethod.LOOPING_MESSAGE, true);
		
		infoSector.setOutputMap(OutputDataType.BUTTON_PRESSED, OutputMethod.TEXT_LOG, true);
		infoSector.setOutputMap(OutputDataType.EXCEPTION_HANDELING, OutputMethod.TEXT_LOG, true);
		infoSector.setOutputMap(OutputDataType.VALID_COIN_INSERTED, OutputMethod.TEXT_LOG, true);
		infoSector.setOutputMap(OutputDataType.COIN_REFUNDED, OutputMethod.TEXT_LOG, true);
		infoSector.setOutputMap(OutputDataType.CREDIT_INFO, OutputMethod.TEXT_LOG, true);
		
		
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
	

	// General Purpose Accessor Methods
	public int getCredit() {
		
		return financeSector.getCredit();
		
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
	public int getNumberOfPopCanRacks(){
		return vm.getNumberOfPopCanRacks(); 
	}
	public String getPopKindName(int index){
		return vm.getPopKindName(index); 
	}
	public int getPopKindCost(int index){
		return vm.getPopKindCost(index); 
	}
	PopCanRack getPopCanRack(int index){
		return vm.getPopCanRack(index); 
	}
	public int getPopCanCount(int index){
		return vm.getPopCanRack(index).size(); 
	}
	public void dispensePopCanRack(int index) throws DisabledException, EmptyException, CapacityExceededException {
		getPopCanRack(index).dispensePopCan();
	
	}
	public void storeCoinsInStorage() throws CapacityExceededException, DisabledException {
		getCoinReceptacle().storeCoins(); 
		
	}
	Display getDisplay(){
		return vm.getDisplay();
	}
	
	CoinReturn getCoinReturn() {
		return vm.getCoinReturn();
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
	 * Gets the credit available for purchases, in cents. 
	 * Public access for testing and external access. 
	 * It is assumed to not be a security vulnerability.
	 * @return The stored credit, in cents.
	 */
	
	
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
	 * @throws IOException 
	 */
	public void addMessage(String str, OutputDataType dataType, int time) throws IOException {
		infoSector.sendOutput( str, dataType, time);
		
		
	}

    /**
     * Adds value to the tracked credit.
     * @param added The credit to add, in cents.
     */
	public void resetDisplay() {
        infoSector.resetDisplay();
	}
	
//vvv=======================VENDING LOGIC START=======================vvv	

	/**
	 * Adds credit to the current amount in the machine.
	 * @param added	Credit to be added.
	 * @throws IOException 
	 */
    public void addCredit(int added) throws IOException{
    	    financeSector.addCredit(added);
    	
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
		financeSector.buy(popIndex);
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
	 * wrapper method for change module so other modules can interact with it
	 */
	
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
		financeSector.updateExactChangeLight();
	}
	public void interruptDisplay(){
		infoSector.interruptLoopingThread();

	}
	/**
	 * Activates the Out Of Order light when necessary.
	 */
	public void setOutOfOrder() {
		getOutOfOrderLight().activate();
	}
}
