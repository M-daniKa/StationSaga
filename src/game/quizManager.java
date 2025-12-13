package game;
import entities.quizQuestion;
import data.progressDAO;
import data.quizDataLoader;
import java.util.List;
public class quizManager {
    private List<quizQuestion> questions;
    private progressDAO progress;
    private int score;

    public quizManager(progressDAO progress) {
        this.progress = progress;
        this.questions = quizDataLoader.loadQuizQuestions();
    }

    public void answerQuestion(quizQuestion question, String answer) {
        if (question.getCorrectAnswer().equals(answer)) {
            score++;
        }
    }

    public void finishQuiz() {
        progress.saveQuizResult(score);
    }

    public List<quizQuestion> getQuiz(){
       return questions;
    }

    public int getScore(){
        return score;
    }
}
