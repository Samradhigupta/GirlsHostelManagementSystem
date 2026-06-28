package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.LeaveDAO;
import model.Leave;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * POST /addLeave
 * Params: studentCode, studentName, reason, destination, details,
 *         leaveDate, leaveTime, returnDate, days
 */
public class AddLeaveHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

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

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> p = parseForm(body);

        Leave l = new Leave();
        l.setStudentCode(p.getOrDefault("studentCode", ""));
        l.setStudentName(p.getOrDefault("studentName", ""));
        l.setReason(p.getOrDefault("reason", ""));
        l.setDestination(p.getOrDefault("destination", ""));
        l.setDetails(p.getOrDefault("details", ""));
        l.setLeaveDate(p.getOrDefault("leaveDate", ""));
        l.setLeaveTime(p.getOrDefault("leaveTime", ""));
        l.setReturnDate(p.getOrDefault("returnDate", ""));
        l.setDays(p.getOrDefault("days", ""));

        boolean ok = new LeaveDAO().addLeave(l);
        String response = ok ? "Leave request submitted successfully." : "Failed to submit leave request.";
        send(exchange, 200, response);
    }

    private void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }

    private Map<String, String> parseForm(String body) {
        Map<String, String> map = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                try {
                    map.put(
                        URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8)
                    );
                } catch (Exception ignored) {}
            }
        }
        return map;
    }
}