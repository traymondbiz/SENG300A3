package ca.ucalgary.seng300.a3.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ChangeModuleTest.class, CoinSlotTesting.class, ConfigurationTest.class, DisplayModuleTest.class,
		LoggingModuleTest.class, TechTest.class, TransactionModuleTest.class, UserTest.class })
public class AllTests {

}
