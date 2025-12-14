package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

public class StartingPage extends JFrame {

    public StartingPage() {
        initializeFrame();
        setupComponents();
        addClickListener();
    }

    private void initializeFrame() {
        setTitle("Station Saga");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
    }

    private void setupComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImg = new ImageIcon(getClass().getResource("/Background/background.png"));
                g.drawImage(backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);

                ImageIcon borderImg = new ImageIcon(getClass().getResource("/Background/border.png"));
                g.drawImage(borderImg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Station Saga", JLabel.CENTER);
        Color darkBrown = new Color(0x65432F);
        titleLabel.setForeground(darkBrown);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));

        try (InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf")) {
            Font pressStart = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, 90);
            titleLabel.setFont(pressStart);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel continueLabel = new JLabel("Click anywhere to continue", JLabel.CENTER);
        continueLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        continueLabel.setForeground(Color.WHITE);
        continueLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(continueLabel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addClickListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {new StartingPage().setVisible(true);});

    }
}
