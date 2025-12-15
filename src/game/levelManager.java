// Language: java
package game;

public class levelManager {

    // \[1-based\] completion flags; expand as you add more levels
    private static boolean level1Completed = false;
    private static boolean level2Completed = false;
    private static boolean level3Completed = false;
    private static boolean level4Completed = false;

    private levelManager() {
        // utility class; no instances
    }

    // ---- Level 1 ----
    public static boolean isLevel1Completed() {
        return level1Completed;
    }

    public static void setLevel1Completed(boolean completed) {
        level1Completed = completed;
    }

    // ---- Level 2 ----
    public static boolean isLevel2Completed() {
        return level2Completed;
    }

    public static void setLevel2Completed(boolean completed) {
        level2Completed = completed;
    }

    // ---- Level 3 ----
    public static boolean isLevel3Completed() {
        return level3Completed;
    }

    public static void setLevel3Completed(boolean completed) {
        level3Completed = completed;
    }

    // ---- Level 4 ----
    public static boolean isLevel4Completed() {
        return level4Completed;
    }

    public static void setLevel4Completed(boolean completed) {
        level4Completed = completed;
    }
}
