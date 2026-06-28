package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RentDAO;
import model.Rent;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UpdateRentHandler
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

        String studentName =
                URLDecoder.decode(
                        parts[1].split("=")[1],
                        StandardCharsets.UTF_8);

        String month =
                URLDecoder.decode(
                        parts[2].split("=")[1],
                        StandardCharsets.UTF_8);

        double amount =
                Double.parseDouble(
                        parts[3].split("=")[1]);

        String status =
                URLDecoder.decode(
                        parts[4].split("=")[1],
                        StandardCharsets.UTF_8);

        Rent rent = new Rent();

        rent.setId(id);
        rent.setStudentName(studentName);
        rent.setMonth(month);
        rent.setRoomRent(amount);
        rent.setElectricBill(0.0);
        rent.setMeterReading("0");
        rent.setTotal(0.0);
        rent.setStatus(status);

        RentDAO dao =
                new RentDAO();

        boolean result =
                dao.updateRent(rent);

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