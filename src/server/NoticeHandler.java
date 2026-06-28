package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.NoticeDAO;
import model.Notice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class NoticeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        NoticeDAO dao = new NoticeDAO();

        List<Notice> notices =
                dao.getAllNotices();

        StringBuilder response =
                new StringBuilder();

        for(Notice notice : notices) {

            response.append(
                    notice.getTitle())
                    .append("|")

                    .append(
                            notice.getMessage())
                    .append("|")

                    .append(
                            notice.getDatePosted())
                    .append("\n");
        }

        exchange.sendResponseHeaders(
                200,
                response.toString().length());

        OutputStream os =
                exchange.getResponseBody();

        os.write(
                response.toString().getBytes());

        os.close();
    }
}