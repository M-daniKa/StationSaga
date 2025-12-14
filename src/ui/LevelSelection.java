package ui;

import javax.swing.*;
import java.awt.*;

public class LevelSelection extends JFrame {

    private static final int TOTAL_LEVELS = 4;
    private int unlockedLevel = 1; // CHANGE based on progress

    public LevelSelection() {
        setupFrame();
        add(createMainPanel());
    }

    private void setupFrame() {
        setTitle("Station Saga - Level Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 40, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 150, 100, 150));
        panel.setOpaque(false);

        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            panel.add(new LevelCard(i, i <= unlockedLevel));
        }

        return panel;
    }

    class LevelCard extends JPanel {

        private final int levelNumber;
        private final boolean unlocked;

        LevelCard(int levelNumber, boolean unlocked) {
            this.levelNumber = levelNumber;
            this.unlocked = unlocked;

            setPreferredSize(new Dimension(300, 300));
            setOpaque(false);

            if (unlocked) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        startLevel(levelNumber);
                    }
                });
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Card background
            g2.setColor(new Color(0xFFF4D7));
            g2.fillRoundRect(0, 0, w, h, 30, 30);

            // Card border
            g2.setColor(new Color(0x826237));
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(0, 0, w, h, 30, 30);

            // LEVEL text
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.drawString("LEVEL", w / 2 - 40, 40);

            // Center circle
            int circleSize = 120;
            int cx = (w - circleSize) / 2;
            int cy = (h - circleSize) / 2;

            g2.setColor(unlocked ? new Color(0x826237) : Color.GRAY);
            g2.fillOval(cx, cy, circleSize, circleSize);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 48));

            if (unlocked) {
                // Level number
                String text = String.valueOf(levelNumber);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(
                        text,
                        w / 2 - fm.stringWidth(text) / 2,
                        h / 2 + fm.getAscent() / 4
                );
            } else {
                // Lock icon
                g2.drawString("🔒", w / 2 - 18, h / 2 + 18);
            }
        }
    }

    private void startLevel(int levelNumber) {
        JOptionPane.showMessageDialog(
                this,
                "Starting Level " + levelNumber,
                "Level Start",
                JOptionPane.INFORMATION_MESSAGE
        );
        // TODO: Load level scene here
    }
}
