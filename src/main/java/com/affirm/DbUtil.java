package com.affirm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.affirm.bean.Bank;
import com.affirm.bean.Covenants;
import com.affirm.bean.Facilities;

/**
 * @author Prashant Rai
 *
 */
public class DbUtil {
	
	public static final String PATH = "/Users/prash/Documents/MyWorkspace/affirm/files/";
	
	private static final String SQL_FACILILTY = "INSERT INTO facilities (id, interest_rate, amount, bank_id, expected_yield) " + "VALUES (?,?,?,?,?);";
	private static final String SQL_CONVENANTS = "INSERT INTO covenants (facility_id, max_default_likelihood, bank_id, banned_state) " + "VALUES (?,?,?,?);";
	private static final String SQL_BANKS = "INSERT INTO banks (id, name) " + "VALUES (?,?);";
	
	//--For unit testing only
	public static void main(String[] args) throws Exception {
		initDB();
	}
	
	public static void initDB() throws IOException {
		//dropTables();
		//createTables();
		
		resetData();
		
		showCovenants();
		showFacility();
		showBanks();
		
	} 

	public static Connection getConnection() {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		//System.out.println("Opened database successfully");
		return c;
	}

	public static void loadFacility() throws IOException {
		
		String fileName = PATH + "facilities.csv";
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		// read file line by line
		String line = null;
		Scanner scanner = null;
		int index = 0;
		
		Connection c = null;
		PreparedStatement stmt = null;
		
		try{
		
			c = getConnection();
			stmt = c.prepareStatement(SQL_FACILILTY);
			
			boolean isFirstRow = true;
			while ((line = reader.readLine()) != null) {
				
				if(isFirstRow) {
					isFirstRow = false;
					continue;
				}
				
				
				Facilities facility = new Facilities();
				scanner = new Scanner(line);
				scanner.useDelimiter(",");
				
				while(scanner.hasNext()){
	                //read single line, put in string
	                String data = scanner.next();
	                
	                if(index == 0)
	                	facility.setAmt(Float.parseFloat(data));
	                if(index == 1)
	                	facility.setInterestRate(Float.parseFloat(data));
	                if(index == 2)
	                	facility.setId(Integer.parseInt(data));
	                if(index == 3)
	                	facility.setBank_id(Integer.parseInt(data));
	                
	                index++;
	                
	            }
				
				stmt.setInt(1, facility.getId());
				stmt.setFloat(2, facility.getInterestRate());
				stmt.setFloat(3, facility.getAmt());
				stmt.setInt(4, facility.getBank_id());
				stmt.setFloat(5, 0);
				stmt.executeUpdate();
				
				index = 0;
				
			}
			
		} catch (SQLException e) {
			System.err.println("Error loading facilities:: "+e);
		} finally {
			reader.close();
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		} 

	}
	
	
	public static void loadCovenant() throws IOException {
		
		String fileName = PATH + "covenants.csv";
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		// read file line by line
		String line = null;
		Scanner scanner = null;
		int index = 0;
		
		Connection c = null;
		PreparedStatement stmt = null;
		
		try{
		
			c = getConnection();
			stmt = c.prepareStatement(SQL_CONVENANTS);
			
			boolean isFirstRow = true;
			while ((line = reader.readLine()) != null) {
				
				if(isFirstRow) {
					isFirstRow = false;
					continue;
				}
				c = getConnection();
				
				Covenants covenants = new Covenants();
				scanner = new Scanner(line);
				scanner.useDelimiter(",");
				while(scanner.hasNext()){
	                //read single line, put in string
	                String data = scanner.next();
	                
	                if(index == 0)
	                	covenants.setFacilityId(Integer.parseInt(data));
	                if(index == 1)
	                	covenants.setMaxDefault(Float.parseFloat(data.length() == 0 ? "0" : data));
	                if(index == 2)
	                	covenants.setBankId(Integer.parseInt(data));
	                if(index == 3)
	                	covenants.setBannedState(data);
	                
	                index++;
	                
	            }
				index = 0;
				
				stmt.setInt(1, covenants.getFacilityId());
				stmt.setFloat(2, covenants.getMaxDefault());
				stmt.setInt(3, covenants.getBankId());
				stmt.setString(4, covenants.getBannedState());
				stmt.executeUpdate();
				
			}
			
		} catch (SQLException e) {
			System.err.println("Error loading covenants:: "+e);
		} finally {
			reader.close();
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		} 

	}
	
