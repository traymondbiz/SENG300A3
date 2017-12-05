package ca.ucalgary.seng300.a3.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a3.core.*;
import ca.ucalgary.seng300.a3.finance.FinanceSector;

/**
 * Software Engineering 300 - Group Assignment 2
 * ChangeModuleTest.java
 * 
 * This class is used to test the functionality of the ChangeModule class.
 * 
 * 100.0% code coverage was achieved in ChangeModule.
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
public class ChangeModuleTest {	
	private FinanceSector financeSector;
	private VendingMachine vend;
	private int[] validCoins;
	private int[] coinCount = {0, 10, 1, 3, 5, 0};
	
	/**
	 * A method to prepare a vending machine to the basic specifications outlined by the Client 
	 * Canadian coins
	 * 6 buttons/kinds of pop
	 * 200 coins in each coin rack
	 * 10 pops per rack
	 * 200 coins can be stored in each receptacle
	 * 5 pops per delivery chute
	 * 5 coins can be returned in each receptacle
	 */
	@Before
	public void setup(){
    	int[] coinKind = {200, 1, 25, 10, 5, 100};
    	int selectionButtonCount = 6;
    	int coinRackCapacity = 200;	
    	int popCanRackCapacity = 10;
    	int receptacleCapacity = 200; 
    	int deliveryChuteCapacity = 5;
    	int coinReturnCapacity = 5;
    	vend = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
    	
    	validCoins = coinKind;
	}
	
	/**
	 * Ensure the getPossibleChangeValues and canMakeChange algorithms calculate correctly when the machine is not able to make exact change.
	 * @throws IOException 
	 */
	@Test
	public void testNotExactChange() throws IOException{
		configureVend(170);
		financeSector = FinanceSector.getInstance();
		financeSector.updateExactChangeLight();
		boolean expected = financeSector.checkChangeLight(validCoins, coinCount);
		assertEquals(expected, false);
	}
	
	/**
	 * Ensure the getPossibleChangeValues algorithm calculates correctly when the machine is able to make exact change.
	 * @throws IOException 
	 */
	@Test
	public void testExactChange() throws IOException{
		configureVend(200);
		financeSector = FinanceSector.getInstance();
		financeSector.updateExactChangeLight();
		boolean expected = financeSector.checkChangeLight(validCoins, coinCount);
		assertEquals(expected, true);
	}

	/**
	 * Ensure the getPossibleChangeValues and canMakeChange algorithms calculate correctly when the machine is able to make exact change.
	 * @throws IOException 
	 */
	@Test
	public void testExactChange2() throws IOException{
		configureVend(150);
		financeSector = FinanceSector.getInstance();
		financeSector.updateExactChangeLight();
		boolean expected = financeSector.checkChangeLight(validCoins, coinCount);
		assertEquals(expected, true);
	}

	/**
	 * Ensure the getCoinsToReturn returns the correct available coins for returns.
	 * @throws IOException 
	 */
	@Test
	public void testCoinsToReturn() throws IOException{
		configureVend(150);
		financeSector = FinanceSector.getInstance();
		financeSector.updateExactChangeLight();
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		returnList = financeSector.getCoinsToReturn(10, validCoins, coinCount);
		ArrayList<Integer> expectedReturn = new ArrayList<Integer>();
		expectedReturn.add(10);
		assertEquals(expectedReturn, returnList);
	}
	
	
	/**
	 * Method to destroy the vending machine and change module after each test in order to not affect the following test.
	 */
	@After
	public void tearDown(){
		financeSector = null;
		vend = null;
	}
	
	/**
     * Configures the hardware to use a set of names and costs for pop cans.
     * 
	 * @param popPrice Cost for each pop. Cannot be non-positive.
	 * @throws IOException 
	 */
	void configureVend(int popPrice) throws IOException{
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
			popCanCosts.add(popPrice);
		}
		vend.configure(popCanNames, popCanCosts);	
    	VendingManager.initialize(vend);		
	}
}