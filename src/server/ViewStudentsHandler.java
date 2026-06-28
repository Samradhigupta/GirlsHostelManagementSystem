package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.StudentDAO;
import model.Student;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ViewStudentsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        StudentDAO dao = new StudentDAO();
        List<Student> students = dao.getAllStudents();

        StringBuilder response = new StringBuilder();

        for (Student s : students) {
            response
                .append(s.getStudentCode())               .append(",")
                .append(s.getId())                        .append(",")
                .append(s.getName())                      .append(",")
                .append(s.getAge())                       .append(",")
                .append(s.getRoomNo())                    .append(",")
                .append(s.getContact())                   .append(",")
                .append(nullSafe(s.getOccupation()))      .append(",")
                .append(nullSafe(s.getEmail()))           .append(",")
                .append(nullSafe(s.getDob()))             .append(",")
                .append(nullSafe(s.getAddress()))         .append(",")
                .append(s.getFloor())                     .append(",")
                .append(nullSafe(s.getJoiningDate()))
                .append("\n");
        }

        byte[] bytes = response.toString().getBytes();

        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }
}