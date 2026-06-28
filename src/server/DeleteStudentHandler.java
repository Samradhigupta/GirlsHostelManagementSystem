package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RoomDAO;
import dao.StudentDAO;
import model.Student;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DeleteStudentHandler
        implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        String name =
                URLDecoder.decode(
                        query.split("=")[1],
                        StandardCharsets.UTF_8);
                System.out.println( "Deleting Student : " + name);

        StudentDAO dao =
        new StudentDAO();


// Get student before deleting
Student student =
        dao.getStudentByName(name);

if(student != null) {
    System.out.println(
            "Room No = " +
            student.getRoomNo());
}

// Delete student
boolean result =
        dao.deleteStudent(name);
        System.out.println(
        "Delete Result = " +
        result);

// Decrease occupied count
if (result && student != null) {

    RoomDAO roomDAO =
            new RoomDAO();

    roomDAO.decreaseOccupied(
            student.getRoomNo());
}

        String response =
                result ? "SUCCESS" : "FAILED";

        exchange.sendResponseHeaders(
                200,
                response.length());

        OutputStream os =
                exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }
}