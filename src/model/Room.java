package model;

public class Room {

    private int roomNo;
    private int capacity;
    private int occupied;

    public Room() {
    }

    public Room(int roomNo, int capacity, int occupied) {
        this.roomNo = roomNo;
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }
}