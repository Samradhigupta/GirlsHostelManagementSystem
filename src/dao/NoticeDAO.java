package dao;

import database.DBConnection;
import model.Notice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDAO {

    // Add Notice
    public boolean addNotice(Notice notice) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO notices(title, message, date_posted) VALUES(?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, notice.getTitle());
            ps.setString(2, notice.getMessage());
            ps.setString(3, notice.getDatePosted());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // View All Notices
    public List<Notice> getAllNotices() {

        List<Notice> notices = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM notices ORDER BY date_posted DESC";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                Notice notice = new Notice();

                notice.setId(rs.getInt("id"));
                notice.setTitle(rs.getString("title"));
                notice.setMessage(rs.getString("message"));
                notice.setDatePosted(
                        rs.getString("date_posted"));

                notices.add(notice);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return notices;
    }

    // Get Notice By ID
    public Notice getNoticeById(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM notices WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                Notice notice = new Notice();

                notice.setId(rs.getInt("id"));
                notice.setTitle(rs.getString("title"));
                notice.setMessage(rs.getString("message"));
                notice.setDatePosted(
                        rs.getString("date_posted"));

                return notice;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update Notice
    public boolean updateNotice(Notice notice) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "UPDATE notices SET title=?, message=?, date_posted=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, notice.getTitle());
            ps.setString(2, notice.getMessage());
            ps.setString(3, notice.getDatePosted());
            ps.setInt(4, notice.getId());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Notice
    public boolean deleteNotice(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "DELETE FROM notices WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Total Notices
    public int getNoticeCount() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT COUNT(*) FROM notices";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return rs.getInt(1);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}