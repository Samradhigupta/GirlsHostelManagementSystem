package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ComplaintDAO;
import model.Complaint;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ViewStudentComplaintsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query = exchange.getRequestURI().getQuery();
        String studentCode = query.split("=")[1];

        ComplaintDAO dao = new ComplaintDAO();
        List<Complaint> complaints =
                dao.getComplaintsByStudentCode(studentCode);

        StringBuilder sb = new StringBuilder();

        for (Complaint c : complaints) {

            sb.append(c.getId()).append(",")
              .append(c.getTitle()).append(",")
              .append(c.getCategory()).append(",")
              .append(c.getDescription()).append(",")
              .append(c.getComplaintDate()).append(",")
              .append(c.getStatus()).append(",")
              .append(c.getRemarks()).append("\n");
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}