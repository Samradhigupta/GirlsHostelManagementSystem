package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.StudentDAO;
import model.Student;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * GET /getStudent?name=Tanvi
 *
 * Returns a JSON object so the large photo field does not break
 * the comma-delimited format.
 *
 * JSON shape:
 * {
 *   "id":        1,
 *   "name":      "Tanvi",
 *   "age":       25,
 *   "roomNo":    104,
 *   "contact":   "62315449",
 *   "password":  "tanvi",
 *   "studentCode":"GHMS1007",
 *   "occupation":"Student",
 *   "email":     "tanvi@example.com",
 *   "dob":       "2000-01-15",
 *   "address":   "42 MG Road, Indore",
 *   "floor":     1,
 *   "joiningDate":"2024-06-01",
 *   "photo":     "data:image/jpeg;base64,/9j/..."
 * }
 */
public class GetStudentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        String query = exchange.getRequestURI().getQuery();
        String name  = query != null && query.contains("=")
                ? java.net.URLDecoder.decode(query.split("=", 2)[1], "UTF-8")
                : "";

        StudentDAO dao     = new StudentDAO();
        Student    student = dao.getStudentByName(name);

        String response;

        if (student == null) {
            response = "{}";
        } else {
            response = "{"
                + jsonField("id",          String.valueOf(student.getId()),     false)
                + jsonField("name",         student.getName(),                   true)
                + jsonField("age",          String.valueOf(student.getAge()),    false)
                + jsonField("roomNo",       String.valueOf(student.getRoomNo()), false)
                + jsonField("contact",      student.getContact(),                true)
                + jsonField("password",     student.getPassword(),               true)
                + jsonField("studentCode",  student.getStudentCode(),            true)
                + jsonField("occupation",   nullSafe(student.getOccupation()),   true)
                + jsonField("email",        nullSafe(student.getEmail()),        true)
                + jsonField("dob",          nullSafe(student.getDob()),          true)
                + jsonField("address",      nullSafe(student.getAddress()),      true)
                + jsonField("floor",        String.valueOf(student.getFloor()),  false)
                + jsonField("joiningDate",  nullSafe(student.getJoiningDate()),  true)
                + "\"photo\":" + jsonString(nullSafe(student.getPhoto()))
                + "}";
        }

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    // Emits:  "key": <value>,
    private String jsonField(String key, String value, boolean quoted) {
        if (quoted) {
            return "\"" + key + "\":" + jsonString(value) + ",";
        } else {
            return "\"" + key + "\":" + value + ",";
        }
    }

    // JSON-safe string with escaping
    private String jsonString(String value) {
        if (value == null || value.isEmpty()) return "\"\"";
        // Escape backslashes and double-quotes inside the value
        String escaped = value.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + escaped + "\"";
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }
}