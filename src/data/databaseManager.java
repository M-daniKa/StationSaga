package data;

import java.sql.*;


public class databaseManager {
    private static final String DB_URL = "jdbc:sqlite:stationsaga.db";
    private static Connection connection = null;


    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Database connection established.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
        return connection;
    }


    public static void initializeDatabase() {
        Connection conn = getConnection();
        if (conn == null) return;

        try (Statement stmt = conn.createStatement()) {
            // Player table
            String createPlayerTable = """
                CREATE TABLE IF NOT EXISTS players (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.execute(createPlayerTable);

            String createProgressTable = """
                CREATE TABLE IF NOT EXISTS progress (
                    player_id INTEGER PRIMARY KEY,
                    current_level INTEGER DEFAULT 1,
                    level1_completed BOOLEAN DEFAULT 0,
                    level2_completed BOOLEAN DEFAULT 0,
                    level3_completed BOOLEAN DEFAULT 0,
                    level4_completed BOOLEAN DEFAULT 0,
                    quiz_completed BOOLEAN DEFAULT 0,
                    quiz_score INTEGER DEFAULT 0,
                    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (player_id) REFERENCES players(id)
                )
                """;
            stmt.execute(createProgressTable);

            System.out.println("Database tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }


    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database: " + e.getMessage());
        }
    }


    public static void resetDatabase() {
        Connection conn = getConnection();
        if (conn == null) return;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS progress");
            stmt.execute("DROP TABLE IF EXISTS players");
            System.out.println("Database reset complete.");
            initializeDatabase(); // Recreate tables
        } catch (SQLException e) {
            System.err.println("Failed to reset database: " + e.getMessage());
        }
    }
}