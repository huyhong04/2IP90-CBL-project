import java.awt.*;
// import java.util.LinkedList; // For BFS algorithm
// import java.util.Queue; // For BFS algorithm
import java.util.Random;
import javax.swing.*;

/** This generates the stage map, which has a different size depending on the chosen difficulty.
 *  The following game elements are in each stage difficulty 
 *  (except monsters are not present in 'Easy' the stage):
 *  - Walls (immobile and they stop the player from moving in a certain direction).
 *  - Obstacles (immobile and they kill the player if touched).
 *  - Monsters (mobile and they kill the player if touched).
 *  In the stage map, each game element is highlighted a color, where:
 *  - Walls are brown.
 *  - Obstacles are yellow.
 *  - Monsters are red.
 *  Within the stage map, the player is highlighted blue and the goal is highlighted green.
 *  Once the player reaches this goal, the player has completed the stage.
 */
public class GameStage extends JPanel {

    private int rowSize; // Number of rows in the stage map

    private int colSize; // Number of columns in the stage map

    private JLabel[][] stageMap; // A 2D array to represent the cells in the stage map

    PlayerMovement player;

    /** Create a layout for the stage map.
     * 
     * @param rowSize The number of rows in the stage map.
     * @param colSize The number of columns in the stage map.
     */
    public GameStage(int rowSize, int colSize) {
        super(new GridLayout(rowSize, colSize));
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.stageMap = new JLabel[rowSize][colSize]; // Initializes the stage map

        initializeStage();

        // Initialize the player at the start of the stage map
        player = new PlayerMovement(0, 0, this);
        this.addKeyListener(player);
        this.setFocusable(true);
        this.requestFocusInWindow();
        System.out.println("KeyListener added to GameStage");
        updatePlayerPosition();
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    public void updatePlayerPosition() {
        if (stageMap[player.getPreviousX()][player.getPreviousY()] != null) {
            stageMap[player.getPreviousX()][player.getPreviousY()].setBackground(new Color(153, 152, 156));
        }

        if (stageMap[player.getPlayerX()][player.getPlayerY()] != null) {
            stageMap[player.getPlayerX()][player.getPlayerY()].setBackground(Color.BLUE);
        }

        repaint();
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    /** Initializes the formatting and coloring of the stage map.
     */
    private void initializeStage() {
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < colSize; c++) {
                JLabel cell = new JLabel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                cell.setOpaque(true);
                cell.setBackground(new Color(153, 152, 156));
                stageMap[r][c] = cell;
                add(cell);
            }
        }

        // Customizing game elements onto the stage map.
        gameElements();
    }

    /** Create the number of game elements for the stage map according
     *  to the chosen difficulty.
     */
    private void gameElements() {

        int wallCount; // Number of walls randomly spawned in the stage map.
        int obstacleCount; // Number of obstacles randomly spawned in the stage map.
        int monsterCount; // Number of monsters randomly spawned in the stage map.
        // boolean validPath = true; // There is a path from the player to the goal.

        // while (validPath) {
        // Distribute the number of spawned game elements with the given number of
        // rows and columns in the stage map.
        wallCount = ((rowSize * colSize) / 10);
        obstacleCount = ((rowSize * colSize) / 12);
        monsterCount = ((rowSize * colSize) / 15);

        // Set the spawn position of the player and the goal.
        // Note that these spawn positions aren't random.
        stageMap[0][0].setBackground(Color.BLUE); // Set the player color as blue
        // Set the goal color as green
        stageMap[rowSize - 1][colSize - 1].setBackground(new Color(68, 255, 0)); 

        // Generate game elements with the corresponding chosen colors.
        generateElements(wallCount, new Color(77, 58, 44));
        generateElements(obstacleCount, Color.YELLOW);
        generateElements(monsterCount, Color.RED);

        // Checks for an available path from the player to the goal.
        // validPath = findPath();
    }

    /** Generate elements randomly onto the stage map.
     * 
     * @param elementCount The number of game elements.
     * @param elementColor The color of the game element.
     */
    private void generateElements(int elementCount, Color elementColor) {

        int cellX; // A cell in a row
        int cellY; // A cell in a column

        Random random = new Random(); // Create random object for random spawning of game elements.

        while (elementCount > 0) {
            // Decrements the element count until the sufficient number of game elements is spawned.
            elementCount--;
            // Gets a random cell at a random row and column according to the
            // number of rows and columns in the stage map.
            cellX = random.nextInt(rowSize);
            cellY = random.nextInt(colSize);

            // Avoid putting game elements where the player and goal is spawned.
            if ((cellX == 0 && cellY == 0) || (cellX == rowSize - 1 && cellY == colSize - 1)
                || (!stageMap[cellX][cellY].getBackground().equals(new Color(153, 152, 156)))) {
                continue;
            }

            // Set the according game element at a random cell in the stage map
            // with the corresponding element color.
            stageMap[cellX][cellY].setBackground(elementColor);
        }
    }

    // /** Implement a BFS algorithm to determine if there is a path
    //  *  from the player to the goal.
    //  * 
    //  * @return true if there is this path, false if otherwise.
    //  */
    // private boolean findPath() {

    //     // Initialize an array with visited cells.
    //     boolean[][] visitedCells = new boolean[rowSize][colSize];
    //     Queue<Point> cellQueue = new LinkedList<>();
    //     cellQueue.add(new Point(0, 0));
    //     visitedCells[0][0] = true; // Where the player is spawned.

    //     // Directions for moving the player.
    //     int[] rowDirection = {-1, 1, 0, 0};
    //     int[] colDirection = {0, 0, -1, 1};

    //     while (!cellQueue.isEmpty()) {
    //         Point currentCell = cellQueue.poll(); // Takes the current cell in the queue.
    //         int currentRow = currentCell.x;
    //         int currentCol = currentCell.y;

    //         // Checks if the player has reached the goal in this path-finding search.
    //         if (currentRow == rowSize - 1 && currentCol == colSize - 1) {
    //             return true;
    //         }

    //         // Check for neighbor cells
    //         for (int i = 0; i < 4; i++) {
    //             int newRow = currentRow + rowDirection[i];
    //             int newCol = currentCol + colDirection[i];

    //             // Checks if this path is within bounds and if there are any unvisited cells.
    //             if (stageMapBounds(newRow, newCol) 
    //                 && !visitedCells[newRow][newCol]
    //                 && stageMap[newRow][newCol].getBackground().equals(new Color(153, 152, 156))) {
                    
    //                 cellQueue.add(new Point(newRow, newCol));
    //                 visitedCells[newRow][newCol] = true;
    //             }
    //         }
    //     }

    //     return false; // There is no path from the player to the goal.
    // }

    /** Checks if the path-finding algorithm stays within bounds of the stage map.
     * 
     * @param row The row where the player is at in the available path 
     *      (during the path-finding algorithm).
     * @param col The column where the player is at in the available path 
     *      (during the path-finding algorithm).
     * @return true if this path-finding algorithm doesn't go out of bounds, false otherwise.
     */
    private boolean stageMapBounds(int row, int col) {
        return ((row >= 0 && row < rowSize) && (col >= 0 && col < colSize));
    }
}