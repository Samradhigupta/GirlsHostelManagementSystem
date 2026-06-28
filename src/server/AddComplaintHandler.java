package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ComplaintDAO;
import model.Complaint;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AddComplaintHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // Read POST body
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> params = parseForm(body);

        Complaint c = new Complaint();
        c.setStudentCode(params.getOrDefault("studentCode", ""));
        c.setStudentName(params.getOrDefault("studentName", ""));
        c.setTitle(params.getOrDefault("title", ""));
        c.setCategory(params.getOrDefault("category", "General"));
        c.setDescription(params.getOrDefault("description", ""));
        c.setComplaintDate(LocalDate.now().toString());

        ComplaintDAO dao = new ComplaintDAO();
        boolean success = dao.addComplaint(c);

        String response = success ? "Complaint registered successfully" : "Failed to register complaint";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private Map<String, String> parseForm(String body) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(
                    URLDecoder.decode(kv[0], "UTF-8"),
                    URLDecoder.decode(kv[1], "UTF-8")
                );
            }
        }
        return map;
    }
}