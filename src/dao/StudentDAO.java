package dao;

import database.DBConnection;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // ── Add Student ──────────────────────────────────────────────
    public boolean addStudent(Student student) {
        try {
            Connection con = DBConnection.getConnection();

            String sql =
                "INSERT INTO students " +
                "(student_code, name, age, room_no, contact, password, " +
                " occupation, email, dob, address, floor, joining_date, photo) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,  student.getStudentCode());
            ps.setString(2,  student.getName());
            ps.setInt(3,     student.getAge());
            ps.setInt(4,     student.getRoomNo());
            ps.setString(5,  student.getContact());
            ps.setString(6,  student.getPassword());
            ps.setString(7,  student.getOccupation());
            ps.setString(8,  student.getEmail());
            ps.setString(9,  student.getDob());
            ps.setString(10, student.getAddress());
            ps.setInt(11,    student.getFloor());
            ps.setString(12, student.getJoiningDate());
            ps.setString(13, student.getPhoto());          // NEW

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Student Login ─────────────────────────────────────────────
    public boolean loginStudent(String name, String password) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM students WHERE name=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Get Student By Name ───────────────────────────────────────
    public Student getStudentByName(String name) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM students WHERE name=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapStudent(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── View All Students ─────────────────────────────────────────
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            // Exclude photo from list query for performance (photos are large)
            String sql =
                "SELECT id, student_code, name, age, room_no, contact, password, " +
                "occupation, email, dob, address, floor, joining_date FROM students";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(mapStudentNoPhoto(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    // ── Update Student ────────────────────────────────────────────
    public boolean updateStudent(Student student) {
        try {
            Connection con = DBConnection.getConnection();

            // If a new photo was supplied, update it too; otherwise keep existing
            boolean hasPhoto = student.getPhoto() != null && !student.getPhoto().isEmpty();

            String sql = hasPhoto
                ? "UPDATE students SET age=?, room_no=?, contact=?, password=?, " +
                  "occupation=?, email=?, dob=?, address=?, floor=?, joining_date=?, photo=? " +
                  "WHERE name=?"
                : "UPDATE students SET age=?, room_no=?, contact=?, password=?, " +
                  "occupation=?, email=?, dob=?, address=?, floor=?, joining_date=? " +
                  "WHERE name=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,     student.getAge());
            ps.setInt(2,     student.getRoomNo());
            ps.setString(3,  student.getContact());
            ps.setString(4,  student.getPassword());
            ps.setString(5,  student.getOccupation());
            ps.setString(6,  student.getEmail());
            ps.setString(7,  student.getDob());
            ps.setString(8,  student.getAddress());
            ps.setInt(9,     student.getFloor());
            ps.setString(10, student.getJoiningDate());
            if (hasPhoto) {
                ps.setString(11, student.getPhoto());
                ps.setString(12, student.getName());
            } else {
                ps.setString(11, student.getName());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Delete Student ────────────────────────────────────────────
    public boolean deleteStudent(String name) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM students WHERE name=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Total Students ────────────────────────────────────────────
    public int getStudentCount() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM students";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ── Helpers ───────────────────────────────────────────────────

    /** Full mapping including photo (used by GetStudentHandler) */
    private Student mapStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setStudentCode(rs.getString("student_code"));
        s.setName(rs.getString("name"));
        s.setAge(rs.getInt("age"));
        s.setRoomNo(rs.getInt("room_no"));
        s.setContact(rs.getString("contact"));
        s.setPassword(rs.getString("password"));
        s.setOccupation(rs.getString("occupation"));
        s.setEmail(rs.getString("email"));
        s.setDob(rs.getString("dob"));
        s.setAddress(rs.getString("address"));
        s.setFloor(rs.getInt("floor"));
        s.setJoiningDate(rs.getString("joining_date"));
        s.setPhoto(rs.getString("photo"));             // NEW
        return s;
    }

    /** Mapping without photo (used by ViewStudentsHandler for performance) */
    private Student mapStudentNoPhoto(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setStudentCode(rs.getString("student_code"));
        s.setName(rs.getString("name"));
        s.setAge(rs.getInt("age"));
        s.setRoomNo(rs.getInt("room_no"));
        s.setContact(rs.getString("contact"));
        s.setPassword(rs.getString("password"));
        s.setOccupation(rs.getString("occupation"));
        s.setEmail(rs.getString("email"));
        s.setDob(rs.getString("dob"));
        s.setAddress(rs.getString("address"));
        s.setFloor(rs.getInt("floor"));
        s.setJoiningDate(rs.getString("joining_date"));
        return s;
    }
}