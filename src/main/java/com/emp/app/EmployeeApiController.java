package com.emp.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import models.Employee;
import models.EmployeeResponse;
import perststance.DatabaseManager;

@Path("api/employee")
@Consumes({ MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON })
public class EmployeeApiController {
	
	@GET
	@Path("get/all")
	public Response getAllEmployee(){
		EmployeeResponse response = new EmployeeResponse();
		List<Employee> list = new ArrayList<Employee>();
		try {
			DatabaseManager dbManager = new DatabaseManager();
			list = dbManager.getAllEmployee();
			response.setSuccess(true);
			response.setMessage(list.size() + " Elements Feteched");
			response.setEmployee(list);
		} catch (ClassNotFoundException e) {
			response.setSuccess(false);
			response.setMessage(" Error due to "+e.toString());
			response.setEmployee(list);
		} catch (SQLException e) {
			response.setSuccess(false);
			response.setMessage(" Error due to "+e.toString());
			response.setEmployee(list);
		}
		return Response.ok(response).build();
	}
	
	@GET
	@Path("get/{empId}")
	public Response getEmployeeById(@PathParam("empId") String empId) {
		EmployeeResponse response = new EmployeeResponse();
		
		try {
			DatabaseManager dbManager = new DatabaseManager();
			Employee emp = dbManager.getEmployee(empId);
			response.setSuccess(true);
			response.setMessage("1 Elements Feteched");
			List<Employee> list = new ArrayList<Employee>();
			list.add(emp);
			response.setEmployee(list);
		} catch (ClassNotFoundException e) {
			response.setSuccess(false);
			response.setMessage(" Error due to "+e.toString());
		} catch (SQLException e) {
			response.setSuccess(false);
			response.setMessage(" Error due to "+e.toString());
		}
		return Response.ok(response).build();
	}
	
	@POST
	@Path("add")
	public Response addEmployee(Employee employee) {
		EmployeeResponse employeeResponse = new EmployeeResponse();
		ResponseBuilder responseBuilder;
		try {
			DatabaseManager dbManager = new DatabaseManager();
			int status = dbManager.addEmployee(employee);
			if(status > 0) 
			{
				employeeResponse.setSuccess(true);
				employeeResponse.setMessage("Added Successfully");
				responseBuilder = Response.status(HttpServletResponse.SC_CREATED).entity(employeeResponse);
			}
			else 
			{
				employeeResponse.setSuccess(false);
				employeeResponse.setMessage("Add Error due to Data Error");
				responseBuilder = Response.status(HttpServletResponse.SC_NOT_ACCEPTABLE).entity(employeeResponse);
			}
		} catch (ClassNotFoundException e) {
			employeeResponse.setSuccess(false);
			employeeResponse.setMessage("Add Error due to Class Not Found");
			responseBuilder = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(employeeResponse);
		} catch (SQLException e) {
			employeeResponse.setSuccess(false);
			employeeResponse.setMessage("Add Error due to Database Error");
			responseBuilder = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(employeeResponse);
		}
		return responseBuilder.build();
	}

	@PUT
	@Path("update")
	public Response updateEmployee(Employee employee) {
		EmployeeResponse employeeResponse = new EmployeeResponse();
		ResponseBuilder responseBuilder;
		
		if(employee == null) {
			employeeResponse.setSuccess(false);
			employeeResponse.setMessage("Please set Employee data");
			responseBuilder = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		try {
			DatabaseManager dbManager = new DatabaseManager();
			int status = dbManager.updateEmployee(employee);
			if(status > 0)
			{
				employeeResponse.setSuccess(true);
				employeeResponse.setMessage("Updated Successfully");
				responseBuilder = Response.status(HttpServletResponse.SC_OK).entity(employeeResponse);
			}
			else
			{
				employeeResponse.setSuccess(false);
				employeeResponse.setMessage("Not found");
				responseBuilder = Response.status(HttpServletResponse.SC_NOT_FOUND).entity(employeeResponse);
			}
		} catch (ClassNotFoundException e) {
			employeeResponse.setSuccess(false);
			employeeResponse.setMessage("Error due to "+ e.toString());
			responseBuilder = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(employeeResponse);
		} catch (SQLException e) {
			employeeResponse.setSuccess(false);
			employeeResponse.setMessage("Error due to "+ e.toString());
			responseBuilder = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(employeeResponse);
		}
		return responseBuilder.build();
	}
	
	@Path("delete/{empId}")
	@DELETE
	public Response deleteEmployee(@PathParam("empId") String empId) {
		ResponseBuilder responseBuilder = null;
		EmployeeResponse employeeResponse = new EmployeeResponse();
		try {
			DatabaseManager dbManager = new DatabaseManager();
			int status = dbManager.deleteEmployee(empId);
			if(status > 0) 
			{
				employeeResponse.setSuccess(true);
				employeeResponse.setMessage("Deleted Successfully");
				responseBuilder = Response.status(HttpServletResponse.SC_OK).entity(employeeResponse);
			}
			else
			{
				employeeResponse.setSuccess(false);
				employeeResponse.setMessage("Employee with following Id not Found");
				responseBuilder = Response.status(HttpServletResponse.SC_NOT_FOUND).entity(employeeResponse);
			}
		} catch (ClassNotFoundException e) {
			employeeResponse.setSuccess(false);
			employeeResponse.setMessage("Employee with following Id not Found");
			responseBuilder = Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(employeeResponse);
		} catch (SQLException e) {
			employeeResponse.setSuccess(false);
			employeeResponse.setMessage("Employee with following Id not Found");
			responseBuilder = Response.status(HttpServletResponse.SC_NOT_FOUND).entity(employeeResponse);
		}
		
		return responseBuilder.build();
	}
}
