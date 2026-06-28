package model;

public class Student {

    private int    id;
    private String name;
    private int    age;
    private int    roomNo;
    private String contact;
    private String password;
    private String studentCode;
    private String occupation;
    private String email;
    private String dob;
    private String address;
    private int    floor;
    private String joiningDate;
    private String photo;          // base64 data-URL, stored as MEDIUMTEXT in DB

    public Student() {}

    public Student(int id, String name, int age, int roomNo, String contact) {
        this.id      = id;
        this.name    = name;
        this.age     = age;
        this.roomNo  = roomNo;
        this.contact = contact;
    }

    public int    getId()                            { return id; }
    public void   setId(int id)                      { this.id = id; }

    public String getStudentCode()                   { return studentCode; }
    public void   setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getName()                          { return name; }
    public void   setName(String name)               { this.name = name; }

    public int    getAge()                           { return age; }
    public void   setAge(int age)                    { this.age = age; }

    public int    getRoomNo()                        { return roomNo; }
    public void   setRoomNo(int roomNo)              { this.roomNo = roomNo; }

    public String getContact()                       { return contact; }
    public void   setContact(String contact)         { this.contact = contact; }

    public String getPassword()                      { return password; }
    public void   setPassword(String password)       { this.password = password; }

    public String getOccupation()                    { return occupation; }
    public void   setOccupation(String occupation)   { this.occupation = occupation; }

    public String getEmail()                         { return email; }
    public void   setEmail(String email)             { this.email = email; }

    public String getDob()                           { return dob; }
    public void   setDob(String dob)                 { this.dob = dob; }

    public String getAddress()                       { return address; }
    public void   setAddress(String address)         { this.address = address; }

    public int    getFloor()                         { return floor; }
    public void   setFloor(int floor)                { this.floor = floor; }

    public String getJoiningDate()                   { return joiningDate; }
    public void   setJoiningDate(String joiningDate) { this.joiningDate = joiningDate; }

    public String getPhoto()                         { return photo; }
    public void   setPhoto(String photo)             { this.photo = photo; }
}