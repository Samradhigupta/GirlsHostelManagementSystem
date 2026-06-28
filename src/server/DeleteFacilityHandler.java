package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.FacilityDAO;

import java.io.IOException;
import java.io.OutputStream;

public class DeleteFacilityHandler
        implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        int id =
                Integer.parseInt(
                        query.split("=")[1]);

        FacilityDAO dao =
                new FacilityDAO();

        boolean result =
                dao.deleteFacility(id);

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