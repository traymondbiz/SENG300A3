package ca.ucalgary.seng300.a3.test;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.Display;
import org.lsmr.vending.hardware.DisplayListener;
import org.lsmr.vending.hardware.IndicatorLight;
import org.lsmr.vending.hardware.IndicatorLightListener;
import org.lsmr.vending.hardware.Lock;
import org.lsmr.vending.hardware.LockListener;
import org.lsmr.vending.hardware.PopCanRack;
import org.lsmr.vending.hardware.PopCanRackListener;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a3.VendingListener;
import ca.ucalgary.seng300.a3.VendingManager;

public class GUIModule implements PopCanRackListener, DisplayListener, IndicatorLightListener, LockListener{

	private JFrame guiFrame;
	private JPanel userPanel;
	private JPanel techPanel;
	private VendingMachine vm;
	private VendingManager mgr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		GUIModule window = new GUIModule();
		window.guiFrame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public GUIModule() {
		
		/*
		 * Block creating all the necessary things for a Vending Machine simulation.
		 * It creates all the coins used. (Canadian denominations and a 'bad' coin for testing.
		 * It sets the values for the size/type/capacity of each particular component of the machine.
		 * It creates the VendingMachine itself.
		 * It initializes VendingManager with such a machine, so the logic connected with the hardware.
		 * And finally, an instance of it is retrieved to play with.
		 */
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
		vm = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
		VendingManager.initialize(vm);
		mgr = VendingManager.getInstance();
		
		//Register the GUI as a listener to the popCanRack
		for(int i = 0; i < selectionButtonCount; i++)
		{
			vm.getPopCanRack(i).register(this);
		}
		vm.getDisplay().register(this);
		vm.getExactChangeLight().register(this);
		vm.getOutOfOrderLight().register(this);

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
		vm.configure(popCanNames, popCanCosts);
		
		// Stock Vending Machine: 5 Lime Zillas
		try {
			for(int i = 0; i < 5; i++) {
				vm.getPopCanRack(0).acceptPopCan(limeZilla);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
		
		// Stock Vending Machine: 5 Fissure Drinks
		try {
			for(int i = 0; i < 5; i++) {
				vm.getPopCanRack(1).acceptPopCan(fissure);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
		
		// Stock Vending Machine: 5 Himalayan Rains
		try {
			for(int i = 0; i < 5; i++) {
				vm.getPopCanRack(2).acceptPopCan(himalayanRain);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
		
		// Stock Vending Machine: 5 Dr. Walkers
		try {
			for(int i = 0; i < 5; i++) {
				vm.getPopCanRack(3).acceptPopCan(drWalker);
			}
		} 
		catch (CapacityExceededException | DisabledException e) {
		}
		
		guiFrame = new JFrame();
		guiFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(GUIModule.class.getResource("/javax/swing/plaf/metal/icons/ocean/menu.gif")));
		guiFrame.getContentPane().setBackground(Color.WHITE);
		guiFrame.setMinimumSize(new Dimension(800, 600));
		guiFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		guiFrame.setResizable(false);
		guiFrame.getContentPane().setSize(new Dimension(800, 600));
		guiFrame.setTitle("SENG 300 - Group 2 - Assn 3 - GUIModule");
		guiFrame.setSize(new Dimension(800, 600));
		guiFrame.setBounds(100, 100, 450, 300);
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane guiTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		guiTabbedPane.setBackground(Color.WHITE);
		guiFrame.getContentPane().add(guiTabbedPane, BorderLayout.CENTER);
		
		userPanel = new JPanel();
		userPanel.setBorder(null);
		guiTabbedPane.addTab("User", null, userPanel, null);
		userPanel.setLayout(null);
		
		//Component 0
		JLabel displayLabel = new JLabel("CREDIT AND MESSAGE DISPLAY HERE");
		displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		displayLabel.setIconTextGap(0);
		displayLabel.setForeground(Color.GREEN);
		displayLabel.setOpaque(true);
		displayLabel.setFont(new Font("DialogInput", Font.PLAIN, 18));
		displayLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		displayLabel.setBackground(Color.DARK_GRAY);
		displayLabel.setSize(750, 25);
		displayLabel.setLocation(new Point(25, 25));
		displayLabel.setToolTipText("Displays messages created by Display Module.");
		userPanel.add(displayLabel);
		
		//Component 1
		JLabel outOfOrderLabel = new JLabel("Out of Order");
		outOfOrderLabel.setEnabled(false);
		outOfOrderLabel.setIconTextGap(10);
		outOfOrderLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		outOfOrderLabel.setHorizontalAlignment(SwingConstants.LEFT);
		outOfOrderLabel.setVerticalAlignment(SwingConstants.TOP);
		outOfOrderLabel.setDisabledIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/red.png")));
		outOfOrderLabel.setIcon(new ImageIcon("C:\\Users\\RaylazorIII\\Desktop\\green.png"));
		outOfOrderLabel.setBounds(39, 75, 250, 50);
		userPanel.add(outOfOrderLabel);
		
		//Component 2
		JLabel exactChangeLabel = new JLabel("Exact Change");
		exactChangeLabel.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/green.png")));
		exactChangeLabel.setDisabledIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/red.png")));
		exactChangeLabel.setVerticalAlignment(SwingConstants.TOP);
		exactChangeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		exactChangeLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		exactChangeLabel.setBounds(539, 75, 250, 50);
		userPanel.add(exactChangeLabel);
		
		//Component 3
		JLabel safetyActiveLabel = new JLabel("Safety Active");
		safetyActiveLabel.setEnabled(false);
		safetyActiveLabel.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/green.png")));
		safetyActiveLabel.setDisabledIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/red.png")));
		safetyActiveLabel.setVerticalAlignment(SwingConstants.TOP);
		safetyActiveLabel.setHorizontalAlignment(SwingConstants.LEFT);
		safetyActiveLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		safetyActiveLabel.setBounds(289, 75, 250, 50);
		userPanel.add(safetyActiveLabel);
		
		//Component 4
		JButton limeZillaButton = new JButton("");
		limeZillaButton.setContentAreaFilled(false);
		limeZillaButton.setBorderPainted(false);
		limeZillaButton.setOpaque(false);
		limeZillaButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaLime.png")));
		limeZillaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vm.getSelectionButton(0).press();
			}
		});
		limeZillaButton.setBounds(75, 150, 150, 75);
		userPanel.add(limeZillaButton);
		
		//Component 5
		JButton fissureButton = new JButton("");
		fissureButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaFissure.png")));
		fissureButton.setContentAreaFilled(false);
		fissureButton.setBorderPainted(false);
		fissureButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vm.getSelectionButton(1).press();
			}
		});
		fissureButton.setBounds(250, 150, 150, 75);
		userPanel.add(fissureButton);
		
