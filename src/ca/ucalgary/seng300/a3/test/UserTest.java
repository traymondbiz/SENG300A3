package ca.ucalgary.seng300.a3.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.Display;
import org.lsmr.vending.hardware.DisplayListener;
import org.lsmr.vending.hardware.EmptyException;
import org.lsmr.vending.hardware.IndicatorLight;
import org.lsmr.vending.hardware.IndicatorLightListener;
import org.lsmr.vending.hardware.Lock;
import org.lsmr.vending.hardware.LockListener;
import org.lsmr.vending.hardware.PopCanRack;
import org.lsmr.vending.hardware.PopCanRackListener;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.exceptions.InsufficientFundsException;

/**
 * Software Engineering 300 - Group Assignment 3
 * VendingListener.java
 * 
 * This class simulates the interaction a user will have with the vending machine 
 * 
 * Id Input/Output Technology and Solutions (Group 2)
 * @author Raymond Tran 			(30028473)
 * @author Hooman Khosravi 			(30044760)
 * @author Christopher Smith 		(10140988)
 * @author Mengxi Cheng 			(10151992)
 * @author Zachary Metz 			(30001506)
 * @author Abdul Basit 				(30033896)
 * @author Elodie Boudes			(10171818)
 * @author Michael De Grood 		(10134884)
 * @author Tae Chyung				(10139101)
 * @author Xian Meng Low			(10127970)
 * 
 * @version	3.0
 * @since	1.0
 */

public class UserTest implements PopCanRackListener, DisplayListener, IndicatorLightListener, LockListener{

	private VendingMachine vendingMachine;
	private int[] validCoins;
	private int[] coinCount = {0, 10, 1, 3, 5, 0};
	private VendingManager mgr;

	List<String> popCanNames = new ArrayList<String>();	
	List<Integer> popCanCosts = new ArrayList<Integer>();
	
	
	@Before
	public void setup(){
		Coin toonie = new Coin(200);
		Coin loonie = new Coin(100);
		Coin quarter = new Coin(25);
		Coin dime = new Coin(10);
		Coin nickel = new Coin(5);
		Coin invalidCoin = new Coin(5000);
		int[] coinKind = {5, 10, 25, 100, 200};
		
		int selectionButtonCount = 4;
		int coinRackCapacity = 200;
		int popCanRackCapacity = 10;
		int receptacleCapacity = 200; 
		int deliveryChuteCapacity = 20;
		int coinReturnCapacity = 20;
		vendingMachine = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
		vendingMachine.loadCoins(10,10,10,10,10);
		VendingManager.initialize(vendingMachine);
		mgr = VendingManager.getInstance();
		
		for(int i = 0; i < selectionButtonCount; i++)
		{
			vendingMachine.getPopCanRack(i).register(this);
		}
		vendingMachine.getDisplay().register(this);
		vendingMachine.getExactChangeLight().register(this);
		vendingMachine.getOutOfOrderLight().register(this);
		vendingMachine.getLock().register(this);
		vendingMachine.getConfigurationPanel().getDisplay().register(this);

		popCanNames.add("Lime Zilla");
		popCanNames.add("Fissure");
		popCanNames.add("Himalayan Rain");
		popCanNames.add("Dr. Walker");
		PopCan limeZilla = new PopCan("Lime Zilla");
		PopCan fissure = new PopCan("Fissure");
		PopCan himalayanRain = new PopCan("Himalayan Rain");
		PopCan drWalker = new PopCan("Dr. Walker");
		
		// Stock Vending Machine: 5 Lime Zillas
		try {
			for(int i = 0; i < 2; i++) {
				vendingMachine.getPopCanRack(0).acceptPopCan(limeZilla);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
	}
	
	
	/**
	 * Buying pop, pop in rack, machine on, enough money, no change needs to be returned 
	 * @throws IOException 
	 */
	@Test
	public void testBuyPopPerfectCondition() throws IOException {
		mgr.addCredit(130);
		popCanCosts.add(130);
		popCanCosts.add(300);
		popCanCosts.add(225);
		popCanCosts.add(554);
		vendingMachine.configure(popCanNames, popCanCosts);
	    try {
			mgr.buy(0);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException
				| IOException e) {
			e.printStackTrace();
		}
	    
	    assertEquals(1,mgr.getPopCanCount(0));
	}

	/**
	 * Buying 2 pops, so no more pop in rack (2 at first) , machine on, enough money, no change needs to be returned 
	 * @throws IOException 
	 */
	@Test
	public void testBuyPopNoPop() throws IOException {
		popCanCosts.add(130);
		popCanCosts.add(300);
		popCanCosts.add(225);
		popCanCosts.add(554);
		vendingMachine.configure(popCanNames, popCanCosts);
	    try {
	    	mgr.addCredit(130);
	    	mgr.buy(0);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException
				| IOException e) {
			e.printStackTrace();
		}
	    try {
	    	mgr.addCredit(130);
	    	mgr.buy(0);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException
				| IOException e) {
			e.printStackTrace();
		}	   
	    assertEquals(0,mgr.getPopCanCount(0));
	}
	
	/**
	 * Buy pop, check value of change 
	 * @throws IOException
	 */
	@Test 
	public void testBuyPopReturnChange() throws IOException {
		popCanCosts.add(130);
		popCanCosts.add(300);
		popCanCosts.add(225);
		popCanCosts.add(554);
		vendingMachine.configure(popCanNames, popCanCosts);
		
		mgr.addCredit(200);
		int remaining = mgr.getCredit() - 130 ;
		try {
			mgr.buy(0);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException
				| IOException e) {
			e.printStackTrace();
		}
		
		assertEquals(1,mgr.getPopCanCount(0));
		assertEquals(70,remaining);   
	} 
	
	/**
	 * Buy pop, check actual coins returned 
	 * @throws IOException
	 */
	@Test
	public void testBuyPopReturnChangeCoins() throws IOException {
		popCanCosts.add(130);
		popCanCosts.add(300);
		popCanCosts.add(225);
		popCanCosts.add(554);
		vendingMachine.configure(popCanNames, popCanCosts);
		
		mgr.addCredit(200);	
		int remaining = mgr.getCredit() - 130 ;
		try {
			mgr.buy(0);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException
				| IOException e) {
			e.printStackTrace();
		}
		
		assertEquals(1,mgr.getPopCanCount(0));
		assertEquals(70,remaining); 
		int[] returnList = new int[5];
		returnList = mgr.getCoinCount(); 
		int[] returnListExpected={10,8,8,10,10}; 
		for (int i=0; i<5; i++) {
			assertEquals(returnListExpected[i],returnList[i]);
		}
		
		
	}


	
	
	
    // UNUSED METHODS 
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	public void locked(Lock lock) {}
    public void unlocked(Lock lock) {}
	public void activated(IndicatorLight light) {}
	public void deactivated(IndicatorLight light) {}
	public void messageChange(Display display, String oldMessage, String newMessage) {}
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {}
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {}
	public void popCansFull(PopCanRack popCanRack) {}
	public void popCansEmpty(PopCanRack popCanRack) {}
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {}
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {}

}
