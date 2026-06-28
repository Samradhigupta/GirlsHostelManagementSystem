package model;

public class Complaint {

    private int id;
    private String studentCode;
    private String studentName;
    private String title;
    private String category;
    private String description;
    private String complaintDate;
    private String status;   // Pending / In Progress / Resolved / Rejected
    private String remarks;  // Admin reply / remarks

    public Complaint() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getComplaintDate() { return complaintDate; }
    public void setComplaintDate(String complaintDate) { this.complaintDate = complaintDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}