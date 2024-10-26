import java.awt.event.*;

/**
 * Handles movement logic for the player in the game.
 * This includes methods for moving in various directions,
 * stay still, death, and respawn.
 */
public class PlayerMovement implements KeyListener {
    
    private int playerX, playerY;  // Position of the player.
    private int previousX, previousY; // Previous valid position.
    private boolean dead;

    /**
     * Initializes the PlayerMovement with a starting position and sets the player as alive.
     *
     * @param startX the initial x-coordinate of the player.
     * @param startY the initial y-coordinate of the player.
     */
    public PlayerMovement(int startX, int startY) {
        this.playerX = startX;
        this.playerY = startY;
        this.previousX = startX;
        this.previousY = startY;
        this.dead = false; // Player starts alive.

    }

    /**
     * Returns the current x-coordinate of the player.
     *
     * @return the x-coordinate of the player.
     */
    public int getPlayerX() {
        return playerX;
    }

    /**
     * Returns the current y-coordinate of the player.
     *
     * @return the y-coordinate of the player.
     */
    public int getPlayerY() {
        return playerY;
    }

    /**
     * Returns the previous x-coordinate of the player before the last move.
     *
     * @return the previous x-coordinate of the player.
     */
    public int getPreviousX() {
        return previousX;
    }

    /**
     * Returns the previous y-coordinate of the player before the last move.
     *
     * @return the previous y-coordinate of the player.
     */
    public int getPreviousY() {
        return previousY;
    }

     /**
     * Saves the current position of the player as the previous position.
     * This method is typically called before moving the player to track the last valid position.
     */
    public void savePrevPosition() {
        previousX = playerX; 
        previousY = playerY;
    }

    /**
     * Reverts the player to their previous position, simulating a "stay still" action.
     * This can be used when a movement action is invalidated or blocked.
     */
    public void stayStill() {
        playerX = previousX;
        playerY = previousY;
    }

     /**
     * Marks the player as dead, setting the dead status to true.
     */
    public void die() {
        dead = true;
    }

     /**
     * Checks if the player is marked as dead.
     *
     * @return true if the player is dead; false otherwise.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Resets the player to an alive state by setting the dead status to false.
     * This method can be called to "revive" the player if needed.
     */
    public void respawn(int newX, int newY) {
        playerX = newX;
        playerY = newY;
        dead = false; 

    }

    // Methods used to check the player movement (mainly for testing).
    public void moveUp() {
        playerY -= 1;
    }

    public void moveDown() {
        playerY += 1;
    }

    public void moveLeft() {
        playerX -= 1;
    }

    public void moveRight() {
        playerX += 1;
    }

    /**
     * Handles key press events to control player movement.
     * Updates the player's position based on arrow key inputs.
     *
     * @param e the KeyEvent containing information about the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // The key will move the player based on 'WASD' or arrow keys.
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            moveUp();
        } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            moveLeft();
        } else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            moveDown();
        } else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            moveRight();
        }
    }

    
    /**
     * Unused key typed event method. Required by KeyListener interface.
     *
     * @param e the KeyEvent containing information about the key typed.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    /**
     * Unused key released event method. Required by KeyListener interface.
     *
     * @param e the KeyEvent containing information about the key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
