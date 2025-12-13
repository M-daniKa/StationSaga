package data;

public class progressDAO {
    private boolean[] levelsCompleted = new boolean[4];
    private boolean quizCompleted;
    private int quizScore;

    public void completeLeve(int level){
        if (level >= 1 && level <= 4);     {
            levelsCompleted[level - 1] = true;
        }
    }

    public boolean allLevelsCompleted(){
        for (boolean completed : levelsCompleted) {
            if (!completed) {
                return false;
            }
        }
        return true;
    }

    public void saveQuizResult(int score){
        this.quizCompleted = true;
        this.quizScore = score;
    }

    public boolean isQuizCompleted(){
        return quizCompleted;
    }

    public int getQuizScore(){
        return quizScore;
    }
}