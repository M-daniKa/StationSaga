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

    private quizManager quizManager;
    private List<quizQuestion> questions;
    private int currentIndex = 0;

    private JLabel questionLabel;
    private JLabel selectedAnswerLabel;
    private JPanel choicesPanel;

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

    // ===== BACKGROUND PANEL (FROM MAIN MENU STYLE) =====
    private JPanel createBackgroundPanel() {
        return new JPanel(new GridBagLayout()) {

            private final Image backgroundImg =
                    new ImageIcon(getClass().getResource("/Background/background.png")).getImage();
            private final Image borderImg =
                    new ImageIcon(getClass().getResource("/Background/border.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
                g.drawImage(borderImg, 0, 0, getWidth(), getHeight(), this);
            }

            {
                add(createQuizPanel());
            }
        };
    }

    // ===== QUIZ CONTENT =====
    private JPanel createQuizPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 30));
        questionLabel.setForeground(BORDER_COLOR);
        gbc.gridy = 0;
        panel.add(questionLabel, gbc);

        selectedAnswerLabel = new JLabel(" ");
        selectedAnswerLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        selectedAnswerLabel.setForeground(BORDER_COLOR);
        gbc.gridy = 1;
        panel.add(selectedAnswerLabel, gbc);

        choicesPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        choicesPanel.setOpaque(false);
        gbc.gridy = 2;
        panel.add(choicesPanel, gbc);

        return panel;
    }

    // ===== LOAD QUESTIONS =====
    private void loadQuestion() {
        if (currentIndex >= questions.size()) {
            showResult();
            return;
        }

        quizQuestion q = questions.get(currentIndex);
        questionLabel.setText(q.getQuestion());
        selectedAnswerLabel.setText(" ");

        choicesPanel.removeAll();

        for (Map.Entry<String, String> entry : q.getChoices().entrySet()) {
            JButton btn = createChoiceButton(entry.getKey(), entry.getValue(), q);
            choicesPanel.add(btn);
        }

        revalidate();
        repaint();
    }

    // ===== STYLED BUTTON =====
    private JButton createChoiceButton(String key, String text, quizQuestion q) {
        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(500, 60));
        button.setBackground(FILL_COLOR);
        button.setForeground(BORDER_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BORDER_COLOR, 4, true));

        button.addActionListener(e -> {
            selectedAnswerLabel.setText("You selected: " + text);
            quizManager.answerQuestion(q, key);

            Timer timer = new Timer(800, evt -> {
                currentIndex++;
                loadQuestion();
            });
            timer.setRepeats(false);
            timer.start();
        });

        return button;
    }

    // ===== RESULT SCREEN =====
    private void showResult() {
        JOptionPane.showMessageDialog(
                this,
                "Quiz Finished!\nScore: " + quizManager.getScore(),
                "Results",
                JOptionPane.INFORMATION_MESSAGE
        );

        new MainMenu().setVisible(true);
        dispose();
    }
    public static void main(String[] args) {
        new QuizFrame(1).setVisible(true);
    }
}
