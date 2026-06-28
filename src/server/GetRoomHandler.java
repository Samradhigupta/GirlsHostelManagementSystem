package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RoomDAO;
import model.Room;

import java.io.IOException;
import java.io.OutputStream;

public class GetRoomHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        int roomNo =
                Integer.parseInt(
                        query.split("=")[1]);

        RoomDAO dao =
                new RoomDAO();

        Room room =
                dao.getRoomByNumber(roomNo);

        String response = "";

        if(room != null) {

            response =
                    room.getRoomNo() + "," +
                    room.getCapacity() + "," +
                    room.getOccupied();
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