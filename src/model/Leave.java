package model;

public class Leave {

    private int    id;
    private String studentCode;
    private String studentName;
    private String reason;
    private String destination;
    private String details;
    private String leaveDate;
    private String leaveTime;
    private String returnDate;
    private String days;
    private String status;   // Pending | Approved | Rejected

    // ── Getters ──────────────────────────────────────────────────
    public int    getId()           { return id; }
    public String getStudentCode()  { return studentCode; }
    public String getStudentName()  { return studentName; }
    public String getReason()       { return reason; }
    public String getDestination()  { return destination; }
    public String getDetails()      { return details; }
    public String getLeaveDate()    { return leaveDate; }
    public String getLeaveTime()    { return leaveTime; }
    public String getReturnDate()   { return returnDate; }
    public String getDays()         { return days; }
    public String getStatus()       { return status; }

    // ── Setters ──────────────────────────────────────────────────
    public void setId(int id)                      { this.id = id; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setReason(String reason)           { this.reason = reason; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDetails(String details)         { this.details = details; }
    public void setLeaveDate(String leaveDate)     { this.leaveDate = leaveDate; }
    public void setLeaveTime(String leaveTime)     { this.leaveTime = leaveTime; }
    public void setReturnDate(String returnDate)   { this.returnDate = returnDate; }
    public void setDays(String days)               { this.days = days; }
    public void setStatus(String status)           { this.status = status; }
}