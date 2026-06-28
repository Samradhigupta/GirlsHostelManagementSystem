package model;

public class Rent {

    private int id;
    private String studentName;
    private String month;
    private String status;
    private int roomNo;
    private double roomRent;
    private double electricBill;
    private String meterReading;
    private double total;
    private String studentCode;

    public Rent() {
    }

    public Rent(
                int id,
                String studentName,
                String month,
                String meterReading,
                int roomNo,
                double roomRent,
                double electricBill,
                double total,
                String status) {

        this.id = id;
        this.studentName = studentName;
        this.month = month;
        this.meterReading = meterReading;
        this.roomNo = roomNo;
        this.roomRent = roomRent;
        this.electricBill = electricBill;
        this.total = total;
        this.status = status;
    }
     public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

public String getStudentCode() {return studentCode;}

public void setStudentCode(String studentCode) {
    this.studentCode = studentCode;
}



    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public int getRoomNo() {
    return roomNo;
}

public void setRoomNo(int roomNo) {
    this.roomNo = roomNo;}


  

public double getRoomRent() {
    return roomRent;
}

public void setRoomRent(double roomRent) {
    this.roomRent = roomRent;
    
}
public double getElectricBill() {
    return electricBill;
}

public void setElectricBill(double electricBill) {
    this.electricBill = electricBill;
}


    public String getMeterReading() {
    return meterReading;
}

public void setMeterReading(String meterReading) {
    this.meterReading = meterReading;
}
public double getTotal() {
    return total;
}

public void setTotal(double total) {
    this.total = total;
}


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}