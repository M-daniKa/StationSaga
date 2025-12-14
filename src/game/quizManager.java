package game;

import entities.quizQuestion;
import data.progressDAO;
import data.quizDataLoader;
import java.util.List;

public class quizManager {
    private List<quizQuestion> questions;
    private progressDAO progress;
    private int playerId;
    private int score;

    public quizManager(int playerId) {
        this.playerId = playerId;
        this.progress = new progressDAO();
        this.questions = quizDataLoader.loadQuizQuestions();
        this.score = 0;
    }

    public void answerQuestion(quizQuestion question, String answer) {
        if (question.getCorrectAnswer().equals(answer)) {
            score++;
        }
    }

    public void finishQuiz() {
        progress.saveQuizResult(playerId, score);
    }

    public List<quizQuestion> getQuiz(){
        return questions;
    }

    public int getScore(){
        return score;
    }
}