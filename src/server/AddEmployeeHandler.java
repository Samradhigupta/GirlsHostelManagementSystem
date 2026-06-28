package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.EmployeeDAO;
import model.Employee;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AddEmployeeHandler implements HttpHandler {

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

        String name =
                URLDecoder.decode(
                        parts[0].split("=")[1],
                        StandardCharsets.UTF_8);

        String role =
                URLDecoder.decode(
                        parts[1].split("=")[1],
                        StandardCharsets.UTF_8);

        String contact =
                URLDecoder.decode(
                        parts[2].split("=")[1],
                        StandardCharsets.UTF_8);

        Employee employee =
                new Employee();

        employee.setName(name);
        employee.setRole(role);
        employee.setContact(contact);

        EmployeeDAO dao =
                new EmployeeDAO();

        boolean result =
                dao.addEmployee(employee);

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