		//Component 6
		JButton himalayanRainButton = new JButton("");
		himalayanRainButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaRain.png")));
		himalayanRainButton.setContentAreaFilled(false);
		himalayanRainButton.setBorderPainted(false);
		himalayanRainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vm.getSelectionButton(2).press();
			}
		});
		himalayanRainButton.setBounds(75, 250, 150, 75);
		userPanel.add(himalayanRainButton);
		
		//Component 7
		JButton drWalkerButton = new JButton("");
		drWalkerButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaDrWalker.png")));
		drWalkerButton.setContentAreaFilled(false);
		drWalkerButton.setBorderPainted(false);
		drWalkerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vm.getSelectionButton(3).press();
			}
		});
		drWalkerButton.setBounds(250, 250, 150, 75);
		userPanel.add(drWalkerButton);
		
		//Component 8
		JButton toonieButton = new JButton("<html><center>200</center><br>(Toonie)</html>");
		toonieButton.setBorder(null);
		toonieButton.setContentAreaFilled(false);
		toonieButton.setOpaque(false);
		toonieButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinToonie.png")));
		toonieButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					vm.getCoinSlot().addCoin(toonie);
				}
				catch (DisabledException e1) {
				}
			}
		});
		toonieButton.setBounds(450, 150, 75, 75);
		userPanel.add(toonieButton);
		
		//Component 9
		JButton loonieButton = new JButton("<html><center>100</center><br>(Loonie)</html>");
		loonieButton.setBorder(null);
		loonieButton.setContentAreaFilled(false);
		loonieButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinLoonie.png")));
		loonieButton.setOpaque(false);
		loonieButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					vm.getCoinSlot().addCoin(loonie);
				}
				catch (DisabledException e1) {
				}
			}
		});
		loonieButton.setBounds(538, 150, 75, 75);
		userPanel.add(loonieButton);
		
		//Component 10
		JButton quarterButton = new JButton("<html><center>25</center><br>(Quarter)</html>");
		quarterButton.setBorder(null);
		quarterButton.setContentAreaFilled(false);
		quarterButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinQuarter.png")));
		quarterButton.setOpaque(false);
		quarterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					vm.getCoinSlot().addCoin(quarter);
				}
				catch (DisabledException e1) {
				}
			}
		});
		quarterButton.setBounds(623, 150, 75, 75);
		userPanel.add(quarterButton);
		
		//Component 11
		JButton dimeButton = new JButton("<html><center>10</center><br>(Dime)</html>");
		dimeButton.setBorder(null);
		dimeButton.setContentAreaFilled(false);
		dimeButton.setOpaque(false);
		dimeButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinDime.png")));
		dimeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					vm.getCoinSlot().addCoin(dime);
				}
				catch (DisabledException e1) {
				}
			}
		});
		dimeButton.setBounds(450, 250, 75, 75);
		userPanel.add(dimeButton);
		
		//Component 12
		JButton nickelButton = new JButton("<html><center>5</center><br>(Nickel)</html>");
		nickelButton.setBorder(null);
		nickelButton.setContentAreaFilled(false);
		nickelButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinNickel.png")));
		nickelButton.setOpaque(false);
		nickelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					vm.getCoinSlot().addCoin(nickel);
				}
				catch (DisabledException e1) {
				}
			}
		});
		nickelButton.setBounds(538, 250, 75, 75);
		userPanel.add(nickelButton);
		
		//Component 13
		JButton invalidButton = new JButton("<html><center>5000</center><br>(Invalid)</html>");
		invalidButton.setBorder(null);
		invalidButton.setContentAreaFilled(false);
		invalidButton.setOpaque(false);
		invalidButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinInvalid.png")));
		invalidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					vm.getCoinSlot().addCoin(invalidCoin);
				}
				catch (DisabledException e1) {
				}
			}
		});
		invalidButton.setBounds(623, 250, 75, 75);
		userPanel.add(invalidButton);
		
		//Component 14
		JLabel dispensedLabel = new JLabel("[Dispensed]: <PopName> <Change (int Value or List of Coins)>");
		dispensedLabel.setOpaque(true);
		dispensedLabel.setBackground(Color.WHITE);
		dispensedLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		dispensedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dispensedLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		dispensedLabel.setBounds(26, 490, 749, 42);
		userPanel.add(dispensedLabel);
		
		
		JLabel label_2 = new JLabel("5.54");
		label_2.setBounds(250, 325, 150, 14);
		userPanel.add(label_2);
		
		JLabel label_3 = new JLabel("2.25");
		label_3.setBounds(75, 325, 150, 14);
		userPanel.add(label_3);
		
		JLabel label_4 = new JLabel("1.30");
		label_4.setBounds(75, 225, 150, 14);
		userPanel.add(label_4);
		
		JLabel label_1 = new JLabel("3.00");
		label_1.setBounds(250, 225, 150, 14);
		userPanel.add(label_1);
		
		ImageIcon bg = new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/bg.png"));
		
		techPanel = new JPanel();
		guiTabbedPane.addTab("Tech", null, techPanel, null);
		techPanel.setLayout(null);
		
		//Component 0
		JLabel label = new JLabel("CREDIT AND MESSAGE DISPLAY HERE");
		label.setToolTipText("Displays messages created by Display Module.");
		label.setOpaque(true);
		label.setLocation(new Point(25, 25));
		label.setIconTextGap(0);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.GREEN);
		label.setFont(new Font("DialogInput", Font.PLAIN, 18));
		label.setBorder(new LineBorder(new Color(0, 0, 0)));
		label.setBackground(Color.DARK_GRAY);
		label.setBounds(25, 25, 750, 25);
		techPanel.add(label);
		
		//Component 1
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Dialog", Font.BOLD, 30));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		comboBox.setBounds(564, 188, 100, 100);
		((JLabel)comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel)comboBox.getRenderer()).setVerticalAlignment(SwingConstants.CENTER);
		techPanel.add(comboBox);
		
		//Component 2
		JButton btnNewButton = new JButton("1");
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 30));
		btnNewButton.setBounds(25, 299, 100, 100);
		techPanel.add(btnNewButton);
		
		//Component 3
		JButton btnO = new JButton("0");
		btnO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnO.setFont(new Font("Dialog", Font.BOLD, 30));
		btnO.setBounds(25, 406, 100, 100);
		techPanel.add(btnO);
		
		//Component 4
		JButton button = new JButton("2");
		button.setFont(new Font("Dialog", Font.BOLD, 30));
		button.setBounds(135, 299, 100, 100);
		techPanel.add(button);
		
		//Component 5
		JButton button_1 = new JButton("3");
		button_1.setFont(new Font("Dialog", Font.BOLD, 30));
		button_1.setBounds(245, 299, 100, 100);
		techPanel.add(button_1);
		
		//Component 6
		JButton button_2 = new JButton("4");
		button_2.setFont(new Font("Dialog", Font.BOLD, 30));
		button_2.setBounds(25, 188, 100, 100);
		techPanel.add(button_2);
		
		//Component 7
		JButton button_3 = new JButton("5");
		button_3.setFont(new Font("Dialog", Font.BOLD, 30));
		button_3.setBounds(135, 188, 100, 100);
		techPanel.add(button_3);
		
		//Component 8
		JButton button_4 = new JButton("6");
		button_4.setFont(new Font("Dialog", Font.BOLD, 30));
		button_4.setBounds(245, 188, 100, 100);
		techPanel.add(button_4);
		
		//Component 9
		JButton button_5 = new JButton("7");
		button_5.setFont(new Font("Dialog", Font.BOLD, 30));
		button_5.setBounds(25, 77, 100, 100);
		techPanel.add(button_5);
		
		//Component 10
		JButton button_6 = new JButton("8");
		button_6.setFont(new Font("Dialog", Font.BOLD, 30));
		button_6.setBounds(135, 77, 100, 100);
		techPanel.add(button_6);
		
		//Component 11
		JButton button_7 = new JButton("9");
		button_7.setFont(new Font("Dialog", Font.BOLD, 30));
		button_7.setBounds(245, 77, 100, 100);
		techPanel.add(button_7);
		
		//Component 12
		JButton btnShift = new JButton("SHIFT");
		btnShift.setFont(new Font("Dialog", Font.BOLD, 20));
		btnShift.setBounds(134, 406, 211, 100);
		techPanel.add(btnShift);
		
		//Component 13
		JButton btnEnter = new JButton("ENTER");
		btnEnter.setFont(new Font("Dialog", Font.BOLD, 20));
		btnEnter.setBounds(575, 406, 200, 100);
		techPanel.add(btnEnter);
		
		//Component 14
		JButton btnLock = new JButton("LOCK");
		btnLock.setFont(new Font("Dialog", Font.BOLD, 20));
		btnLock.setBounds(564, 77, 211, 100);
		techPanel.add(btnLock);
		
		//Component 15
		JButton btninsertcharacter = new JButton("<html><center>INSERT<br>CHAR</center></html>");
		btninsertcharacter.setFont(new Font("Dialog", Font.BOLD, 20));
		btninsertcharacter.setBounds(674, 188, 100, 100);
		techPanel.add(btninsertcharacter);

	}

	// UNUSED FUNCTIONS
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {}
	public void popCansFull(PopCanRack popCanRack) {}
	public void popCansEmpty(PopCanRack popCanRack) {}
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {}
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {}
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	
	// Listener function that is called when pop is dispensed
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		((JLabel) userPanel.getComponent(14)).setText("[Dispensed]: " + popCan.getName());
	}

	public void locked(Lock lock) {

	}

	public void unlocked(Lock lock) {

	}

	public void activated(IndicatorLight light) {
		if(light == vm.getExactChangeLight())
			((JLabel) userPanel.getComponent(2)).setEnabled(true);
		else
			((JLabel) userPanel.getComponent(1)).setEnabled(true);		
	}

	public void deactivated(IndicatorLight light) {
		if(light == vm.getExactChangeLight())
			((JLabel) userPanel.getComponent(2)).setEnabled(false);
		else
			((JLabel) userPanel.getComponent(1)).setEnabled(false);
	}

	public void messageChange(Display display, String oldMessage, String newMessage) {
		((JLabel) userPanel.getComponent(0)).setText(newMessage);
	}
}
