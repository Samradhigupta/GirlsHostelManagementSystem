package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RentDAO;
import model.Rent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RentHandler implements HttpHandler {

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

        List<Rent> rents =
                dao.getRentByStudent(studentName);

        StringBuilder response =
                new StringBuilder();

        for(Rent rent : rents) {

            response.append(
                    rent.getMonth())
                    .append(",")

                    .append(
                            rent.getRoomRent())
                    .append(",")
                        .append(
                                rent.getElectricBill())
                    .append(",")
                        .append(
                                rent.getMeterReading())
                    .append(",")
                        .append(
                                rent.getTotal())
                    .append(",")
                        .append(
                                rent.getStatus())
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