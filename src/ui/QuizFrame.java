// Language: java
package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import data.quizDataLoader;
import entities.quizQuestion;

public class QuizFrame extends JFrame {

    private static final Color BORDER_COLOR     = new Color(0x6B4E2E);
    private static final Color FILL_COLOR       = new Color(0xFFF4D7);
    private static final Color SELECTED_COLOR   = new Color(0xF1DFB5);
    private static final Color HOVER_COLOR      = new Color(0xFAEBC7);
    private static final Color SHADOW_COLOR     = new Color(0, 0, 0, 40);

    private final List<quizQuestion> questions;
    private final Map<Integer, String> selections = new HashMap<>();
    private int currentIndex = 0;
    private String selectedKey = null;

    private JLabel questionLabel;
    private JPanel choicesPanel;
    private JLabel selectedLabel;
    private JLabel progressLabel;
    private JButton prevBtn;
    private JButton nextBtn;

    private final java.util.List<JButton> choiceButtons = new ArrayList<>();

    private final Image backgroundImg = loadImage("/Background/background.png");
    private final Image borderImg     = loadImage("/Background/border.png");

    public QuizFrame() {
        questions = quizDataLoader.loadQuizQuestions();

        setTitle("Linked List Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImg != null) g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
                if (borderImg != null) g.drawImage(borderImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        JPanel quizPanel = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                // Soft shadow
                g2.setColor(SHADOW_COLOR);
                g2.fillRoundRect(6, 8, getWidth() - 12, getHeight() - 12, 22, 22);
                // Card
                g2.setColor(FILL_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                g2.dispose();
            }
        };
        quizPanel.setOpaque(false);
        quizPanel.setPreferredSize(new Dimension(1000, 650));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        root.add(quizPanel);

        // Header with title and progress
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Linked List Quiz", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(BORDER_COLOR);

        progressLabel = new JLabel("", SwingConstants.RIGHT);
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        progressLabel.setForeground(BORDER_COLOR);

        header.add(title, BorderLayout.WEST);
        header.add(progressLabel, BorderLayout.EAST);
        quizPanel.add(header, BorderLayout.NORTH);

        // Question
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        questionLabel.setForeground(BORDER_COLOR);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        // Choices
        choicesPanel = new JPanel(new GridLayout(4, 1, 12, 12));
        choicesPanel.setOpaque(false);
        choicesPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(questionLabel, BorderLayout.NORTH);
        center.add(choicesPanel, BorderLayout.CENTER);
        quizPanel.add(center, BorderLayout.CENTER);

        // Bottom bar
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        quizPanel.add(bottom, BorderLayout.SOUTH);

        selectedLabel = new JLabel("Selected: none", SwingConstants.LEFT);
        selectedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        selectedLabel.setForeground(BORDER_COLOR);
        selectedLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        bottom.add(selectedLabel, BorderLayout.WEST);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        nav.setOpaque(false);
        bottom.add(nav, BorderLayout.EAST);

        prevBtn = createNavButton("PREV");
        nextBtn = createNavButton("NEXT");
        nav.add(prevBtn);
        nav.add(nextBtn);

        prevBtn.addActionListener(e -> {
            if (currentIndex > 0) {
                if (selectedKey != null) selections.put(currentIndex, selectedKey);
                currentIndex--;
                selectedKey = selections.getOrDefault(currentIndex, null);
                loadQuestion();
            }
        });

        nextBtn.addActionListener(e -> {
            if (selectedKey != null) selections.put(currentIndex, selectedKey);
            int nextIndex = currentIndex + 1;

            if (nextIndex >= questions.size()) {
                // Quiz is finished
                int correct = 0;
                int incorrect = 0;
                for (int i = 0; i < questions.size(); i++) {
                    String selected = selections.get(i);
                    String correctAnswer = questions.get(i).getCorrectAnswer();
                    if (selected != null && selected.equals(correctAnswer)) correct++;
                    else incorrect++;
                }

                JOptionPane.showMessageDialog(
                        this,
                        String.format(
                                "Congratulations!\n\nYou have completed the quiz!\n\nTotal Questions: %d\nCorrect: %d\nIncorrect: %d\nScore: %d%%\n\nReturning to Level Selection...",
                                questions.size(), correct, incorrect, Math.round(100.0 * correct / questions.size())
                        ),
                        "Quiz Complete",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
                new LevelSelection().setVisible(true);
                return;
            } else {
                currentIndex = nextIndex;
                selectedKey = selections.getOrDefault(currentIndex, null);
                loadQuestion();
            }
        });

        // Keyboard shortcuts
        registerShortcuts();

        loadQuestion();
    }

    private void loadQuestion() {
        if (questions == null || questions.isEmpty()) {
            questionLabel.setText("No questions available!");
            progressLabel.setText("");
            choicesPanel.removeAll();
            choicesPanel.revalidate();
            choicesPanel.repaint();
            return;
        }

        quizQuestion q = questions.get(currentIndex);
        progressLabel.setText(String.format("Question %d of %d", currentIndex + 1, questions.size()));

        questionLabel.setText("<html><div style='text-align:center;font-weight:bold;line-height:1.35;'>" +
                escapeHtml(q.getQuestion()) + "</div></html>");

        java.util.List<String> order = Arrays.asList("A", "B", "C", "D");
        Map<String, String> choices = q.getChoices();

        choicesPanel.removeAll();
        choiceButtons.clear();

        for (String key : order) {
            if (choices.containsKey(key)) {
                JButton btn = createChoiceButton(key, choices.get(key));
                choiceButtons.add(btn);
                choicesPanel.add(btn);
                if (key.equals(selectedKey)) {
                    highlightSelection(btn);
                    selectedLabel.setText("Selected: " + key);
                }
            }
        }

        prevBtn.setEnabled(currentIndex > 0);
        nextBtn.setEnabled(true);

        choicesPanel.revalidate();
        choicesPanel.repaint();
    }

    private JButton createChoiceButton(String key, String text) {
        String html = "<html><b>" + key + ".</b> " + escapeHtml(text) + "</html>";
        JButton btn = new JButton(html);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setBackground(FILL_COLOR);
        btn.setForeground(BORDER_COLOR);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new LineBorder(BORDER_COLOR, 2, true));
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!key.equals(selectedKey)) btn.setBackground(HOVER_COLOR);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (!key.equals(selectedKey)) btn.setBackground(FILL_COLOR);
            }
        });

        btn.addActionListener(e -> {
            selectedKey = key;
            highlightSelection(btn);
            selectedLabel.setText("Selected: " + key);
        });

        return btn;
    }

    private void highlightSelection(JButton selected) {
        for (Component c : choicesPanel.getComponents()) {
            if (c instanceof JButton b) b.setBackground(FILL_COLOR);
        }
        selected.setBackground(SELECTED_COLOR);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(FILL_COLOR);
        btn.setForeground(BORDER_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(BORDER_COLOR, 2, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(HOVER_COLOR); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(FILL_COLOR); }
        });
        return btn;
    }

    private void registerShortcuts() {
        JRootPane rp = getRootPane();
        rp.registerKeyboardAction(e -> prevBtn.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        rp.registerKeyboardAction(e -> nextBtn.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            rp.registerKeyboardAction(e -> selectByIndex(idx),
                    KeyStroke.getKeyStroke(KeyEvent.VK_1 + i, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    private void selectByIndex(int idx) {
        if (idx < 0 || idx >= choiceButtons.size()) return;
        choiceButtons.get(idx).doClick();
    }

    private String keyToNumber(String key) {
        return switch (key) {
            case "A" -> "1";
            case "B" -> "2";
            case "C" -> "3";
            case "D" -> "4";
            default -> "?";
        };
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private Image loadImage(String path) {
        java.net.URL url = getClass().getResource(path);
        return (url == null) ? null : new ImageIcon(url).getImage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizFrame().setVisible(true));
    }
}
