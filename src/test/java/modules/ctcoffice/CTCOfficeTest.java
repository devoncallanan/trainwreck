import org.junit.*;
import static org.junit.Assert.*;
import modules.ctcoffice.*;
import shared.*;
public class CTCOfficeTest {
	
	Trainwreck _tw;
	CTCOffice _ctc;
	MessageQueue _mq;

	@Before
	public void setup() {
		_tw = new Trainwreck();
		_mq = new MessageQueue();
		_ctc = new CTCOffice(_mq,_tw);
	}

	/***************** 
	* DISPATCH TESTS *
	******************/

	/**
	 * Test that dispatching from Yard to C7(Shadyside) returns
	 * an authority of 225.0 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToShadyside() {
		_ctc.dispatchTrain(74,7);
		assertEquals(225.0,_ctc.getAuthority(),0.1);
	}

	/**
	 * Test that dispatching from Yard to F16(Herron Ave) returns
	 * an authority of 575.0 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToHerronAve() {
		_ctc.dispatchTrain(74,16);
		assertEquals(575.0,_ctc.getAuthority(),0.1);
	}

	/**
	 * Test that dispatching from Yard to G21(Swissville) returns
	 * an authority of 1875.0 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToSwissville() {
		_ctc.dispatchTrain(74,21);
		assertEquals(1875.0,_ctc.getAuthority(),0.1);
	}

	/**
	 * Test that dispatching from Yard to H25(Penn Station) returns
	 * an authority of 2175.0 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToPennStation() {
		_ctc.dispatchTrain(74,25);
		assertEquals(2175.0,_ctc.getAuthority(),0.1);
	}

	/**
	 * Test that dispatching from Yard to H35(Steel Plaza) returns
	 * an authority of 2695.0 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToSteelPlaza() {
		_ctc.dispatchTrain(74,35);
		assertEquals(2695.0,_ctc.getAuthority(),0.1);
	}

	/**
	 * Test that dispatching from Yard to H45(First Ave) returns
	 * an authority of 3215.0 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToFirstAve() {
		_ctc.dispatchTrain(74,45);
		assertEquals(3215.0,_ctc.getAuthority(),0.1);
	}

	/**
	 * Test that dispatching from Yard to I48(StationSquare) returns
	 * an authority of 3440.0 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToStationSquare() {
		_ctc.dispatchTrain(74,48);
		assertEquals(3440.0,_ctc.getAuthority(),0.1);
	}

	/**
	 * Test that dispatching from Yard to L60(South Hills Junction) returns
	 * an authority of 4183.2 with a margin of error of 0.1
	 */
	@Test
	public void dispatchRedYardToSouthHillsJunction() {
		_ctc.dispatchTrain(74,60);
		assertEquals(4183.2,_ctc.getAuthority(),0.1);
	}
}