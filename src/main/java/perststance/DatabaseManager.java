package perststance;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.emp.util.DateUtil;

import models.Employee;

public class DatabaseManager {
	
	private static final String DATABASE_NAME 	= "EMPLOYEEDB";
	private static final String TABLE_EMPLOYEE 	= "TBL_EMPLOYEE";
	private static final String USER_NAME 		= "root";
	private static final String USER_PASSWORD 	= "";
	private static final String DATABASE_URL 	= "localhost:3306";
	private static final String ConnectionUrl 	= "jdbc:mysql://"+DATABASE_URL+"/"+DATABASE_NAME;
	
	private Connection connection;
	
	public DatabaseManager() throws ClassNotFoundException, SQLException  {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(ConnectionUrl, USER_NAME, USER_PASSWORD);
	}
	
	public List<Employee> getAllEmployee(){
		List<Employee> employeeList = new ArrayList<Employee>();
		try {
			Statement statement = connection.createStatement();
			String QUERY = "SELECT * FROM "+TABLE_EMPLOYEE;
			ResultSet resultSet = statement.executeQuery(QUERY);
			while (resultSet.next()) {
				Employee employee = new Employee();
				employee.setId(resultSet.getInt("ID"));
				employee.setEmpId(resultSet.getString("EMPID"));
				employee.setEmpFirstName(resultSet.getString("EMPFIRSTNAME"));
				employee.setEmpLastName(resultSet.getString("EMPLASTNAME"));
				employee.setGender(resultSet.getString("EMPGENDER"));
				employee.setHireDate(resultSet.getDate("EMPDOB"));
				employeeList.add(employee);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeConnection();
		}
		return employeeList;
	}
	
	public Employee getEmployee(String empId) {
		Employee employee = new Employee();
		try {
			String QUERY= "SELECT * FROM "+TABLE_EMPLOYEE+" WHERE EMPID = ?";
			java.sql.PreparedStatement statement = connection.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, empId);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				employee.setId(resultSet.getInt("ID"));
				employee.setEmpId(resultSet.getString("EMPID"));
				employee.setEmpFirstName(resultSet.getString("EMPFIRSTNAME"));
				employee.setEmpLastName(resultSet.getString("EMPLASTNAME"));
				employee.setGender(resultSet.getString("EMPGENDER"));
				employee.setHireDate(resultSet.getDate("EMPDOB"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employee;
	}
	
	
	public void closeConnection() {
		if(connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int addEmployee(Employee employee) {
		if(employee == null) {
			return 0;
		}
		
		int status = 0;
		String QUERY = "INSERT INTO TBL_EMPLOYEE (EMPID,EMPFIRSTNAME,EMPLASTNAME,EMPGENDER,EMPDOB)VALUES(?,?,?,?,?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
			preparedStatement.setString(1, employee.getEmpId());
			preparedStatement.setString(2, employee.getEmpFirstName());
			preparedStatement.setString(3, employee.getEmpLastName());
			preparedStatement.setString(4, employee.getGender());
			java.sql.Date sqlDate = DateUtil.convertJavaDateToSqlDate(employee.getHireDate());
			preparedStatement.setDate(5, sqlDate);
			status = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			status = -1;
		}
		return status;
	}

	public int deleteEmployee(String empId) {
		int status = 0;
		String QUERY = "DELETE FROM "+TABLE_EMPLOYEE+" WHERE EMPID=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
			preparedStatement.setString(1, empId);
			status = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			status = -1;
		}
		return status;
	}

	public int updateEmployee(Employee employee) {
		int status = 0;
		String QUERY = "UPDATE "+TABLE_EMPLOYEE+" SET ";
		if(null != employee.getEmpFirstName())
			QUERY +="EMPFIRSTNAME = "+employee.getEmpFirstName();
		
		if(null != employee.getEmpLastName())
			QUERY +=", EMPLASTNAME = "+employee.getEmpFirstName();
		
		if(null != employee.getGender())
			QUERY +=", EMPGENDER = "+employee.getGender();
		
		if(null != employee.getHireDate())
			QUERY +=", EMPDOB = "+employee.getHireDate();
		
		QUERY+=" WHERE EMPID = "+employee.getEmpId();
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
			status = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			status = -1;
		}
		return status;
	}
}
