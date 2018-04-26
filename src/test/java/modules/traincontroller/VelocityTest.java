import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modules.traincontroller.Velocity;

class VelocityTest {
	Velocity tester;
	@BeforeEach
	void setUp() throws Exception {
		tester = new Velocity();
	}

	@Test
	void testFeedback() {
		tester.setSpeedLimit(70, true);
		tester.setFeedback(25, true, false);
		assertEquals(25, tester.feedback());
		
	}

	@Test
	void testError() {
		tester.setSpeedLimit(70, true);
		tester.setFeedback(25, true, false);
		assertEquals(45, tester.error());
	}

	@Test
	void testSetSpeedLimit() {
		tester.setSpeedLimit(70, true);
		assertEquals(70, tester.getSpeedLimit());
	}

	@Test
	void testSetSetpointSpeed() {
		tester.setSetpointSpeed(25);
		assertEquals(25, tester.getSetpointSpeed());
	}

	@Test
	void testSetSuggestedSpeed() {
		tester.setSuggestedSpeed(25);
		assertEquals(25, tester.getSuggestedSpeed());
	}

}
