package entities;
import java.util.List;
public class levelData {
    private int levelNumber;
    private String station;
    private List<String> jonaDialogue;
    private List<String> explanations;

    public levelData(int levelNumber, String station, List<String> jonaDialogue, List<String> explanations) {
        this.levelNumber = levelNumber;
        this.station = station;
        this.jonaDialogue = jonaDialogue;
        this.explanations = explanations;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
    public String getStation() {
        return station;
    }

    public List<String> getJonaDialogue() {
        return jonaDialogue;
    }

    public List<String> getExplanations() {
        return explanations;
    }
}
