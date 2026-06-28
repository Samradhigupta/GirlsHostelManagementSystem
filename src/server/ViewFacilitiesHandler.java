package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.FacilityDAO;
import model.Facility;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ViewFacilitiesHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        FacilityDAO dao =
                new FacilityDAO();

        List<Facility> facilities =
                dao.getAllFacilities();

        StringBuilder response =
                new StringBuilder();

        for(Facility f : facilities) {

            response.append(f.getId()).append(",")
                    .append(f.getFacilityName()).append(",")
                    .append(f.getTiming()).append(",")
                    .append(f.getDescription())
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