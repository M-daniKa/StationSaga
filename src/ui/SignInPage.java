package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignInPage extends JFrame {

    public SignInPage() {
        setTitle("Station Saga - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
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
        add(mainPanel);

        // Center panel
        JPanel centerPanel = new LoginPage.RoundedPanel(new Color(0xFFF4D7), new Color(0x826237), 40);
        centerPanel.setPreferredSize(new Dimension(400, 200));
        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Sign Up", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0x826237));
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(titleLabel, gbc);

        // Redirect to Login
        JLabel loginLabel = new JLabel("<html>Already have an account? <u>Log In</u></html>", JLabel.CENTER);
        loginLabel.setForeground(new Color(0x826237));
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginPage().setVisible(true);
            }
        });
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(loginLabel, gbc);

        mainPanel.add(centerPanel);
    }
}
