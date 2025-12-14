package data;

import java.sql.*;

/**
 * Manages player authentication (login/signup) and account data.
 */
public class saveFileManager {

    /**
     * Registers a new player.
     * Returns player ID if successful, -1 if email already exists.
     */
    public static int registerPlayer(String name, String email, String password) {
        // Check if email already exists
        if (playerExists(email)) {
            System.out.println("Email already registered.");
            return -1;
        }

        String sql = "INSERT INTO players (name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password); // In real app, hash this!
            pstmt.executeUpdate();

            // Get generated player ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int playerId = rs.getInt(1);
                System.out.println("Player registered successfully. ID: " + playerId);

                // Create progress record for new player
                progressDAO dao = new progressDAO();
                dao.createProgress(playerId);

                return playerId;
            }

        } catch (SQLException e) {
            System.err.println("Failed to register player: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Logs in a player.
     * Returns player ID if successful, -1 if credentials are wrong.
     */
    public static int loginPlayer(String email, String password) {
        String sql = "SELECT id, password FROM players WHERE email = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    int playerId = rs.getInt("id");
                    System.out.println("Login successful. Player ID: " + playerId);
                    return playerId;
                } else {
                    System.out.println("Incorrect password.");
                }
            } else {
                System.out.println("Email not found.");
            }

        } catch (SQLException e) {
            System.err.println("Failed to login: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Checks if a player with this email exists.
     */
    public static boolean playerExists(String email) {
        String sql = "SELECT id FROM players WHERE email = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Failed to check player existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Gets player name by ID.
     */
    public static String getPlayerName(int playerId) {
        String sql = "SELECT name FROM players WHERE id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }

        } catch (SQLException e) {
            System.err.println("Failed to get player name: " + e.getMessage());
        }
        return "Unknown";
    }

    /**
     * Updates player name.
     */
    public static void updatePlayerName(int playerId, String newName) {
        String sql = "UPDATE players SET name = ? WHERE id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newName);
            pstmt.setInt(2, playerId);
            pstmt.executeUpdate();
            System.out.println("Player name updated.");

        } catch (SQLException e) {
            System.err.println("Failed to update player name: " + e.getMessage());
        }
    }

    /**
     * Deletes a player account (and their progress).
     */
    public static void deletePlayer(int playerId) {
        String deleteProgress = "DELETE FROM progress WHERE player_id = ?";
        String deletePlayer = "DELETE FROM players WHERE id = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(deleteProgress);
             PreparedStatement pstmt2 = conn.prepareStatement(deletePlayer)) {

            // Delete progress first (foreign key)
            pstmt1.setInt(1, playerId);
            pstmt1.executeUpdate();

            // Delete player
            pstmt2.setInt(1, playerId);
            pstmt2.executeUpdate();

            System.out.println("Player account deleted.");

        } catch (SQLException e) {
            System.err.println("Failed to delete player: " + e.getMessage());
        }
    }
}