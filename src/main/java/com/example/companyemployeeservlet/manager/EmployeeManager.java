package com.example.companyemployeeservlet.manager;


import com.example.companyemployeeservlet.db.DBConnectionProvider;
import com.example.companyemployeeservlet.model.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {

    private Connection connection = DBConnectionProvider.getInstance().getConnection();
    private CompanyManager companyManager = new CompanyManager();

    public void save(Employee employee) {
        try (Statement statement = connection.createStatement()) {
            String sql = "INSERT INTO employee(name,surname,email,company_id) VALUES('%s','%s','%s',%d)";
            String sqlFormatted = String.format(sql, employee.getName(), employee.getSurname(), employee.getEmail(),
                    employee.getCompany().getId());
            statement.executeUpdate(sqlFormatted, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                employee.setId(generatedKeys.getInt(1));
            }
            System.out.println("employee inserted into DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee getById(int id) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("Select * from employee where id = " + id);
            if (resultSet.next()) {
                return getEmployeeFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from employee");
            while (resultSet.next()) {
                employees.add(getEmployeeFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> getAllByCompanyId(int companyId) {
        List<Employee> employees = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from employee where company_id=" + companyId);
            while (resultSet.next()) {
                employees.add(getEmployeeFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("id"));
        employee.setName(resultSet.getString("name"));
        employee.setSurname(resultSet.getString("surname"));
        employee.setEmail(resultSet.getString("email"));
        int companyId = resultSet.getInt("company_id");
        employee.setCompany(companyManager.getById(companyId));
        return employee;
    }


    public void removeById(int employeeId) {
        String sql = "DELETE FROM employee WHERE id = " + employeeId;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
