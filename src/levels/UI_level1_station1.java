// Language: java
package levels;

import core.trackLinkedList;
import data.levelDataLoader;
import entities.DialogueEntry;
import entities.levelData;
import entities.trainCar;
import ui.LevelSelection;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public class UI_level1_station1 extends JFrame {

    private final levelData levelData;
    private int dialogueIndex = 0;
    private JLabel dialogueLabel;
    private JPanel trainPanel;

    private static final Color FILL_COLOR = new Color(0xFFF4D7);
    private static final Color BORDER_COLOR = new Color(0x826237);

    // Shared across Station 1 -> Station 2
    public static trackLinkedList track = new trackLinkedList();

    // gating
    private boolean canAdd;
    private boolean canDelete;
    private boolean canSearch;
    private boolean canSwap;
    private boolean canSort;

    private boolean deleteDone;
    private boolean searchDone;
    private boolean swapDone;
    private boolean sortDone;

    private JButton btnAdd, btnDelete, btnSearch, btnSwap, btnSort;
    private JButton nextButtonRef;

    private enum RequiredAction { NONE, ADD, DELETE, SEARCH, SWAP, SORT }
    private UI_level1_station1.RequiredAction currentRequired = UI_level1_station1.RequiredAction.NONE;

    public UI_level1_station1() {
        this.levelData = levelDataLoader.loadLevel(1, 1);

        setTitle("Station Saga - Level 1 (Station 1)");
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

        // Build all sections
        buildTrainPanel(mainPanel);
        buildTop(mainPanel);
        buildDialogue(mainPanel);
        buildBottomButtons(mainPanel);

        resetPermissions();
        showCurrentDialogue();
        updateNextEnabled();
    }

    // -------------------- TOP --------------------
    private void resetPermissions() {
        canAdd = false; canDelete = false; canSearch = false; canSwap = false; canSort = false;
    }

    private void resetRequirementProgress(RequiredAction action) {
        switch (action) {
            case DELETE -> deleteDone = false;
            case SEARCH -> searchDone = false;
            case SWAP -> swapDone = false;
            case SORT -> sortDone = false;
            default -> {}
        }
    }

    private int requiredAddCount = 0;
    private int currentAddCount = 0;

    private void updateControllerState(DialogueEntry entry) {
        resetPermissions();

        RequiredAction newRequired = RequiredAction.NONE;

        if (entry.getText().toLowerCase().contains("add")) {
            newRequired = RequiredAction.ADD;
            canAdd = true;

            requiredAddCount = 1;
            currentAddCount = 0;
        }

        if (newRequired != currentRequired) {
            resetRequirementProgress(newRequired);
        }

        currentRequired = newRequired;
        updateNextEnabled();
    }


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

    // -------------------- DIALOGUE --------------------
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
        nextButtonRef = nextButton;

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

    // -------------------- ACTION BOX --------------------
    private void buildBottomButtons(JPanel mainPanel) {
        JPanel box = new JPanel() {
            @Override
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
                btn.setBackground(BORDER_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BORDER_COLOR);
            }
        });

        return btn;
    }

    // -------------------- TRAIN PANEL --------------------
    private void buildTrainPanel(JPanel mainPanel) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setLayout(null);

        trainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int totalCars = track.getSize();
                if (totalCars == 0) return;

                int carWidth = 240;
                int carHeight = 150;
                int gap = 3;

                int trainWidth = totalCars * carWidth + (totalCars - 1) * gap;

                int startX = (getWidth() - trainWidth) / 2;
                int y = (int) (getHeight() * 0.68);

                int x = startX;
                for (int i = 0; i < totalCars; i++) {
                    trainCar car = track.getCarAt(i);
                    if (car == null || car.getImagePath() == null) continue;

                    Image img = new ImageIcon(getClass().getResource(car.getImagePath())).getImage();
                    g.drawImage(img, x, y, carWidth, carHeight, this);
                    x += carWidth + gap;
                }
            }
        };
        trainPanel.setOpaque(false);

        trainPanel.setBounds(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
        layeredPane.add(trainPanel, JLayeredPane.PALETTE_LAYER);

        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = mainPanel.getWidth();
                int h = mainPanel.getHeight();
                layeredPane.setBounds(0, 0, w, h);
                trainPanel.setBounds(0, 0, w, h);
                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });

        layeredPane.setBounds(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
        mainPanel.add(layeredPane, BorderLayout.CENTER);
    }

    // -------------------- DIALOGUE + GATING --------------------
    private void showCurrentDialogue() {
        nextButtonRef.setEnabled(false);

        List<DialogueEntry> entries = levelData.getEntries();
        if (entries.isEmpty()) return;

        DialogueEntry entry = entries.get(dialogueIndex);
        dialogueLabel.setText("<html><center>" + entry.getText() + "</center></html>");

        updateControllerState(entry);
    }


    private void showNextDialogue() {
        if (!isRequirementSatisfied()) {
            JOptionPane.showMessageDialog(this,
                    "Complete the required action before continuing.",
                    "Action required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nextIndex = dialogueIndex + 1;

        if (nextIndex >= levelData.getEntries().size()) {
            // move to Station 2
            dispose();
            new UI_level1_station2().setVisible(true);
            return;
        }

        dialogueIndex = nextIndex;
        showCurrentDialogue();
    }

    private void showPreviousDialogue() {
        if (dialogueIndex > 0) {
            dialogueIndex--;
            showCurrentDialogue();
        }
        updateNextEnabled();
    }

    private boolean canAdvanceFrom(int index) {
        return isRequirementSatisfied();
    }

    private boolean isRequirementSatisfied() {
        return switch (currentRequired) {
            case NONE -> true;
            case ADD -> currentAddCount >= requiredAddCount;
            case DELETE -> deleteDone;
            case SEARCH -> searchDone;
            case SWAP -> swapDone;
            case SORT -> sortDone;
        };
    }

    private void updateNextEnabled() {
        if (nextButtonRef != null) {
            nextButtonRef.setEnabled(isRequirementSatisfied());
        }
    }

    // -------------------- ACTIONS --------------------
    private void setupActions() {
        btnAdd.addActionListener(e -> {
            if (!canAdd) {
                JOptionPane.showMessageDialog(this,
                        "You are not allowed to add here.\nWait until the dialogue tells you to Add.",
                        "Not allowed",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            handleResult(performAddCar());
            updateNextEnabled();
        });

        btnDelete.addActionListener(e -> {
            if (!canDelete) {
                JOptionPane.showMessageDialog(this,
                        "You are not allowed to delete yet.\nWait until the dialogue tells you to Delete.",
                        "Not allowed yet",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            withIntInput("Index to delete:", idx -> {
                handleResult(performDeleteByIndex(idx));
                updateNextEnabled();
                return null;
            });
        });

        btnSearch.addActionListener(e -> {
            if (!canSearch) {
                JOptionPane.showMessageDialog(this,
                        "You are not allowed to search in this station.",
                        "Not allowed",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            withIntInput("Capacity to search:", cap -> {
                handleResult(performSearchByCapacity(trainCar.carType.PASSENGER, cap));
                updateNextEnabled();
                return null;
            });
        });

        btnSwap.addActionListener(e -> {
            if (!canSwap) {
                JOptionPane.showMessageDialog(this,
                        "You are not allowed to swap in this station.",
                        "Not allowed",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            withIntInput("First index to swap:", i1 -> {
                withIntInput("Second index to swap:", i2 -> {
                    handleResult(performSwap(i1, i2));
                    updateNextEnabled();
                    return null;
                });
                return null;
            });
        });

        btnSort.addActionListener(e -> {
            if (!canSort) {
                JOptionPane.showMessageDialog(this,
                        "You are not allowed to sort in this station.",
                        "Not allowed",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            handleResult(performSortAscending());
            updateNextEnabled();
        });
    }

    // -------------------- PAUSE --------------------
    private UI_level1_station2.ActionResult performAddCar() {
        if (!canAdd) return UI_level1_station2.ActionResult.notAllowed("You can only add a car when instructed.");

        boolean isFirstNode = (track.getSize() == 0);

        trainCar car = new trainCar(trainCar.carType.PASSENGER, 10, trainCar.carState.AVAILABLE);

        if (isFirstNode) {
            car.setImagePath("/Train/Head.png");
        } else {
            car.setImagePath("/Train/TrainCar.png");
        }

        if (canAdd) {
            currentAddCount++;
        }

        track.addCar(car);
        trainPanel.repaint();
        int index = track.getSize() - 1;

        return UI_level1_station2.ActionResult.successAdded(car, index);
    }

    private UI_level1_station2.ActionResult performDeleteByIndex(int index) {
        if (!canDelete) return UI_level1_station2.ActionResult.notAllowed("You can only delete when instructed.");
        if (index < 0) return UI_level1_station2.ActionResult.error("Index must be non-negative.");

        boolean removed = track.removeByIndex(index);
        if (!removed) return UI_level1_station2.ActionResult.error("No car at index " + index);

        trainPanel.repaint();

        deleteDone = true;
        return UI_level1_station2.ActionResult.successDeleted(index);
    }

    private UI_level1_station2.ActionResult performSearchByCapacity(trainCar.carType type, int capacity) {
        if (!canSearch) return UI_level1_station2.ActionResult.notAllowed("You can only search when instructed.");
        if (capacity < 0) return UI_level1_station2.ActionResult.error("Capacity must be non-negative.");

        List<Integer> result = track.searchByCapacity(type, capacity);
        searchDone = true;
        return UI_level1_station2.ActionResult.successSearch(result, capacity);
    }

    private UI_level1_station2.ActionResult performSwap(int index1, int index2) {
        if (!canSwap) return UI_level1_station2.ActionResult.notAllowed("You can only swap when instructed.");
        if (index1 < 0 || index2 < 0) return UI_level1_station2.ActionResult.error("Invalid index.");

        boolean ok = track.swapByIndex(index1, index2);
        if (!ok) return UI_level1_station2.ActionResult.error("Swap failed.");

        trainPanel.repaint();

        swapDone = true;
        return UI_level1_station2.ActionResult.successGeneric("Cars swapped successfully.");
    }

    private UI_level1_station2.ActionResult performSortAscending() {
        if (!canSort) return UI_level1_station2.ActionResult.notAllowed("You can only sort when instructed.");

        track.sortByCapacityAscending();
        trainPanel.repaint();

        sortDone = true;
        return UI_level1_station2.ActionResult.successGeneric("Train sorted from lightest to heaviest.");
    }
    private void withIntInput(String prompt, Function<Integer, Void> handler) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, prompt);
            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "You must enter a value to continue.",
                        "Input required",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }
            try {
                int value = Integer.parseInt(input.trim());
                handler.apply(value);
                break;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid integer.",
                        "Invalid input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleResult(UI_level1_station2.ActionResult result) {
        switch (result.getType()) {
            case NOT_ALLOWED -> JOptionPane.showMessageDialog(this,
                    result.getMessage(),
                    "Not allowed yet",
                    JOptionPane.WARNING_MESSAGE);
            case ERROR -> JOptionPane.showMessageDialog(this,
                    result.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            case SUCCESS_ADD -> {
                JOptionPane.showMessageDialog(this,
                        "Added car at index " + result.getIndex(),
                        "Car added",
                        JOptionPane.INFORMATION_MESSAGE);
                showNextDialogue();
            }
            case SUCCESS_DELETE -> {
                JOptionPane.showMessageDialog(this,
                        "Deleted car at index " + result.getIndex(),
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                showNextDialogue();
            }
            case SUCCESS_SEARCH -> {
                JOptionPane.showMessageDialog(this,
                        "Found cars at indices: " + result.getSearchIndices() +
                                " with capacity " + result.getCapacity(),
                        "Search result",
                        JOptionPane.INFORMATION_MESSAGE);
                showNextDialogue();
            }
            case SUCCESS_GENERIC -> {
                JOptionPane.showMessageDialog(this,
                        result.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                showNextDialogue();
            }
        }
    }
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
            new LevelSelection().setVisible(true);
        }
    }

    public static class ActionResult {
        public enum Type {
            SUCCESS_ADD, SUCCESS_DELETE, SUCCESS_SEARCH, SUCCESS_GENERIC, NOT_ALLOWED, ERROR
        }

        private final UI_level1_station1.ActionResult.Type type;
        private final String message;
        private final trainCar addedCar;
        private final Integer index;
        private final List<Integer> searchIndices;
        private final Integer capacity;

        private ActionResult(UI_level1_station1.ActionResult.Type type, String message,
                             trainCar car, Integer index,
                             List<Integer> searchIndices, Integer capacity) {
            this.type = type;
            this.message = message;
            this.addedCar = car;
            this.index = index;
            this.searchIndices = searchIndices;
            this.capacity = capacity;
        }

        public static UI_level1_station1.ActionResult notAllowed(String msg) {
            return new UI_level1_station1.ActionResult(UI_level1_station1.ActionResult.Type.NOT_ALLOWED, msg, null, null, null, null);
        }

        public static UI_level1_station1.ActionResult error(String msg) {
            return new UI_level1_station1.ActionResult(UI_level1_station1.ActionResult.Type.ERROR, msg, null, null, null, null);
        }

        public static UI_level1_station1.ActionResult successAdded(trainCar car, int index) {
            return new UI_level1_station1.ActionResult(UI_level1_station1.ActionResult.Type.SUCCESS_ADD, null, car, index, null, null);
        }

        public static UI_level1_station1.ActionResult successDeleted(int index) {
            return new UI_level1_station1.ActionResult(UI_level1_station1.ActionResult.Type.SUCCESS_DELETE, null, null, index, null, null);
        }

        public static UI_level1_station1.ActionResult successSearch(List<Integer> indices, int capacity) {
            return new UI_level1_station1.ActionResult(UI_level1_station1.ActionResult.Type.SUCCESS_SEARCH, null, null, null, indices, capacity);
        }

        public static UI_level1_station1.ActionResult successGeneric(String msg) {
            return new UI_level1_station1.ActionResult(UI_level1_station1.ActionResult.Type.SUCCESS_GENERIC, msg, null, null, null, null);
        }

        public String getMessage() { return message; }
        public trainCar getAddedCar() { return addedCar; }
        public Integer getIndex() { return index; }
        public List<Integer> getSearchIndices() { return searchIndices; }
        public Integer getCapacity() { return capacity; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UI_level1_station1().setVisible(true));
    }
}
