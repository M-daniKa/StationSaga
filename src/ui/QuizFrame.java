package ui;

import game.quizManager;
import entities.quizQuestion;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class QuizFrame extends JFrame {

    private static final Color BORDER_COLOR = new Color(0x826237);
    private static final Color FILL_COLOR   = new Color(0xFFF4D7);
    private static final Color SELECTED_COLOR = new Color(0xE8D3A1);

    private quizManager quizManager;
    private List<quizQuestion> questions;
    private int currentIndex = 0;

    private JLabel questionLabel;
    private JPanel choicesPanel;
    private JPanel actionPanel;

    private String selectedKey = null;

    public QuizFrame(int playerId) {
        quizManager = new quizManager(playerId);
        questions = quizManager.getQuiz();

        setupFrame();
        add(createBackgroundPanel());
        loadQuestion();
    }

    private void setupFrame() {
        setTitle("Station Saga - Quiz");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ===== BACKGROUND =====
    private JPanel createBackgroundPanel() {
        return new JPanel(new GridBagLayout()) {

            private final Image bg =
                    new ImageIcon(getClass().getResource("/Background/background.png")).getImage();
            private final Image border =
                    new ImageIcon(getClass().getResource("/Background/border.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                g.drawImage(border, 0, 0, getWidth(), getHeight(), this);
            }

            {
                add(createQuizCard());
            }
        };
    }

    // ===== MAIN QUIZ CARD =====
    private JPanel createQuizCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(FILL_COLOR);
        card.setBorder(new LineBorder(BORDER_COLOR, 6, true));
        card.setPreferredSize(new Dimension(900, 550));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30);
        gbc.gridx = 0;

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setForeground(BORDER_COLOR);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 28));
        questionLabel.setPreferredSize(new Dimension(800, 120));
        gbc.gridy = 0;
        card.add(questionLabel, gbc);

        choicesPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        choicesPanel.setOpaque(false);
        gbc.gridy = 1;
        card.add(choicesPanel, gbc);

        actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        actionPanel.setOpaque(false);
        actionPanel.setVisible(false);
        gbc.gridy = 2;
        card.add(actionPanel, gbc);

        return card;
    }

    // ===== LOAD QUESTION =====
    private void loadQuestion() {
        if (currentIndex >= questions.size()) {
            showFinalScreen();
            return;
        }

        quizQuestion q = questions.get(currentIndex);
        selectedKey = null;

        questionLabel.setText(
                "<html><div style='text-align:center;width:780px;'>"
                        + q.getQuestion() + "</div></html>"
        );

        choicesPanel.removeAll();
        actionPanel.removeAll();
        actionPanel.setVisible(false);

        for (Map.Entry<String, String> entry : q.getChoices().entrySet()) {
            choicesPanel.add(createChoiceButton(entry.getKey(), entry.getValue()));
        }

        revalidate();
        repaint();
    }

    // ===== CHOICE BUTTON =====
    private JButton createChoiceButton(String key, String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setBackground(FILL_COLOR);
        button.setForeground(BORDER_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BORDER_COLOR, 3, true));
        button.setPreferredSize(new Dimension(760, 60));

        button.addActionListener(e -> {
            selectedKey = key;
            highlightSelection(button);
            showActionButtons();
        });

        return button;
    }

    private void highlightSelection(JButton selected) {
        for (Component c : choicesPanel.getComponents()) {
            if (c instanceof JButton btn) {
                btn.setBackground(FILL_COLOR);
            }
        }
        selected.setBackground(SELECTED_COLOR);
    }

    // ===== ACTION BUTTONS =====
    private void showActionButtons() {
        actionPanel.removeAll();

        JButton back = createActionButton("BACK");
        JButton next = createActionButton("CONTINUE");

        back.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                loadQuestion();
            }
        });

        next.addActionListener(e -> {
            quizManager.answerQuestion(
                    questions.get(currentIndex),
                    selectedKey
            );
            currentIndex++;
            loadQuestion();
        });

        actionPanel.add(back);
        actionPanel.add(next);
        actionPanel.setVisible(true);
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(FILL_COLOR);
        btn.setForeground(BORDER_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(BORDER_COLOR, 3, true));
        return btn;
    }

    // ===== FINAL SCREEN =====
    private void showFinalScreen() {
        JPanel finalPanel = new JPanel(new GridBagLayout());
        finalPanel.setOpaque(false);

        JLabel congrats = new JLabel("CONGRATULATIONS!");
        congrats.setFont(new Font("Arial", Font.BOLD, 60));
        congrats.setForeground(BORDER_COLOR);

        JLabel score = new JLabel("Your Score: " + quizManager.getScore());
        score.setFont(new Font("Arial", Font.BOLD, 32));
        score.setForeground(BORDER_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridy = 0;
        finalPanel.add(congrats, gbc);
        gbc.gridy = 1;
        finalPanel.add(score, gbc);

        getContentPane().removeAll();
        add(createBackgroundPanel());
        add(finalPanel);

        revalidate();
        repaint();
    }
    public static void main(String[] args) {
        new QuizFrame(1).setVisible(true);
    }

}
