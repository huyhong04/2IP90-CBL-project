// All imports needed to create the game stage.
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.TreeMap;
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

    /** Enums to represent game elements in the stage map.
     */
    private static enum Tile {
        EMPTY, WALL, OBSTACLE, MONSTER, GOAL, START
    }

    /** Enums to represent difficulties.
     */
    public static enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private InputMap inputMap; // Inputting keys for player movement.

    private ActionMap actionMap; // Creating player movement from inputted keys.

    private Difficulty difficulty; // For the stage difficulty.

    private int rowSize; // Number of rows in the stage map.

    private int colSize; // Number of columns in the stage map.

    private Tile[][] stageMap; // A 2D array to represent the cells in the stage map.

    private JLabel[][] graphics; // A 2D array to represent the data of stageMap in a JLabel.

    private PlayerMovement player; // A player with movement methods.

    private GameWindow gameWindow; // The game window.

    private String playerName; // The inputted player name.

    /** Create a layout for the stage map.
     * 
     *  @param gameWindow The game window.
     *  @param difficulty The stage with the corresponding difficulty.
     */
    public GameStage(GameWindow gameWindow, Difficulty difficulty) {
        // Emphasizes the current game window and difficulty.
        this.gameWindow = gameWindow;
        this.difficulty = difficulty;

        /* Create cases for the difficulty where we can switch the difficulty
         * the according stage map with corresponding game elements and stage map dimensions.
         */
        switch (difficulty) {
            case EASY -> {
                rowSize = 15;
                colSize = 15;
            } 
            case MEDIUM -> {
                rowSize = 25;
                colSize = 25;
            }
            case HARD -> {
                rowSize = 40;
                colSize = 40;
            }
        }

        // Emphasizes keybinds.
        this.inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.actionMap = this.getActionMap();

        /* Creates the layout for the stage map with dimensions 
         * corresponding to the stage difficulty.    
         */
        this.setLayout(new GridLayout(rowSize, colSize));

        // Initialize the player at the start of the stage map.
        player = new PlayerMovement(0, 0, this);

        // Initialise an array with corresponding dimensions.
        this.stageMap = new Tile[rowSize][colSize]; // Initializes the stage map
        this.graphics = new JLabel[rowSize][colSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                graphics[i][j] = new JLabel(); // Create a JLabel for each cell in the stage map.
                graphics[i][j].setBackground(Color.BLACK);
                graphics[i][j].setOpaque(true);
                graphics[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(graphics[i][j]);
            }
        }

        // Generate game elements in the stage map.
        generateMap();
        
        // Draw game elements with distinct colors.
        draw();

        // Set the stage map window to be focused when accessed.
        this.setFocusable(true);

        // Keybinds for player movement.
        register("UP", () -> player.moveUp(),
                new KeyStroke[] {KeyStroke.getKeyStroke("W")});
        register("DOWN", () -> player.moveDown(),
            new KeyStroke[] {KeyStroke.getKeyStroke("S")});
        register("LEFT", () -> player.moveLeft(),
            new KeyStroke[] {KeyStroke.getKeyStroke("A")});
        register("RIGHT", () -> player.moveRight(),
            new KeyStroke[] {KeyStroke.getKeyStroke("D")});

        // Sets the preferred size for the stage map dimension window.
        setPreferredSize(new Dimension(400, 400));
    }

    /** Draw the data of how the stage map is generated,
     *  so that this can be seen visually in the game stage.
     */
    public void draw() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                switch (stageMap[i][j]) {
                    case EMPTY -> {
                        graphics[i][j].setBackground(Color.WHITE); // Empty cells
                    }
                    case MONSTER -> {
                        graphics[i][j].setBackground(Color.RED); // Monsters
                    }
                    case OBSTACLE -> {
                        graphics[i][j].setBackground(Color.YELLOW); // Obstacles
                    }
                    case GOAL -> {
                        graphics[i][j].setBackground(Color.GREEN); // Goal
                    }
                    case START -> {
                        graphics[i][j].setBackground(Color.BLACK); // Start cell
                    }
                    case WALL -> {
                        graphics[i][j].setBackground(new Color(84, 61, 43)); // Walls
                    }
                    default -> {
                        // Other cases (do nothing since we don't have other game elements).
                    }
                }
            }
        }

        // Color the player to be blue.
        graphics[player.getPlayerY()][player.getPlayerX()].setBackground(Color.BLUE);

        // Update the player when a key is pressed (visually).
        revalidate();
    }

    /** Generates the map with game elements, where this amount is based on the density
     *  according to the stage difficulty.
     * 
     *  We compute the density of the game elements using weights, 
     *  allowing for easier modifications.
     *  In general, the probability for the game elements are calculated as follows:
     *  - Wall weight is some positive float,
     *  - Obstacle weight is some positive float,
     *  - Monster weight is some positive float,
     *  - Empty cells weight is the sum of the wall, obstacle and monster weights.
     *  - Total weights is the sum of the all the weights.
     *  Then the probability of a certain game element in the stage map is its weight
     *  divided by the total weights.
     *  Note that the empty weights always take up 50% of the stage map.
     */
    public void generateMap() {
        /* Returns the difficulty based on the enum order for difficulties,
         * where 'Easy' has scaling 0, 'Medium' has scaling 1, and 'Hard' has scaling 2.
         */
        double scaling = (double) difficulty.ordinal();

        /* For example, in the 'Medium' stage:
         * - The probability of walls is 3 / 15 (approx. 22.2%).
         * - The probability of obstacles is 2.5 / 15 (approx. 18.5%).
         * - The probability of monsters is 2 / 15 (approx. 14.8%).
         * - The probability of empty cells is 7.5 / 15 (50%).
         */
        double wallWeight = 1.5 * (scaling + 1.0); // Density of walls on the stage map.
        double obstacleWeight = 1.25 * (scaling + 1.0); // Density of obstacles on the stage map.
        double monsterWeight = 2 * (scaling); // Density of monsters on the stage map.
        double emptyWeight = wallWeight + obstacleWeight + monsterWeight; // Density of empty cells.

        // Use a tree map to map the following weights to the corresponding game elements.
        TreeMap<Double, Tile> treeMap = new TreeMap<>();

        treeMap.put(treeMap.keySet().stream().mapToDouble(i -> i).sum() 
            + monsterWeight, Tile.MONSTER);
        treeMap.put(treeMap.keySet().stream().mapToDouble(i -> i).sum() 
            + obstacleWeight, Tile.OBSTACLE); // 0-5
        treeMap.put(treeMap.keySet().stream().mapToDouble(i -> i).sum() 
            + wallWeight, Tile.WALL); // 5-10
        treeMap.put(treeMap.keySet().stream().mapToDouble(i -> i).sum() 
            + emptyWeight, Tile.EMPTY); // 10-20

        double maxWeight = treeMap.keySet().stream().mapToDouble(i -> i).max().getAsDouble();
        Random random = new Random();

        // Generates game elements randomly on the stage map (no guarantee of having a valid path).
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                double randomWeight = random.nextDouble() * maxWeight;
                stageMap[i][j] = treeMap.ceilingEntry(randomWeight).getValue();
            }
        }
        /* Generate the player spawn point to be in the top left corner,
         * and the goal to be in the bottom right corner.
         */
        stageMap[0][0] = Tile.START;
        stageMap[rowSize - 1][colSize - 1] = Tile.GOAL;
    }

    /** Move monsters according to player position and movement
     *  to create a sense of "unpredictability".
     *  Determines which tiles are occupied and where none of the game elements
     *  can overlap a cell on the stage map.
     *  If the player dies or wins, 
     *  the player is redirected to the stage selection screen or leaderboards.
     */
    public void tick() {
        Random random = new Random();
        
        // Iterate over each cell in the stage map.
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                // Continue moving if the corresponding cell isn't a monster.
                if (stageMap[i][j] != Tile.MONSTER) {
                    continue;
                } else {
                    // Decides a random move direction for the monster to move to an adjacent tile.
                    int move = random.nextInt(4); 
                    switch (move) {
                        case 0 -> {
                            // If the move is valid and the tile is empty, the monster moves up.
                            if (0 <= j - 1 && j - 1 < colSize && stageMap[i][j - 1] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i][j - 1] = Tile.MONSTER;
                            }
                        }
                        case 1 -> {
                            // If the move is valid and the tile is empty, the monster moves right.
                            if (0 <= i + 1 && i + 1 < colSize && stageMap[i + 1][j] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i + 1][j] = Tile.MONSTER;
                            }
                        }
                        case 2 -> {
                            // If the move is valid and the tile is empty, the monster moves down.
                            if (0 <= j + 1 && j + 1 < colSize && stageMap[i][j + 1] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i][j + 1] = Tile.MONSTER;
                            }
                        }
                        case 3 -> {
                            // If the move is valid and the tile is empty, the monster moves left.
                            if (0 <= i - 1 && i - 1 < colSize && stageMap[i - 1][j] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i - 1][j] = Tile.MONSTER;
                            }
                        }
                        default -> {
                            // The move is invalid, resulting in an illegal game state.
                            throw new IllegalStateException();
                        }
                    }
                }
            }
        }

        // Marks a tile with the player occupied.
        Tile occupied = stageMap[player.getPlayerY()][player.getPlayerX()];

        
        if (occupied == Tile.OBSTACLE || occupied == Tile.MONSTER) {
            // Player has collided with an obstacle or monster.
            player.die();
        } 
        if (occupied == Tile.GOAL) {
            // Player has reached the goal and is redirected to the leaderboards.
            gameWindow.stopStageTime();
            int finishTime = gameWindow.getElapsedTime();
            gameWindow.registerScore(difficulty, playerName, finishTime);
            JOptionPane.showMessageDialog(this, 
                "You win! You can see your score in the leaderboards after closing this message."
            );
            gameWindow.showLeaderboard();
        }
        if (player.isDead()) {
            // Player has died and is redirected to the stage selection screen.
            gameWindow.stopStageTime();
            JOptionPane.showMessageDialog(this, 
                "You died. You can choose a stage and try again after closing this message."
            );
            gameWindow.showStageSelectionScreen();
        }
        // Calls draw() to update the movement on the monsters on the JLabel (visually).
        draw();
    }

    /** Checks if the player can move to a cell on the stage.
     *  The player can move if:
     *  - it is within the boundaries of the stage map.
     *  - there is no wall that is blocking the player's movement direction.
     * 
     * @param x x-coordinate of the player (which row).
     * @param y y-coordinate of the player (which column).
     * @return true if the player can move or if the player is within bounds, false otherwise. 
     */
    public boolean canMoveTo(int x, int y) {
        boolean isInBounds = (0 <= x && x < rowSize) && (0 <= y && y < colSize);
        return isInBounds && stageMap[x][y] != Tile.WALL;
    }

    /** Creates keybinds for the movement methods.
     * 
     * @param name The name of the key pressed.
     * @param action The action of the key pressed.
     * @param keys The available keys to move the player.
     */
    public void register(String name, Runnable action, KeyStroke[] keys) {
        for (KeyStroke key: keys) {
            inputMap.put(key, name);
        }
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }
}