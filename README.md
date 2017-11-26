# SENG 300 - Group Assignment 3

Larger groups, more complex tasks. For this final iteration, make sure you emphasize on good software design.

### Revision 2 Requirements (2017-11-26 UPDATE)
* Bitcoin payment is no longer required, but logic must be ready for easy Bitcoin extensibility.
* New: The locking/unlocking mechanism needs to make it safe for a technician to operate.

### Revision 1 Requirements
* Comprehensible, evolvable software. Performance is not a priority, but it shouldn't take one minute for something to be vended.
* Avoid dependency on hardware, and future iterations of hardware are expected soon.
* Machines must support payment via debit cards, credit cards, coins, bitcoin, and mixed transactions.
* A GUI for the simulation. Must be implemented through Java Swing in the default libraries.
* Configuration panel logic that allows a technician to configure the prices of pop.
* All previous requirements- provided they don't contradict the current ones.

### Design Goals
In addition, design goals should allow for the ease of the following:
* Easy extensibility to other forms of payment, including mixed modes (e.g., paying partially with coins, partially with credit card, bitcoin
* Ease of changing details on communication with the user and to the event log
* Ease of supporting alternative hardware  
(Note, do **not** implement other forms of payment for now. Your design however, should be able to support such a change.)  
(Note, ease is defined as the addition of new code without any severe modification of the previous code. Rewriting the entire system is not considered 'easy')

### Deliverables
The following **six** items are to be provided for submission:
#### Team Submission:
* Source code logic that meets the given requirements as well as the requirements in the previous two iterations. (Excluding contradictory requirements.)
* A **two** page design document proving how the design goals are met. (Design Justification)
* JUnit test cases that prove there are no serious bugs in the software. Must include **both** unit and system tests.
* A live demonstration of the software on December 5, 2017 (Tuesday) **in-class**. At least one member must be present.
#### Individual Submission:
* A peer evaluation document of you and your team's contribution to a solution.
  - Submitted in the form of an **excel** spreadsheet.  
  - Based on an average, give a member a positive, neutral, or negative score.  
  - Have the total of the scores SUM to 0.  
  - Write a justification for each of the scores.  
  - Includes yourself.

### Questions and Answers (Relevant)

*Technician Panel in GUI*  
**Q:** Should the technician panel be represented as a GUI? If so, should this be included in the vending machine GUI, or as a separate GUI?  
**A:** The technician panel should be a separate GUI.  It should only be accessible when the machine is unlocked, to simulate the hardware situation.

*Technician's Role in the VM*  
**Q:** We understand that the lock mechanism will need to stop the machine from behaving unsafely if it is being worked on by a technitian. Could you describe the process by which a technician would open the machine then, to, say, load pop?   
**A:** The lock already exists in the hardware, but it will only open the machine.  Your logic needs to react to locking/unlocking to enable/disable other parts of the hardware.  The technician will unlock the machine, presumably remove coins from the storage places/coin racks, ensure that the coin racks have the right amount of  money in them, and add/remove pops from the pop racks.  Then they will close the machine and lock it, which should reenable it.

*GUI Message Display*  
**Q:** For the GUI, we were wondering if you had any further details about what types of messages you'd like displayed. Will they just be basic prompts for the user to do things like enter change and then display their credit? Or should our GUI also include all the available buttons for the user?  
**A:** The GUI for the simulator should look (and behave) like a vending machine.

*Configuration Panel Design*  
**Q:** We were also wondering if the Configuration Panel should use the same GUI as everyday customers. If so, would you like the panel to be secured in a way such that technicians must verify themselves before gaining access to the Configuration Panel? Or were you envisioning a more physical panel for the technicians to work with?  
**A:** The GUI for the simulator should look (and behave) like a vending machine.
