import java.awt.event.*;

public class PlayerMovement implements KeyListener {
    
    private int playerX, playerY;  // Position of the player.
    private int previousX, previousY; // Previous valid position.

    public PlayerMovement(int startX, int startY) {
        this.playerX = startX;
        this.playerY = startY;
        this.previousX = startX;
        this.previousY = startY;

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

    @Override
    public void keyTyped(KeyEvent e) {
        // Used if necessary.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Used if necessary.
    }
}
