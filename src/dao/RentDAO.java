package dao;

import database.DBConnection;
import model.Rent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentDAO {

    // Add Rent
  public boolean addRent(Rent rent) {

    try {

        Connection con =
                DBConnection.getConnection();

        String sql =
"INSERT INTO rent(student_code, student_name, month, room_no, room_rent, electric_bill, meter_reading, total, status) VALUES(?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1, rent.getStudentCode());
        ps.setString(2, rent.getStudentName());
        ps.setString(3, rent.getMonth());
        ps.setInt(4, rent.getRoomNo());
        ps.setDouble(5, rent.getRoomRent());
        ps.setDouble(6, rent.getElectricBill());
        ps.setString(7, rent.getMeterReading());
        ps.setDouble(8, rent.getTotal());
        ps.setString(9, rent.getStatus());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    // Get All Rent Records
    public List<Rent> getAllRent() {

        List<Rent> rents =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM rent";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                Rent rent =
                        new Rent();

                rent.setId( rs.getInt("id"));
                rent.setStudentCode( rs.getString("student_code"));
                rent.setStudentName( rs.getString("student_name"));
                rent.setMonth( rs.getString("month"));
                rent.setRoomNo(rs.getInt("room_no"));
                rent.setRoomRent( rs.getDouble("room_rent"));
                rent.setElectricBill(rs.getDouble("electric_bill"));
                rent.setMeterReading(rs.getString("meter_reading"));
                rent.setTotal(rs.getDouble("total"));

                rent.setStatus( rs.getString("status"));

                rents.add(rent);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return rents;
    }

    // Get Rent By Student
    public List<Rent> getRentByStudent(
            String studentName) {

        List<Rent> rents =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM rent WHERE student_name=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, studentName);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                Rent rent =
                        new Rent();

                rent.setId( rs.getInt("id"));
                rent.setStudentName(rs.getString("student_name"));
                rent.setMonth(rs.getString("month"));
                rent.setRoomNo(rs.getInt("room_no"));
                rent.setRoomRent( rs.getDouble("room_rent"));
                rent.setElectricBill( rs.getDouble("electric_bill"));
                rent.setMeterReading(rs.getString("meter_reading"));
                rent.setTotal(rs.getDouble("total"));
                rent.setStatus( rs.getString("status"));

                rents.add(rent);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return rents;
    }

    // Get Rent By ID
    public Rent getRentById(int id) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM rent WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                Rent rent =
                        new Rent();

                rent.setId(
                        rs.getInt("id"));

                rent.setStudentName(
                        rs.getString("student_name"));

                rent.setMonth(
                        rs.getString("month"));
                 rent.setRoomNo(
                        rs.getInt("room_no"));

                rent.setRoomRent(
                        rs.getDouble("room_rent"));

                rent.setElectricBill(
                        rs.getDouble("electric_bill"));

                rent.setMeterReading(
                        rs.getString("meter_reading"));

                rent.setTotal(
                        rs.getDouble("total"));

             


                rent.setStatus(
                        rs.getString("status"));

                return rent;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update Rent
    public boolean updateRent(Rent rent) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE rent SET student_name=?, month=?, room_no=?, room_rent=?, electric_bill=?, meter_reading=?, total=?, status=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, rent.getStudentName());
            ps.setString(2, rent.getMonth());
            ps.setInt(3, rent.getRoomNo());
            ps.setDouble(4, rent.getRoomRent());
            ps.setDouble(5, rent.getElectricBill());
            ps.setString(6, rent.getMeterReading());
            ps.setDouble(7, rent.getTotal());
            ps.setString(8, rent.getStatus());
            ps.setInt(9, rent.getId());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update Rent Status
    public boolean updateRentStatus(
            int id,
            String status) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE rent SET status=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Rent
    public boolean deleteRent(int id) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "DELETE FROM rent WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public Rent getRentByStudentName(
        String studentName) {

    try {

        Connection con =
                DBConnection.getConnection();

        String sql =
                "SELECT * FROM rent WHERE student_name=?";

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1, studentName);

        ResultSet rs =
                ps.executeQuery();

        if (rs.next()) {

            Rent rent = new Rent();

            rent.setId(rs.getInt("id"));
            rent.setStudentName(
                    rs.getString("student_name"));
            rent.setMonth(
                    rs.getString("month"));
            rent.setRoomNo(
                    rs.getInt("room_no"));
            rent.setRoomRent(
                    rs.getDouble("room_rent"));
            rent.setElectricBill(
                    rs.getDouble("electric_bill"));
            rent.setMeterReading(
                    rs.getString("meter_reading"));
            rent.setTotal(
                    rs.getDouble("total"));
            rent.setStatus(
                    rs.getString("status"));

            return rent;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
}