import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GameStage extends JPanel implements KeyListener {

    private int rowSize;
    private int colSize;
    // private Player player;
    // private Cell[][] stageMap;
    private Point endPoint;

    public GameStage(int rowSize, int colSize) {
        // stageMap = new Cell[rowSize][colSize];
        initializeBoard();
        addKeyListener(this);
        setFocusable(true);
    }

    private void initializeBoard() {
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < colSize; c++) {
                // stageMap[r][c] = new Cell(CellType.EMPTY);
            }
        }

        // player = new Player(0, 0);
        // stageMap[0][0].setType(CellType.PLAYER);

        // endPoint = new Point(rowSize, colSize);
        // stageMap[endPoint.x][endPoint.y].setType(CellType.END);

        addWalls();
        addObstacles(50);
        addMonsters();
    }

    private void addWalls() {
        
    }

    private void addObstacles(int obstacleCount) {
        Random randomObstacles = new Random();
        
        obstacleCount = 10;

        for (int i = 0; i < obstacleCount; i++) {
            int r = randomObstacles.nextInt(rowSize);
            int c = randomObstacles.nextInt(colSize);
            // if (stageMap[r][c].getType() == CellType.EMPTY) {
            //     board[x][y].setType(CellType.OBSTACLE);
            }
        }
    }

    private void addMonsters() {

    }
    
    public static void main(String[] args) {
//        (new Stage()).movement();
    }
}

