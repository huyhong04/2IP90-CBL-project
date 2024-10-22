import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.KeyEvent;

public class PlayerMonsterInteractionTest {

    private PlayerMovement player;

    @Before
    public void setUp() {
        // Initialize player with starting coordinates.
        player = new PlayerMovement(0, 0);
    }
    
    // Test 1: Player cannot move through walls.
    @Test
    public void testWallCollision() {
        Wall wall = new Wall(0, 1);
        
        player.moveDown();
        if (player.collidesWith(wall)) {
            player.stayStill();
        }

        // Player stays still after colliding with wall(s).
        assertEquals(0, player.getPlayerX());
        assertEquals(0, player.getPlayerY());
    }

    // Test 2: Player die upon monster collision.
    @Test
    public void testMonsterCollision() {
        // Monster at player's position.
        Monster monster = new Monster(0, 0);

        if (player.collidesWith(monster)) {
            player.die();
        }

        // Check if player dies upon monster collision.
        assertTrue(player.isDead());

        // Simulate respawn
        player.respawn(0, 0); // Respawn player at (0, 0)
        assertEquals(0, player.getPlayerX());
        assertEquals(0, player.getPlayerY());
    }

    // Test 3: Player die upon interaction with obstacles (not walls).
    @Test
    public void testObstacleCollision() {
        Obstacle obstacle = new Obstacle(0, 0);

        if (player.collidesWith(obstacle)) {
            player.die();
        }

        // Check if player dies upon obstacle collision.
        assertTrue(player.isDead());

        // Simulate respawn
        player.respawn(0, 0); // Respawn player at (0, 0)
        assertEquals(0, player.getPlayerX());
        assertEquals(0, player.getPlayerY());
    }
}
