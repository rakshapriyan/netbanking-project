//$Id$
package com.banking.logical;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.entity.Employee;
import com.banking.service.EmployeeService;
import com.banking.util.ConvertJson;
import com.banking.util.ThreadLocale;

public class EmployeeImpl {
	private static EmployeeService employeeService = new EmployeeService();
	public static void getEmployees(HttpServletRequest request, HttpServletResponse response){
			Long userId = ThreadLocale.getUser().getUserId();
			Long branchId = employeeService.getEmployeeById(userId).getBranchId();
			List<Employee> employees = employeeService.getAllEmployeesInBranch(branchId);
			String json;
			try {
				json = ConvertJson.toJson(employees);
				response.getWriter().write(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void insertEmployee(HttpServletRequest request, HttpServletResponse response) {
		try {
			Employee employee = ConvertJson.fromJson(request, Employee.class);
			employeeService.addEmployee(employee);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateEmployee(HttpServletRequest request, HttpServletResponse response) {
		try {
			Employee employee = ConvertJson.fromJson(request, Employee.class);
			employeeService.updateEmployee(employee);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
