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
    private static final Color DISABLED_COLOR = new Color(0x9E9E9E);

    // controller
    private final level1_AddRemove controller = new level1_AddRemove();

    // buttons (need access globally)
    private JButton btnAdd, btnDelete, btnSearch, btnSwap, btnSort;

    public UI_level1() {
        this.levelData = levelDataLoader.loadLevel(1, 1);

        setTitle("Station Saga - Level 1: Add/Remove (Station 1)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(getClass().getResource("/levelBackground/Level1.png"))
                        .getImage(), 0, 0, getWidth(), getHeight(), this);
                g.drawImage(new ImageIcon(getClass().getResource("/Background/border.png"))
                        .getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        buildTop(mainPanel);
        buildDialogue(mainPanel);
        buildBottomButtons(mainPanel);

        showCurrentDialogue();
    }

    // --------------------------------------------------
    // TOP PANEL
    // --------------------------------------------------
    private void buildTop(JPanel mainPanel) {
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(50, 65, 0, 65));

        JLabel stationLabel = new JLabel(levelData.getStation(), SwingConstants.CENTER);
        stationLabel.setForeground(Color.WHITE);

        try (InputStream is = getClass().getResourceAsStream("/Fonts/PressStart2P-Regular.ttf")) {
            stationLabel.setFont(Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(28f));
        } catch (Exception e) {
            stationLabel.setFont(new Font("Arial", Font.BOLD, 28));
        }

        JPanel pausePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pausePanel.setOpaque(false);
        pausePanel.add(createPauseButton());

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        titleRow.add(stationLabel, BorderLayout.CENTER);
        titleRow.add(pausePanel, BorderLayout.EAST);

        topPanel.add(titleRow);
        mainPanel.add(topPanel, BorderLayout.NORTH);
    }

    // --------------------------------------------------
    // DIALOGUE
    // --------------------------------------------------
    private void buildDialogue(JPanel mainPanel) {
        JPanel dialogueBox = new JPanel(new BorderLayout());
        dialogueBox.setBackground(FILL_COLOR);
        dialogueBox.setBorder(new LineBorder(BORDER_COLOR, 3));
        dialogueBox.setPreferredSize(new Dimension(600, 90));

        dialogueLabel = new JLabel("", SwingConstants.CENTER);
        dialogueLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dialogueLabel.setForeground(BORDER_COLOR);
        dialogueLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        dialogueLabel.setVerticalAlignment(SwingConstants.CENTER);

        JButton prevButton = navButton("/Stuff/backbutton.png", this::showPreviousDialogue);
        JButton nextButton = navButton("/Stuff/forward button.png", this::showNextDialogue);

        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtons.setOpaque(false);
        navButtons.add(prevButton);
        navButtons.add(nextButton);

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 10));
        navPanel.add(navButtons, BorderLayout.CENTER);

        dialogueBox.add(dialogueLabel, BorderLayout.CENTER);
        dialogueBox.add(navPanel, BorderLayout.EAST);


        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 120, 10, 120));
        wrapper.add(dialogueBox, BorderLayout.NORTH);

        mainPanel.add(wrapper, BorderLayout.CENTER);
    }

    private JButton navButton(String iconPath, Runnable action) {
        ImageIcon icon = new ImageIcon(
                new ImageIcon(getClass().getResource(iconPath))
                        .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)
        );
        JButton btn = new JButton(icon);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setOpaque(true);
        btn.setBackground(new Color(0x826237));
        btn.setForeground(Color.WHITE);
        btn.addActionListener(e -> action.run());

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(new Color(0x6B4F3D));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(new Color(0x826237));
            }
        });

        return btn;
    }


    // --------------------------------------------------
    // BOTTOM BUTTONS
    // --------------------------------------------------
    private void buildBottomButtons(JPanel mainPanel) {
        JPanel box = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FILL_COLOR);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
            }
        };

        box.setOpaque(false);
        box.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        box.setPreferredSize(new Dimension(580, 110));

        btnAdd = actionButton("Add", "/fourChoices/Add.jpg");
        btnDelete = actionButton("Delete", "/fourChoices/Delete.jpg");
        btnSearch = actionButton("Search", "/fourChoices/Search.jpg");
        btnSwap = actionButton("Swap", "/fourChoices/Swap.jpg");
        btnSort = actionButton("Sort", "/fourChoices/Sort.jpg");

        setupActions();

        box.add(btnAdd);
        box.add(btnDelete);
        box.add(btnSearch);
        box.add(btnSwap);
        box.add(btnSort);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 40, 20));
        wrapper.add(box);

        mainPanel.add(wrapper, BorderLayout.SOUTH);
    }

    private JButton actionButton(String text, String iconPath) {
        ImageIcon icon = new ImageIcon(
                new ImageIcon(getClass().getResource(iconPath))
                        .getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)
        );
        JButton btn = new JButton(text, icon);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BORDER_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(BORDER_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(BORDER_COLOR);
            }
        });

        return btn;
    }

    // --------------------------------------------------
    // LOGIC
    // --------------------------------------------------
    private void showCurrentDialogue() {
        List<DialogueEntry> entries = levelData.getEntries();
        if (entries.isEmpty()) return;

        DialogueEntry entry = entries.get(dialogueIndex);
        dialogueLabel.setText("<html><center>" + entry.getText() + "</center></html>");

        // let the controller interpret the dialogue
        controller.updateControllerState(entry);
        updateButtonVisuals();
    }

    // removed old updateControllerState(String) that used enableAdd/enableDelete/enableSearch

    private void updateButtonVisuals() {
        setEnabled(btnAdd, controller.isCanAdd());
        setEnabled(btnDelete, controller.isCanDelete());
        setEnabled(btnSearch, controller.isCanSearch());
        // optional: disable swap/sort for now
        setEnabled(btnSwap, controller.isCanSwap());
        setEnabled(btnSort, controller.isCanSort());
    }

    private void setEnabled(JButton btn, boolean enabled) {
        btn.setEnabled(enabled);
        btn.setBackground(enabled ? BORDER_COLOR : DISABLED_COLOR);
    }

    private void showNextDialogue() {
        if (dialogueIndex < levelData.getEntries().size() - 1) dialogueIndex++;
        showCurrentDialogue();
    }

    private void showPreviousDialogue() {
        if (dialogueIndex > 0) dialogueIndex--;
        showCurrentDialogue();
    }

    // --------------------------------------------------
    // BUTTON ACTIONS
    // --------------------------------------------------
    private void setupActions() {
        btnAdd.addActionListener(e -> {
            level1_AddRemove.ActionResult result = controller.performAddCar();
            handleResult(result);
        });

        btnDelete.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Index to delete:");
            if (input == null) return;
            try {
                int idx = Integer.parseInt(input);
                level1_AddRemove.ActionResult result = controller.performDeleteByIndex(idx);
                handleResult(result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid index", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSearch.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Capacity:");
            if (input == null) return;
            try {
                int cap = Integer.parseInt(input);
                level1_AddRemove.ActionResult result =
                        controller.performSearchByCapacity(trainCar.carType.PASSENGER, cap);
                handleResult(result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid capacity", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // handle feedback from controller
    private void handleResult(level1_AddRemove.ActionResult result) {
        switch (result.getType()) {
            case NOT_ALLOWED:
            case ERROR:
                JOptionPane.showMessageDialog(this,
                        result.getMessage(),
                        "Info",
                        JOptionPane.WARNING_MESSAGE);
                break;
            case SUCCESS_ADD:
                trainCar car = result.getAddedCar();
                Integer idx = result.getIndex();
                JOptionPane.showMessageDialog(this,
                        "Added car at index " + idx + ":\n" + stationUtils.carInfo(car),
                        "Car Added",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case SUCCESS_DELETE:
                JOptionPane.showMessageDialog(this,
                        "Deleted car at index " + result.getIndex(),
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case SUCCESS_SEARCH:
                JOptionPane.showMessageDialog(this,
                        "Cars with capacity " + result.getCapacity() +
                                " found at indices: " + result.getSearchIndices(),
                        "Search Result",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case SUCCESS_GENERIC:
                JOptionPane.showMessageDialog(this,
                        result.getMessage(),
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    // --------------------------------------------------
    private JButton createPauseButton() {
        ImageIcon icon = new ImageIcon(
                new ImageIcon(getClass().getResource("/Icons/pause.png"))
                        .getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)
        );
        JButton btn = new JButton(icon);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> showPauseDialog());
        return btn;
    }

    private void showPauseDialog() {
        if (JOptionPane.showConfirmDialog(this, "Return to menu?",
                "Paused", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            new ui.LevelSelection().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UI_level1().setVisible(true));
    }
}
