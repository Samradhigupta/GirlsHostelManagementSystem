package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RoomDAO;
import dao.StudentDAO;
import model.Student;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UpdateStudentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        // Read full body (photo can be large)
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
        String data = bos.toString(StandardCharsets.UTF_8.name());

        Map<String, String> params = new HashMap<>();
        for (String part : data.split("&")) {
            int eq = part.indexOf('=');
            if (eq < 0) continue;
            String key   = URLDecoder.decode(part.substring(0, eq),  StandardCharsets.UTF_8.name());
            String value = URLDecoder.decode(part.substring(eq + 1), StandardCharsets.UTF_8.name());
            params.put(key, value);
        }

        String name        = params.getOrDefault("name",        "");
        int    age         = parseIntSafe(params.get("age"));
        int    roomNo      = parseIntSafe(params.get("roomNo"));
        String contact     = params.getOrDefault("contact",     "");
        String password    = params.getOrDefault("password",    "");
        String occupation  = params.getOrDefault("occupation",  "");
        String email       = params.getOrDefault("email",       "");
        String dob         = params.getOrDefault("dob",         "");
        String address     = params.getOrDefault("address",     "");
        int    floor       = parseIntSafe(params.get("floor"));
        String joiningDate = params.getOrDefault("joiningDate", "");
        String photo       = params.getOrDefault("photo",       "");  // NEW — empty means keep existing

        StudentDAO dao = new StudentDAO();

        // Remember old room to fix occupancy if room changed
        Student oldStudent = dao.getStudentByName(name);
        int oldRoom = (oldStudent != null) ? oldStudent.getRoomNo() : roomNo;

        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setRoomNo(roomNo);
        student.setContact(contact);
        student.setPassword(password);
        student.setOccupation(occupation);
        student.setEmail(email);
        student.setDob(dob);
        student.setAddress(address);
        student.setFloor(floor);
        student.setJoiningDate(joiningDate);
        student.setPhoto(photo);  // DAO will skip update if empty

        boolean result = dao.updateStudent(student);

        if (result && oldRoom != roomNo) {
            RoomDAO roomDAO = new RoomDAO();
            roomDAO.decreaseOccupied(oldRoom);
            roomDAO.increaseOccupied(roomNo);
        }

        String response = result ? "SUCCESS" : "FAILED";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private int parseIntSafe(String value) {
        try {
            return (value != null && !value.isEmpty()) ? Integer.parseInt(value.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}