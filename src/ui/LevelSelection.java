package ui;

import levels.*;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class LevelSelection extends JFrame {

    private int highestUnlocked = 1;

    public LevelSelection() {
        setTitle("Station Saga - Level Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Background panel
        JPanel mainPanel = new JPanel() {
            private Image backgroundImg = loadImage("/Background/background.png");
            private Image borderImg = loadImage("/Background/border.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight();
                if (backgroundImg != null) g.drawImage(backgroundImg, 0, 0, w, h, this);
                if (borderImg != null) g.drawImage(borderImg, 0, 0, w, h, this);
            }

            private Image loadImage(String path) {
                try {
                    return new ImageIcon(getClass().getResource(path)).getImage();
                } catch (Exception e) {
                    System.out.println("Failed to load image: " + path);
                    return null;
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Add level panels
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 50, 50, 50);

        for (int i = 1; i <= 4; i++) {
            JPanel levelPanel = createLevelPanel(i);
            gbc.gridx = (i-1) % 2;
            gbc.gridy = (i-1) / 2;
            mainPanel.add(levelPanel, gbc);
        }

        JButton backButton = new JButton("BACK");
        backButton.setPreferredSize(new Dimension(200, 60));
        backButton.setFont(new Font("Arial", Font.BOLD, 22));
        backButton.setBackground(new Color(0xFFF4D7));
        backButton.setForeground(new Color(0x826237));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(0x826237), 5));

        backButton.addActionListener(e -> {
            dispose();
            new MainMenu().setVisible(true);
        });

        GridBagConstraints backGbc = new GridBagConstraints();
        backGbc.gridx = 0;
        backGbc.gridy = 2;
        backGbc.gridwidth = 2;
        backGbc.insets = new Insets(15, 0, 15, 0);
        backGbc.anchor = GridBagConstraints.SOUTH;

        mainPanel.add(backButton, backGbc);
    }

    private JPanel createLevelPanel(int levelNumber) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0xFFF4D7));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(0x826237));
                g.fillRect(0, 0, getWidth(), 8);
                g.fillRect(0, 0, 8, getHeight());
                g.fillRect(getWidth()-8, 0, 8, getHeight());
                g.fillRect(0, getHeight()-8, getWidth(), 8);

                // Circle in center
                int diameter = Math.min(getWidth(), getHeight()) / 2;
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2 + 15;
                g.setColor(Color.WHITE);
                g.fillOval(x, y, diameter, diameter);
                g.setColor(new Color(0x826237));
                g.drawOval(x, y, diameter, diameter);
            }
        };
        panel.setPreferredSize(new Dimension(250, 250));
        panel.setLayout(new BorderLayout());

        // LEVEL
        JLabel topLabel = new JLabel("LEVEL", JLabel.CENTER);
        try (InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf")) {
            Font pressStart = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, 28f);
            topLabel.setFont(pressStart);
        } catch (Exception e) {
            e.printStackTrace();
            topLabel.setFont(new Font("Arial", Font.BOLD, 28));
        }
        topLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        panel.add(topLabel, BorderLayout.NORTH);

        // level number or lock
        JLabel centerLabel;
        if (levelNumber <= highestUnlocked) {
            centerLabel = new JLabel(String.valueOf(levelNumber), JLabel.CENTER);
            centerLabel.setFont(new Font("Arial", Font.BOLD, 48));
            centerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        } else {
            ImageIcon lockIcon = new ImageIcon(getClass().getResource("/Icons/padlock.png"));
            Image scaled = lockIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            centerLabel = new JLabel(new ImageIcon(scaled), JLabel.CENTER);
            centerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        }

        panel.add(centerLabel, BorderLayout.CENTER);

        // Only clickable if unlocked
        if (levelNumber <= highestUnlocked) {
            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    openLevel(levelNumber);
                }
            });
        }

        return panel;
    }

    private void openLevel(int levelNumber) {
        dispose();
        switch (levelNumber) {
            case 1 -> new UI_level1().setVisible(true);
            case 2 -> new UI_level2().setVisible(true);
            case 3 -> new UI_level3().setVisible(true);
            case 4 -> new UI_level4().setVisible(true);
        }
        if (levelNumber == highestUnlocked && highestUnlocked < 4) {
            highestUnlocked++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LevelSelection().setVisible(true));
    }
}
