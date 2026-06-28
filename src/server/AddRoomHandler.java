package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RoomDAO;
import model.Room;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AddRoomHandler implements HttpHandler {

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

        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            exchange.sendResponseHeaders(405, -1);
            return;
        }

        BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(
                                exchange.getRequestBody()));

        String data = br.readLine();

        String[] parts = data.split("&");

        int roomNo =
                Integer.parseInt(
                        URLDecoder.decode(
                                parts[0].split("=")[1],
                                StandardCharsets.UTF_8));

        int capacity =
                Integer.parseInt(
                        URLDecoder.decode(
                                parts[1].split("=")[1],
                                StandardCharsets.UTF_8));

        int occupied =
                Integer.parseInt(
                        URLDecoder.decode(
                                parts[2].split("=")[1],
                                StandardCharsets.UTF_8));

        Room room = new Room();

        room.setRoomNo(roomNo);
        room.setCapacity(capacity);
        room.setOccupied(occupied);

        RoomDAO dao = new RoomDAO();

        boolean result = dao.addRoom(room);

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