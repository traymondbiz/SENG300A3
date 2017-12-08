package ca.ucalgary.seng300.a3.enums;

/**
 * Software Engineering 300 - Group Assignment 3
 * OutputDataType.java
 * 
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

public enum OutputDataType {
	WELCOME_MESSAGE_TEXT(0), CREDIT_INFO(1), BUTTON_PRESSED(2), EXCEPTION_HANDLING(3), VALID_COIN_INSERTED(4), COIN_REFUNDED(5), CONFIG_PANEL_MESSAGE(6); 
	
	private final int value;

    private OutputDataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}