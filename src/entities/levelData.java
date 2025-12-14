// Language: java
package entities;

import java.util.List;

public class levelData {
    private int levelNumber;
    private String station;
    private List<String> jonaDialogue;
    private List<String> explanations;
    private List<DialogueEntry> entries; // merged, ordered

    public levelData(int levelNumber, String station,
                     List<String> jonaDialogue,
                     List<String> explanations,
                     List<DialogueEntry> entries) {
        this.levelNumber = levelNumber;
        this.station = station;
        this.jonaDialogue = jonaDialogue;
        this.explanations = explanations;
        this.entries = entries;
    }

    public int getLevelNumber() { return levelNumber; }
    public String getStation() { return station; }
    public List<String> getJonaDialogue() { return jonaDialogue; }
    public List<String> getExplanations() { return explanations; }
    public List<DialogueEntry> getEntries() { return entries; }
}
