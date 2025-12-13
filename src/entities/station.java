package entities;

public class station {
    private String name;
    private int levelNumber;

    public station(String name, int levelNumber) {
        this.name = name;
        this.levelNumber = levelNumber;
    }

    public String getName() {
        return name;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