	public static void loadBank() throws IOException {
		
		String fileName = PATH + "banks.csv";
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		// read file line by line
		String line = null;
		Scanner scanner = null;
		int index = 0;
		
		Connection c = null;
		PreparedStatement stmt = null;
		
		try{
		
			c = getConnection();
			stmt = c.prepareStatement(SQL_BANKS);
			
			boolean isFirstRow = true;
			while ((line = reader.readLine()) != null) {
				
				if(isFirstRow) {
					isFirstRow = false;
					continue;
				}
				c = getConnection();
				
				Bank bank = new Bank();
				scanner = new Scanner(line);
				scanner.useDelimiter(",");
				while(scanner.hasNext()){
	                //read single line, put in string
	                String data = scanner.next();
	                
	                if(index == 0)
	                	bank.setId(Integer.parseInt(data));
	                if(index == 1)
	                	bank.setName(data);
	                
	                index++;
	                
	            }
				index = 0;
				
				stmt.setInt(1, bank.getId());
				stmt.setString(2, bank.getName());
				stmt.executeUpdate();
				
			}
			
		} catch (SQLException e) {
			System.err.println("Error loading banks:: "+e);
		} finally {
			reader.close();
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		} 

	}
	
	
	private static void createTables() {
		
		Connection c = null;
		Statement stmt = null;
		
		try{
			c = getConnection();
			stmt = c.createStatement();
			stmt.executeUpdate("create table if not exists banks (id real, name text)");
			stmt.executeUpdate("create table if not exists facilities (amount real, interest_rate real, id real, bank_id real, expected_yield real)");
			stmt.executeUpdate("create table if not exists covenants (facility_id real, max_default_likelihood real, bank_id real, banned_state text)");
		} catch (SQLException e) {
			System.err.println("Error creating tables:: "+e);
		} finally {
			
			try {
				stmt.close();
				c.close();
			} catch(SQLException e){}
			
		}
	}
	
	
	public static void showFacility() {
		Connection c = null;
		Statement stmt = null;
		
		System.out.println("*** Show Facility *****");
		
		try {
			c = getConnection();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM facilities;");
	
			while (rs.next()) {
				
				float amt = rs.getFloat("amount");
				float rate = rs.getFloat("interest_rate");
				int id = rs.getInt("id");
				int bankId = rs.getInt("bank_id");
				float expected_yield = rs.getFloat("expected_yield");
	
				
				System.out.print("[ ID = " + id);
				System.out.print(", Rate = " + rate);
				System.out.print(", Amt = " + amt);
				System.out.print(", bankid = " + bankId);
				System.out.print(", expected_yield = " + expected_yield);
				System.out.println("]");
			}
			
		
		} catch (SQLException e) {
			System.err.println("Error in showFacility:: "+e);
		} finally {
			
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		}
	}
	
	public static void showCovenants() {
		Connection c = null;
		Statement stmt = null;
		
		System.out.println("*** Show Covenants *****");
		
		try {
			c = getConnection();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM covenants");
	
			while (rs.next()) {
				
				int facilityId = rs.getInt(1);
				float max_default = rs.getFloat(2);
				int bankId = rs.getInt(3);
				String bannedState = rs.getString(4);
	
				System.out.print("[ facilityId = " + facilityId);
				System.out.print(", max_default = " + max_default);
				System.out.print(", bankId = " + bankId);
				System.out.print(", bannedState = " + bannedState);
				System.out.println(" ]");
			}
			
		
		} catch (SQLException e) {
			System.err.println("Error in showCovenants:: "+e);
		} finally {
			
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		}
	}
	
	
	public static void showBanks() {
		Connection c = null;
		Statement stmt = null;
		
		System.out.println("*** Show Banks *****");
		
		try {
			c = getConnection();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM banks;");
	
			while (rs.next()) {
				
				int id = rs.getInt(1);
				String name = rs.getString(2);
	
				System.out.print("[ ID = " + id);
				System.out.println(", Name = " + name +" ]");
			}
		
		} catch (SQLException e) {
			System.err.println("Error in showBanks:: "+e);
			e.printStackTrace();
		} finally {
			
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		}
	}

	
	private static void dropTables() {
		
		Connection c = null;
		Statement stmt = null;
		
		try {
			c = getConnection();
			stmt = c.createStatement();
			stmt.executeUpdate("drop table if exists banks");
			stmt.executeUpdate("drop table if exists facilities");
			stmt.executeUpdate("drop table if exists covenants");
			
		} catch (SQLException e) {
			System.err.println("Error dropping tables:: "+e);
		} finally {
			
			try {
				stmt.close();
				c.close();
			} catch(SQLException e){}
			
		}
	}
	
	private static void deleteTables() {
		
		Connection c = null;
		Statement stmt = null;
		
		try {
			c = getConnection();
			stmt = c.createStatement();
			stmt.executeUpdate("delete from banks");
			stmt.executeUpdate("delete from facilities");
			stmt.executeUpdate("delete from covenants");
			
		} catch (SQLException e) {
			System.err.println("Error deleting tables:: "+e);
		} finally {
			
			try {
				stmt.close();
				c.close();
			} catch(SQLException e){}
			
		}
	}
	
	private static void resetData() throws IOException{
		
		deleteTables();
		loadFacility();
		loadCovenant();
		loadBank();
		System.out.println("Date reset.");
	}
	
}
