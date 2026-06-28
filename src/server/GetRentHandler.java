package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RentDAO;
import model.Rent;

import java.io.IOException;
import java.io.OutputStream;

public class GetRentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        int id =
                Integer.parseInt(
                        query.split("=")[1]);

        RentDAO dao =
                new RentDAO();

        Rent rent =
                dao.getRentById(id);

        String response = "";

        if(rent != null) {

            response =
                    rent.getId() + "," +
                    rent.getStudentName() + "," +
                    rent.getMonth() + "," +
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