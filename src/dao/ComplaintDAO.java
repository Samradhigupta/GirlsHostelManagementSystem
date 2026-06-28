package dao;

import database.DBConnection;
import model.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    // ── Student: Register a new complaint ──────────────────────────
    public boolean addComplaint(Complaint c) {

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                "INSERT INTO complaint" +
                "(student_code, student_name, title, category, description, complaint_date, status, remarks) " +
                "VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, c.getStudentCode());
            ps.setString(2, c.getStudentName());
            ps.setString(3, c.getTitle());
            ps.setString(4, c.getCategory());
            ps.setString(5, c.getDescription());
            ps.setString(6, c.getComplaintDate());
            ps.setString(7, "Pending");
            ps.setString(8, "");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Admin: View all complaints ─────────────────────────────────
    public List<Complaint> getAllComplaints() {

        List<Complaint> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();

            String sql =
    "SELECT * FROM complaint";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Student: View complaints by student_code ───────────────────
    public List<Complaint> getComplaintsByStudentCode(String studentCode) {

        List<Complaint> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();

            String sql =
    "SELECT * FROM complaint WHERE student_code=?";
            

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentCode);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Get single complaint by ID ─────────────────────────────────
    public Complaint getComplaintById(int id) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM complaint WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── Admin: Update status and remarks ──────────────────────────
    public boolean updateComplaintStatus(int id, String status, String remarks) {

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                "UPDATE complaint SET status=?, remarks=? WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Admin: Delete a complaint ──────────────────────────────────
    public boolean deleteComplaint(int id) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM complaint WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Count complaints by status (for dashboard) ─────────────────
    public int countByStatus(String status) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM complaint WHERE status=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ── Helper: map ResultSet row → Complaint ──────────────────────
    private Complaint mapRow(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setId(rs.getInt("id"));
        c.setStudentCode(rs.getString("student_code"));
        c.setStudentName(rs.getString("student_name"));
        c.setTitle(rs.getString("title"));
        c.setCategory(rs.getString("category"));
        c.setDescription(rs.getString("description"));
        c.setComplaintDate(rs.getString("complaint_date"));
        c.setStatus(rs.getString("status"));
        c.setRemarks(rs.getString("remarks"));
        return c;
    }
}