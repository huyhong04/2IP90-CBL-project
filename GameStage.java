import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 * 
 */
public class GameStage extends JPanel {

    private int rowSize; // Number of rows in the stage map
    private int colSize; // Number of columns in the stage map
    private JLabel[][] stageMap; // A 2D array to represent the cells in the stage map

    /** 
     * 
     * @param rowSize
     * @param colSize
     */
    public GameStage(int rowSize, int colSize) {
        this.rowSize = rowSize;
        this.colSize = colSize;

        setLayout(new GridLayout(rowSize, colSize)); // Set the layout for the stage map
        stageMap = new JLabel[rowSize][colSize]; // Initializes the stage map

        initializeBoard();
    }

    /**
     * 
     */
    private void initializeBoard() {
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < colSize; c++) {
                JLabel cell = new JLabel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                cell.setOpaque(true);
                cell.setBackground(new Color(153, 152, 156));
                stageMap[r][c] = cell;
                add(cell);
            }
        }

        gameElements();
    }

    /**
     * 
     */
    private void gameElements() {
        int wallCount;
        int obstacleCount;
        int monsterCount;

        wallCount = ((rowSize * colSize) / 4);
        obstacleCount = ((rowSize * colSize) / 8);
        monsterCount = ((rowSize * colSize) / 10);

        stageMap[0][0].setBackground(Color.BLACK);
        stageMap[rowSize - 1][colSize - 1].setBackground(new Color(68, 255, 0));

        generateElements(wallCount, new Color(77, 58, 44));
        generateElements(obstacleCount, Color.YELLOW);
        generateElements(monsterCount, Color.RED);
    }

    /**
     * 
     * @param elementCount
     * @param elementColor
     */
    private void generateElements(int elementCount, Color elementColor) {

        int cellX;
        int cellY;

        Random random = new Random();

        while (elementCount > 0) {
            elementCount--;
            cellX = random.nextInt(rowSize);
            cellY = random.nextInt(colSize);

            if ((cellX == 0 && cellY == 0) || (cellX == rowSize - 1 && cellY == colSize - 1)
                || (!stageMap[cellX][cellY].getBackground().equals(new Color(153, 152, 156)))) {
                continue;
            }

            stageMap[cellX][cellY].setBackground(elementColor);
        }
    }
}