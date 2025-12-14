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

    // Backward compatible: defaults to the first station block
    public static levelData loadLevel(int levelNumber) {
        return loadLevel(levelNumber, 1);
    }

    // New: select a station block within a level file that may contain multiple LEVEL: sections
    public static levelData loadLevel(int levelNumber, int stationNumber) {
        String resourcePath = "/level" + levelNumber + ".txt";
        InputStream is = levelDataLoader.class.getResourceAsStream(resourcePath);

        String station = "";
        List<String> jonaDialogue = new ArrayList<>();
        List<String> explanations = new ArrayList<>();

        if (is == null) {
            System.out.println("Missing level resource: " + resourcePath);
            return new levelData(levelNumber, station, jonaDialogue, explanations, new ArrayList<>());
        }

        int foundStationsInTargetLevel = 0;
        boolean inTargetLevel = false;
        boolean inTargetStation = false;
        String currentSection = ""; // "LEVEL", "STATION", "JONA_DIALOGUE", "EXPLANATION"

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Section headers
                if (line.equalsIgnoreCase("LEVEL:")) {
                    currentSection = "LEVEL";
                    inTargetStation = false; // reset when a new level block starts
                    continue;
                }
                if (line.equalsIgnoreCase("STATION:")) {
                    currentSection = "STATION";
                    continue;
                }
                if (line.equalsIgnoreCase("JONA_DIALOGUE:")) {
                    currentSection = "JONA_DIALOGUE";
                    continue;
                }
                if (line.equalsIgnoreCase("EXPLANATION:")) {
                    currentSection = "EXPLANATION";
                    continue;
                }

                // Skip empty lines
                if (line.isEmpty()) continue;

                // Parse values
                switch (currentSection) {
                    case "LEVEL": {
                        // Expect a number line following "LEVEL:"
                        try {
                            int parsedLevel = Integer.parseInt(line);
                            inTargetLevel = (parsedLevel == levelNumber);
                            // When entering target level, reset counts for station blocks
                            if (inTargetLevel) {
                                foundStationsInTargetLevel = 0;
                                station = "";
                                jonaDialogue.clear();
                                explanations.clear();
                                inTargetStation = false;
                            }
                        } catch (NumberFormatException ignored) {
                            // If the format is not a number, treat as non-target
                            inTargetLevel = false;
                            inTargetStation = false;
                        }
                        break;
                    }
                    case "STATION": {
                        if (!inTargetLevel) break;
                        // Each time we hit STATION, it starts a new station block
                        foundStationsInTargetLevel++;
                        inTargetStation = (foundStationsInTargetLevel == stationNumber);
                        // Capture the station name only for the target station
                        if (inTargetStation) {
                            station = line;
                            // Clear dialogues for the newly selected station block
                            jonaDialogue.clear();
                            explanations.clear();
                        }
                        break;
                    }
                    case "JONA_DIALOGUE": {
                        if (inTargetLevel && inTargetStation) {
                            jonaDialogue.add(line);
                        }
                        break;
                    }
                    case "EXPLANATION": {
                        if (inTargetLevel && inTargetStation) {
                            explanations.add(line);
                        }
                        break;
                    }
                    default:
                        // Lines outside known sections are ignored
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load level data: " + e.getMessage());
        }

        // Merge into ordered DialogueEntry list
        List<DialogueEntry> entries = new ArrayList<>();
        int max = Math.max(jonaDialogue.size(), explanations.size());
        for (int i = 0; i < max; i++) {
            if (i < jonaDialogue.size()) {
                entries.add(new DialogueEntry(DialogueEntry.Type.JONA, jonaDialogue.get(i)));
            }
            if (i < explanations.size()) {
                entries.add(new DialogueEntry(DialogueEntry.Type.EXPLANATION, explanations.get(i)));
            }
        }

        return new levelData(levelNumber, station, jonaDialogue, explanations, entries);
    }
}
