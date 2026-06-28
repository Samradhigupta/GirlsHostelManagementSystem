package dao;

import database.DBConnection;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Add Employee
    public boolean addEmployee(Employee employee) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO employees(name, role, contact) VALUES(?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getRole());
            ps.setString(3, employee.getContact());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // View All Employees
    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM employees";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Employee employee = new Employee();

                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setRole(rs.getString("role"));
                employee.setContact(rs.getString("contact"));

                employees.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return employees;
    }

    // Get Employee By ID
    public Employee getEmployeeById(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM employees WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Employee employee = new Employee();

                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setRole(rs.getString("role"));
                employee.setContact(rs.getString("contact"));

                return employee;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update Employee
    public boolean updateEmployee(Employee employee) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "UPDATE employees SET name=?, role=?, contact=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getRole());
            ps.setString(3, employee.getContact());
            ps.setInt(4, employee.getId());
            System.out.println(
                       "Updating Employee: "
                       + employee.getId() + " "
                       + employee.getName() + " "
                       + employee.getRole() + " "
                       + employee.getContact()
                );
                int rows = ps.executeUpdate();

               System.out.println("Rows Updated = " + rows);

                    return rows > 0;

          

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete Employee
    public boolean deleteEmployee(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "DELETE FROM employees WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Total Employees
    public int getEmployeeCount() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT COUNT(*) FROM employees";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}