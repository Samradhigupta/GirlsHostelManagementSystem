package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.LeaveDAO;
import model.Leave;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * GET /studentLeaves?code=STUDENT_CODE
 * Returns leave requests for a specific student as CSV.
 */
public class StudentLeaveHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Parse ?code= query param
        String code = "";
        URI uri   = exchange.getRequestURI();
        String q  = uri.getQuery();
        if (q != null) {
            for (String pair : q.split("&")) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2 && "code".equals(kv[0])) {
                    code = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                }
            }
        }

        List<Leave> leaves = new LeaveDAO().getLeavesByStudentCode(code);

        StringBuilder sb = new StringBuilder();
        for (Leave l : leaves) {
            sb.append(LeaveDAO.toCsv(l)).append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}