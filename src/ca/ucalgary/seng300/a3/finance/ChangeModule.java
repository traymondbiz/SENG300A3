package ca.ucalgary.seng300.a3.finance;

import java.util.ArrayList;

import ca.ucalgary.seng300.a3.core.VendingManager;

/**
 * Software Engineering 300 - Group Assignment 2
 * ChangeModule.java
 *
 * Performs calculations to determine all possible change denominations that may have to been returned,
 * and if the current machine's resources allows it to. Allows changes the 'Exact Change Light' accordingly
 * based on the above capabilities. Relies on a sorted array of valid coins, and thus a sorting algorithm has been included.
 *
 * Furthermore, due to the logic relying on mathematics, any currency-pop combination.
 * is compatible provided that the appropriate lists are sent of ChangeModule to be calculated on.
 *
 * Contains human-readable print to console statements for debugging purposes.
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
public class ChangeModule {

 	/*
	 * Initialized arrays representing arbitrary values to calculate on.
	 * valid_coins	represents the machine's accepted currency denominations.
	 * pop_prices	represents the costs of some particular pop. (Dependent on index.)
	 * coin_count	represents the current available amount of coins (excluding the user's)
	 * 				that can be used to make change. Ascending value order.
	 */

	/**
	 * Self-referential variable. (Singleton)
	 */
	private static ChangeModule changeModule;

	/**
	 * Reference to manager of this module. (Hardware calls, other module calls, etc.)
	 */
	private static TransactionModule mgr;

	/**
	 * An array containing all the valid currency denomination.
	 */
	private static int[] validCoins;

	/**
	 * An array corresponding with validCoins, showing the coin count to its equivalent index.
	 */
	private static int[] coinCount;

	/**
	 * An array corresponding to the prices of pop available in the machine.
	 */
	private static int[] popPrices;

	/**
	 * Private constructor to prevent additional creations. (Singleton)
	 */
	private ChangeModule() {}

	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 *
	 * @param manager	The VendingManager assigning itself this class.
	 */
	public static void initialize(TransactionModule manager) {
		if (manager != null) {
			mgr = manager;
			changeModule = new ChangeModule();
		}
	}

	/**
	 * Provides access to the singleton instance for package-internal classes. (Singleton)
	 * @return The single instance of the ChangeModule.
	 */
	public static ChangeModule getInstance() {
		return changeModule;
	}

	/**
	 * Sets the valid coin denominations, and their current count for each.
	 * <p>
	 * validCoins and coinCount are tied to each other to prevent unbalanced sizes.
	 *
	 * @param inValidCoins	An 'in' array of valid coin denominations.
	 * @param inCoinCount	An 'in' array of the number of coins corresponding to each denomination.
	 */
	public static void setCoins(int[] inValidCoins, int[] inCoinCount) {
		if (inValidCoins.length == inCoinCount.length) {
			validCoins = inValidCoins;
			coinCount = inCoinCount;
		}
	}

	/**
	 * Sets the list containing the prices of each pop in the vending machine.
	 * @param inPopPrices	An 'in' array of each pop price.
	 */
	public static void setPopPrices(int[] inPopPrices) {
		popPrices = inPopPrices;
	}

	/**
	 * Debugging method that receives exact values (initialized above) and performs an
	 * algorithm to determine whether the 'Exact Change Light' should be on or off.
	 */




	/**
	 * Determines what possible change values we may need to make.
	 * <p>
	 * Attempts to pay for every purchase with a single type of coin for all coins.
	 * Any remainders that cannot be paid are placed into a list of potential change values that
	 * will need to be made by the machine. (A 0 remainder means the machine can return some of the inserted coins).
	 *
	 * @param validCoins	A list of potential coin denominations that could be used.
	 * @param popPrices		The price of each particular product, so that every value can be calculate for every pop.
	 * @return				Returns a list containing every cent value change the machine needs to be prepared to make.
	 */
	private static ArrayList<Integer> getPossibleChangeValues(int[] validCoins, int[] popPrices) {
		
		ArrayList<Integer> changesToMake = new ArrayList<Integer>();
		int remainder;
		int loopCount;
		// For each coin on each type of pop, perform the test.
		
		for(int popPrice: popPrices) {
			for(int validCoinIndex: validCoins) {

				// Perform subtractions with a single type of coin until unable to.
				remainder = Math.abs(popPrice - validCoinIndex);
				loopCount = 1;
				while((validCoinIndex-1) < remainder) {
					remainder = Math.abs(remainder - validCoinIndex);
					loopCount ++;
				}
				// Debug print statements.
				System.out.printf("[%3d] Coin: %3d (%3d) times:  (Change/Remainder): %3d cents needs to be made.\n", popPrice, validCoinIndex, loopCount, remainder);
				if(remainder > 0 & !changesToMake.contains(remainder)) { // there is a remainder we need to add the ammount of change we need to give
					changesToMake.add(remainder);
				}
			}
		}
		return changesToMake;
	}

	/**
	 * Performs a Quicksort algorithm on the validCoins list, and makes identical changes to coinCount to carry the values over each swap.
	 * <p>
	 * Performs a QuickSort algorithm on validCoins, but also carries similar index swaps on coinCount.
	 * qoinSort and qoinPartition is an implementation of the QuickSort algorithm described in the textbook
	 * 'Introduction to Algorithms Third Edition' by Cormen, Leiserson, Rivest, and Stein.
	 *
	 * @param low	The recursively called lower-end of the array to be sorted.
	 * @param high	The recursively called upper-end of the array to be sorted.
	 */
	private static void qoinSort(int low, int high) {
		if (low < high) {
			int q = qoinPartition(low, high);
			qoinSort(low, q - 1);
			qoinSort(q + 1, high);
		}
	}

	/**
	 * Performs a dPartition algorithm on the validCoins list, and makes identical changes to coinCount to carry the values over each swap.
	 *
	 * @param low	The recursively called lower-end of the array to be sorted.
	 * @param high	The recursively called upper-end of the array to be sorted.
	 * @return		The 'center' of the algorithm.
	 */
	private static int qoinPartition(int low, int high) {
		validCoins = mgr.getValidCoinsArray();
		coinCount = mgr.getCoinCount();
		int p = validCoins[high];
		int i = low;
		int j = high - 1;
		int temp;

			while(i <= j) {
				// Rightward sweep.
				while(i <= j && validCoins[i] <= p) {
					i++;
				}
				// Leftward sweep.
				while(j >= i && validCoins[j] >= p) {
					j--;
				}
				// Perform swap.
				if(i < j) {
					// Swap validCoins
					temp = validCoins[i];
					validCoins[i] = validCoins[j];
					validCoins[j] = temp;

					// Swap coinCount
					temp = coinCount[i];
					coinCount[i] = coinCount[j];
					coinCount[j] = temp;
				}
			}
		// Place Pivot (validCoins)
		temp = validCoins[i];
		validCoins[i] = validCoins[high];
		validCoins[high] = temp;

		// Place Pivot (coinCount)
		temp = coinCount[i];
		coinCount[i] = coinCount[high];
		coinCount[high] = temp;

		return i;
	}

	/**
	 * Produces a list of coin values that a machine needs to return.
	 *
	 * @param change		The amount to be returned.
	 * @param validCoins	A list of valid coin denominations.
	 * @param coinCount		A list corresponding with validCoins on the number of coins per denomination.
	 * @return				An ArrayList containing all the coin values that must be returned.
	 */
	public ArrayList<Integer> getCoinsToReturn(int change, int[] coinKinds, int[] coinsIn) {

		int[][] bestChange = new int[change + 1][coinKinds.length];

		for (int i = 1; i <= change; i++) {
			int[] best = new int[coinKinds.length];
			int bestLength = change + 1;
			for (int j = 0; j < coinKinds.length; j++) {
				if (coinKinds[j] > i) { // coin denomination is more than change than we have to make
					continue;
				}
				if (coinsIn[j] == 0) { // there are no coins of this type
					continue;
				}

				int changeLeft = i - coinKinds[j];
				int[] changePossible = new int[best.length];

				for (int k = 0; k < best.length; k++) { //
					changePossible[k] = bestChange[changeLeft][k];
				}

				if (changeLeft == 0) { // returning only this coin will give the amount of change necessary
					best = new int[best.length];
					best[j] = 1;
					bestLength = 1;
					break;
				}
				if (bestChange[changeLeft][j] == coinsIn[j]) { // we've already used up every type of this coin
					continue;
				}
				if (sumArray(bestChange[changeLeft]) == 0) { // change could not be made for this particular
																// denomination
					continue;
				}
				changePossible[j] += 1;

				if (sumArray(changePossible) <= bestLength) {
					for (int k = 0; k < changePossible.length; k++) {
						best[k] = changePossible[k];
					}
					bestLength = sumArray(changePossible);
				}

			}

			for (int k = 0; k < best.length; k++) {
				bestChange[i][k] = best[k];
			}
		}

		ArrayList<Integer> map = new ArrayList<Integer>();
		//for (int i = 0; i < coinKinds.length; i++) {
		//	map.put(coinKinds[i], 0);
		//}

		for (int i = change; i >= 0; i--) {
			if (sumArray(bestChange[i]) > 0) {
				for (int j = 0; j < coinKinds.length; j++) {
					for(int k = 0; k < bestChange[i][j]; k++) {
						map.add(coinKinds[j]);
					}
				}
				break;
			}
		}
		return map;
	}

	//added by Zach
	private int sumArray(int[] array){
		int sum = 0;
		for(int value: array){
			sum = sum + value;
		}
		return sum;
	}

	/**
	 * [DEBUGGING METHOD]: Used in order to support the accompanying unit test: ChangeModuleTest.java
	 * <p>
	 * Generates a list of possible change values that the machine may need to accompany, then it checks
	 * whether or not the machine is able to accompany them.
	 *
	 * @param validCoins	Currency values accepted by the given machine.
	 * @param coinCount		Number of coins for each corresponding currency value by the given machine.
	 * @return				true if the machine is able to make change for every instance
	 */
	public boolean checkChangeLight(int[] validCoins, int[] coinCount) {
		ArrayList<Integer> valuesOfChange = getPossibleChangeValues(validCoins, mgr.getPopPrices());
		for(int change : valuesOfChange) {
			// If at some point, we are NOT able to make change for a specific 'remainder', return false.
			if(!canMakeChange(change,mgr.getValidCoinsArray(), mgr.getCoinCount())) {
				return false;
			}
		}
		return true;
	}
	
	

	/**
	 * [DEBUGGING METHOD]: Used in order to support the accompanying unit test: ChangeModuleTest.java
	 * <p>
	 * Attempts to get change to equal 0 by subtracting the largest supported coin denomination
	 * that is in stock until it either reaches 0 successfully, or no longer has coins to support it.
	 *
	 * @param change		A particular value change value that the VM may have to make.
	 * @param validCoins	Currency values accepted by the given machine.
	 * @param coinCount		Number of coins for each corresponding currency value by the given machine.
	 * @return				true if the machine is able to make change. false otherwise.
	 */
	private static boolean canMakeChange(int change, int[] validCoins, int[] coinCount) {
		qoinSort(0, validCoins.length -1);
		int[] numOfCoins = coinCount.clone();

		// Indexes start at 0.
		int i = validCoins.length - 1;

		while(i >= 0) {
			// In descending order from largest coin to smallest,
			// While that particular coin is still 'in-stock' in machine AND
			// there is still change that machine COULD return, perform the following:
			while(numOfCoins[i] > 0 && change >= validCoins[i] ) {

				// Remaining change that the machine must produce.
				System.out.printf("[Step] Change: %3d   ", change);
				// Particular coin being used to return currency.
				System.out.printf("Coin: %3d   ", validCoins[i]);
				// Remaining amount of particular coin in machine.
				System.out.printf("Left: %3d   ", numOfCoins[i]);

				// Logical operation to find remaining change to operate on.
				// Decrement the number of remaining particular coin in machine.
				change -= validCoins[i];
				numOfCoins[i] --;

				// Remaining change that the machine must produce.
				System.out.printf("Remainder: %3d\n", change);

			}

			// If no more change is needed to made, return true since
			// this amount of change to return is supported.
            if(change == 0) {
            	return true;
            }

            // Move to the next coin to perform operation.
			i--;
		}

		// If there are no ways the machine can return the
		// exact amount of change given its current resources,
		// return false. (And have another method enable the ExactChangeLight)
		return false;
	}
}
