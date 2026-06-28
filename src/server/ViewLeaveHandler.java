package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.LeaveDAO;
import model.Leave;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * GET /viewLeaves
 * Returns all leave requests as CSV (admin use).
 * Format per line: id,studentCode,studentName,reason,destination,
 *                  leaveDate,leaveTime,returnDate,days,status,details
 */
public class ViewLeaveHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        List<Leave> leaves = new LeaveDAO().getAllLeaves();

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