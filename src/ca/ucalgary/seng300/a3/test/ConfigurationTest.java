package ca.ucalgary.seng300.a3.test;

/**
 * Software Engineering 300 - Group Assignment 3
 * ConfigurationTest.java
 * 
 * This class tests the configuration of the vending machine 
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

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a3.core.VendingListener;
import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.enums.DisplayType;

public class ConfigurationTest {
	private VendingMachine vendingMachine;

	/**
	 * A method to prepare a vending machine to the basic specifications outlined by the Client 
	 * Canadian coins
	 * And to configure the hardware to use a set of names and costs for pop cans.
	 * 6 buttons/kinds of pop
	 * 200 coins in each coin rack
	 * 10 pops per rack
	 * 200 coins can be stored in each receptacle
	 * 5 pops per delivery chute
	 * 5 coins can be returned in each receptacle
	 */
	@Before
	public void setup() {
    	int[] coinKind = {5, 10, 25, 100, 200};
    	int selectionButtonCount = 6;
    	int coinRackCapacity = 200;
    	int popCanRackCapacity = 10;
    	int receptacleCapacity = 200; 
    	int deliveryChuteCapacity = 5;
    	int coinReturnCapacity = 5;
    	vendingMachine = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
    	
    	//Generate pop names
		List<String> popCanNames = new ArrayList<String>();
		popCanNames.add("Coke"); 
		popCanNames.add("Pepsi"); 
		popCanNames.add("Sprite"); 
		popCanNames.add("Mountain dew"); 
		popCanNames.add("Water"); 
		popCanNames.add("Iced Tea");
		
        //Add Coke to pop can rack 0
		PopCan popcan = new PopCan("Coke");
		try {
			vendingMachine.getPopCanRack(0).acceptPopCan(popcan);
		} catch (CapacityExceededException | DisabledException e) {
		};
		
        //Set all popcan prices to 200
		List<Integer> popCanCosts = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			popCanCosts.add(200);
		}
		
		vendingMachine.configure(popCanNames, popCanCosts);
	}
	
	/**Tested price changing capabilities. Changed pop slot 1's price from 200 to 100.
	 */
	@Test
	public void testPriceChange(){
		//Create vending machine
		VendingManager.initialize(vendingMachine);
		VendingManager vm = VendingManager.getInstance();
		//Enter 1 to change rack 1 price
		vm.pressConfigButton(1);
		vm.pressedConfigEnterButton();
		//Enter new price to be 100
		vm.pressConfigButton(1);
		vm.pressConfigButton(0);
		vm.pressConfigButton(0);
		vm.pressedConfigEnterButton();
		assertEquals(100, vendingMachine.getPopKindCost(1)); //Confirm price is now 100
	}
	
	/**Tested configuration panel's display.
	 * 
	 */
	@Test
	public void testConfigDisplay() throws InterruptedException {
		//Create vending machine
		VendingManager.initialize(vendingMachine);
		VendingManager vm = VendingManager.getInstance();
		//Enter 2 to change rack 2 price
		
		vm.pressConfigButton(2);
		assertEquals("Pop Slot: 2", VendingListener.returnMsg(DisplayType.BACK_PANEL_DISPKAY)); //New character "2" should be added to display
		
		vm.pressedConfigEnterButton();
		assertEquals("New Price: ", VendingListener.returnMsg(DisplayType.BACK_PANEL_DISPKAY)); //Display should reset after entering new mode
		
		//Change price to be 100
		vm.pressConfigButton(1);
		vm.pressConfigButton(0);
		vm.pressConfigButton(0);
		assertEquals("New Price: 100", VendingListener.returnMsg(DisplayType.BACK_PANEL_DISPKAY)); //New characters "100" should be added to display
		vm.pressedConfigEnterButton();
		assertEquals(100, vendingMachine.getPopKindCost(2));		//Confirm price updated
		assertEquals("Pop Slot: ", VendingListener.returnMsg(DisplayType.BACK_PANEL_DISPKAY));	//Display should reset after entering new mode
	}
}
