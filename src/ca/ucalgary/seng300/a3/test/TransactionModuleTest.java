package ca.ucalgary.seng300.a3.test;

import static org.junit.Assert.*;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a3.core.*;
import ca.ucalgary.seng300.a3.exceptions.InsufficientFundsException;

/**
 * Software Engineering 300 - Group Assignment 2
 * TransactionModuleTest.java
 * 
 * This class is used to test the functionality of the TransactionModule class.
 * 
 * 87.0% code coverage was achieved in TransactionModule.
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
public class TransactionModuleTest {
	private VendingMachine vend;

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
    	vend = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
   
		List<String> popCanNames = new ArrayList<String>();
		popCanNames.add("Coke"); 
		popCanNames.add("Pepsi"); 
		popCanNames.add("Sprite"); 
		popCanNames.add("Mountain dew"); 
		popCanNames.add("Water"); 
		popCanNames.add("Iced Tea");
		
		PopCan popcan = new PopCan("Coke");
		try {
			vend.getPopCanRack(0).acceptPopCan(popcan);
		} catch (CapacityExceededException | DisabledException e) {
		};
		
		List<Integer> popCanCosts = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			popCanCosts.add(200);
		}
		
		vend.configure(popCanNames, popCanCosts);
	}
	
	/**
	 * Ensures the display device displays the message "Hi there!" when a purchase happens
	 * and the updated credit is reset to zero.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 * @throws IOException 
	 */
	@Test
	public void testPostPCreditZero() throws InterruptedException, IOException{
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		vm.addCredit(200);
		try {
			vm.buy(0);
			Thread.sleep(1000);
			assertEquals(VendingListener.returnMsg(), "Hi there!");
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException e) {
			assertTrue(false);
		}
	}

	/**
	 * Ensures the display device displays the message "Credit: " and the amount of updated credit when a purchase happens
	 * and the updated credit is non-zero.
	 * @throws IOException 
	 */
	@Test
	public void testPostPCreditNotZero() throws IOException{
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		vm.addCredit(250);
		try {
			vm.buy(0);
			assertEquals(VendingListener.returnMsg(), "Credit: 50");
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException e) {
			assertTrue(false);
		}
	}
	
	/**
	 * Ensures the buy function throws the correct exception when the credit < cost.
	 * @throws IOException 
	 */
	@Test
	public void testInsufficentFundsException() throws IOException{
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		vm.addCredit(50);
		try {
			vm.buy(0);
			assertTrue(false);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException e){
			assertTrue(true);
		}
	}
	
	/**
	 * Method to destroy the vending machine and change module after each test in order to not affect the following test.
	 */	
	@After
	public void tearDown() {
		vend = null; 
	} 
}
