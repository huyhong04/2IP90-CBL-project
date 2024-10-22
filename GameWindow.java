import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private JFrame gameFrame;
    private CardLayout card; // Card layout for the game window
    private JPanel cardPanel; // Main panel
    private Timer easyStageTime; // Timer object for the 'Easy' stage
    private Timer mediumStageTime; // Timer object for the 'Medium' stage
    private Timer hardStageTime; // Timer object for the 'Hard' stage
    private JLabel timeLabel; // Label to display time taken to complete the stage
    int elapsedTime; // Elapsed time in seconds

    int screenSizeHorizontal = 1080; // Screen window dimensions (horizontal)
    int screenSizeVertical = 720; // Screen window dimensions (vertical)
    String playerName; // For name input


    /** Setting the game window, we use a card layout to allow navigation
     *  between different panels.
     */
    public GameWindow() {
        card = new CardLayout();
        cardPanel = new JPanel(card); // Configure game panel with CardLayout
        gameFrame = new JFrame("Escapade"); // Set game title.
        gameFrame.setResizable(false); // Set fixed size window (no resize)
        gameFrame.setSize(screenSizeHorizontal, screenSizeVertical); // Set screen dimensions
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null); // Centering the game window to the computer screen

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
        cardPanel.add(leaderboardPanel(), "Leaderboard");
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
        BoxLayout homeLayout = new BoxLayout(homePanel, BoxLayout.Y_AXIS);
        homePanel.setBackground(new Color(39, 49, 135));
        homePanel.setLayout(homeLayout);

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
        BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setBackground(new Color(39, 49, 135));
        buttonPanel.setLayout(buttonLayout);
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

        // Implement listeners for each button
        // Listener for the 'New Stage' button
        startNewStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "New Stage");
            }
        });
        // Listener for the 'Leaderboard' button
        playerLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "Leaderboard");
            }
        });
        // Listener for the 'Quit Game' button
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
     * 
     *  @return newStagePanel (the name input panel which is a JPanel).
     */
    private JPanel newStagePanel() {
        // Set and customize panel for the new stage screen
        JPanel newStagePanel = new JPanel();
        BoxLayout newStageLayout = new BoxLayout(newStagePanel, BoxLayout.Y_AXIS);
        newStagePanel.setBackground(new Color(39, 76, 135));
        newStagePanel.setLayout(newStageLayout);

        // Set a description for the name input
        JLabel nameInputLabel = new JLabel(
            "What's your name? Enter it here as a String with maximum 20 characters (incl. spaces)."
            );
        nameInputLabel.setForeground(Color.WHITE);
        nameInputLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        nameInputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set and customize the input box for the player name
        JTextField nameField = new JTextField();
        Dimension nameFieldSize = new Dimension(150, 30);
        nameField.setMaximumSize(nameFieldSize);
        nameField.setPreferredSize(nameFieldSize);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Set and customizing buttons
        JButton submitNameInput = new JButton("Submit Player Name");
        JButton backToHome = new JButton("Home Screen");
        submitNameInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToHome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adding button panel to player name input screen
        newStagePanel.add(nameInputLabel);
        newStagePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        newStagePanel.add(nameField);
        newStagePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        newStagePanel.add(Box.createHorizontalGlue());
        newStagePanel.add(submitNameInput);
        newStagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        newStagePanel.add(backToHome);
        newStagePanel.add(Box.createHorizontalGlue());

        // Checks the following cases:
        // - Name input can't be empty.
        // - Name length can't exceed 20 characters (including spaces).
        // - Name input must be of String type (uses regex to check this).
        submitNameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameInput = nameField.getText().trim();

                if (nameInput.length() > 20) {
                    JOptionPane.showMessageDialog(
                        GameWindow.this,
                        "Player name can't exceed 20 characters.");
                } else if (!nameInput.matches("[a-zA-z\\s]+")) {
                    // Using a regex to check if player name is of String input and if string empty.
                    JOptionPane.showMessageDialog(
                        GameWindow.this,
                        "Player name must be of String type and can't be empty.");
                } else {
                    playerName = nameInput;
                    card.show(cardPanel, "Stage Selection");
                }
            }
        });

        // Adding listener to the button which redirects the player to the home screen.
        backToHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "Home Screen");
            }
        });

        return newStagePanel;
    }

    /** Customizing the stage selection screen with buttons "Easy", "Medium",
     *  and "Hard".
     * 
     *  @return stageSelectionPanel (the stage selection panel which is a JPanel).
     */
    private JPanel stageSelectionPanel() {
        // Set and customize panel for the stage selection screen
        JPanel stageSelectionPanel = new JPanel();
        BoxLayout stageSelectionLayout = new BoxLayout(stageSelectionPanel, BoxLayout.Y_AXIS);
        stageSelectionPanel.setBackground(new Color(39, 105, 135));
        stageSelectionPanel.setLayout(stageSelectionLayout);

        // Set a description of each stage.
        // General description for the stage selection screen.
        JLabel stageSelectionDesc = new JLabel(
            "Select a stage. Note the possible features in the stages below:"
            );
        stageSelectionDesc.setForeground(Color.WHITE);
        stageSelectionDesc.setFont(new Font("Monospaced", Font.BOLD, 20));
        stageSelectionDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel wallExplanation = new JLabel(
            "Walls: They stop you from moving in a certain direction and are immobile."
            );
        wallExplanation.setForeground(Color.WHITE);
        wallExplanation.setFont(new Font("Dialog", Font.PLAIN, 16));
        wallExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel obstacleExplanation = new JLabel(
            "Obstacles: They kill you and are immobile."
        );
        obstacleExplanation.setForeground(Color.WHITE);
        obstacleExplanation.setFont(new Font("Dialog", Font.PLAIN, 16));
        obstacleExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel monsterExplanation = new JLabel(
            "Monsters: They kill you and are mobile."
        );
        monsterExplanation.setForeground(Color.WHITE);
        monsterExplanation.setFont(new Font("Dialog", Font.PLAIN, 16));
        monsterExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel easyStageDesc = new JLabel(
            "'Easy': there are walls and obstacles and no monsters. Map is of size 20px x 20px."
        );
        easyStageDesc.setForeground(Color.WHITE);
        easyStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        easyStageDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mediumStageDesc = new JLabel(
            "'Medium': there are walls, obstacles and few monsters. Map is of size 40px x 40px."
        );
        mediumStageDesc.setForeground(Color.WHITE);
        mediumStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        mediumStageDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hardStageDesc = new JLabel(
            "'Hard': there are walls, obstacles and many monsters. Map is of size 60px x 60px."
        );
        hardStageDesc.setForeground(Color.WHITE);
        hardStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        hardStageDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set and customize buttons for the stage selection screen.
        JPanel buttonPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setBackground(new Color(39, 105, 135));
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        JButton buttonEasyStage = new JButton("Easy"); // Stage 1
        JButton buttonMediumStage = new JButton("Medium"); // Stage 2
        JButton buttonHardStage = new JButton("Hard"); // Stage 3

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(buttonEasyStage);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(buttonMediumStage);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(buttonHardStage);
        buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonPanel.add(Box.createHorizontalGlue());

        // Add listeners for each buttton (for each stage)
        buttonEasyStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startEasyStageTime();
                card.show(cardPanel, "Easy");
            }
        });

        buttonMediumStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startMediumStageTime();
                card.show(cardPanel, "Medium");
            }
        });

        buttonHardStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startHardStageTime();
                card.show(cardPanel, "Hard");
            }
        });

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

    private JPanel easyStage() {
        // Set panel for the 'Easy' stage window
        JPanel easyStagePanel = new JPanel();
        BoxLayout easyStageLayout = new BoxLayout(easyStagePanel, BoxLayout.Y_AXIS);
        easyStagePanel.setBackground(new Color(105, 161, 96));
        easyStagePanel.setLayout(easyStageLayout);

        // 'Easy' stage description
        JLabel easyStageDesc = new JLabel(
            "'Easy': there are walls and obstacles and no monsters. Map is of size 20px x 20px."
        );
        easyStageDesc.setForeground(Color.WHITE);
        easyStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        easyStageDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Set stopwatch
        timeLabel = new JLabel(" Elapsed Time:  0s ");
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
        timeLabel.setAlignmentY(BOTTOM_ALIGNMENT);

        // Set and customize buttons for the easy stage.
        JPanel buttonPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
        buttonPanel.setBackground(new Color(105, 161, 96));
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

        // Adding all the implemented components of the home panel to the home screen.
        easyStagePanel.add(easyStageDesc);
        easyStagePanel.add(buttonPanel);
        easyStagePanel.add(timeLabel);

        // Listener for the 'Quit Stage' button
        quitStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Stage Selection");
            }
        });

        // Listener for the 'Quit Game Session' button
        quitGameSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Home Screen");
            }
        });

        // Listener for the 'Quit Game' button
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

        return easyStagePanel;
    }

    private JPanel mediumStage() {
        // Set panel for the 'Medium' stage window
        JPanel mediumStagePanel = new JPanel();
        BoxLayout mediumStageLayout = new BoxLayout(mediumStagePanel, BoxLayout.Y_AXIS);
        mediumStagePanel.setBackground(new Color(161, 147, 96));
        mediumStagePanel.setLayout(mediumStageLayout);

        // 'Medium' stage description
        JLabel mediumStageDesc = new JLabel(
            "'Medium': there are walls, obstacles and few monsters. Map is of size 40px x 40px."
        );
        mediumStageDesc.setForeground(Color.WHITE);
        mediumStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        mediumStageDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Set stopwatch
        timeLabel = new JLabel(" Elapsed Time:  0s ");
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
        timeLabel.setAlignmentY(BOTTOM_ALIGNMENT);

        // Set and customize buttons for the medium stage.
        JPanel buttonPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
        buttonPanel.setBackground(new Color(161, 147, 96));
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

        // Adding all the implemented components of the home panel to the home screen.
        mediumStagePanel.add(mediumStageDesc);
        mediumStagePanel.add(buttonPanel);
        mediumStagePanel.add(timeLabel);

        // Listener for the 'Quit Stage' button
        quitStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Stage Selection");
            }
        });

        // Listener for the 'Quit Game Session' button
        quitGameSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Home Screen");
            }
        });

        // Listener for the 'Quit Game' button
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
        
        return mediumStagePanel;
    }

    private JPanel hardStage() {
        // Set panel for the 'Hard' stage window
        JPanel hardStagePanel = new JPanel();
        BoxLayout hardStageLayout = new BoxLayout(hardStagePanel, BoxLayout.Y_AXIS);
        hardStagePanel.setBackground(new Color(161, 96, 96));
        hardStagePanel.setLayout(hardStageLayout);

        // 'Hard' stage description
        JLabel hardStageDesc = new JLabel(
            "'Hard': there are walls, obstacles and many monsters. Map is of size 60px x 60px."
        );
        hardStageDesc.setForeground(Color.WHITE);
        hardStageDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        hardStageDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Set stopwatch
        timeLabel = new JLabel(" Elapsed Time:  0s ");
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("DialogInput", Font.BOLD, 14));
        timeLabel.setAlignmentY(BOTTOM_ALIGNMENT);

        // Set and customize buttons for the medium stage.
        JPanel buttonPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout(FlowLayout.LEFT, 10, 10);
        buttonPanel.setBackground(new Color(161, 96, 96));
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

        // Adding all the implemented components of the home panel to the home screen.
        hardStagePanel.add(hardStageDesc);
        hardStagePanel.add(buttonPanel);
        hardStagePanel.add(timeLabel);

        // Listener for the 'Quit Stage' button
        quitStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Stage Selection");
            }
        });

        // Listener for the 'Quit Game Session' button
        quitGameSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStageTime();
                card.show(cardPanel, "Home Screen");
            }
        });

        // Listener for the 'Quit Game' button
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
        
        return hardStagePanel;
    }

    private void startEasyStageTime() {
        if (easyStageTime != null && easyStageTime.isRunning()) {
            easyStageTime.stop(); // Stop any previously run stopwatch
        }

        elapsedTime = 0; // Reset stopwatch every time the stage screen is opened.
        timeLabel.setText(" Elapsed Time:  0s ");

        easyStageTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timeLabel.setText(" Elapsed Time:  " + elapsedTime + "s ");
            }
        });

        easyStageTime.start();
    }

    private void startMediumStageTime() {
        if (mediumStageTime != null && mediumStageTime.isRunning()) {
            mediumStageTime.stop(); // Stop any previously run stopwatch
        }

        elapsedTime = 0; // Reset stopwatch every time the stage screen is opened.
        timeLabel.setText(" Elapsed Time:  0s ");

        mediumStageTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timeLabel.setText(" Elapsed Time:  " + elapsedTime + "s ");
            }
        });

        mediumStageTime.start();
    }

    private void startHardStageTime() {
        if (hardStageTime != null && hardStageTime.isRunning()) {
            hardStageTime.stop(); // Stop any previously run stopwatch
        }

        elapsedTime = 0; // Reset stopwatch every time the stage screen is opened.
        timeLabel.setText(" Elapsed Time:  0s ");

        hardStageTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timeLabel.setText(" Elapsed Time:  " + elapsedTime + "s ");
            }
        });

        hardStageTime.start();
    }

    private void stopStageTime() {
        if (easyStageTime != null && easyStageTime.isRunning()) {
            easyStageTime.stop();
        }
        if (mediumStageTime != null && mediumStageTime.isRunning()) {
            mediumStageTime.stop();
        }
        if (hardStageTime != null && hardStageTime.isRunning()) {
            hardStageTime.stop();
        }
    }

    private JPanel leaderboardPanel() {
        JPanel leaderboardPanel = new JPanel();
        BoxLayout leaderboardLayout = new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS);
        leaderboardPanel.setBackground(new Color(39, 92, 135));
        leaderboardPanel.setLayout(leaderboardLayout);

        JButton backToHome = new JButton("Home Screen");
        backToHome.setAlignmentX(Component.CENTER_ALIGNMENT);

        leaderboardPanel.add(backToHome);

        // Adding listener to the button which redirects the player to the home screen.
        backToHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "Home Screen");
            }
        });

        return leaderboardPanel;
    }
    
    public static void main(String[] a) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}