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

import ca.ucalgary.seng300.a3.core.VendingListener;
import ca.ucalgary.seng300.a3.core.VendingManager;

public class GUIModule implements PopCanRackListener, DisplayListener, IndicatorLightListener, LockListener{

	private JFrame guiFrame;
	private JPanel userPanel;
	private JPanel techPanel;
	private VendingMachine vm;
	private VendingManager mgr;
	private boolean vendLocked = true;

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
		vm.loadCoins(10,10,10,10,10);//ESB		
		
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
		vm.getLock().register(this);
		vm.getConfigurationPanel().getDisplay().register(this);

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
		popCanCosts.add(555);
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
		JLabel displayLabel = new JLabel("Hi There!");
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
		outOfOrderLabel.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/green.png")));
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
		limeZillaButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaLimeM.png")));
		limeZillaButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaLimeP.png")));
		limeZillaButton.setContentAreaFilled(false);
		limeZillaButton.setBorderPainted(false);
		limeZillaButton.setOpaque(false);
		limeZillaButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaLime.png")));
		limeZillaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vm.getSelectionButton(0).press();
				int[] returnList = mgr.getCoinCount(); 
				for (int i =0; i<5; i++){
					System.out.println(returnList[i]);
				}
				
			}
		});
		limeZillaButton.setBounds(75, 150, 150, 75);
		userPanel.add(limeZillaButton);
		
		//Component 5
		JButton fissureButton = new JButton("");
		fissureButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaFissureM.png")));
		fissureButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaFissureP.png")));
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
		himalayanRainButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaRainM.png")));
		himalayanRainButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaRainP.png")));
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
		drWalkerButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaDrWalkerM.png")));
		drWalkerButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/sodaDrWalkerP.png")));
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
		toonieButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinToonieM.png")));
		toonieButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinToonieP.png")));
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
		loonieButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinLoonieM.png")));
		loonieButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinLoonieP.png")));
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
		quarterButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinQuarterM.png")));
		quarterButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinQuarterP.png")));
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
		dimeButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinDimeM.png")));
		dimeButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinDimeP.png")));
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
		nickelButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinNickelM.png")));
		nickelButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinNickelP.png")));
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
		invalidButton.setRolloverIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinInvalidM.png")));
		invalidButton.setPressedIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/coinInvalidP.png")));
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
		
		//Component 15
		JLabel label_4 = new JLabel("1.30");
		label_4.setBounds(75, 225, 150, 14);
		userPanel.add(label_4);
		
		//Component 16
		JLabel label_1 = new JLabel("3.00");
		label_1.setBounds(250, 225, 150, 14);
		userPanel.add(label_1);
		
		//Component 17
		JLabel label_3 = new JLabel("2.25");
		label_3.setBounds(75, 325, 150, 14);
		userPanel.add(label_3);
		
		//Component 18
		JLabel label_2 = new JLabel("5.55");//ESB
		label_2.setBounds(250, 325, 150, 14);
		userPanel.add(label_2);
		
		JLabel label = new JLabel("");
		label.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		label.setOpaque(true);
		label.setBackground(Color.WHITE);
		label.setBounds(25, 61, 750, 288);
		userPanel.add(label);
		
		JLabel bgUser = new JLabel("");
		bgUser.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/bg.png")));
		bgUser.setBounds(0, 0, 789, 543);
		userPanel.add(bgUser);
		
		ImageIcon bg = new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/bg.png"));
		
		techPanel = new JPanel();
		guiTabbedPane.addTab("Tech", null, techPanel, null);
		techPanel.setLayout(null);
		
		//Component 0
		JLabel lblEnterPopIndex = new JLabel("ENTER POP INDEX TO PROCEED");
		lblEnterPopIndex.setToolTipText("Displays messages created by Display Module.");
		lblEnterPopIndex.setOpaque(true);
		lblEnterPopIndex.setLocation(new Point(25, 25));
		lblEnterPopIndex.setIconTextGap(0);
		lblEnterPopIndex.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterPopIndex.setForeground(Color.GREEN);
		lblEnterPopIndex.setFont(new Font("DialogInput", Font.PLAIN, 18));
		lblEnterPopIndex.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblEnterPopIndex.setBackground(Color.DARK_GRAY);
		lblEnterPopIndex.setBounds(25, 25, 750, 25);
		techPanel.add(lblEnterPopIndex);
		
		//Component 1
		JComboBox comboBox = new JComboBox();
		comboBox.setEnabled(false);
		comboBox.setFont(new Font("Dialog", Font.BOLD, 30));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		comboBox.setBounds(564, 188, 100, 100);
		((JLabel)comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel)comboBox.getRenderer()).setVerticalAlignment(SwingConstants.CENTER);
		techPanel.add(comboBox);
		
		//Component 2
		JButton btn1 = new JButton("1");
		btn1.setEnabled(false);
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(1);
			}
		});
		btn1.setFont(new Font("Dialog", Font.BOLD, 30));
		btn1.setBounds(25, 299, 100, 100);
		techPanel.add(btn1);
		
		//Component 3
		JButton btn0 = new JButton("0");
		btn0.setEnabled(false);
		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(0);
			}
		});
		btn0.setFont(new Font("Dialog", Font.BOLD, 30));
		btn0.setBounds(25, 406, 100, 100);
		techPanel.add(btn0);
		
		//Component 4
		JButton btn2 = new JButton("2");
		btn2.setEnabled(false);
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(2);
			}
		});
		btn2.setFont(new Font("Dialog", Font.BOLD, 30));
		btn2.setBounds(135, 299, 100, 100);
		techPanel.add(btn2);
		
		//Component 5
		JButton btn3 = new JButton("3");
		btn3.setEnabled(false);
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(3);
			}
		});
		btn3.setFont(new Font("Dialog", Font.BOLD, 30));
		btn3.setBounds(245, 299, 100, 100);
		techPanel.add(btn3);
		
		//Component 6
		JButton btn4 = new JButton("4");
		btn4.setEnabled(false);
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(4);
			}
		});
		btn4.setFont(new Font("Dialog", Font.BOLD, 30));
		btn4.setBounds(25, 188, 100, 100);
		techPanel.add(btn4);
		
		//Component 7
		JButton btn5 = new JButton("5");
		btn5.setEnabled(false);
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(5);
			}
		});
		btn5.setFont(new Font("Dialog", Font.BOLD, 30));
		btn5.setBounds(135, 188, 100, 100);
		techPanel.add(btn5);
		
		//Component 8
		JButton btn6 = new JButton("6");
		btn6.setEnabled(false);
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(6);
			}
		});
		btn6.setFont(new Font("Dialog", Font.BOLD, 30));
		btn6.setBounds(245, 188, 100, 100);
		techPanel.add(btn6);
		
		//Component 9
		JButton btn7 = new JButton("7");
		btn7.setEnabled(false);
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(7);
			}
		});
		btn7.setFont(new Font("Dialog", Font.BOLD, 30));
		btn7.setBounds(25, 77, 100, 100);
		techPanel.add(btn7);
		
		//Component 10
		JButton btn8 = new JButton("8");
		btn8.setEnabled(false);
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(8);
			}
		});
		btn8.setFont(new Font("Dialog", Font.BOLD, 30));
		btn8.setBounds(135, 77, 100, 100);
		techPanel.add(btn8);
		
		//Component 11
		JButton btn9 = new JButton("9");
		btn9.setEnabled(false);
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressConfigButton(9);
			}
		});
		btn9.setFont(new Font("Dialog", Font.BOLD, 30));
		btn9.setBounds(245, 77, 100, 100);
		techPanel.add(btn9);
		
		//Component 12
		JButton btnShift = new JButton("SHIFT");
		btnShift.setEnabled(false);
		btnShift.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnShift.setFont(new Font("Dialog", Font.BOLD, 20));
		btnShift.setBounds(134, 406, 211, 100);
		techPanel.add(btnShift);
		
		//Component 13
		JButton btnEnter = new JButton("ENTER");
		btnEnter.setEnabled(false);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mgr.pressedConfigEnterButton();
			}
		});
		btnEnter.setFont(new Font("Dialog", Font.BOLD, 20));
		btnEnter.setBounds(575, 406, 200, 100);
		techPanel.add(btnEnter);
		
		//Component 14
		JButton btnLock = new JButton("LOCK");
		btnLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(vendLocked)
				{
					vm.getLock().unlock();
					vendLocked = false;
				}
				else
				{
					vm.getLock().lock();
					vendLocked = true;
				}
			}
		});
		btnLock.setFont(new Font("Dialog", Font.BOLD, 20));
		btnLock.setBounds(564, 77, 211, 100);
		techPanel.add(btnLock);
		
		//Component 15
		JButton btninsertcharacter = new JButton("<html><center>INSERT<br>CHAR</center></html>");
		btninsertcharacter.setEnabled(false);
		btninsertcharacter.setFont(new Font("Dialog", Font.BOLD, 20));
		btninsertcharacter.setBounds(674, 188, 100, 100);
		techPanel.add(btninsertcharacter);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/bg.png")));
		lblNewLabel.setBounds(0, 0, 789, 543);
		techPanel.add(lblNewLabel);

		mgr.updateExactChangeLightState();//ESB MDG 
		
	}

	
	
	
	// UNUSED FUNCTIONS
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {}
	public void popCansFull(PopCanRack popCanRack) {}
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {}
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {}
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	
	
	public void popCansEmpty(PopCanRack popCanRack) {
		for(int i = 0; i < vm.getNumberOfPopCanRacks(); i++)
		{
			if(popCanRack == vm.getPopCanRack(i))
			{
				((JButton) userPanel.getComponent(i + 4)).setEnabled(false);
				break;
			}
		}
	}
	
	// Listener function that is called when pop is dispensed
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		((JLabel) userPanel.getComponent(14)).setText("[Dispensed]: " + popCan.getName());
	}

	public void locked(Lock lock) {
		for(int i = 4; i < 8; i++)
		{
			if(vm.getPopCanRack(i-4).size() > 0)
				((JButton) userPanel.getComponent(i)).setEnabled(true);
		}
		for(int i = 8; i < 14; i++)
		{
			((JButton) userPanel.getComponent(i)).setEnabled(true);		
		}
		((JLabel) userPanel.getComponent(3)).setEnabled(false);
		
		for(int i = 2; i < 14; i++)
		{
			((JButton) techPanel.getComponent(i)).setEnabled(false);		
		}
		((JButton) techPanel.getComponent(15)).setEnabled(false);
		((JComboBox) techPanel.getComponent(1)).setEnabled(false);		
	}

	public void unlocked(Lock lock) {
		for(int i = 4; i < 14; i++)
		{
			((JButton) userPanel.getComponent(i)).setEnabled(false);
		}
		((JLabel) userPanel.getComponent(3)).setEnabled(true);
		
		for(int i = 2; i < 14; i++)
		{
			((JButton) techPanel.getComponent(i)).setEnabled(true);		
		}
		((JButton) techPanel.getComponent(15)).setEnabled(true);
		((JComboBox) techPanel.getComponent(1)).setEnabled(true);		
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
		if(display == vm.getDisplay()) {
			try {
				Thread.sleep(200); // It takes roughly ~185ms for all threads to boot up properly and produce messages. As such, we avoid immediately retrieving the message.
			} 
      catch (InterruptedException e) {
			}
		  ((JLabel) userPanel.getComponent(0)).setText(newMessage);
		}
		else {
		}
		((JLabel) techPanel.getComponent(0)).setText(newMessage);
			if(!mgr.getConfigMode()){
				for(int i = 0 ; i < 4; i++){
					((JLabel) userPanel.getComponent(i + 15)).setText(String.valueOf(vm.getPopKindCost(i)));
				}
			}
		}
	}
}
