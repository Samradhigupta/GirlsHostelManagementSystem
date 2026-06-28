package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RentDAO;
import model.Rent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ViewRentsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        RentDAO dao = new RentDAO();

        List<Rent> rents =
                dao.getAllRent();

        StringBuilder response =
                new StringBuilder();

        for(Rent r : rents) {

            response.append(r.getId()).append(",")
                    .append(r.getStudentCode()).append(",")
                    .append(r.getStudentName()).append(",")
                    .append(r.getMonth()).append(",")
                    .append(r.getRoomNo()).append(",")

                    .append(r.getRoomRent()).append(",")
                        .append(r.getElectricBill()).append(",")
                        .append(r.getMeterReading()).append(",")
                        .append(r.getTotal()).append(",")

                    .append(r.getStatus())
                    .append("\n");
        }

        exchange.sendResponseHeaders(
                200,
                response.toString().length());

        OutputStream os =
                exchange.getResponseBody();

        os.write(response.toString().getBytes());

        os.close();
    }
}