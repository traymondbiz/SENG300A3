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
		
		JButton popOptionALabel = new JButton("");
		popOptionALabel.setContentAreaFilled(false);
		popOptionALabel.setBorderPainted(false);
		popOptionALabel.setOpaque(false);
		popOptionALabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		popOptionALabel.setIcon(new ImageIcon(GUIModule.class.getResource("/ca/ucalgary/seng300/a3/test/guiresources/Pop.png")));
		popOptionALabel.setBounds(25, 150, 150, 75);
		userPanel.add(popOptionALabel);
		
		JLabel lblNewLabel = new JLabel("Example Model\r\n");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\RaylazorIII\\Desktop\\VM.png"));
		lblNewLabel.setBounds(584, 379, 191, 153);
		userPanel.add(lblNewLabel);
		
		JPanel techPanel = new JPanel();
		guiTabbedPane.addTab("Tech", null, techPanel, null);
		techPanel.setLayout(null);
		
		JPanel miscPanel = new JPanel();
		guiTabbedPane.addTab("Misc", null, miscPanel, null);
		miscPanel.setLayout(null);
	}
}
