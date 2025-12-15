// Language: java
package levels;

import core.stationUtils;
import data.levelDataLoader;
import entities.DialogueEntry;
import entities.levelData;
import entities.trainCar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

public class UI_level1 extends JFrame {

    private final levelData levelData;
    private int dialogueIndex = 0;
    private JLabel dialogueLabel;

    private static final Color FILL_COLOR = new Color(0xFFF4D7);
    private static final Color BORDER_COLOR = new Color(0x826237);

    // controller link between UI and trackLinkedList
    private final level1_AddRemove controller = new level1_AddRemove();

    public UI_level1() {
        this.levelData = levelDataLoader.loadLevel(1, 1);

        setTitle("Station Saga - Level 1: Add/Remove (Station 1)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

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

        // --- Top (station label + pause) ---

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

        // --- Dialogue box ---

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

        // --- Bottom action buttons box ---

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

        // --- Button actions using controller + track ---

        // Add a basic train car when allowed
        btnAdd.addActionListener(e -> {
            if (!controller.isCanAdd()) {
                JOptionPane.showMessageDialog(
                        this,
                        "You can only add a car when the dialogue tells you to.",
                        "Not yet",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // simple fixed example, you can randomize later
            trainCar car = new trainCar(
                    trainCar.carType.PASSENGER,
                    10,
                    trainCar.carState.AVAILABLE
            );
            controller.getTrack().addCar(car);

            int index = controller.getTrainSize() - 1;
            JOptionPane.showMessageDialog(
                    this,
                    "Added car at index " + index + ".\n" + stationUtils.carInfo(car),
                    "Car added",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Delete by index when allowed
        btnDelete.addActionListener(e -> {
            if (!controller.isCanDelete()) {
                JOptionPane.showMessageDialog(
                        this,
                        "You can only delete a car when the dialogue tells you to.",
                        "Not yet",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String input = JOptionPane.showInputDialog(this, "Index to delete (0-based):");
            if (input == null) return;
            try {
                int index = Integer.parseInt(input);
                boolean removed = controller.getTrack().removeByIndex(index);
                if (!removed) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No car at index " + index + ".",
                            "Delete failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Removed car at index " + index + ".",
                            "Car deleted",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid number.",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Simple search by capacity when allowed
        btnSearch.addActionListener(e -> {
            if (!controller.isCanSearch()) {
                JOptionPane.showMessageDialog(
                        this,
                        "You can only search when the dialogue tells you to.",
                        "Not yet",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String input = JOptionPane.showInputDialog(this, "Capacity to search:");
            if (input == null) return;
            try {
                int capacity = Integer.parseInt(input);
                List<Integer> result = controller.getTrack()
                        .searchByCapacity(trainCar.carType.PASSENGER, capacity);
                JOptionPane.showMessageDialog(
                        this,
                        result.isEmpty()
                                ? "No cars found with capacity " + capacity + "."
                                : "Found cars at indices: " + result,
                        "Search result",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid number.",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Swap and sort placeholders
        btnSwap.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Swap not implemented yet.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        ));

        btnSort.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Sort not implemented yet.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        ));

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

        // start at first dialogue
        showCurrentDialogue();
    }

    // --- Dialogue handling ---

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

        // Enable actions at specific lines from `level1.txt`.
        // You may tweak these indices based on how `levelDataLoader` builds the list.

        // Station 1: "To add a train car, click that small add button below!"
        // This is the 4th line of Station 1 dialogue => index 3 (0-based) in that block.
        // Assuming the loader puts them sequentially, that will be near 3 overall.
        if (dialogueIndex == 3) {
            controller.enableAdd(true);
        }

        // "To add another train car, click the add button again."
        // That is later in Station 1; here we assume index 15.
        if (dialogueIndex == 15) {
            controller.enableAdd(true);
        }

        // Station 2: "To delete a train car, click the delete button below!"
        // That is the 4th line of Station 2 dialogue; we assume around 25.
        if (dialogueIndex == 25) {
            controller.enableDelete(true);
        }

        // If you later add search tutorial lines, you can enable search the same way:
        // if (dialogueIndex == X) controller.enableSearch(true);
    }

    private void showPreviousDialogue() {
        List<DialogueEntry> entries = levelData.getEntries();
        if (entries.isEmpty()) return;
        if (dialogueIndex > 0) {
            dialogueIndex--;
        }
        showCurrentDialogue();
    }

    // --- Pause dialog ---

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
        SwingUtilities.invokeLater(() -> new UI_level1().setVisible(true));
    }
}
