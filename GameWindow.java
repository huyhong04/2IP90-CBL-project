// All imports needed to run the game window.
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/** This generates the game window along with the functionality to
 *  move between pages in the game.
 *  The class contains the following pages:
 *  - Home screen (with buttons "New Game", "Leaderboard", and "Quit Game")
 *  - Name input screen (with name input of maximum 20 characters and button "Submit Player Name")
 *  - Stage selection screen (with buttons "Easy", "Medium", "Hard")
 *  - Stage screen (with corresponding chosen stage and buttons "Quit Stage" and "Quit Session")
 *  - Leaderboards screen (with records)
 */
public class GameWindow extends JFrame {

    private JFrame gameFrame; // Frame for the game

    private JPanel cardPanel; // Main panel

    private CardLayout card; // Card layout for the game window

    private Timer stageTime; // Timer object which represents a stopwatch during the stage

    private JLabel easyTimeLabel; // Label to display time taken to complete the 'Easy' stage

    private JLabel mediumTimeLabel; // Label to display time taken to complete the 'Easy' stage

    private JLabel hardTimeLabel; // Label to display time taken to complete the 'Easy' stage

    private final Map<GameStage.Difficulty, Map<String, List<Integer>>> scores = new HashMap(); 
    /* Creates a map of maps, where the key is the stage difficulty, 
     * and the value maps the player name to the corresponding finished stage time.
     */

    int elapsedTime; // Elapsed time in seconds (changing digit)

    int screenSizeHorizontal = 1280; // Game window screen (horizontal dimension)

    int screenSizeVertical = 720; // Game window screen (vertical dimension)

    String playerName; // Player name input

    PlayerMovement player; // Player

    /** We use a card layout to allow navigation between different panels
     *  in the game window.
     */
    public GameWindow() {
        card = new CardLayout();
        cardPanel = new JPanel(card); // Configure game panel with CardLayout
        gameFrame = new JFrame("Escapade"); // Set game title.
        gameFrame.setResizable(false); // Set fixed size window (no resize)
        gameFrame.setSize(screenSizeHorizontal, screenSizeVertical); // Set screen dimensions
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null); // Centering the game window to the computer screen
        
        // Returns empty hashmap instead of null to avoid error of showing multiple scores at once.
        for (GameStage.Difficulty difficulty : GameStage.Difficulty.values()) {
            scores.put(difficulty, new HashMap<>());
        }

