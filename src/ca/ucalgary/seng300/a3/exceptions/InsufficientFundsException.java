package ca.ucalgary.seng300.a3.exceptions;

/**
 * Software Engineering 300 - Group Assignment 3
 * InsufficientFundsException.java
 * 
 * This class is used to generate an exception to signal that there are no sufficient funds to make a purchase.
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


@SuppressWarnings("serial")
public class InsufficientFundsException extends Exception {
	private String message;
	public InsufficientFundsException(String message){
		super(message);
	}
	
	public String toString(){
		return message;
	}
}