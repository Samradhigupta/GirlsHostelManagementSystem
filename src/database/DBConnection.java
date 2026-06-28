package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // ── IMPORTANT ────────────────────────────────────────────────
    // Replace "user" and "password" below with YOUR own local MySQL
    // credentials before running the project. These are NOT shared
    // values — every developer's MySQL installation has its own
    // root password (or a dedicated user you created).
    // After editing, run database/schema.sql once to create the
    // "hostel_management" database and its tables.
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/hostel_management";
            String user = "root";
            String password = "Samradhi@19";

            Connection con = DriverManager.getConnection(url, user, password);

            System.out.println("Database Connected Successfully!");
            return con;

        } catch (Exception e) {
            System.out.println(
                "Database Connection FAILED. Check that: \n" +
                "  1) MySQL is running on localhost:3306\n" +
                "  2) The 'hostel_management' database exists (run database/schema.sql)\n" +
                "  3) The username/password in DBConnection.java match your MySQL setup"
            );
            e.printStackTrace();
            return null;
        }
    }
}
