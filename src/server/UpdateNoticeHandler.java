package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.NoticeDAO;
import model.Notice;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UpdateNoticeHandler
        implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(
                                exchange.getRequestBody()));

        String data = br.readLine();

        String[] parts =
                data.split("&");

        int id =
                Integer.parseInt(
                        parts[0].split("=")[1]);

        String title =
                URLDecoder.decode(
                        parts[1].split("=")[1],
                        StandardCharsets.UTF_8);

        String message =
                URLDecoder.decode(
                        parts[2].split("=")[1],
                        StandardCharsets.UTF_8);

        String datePosted =
                URLDecoder.decode(
                        parts[3].split("=")[1],
                        StandardCharsets.UTF_8);

        Notice notice =
                new Notice();

        notice.setId(id);
        notice.setTitle(title);
        notice.setMessage(message);
        notice.setDatePosted(datePosted);

        NoticeDAO dao =
                new NoticeDAO();

        boolean result =
                dao.updateNotice(notice);

        String response =
                result ? "SUCCESS" : "FAILED";

        exchange.sendResponseHeaders(
                200,
                response.length());

        OutputStream os =
                exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }
}