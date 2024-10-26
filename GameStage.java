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

    private static enum Tile {
        EMPTY, WALL, OBSTACLE, MONSTER, GOAL, START
    }

    public static enum Difficulty {
        EASY, MEDIUM, HARD
    }
    private InputMap inputMap;
    private ActionMap actionMap;

    private Difficulty difficulty;
    private int rowSize; // Number of rows in the stage map

    private int colSize; // Number of columns in the stage map

    private Tile[][] stageMap; // A 2D array to represent the cells in the stage map

    private JLabel[][] graphics;

    private PlayerMovement player;

    private GameWindow gameWindow;

    private String playerName;

    public boolean canMoveTo(int x, int y) {
        // Is In Bounds
        boolean isInBounds = (0 <= x && x < rowSize) && (0 <= y && y < colSize);
        return isInBounds && stageMap[x][y] != Tile.WALL;
    }
    public void draw() {
        for (int i = 0; i < rowSize; i ++) {
            for (int j = 0; j < colSize; j++) {
                switch (stageMap[i][j]) {
                    case EMPTY -> {
                        graphics[i][j].setBackground(Color.WHITE);
                    }
                    case MONSTER -> {
                        graphics[i][j].setBackground(Color.RED);
                    }
                    case OBSTACLE -> {
                        graphics[i][j].setBackground(Color.YELLOW);
                    }
                    case GOAL -> {
                        graphics[i][j].setBackground(Color.GREEN);
                    }
                    case START -> {
                        graphics[i][j].setBackground(Color.BLACK);
                    }
                    case WALL -> {
                        graphics[i][j].setBackground(new Color(84, 61, 43));
                    }
                }
            }
        }
        graphics[player.getPlayerY()][player.getPlayerX()].setBackground(Color.BLUE);
        revalidate();
    }

    public void tick() {
        Random random = new Random();
        
        // Do the monster shuffle
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (stageMap[i][j] != Tile.MONSTER) {
                    continue;
                } else {
                    int move = random.nextInt(4);
                    switch (move) {
                        case 0 -> {
                            if (0 <= j - 1 && j - 1 < colSize && stageMap[i][j - 1] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i][j - 1] = Tile.MONSTER;
                            }
                        }
                        case 1 -> {
                            if (0 <= i + 1 && i + 1 < colSize && stageMap[i + 1][j] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i + 1][j] = Tile.MONSTER;
                            }
                        }
                        case 2 -> {
                            if (0 <= j + 1 && j + 1 < colSize && stageMap[i][j + 1] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i][j + 1] = Tile.MONSTER;
                            }
                        }
                        case 3 -> {
                            if (0 <= i - 1 && i - 1 < colSize && stageMap[i - 1][j] != Tile.GOAL) {
                                stageMap[i][j] = Tile.EMPTY;
                                stageMap[i - 1][j] = Tile.MONSTER;
                            }
                        }
                        default -> {
                            throw new IllegalStateException();
                        }
                    }
                }
            }
        }

        Tile occupied = stageMap[player.getPlayerY()][player.getPlayerX()];

        if (occupied == Tile.OBSTACLE || occupied == Tile.MONSTER) {
            player.die();
        } 
        if (occupied == Tile.GOAL) {
            gameWindow.stopStageTime();
            int finishTime = gameWindow.getElapsedTime();
            gameWindow.registerScore(difficulty, playerName, finishTime);
            JOptionPane.showMessageDialog(this, 
            "You win! You can see your score in the leaderboards after closing this message."
            );
            gameWindow.showLeaderboard();
        }
        if (player.isDead()) {
            gameWindow.stopStageTime();
            JOptionPane.showMessageDialog(this, 
            "You died. You can choose a stage and try again after closing this message."
            );
            gameWindow.showStageSelectionScreen();
        }
        draw();
    }

    /** Create a layout for the stage map.
     * 
     * @param rowSize The number of rows in the stage map.
     * @param colSize The number of columns in the stage map.
     */
    public GameStage(GameWindow gameWindow, Difficulty difficulty) {
        this.gameWindow = gameWindow;
        this.difficulty = difficulty;
        switch (difficulty) {
            case EASY -> {
                rowSize = 10;
                colSize = 10;
            } 
            case MEDIUM -> {
                rowSize = 20;
                colSize = 20;
            }
            case HARD -> {
                rowSize = 30;
                colSize = 30;
            }
        }

        this.inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.actionMap = this.getActionMap();

        this.setLayout(new GridLayout(rowSize, colSize));

        // Initialize the player at the start of the stage map
        player = new PlayerMovement(0, 0, this);

        // Initialise Array with nothing in it (Tile.EMPTY)
        this.stageMap = new Tile[rowSize][colSize]; // Initializes the stage map
        this.graphics = new JLabel[rowSize][colSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                graphics[i][j] = new JLabel();
                graphics[i][j].setBackground(Color.black);
                graphics[i][j].setOpaque(true);
                graphics[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(graphics[i][j]);
            }
        }
        generateMap();
        
        draw();

        this.setFocusable(true);

        register("UP", () -> player.moveUp(),
                new KeyStroke[] {KeyStroke.getKeyStroke("W")});
        register("DOWN", () -> player.moveDown(),
            new KeyStroke[] {KeyStroke.getKeyStroke("S")});
        register("LEFT", () -> player.moveLeft(),
            new KeyStroke[] {KeyStroke.getKeyStroke("A")});
        register("RIGHT", () -> player.moveRight(),
            new KeyStroke[] {KeyStroke.getKeyStroke("D")});

        setPreferredSize(new Dimension(400, 400));
    }

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

    public void generateMap() {
        double scaling = (double) difficulty.ordinal();
        double wallWeight = 1.5 * (scaling + 1.0);
        double obstacleWeight = 1.5 * (scaling + 1.0);
        double monsterWeight = 3 * (scaling);
        double emptyWeight = wallWeight + obstacleWeight + monsterWeight;

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
        // Player spawns at 0, 0
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                double randomWeight = random.nextDouble() * maxWeight;
                stageMap[i][j] = treeMap.ceilingEntry(randomWeight).getValue();
            }
        }
        stageMap[0][0] = Tile.START;
        stageMap[rowSize - 1][colSize - 1] = Tile.GOAL;
    }
}