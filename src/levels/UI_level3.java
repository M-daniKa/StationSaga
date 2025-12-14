package levels;

import javax.swing.*;
import java.awt.*;

public class UI_level3 extends JFrame {

    public UI_level3() {
        setTitle("Station Saga - Level 1: Add/Remove");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgIcon = new ImageIcon(getClass().getResource("/Background/background.png"));
                g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);

                ImageIcon borderIcon = new ImageIcon(getClass().getResource("/Background/border.png"));
                g.drawImage(borderIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Level 1: Add/Remove Train Cars", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel gameplayPanel = new JPanel();
        gameplayPanel.setOpaque(false);
        JLabel placeholder = new JLabel("Train Linked List Interaction Here");
        placeholder.setFont(new Font("Arial", Font.BOLD, 24));
        placeholder.setForeground(Color.WHITE);
        gameplayPanel.add(placeholder);
        mainPanel.add(gameplayPanel, BorderLayout.CENTER);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 20));
        pauseButton.addActionListener(e -> showPauseDialog());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(pauseButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showPauseDialog() {
        String[] options = {"Resume", "Back"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Game Paused",
                "Pause",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to go back?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new ui.LevelSelection().setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UI_level1().setVisible(true));
    }
}
