package ca.ucalgary.seng300.a3;

import java.util.ArrayList;

import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;

/**
 * Software Engineering 300 - Group Assignment 2
 * TransactionModule.java
 * 
 * Handles all purchase-type interactions.
 * 
 * Id Input/Output Technology and Solutions (Group 2)
 * @author Raymond Tran 			(30028473)
 * @author Hooman Khosravi 			(30044760)
 * @author Christopher Smith 		(10140988)
 * @author Mengxi Cheng 			(10151992)
 * @author Zachary Metz 			(30001506)
 * @author Abdul Basit 				(30033896)
 * 
 * @version	2.0
 * @since	2.0
 */
public class TransactionModule {
	
	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static VendingManager mgr;
	
	/**
	 * Self-referential variable. (Singleton)
	 */
	private static TransactionModule transactionModule;
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(VendingManager host){
		transactionModule = new TransactionModule();
		mgr  = host;
	}

	/**
	 * Provides access to the singleton instance for package-internal classes. (Singleton)
	 * @return The single instance of the TransactionModule
	 */
	public static TransactionModule getInstance(){
		return transactionModule;
	}
	
	/**
	 * Adds and updates the manager on the current credit value. Display message.
	 * @param added	The amount of credit that the machine has accepted.
	 */
    public void addCredit(int added){
    	mgr.setCredit( added + mgr.getCredit()  );
        if(mgr.getCredit() != 0) {
            mgr.getLoopingThread2().interrupt();
            mgr.addMessage("Credit: " + Integer.toString(mgr.getCredit()));
        } 
        else {
        	mgr.resetDisplay();
        }
    }
	
    /**
     * Attempts to purchase a pop, and put the machine in the correct state.
     * @param popIndex						The desired pop's location on the virtual list of pops.
     * @throws InsufficientFundsException	Thrown if there isn't enough money.
     * @throws EmptyException				Thrown if there aren't any pops there.
     * @throws DisabledException			Thrown if the machine is disabled.
     * @throws CapacityExceededException	Thrown if the machine cannot take any more coins.
     */
	public void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException {
		
		// Checks to see there is enough credit to purchase a pop.
		int cost = mgr.getPopKindCost(popIndex);
		if (mgr.getCredit() >= cost){
			// Checks to see if that particular pop selected is still in stock.
			int canCount = mgr.getPopCanCount(popIndex);
			if (canCount > 0){
				// Dispense the pop. Take currency.
				mgr.dispensePopCanRack(popIndex);
				int remaining = mgr.getCredit() - cost ;
				if(remaining > 0) { // if true there is change to give
					ArrayList<Integer> returnList = mgr.getCoinsToReturn(remaining);
					while(!returnList.isEmpty()) {
						mgr.dispenseCoin(returnList.get(0));
						remaining -= returnList.get(0);
						returnList.remove(0);
					}
				}
				// Refresh the machine.
				mgr.setCredit(remaining); //all change has been given
				mgr.storeCoinsInStorage();
				mgr.updateExactChangeLightState();
				mgr.addCredit(0); //update screen with adding 0 credit
				if(popIsEmpty()) { // set the out of order light on if we are out of pop in all racks 
					mgr.setOutOfOrder();
				}
			}
		}
		else {
			int dif = cost - mgr.getCredit();  
			String popName = mgr.getPopKindName(popIndex);
			throw new InsufficientFundsException("Cannot buy " + popName + ". " + dif + " cents missing.");
		}
	}
	
	/**
	 * Checks to see whether there is still pop in the machine.
	 * @return	true is there is pop, false otherwise.
	 */
	private boolean popIsEmpty() {
		for(int i=0;i<mgr.getNumberOfPopCanRacks();i++) {
			if(mgr.getPopCanRack(i).size() > 0) {
				return false;
			}
		}
		return true;
	}
}