import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.KeyEvent;

public class PlayerMovementTest {

    private PlayerMovement playerMovement;

    @Before
    public void setUp() {
        // Initialize PlayerMovement with starting coordinates (e.g., 0, 0)
        playerMovement = new PlayerMovement(0, 0);
    }

    @Test
    public void testKeyPressed_WKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W, 'W');
        playerMovement.keyPressed(event);
        assertEquals(0, playerMovement.getPlayerX());
        assertEquals(-1, playerMovement.getPlayerY());
    }

    @Test
    public void testKeyPressed_AKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        playerMovement.keyPressed(event);
        assertEquals(-1, playerMovement.getPlayerX());
        assertEquals(0, playerMovement.getPlayerY());
    }

    @Test
    public void testKeyPressed_SKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S, 'S');
        playerMovement.keyPressed(event);
        assertEquals(0, playerMovement.getPlayerX());
        assertEquals(1, playerMovement.getPlayerY());
    }

    @Test
    public void testKeyPressed_DKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D, 'D');
        playerMovement.keyPressed(event);
        assertEquals(1, playerMovement.getPlayerX());
        assertEquals(0, playerMovement.getPlayerY());
    }

    @Test
    public void testKeyPressed_UpArrowKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
        playerMovement.keyPressed(event);
        assertEquals(0, playerMovement.getPlayerX());
        assertEquals(-1, playerMovement.getPlayerY());
    }

    @Test
    public void testKeyPressed_LeftArrowKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        playerMovement.keyPressed(event);
        assertEquals(-1, playerMovement.getPlayerX());
        assertEquals(0, playerMovement.getPlayerY());
    }

    @Test
    public void testKeyPressed_DownArrowKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
        playerMovement.keyPressed(event);
        assertEquals(0, playerMovement.getPlayerX());
        assertEquals(1, playerMovement.getPlayerY());
    }

    @Test
    public void testKeyPressed_RightArrowKey() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        playerMovement.keyPressed(event);
        assertEquals(1, playerMovement.getPlayerX());
        assertEquals(0, playerMovement.getPlayerY());
    }

    @Test
    public void testSavePrevPosition() {
        // Move the player to a new position (1, 0)
        playerMovement.moveRight();
        playerMovement.savePrevPosition();  // Save the current position
    
        // Verify that the player has moved to (1, 0)
        assertEquals(1, playerMovement.getPlayerX());
        assertEquals(0, playerMovement.getPlayerY());
    
        // Now move the player to (1, 1)
        playerMovement.moveDown();
    
        // Check that the previous position was correctly saved as (1, 0)
        assertEquals(1, playerMovement.getPreviousX());
        assertEquals(0, playerMovement.getPreviousY());
    }

    @Test
    public void testStayStill() {
        // Move the player to a new position
        playerMovement.moveRight();  // Player moves to (1, 0)
        playerMovement.savePrevPosition();  // Save the current position

        // Move again to (1, 1)
        playerMovement.moveDown();  // Player moves to (1, 1)

        // Call stayStill to revert to the previous position (1, 0)
        playerMovement.stayStill();

        // Verify that the player has reverted to the previous position (1, 0)
        assertEquals(1, playerMovement.getPlayerX());
        assertEquals(0, playerMovement.getPlayerY());
    }
}
