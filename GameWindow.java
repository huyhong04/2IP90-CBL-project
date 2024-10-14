import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Escapade");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        JButton startNewStage = new JButton("New Stage");
        JButton playerLeaderboard = new JButton("Leaderboard");
        JButton quitApplication = new JButton("Quit Application");

        startNewStage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newStage();
            }
        });

        playerLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leaderboard();
            }
        });

        quitApplication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitApp();
            }
        }); 

        gamePanel.add(Box.createRigidArea(new Dimension(40, 40)));
        gamePanel.add(startNewStage);
        gamePanel.add(Box.createRigidArea(new Dimension(40, 40)));
        gamePanel.add(playerLeaderboard);
        gamePanel.add(Box.createRigidArea(new Dimension(40, 40)));
        gamePanel.add(quitApplication);

        add(gamePanel);

        setVisible(true);

    }

    private void newStage() {
        this.dispose();
    }
    
    private void leaderboard() {
        this.dispose();
    }
    
    private void quitApp() {
        int quitResponse = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to quit the game?", "Quit", 
        JOptionPane.YES_NO_OPTION);
        if (quitResponse == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] a) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}