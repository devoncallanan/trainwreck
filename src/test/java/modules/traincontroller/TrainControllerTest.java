

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modules.traincontroller.TrainController;
import shared.MessageQueue;

class TrainControllerTest {
        MessageQueue message;
        TrainController tester;
        @BeforeEach
        void setUp() throws Exception {
            message = new MessageQueue();
            tester = new TrainController(message , 0);
        }

        
        @Test
        void testSetMode() {
            tester.setMode(false);
            assertFalse(tester.getMode());
        }

        @Test
        void testSetEmergency() {
            tester.setEmergency(true);
            assertTrue(tester.getEmergency());
        }

        @Test
        void testSetService() {
            tester.setService(true);
            assertTrue(tester.getService());
        }

        @Test
        void testSetAuthority() {
            tester.setAuthority(500);
            assertEquals(500, tester.getAuthority());
        }

        @Test
        void testOperateDoors() {
            tester.operateDoors(0);
            assertFalse(tester.isLeftDoors());
            tester.operateDoors(1);
            assertTrue(tester.isLeftDoors());
            tester.operateDoors(2);
            assertFalse(tester.isRightDoors());
            tester.operateDoors(3);
            assertTrue(tester.isRightDoors());
        }

        @Test
        void testSetTemp() {
            tester.setTemp(80);
            assertEquals(80, tester.getTemp());
        }

        @Test
        void testSetLights() {
            tester.setLights(true);
            assertTrue(tester.getLights());
        }

}
