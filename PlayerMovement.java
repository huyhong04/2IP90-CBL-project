/**
 * Handles movement logic for the player in the game.
 * This includes methods for moving in various directions,
 * stay still, death, and respawn.
 */
public class PlayerMovement {
    
    private int playerX; // Position of the player (x-coordinate)
    private int playerY;  // Position of the player (y-coordinate)
    private int previousX; // Previous valid position (x-coordinate)
    private int previousY; // Previous valid position (y-coordinate)
    private boolean dead; // Dead
    private GameStage gameStage; // The game stage

    /**
     * Initializes the PlayerMovement with a starting position and sets the player as alive.
     *
     * @param startX the initial x-coordinate of the player.
     * @param startY the initial y-coordinate of the player.
     * @param gameStage the game stage.
     */
    public PlayerMovement(int startX, int startY, GameStage gameStage) {
        this.playerX = startX;
        this.playerY = startY;
        this.previousX = startX;
        this.previousY = startY;
        this.dead = false; // Player starts alive.
        this.gameStage = gameStage;
    }

    /**
     * Returns the current x-coordinate of the player.
     *
     * @return the x-coordinate of the player.
     */
    public int getPlayerX() {
        // Mainly used for testing.
        return playerX;
    }

    /**
     * Returns the current y-coordinate of the player.
     *
     * @return the y-coordinate of the player.
     */
    public int getPlayerY() {
        // Mainly used for testing.
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
        // System.out.println("DEAD"); // For testing if this holds.
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

    /**
     * Checks if the player can move up according to the game stage bounds.
     * Moves the monsters according to the player's movement from the method tick()
     * in GameStage.
     */
    public void moveUp() {
        if (gameStage.canMoveTo(playerY - 1, playerX)) {
            playerY -= 1;
        }
        gameStage.tick();
    }

    /**
     * Checks if the player can move down according to the game stage bounds.
     * Moves the monsters according to the player's movement from the method tick()
     * in GameStage.
     */
    public void moveDown() {
        if (gameStage.canMoveTo(playerY + 1, playerX)) {
            playerY += 1;
        }
        gameStage.tick();
    }

    /**
     * Checks if the player can move left according to the game stage bounds.
     * Moves the monsters according to the player's movement from the method tick()
     * in GameStage.
     */
    public void moveLeft() {
        if (gameStage.canMoveTo(playerY, playerX - 1)) {
            playerX -= 1;    
        }
        gameStage.tick();
    }

    /**
     * Checks if the player can move right according to the game stage bounds.
     * Moves the monsters according to the player's movement from the method tick()
     * in GameStage.
     */
    public void moveRight() {
        if (gameStage.canMoveTo(playerY, playerX + 1)) {
            playerX += 1;
        }
        gameStage.tick();
    }
}