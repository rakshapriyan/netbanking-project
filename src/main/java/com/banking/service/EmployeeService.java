package com.banking.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.entity.Employee;
import com.banking.util.Constant;

public class EmployeeService {

    private QueryBuilder queryBuilder;
    private DBService dbService;

    public EmployeeService() {
        queryBuilder = new QueryBuilder();
        dbService = new DBService(Constant.YAML_PATH);
    }

    public List<Employee> getAllEmployees() {
        return dbService.get(Employee.class, null, null);
    }

    public void addEmployee(Employee employee) throws SQLException {
        dbService.insert(employee);
    }

    public Employee getEmployeeById(long id) {
        String query = queryBuilder
                .select("*")
                .table("Employee")
                .where("employeeId", "=", String.valueOf(id))
                .build();

        System.out.println(query);

        List<Employee> employees = dbService.executeQuery(query, Employee.class);

        return employees.isEmpty() ? null : employees.get(0);
    }

    public int updateEmployee(Employee employee) throws SQLException {
        Employee existingEmployee = getEmployeeById(employee.getEmployeeId());

        if (existingEmployee == null) {
            throw new IllegalArgumentException("Employee with ID " + employee.getEmployeeId() + " does not exist.");
        }

        QueryBuilder query = queryBuilder.table("Employee");

        Map<String, String> map = new HashMap<>();

        if (!employee.getName().equals(existingEmployee.getName())) {
            map.put("name", employee.getName());
        }
        if (!employee.getPhoneNumber().equals(existingEmployee.getPhoneNumber())) {
            map.put("phoneNumber", employee.getPhoneNumber().toString());
        }
        if (!employee.getEmailId().equals(existingEmployee.getEmailId())) {
            map.put("emailId", employee.getEmailId());
        }
        if (!employee.getRole().equals(existingEmployee.getRole())) {
            map.put("role", employee.getRole());
        }
        if (!employee.getUserStatus().equals(existingEmployee.getUserStatus())) {
            map.put("userStatus", employee.getUserStatus());
        }
        if (!employee.getBranchId().equals(existingEmployee.getBranchId())) {
            map.put("branchId", employee.getBranchId().toString());
        }

        if (!map.isEmpty()) {
            query.update(map).where("employeeId", "=", String.valueOf(employee.getEmployeeId()));
            return dbService.executeUpdate(query.build());
        }

        return 0;
    }
}
