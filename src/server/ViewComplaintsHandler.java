package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ComplaintDAO;
import model.Complaint;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ViewComplaintsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        ComplaintDAO dao = new ComplaintDAO();
        List<Complaint> complaints = dao.getAllComplaints();

        StringBuilder sb = new StringBuilder();
        for (Complaint c : complaints) {
            sb.append(c.getId()).append(",")
              .append(safe(c.getStudentCode())).append(",")
              .append(safe(c.getStudentName())).append(",")
              .append(safe(c.getTitle())).append(",")
              .append(safe(c.getCategory())).append(",")
              .append(safe(c.getDescription())).append(",")
              .append(safe(c.getComplaintDate())).append(",")
              .append(safe(c.getStatus())).append(",")
              .append(safe(c.getRemarks())).append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private String safe(String val) {
        return val == null ? "" : val.replace(",", ";").replace("\n", " ");
    }
}