package ca.ucalgary.seng300.a3;

import java.io.IOException;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

/**
 * Software Engineering 300 - Group Assignment 2
 * VendingListener.java
 * 
 * This class is registered by VendingManager with hardware classes to listen for hardware
 * events and perform first-pass checks and error-handling for them. Most "heavy-lifting" 
 * is completed within VendingManager.
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
 * @since	1.0
 */

public class VendingListener implements CoinSlotListener, PushButtonListener, CoinReturnListener, DisplayListener {
	
	/**
	 * Self-referential variable. (Singleton)
	 */
	private static VendingListener listener;
	
	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static VendingManager mgr;
	
	/**
	 * String representing the current message on display.
	 */
	private static String message = null;
	
	/**
	 * Private constructor to prevent additional creations. (Singleton)
	 */
	private VendingListener (){}
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	public static void initialize(VendingManager manager){		
		if (manager != null){
			mgr = manager;
			listener = new VendingListener();
		}
	}
	
	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton VendingListener instance  
	 */
	public static VendingListener getInstance(){
		return listener;
	}

	// Currently unneeded listener events.
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {}
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	@Override
	public void returnIsFull(CoinReturn coinReturn) {}

	/**
	 * Responds to "pressed" notifications from registered SelectionButtons. 
	 * If no matching button is found in the VendingMachine, nothing is done.
	 * Uses the buy() method in VendingManager to process the purchase.
	 * All exceptions thrown by buy() are caught here (InsufficientFunds, Disabled, Empty, etc.) 
	 * @throws IOException 
	 */
	@Override
	public void pressed(PushButton button) {
		
		int bIndex = mgr.getButtonIndex(button); 
		if (bIndex == -1){
			//Then it's not a pop selection button. 
			//This may be where we handle "change return" button presses
		}
		else{
			try{
				//Assumes a 1-to-1, strictly ordered mapping betwee
				mgr.buy(bIndex); 
				mgr.addLog(mgr.getPopKindName(bIndex)+" button pressed by user with: "+ Integer.toString(mgr.getCredit()));
			} 
			catch(InsufficientFundsException e){
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + ". " + Integer.toString(mgr.getPopKindCost(bIndex)-mgr.getCredit()) + " cents missing from credit.");
			} 
			catch(DisabledException e){
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + " since the system is disabled");
				mgr.setOutOfOrder(); // set the out of order light
			} 
			catch (EmptyException e){
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + " becasue there is none in the machine.");
			} 
			catch (CapacityExceededException e){
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + " becasue the deivery chute is full of change");
				mgr.setOutOfOrder();
			}
		}		
	}

	/**
	 * Responds to "Valid coin inserted" notifications from the registered CoinSlot.
	 * Adds the value of the coin to the VendingManager's tracked credit.
	 * @throws IOException 
	 */
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		mgr.addCredit(coin.getValue());
		
			mgr.addLog("User instered: " + Integer.toString(coin.getValue()) +"coin to coin slot");
		
	}

	/**
	 * Logs the number of coins returned to the user of the machine.
	 */
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins){
		mgr.resetDisplay();
		
		mgr.addLog("Coins Returned to User: " + coins.toString());
		
	}
	
	/**
	 * Changes the current message displayed on-screen.
	 */
	@Override
	public void messageChange(Display display, String oldMessage, String newMessage) {
		message = newMessage;
	}

	/**
	 * Retrieve the current message.
	 * @return	the current message displayed.
	 */
	public static String returnMsg(){
		return message;
	}
}