package data;
import entities.levelData;
import java.io.*;
import java.util.*;
public class levelDataLoader {
    public static levelData loadLevel(int levelNumber) {
    String fileName = "/level" + levelNumber + ".txt";
    String station = "";
    List<String> jonaDialogue = new ArrayList<>();
    List<String> explanations = new ArrayList<>();
    try (InputStream is = levelDataLoader.class.getResourceAsStream(fileName);
         BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
        String line;
        String section = "";
        while ((line = reader.readLine()) != null) {
            if (line.isBlank()) continue;
            if (line.endsWith(":")) {
                section = line.replace(":", "");
                continue;
            }

            switch (section) {
                case "STATION" -> station = line;
                case "JONA_DIALOGUE" -> jonaDialogue.add(line);
                case "EXPLANATION" -> explanations.add(line);
            }
        }
    } catch (Exception e) {
        System.out.println("Error loading level data: " + e.getMessage());
    }
    return new levelData(levelNumber, station, jonaDialogue, explanations);
    }
}
