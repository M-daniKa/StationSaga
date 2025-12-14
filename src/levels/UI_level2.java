package levels;

import javax.swing.*;
import java.awt.*;

public class UI_level2 extends JFrame {

    public UI_level2() {
        setTitle("Station Saga - Level 1: Add/Remove");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon bgIcon = new ImageIcon(
                        getClass().getResource("/levelBackground/Level1.png")
                );
                g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);

                ImageIcon borderIcon = new ImageIcon(
                        getClass().getResource("/Background/border.png")
                );
                g.drawImage(borderIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // ===== TOP PANEL (Title + Pause Button) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(45, 65, 45, 65));

        JLabel titleLabel = new JLabel("Level 1: Add/Remove Train Cars", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        // ----- Pause Button -----
        ImageIcon lockIcon = new ImageIcon(
                getClass().getResource("/Icons/pause.png")
        );

        // scale icon (SMALL)
        Image scaledImage = lockIcon.getImage().getScaledInstance(
                60, 60, Image.SCALE_SMOOTH
        );
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton pauseButton = new JButton(scaledIcon);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.setOpaque(false);
        pauseButton.setPreferredSize(new Dimension(60, 60));
        pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pauseButton.addActionListener(e -> showPauseDialog());

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(pauseButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== GAMEPLAY PANEL =====
        JPanel gameplayPanel = new JPanel();
        gameplayPanel.setOpaque(false);

        JLabel placeholder = new JLabel("Train Linked List Interaction Here");
        placeholder.setFont(new Font("Arial", Font.BOLD, 24));
        placeholder.setForeground(Color.WHITE);

        gameplayPanel.add(placeholder);
        mainPanel.add(gameplayPanel, BorderLayout.CENTER);
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
        SwingUtilities.invokeLater(() -> new UI_level2().setVisible(true));
    }
}
