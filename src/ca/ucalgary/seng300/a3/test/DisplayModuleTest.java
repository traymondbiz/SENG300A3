package ca.ucalgary.seng300.a3.test;

import static org.junit.Assert.*;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a3.core.*;
import ca.ucalgary.seng300.a3.enums.DisplayType;

/**
 * Software Engineering 300 - Group Assignment 3
 * DisplayeModuleTest.java
 * 
 * This class tests the displays 
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
public class DisplayModuleTest {
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
	
	/**
	 * Ensures the display device displays the "Hi there!" message within the first 5 seconds if the machine contains no credit.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 */
	@Test
	public void testHiThere() throws InterruptedException{
		VendingManager.initialize(vendingMachine);
		Thread.sleep(1000);
		assertEquals("Hi there!", VendingListener.returnMsg(DisplayType.FRONT_DISPLAY));
	}
	
	/**
	 * Ensures the display device erases the "Hi there!" message during the following 10 seconds if the machine contains no credit.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 */
	@Test
	public void testHiThereErased() throws InterruptedException{
		VendingManager.initialize(vendingMachine);
		Thread.sleep(6000);
		assertEquals("", VendingListener.returnMsg(DisplayType.FRONT_DISPLAY));
	}
	
	/**
	 * Ensures the display device repeats the message display cycle every 15 seconds if the machine contains no credit.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 */
	@Test
	public void testMessageCycle() throws InterruptedException{
		VendingManager.initialize(vendingMachine);
		Thread.sleep(16000);
		assertEquals("Hi there!", VendingListener.returnMsg(DisplayType.FRONT_DISPLAY));

	}
	
	/** 
	 * Ensures the display device displays the message "Credit: " and the amount of credit when the user enters valid coins.
	 */
	@Test
	public void testCreditChange() throws IOException{
		VendingManager.initialize(vendingMachine);
		VendingManager vm = VendingManager.getInstance();
		vm.addCredit(200);
		assertEquals("Credit: 200", VendingListener.returnMsg(DisplayType.FRONT_DISPLAY));
	} 
	
	/**
	 * Method to destroy the vending machine and change module after each test in order to not affect the following test.
	 */	
	@After
	public void tearDown() {
		vendingMachine = null; 
	} 
}
