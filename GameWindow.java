import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {

    private JFrame gameFrame;
    private CardLayout card;
    private JPanel cardPanel;
    int SCREENSIZE_HOR = 1080;
    int SCREENSIZE_VER = 720;
    String playerName;

    public GameWindow() {
        card = new CardLayout();
        cardPanel = new JPanel(card);
        gameFrame = new JFrame("Game Title");
        gameFrame.setResizable(false);
        gameFrame.setSize(SCREENSIZE_HOR, SCREENSIZE_VER);
        gameFrame.setDefaultCloseOperation(gameFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);

        homeScreen();
    }

    public void homeScreen() {
        // Set layout of panel
        cardPanel.add(homePanel(), "Home Screen");
        cardPanel.add(newStagePanel(), "New Stage");
        cardPanel.add(levelSelectionPanel(), "Level Selection");
        cardPanel.add(leaderboardPanel(), "Leaderboard");

        card.show(cardPanel, "Home Screen");
        gameFrame.add(cardPanel);
        gameFrame.setVisible(true);
    }

    private JPanel homePanel() {
        JPanel homePanel = new JPanel();
        JLabel introMessage = new JLabel("Welcome to the game 'Escapade'!");
        homePanel.setBackground(new Color(39, 49, 135));
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

        JButton startNewStage = new JButton("New Stage");
        JButton playerLeaderboard = new JButton("Leaderboard");
        JButton quitGame = new JButton("Quit Game");

        startNewStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "New Stage");
            }
        });

        playerLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "Leaderboard");
            }
        });

        quitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quitResponse = JOptionPane.showConfirmDialog(GameWindow.this, 
                "Are you sure you want to quit the game?", "Quit", JOptionPane.YES_NO_OPTION);
                if (quitResponse == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        homePanel.add(introMessage);
        homePanel.add(Box.createHorizontalGlue());
        homePanel.add(startNewStage);
        homePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        homePanel.add(playerLeaderboard);
        homePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        homePanel.add(quitGame);
        homePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        homePanel.add(Box.createHorizontalGlue());

        return homePanel;
    }

    private JPanel newStagePanel() {
        JPanel newStagePanel = new JPanel();
        newStagePanel.setBackground(new Color(14, 1, 66));
        newStagePanel.setLayout(new BoxLayout(newStagePanel, BoxLayout.Y_AXIS));

        JLabel nameInputLabel = new JLabel("Enter your name (max 20 characters, including spaces).");
        JTextField nameField = new JTextField(20);
        JButton submitNameInput = new JButton("Submit Name Input");

        submitNameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameInput = nameField.getText().trim();

                if (nameInput.length() < 0 || nameInput.length() > 20) {
                    JLabel invalidInput = new JLabel("Player name can't exceed 20 characters, including spaces.");
                } else {
                    nameInput = playerName;
                    card.show(cardPanel, "Level Selection");
                }
                // To check if input is of String type??
                // try {
                //     Integer.parseInt(playerName);
                //     JLabel invalidInput = new JLabel("Player name can't contain integers");
                // } catch (NumberFormatException e) {
                    
                // }
            }
        });

        newStagePanel.add(nameInputLabel);
        newStagePanel.add(nameField);
        newStagePanel.add(Box.createRigidArea(new Dimension(0, 40)));
        newStagePanel.add(submitNameInput);

        return newStagePanel;
    }

    private JPanel levelSelectionPanel() {
        JPanel levelSelectionPanel = new JPanel();
        levelSelectionPanel.setBackground(new Color(24, 84, 25));
        levelSelectionPanel.setLayout(new BoxLayout(levelSelectionPanel, BoxLayout.Y_AXIS));

        JLabel levelSelectionLabel = new JLabel("Select a level:");
        JButton buttonEasyLevel = new JButton("Easy"); // Level 1
        JButton buttonMediumLevel = new JButton("Medium"); // Level 2
        JButton buttonHardLevel = new JButton("Hard"); // Level 3

        buttonEasyLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(1);
            }
        });

        buttonMediumLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(2);
            }
        });

        buttonHardLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(3);
            }
        });

        levelSelectionPanel.add(levelSelectionLabel);
        levelSelectionPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        levelSelectionPanel.add(buttonEasyLevel);
        levelSelectionPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        levelSelectionPanel.add(buttonMediumLevel);
        levelSelectionPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        levelSelectionPanel.add(buttonHardLevel);

        return levelSelectionPanel;
    }

    private void startGame(int level) {
        card.show(cardPanel, "Stage");
    }

    private JPanel leaderboardPanel() {
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setBackground(new Color(39, 92, 135));
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));

        // add more stuff here 

        return leaderboardPanel;
    }
    
    public static void main(String[] a) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}