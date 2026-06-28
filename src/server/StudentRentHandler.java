package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.RentDAO;
import model.Rent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class StudentRentHandler
        implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        String studentName =
                URLDecoder.decode(
                        query.split("=")[1],
                        StandardCharsets.UTF_8);

        RentDAO dao =
                new RentDAO();

        Rent rent =
                dao.getRentByStudentName(studentName);

        String response = "";

        if (rent != null) {

            response =
                    rent.getMonth() + "," +
                    rent.getRoomNo() + "," +
                    rent.getRoomRent() + "," +
                    rent.getElectricBill() + "," +
                    rent.getMeterReading() + "," +
                    rent.getTotal() + "," +
                    rent.getStatus();
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