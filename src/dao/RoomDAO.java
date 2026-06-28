package dao;

import database.DBConnection;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // Add Room
    public boolean addRoom(Room room) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO rooms(room_no, capacity, occupied) VALUES(?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, room.getRoomNo());
            ps.setInt(2, room.getCapacity());
            ps.setInt(3, room.getOccupied());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get All Rooms
    public List<Room> getAllRooms() {

        List<Room> rooms = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM rooms";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Room room = new Room();

                room.setRoomNo(rs.getInt("room_no"));
                room.setCapacity(rs.getInt("capacity"));
                room.setOccupied(rs.getInt("occupied"));

                rooms.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }

    // Get Room By Number
    public Room getRoomByNumber(int roomNo) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM rooms WHERE room_no=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, roomNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Room room = new Room();

                room.setRoomNo(rs.getInt("room_no"));
                room.setCapacity(rs.getInt("capacity"));
                room.setOccupied(rs.getInt("occupied"));

                return room;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update Room
    public boolean updateRoom(Room room) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "UPDATE rooms SET capacity=?, occupied=? WHERE room_no=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, room.getCapacity());
            ps.setInt(2, room.getOccupied());
            ps.setInt(3, room.getRoomNo());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Room
    public boolean deleteRoom(int roomNo) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "DELETE FROM rooms WHERE room_no=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, roomNo);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Available Beds
    public int getAvailableBeds(int roomNo) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT capacity, occupied FROM rooms WHERE room_no=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, roomNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int capacity = rs.getInt("capacity");
                int occupied = rs.getInt("occupied");

                return capacity - occupied;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    // Increase Occupied
    public void increaseOccupied(int roomNo) {

    try {

        System.out.println("Updating Room : " + roomNo);

        Connection con =
                DBConnection.getConnection();

        String sql =
                "UPDATE rooms SET occupied = occupied + 1 WHERE room_no=?";

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1, roomNo);

        int rows =
                ps.executeUpdate();

        System.out.println("Rows Updated = " + rows);

    } catch(Exception e) {
        e.printStackTrace();
    }
}
// Decrease Occupied
public void decreaseOccupied(int roomNo) {

    try {

        System.out.println(
        "Updating Room : " +
        roomNo);

        Connection con =
                DBConnection.getConnection();

        String sql =
                "UPDATE rooms SET occupied = occupied - 1 WHERE room_no=?";

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1, roomNo);

       int rows =
        ps.executeUpdate();

System.out.println(
        "Rows Updated = " +
        rows);

    } catch(Exception e) {
        e.printStackTrace();
    }
}


}