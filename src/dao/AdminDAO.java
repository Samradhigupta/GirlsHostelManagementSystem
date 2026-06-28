package dao;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    public boolean loginAdmin(String username,
                              String password) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM admins WHERE username=? AND password=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            boolean found = rs.next();

            System.out.println(
                    "Admin Login Result = " + found);

            return found;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}