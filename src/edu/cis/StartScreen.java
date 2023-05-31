package edu.cis;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;

public class StartScreen extends JPanel {
    private JFrame frame;
    private JButton startButton;

    public StartScreen() {
        // Add high score list and clear scores button
        JPanel scorePanel = new JPanel();
        scorePanel.setBounds(10, 10, 200, 200);  // Position at top
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));

        scorePanel.add(new JLabel("Top 5 Times"));

        HighScore scores = new HighScore();
        ArrayList<Score> highScores = scores.getScores();

        for (int i = 0; i < highScores.size(); i++) {
            Score s = highScores.get(i);
            JLabel rank = new JLabel(String.valueOf(i + 1) + ". ");
            JLabel score = new JLabel(s.getName() + ": " + s.getTime() + "s");
            JPanel entry = new JPanel();
            entry.add(rank);
            entry.add(score);
            scorePanel.add(entry);
        }

        JButton clearButton = new JButton("Clear Scores");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scores.clearScores();
                scorePanel.removeAll();
                frame.revalidate();
                frame.repaint();
            }
        });
        scorePanel.add(clearButton);

        add(scorePanel);
        frame = new JFrame("Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setResizable(false);

        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.setBounds(200, 30, 100, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame gameFrame = new JFrame("Breakout Game");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setResizable(false);
                gameFrame.setLocationRelativeTo(null);

                Breakout game = new Breakout();
                gameFrame.setContentPane(game);
                gameFrame.pack();
                gameFrame.setSize(game.getPreferredSize()); // Set the size of the game window
                gameFrame.setVisible(true);
                game.init();
                game.start();
            }
        });
        add(startButton);

        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}