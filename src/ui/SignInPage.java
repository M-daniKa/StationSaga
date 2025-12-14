package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignInPage extends JFrame {

    public SignInPage() {
        initializeFrame();
        setupComponents();
    }

    private void initializeFrame() {
        setTitle("Station Saga - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }

    private void setupComponents() {
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

        // Center panel
        JPanel centerPanel = new RoundedPanel(new Color(0xFFF4D7), new Color(0x826237), 40);
        centerPanel.setPreferredSize(new Dimension(400, 350));
        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("SIGNUP", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        titleLabel.setForeground(new Color(0x826237));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        centerPanel.add(titleLabel, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        emailLabel.setForeground(new Color(0x826237));
        gbc.insets = new Insets(10, 10, 20, 10);
        centerPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(100, 30));
        centerPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setForeground(new Color(0x826237));
        centerPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(100, 30));
        centerPanel.add(passwordField, gbc);

        // Sign Up button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Sign Up");
        loginButton.setBackground(new Color(0x826237));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        centerPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });

        // Sign in redirect
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        JLabel signupLabel = new JLabel("<html>Already have an account? <u>Login</u></html>", JLabel.CENTER);
        signupLabel.setForeground(new Color(0x826237));
        signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginPage().setVisible(true);
            }
        });
        centerPanel.add(signupLabel, gbc);

        mainPanel.add(centerPanel);
    }

    // Rounded rectangle panel
    static class RoundedPanel extends JPanel {
        private Color fillColor;
        private Color borderColor;
        private int cornerRadius;

        public RoundedPanel(Color fillColor, Color borderColor, int cornerRadius) {
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}