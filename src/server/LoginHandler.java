package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.AdminDAO;
import dao.StudentDAO;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

                
        // CORS headers (needed when the frontend runs on file:// or a different origin)
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

        String role =
                URLDecoder.decode(
                        parts[0].split("=")[1],
                        StandardCharsets.UTF_8);

        String username =
                URLDecoder.decode(
                        parts[1].split("=")[1],
                        StandardCharsets.UTF_8);

        String password =
                URLDecoder.decode(
                        parts[2].split("=")[1],
                        StandardCharsets.UTF_8);

        String response = "FAILED";

        if(role.equals("Admin")) {

            AdminDAO dao =
                    new AdminDAO();

            if(dao.loginAdmin(
                    username,
                    password)) {

                response = "ADMIN";
            }

        } else {

            StudentDAO dao =
                    new StudentDAO();

            if(dao.loginStudent(
                    username,
                    password)) {

                response = "STUDENT";
            }
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