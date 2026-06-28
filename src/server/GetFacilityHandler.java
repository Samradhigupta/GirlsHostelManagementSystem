package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.FacilityDAO;
import model.Facility;

import java.io.IOException;
import java.io.OutputStream;

public class GetFacilityHandler implements HttpHandler {

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

        Facility facility =
                dao.getFacilityById(id);

        String response = "";

        if(facility != null) {

            response =
                    facility.getId() + "," +
                    facility.getFacilityName() + "," +
                    facility.getTiming() + "," +
                    facility.getDescription();
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