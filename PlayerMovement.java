import javax.swing.*;

public class PlayerMovement {
    
    private int playerX; // Position of the player (x-coordinate)
    private int playerY;  // Position of the player (y-coordinate)
    private int previousX; // Previous valid position (x-coordinate)
    private int previousY; // Previous valid position (y-coordinate)
    private boolean dead; // Dead
    private GameStage gameStage;

    public PlayerMovement(int startX, int startY, GameStage gameStage) {
        this.playerX = startX;
        this.playerY = startY;
        this.previousX = startX;
        this.previousY = startY;
        this.dead = false; // Player starts alive.
        this.gameStage = gameStage;
    }

    public int getPlayerX() {
        // Mainly used for testing.
        return playerX;
    }

    public int getPlayerY() {
        // Mainly used for testing.
        return playerY;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void savePrevPosition() {
        previousX = playerX; 
        previousY = playerY;
    }

    // Revert to the previous position (stay still)
    public void stayStill() {
        playerX = previousX;
        playerY = previousY;
    }

    public void die() {
        System.out.println("PLAYER DEAD");
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public void respawn(int newX, int newY) {
        playerX = newX;
        playerY = newY;
        dead = false; 
    }

    // Methods used to check the player movement (mainly for testing).
    public void moveUp() {
        if (gameStage.canMoveTo(playerY - 1, playerX)) {
            playerY -= 1;
        }
        gameStage.tick();
    }

    public void moveDown() {
        if (gameStage.canMoveTo(playerY + 1, playerX)) {
            playerY += 1;
        }
        gameStage.tick();
    }

    public void moveLeft() {
        if (gameStage.canMoveTo(playerY, playerX - 1 )) {
            playerX -= 1;    
        }
        gameStage.tick();
    }

    public void moveRight() {
        if (gameStage.canMoveTo(playerY, playerX + 1)) {
            playerX += 1;
        }
        gameStage.tick();
    }


}