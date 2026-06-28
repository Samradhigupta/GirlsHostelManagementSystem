package server;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HostelServer {

    public static void main(String[] args) {

        try {

            HttpServer server =
                    HttpServer.create(
                            new InetSocketAddress(8080),
                            0);

            // ── Login ────────────────────────────────────────────────
            server.createContext("/login",         new LoginHandler());

            // ── Students ─────────────────────────────────────────────
            server.createContext("/student",        new StudentHandler());
            server.createContext("/addStudent",     new AddStudentHandler());
            server.createContext("/viewStudents",   new ViewStudentsHandler());
            server.createContext("/getStudent",     new GetStudentHandler());
            server.createContext("/updateStudent",  new UpdateStudentHandler());
            server.createContext("/deleteStudent",  new DeleteStudentHandler());

            // ── Employees ────────────────────────────────────────────
            server.createContext("/addEmployee",    new AddEmployeeHandler());
            server.createContext("/viewEmployees",  new ViewEmployeesHandler());
            server.createContext("/getEmployee",    new GetEmployeeHandler());
            server.createContext("/updateEmployee", new UpdateEmployeeHandler());
            server.createContext("/deleteEmployee", new DeleteEmployeeHandler());

            // ── Rooms ────────────────────────────────────────────────
            server.createContext("/addRoom",        new AddRoomHandler());
            server.createContext("/viewRooms",      new ViewRoomsHandler());
            server.createContext("/getRoom",        new GetRoomHandler());
            server.createContext("/updateRoom",     new UpdateRoomHandler());
            server.createContext("/deleteRoom",     new DeleteRoomHandler());

            // ── Facilities ───────────────────────────────────────────
            server.createContext("/addFacility",    new AddFacilityHandler());
            server.createContext("/viewFacilities", new ViewFacilitiesHandler());
            server.createContext("/getFacility",    new GetFacilityHandler());
            server.createContext("/updateFacility", new UpdateFacilityHandler());
            server.createContext("/deleteFacility", new DeleteFacilityHandler());

            // ── Notices ──────────────────────────────────────────────
            server.createContext("/addNotice",      new AddNoticeHandler());
            server.createContext("/notices",        new NoticeHandler());
            server.createContext("/viewNotices",    new ViewNoticesHandler());
            server.createContext("/getNotice",      new GetNoticeHandler());
            server.createContext("/updateNotice",   new UpdateNoticeHandler());
            server.createContext("/deleteNotice",   new DeleteNoticeHandler());

            // ── Rent ─────────────────────────────────────────────────
            server.createContext("/addRent",        new AddRentHandler());
            server.createContext("/rent",           new StudentRentHandler());
            server.createContext("/viewRents",      new ViewRentsHandler());
            server.createContext("/updateRent",     new UpdateRentHandler());
            server.createContext("/deleteRent",     new DeleteRentHandler());

            // ── Complaints ───────────────────────────────────────────
            server.createContext("/addComplaint",       new AddComplaintHandler());
            server.createContext("/viewComplaints",     new ViewComplaintsHandler());
            server.createContext("/studentComplaints",  new ViewStudentComplaintsHandler());
            server.createContext("/updateComplaint",    new UpdateComplaintHandler());
            server.createContext("/deleteComplaint",    new DeleteComplaintHandler());

            // ── Leave / Out-of-Hostel (NEW) ──────────────────────────
            server.createContext("/addLeave",           new AddLeaveHandler());
            server.createContext("/viewLeaves",         new ViewLeaveHandler());
            server.createContext("/studentLeaves",      new StudentLeaveHandler());
            server.createContext("/updateLeaveStatus",  new UpdateLeaveStatusHandler());

            server.setExecutor(null);
            server.start();

            System.out.println("Hostel Server Running on Port 8080");
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}