package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.EmployeeDAO;
import model.Employee;

import java.io.IOException;
import java.io.OutputStream;

public class GetEmployeeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String query =
                exchange.getRequestURI().getQuery();

        int id =
                Integer.parseInt(
                        query.split("=")[1]);

        EmployeeDAO dao =
                new EmployeeDAO();

        Employee employee =
                dao.getEmployeeById(id);

        String response = "";

        if(employee != null) {

            response =
                    employee.getId() + "," +
                    employee.getName() + "," +
                    employee.getRole() + "," +
                    employee.getContact();
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