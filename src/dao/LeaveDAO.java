package dao;

import database.DBConnection;
import model.Leave;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the student_leave table.
 *
 * SQL to create the table (run once in your DB):
 * ─────────────────────────────────────────────────────────────
 * CREATE TABLE IF NOT EXISTS student_leave (
 *     id           INT AUTO_INCREMENT PRIMARY KEY,
 *     student_code VARCHAR(50),
 *     student_name VARCHAR(100),
 *     reason       VARCHAR(100),
 *     destination  VARCHAR(200),
 *     details      TEXT,
 *     leave_date   VARCHAR(20),
 *     leave_time   VARCHAR(10),
 *     return_date  VARCHAR(20),
 *     days         VARCHAR(10),
 *     status       VARCHAR(20) DEFAULT 'Pending'
 * );
 * ─────────────────────────────────────────────────────────────
 */
public class LeaveDAO {

    // ── Add a new leave request ───────────────────────────────────
    public boolean addLeave(Leave l) {
        try {
            Connection con = DBConnection.getConnection();
            String sql =
                "INSERT INTO student_leave " +
                "(student_code, student_name, reason, destination, details, " +
                " leave_date, leave_time, return_date, days, status) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, l.getStudentCode());
            ps.setString(2, l.getStudentName());
            ps.setString(3, l.getReason());
            ps.setString(4, l.getDestination());
            ps.setString(5, l.getDetails());
            ps.setString(6, l.getLeaveDate());
            ps.setString(7, l.getLeaveTime());
            ps.setString(8, l.getReturnDate());
            ps.setString(9, l.getDays());
            ps.setString(10, "Pending");

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Get all leave requests (admin view) ───────────────────────
    public List<Leave> getAllLeaves() {
        List<Leave> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM student_leave ORDER BY id DESC"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Get leaves for a specific student ─────────────────────────
    public List<Leave> getLeavesByStudentCode(String code) {
        List<Leave> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM student_leave WHERE student_code = ? ORDER BY id DESC"
            );
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Update leave status (Approve / Reject) ────────────────────
    public boolean updateLeaveStatus(int id, String status) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE student_leave SET status = ? WHERE id = ?"
            );
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Helper: map a ResultSet row to a Leave object ─────────────
    private Leave mapRow(ResultSet rs) throws SQLException {
        Leave l = new Leave();
        l.setId(rs.getInt("id"));
        l.setStudentCode(rs.getString("student_code"));
        l.setStudentName(rs.getString("student_name"));
        l.setReason(rs.getString("reason"));
        l.setDestination(rs.getString("destination"));
        l.setDetails(rs.getString("details"));
        l.setLeaveDate(rs.getString("leave_date"));
        l.setLeaveTime(rs.getString("leave_time"));
        l.setReturnDate(rs.getString("return_date"));
        l.setDays(rs.getString("days"));
        l.setStatus(rs.getString("status"));
        return l;
    }

    // ── Helper: format a Leave as a CSV row for HTTP responses ────
    public static String toCsv(Leave l) {
        return l.getId()            + "," +
               safe(l.getStudentCode())  + "," +
               safe(l.getStudentName())  + "," +
               safe(l.getReason())       + "," +
               safe(l.getDestination())  + "," +
               safe(l.getLeaveDate())    + "," +
               safe(l.getLeaveTime())    + "," +
               safe(l.getReturnDate())   + "," +
               safe(l.getDays())         + "," +
               safe(l.getStatus())       + "," +
               safe(l.getDetails());
    }

    private static String safe(String s) {
        return s == null ? "" : s.replace(",", ";");  // avoid CSV splitting on commas in values
    }
}