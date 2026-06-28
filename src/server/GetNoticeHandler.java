package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.NoticeDAO;
import model.Notice;

import java.io.IOException;
import java.io.OutputStream;

public class GetNoticeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        int id =
                Integer.parseInt(
                        query.split("=")[1]);

        NoticeDAO dao =
                new NoticeDAO();

        Notice notice =
                dao.getNoticeById(id);

        String response = "";

        if(notice != null) {

            response =
                    notice.getId() + "," +
                    notice.getTitle() + "," +
                    notice.getMessage() + "," +
                    notice.getDatePosted();
        }

        exchange.sendResponseHeaders(
                200,
                response.length());

        OutputStream os =
                exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }
}