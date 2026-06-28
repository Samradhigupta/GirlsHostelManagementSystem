package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RoomDAO;
import model.Room;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ViewRoomsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        RoomDAO dao =
                new RoomDAO();

        List<Room> rooms =
                dao.getAllRooms();

        StringBuilder response =
                new StringBuilder();

        for(Room r : rooms) {

            response.append(r.getRoomNo())
                    .append(",")
                    .append(r.getCapacity())
                    .append(",")
                    .append(r.getOccupied())
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