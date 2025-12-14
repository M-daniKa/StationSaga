// Language: java
package levels;

import data.levelDataLoader;
import entities.levelData;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

public class UI_level3 extends JFrame {

    private final levelData levelData;
    private int dialogueIndex = 0;
    private JLabel dialogueLabel;

    private static final Color FILL_COLOR = new Color(0xFFF4D7);
    private static final Color BORDER_COLOR = new Color(0x826237);

    public UI_level3() {
        this.levelData = levelDataLoader.loadLevel(3);

        setTitle("Station Saga - Level 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bgIcon = new ImageIcon(getClass().getResource("/levelBackground/Level3.png"));
                g.drawImage(bgIcon.getImage(), 0, 0, getWidth(), getHeight(), this);

                ImageIcon borderIcon = new ImageIcon(getClass().getResource("/Background/border.png"));
                g.drawImage(borderIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Top: station label + pause
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(50, 65, 10, 65));

        JLabel stationLabel = new JLabel("" + levelData.getStation());
        stationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stationLabel.setForeground(Color.WHITE);

        try (InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf")) {
            Font pressStart = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 28f);
            stationLabel.setFont(pressStart);
        } catch (Exception e) {
            stationLabel.setFont(new Font("Arial", Font.BOLD, 28));
        }

        JPanel pausePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pausePanel.setOpaque(false);
        pausePanel.add(createPauseButton());

        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(stationLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(pausePanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Dialogue box
        JPanel dialogueBox = new JPanel(new BorderLayout());
        dialogueBox.setBackground(FILL_COLOR);
        dialogueBox.setBorder(new LineBorder(BORDER_COLOR, 3));
        dialogueBox.setPreferredSize(new Dimension(600, 80));

        dialogueLabel = new JLabel("", JLabel.LEFT);
        dialogueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dialogueLabel.setForeground(BORDER_COLOR);
        dialogueLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogueBox.add(dialogueLabel, BorderLayout.CENTER);

        JButton prevButton = new JButton("◀");
        prevButton.setFont(new Font("Arial", Font.BOLD, 18));
        prevButton.setForeground(Color.WHITE);
        prevButton.setBackground(new Color(0x6C757D));
        prevButton.setFocusPainted(false);
        prevButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        prevButton.addActionListener(e -> showPreviousDialogue());

        JButton nextButton = new JButton("▶");
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBackground(new Color(0x28A745));
        nextButton.setFocusPainted(false);
        nextButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        nextButton.addActionListener(e -> showNextDialogue());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        dialogueBox.add(buttonPanel, BorderLayout.EAST);

        JPanel topDialoguePanel = new JPanel(new BorderLayout());
        topDialoguePanel.setOpaque(false);
        topDialoguePanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 10, 200));
        topDialoguePanel.add(dialogueBox, BorderLayout.NORTH);
        mainPanel.add(topDialoguePanel, BorderLayout.CENTER);

        // Gameplay placeholder
        JPanel gameplayPanel = new JPanel();
        gameplayPanel.setOpaque(false);
        JLabel placeholder = new JLabel("Level 3 Interaction Placeholder");
        placeholder.setFont(new Font("Arial", Font.BOLD, 24));
        placeholder.setForeground(Color.WHITE);
        gameplayPanel.add(placeholder);
        mainPanel.add(gameplayPanel, BorderLayout.SOUTH);

        showNextDialogue();
    }

    private void showNextDialogue() {
        List<String> dialogues = levelData.getJonaDialogue();
        if (dialogues.isEmpty()) return;
        if (dialogueIndex < dialogues.size() - 1) dialogueIndex++;
        dialogueLabel.setText("<html><center>" + dialogues.get(dialogueIndex) + "</center></html>");
    }

    private void showPreviousDialogue() {
        List<String> dialogues = levelData.getJonaDialogue();
        if (dialogues.isEmpty()) return;
        if (dialogueIndex > 0) dialogueIndex--;
        dialogueLabel.setText("<html><center>" + dialogues.get(dialogueIndex) + "</center></html>");
    }

    private JButton createPauseButton() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Icons/pause.png"));
        Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JButton pauseButton = new JButton(new ImageIcon(scaledImage));
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
            dispose();
            new ui.LevelSelection().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UI_level3().setVisible(true));
    }
}
