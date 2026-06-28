package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.StudentDAO;
import model.Student;

import java.io.IOException;
import java.io.OutputStream;

public class StudentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        String username =
                query.split("=")[1];

        StudentDAO dao =
                new StudentDAO();

        Student student =
                dao.getStudentByName(username);

        String response = "";

        if(student != null) {

            response =
                    student.getStudentCode() + "," +
                    student.getName() + "," +
                    student.getAge() + "," +
                    student.getRoomNo() + "," +
                    student.getContact();
        }

        exchange.sendResponseHeaders(
                200,
                response.length());

        OutputStream os =
                exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }
}