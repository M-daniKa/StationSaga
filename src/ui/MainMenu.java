package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.InputStream;

public class MainMenu extends JFrame {

    private static final Color BORDER_COLOR = new Color(0x826237);
    private static final Color FILL_COLOR   = new Color(0xFFF4D7);

    public MainMenu() {
        setupFrame();
        add(createBackgroundPanel());
    }

    private void setupFrame() {
        setTitle("Station Saga - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }

    private JPanel createBackgroundPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout()) {

            private final Image backgroundImg = loadImage("/Background/background.png");
            private final Image borderImg = loadImage("/Background/border.png");

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

        mainPanel.add(createMenuPanel());
        return mainPanel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 80, 20);
        panel.add(createTitleLabel(), gbc);

        gbc.insets = new Insets(20, 20, 20, 20);

        gbc.gridy = 1;
        panel.add(createButton("PLAY", 400, 100, 32), gbc);

        gbc.gridy = 2;
        panel.add(createButton("PROFILE", 280, 70, 22), gbc);

        gbc.gridy = 3;
        panel.add(createButton("QUIT", 280, 70, 22), gbc);


        return panel;
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("STATION SAGA");
        titleLabel.setForeground(BORDER_COLOR);

        try (InputStream is = getClass()
                .getResourceAsStream("/Fonts/PressStart2P-Regular.ttf")) {

            Font pressStart = Font.createFont(Font.TRUETYPE_FONT, is)
                    .deriveFont(Font.BOLD, 90f);
            titleLabel.setFont(pressStart);

        } catch (Exception e) {
            e.printStackTrace();
            titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        }

        return titleLabel;
    }

    private JButton createButton(String text, int width, int height, int fontSize) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setBackground(FILL_COLOR);
        button.setForeground(BORDER_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BORDER_COLOR, 4, true));

        if (text.equals("QUIT")) {
            button.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to quit?",
                        "Exit Game",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            });
        } else if (text.equals("PLAY")) {
            button.addActionListener(e -> {
                new LevelSelection().setVisible(true);
                this.dispose();
            });
        }

        return button;
    }

}
