package ca.ucalgary.seng300.a3.financeSector;

import java.io.IOException;
import java.util.ArrayList;

import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;

import ca.ucalgary.seng300.a3.InsufficientFundsException;
import ca.ucalgary.seng300.a3.VendingManager;
import enumTypes.OutputDataType;

public class FinanceSector {

	
private static FinanceSector financeSector;
	
	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static VendingManager mgr;
	
	private static TransactionModule transactionModule;
	private static ChangeModule changeModule;
	private int credit = 0;
	
	
	
	public void setModule() {

		ChangeModule.setCoins(mgr.getValidCoinsArray(), mgr.getCoinCount());
		ChangeModule.setPopPrices(mgr.getPopPrices());
		
	}
	
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(VendingManager host){
		financeSector = new FinanceSector(host);
		
		
	}
	
	/**
	 * Assigns FinanceSector.java a (the) manager.
	 * 
	 * @param host	The VendingManager to call upon for hardware interactions.
	 */
	private FinanceSector(VendingManager host){		
		
		
		mgr = host;
		
		ChangeModule.initialize(this);
		TransactionModule.initialize(this);
		
		changeModule = ChangeModule.getInstance();
		transactionModule = TransactionModule.getInstance();
		
		
		
	}
	
	
	
	/**
	 * Provides access to the singleton instance for package-internal classes. (Singleton)
	 * @return The single instance of FinanceSector.
	 */
	public static FinanceSector getInstance(){
		return financeSector;
	}

	public void addCredit(int credit) throws IOException {
		
		transactionModule.addCredit(credit);
	}
	public void buy(int popIndex) throws InsufficientFundsException, EmptyException, DisabledException, CapacityExceededException, IOException {
		
		transactionModule.buy(popIndex);
	}
	
	public int getCredit(){
		return credit;
	}
	
	/**
	 * Sets the credit of the current machine.
	 * @param temp	The new credit value to be represented.
	 */
	void setCredit(int temp){
		credit = temp;
	}
	ArrayList<Integer> getCoinsToReturn(int remaining) {
		return changeModule.getCoinsToReturn(remaining, mgr.getValidCoinsArray(), mgr.getCoinCount());
	}
	public void updateExactChangeLight() {
		if(changeModule.checkChangeLight(mgr.getValidCoinsArray(),mgr.getCoinCount())){
			// Can make change, deactivate light.
			mgr.deactivateExactChangeLight();
		}else {
			// Can't make change, activate light.
			mgr.activateExactChangeLight();
		}
		
	}
	public void resetDisplay() {
        mgr.resetDisplay();
	}
	public void interruptDisplay(){
		mgr.interruptDisplay();

	}


	public int getPopKindCost(int popIndex) {
		return mgr.getPopKindCost(popIndex);
	}


	public int getPopCanCount(int popIndex) {
		return mgr.getPopCanCount(popIndex);
	}


	public void dispensePopCanRack(int popIndex) throws DisabledException, EmptyException, CapacityExceededException {
		mgr.dispensePopCanRack(popIndex);
		
	}


	public void dispenseCoin(Integer integer) {
		mgr.dispenseCoin( integer);
		
	}


	public void storeCoinsInStorage() throws CapacityExceededException, DisabledException {
		mgr.storeCoinsInStorage();
		
	}


	public void setOutOfOrder() {
		mgr.setOutOfOrder();
		
	}


	public String getPopKindName(int popIndex) {
		return mgr.getPopKindName( popIndex);
	}


	public int getNumberOfPopCanRacks() {
		return mgr.getNumberOfPopCanRacks();
	}


	public int getPopCanRackSize(int i) {
		
		return getPopCanRackSize(i);
	}


	public void addMessage(String string, OutputDataType dataType, int i) throws IOException {
		mgr.addMessage(string, dataType, i) ;
		
	}

	public boolean checkChangeLight(int[] validCoins, int[] coinCount) {
		return changeModule.checkChangeLight(validCoins, coinCount);

	}
	public ArrayList<Integer> getCoinsToReturn(int change, int[] validCoins, int[] coinCount) {
		return changeModule.getCoinsToReturn(change, validCoins, coinCount);
	}
	
}
