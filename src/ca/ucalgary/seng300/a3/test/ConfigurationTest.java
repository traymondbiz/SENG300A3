package ca.ucalgary.seng300.a3;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class ConfigurationTest {
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
	
	/**Tested price changing capabilities. Changed pop slot 1's price from 200 to 100.
	 */
	@Test
	public void testPriceChange(){
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		vm.pressConfigButton(1);
		vm.pressedConfigEnterButton();
		vm.pressConfigButton(1);
		vm.pressConfigButton(0);
		vm.pressConfigButton(0);
		vm.pressedConfigEnterButton();
		assertEquals(100, vm.getPopKindCost(1));
	}
	
	/**Tested configuration panel's display.
	 * 
	 */
	@Test
	public void testConfigDisplay() throws InterruptedException {
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		vm.pressConfigButton(2);
		assertEquals(VendingListener.returnMsg(), "Pop Slot: 2");
		vm.pressedConfigEnterButton();
		assertEquals(VendingListener.returnMsg(), "New Price: ");
		vm.pressConfigButton(1);
		vm.pressConfigButton(0);
		vm.pressConfigButton(0);
		assertEquals(VendingListener.returnMsg(), "New Price: 100");
		vm.pressedConfigEnterButton();
		assertEquals(100, vm.getPopKindCost(2));
		assertEquals(VendingListener.returnMsg(), "Pop Slot: ");
	}
}
