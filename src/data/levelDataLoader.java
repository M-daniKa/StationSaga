// Language: java
package data;

import entities.DialogueEntry;
import entities.levelData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class levelDataLoader {

    public static levelData loadLevel(int levelNumber) {
        return loadLevel(levelNumber, 1);
    }

    public static levelData loadLevel(int levelNumber, int stationNumber) {
        String resourcePath = "/level" + levelNumber + ".txt";
        InputStream is = levelDataLoader.class.getResourceAsStream(resourcePath);

        String station = "";
        List<String> jonaDialogue = new ArrayList<>();
        List<String> explanations = new ArrayList<>();
        List<DialogueEntry> entries = new ArrayList<>();

        if (is == null) {
            System.out.println("Missing level resource: " + resourcePath);
            return new levelData(levelNumber, station, jonaDialogue, explanations, entries);
        }

        int foundStationsInTargetLevel = 0;
        boolean inTargetLevel = false;
        boolean inTargetStation = false;
        String currentSection = ""; // "LEVEL", "STATION", "DIALOGUE"

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.equalsIgnoreCase("LEVEL:")) {
                    currentSection = "LEVEL";
                    inTargetStation = false;
                    continue;
                }
                if (line.equalsIgnoreCase("STATION:")) {
                    currentSection = "STATION";
                    continue;
                }
                if (line.equalsIgnoreCase("DIALOGUE:")) {
                    currentSection = "DIALOGUE";
                    continue;
                }

                if (line.isEmpty()) continue;

                switch (currentSection) {
                    case "LEVEL": {
                        try {
                            int parsedLevel = Integer.parseInt(line);
                            inTargetLevel = (parsedLevel == levelNumber);
                            if (inTargetLevel) {
                                foundStationsInTargetLevel = 0;
                                station = "";
                                jonaDialogue.clear();
                                explanations.clear();
                                entries.clear();
                                inTargetStation = false;
                            }
                        } catch (NumberFormatException ignored) {
                            inTargetLevel = false;
                            inTargetStation = false;
                        }
                        break;
                    }
                    case "STATION": {
                        if (!inTargetLevel) break;
                        foundStationsInTargetLevel++;
                        inTargetStation = (foundStationsInTargetLevel == stationNumber);
                        if (inTargetStation) {
                            station = line;
                            jonaDialogue.clear();
                            explanations.clear();
                            entries.clear();
                        }
                        break;
                    }
                    case "DIALOGUE": {
                        if (inTargetLevel && inTargetStation) {
                            // everything is now a generic explanation entry
                            DialogueEntry entry =
                                    new DialogueEntry(DialogueEntry.Type.EXPLANATION, line);
                            entries.add(entry);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load level data: " + e.getMessage());
        }

        return new levelData(levelNumber, station, jonaDialogue, explanations, entries);
    }
}
