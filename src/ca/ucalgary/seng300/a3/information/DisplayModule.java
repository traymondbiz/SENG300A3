package ca.ucalgary.seng300.a3.information;

import java.util.Vector;

import ca.ucalgary.seng300.a3.information.DisplayModule.TimeMessage;

/**
 * Software Engineering 300 - Group Assignment 3
 * DisplayModule.java
 * 
 * Creates a thread that infinitely alternates display message until interrupt
 * is received. Interrupt is triggered when at least one valid coin is inserted.
 * Once a purchase is made and credits return to zero, the thread 
 * 
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

public class DisplayModule  implements Runnable {
	
	/**
	 * Self-referential variable. (Singleton)
	 */
	private static DisplayModule displayModule;
	
	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static OutputModule mgr;
	
	/**
	 * A list of messages to display/modify.
	 */
	private Vector<TimeMessage> messageList = new <TimeMessage>Vector() ;
	
	/**
	 * The message at a particular index of a list.
	 */
	private int messageIndex = 0;
	
	/**
	 * A small structure containing a time-message pair.
	 */
	public class TimeMessage {
		public int time;
		public String message;
		
		public  TimeMessage(String MessageIn, int timeIn ) {
			message = MessageIn;
			time = timeIn;
		}
	}

	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 * 
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(OutputModule host){
		displayModule = new DisplayModule(host);
	}
	
	/**
	 * Assigns DisplayModule.java a (the) manager.
	 * 
	 * @param host	The VendingManager to call upon for hardware interactions.
	 */
	private DisplayModule(OutputModule host){		
		mgr = host;
	}
	
	/**
	 * Provides access to the singleton instance for package-internal classes. (Singleton)
	 * @return The single instance of DisplayModule.
	 */
	public static DisplayModule getInstance(){
		return displayModule;
	}
	
	/**
	 * Adds a message to loop through while the machine is inactive.
	 * @param Str	The message to be displayed.
	 * @param time	The time the given message should be displayed for.
	 */
	public void addLoopMessage (String Str, int time) {
		TimeMessage TM = new TimeMessage(Str, time);
		messageList.addElement(TM);
	}

	/**
	 * Adds a display message.
	 * @param str	The message to be passed along.
	 */
	public void addMessage(String str, boolean locked) {
		mgr.displayMessage(str, locked);
	}

	/**
	 * Resets the list of messages to loop through.
	 */
	public void clearList( ) {
		messageList.clear();
	}

	/**
	 * Begins iterating through the message list automatically when called.
	 */
	@Override
	public void run(){
		try{ 
			while(!Thread.currentThread().isInterrupted()){
				if (!messageList.isEmpty()) {
					
					TimeMessage apple = messageList.get(messageIndex);
					String appleSauce = apple.message;
					mgr.displayMessage(appleSauce, true);
					
					//mgr.displayMessage(messageList.get(messageIndex).message, true);
					
					Thread.sleep( messageList.get(messageIndex).time );
					
					messageIndex++;
				}
				if ( messageIndex >= messageList.size()) messageIndex =0;
			}
			// Message index of 0.
		}
		catch(InterruptedException e){
			Thread.currentThread().interrupt();
			return;
		}
	}
}