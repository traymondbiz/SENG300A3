# SENG 300 - Group Assignment 2

### Revision 2 Requirements (2017-11-07 UPDATE)
* Vending machine now contains a coin return. (CoinReturn.java and CoinReturnListener.java added.)  
    * Coins are now sent here instead of to the delivery chute.  
    * Extends AbstractHardware<CoinReturnListener>, implements CoinAcceptor  
* A configuration panel had been added. (ConfigurationPanel.java added.)  
    * For technicians to use in price configurations.  
    * Contains 37-buttons for data entry, a shift key, an enter key, and a small display.
* The Deliverable class has been removed.  
    * Deliverable.java removed.  
    * Coin.java and PopCan.java are the same, but no longer implements Deliverable.java

### Revision 1 Requirements
Build on-top of given vending machine hardware. Do not alter the hardware.
Implement the following logic:

* ~~Customers must be able to press pop selection buttons and enter coins, in order to ultimately purchase pops.~~ [Completed in Assn1]

* ~~Again, Canadian coin denominations must be supported.~~ [Completed in Assn1]

* ~~When the machine contains no credit for the user, a message "Hi there!" should be displayed for 5 seconds and then should be erased for 10 seconds, with this cycle repeating.~~ **[Needs to be moved to Display.java]**

* ~~When the user enters valid coins, the message "Credit: " and the amount of credit should be displayed.~~

* The machine should provide change once a pop is vended.  If the exact change can be provided, it should be, and the credit should go to zero.  If the exact change cannot be provided, the amount as close as possible to the exact change should be returned, but without going over; any remaining amount that cannot be returned should still exist as credit.

* Whenever exact change cannot be guaranteed for all possible transactions, the "exact change only" light should be turned on; otherwise, it should be off.

* If the machine cannot store additional coins or otherwise becomes aware of a problem that cannot be recovered from (including being out of all pop), the "out of order" light should be turned on; if the safety is enabled, this light should also be turned on; otherwise, it should be turned off.

* Each action of the user and the actions of the machine that are visible to the user should be logged in a text file (called the "event log").  You can determine the format of this text file.  Be aware that the vending machine possesses an internal clock, as per a standard Java virtual machine.

### Design Goals
In addition, design goals should allow for the ease of the following:
* Easy extensibility to other forms of payment, including mixed modes (e.g., paying partially with coins, partially with credit card)
* Ease of changing details on communication with the user and to the event log
* Ease of supporting alternative hardware  
(Note, do **not** implement other forms of payment for now. Your design however, should be able to support such a change.)  
(Note, ease is defined as the addition of new code without any severe modification of the previous code. Rewriting the entire system is not considered 'easy')

### Deliverables
The following **six** items are to be provided for submission:
#### Team Submission:
* Source code that supports the above requirements and design goals.
* JUnit tests that 1) Unit test Logic classes, and 2) System test hardware with logic installed.
* One or more diagrams describing how the hardware and logic support user interactions.
* One page document justifying and design choices and how they meeting the given design goals.
* The log file of your team's Git repository.
#### Individual Submission:
* A peer evaluation document of you and your team's contribution to a solution.
  - Submitted in the form of an **excel** spreadsheet.  
  - Based on an average, give a member a positive, neutral, or negative score.  
  - Have the total of the scores SUM to 0.  
  - Write a justification for each of the scores.  

### Mr. Client's Comments
  "This description is good enough for you to get started, but we're working on certain changes to the hardware that will likely impact the requirements.  Be prepared to make adjustments to your design and implementation.  I'm not sure when our hardware changes will be ready to roll out for you.  We need to work out some issues with the vendors of some of the hardware components."

  "Any time you find any issues with the hardware, we'll have to fix it on our end.  Make sure you give us some time to achieve this.  I know: it's a moving target, but that can't be helped."

  "Oh, did I remember to tell you that the vending machine absolutely must be purple!  The marketing team will have my head if I  forget that."

### Grading Breakdown
* 20% - Models

* 20% - Completeness of Requirements

* 20% - Design

* 30% - Testing

* 10% - Individual Self-Peer Evaluations

### Extra Notes
* Expect there to potentially be a demo hosted during **class time.**
..* No late submissions are permitted.

### Information Gathering (Mr. Client & D2L)

##### Exact Change Light
**Q:** When the “exact change only” light is on, is inexact change still supported? Is the light intended to be a warning to users that returned change shouldn’t be expected?  
**A:** Yes and yes.  If the light is off, the user should expect to get correct change back.

##### Live Demo - User Interface
**Q:** For the potential live demo, will a user interface need to be developed that simulates manual input, such as button presses or coin addition?  
**A:** For this iteration, no.

##### Log Output
**Q:** What all would you like outputted in the log? (Button presses? Listener events? Coin insertion? Change return? etc) Should they be logged even if they are driven by the test suite?  
**A:** All of those sorts of things.  If a button is pressed, it is pressed; it doesn't matter if it is during a test or not.  
**A:** [Update]: All interactions with the hardware should be logged.  Listeners are a purely software aspect that shouldn't be logged.  

##### Request for Hardware Updates
**Q:** Have there been any development regarding hardware changes? Even an indication of the type of hardware that may be adjusted would be much appreciated.  
**A:** It's messy and involves negotiations with other companies.  Sorry, I can't tell you more right now.  I think we'll be able to give you a definitive answer early next week.

##### "Transaction Complete!"
**Q:** Would you like a “transaction complete” message to be displayed once a pop is dispensed? e.g. “Thank you for your purchase”  
**A:** Sure! As long as the other requirements are met.

##### Order Lights
**Q:** When the out of order light is turned on, should all of the safeties be enabled as well?  
**A:** Depends on why the out of order light is on.  You want to permit the technician to restock or otherwise fix it, at which point, the light should go off (presumably).

##### Credit Card Options
**Q:** While our design should easily apply other payment methods, we do not have to include a credit card option just yet, correct?  
**A:** You don't need to include any other payment forms yet, but we are definitely heading that way, so I would worry about it in your design.

##### Log File Separations
**Q:** Should a new log be generated everytime the vending machine is initialized? Or one for its lifecycle?  
**A:** Errrm ... since the machine should last for years, the log file could get to be too big.  You should be prepared to limit the size of the log file.  In future, we are going to transmit the log over a network to a central office in a batch fashion (to reduce communication costs from lots of small records).  Or at least, that's the current plan.

##### Coin Receptacle fills up Halfway through Payment
**Q:** What ought the vending machine do if a user attempts to purchase a pop- but the coin receptacle fills up in the middle of the user paying? Should we keep in mind a return function? Ignore it as is? Or perform some particular function to your specifications?  
**A:** In principle, the machine should be configured so that this won't happen unless it is never serviced by a technician.  However, unlikely events are still possible and it's good that you're thinking about this.  
You should route the extra coins to the coin return, and turn on the "out of order" light.  This means that the technician needs to deal with it.  
At that point, the user could either try to make a purchase (if there is enough money, etc.) or ask for their money back.  If the money isn't returned for whatever reason, the credit should never be taken away.

##### Colors
**Q:** Does the marketing team want any particular shade of purple for the vending machine? Preferably, the answer being in 'hexadecimal' form. Otherwise, we'll proceed with the shade of purple identification as hexadecimal: '#50ff00'  
**A:**  Did I say purple?  Argh, mistranslation from my mother tongue ... I meant crimson.
