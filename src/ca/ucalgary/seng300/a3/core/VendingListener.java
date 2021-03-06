package ca.ucalgary.seng300.a3.core;

import java.io.IOException;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a3.enums.DisplayType;
import ca.ucalgary.seng300.a3.enums.OutputDataType;
import ca.ucalgary.seng300.a3.exceptions.InsufficientFundsException;

/**
 * Software Engineering 300 - Group Assignment 3
 * VendingListener.java
 * 
 * This class is used to implement all the listeners in the hardware. 
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

public class VendingListener implements CoinSlotListener, PushButtonListener, CoinReturnListener, DisplayListener, LockListener {
	
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
	
	private static String messagePanel = null;
	
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



	/**
	 * Responds to "pressed" notifications from registered SelectionButtons. 
	 * If no matching button is found in the VendingMachine, nothing is done.
	 * Uses the buy() method in VendingManager to process the purchase.
	 * All exceptions thrown by buy() are caught here (InsufficientFunds, Disabled, Empty, etc.) 
	 * @throws IOException 
	 */
	@Override
	public void pressed(PushButton button)  {
		
		int bIndex = mgr.getButtonIndex(button); 
		if (bIndex == -1){
			//Then it's not a pop selection button. 
			//This may be where we handle "change return" button presses
			//added by XM
			//If button corresponds to a ConfigurationPanel selection button, it will send the index of the button to VendingManager
			//If button is the enter button, then tell VendingManger that it was pressed.
			if(mgr.getConfigPanel().getEnterButton().equals(button)) {
				mgr.pressedConfigEnterButton();
			}else {
				for(int configBIndex = 0; configBIndex < mgr.getNumberOfConfigButtons(); configBIndex++) {
					if (mgr.getConfigPanel().getButton(configBIndex).equals(button)) {
						mgr.pressConfigButton(configBIndex);
					}
				}
			}
			
		}
		else{
			try{
				mgr.buy(bIndex);
				mgr.addMessage(mgr.getPopKindName(bIndex)+" button pressed by user with: "+ Integer.toString(mgr.getCredit()), OutputDataType.BUTTON_PRESSED  ,0);
				
			} 
			catch(InsufficientFundsException e){
				
					mgr.addMessage("User Could not purchase " + mgr.getPopKindName(bIndex) + ". " + Integer.toString(mgr.getPopKindCost(bIndex)-mgr.getCredit()) + " cents missing from credit.",OutputDataType.EXCEPTION_HANDLING,0);
				 
			} 
			catch(DisabledException e){
				
					mgr.addMessage("User Could not purchase " + mgr.getPopKindName(bIndex) + " since the system is disabled",OutputDataType.EXCEPTION_HANDLING,0);
				
				mgr.setOutOfOrder(); // set the out of order light
			} 
			catch (EmptyException e){
				
					mgr.addMessage("User Could not purchase " + mgr.getPopKindName(bIndex) + " becasue there is none in the machine.",OutputDataType.EXCEPTION_HANDLING,0);
				
			} 
			catch (CapacityExceededException e){
				
					mgr.addMessage("User Could not purchase " + mgr.getPopKindName(bIndex) + " becasue the deivery chute is full of change",OutputDataType.EXCEPTION_HANDLING,0);
				
				mgr.setOutOfOrder();
			} catch (IOException e) {
				e.printStackTrace();
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
		try {
			mgr.addCredit(coin.getValue());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
			mgr.addMessage("User inserted: " + Integer.toString(coin.getValue()) +" coin to coin slot",OutputDataType.VALID_COIN_INSERTED,0);
	}

	/**
	 * Logs the number of coins returned to the user of the machine.
	 */
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins){
		mgr.resetDisplay();	
				mgr.addMessage(("Coins Returned to User: " + coins.toString()),OutputDataType.COIN_REFUNDED,0);		
		
	}
	
	
	
	
	/**
	 * Changes the current message displayed on-screen.
	 */
	@Override
	public void messageChange(Display display, String oldMessage, String newMessage) {
		
		if(mgr.getDisplayType(display) == DisplayType.FRONT_DISPLAY ) message = newMessage;
		
		if(mgr.getDisplayType(display) == DisplayType.BACK_PANEL_DISPKAY ) messagePanel = newMessage;
		
	}

	/**
	 * Retrieve the current message.
	 * @return	the current message displayed.
	 */
	public static String returnMsg(DisplayType displayType){
		
		if(displayType == DisplayType.FRONT_DISPLAY ) return message;	
		if(displayType == DisplayType.BACK_PANEL_DISPKAY ) return messagePanel;	
		return "";
	}

	/**
	 * Disabled the safety from the vending machine 
	 */
	@Override
	public void locked(Lock lock) {
		mgr.disableSafety();
		mgr.deactivateConfigPanel();
	}

	/**
	 * Enabled the safety on the vending machine 
	 */
	@Override
	public void unlocked(Lock lock) {
		mgr.enableSafety();	
		mgr.activateCofigPanel();
	}

	
	
	// Currently unneeded listener events.
	public void coinRejected(CoinSlot slot, Coin coin) {}
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	public void returnIsFull(CoinReturn coinReturn) {}
}
