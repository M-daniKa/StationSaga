package levels;

import data.levelDataLoader;
import entities.levelData;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UI_level1 extends JFrame {

    private final levelData levelData;
    private int dialogueIndex = 0;
    private JLabel dialogueLabel;

    private static final Color FILL_COLOR = new Color(0xFFF4D7);
    private static final Color BORDER_COLOR = new Color(0x826237);

    public UI_level1() {
        this.levelData = levelDataLoader.loadLevel(1);

        setTitle("Station Saga - Level 1: Add/Remove");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // ===== BACKGROUND PANEL =====
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgIcon = new ImageIcon(getClass().getResource("/levelBackground/Level1.png"));
                g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);

                ImageIcon borderIcon = new ImageIcon(getClass().getResource("/Background/border.png"));
                g.drawImage(borderIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // ===== TOP PANEL (Station Number Image + Pause Button + Station Number Text) =====
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(50, 65, 10, 65));

        // Station number image (scaled smaller)
        ImageIcon stationIcon = new ImageIcon(getClass().getResource("/lalagyan/stationNum.png"));
        Image scaledStation = stationIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        JLabel stationLabel = new JLabel(new ImageIcon(scaledStation));
        stationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Station number text
        JLabel stationNumberLabel = new JLabel("Station " + levelData.getStation());
        stationNumberLabel.setFont(new Font("Arial", Font.BOLD, 28));
        stationNumberLabel.setForeground(Color.WHITE);
        stationNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Pause button (on the right)
        JPanel pausePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pausePanel.setOpaque(false);
        JButton pauseButton = createPauseButton();
        pausePanel.add(pauseButton);

        // Add components to top panel
        topPanel.add(stationLabel);
        topPanel.add(Box.createVerticalStrut(10)); // space between image and text
        topPanel.add(stationNumberLabel);
        topPanel.add(Box.createVerticalStrut(10)); // space before pause button
        topPanel.add(pausePanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== DIALOGUE BOX =====
        JPanel dialoguePanel = new JPanel();
        dialoguePanel.setOpaque(false);
        dialoguePanel.setLayout(new BoxLayout(dialoguePanel, BoxLayout.Y_AXIS));
        dialoguePanel.setBorder(BorderFactory.createEmptyBorder(10, 200, 10, 200));

        JPanel dialogueBox = new JPanel();
        dialogueBox.setBackground(FILL_COLOR);
        dialogueBox.setBorder(new LineBorder(BORDER_COLOR, 5));
        dialogueBox.setLayout(new BoxLayout(dialogueBox, BoxLayout.Y_AXIS));
        dialogueBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        dialogueBox.setPreferredSize(new Dimension(500, 300));

        dialogueLabel = new JLabel("", JLabel.CENTER);
        dialogueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dialogueLabel.setForeground(BORDER_COLOR);
        dialogueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dialogueBox.add(Box.createVerticalStrut(10)); // padding top
        dialogueBox.add(dialogueLabel);
        dialogueBox.add(Box.createVerticalStrut(10)); // padding bottom

        dialoguePanel.add(dialogueBox);
        mainPanel.add(dialoguePanel, BorderLayout.CENTER);

        // Start first dialogue
        showNextDialogue();

        // Mouse click advances dialogue
        dialogueBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextDialogue();
            }
        });
    }

    private void showNextDialogue() {
        List<String> dialogues = levelData.getJonaDialogue();
        if (dialogueIndex < dialogues.size()) {
            dialogueLabel.setText("<html><center>" + dialogues.get(dialogueIndex) + "</center></html>");
            dialogueIndex++;
        } else {
            dialogueLabel.setText("<html><center>Click to start the level!</center></html>");
            dialogueLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    startLevel();
                }
            });
        }
    }

    private void startLevel() {
        // TODO: Replace with actual gameplay component
        JOptionPane.showMessageDialog(this, "Level interaction starts now!");
    }

    private JButton createPauseButton() {
        ImageIcon lockIcon = new ImageIcon(getClass().getResource("/Icons/pause.png"));
        Image scaledImage = lockIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton pauseButton = new JButton(scaledIcon);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.setOpaque(false);
        pauseButton.setPreferredSize(new Dimension(60, 60));
        pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pauseButton.addActionListener(e -> showPauseDialog());
        return pauseButton;
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
