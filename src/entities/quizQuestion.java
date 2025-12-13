package entities;
import java.util.*;
public class quizQuestion {
    private String question;
    private Map<String, String> choices;
    private String correctAnswer;

    public quizQuestion (String question, Map<String, String> choices, String correctAnswer) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public Map<String, String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
