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

public class AddStudentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // ── CORS headers (needed when the frontend runs on file://) ──
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // Read the entire body (photo can be large)
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
        String data = bos.toString(StandardCharsets.UTF_8.name());

        // Parse key=value pairs
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
        String photo       = params.getOrDefault("photo",       "");  // NEW — base64 data-URL

        StudentDAO dao = new StudentDAO();
        String studentCode = "GHMS" + (1000 + dao.getStudentCount() + 1);

        Student student = new Student();
        student.setStudentCode(studentCode);
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
        student.setPhoto(photo);                                       // NEW

        System.out.println("Generated Code = " + studentCode);
        boolean result = dao.addStudent(student);
        System.out.println("Student Added = " + result);

        if (result) {
            new RoomDAO().increaseOccupied(roomNo);
        }

        String response = result ? "SUCCESS" : "FAILED";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
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