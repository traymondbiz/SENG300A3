package ca.ucalgary.seng300.a3.test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Point;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class GUIModule {

	private JFrame guiFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIModule window = new GUIModule();
					window.guiFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIModule() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		
		JPanel userPanel = new JPanel();
		userPanel.setBorder(null);
		guiTabbedPane.addTab("User", null, userPanel, null);
		userPanel.setLayout(null);
		
		JLabel displayLabel = new JLabel("CREDIT AND MESSAGE DISPLAY HERE");
		displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		displayLabel.setIconTextGap(0);
		displayLabel.setForeground(Color.GREEN);
		displayLabel.setOpaque(true);
		displayLabel.setFont(new Font("DialogInput", Font.PLAIN, 18));
		displayLabel.setBorder(null);
		displayLabel.setBackground(Color.DARK_GRAY);
		displayLabel.setSize(750, 25);
		displayLabel.setLocation(new Point(25, 25));
		displayLabel.setToolTipText("Displays messages created by Display Module.");
		userPanel.add(displayLabel);
		
		JLabel outOfOrderLabel = new JLabel("Out of Order");
		outOfOrderLabel.setIconTextGap(10);
		outOfOrderLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		outOfOrderLabel.setHorizontalAlignment(SwingConstants.LEFT);
		outOfOrderLabel.setVerticalAlignment(SwingConstants.TOP);
		outOfOrderLabel.setDisabledIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/red.png")));
		outOfOrderLabel.setIcon(new ImageIcon("C:\\Users\\RaylazorIII\\Desktop\\green.png"));
		outOfOrderLabel.setBounds(25, 75, 250, 50);
		userPanel.add(outOfOrderLabel);
		
		JLabel exactChangeLabel = new JLabel("Exact Change");
		exactChangeLabel.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/green.png")));
		exactChangeLabel.setDisabledIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/red.png")));
		exactChangeLabel.setVerticalAlignment(SwingConstants.TOP);
		exactChangeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		exactChangeLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		exactChangeLabel.setBounds(525, 75, 250, 50);
		userPanel.add(exactChangeLabel);
		
		JLabel safetyActiveLabel = new JLabel("Safety Active");
		safetyActiveLabel.setEnabled(false);
		safetyActiveLabel.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/green.png")));
		safetyActiveLabel.setDisabledIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/red.png")));
		safetyActiveLabel.setVerticalAlignment(SwingConstants.TOP);
		safetyActiveLabel.setHorizontalAlignment(SwingConstants.LEFT);
		safetyActiveLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		safetyActiveLabel.setBounds(275, 75, 250, 50);
		userPanel.add(safetyActiveLabel);
		
		JButton popOptionAButton = new JButton("");
		popOptionAButton.setContentAreaFilled(false);
		popOptionAButton.setBorderPainted(false);
		popOptionAButton.setOpaque(false);
		popOptionAButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		popOptionAButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/Pop.png")));
		popOptionAButton.setBounds(25, 150, 150, 75);
		userPanel.add(popOptionAButton);
		
		JButton popOptionBButton = new JButton("");
		popOptionBButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/Pop.png")));
		popOptionBButton.setContentAreaFilled(false);
		popOptionBButton.setBorderPainted(false);
		popOptionBButton.setBounds(225, 150, 150, 75);
		userPanel.add(popOptionBButton);
		
		JButton popOptionCButton = new JButton("");
		popOptionCButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/Pop.png")));
		popOptionCButton.setContentAreaFilled(false);
		popOptionCButton.setBorderPainted(false);
		popOptionCButton.setBounds(25, 275, 150, 75);
		userPanel.add(popOptionCButton);
		
		JButton popOptionDButton = new JButton("");
		popOptionDButton.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/Pop.png")));
		popOptionDButton.setContentAreaFilled(false);
		popOptionDButton.setBorderPainted(false);
		popOptionDButton.setBounds(225, 275, 150, 75);
		userPanel.add(popOptionDButton);
		
		JButton toonieButton = new JButton("<html><center>200</center><br>(Toonie)</html>");
		toonieButton.setBounds(400, 150, 100, 100);
		userPanel.add(toonieButton);
		
		JButton loonieButton = new JButton("<html><center>100</center><br>(Loonie)</html>");
		loonieButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		loonieButton.setBounds(538, 150, 100, 100);
		userPanel.add(loonieButton);
		
		JButton quarterButton = new JButton("<html><center>25</center><br>(Quarter)</html>");
		quarterButton.setBounds(675, 150, 100, 100);
		userPanel.add(quarterButton);
		
		JButton dimeButton = new JButton("<html><center>10</center><br>(Dime)</html>");
		dimeButton.setBounds(400, 275, 100, 100);
		userPanel.add(dimeButton);
		
		JButton nickelButton = new JButton("<html><center>5</center><br>(Nickel)</html>");
		nickelButton.setBounds(538, 275, 100, 100);
		userPanel.add(nickelButton);
		
		JButton invalidButton = new JButton("<html><center>5000</center><br>(Invalid)</html>");
		invalidButton.setBounds(675, 275, 100, 100);
		userPanel.add(invalidButton);
		
		JLabel dispensedLabel = new JLabel("[Dispensed]: <PopName> <Change (int Value or List of Coins)>");
		dispensedLabel.setFont(new Font("Dialog", Font.BOLD, 18));
		dispensedLabel.setBounds(25, 494, 749, 42);
		userPanel.add(dispensedLabel);
		
		JPanel techPanel = new JPanel();
		guiTabbedPane.addTab("Tech", null, techPanel, null);
		techPanel.setLayout(null);
		
		JPanel miscPanel = new JPanel();
		guiTabbedPane.addTab("Misc", null, miscPanel, null);
		miscPanel.setLayout(null);
	}
}
