// Language: java
package levels;

import data.levelDataLoader;
import entities.DialogueEntry;
import entities.levelData;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

public class UI_level3_station5 extends JFrame {

    private final levelData levelData;
    private int dialogueIndex = 0;
    private JLabel dialogueLabel;

    private static final Color FILL_COLOR = new Color(0xFFF4D7);
    private static final Color BORDER_COLOR = new Color(0x826237);

    public UI_level3_station5() {
        this.levelData = levelDataLoader.loadLevel(3, 1);

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

        // --- Top panel (same layout as UI_level1) ---
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(50, 65, 0, 65));

        JLabel stationLabel = new JLabel("" + levelData.getStation());
        stationLabel.setHorizontalAlignment(SwingConstants.CENTER);
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

        JPanel titlePauseRow = new JPanel(new BorderLayout());
        titlePauseRow.setOpaque(false);
        titlePauseRow.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        titlePauseRow.add(stationLabel, BorderLayout.CENTER);
        titlePauseRow.add(pausePanel, BorderLayout.EAST);

        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titlePauseRow);
        topPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- Dialogue box (same as UI_level1) ---
        JPanel dialogueBox = new JPanel(new BorderLayout());
        dialogueBox.setBackground(FILL_COLOR);
        dialogueBox.setBorder(new LineBorder(BORDER_COLOR, 3));
        dialogueBox.setPreferredSize(new Dimension(600, 80));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1.0;

        dialogueLabel = new JLabel("", JLabel.CENTER);
        dialogueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dialogueLabel.setForeground(BORDER_COLOR);
        dialogueLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(dialogueLabel, gbc);

        ImageIcon backIcon = new ImageIcon(getClass().getResource("/Stuff/backbutton.png"));
        Image backImg = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledBackIcon = new ImageIcon(backImg);

        ImageIcon forwardIcon = new ImageIcon(getClass().getResource("/Stuff/forward button.png"));
        Image forwardImg = forwardIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledForwardIcon = new ImageIcon(forwardImg);

        JButton prevButton = new JButton(scaledBackIcon);
        prevButton.setFont(new Font("Arial", Font.BOLD, 18));
        prevButton.setForeground(Color.WHITE);
        prevButton.setBackground(new Color(0x6C757D));
        prevButton.setFocusPainted(false);
        prevButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        prevButton.addActionListener(e -> showPreviousDialogue());

        JButton nextButton = new JButton(scaledForwardIcon);
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

        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        contentPanel.add(buttonPanel, gbc);

        dialogueBox.add(contentPanel, BorderLayout.CENTER);

        JPanel topDialoguePanel = new JPanel(new BorderLayout());
        topDialoguePanel.setOpaque(false);
        topDialoguePanel.setBorder(BorderFactory.createEmptyBorder(10, 200, 10, 200));
        topDialoguePanel.add(dialogueBox, BorderLayout.NORTH);
        mainPanel.add(topDialoguePanel, BorderLayout.CENTER);

        // --- Bottom button box (same as UI_level1) ---
        JPanel bottomButtonBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 40;
                int w = getWidth();
                int h = getHeight();

                g2.setColor(FILL_COLOR);
                g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);

                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

                g2.dispose();
            }
        };
        bottomButtonBox.setOpaque(false);
        bottomButtonBox.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomButtonBox.setPreferredSize(new Dimension(580, 110));

        ImageIcon addIconRaw = new ImageIcon(getClass().getResource("/fourChoices/Add.jpg"));
        ImageIcon deleteIconRaw = new ImageIcon(getClass().getResource("/fourChoices/Delete.jpg"));
        ImageIcon searchIconRaw = new ImageIcon(getClass().getResource("/fourChoices/Search.jpg"));
        ImageIcon swapIconRaw = new ImageIcon(getClass().getResource("/fourChoices/Swap.jpg"));
        ImageIcon sortIconRaw = new ImageIcon(getClass().getResource("/fourChoices/Sort.jpg"));

        int iconSize = 48;
        ImageIcon addIcon = new ImageIcon(addIconRaw.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        ImageIcon deleteIcon = new ImageIcon(deleteIconRaw.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        ImageIcon searchIcon = new ImageIcon(searchIconRaw.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        ImageIcon swapIcon = new ImageIcon(swapIconRaw.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        ImageIcon sortIcon = new ImageIcon(sortIconRaw.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

        JButton btnAdd = new JButton("Add", addIcon);
        JButton btnDelete = new JButton("Delete", deleteIcon);
        JButton btnSearch = new JButton("Search", searchIcon);
        JButton btnSwap = new JButton("Swap", swapIcon);
        JButton btnSort = new JButton("Sort", sortIcon);

        for (JButton b : new JButton[]{btnAdd, btnDelete, btnSearch, btnSwap, btnSort}) {
            b.setFont(new Font("Arial", Font.BOLD, 14));
            b.setForeground(Color.WHITE);
            b.setBackground(BORDER_COLOR);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setVerticalTextPosition(SwingConstants.BOTTOM);
        }

        bottomButtonBox.add(btnAdd);
        bottomButtonBox.add(btnDelete);
        bottomButtonBox.add(btnSearch);
        bottomButtonBox.add(btnSwap);
        bottomButtonBox.add(btnSort);

        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomWrapper.setOpaque(false);
        bottomWrapper.add(bottomButtonBox);
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 40, 20));
        mainPanel.add(bottomWrapper, BorderLayout.SOUTH);

        showCurrentDialogue();
    }

    private void showCurrentDialogue() {
        List<DialogueEntry> entries = levelData.getEntries();
        if (entries.isEmpty()) {
            dialogueLabel.setText("");
            return;
        }
        DialogueEntry entry = entries.get(dialogueIndex);
        dialogueLabel.setText("<html><center>" + entry.getText() + "</center></html>");
    }

    private void showNextDialogue() {
        List<DialogueEntry> entries = levelData.getEntries();
        if (entries.isEmpty()) return;
        if (dialogueIndex < entries.size() - 1) {
            dialogueIndex++;
        }
        showCurrentDialogue();
    }

    private void showPreviousDialogue() {
        List<DialogueEntry> entries = levelData.getEntries();
        if (entries.isEmpty()) return;
        if (dialogueIndex > 0) {
            dialogueIndex--;
        }
        showCurrentDialogue();
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
        SwingUtilities.invokeLater(() -> new UI_level3_station5().setVisible(true));
    }
}
