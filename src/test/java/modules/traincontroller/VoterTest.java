import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modules.traincontroller.Voter;

class VoterTest {
	Voter tester;
	@BeforeEach
	void setUp() throws Exception {
		tester = new Voter();
	}

	@Test
	void testVote() {
		assertEquals(17.5, tester.vote(20, 15, 17.5));
		assertEquals(0, tester.vote(21.4, 100, 3.7));
	}

}
