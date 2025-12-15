package levels;

import ui.LevelSelection;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.InputStream;

public class LevelIntro extends JFrame {

    private static final Color BORDER_COLOR = new Color(0x826237);
    private static final Color FILL_COLOR   = new Color(0xFFF4D7);

    private final int levelNumber;

    public LevelIntro(int levelNumber) {
        this.levelNumber = levelNumber;

        setTitle("Station Saga - Level " + levelNumber);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        add(createBackgroundPanel());
    }

    private JPanel createBackgroundPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout()) {

            private final Image backgroundImg = loadImage("/Background/background.png");
            private final Image borderImg     = loadImage("/Background/border.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight();
                if (backgroundImg != null)
                    g.drawImage(backgroundImg, 0, 0, w, h, this);
                if (borderImg != null)
                    g.drawImage(borderImg, 0, 0, w, h, this);
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

        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        mainPanel.add(createMainBox(), gbc);

        return mainPanel;
    }

    private JPanel createMainBox() {
        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(600, 420));
        box.setBackground(FILL_COLOR);
        box.setBorder(new LineBorder(BORDER_COLOR, 6));
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        // Title
        JLabel title = createTitleLabel();
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        // Description (centered)
        JTextPane description = new JTextPane();
        description.setText(getLevelDescription(levelNumber));
        description.setEditable(false);
        description.setFocusable(false);
        description.setOpaque(false);
        description.setForeground(BORDER_COLOR);
        description.setFont(new Font("Arial", Font.BOLD, 24));
        description.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Center-align the text
        StyledDocument doc = description.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Buttons
        JPanel buttons = createButtonPanel();
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        box.add(title);
        box.add(description);
        box.add(Box.createVerticalGlue());
        box.add(buttons);

        return box;
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel("LEVEL " + levelNumber, JLabel.CENTER);
        label.setForeground(BORDER_COLOR);

        try (InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf")) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is)
                    .deriveFont(Font.BOLD, 48f);
            label.setFont(font);
        } catch (Exception e) {
            label.setFont(new Font("Arial", Font.BOLD, 48));
        }

        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        JButton backBtn = createButton("BACK", 220, 70);
        JButton playBtn = createButton("PLAY", 220, 70);

        backBtn.addActionListener(e -> {
            new LevelSelection().setVisible(true);
            dispose();
        });

        playBtn.addActionListener(e -> openLevel());

        panel.add(backBtn);
        panel.add(Box.createHorizontalStrut(40));
        panel.add(playBtn);

        return panel;
    }

    private JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(FILL_COLOR);
        button.setForeground(BORDER_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BORDER_COLOR, 4, true));
        return button;
    }

    private void openLevel() {
        dispose();
        switch (levelNumber) {
            case 1 -> new UI_level1_station1().setVisible(true);
            case 2 -> new UI_level2_station3().setVisible(true);
            case 3 -> new UI_level3().setVisible(true);
            case 4 -> new UI_level4().setVisible(true);
        }
    }

    private String getLevelDescription(int level) {
        return switch (level) {
            case 1 -> "Learn the basics of adding and removing in a Linked List.\n\nMaster the fundamentals to move forward.";
            case 2 -> "Search and edit existing data.\n\nAccuracy and speed are key.";
            case 3 -> "Swap values efficiently.\n\nUnderstand how data moves in memory.";
            case 4 -> "Sort data using different techniques.\n\nThink logically and optimize your steps.";
            default -> "Unknown level.";
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LevelIntro(1).setVisible(true));
    }
}