        gameScreen(); // Calls the GUI components in the home screen
    }

    /** Configuring the home screen of the game.
     *  Setting the panels "New Stage", "Stage Selection", and "Leaderboard",
     *  where navigation to each panel will show only one panel at a time (by CardLayout).
     */
    public void gameScreen() {
        // Set layout of panel with the functions for each implemented button.
        cardPanel.add(homePanel(), "Home Screen");
        cardPanel.add(newStagePanel(), "New Stage");
        cardPanel.add(stageSelectionPanel(), "Stage Selection");
        cardPanel.add(easyStage(), "Easy");
        cardPanel.add(mediumStage(), "Medium");
        cardPanel.add(hardStage(), "Hard");

        card.show(cardPanel, "Home Screen");
        gameFrame.add(cardPanel);
        gameFrame.setVisible(true);
    }

    /** Customizing the home screen with buttons "New Stage", "Stage Selection",
     *  and "Leaderboard".
     *  @return homePanel (the home panel which is a JPanel).
     */
    private JPanel homePanel() {
        // Set and customize panel for the home screen.
        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(39, 49, 135));
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

        // Set message labels for the home screen.
        JLabel introMessage = new JLabel("Welcome to the game 'Escapade'!", SwingConstants.CENTER);
        JLabel introDescription = new JLabel(
            "Please select one of the buttons to advance further (or quit the game).",
            SwingConstants.CENTER
            ); 
        introMessage.setForeground(Color.WHITE);
        introMessage.setFont(new Font("Monospaced", Font.BOLD, 36));
        introMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        introDescription.setForeground(Color.WHITE);
        introDescription.setFont(new Font("Dialog", Font.PLAIN, 20));
        introDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set and customize buttons for the home screen.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(39, 49, 135));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JButton startNewStage = new JButton("New Stage");
        JButton playerLeaderboard = new JButton("Leaderboard");
        JButton quitGame = new JButton("Quit Game");

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(startNewStage);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(playerLeaderboard);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(quitGame);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(Box.createHorizontalGlue());

        // Adding all the implemented components of the home panel to the home screen.
        homePanel.add(introMessage);
        homePanel.add(introDescription);
        homePanel.add(Box.createVerticalGlue());
        homePanel.add(buttonPanel);
        homePanel.add(Box.createVerticalGlue());

        // Listener for the 'New Stage' button.
        startNewStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "New Stage");
            }
        });
        // Listener for the 'Leaderboard' button.
        playerLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLeaderboard();
            }
        });
        // Listener for the 'Quit Game' button.
        quitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quitResponse = JOptionPane.showConfirmDialog(GameWindow.this, 
                    "Are you sure you want to quit the game?", 
                    "Quit", 
                    JOptionPane.YES_NO_OPTION);
                if (quitResponse == JOptionPane.YES_OPTION) { // Quit if 'Yes' is chosen
                    System.exit(0);
                }
            }
        });

        return homePanel;
    }

    /** Customizing the name input screen with button "Submit Player Name".
     *  The name input screen contains a instruction message which says to input
     *  at most 20 characters in your player name and the name must be of String type.
     * 
     *  @return newStagePanel (the name input panel which is a JPanel).
     */
    private JPanel newStagePanel() {
        // Set and customize panel for the new stage screen.
        JPanel newStagePanel = new JPanel();
        BoxLayout newStageLayout = new BoxLayout(newStagePanel, BoxLayout.Y_AXIS);
        newStagePanel.setBackground(new Color(39, 76, 135));
        newStagePanel.setLayout(newStageLayout);

        // Set a description for the name input.
        JLabel nameInputLabel = new JLabel(
            "What's your name? Enter it here as a String with maximum 20 characters (incl. spaces)."
            );
        nameInputLabel.setForeground(Color.WHITE);
        nameInputLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        nameInputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set and customize the input box for the player name.
        JTextField nameField = new JTextField();
        Dimension nameFieldSize = new Dimension(150, 30);
        nameField.setMaximumSize(nameFieldSize);
        nameField.setPreferredSize(nameFieldSize);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Set and customizing buttons.
        JButton submitNameInput = new JButton("Submit Player Name");
        JButton backToHome = new JButton("Home Screen");
        submitNameInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToHome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adding button panel to player name input screen.
        newStagePanel.add(nameInputLabel);
        newStagePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        newStagePanel.add(nameField);
        newStagePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        newStagePanel.add(Box.createHorizontalGlue());
        newStagePanel.add(submitNameInput);
        newStagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        newStagePanel.add(backToHome);
        newStagePanel.add(Box.createHorizontalGlue());

        /* Checks the following cases:
        * - Name input can't be empty.
        * - Name length can't exceed 20 characters (including spaces).
        * - Name input must be of String type (uses regex to check this).
        */
        submitNameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameInput = nameField.getText().trim();

                if (nameInput.length() > 20) {
                    JOptionPane.showMessageDialog(
                        GameWindow.this,
                        "Player name can't exceed 20 characters.");
                } else if (!nameInput.matches("[a-zA-z\\s]+")) {
                    // Using a regex to check if player name is of String input and not empty.
                    JOptionPane.showMessageDialog(
                        GameWindow.this,
                        "Player name must be of String type and can't be empty.");
                } else {
                    playerName = nameInput;
                    card.show(cardPanel, "Stage Selection");
                }
            }
        });

        // Listener for the button which redirects to the home screen.
        backToHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "Home Screen");
            }
        });

        return newStagePanel;
    }

    /** Creates a label with the stage description.
     * 
     * @param desc The description.
     * @return stageExplanation, label with a description that describes each stage.
     */
    private JLabel stageExplanation(String desc) {
        JLabel stageExplanation = new JLabel(desc);
        stageExplanation.setForeground(Color.WHITE);
        stageExplanation.setFont(new Font("Dialog", Font.PLAIN, 16));
        stageExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return stageExplanation;
    }

    /** Customizing the stage selection screen with buttons "Easy", "Medium",
     *  and "Hard".
     * 
     *  @return stageSelectionPanel (the stage selection panel which is a JPanel).
     */
    public JPanel stageSelectionPanel() {
        // Set and customize panel for the stage selection screen
        JPanel stageSelectionPanel = new JPanel();
        stageSelectionPanel.setBackground(new Color(39, 105, 135));
        stageSelectionPanel.setLayout(new BoxLayout(stageSelectionPanel, BoxLayout.Y_AXIS));

        // General description for the stage selection screen.
        JLabel stageSelectionDesc = stageExplanation(
            "Select a stage. Note the possible features in the stages below:"
        );
        stageSelectionDesc.setFont(new Font("Monospaced", Font.BOLD, 20));

        // Set a description for each of the possible components in the map.
        JLabel wallExplanation = stageExplanation(
            "Walls: They stop you from moving in a certain direction and are immobile."
        );
        JLabel obstacleExplanation = stageExplanation(
            "Obstacles: They kill you and are immobile."
        );
        JLabel monsterExplanation = stageExplanation(
            "Monsters: They kill you and are mobile. They can also eat walls."
        );
        JLabel easyStageDesc = stageExplanation(
            "'Easy': There are walls and obstacles and few monsters. Map is of size 15px x 15px."
        );
        easyStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        JLabel mediumStageDesc = stageExplanation(
            "'Medium': There are walls, obstacles and more monsters. Map is of size 25px x 25px."
        );
        mediumStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        JLabel hardStageDesc = stageExplanation(
            "'Hard': There are walls, obstacles and many monsters. Map is of size 40px x 40px."
        );
        hardStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));

        // Set and customize buttons for the stage selection screen.
        JPanel buttonPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setBackground(new Color(39, 105, 135));
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        JButton buttonEasyStage = new JButton("Easy"); // 'Easy' stage
        JButton buttonMediumStage = new JButton("Medium"); // 'Medium' stage
        JButton buttonHardStage = new JButton("Hard"); // 'Hard' stage

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(buttonEasyStage);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(buttonMediumStage);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(buttonHardStage);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(Box.createHorizontalGlue());

        // Listener for the 'Easy' stage.
        buttonEasyStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startStageTime();
                cardPanel.add(easyStage(), "Easy");
                card.show(cardPanel, "Easy");
            }
        });
        // Listener for the 'Medium' stage.
        buttonMediumStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startStageTime();
                cardPanel.add(mediumStage(), "Medium");
                card.show(cardPanel, "Medium");
            }
        });
        // Listener for the 'Hard' stage.
        buttonHardStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startStageTime();
                cardPanel.add(hardStage(), "Hard");
                card.show(cardPanel, "Hard");
            }
        });

        // Add all created components to the stage selection screen.
        stageSelectionPanel.add(stageSelectionDesc);
        stageSelectionPanel.add(wallExplanation);
        stageSelectionPanel.add(obstacleExplanation);
        stageSelectionPanel.add(monsterExplanation);
        stageSelectionPanel.add(easyStageDesc);
        stageSelectionPanel.add(mediumStageDesc);
        stageSelectionPanel.add(hardStageDesc);
        stageSelectionPanel.add(Box.createVerticalGlue());
        stageSelectionPanel.add(buttonPanel);
        stageSelectionPanel.add(Box.createVerticalGlue());

        return stageSelectionPanel;
    }

    public void showStageSelectionScreen() {
        card.show(cardPanel, "Stage Selection");
    }

    /** Creates a button panel with the "Quit Stage", "Quit Game Session", and "Quit Game" buttons.
     * 
     * @return buttonPanel, a panel containing default buttons for each stage.
     */
    private JPanel stageButtonPanel() {
        // Set and customize buttons for the easy stage.
        JPanel buttonPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        JButton quitStage = new JButton("Quit Stage");
        JButton quitGameSession = new JButton("Quit Game Session");
        JButton quitGame = new JButton("Quit Game");

        // Adding buttons to the button panel.
        buttonPanel.add(quitStage);
        buttonPanel.add(quitGameSession);
        buttonPanel.add(quitGame);

        // Listener for the 'Quit Stage' button.
        quitStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Stage Selection");
            }
        });
        // Listener for the 'Quit Game Session' button.
        quitGameSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Home Screen");
            }
        });
        // Listener for the 'Quit Game' button.
        quitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quitResponse = JOptionPane.showConfirmDialog(GameWindow.this, 
                    "Are you sure you want to quit the game?", 
                    "Quit", 
                    JOptionPane.YES_NO_OPTION);
                if (quitResponse == JOptionPane.YES_OPTION) { // Quit if 'Yes' is chosen
                    System.exit(0);
                    stopStageTime();
                }
            }
        });
        return buttonPanel;
    }

    /** Formats a default description for each stage.
     * 
     * @param inStageDesc The in-stage description.
     * @return stageDesc, formatting the in-stage description in the corresponding stage screen.
     */
    private JLabel inStageText(String inStageDesc) {
        // Set the in-stage description.
        JLabel stageDesc = new JLabel(inStageDesc);
        stageDesc.setForeground(Color.WHITE);
        stageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        stageDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        return stageDesc;
    }

    /** Sets a default time label when entering a stage.
     * 
     * @return timeLabel, the elapsed time label which shows the duration of being in the stage.
     */
    private JLabel timeLabel() {
        // Set stopwatch for the stage.
        JLabel timeLabel = new JLabel(" Elapsed Time:  0s");
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
        timeLabel.setAlignmentY(BOTTOM_ALIGNMENT);

        return timeLabel;
    }

    // Method to get the elapsed time.
    public int getElapsedTime() {
        return elapsedTime;
    }

    /** Formatting and adding all prior components (via methods) into the 'Easy' stage screen.
     * 
     * @return easyStagePanel, the screen for the 'Easy' stage.
     */
    private JPanel easyStage() {
        // Set panel for the 'Easy' stage window.
        JPanel easyStagePanel = new JPanel();
        easyStagePanel.setBackground(new Color(105, 161, 96));
        easyStagePanel.setLayout(new BoxLayout(easyStagePanel, BoxLayout.Y_AXIS));

        // Set the 'Easy' stage description.
        JLabel easyStageDesc = inStageText(
            " You are playing the 'Easy' stage, where the player is denoted by a blue square. The spawn point is denoted by a black square."
        );

        // Set the descriptions for the game elements.
        JLabel gameGoal = inStageText(
            " You have to reach the goal as fast as possible, denoted by a green square in the map."
        );
        JLabel wallDesc = inStageText(
            " Walls are denoted by brown squares."
        );
        JLabel obstacleDesc = inStageText(
            " Obstacles are denoted by yellow squares."
        );
        JLabel monsterDesc = inStageText(
            " Monsters are denoted by red squares."
        );


        // Set a good luck message.
        JLabel goodLuckMessage = inStageText(
            " If the stage results in a dead end, please restart the stage. Good luck!"
        );
        goodLuckMessage.setFont(new Font("Dialog", Font.PLAIN, 18));
        
        // Set the stopwatch.
        easyTimeLabel = timeLabel();

        // Adding stage map
        GameStage easyStageMap = new GameStage(this, GameStage.Difficulty.EASY);
        easyStageMap.setAlignmentX(Component.CENTER_ALIGNMENT);
        easyStageMap.setAlignmentY(Component.CENTER_ALIGNMENT);
        easyStageMap.setPlayerName(playerName);

        // Adding a default button panel
        JPanel easyStageButtonPanel = stageButtonPanel();
        easyStageButtonPanel.setBackground(new Color(105, 161, 96));

        // Alignment of text labels
        JPanel labelPanel1 = new JPanel();
        labelPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel1.setBackground(new Color(105, 161, 96));
        JPanel labelPanel2 = new JPanel();
        labelPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel2.setBackground(new Color(105, 161, 96));
        labelPanel1.add(easyStageDesc);
        labelPanel1.add(easyStageButtonPanel);
        labelPanel2.add(gameGoal);
        labelPanel2.add(wallDesc);
        labelPanel2.add(obstacleDesc);
        labelPanel2.add(monsterDesc);
        labelPanel2.add(goodLuckMessage);
        labelPanel2.add(easyTimeLabel);

        // Adding all the implemented components to the 'Easy' stage screen.
        easyStagePanel.add(labelPanel1);
        easyStagePanel.add(easyStageMap);
        easyStagePanel.add(labelPanel2);

        return easyStagePanel;
    }

    /** Formatting and adding all prior components (via methods) into the 'Medium' stage screen.
     * 
     * @return mediumStagePanel, the screen for the 'Medium' stage.
     */
    private JPanel mediumStage() {
        // Set panel for the 'Medium' stage window.
        JPanel mediumStagePanel = new JPanel();
        mediumStagePanel.setBackground(new Color(161, 147, 96));
        mediumStagePanel.setLayout(new BoxLayout(mediumStagePanel, BoxLayout.Y_AXIS));

        // Set the 'Medium' stage description.
        JLabel mediumStageDesc = inStageText(
            " You are playing the 'Medium' stage, where the player is denoted by a blue square. The spawn point is denoted by a black square."
        );

        // Set the descriptions for the game elements.
        JLabel gameGoal = inStageText(
            " You have to reach the goal as fast as possible, denoted by a green square in the map."
        );
        JLabel wallDesc = inStageText(
            " Walls are denoted by brown squares."
        );
        JLabel obstacleDesc = inStageText(
            " Obstacles are denoted by yellow squares."
        );
        JLabel monsterDesc = inStageText(
            " Monsters are denoted by red squares."
        );

        // Set a good luck message.
        JLabel goodLuckMessage = inStageText(
            " If the stage results in a dead end, please restart the stage. Good luck!"
        );
        goodLuckMessage.setFont(new Font("Dialog", Font.PLAIN, 18));

        // Set stopwatch.
        mediumTimeLabel = timeLabel();

        // Adding stage map
        GameStage mediumStageMap = new GameStage(this, GameStage.Difficulty.MEDIUM);
        mediumStageMap.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumStageMap.setAlignmentY(Component.CENTER_ALIGNMENT);
        mediumStageMap.setPlayerName(playerName);

        // Adding a default button panel
        JPanel mediumStageButtonPanel = stageButtonPanel();
        mediumStageButtonPanel.setBackground(new Color(161, 147, 96));

        // Alignment of text labels
        JPanel labelPanel1 = new JPanel();
        labelPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel1.setBackground(new Color(161, 147, 96));
        JPanel labelPanel2 = new JPanel();
        labelPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel2.setBackground(new Color(161, 147, 96));
        labelPanel1.add(mediumStageDesc);
        labelPanel1.add(mediumStageButtonPanel);
        labelPanel2.add(gameGoal);
        labelPanel2.add(wallDesc);
        labelPanel2.add(obstacleDesc);
        labelPanel2.add(monsterDesc);
        labelPanel2.add(goodLuckMessage);
        labelPanel2.add(mediumTimeLabel);

        // Adding all the implemented components to the 'Medium' stage screen.
        mediumStagePanel.add(labelPanel1);
        mediumStagePanel.add(mediumStageMap);
        mediumStagePanel.add(labelPanel2);
        
        // SwingUtilities.invokeLater(() -> mediumStageMap.requestFocusInWindow());

        return mediumStagePanel;
    }

    /** Formatting and adding all prior components (via methods) into the 'Hard' stage screen.
     * 
     * @return hardStagePanel, the screen for the 'Hard' stage.
     */
    private JPanel hardStage() {
        // Set the panel for the 'Hard' stage window.
        JPanel hardStagePanel = new JPanel();
        hardStagePanel.setBackground(new Color(161, 96, 96));
        hardStagePanel.setLayout(new BoxLayout(hardStagePanel, BoxLayout.Y_AXIS));

        // Set the 'Hard' stage description.
        JLabel hardStageDesc = inStageText(
            " You are playing the 'Hard' stage, where the player is denoted by a blue square. The spawn point is denoted by a black square."
        );

        // Set the descriptions for the game elements.
        JLabel gameGoal = inStageText(
            " You have to reach the goal as fast as possible, denoted by a green square in the map."
        );
        JLabel wallDesc = inStageText(
            " Walls are denoted by brown squares."
        );
        JLabel obstacleDesc = inStageText(
            " Obstacles are denoted by yellow squares."
        );
        JLabel monsterDesc = inStageText(
            " Monsters are denoted by red squares."
        );

        // Set a good luck message.
        JLabel goodLuckMessage = inStageText(
            " If the stage results in a dead end, please restart the stage. Good luck!"
        );
        goodLuckMessage.setFont(new Font("Dialog", Font.PLAIN, 18));        

        // Set the stopwatch.
        hardTimeLabel = timeLabel();

        // Adding stage map
        GameStage hardStageMap = new GameStage(this, GameStage.Difficulty.HARD);
        hardStageMap.setAlignmentX(Component.CENTER_ALIGNMENT);
        hardStageMap.setAlignmentY(Component.CENTER_ALIGNMENT);
        hardStageMap.setPlayerName(playerName);

        // Adding a default button panel
        JPanel hardStageButtonPanel = stageButtonPanel();
        hardStageButtonPanel.setBackground(new Color(161, 96, 96));

        // Alignment of text labels
        JPanel labelPanel1 = new JPanel();
        labelPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel1.setBackground(new Color(161, 96, 96));
        JPanel labelPanel2 = new JPanel();
        labelPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel2.setBackground(new Color(161, 96, 96));
        labelPanel1.add(hardStageDesc);
        labelPanel1.add(hardStageButtonPanel);
        labelPanel2.add(gameGoal);
        labelPanel2.add(wallDesc);
        labelPanel2.add(obstacleDesc);
        labelPanel2.add(monsterDesc);
        labelPanel2.add(goodLuckMessage);
        labelPanel2.add(hardTimeLabel);

        // Adding all the implemented components to the 'Hard' stage screen.
        hardStagePanel.add(labelPanel1);
        hardStagePanel.add(hardStageMap);
        hardStagePanel.add(labelPanel2);
        
        return hardStagePanel;
    }

    /** Starts the stopwatch when the listeners for the 'Easy', 'Medium', and 'Hard'
     *  stage screens are activated (the corresponding respective buttons are clicked).
     */
    public void startStageTime() {
        // Checks if there is already a stopwatch running.
        if (stageTime != null && stageTime.isRunning()) {
            stageTime.stop();
        }

        elapsedTime = 0; // Reset stopwatch every time a stage screen is opened.
        easyTimeLabel.setText(" Elapsed Time:  0s");
        mediumTimeLabel.setText(" Elapsed Time:  0s");
        hardTimeLabel.setText(" Elapsed Time:  0s");

        stageTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                easyTimeLabel.setText(" Elapsed Time:  " + elapsedTime + "s");
                mediumTimeLabel.setText(" Elapsed Time:  " + elapsedTime + "s");
                hardTimeLabel.setText(" Elapsed Time:  " + elapsedTime + "s");
            }
        });

        stageTime.start();
    }

    /** Stops the stopwatch when the listeners for the 'Quit Stage', 'Quit Game Session', 
     *  and 'Quit Game' stage screens are activated 
     *  (the corresponding respective buttons are clicked).
     */
    public void stopStageTime() {
        if (stageTime != null && stageTime.isRunning()) {
            stageTime.stop();
        }
    }

    /** Creates a pair for comparison with player time to sort in the leaderboards.
     */
    private static class Pair<A, B> {
        public A a;
        public B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    /** Put the stage difficulty, inputted player name, and the finished stage time in a map.
     * 
     * @param difficulty The stage with the corresponding difficulty.
     * @param playerName The inputted player name.
     * @param score The score (finished stage time).
     */
    public void registerScore(GameStage.Difficulty difficulty, String playerName, int score) {
        Map<String, List<Integer>> playerFinishTimes = scores.getOrDefault(difficulty, 
            new HashMap<>());
        List<Integer> finishTimes = playerFinishTimes.getOrDefault(playerName, new ArrayList<>());
        finishTimes.add(score);
        playerFinishTimes.put(playerName, finishTimes);
        scores.put(difficulty, playerFinishTimes);
    }

    /** Flattens two maps into one, where the first map contains the stage difficulty,
     *  and the second contains an inputted player name mapped with the corresponding finished
     *  stage time.
     *  After doing so, return a new pair with only the inputted player name and the corresponding
     *  finished stage time, where we compare to see which one has the lower finished stage time. 
     * 
     * @param difficulty The stage with the corresponding difficulty.
     * @return An integer which indicates whether the player time with 
     *      the corresponding player name is lower than the other or not.
     */
    public List<Pair<String, Integer>> getScores(GameStage.Difficulty difficulty) {
        return scores.get(difficulty).entrySet().stream().flatMap(entry -> {
            return entry.getValue().stream().map(value -> {
                return new Pair<>(entry.getKey(), value);
            });
        }).sorted(new Comparator<Pair<String, Integer>>() {

            @Override
            public int compare(GameWindow.Pair<String, Integer> o1, 
                GameWindow.Pair<String, Integer> o2) {
                return o1.b.compareTo(o2.b);
            }
           
        }).toList();
    }

    /** Displays the leaderboard, where it shows 10 stage plays with: 
     * - the lowest finished stage time,
     * - the stage difficulty,
     * - inputted player name.
     * 
     * @return leaderboardPanel, the leaderboard screen.
     */
    private JPanel leaderboardPanel() {
        // Set the panel for the 'Leaderboard' window.
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setBackground(new Color(39, 92, 135));
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));

        // Set the 'Leaderboard' description.
        JLabel leaderboardText = inStageText(
            "Welcome to the leaderboard! The lowest 5 finish times for each difficulty are shown."
            );
        leaderboardText.setAlignmentX(CENTER_ALIGNMENT);
        leaderboardText.setFont(new Font("Monospaced", Font.BOLD, 16));

        // Set a button which redirects the player to the home screen.
        JButton backToHome = new JButton("Home Screen");
        backToHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToHome.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Add the text to the leaderboard panel.
        leaderboardPanel.add(leaderboardText);
        leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        /* Generate sections for stage difficulty, where the inputted player name
         * and finished stage time is shown in order of the time.
         */
        for (GameStage.Difficulty difficulty : GameStage.Difficulty.values()) {
            JLabel difficultyLabel = new JLabel("Stage Difficulty: " + difficulty);
            difficultyLabel.setForeground(Color.WHITE);
            difficultyLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
            difficultyLabel.setAlignmentX(CENTER_ALIGNMENT);
            leaderboardPanel.add(difficultyLabel);

            List<Pair<String, Integer>> scores = getScores(difficulty);
            int leaderboardDisplay = Math.min(5, scores.size());

            /* Add the pair of the inputted player name and the finished stage time
             * into the leaderboard panel.
             */
            for (int rank = 0; rank < leaderboardDisplay; rank++) {
                Pair<String, Integer> scoreEntry = scores.get(rank);
                String playerName = scoreEntry.a;
                int finishTime = scoreEntry.b;

                JLabel scoreLabel = new JLabel((rank + 1) + ". " + playerName 
                    + " : " + finishTime + "s");
                scoreLabel.setForeground(Color.WHITE);
                scoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
                scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
                leaderboardPanel.add(scoreLabel);

                // New updates to the leaderboard panel whenever there is a new play.
                leaderboardPanel.revalidate();
            }
        }

        // Add home button
        leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        leaderboardPanel.add(backToHome);

        // Adding listener to the button which redirects the player to the home screen.
        backToHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "Home Screen");
            }
        });

        return leaderboardPanel; // Return the leaderboard panel.
    }

    /** Redirects to the leaderboard.
     */
    public void showLeaderboard() {
        cardPanel.add(leaderboardPanel(), "Leaderboard");
        card.show(cardPanel, "Leaderboard");
    }
    
    /** Run the game window with according GUI components.
     * 
     * @param a All arguments
     */
    public static void main(String[] a) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}