package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RoomDAO;
import model.Room;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UpdateRoomHandler
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

        int roomNo =
                Integer.parseInt(
                        parts[0].split("=")[1]);

        int capacity =
                Integer.parseInt(
                        parts[1].split("=")[1]);

        int occupied =
                Integer.parseInt(
                        parts[2].split("=")[1]);

        Room room =
                new Room();

        room.setRoomNo(roomNo);
        room.setCapacity(capacity);
        room.setOccupied(occupied);

        RoomDAO dao =
                new RoomDAO();

        boolean result =
                dao.updateRoom(room);

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