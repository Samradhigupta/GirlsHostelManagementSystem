package dao;

import database.DBConnection;
import model.Facility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacilityDAO {

    // Add Facility
    public boolean addFacility(Facility facility) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO facilities(facility_name, timing, description) VALUES(?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, facility.getFacilityName());
            ps.setString(2, facility.getTiming());
            ps.setString(3, facility.getDescription());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // View All Facilities
    public List<Facility> getAllFacilities() {

        List<Facility> facilities = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM facilities";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                Facility facility = new Facility();

                facility.setId(rs.getInt("id"));
                facility.setFacilityName(
                        rs.getString("facility_name"));
                facility.setTiming(
                        rs.getString("timing"));
                facility.setDescription(
                        rs.getString("description"));

                facilities.add(facility);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return facilities;
    }

    // Get Facility By ID
    public Facility getFacilityById(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM facilities WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                Facility facility = new Facility();

                facility.setId(rs.getInt("id"));
                facility.setFacilityName(
                        rs.getString("facility_name"));
                facility.setTiming(
                        rs.getString("timing"));
                facility.setDescription(
                        rs.getString("description"));

                return facility;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update Facility
    public boolean updateFacility(Facility facility) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "UPDATE facilities SET facility_name=?, timing=?, description=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1,
                    facility.getFacilityName());

            ps.setString(2,
                    facility.getTiming());

            ps.setString(3,
                    facility.getDescription());

            ps.setInt(4,
                    facility.getId());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Facility
    public boolean deleteFacility(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "DELETE FROM facilities WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Total Facilities
    public int getFacilityCount() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT COUNT(*) FROM facilities";

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