

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modules.traincontroller.Power;

class PowerTest {
	private Power tester;
	@BeforeEach
	void setUp() throws Exception {
		tester = new Power();
	}

	@Test
	void testPower() {
		
	}

	@Test
	void testGeneratePower() {
		assertEquals(-20.84, tester.generatePower(-1, 0), .1);
		assertEquals(20.84, tester.generatePower(1, 0), .1);
		assertEquals(0, tester.generatePower(0, 0), .1);
		
	}

	@Test
	void testGetKp() {
		assertEquals(75, tester.getKp());
	}

	@Test
	void testGetKi() {
		assertEquals(5, tester.getKi());
	}

	@Test
	void testResetPower() {
		assertEquals(0, tester.getPreviousPowerCmd());
		assertEquals(0, tester.getPreviousUk());
		assertEquals(0, tester.getUk());
		assertEquals(0, tester.getPowerCmd());
	}

}
