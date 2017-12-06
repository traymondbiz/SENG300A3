package ca.ucalgary.seng300.a3.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
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

import ca.ucalgary.seng300.a3.core.VendingListener;
import ca.ucalgary.seng300.a3.core.VendingManager;
import ca.ucalgary.seng300.a3.exceptions.InsufficientFundsException;

public class TechTest implements PopCanRackListener, DisplayListener, IndicatorLightListener, LockListener{
	private VendingMachine vend;
	private VendingManager vm;
	
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
		int deliveryChuteCapacity = 5;
		int coinReturnCapacity = 5;
		vend = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
		VendingManager.initialize(vend);
		vm = VendingManager.getInstance();
		
		//Register the GUI as a listener to the popCanRack
		for(int i = 0; i < selectionButtonCount; i++)
		{
			vend.getPopCanRack(i).register(this);
		}
		vend.getDisplay().register(this);
		vend.getExactChangeLight().register(this);
		vend.getOutOfOrderLight().register(this);
		vend.getLock().register(this);
		vend.getConfigurationPanel().getDisplay().register(this);

		List<String> popCanNames = new ArrayList<String>();
		popCanNames.add("Lime Zilla");
		popCanNames.add("Fissure");
		popCanNames.add("Himalayan Rain");
		popCanNames.add("Dr. Walker");
		PopCan limeZilla = new PopCan("Lime Zilla");
		PopCan fissure = new PopCan("Fissure");
		PopCan himalayanRain = new PopCan("Himalayan Rain");
		PopCan drWalker = new PopCan("Dr. Walker");
		List<Integer> popCanCosts = new ArrayList<Integer>();
		popCanCosts.add(130);
		popCanCosts.add(300);
		popCanCosts.add(225);
		popCanCosts.add(554);
		vend.configure(popCanNames, popCanCosts);
		
		// Stock Vending Machine: 5 Lime Zillas
		try {
			for(int i = 0; i < 5; i++) {
				vend.getPopCanRack(0).acceptPopCan(limeZilla);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
		
		// Stock Vending Machine: 5 Fissure Drinks
		try {
			for(int i = 0; i < 5; i++) {
				vend.getPopCanRack(1).acceptPopCan(fissure);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
		
		// Stock Vending Machine: 5 Himalayan Rains
		try {
			for(int i = 0; i < 5; i++) {
				vend.getPopCanRack(2).acceptPopCan(himalayanRain);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
		
		// Stock Vending Machine: 5 Dr. Walkers
		try {
			for(int i = 0; i < 5; i++) {
				vend.getPopCanRack(3).acceptPopCan(drWalker);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
	}
	
	@Test
	public void testBuyAfterPriceChange() throws InsufficientFundsException, EmptyException, DisabledException, CapacityExceededException, IOException {
		vm.pressConfigButton(1);
		vm.pressedConfigEnterButton();
		vm.pressConfigButton(1);
		vm.pressConfigButton(0);
		vm.pressConfigButton(0);
		vm.pressedConfigEnterButton();
		vm.addCredit(100);
		vm.buy(1);
		assertEquals(vend.getPopCanRack(1).size(), 4);
	}

	@Test
	public void testChangeMultiplePrices() {
		vm.pressConfigButton(0);
		vm.pressedConfigEnterButton();
		vm.pressConfigButton(1);
		vm.pressConfigButton(0);
		vm.pressConfigButton(0);
		vm.pressedConfigEnterButton();
		vm.pressConfigButton(1);
		vm.pressedConfigEnterButton();
		vm.pressConfigButton(2);
		vm.pressConfigButton(0);
		vm.pressConfigButton(0);
		vm.pressedConfigEnterButton();
		assertEquals(vend.getPopKindCost(0), 100);
		assertEquals(vend.getPopKindCost(1), 200);
		assertEquals(vend.getPopKindCost(2), 225);
		assertEquals(vend.getPopKindCost(3), 554);
	}
	
	@Test
	public void testLock() throws IOException, InsufficientFundsException, EmptyException, DisabledException, CapacityExceededException {
		vend.getLock().lock();
		vend.getLock().unlock();
		vm.addCredit(300);
		vm.buy(1);
		assertEquals(vend.getPopCanRack(1).size(), 4);
	}
//	@Test
//	public void testConfigDisplay() {
//		vm.pressConfigButton(2);
//		assertEquals(VendingListener.returnMsg(), "Pop Slot: 2");
//		vm.pressedConfigEnterButton();
//		assertEquals(VendingListener.returnMsg(), "New Price: ");
//		vm.pressConfigButton(1);
//		vm.pressConfigButton(0);
//		vm.pressConfigButton(0);
//		assertEquals(VendingListener.returnMsg(), "New Price: 100");
//		vm.pressedConfigEnterButton();
//		assertEquals(100, vend.getPopKindCost(2));
//		assertEquals(VendingListener.returnMsg(), "Pop Slot: ");
//	}
	
	@After
	public void tearDown(){
		vend = null;
		vm = null;
	}

	// Unused functions
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
