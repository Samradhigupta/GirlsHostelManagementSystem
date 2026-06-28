package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.EmployeeDAO;
import model.Employee;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ViewEmployeesHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        EmployeeDAO dao =
                new EmployeeDAO();

        List<Employee> employees =
                dao.getAllEmployees();

        StringBuilder response =
                new StringBuilder();

        for(Employee e : employees) {

            response.append(
                    e.getId()).append(",")

                    .append(
                            e.getName()).append(",")

                    .append(
                            e.getRole()).append(",")

                    .append(
                            e.getContact())

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