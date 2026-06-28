package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.RentDAO;
import dao.StudentDAO;
import model.Rent;
import model.Student;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class AddRentHandler implements HttpHandler {

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

        String studentName =
                URLDecoder.decode(
                        parts[0].split("=")[1],
                        StandardCharsets.UTF_8);

        String month =
                URLDecoder.decode(
                        parts[1].split("=")[1],
                        StandardCharsets.UTF_8);
                        int roomNo =
        Integer.parseInt(parts[2].split("=")[1]);

double roomRent =
        Double.parseDouble(parts[3].split("=")[1]);

double electricBill =
        Double.parseDouble(parts[4].split("=")[1]);

String meterReading =
        URLDecoder.decode(
                parts[5].split("=")[1],
                StandardCharsets.UTF_8);

double total =
        Double.parseDouble(parts[6].split("=")[1]);
        System.out.println("Room Rent = " + roomRent);
System.out.println("Electric Bill = " + electricBill);
System.out.println("Total = " + total);


        String status =
                URLDecoder.decode(
                        parts[7].split("=")[1],
                        StandardCharsets.UTF_8);

        Rent rent = new Rent();

        rent.setStudentName(studentName);
        StudentDAO studentDAO =
        new StudentDAO();

Student student =
        studentDAO.getStudentByName(studentName);

rent.setStudentCode(
        student.getStudentCode());
        rent.setMonth(month);
        rent.setRoomNo(roomNo);
        rent.setRoomRent(roomRent);
        rent.setElectricBill(electricBill);
        rent.setMeterReading(meterReading);
        rent.setTotal(total);
        rent.setStatus(status);

        RentDAO dao = new RentDAO();

        boolean result = dao.addRent(rent);

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