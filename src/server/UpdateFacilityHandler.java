package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.FacilityDAO;
import model.Facility;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UpdateFacilityHandler
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

        String facilityName =
                URLDecoder.decode(
                        parts[1].split("=")[1],
                        StandardCharsets.UTF_8);

        String timing =
                URLDecoder.decode(
                        parts[2].split("=")[1],
                        StandardCharsets.UTF_8);

        String description =
                URLDecoder.decode(
                        parts[3].split("=")[1],
                        StandardCharsets.UTF_8);

        Facility facility =
                new Facility();

        facility.setId(id);
        facility.setFacilityName(facilityName);
        facility.setTiming(timing);
        facility.setDescription(description);

        FacilityDAO dao =
                new FacilityDAO();

        boolean result =
                dao.updateFacility(facility);

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