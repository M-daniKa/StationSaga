package game;
 // this is where it double checks if the user already has an account, or like for expackage game;

import data.databaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class authManager {

    public boolean doesAccountExist(String email) {
        try (Connection conn = databaseManager.getConnection()) {
            String query = "SELECT 1 FROM players WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    // If the query returns a result, the account exists
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking account existence: " + e.getMessage());
        }
        // If there was an error or no result, assume the account does not exist
        return false;
    }
}