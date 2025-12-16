package data;

import java.sql.*;

public class progressDAO {
    public void createProgress(int playerId) {
        String sql = "INSERT INTO progress (player_id) VALUES (?)";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            pstmt.executeUpdate();
            System.out.println("Progress record created for player ID: " + playerId);

        } catch (SQLException e) {
            System.err.println("Failed to create progress: " + e.getMessage());
        }
    }


    public void completeLevel(int playerId, int level) {
        if (level < 1 || level > 4) {
            System.err.println("Invalid level number: " + level);
            return;
        }

        String columnName = "level" + level + "_completed";
        String sql = "UPDATE progress SET " + columnName + " = 1, last_updated = CURRENT_TIMESTAMP WHERE player_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            pstmt.executeUpdate();
            System.out.println("Level " + level + " marked as completed.");

        } catch (SQLException e) {
            System.err.println("Failed to complete level: " + e.getMessage());
        }
    }


    public boolean isLevelCompleted(int playerId, int level) {
        if (level < 1 || level > 4) return false;

        String columnName = "level" + level + "_completed";
        String sql = "SELECT " + columnName + " FROM progress WHERE player_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            }

        } catch (SQLException e) {
            System.err.println("Failed to check level completion: " + e.getMessage());
        }
        return false;
    }

    public boolean allLevelsCompleted(int playerId) {
        String sql = """
            SELECT level1_completed, level2_completed, level3_completed, level4_completed 
            FROM progress WHERE player_id = ?
            """;

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("level1_completed") &&
                        rs.getBoolean("level2_completed") &&
                        rs.getBoolean("level3_completed") &&
                        rs.getBoolean("level4_completed");
            }

        } catch (SQLException e) {
            System.err.println("Failed to check all levels: " + e.getMessage());
        }
        return false;
    }

    public void updateCurrentLevel(int playerId, int level) {
        String sql = "UPDATE progress SET current_level = ?, last_updated = CURRENT_TIMESTAMP WHERE player_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, level);
            pstmt.setInt(2, playerId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to update current level: " + e.getMessage());
        }
    }

    public int getCurrentLevel(int playerId) {
        String sql = "SELECT current_level FROM progress WHERE player_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("current_level");
            }

        } catch (SQLException e) {
            System.err.println("Failed to get current level: " + e.getMessage());
        }
        return 1; // Default to level 1
    }

    public void saveQuizResult(int playerId, int score) {
        String sql = "UPDATE progress SET quiz_completed = 1, quiz_score = ?, last_updated = CURRENT_TIMESTAMP WHERE player_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, score);
            pstmt.setInt(2, playerId);
            pstmt.executeUpdate();
            System.out.println("Quiz result saved: " + score);

        } catch (SQLException e) {
            System.err.println("Failed to save quiz result: " + e.getMessage());
        }
    }

    public boolean isQuizCompleted(int playerId) {
        String sql = "SELECT quiz_completed FROM progress WHERE player_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("quiz_completed");
            }

        } catch (SQLException e) {
            System.err.println("Failed to check quiz completion: " + e.getMessage());
        }
        return false;
    }

    public int getQuizScore(int playerId) {
        String sql = "SELECT quiz_score FROM progress WHERE player_id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quiz_score");
            }

        } catch (SQLException e) {
            System.err.println("Failed to get quiz score: " + e.getMessage());
        }
        return 0;
    }
}