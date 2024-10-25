import java.awt.event.*;

public class PlayerMovement implements KeyListener {
    
    private int playerX, playerY;  // Position of the player.
    private int previousX, previousY; // Previous valid position.
    private boolean dead;
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
        if (playerY > 0) {
            savePrevPosition();
            playerY -= 1;
            gameStage.updatePlayerPosition();
        }
        System.out.println("Up");
    }

    public void moveDown() {
        if (playerY < gameStage.getColSize() - 1) {
            savePrevPosition();
            playerY += 1;
            gameStage.updatePlayerPosition();
        }
        System.out.println("Down");
    }

    public void moveLeft() {
        if (playerX > 0) {
            savePrevPosition();
            playerX -= 1;
            gameStage.updatePlayerPosition();
        }
        System.out.println("Left");
    }

    public void moveRight() {
        if (playerX < gameStage.getRowSize() - 1) {
            savePrevPosition();
            playerX += 1;
            gameStage.updatePlayerPosition();
        }
        System.out.println("Right");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println("Key pressed: " + KeyEvent.getKeyText(key));

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
        gameStage.updatePlayerPosition();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Used if necessary.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Used if necessary.
    }
}
