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
 * Hello world!
 *
 */
public class DbUtil {
	
	public static final String PATH = "/Users/prash/Documents/MyWorkspace/affirm/files/";
	
	private static final String SQL_FACILILTY = "INSERT INTO facilities (id, interest_rate, amount, bank_id, expected_yield) " + "VALUES (?,?,?,?,?);";
	private static final String SQL_CONVENANTS = "INSERT INTO covenants (facility_id, max_default_likelihood, bank_id, banned_state) " + "VALUES (?,?,?,?);";
	private static final String SQL_BANKS = "INSERT INTO banks (id, name) " + "VALUES (?,?);";
	
	public static void main(String[] args) throws Exception {
		initDB();
	}
	
	public static void initDB() throws IOException {
		//dropTables();
		
		//createTables();
//		deleteTables();
		
//		loadFacility();
//		loadCovenant();
//		loadBank();
		
		showCovenants();
		showFacility();
		showBanks();
		//runJoinSelect();
		
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
				////create table if not exists covenants (bank_id real, facility_id real, max_default_likelihood real, banned_state text)
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
			System.err.println("Error loading covenants:: "+e);
		} finally {
			reader.close();
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		} 

	}
	
	
	public static void createTables() {
		
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
	
	
	public static void dropTables() {
		
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
	
	public static void deleteTables() {
		
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
			
			rs = stmt.executeQuery("SELECT COUNT(*) FROM facilities;");
			System.out.println("***Rows="+rs.getInt(1));
		
		} catch (SQLException e) {
			System.err.println("Error deleting tables:: "+e);
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
			
			rs = stmt.executeQuery("SELECT COUNT(*) FROM covenants;");
			System.out.println("***Rows="+rs.getInt(1));
		
		} catch (SQLException e) {
			System.err.println("Error showing tables:: "+e);
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

	public static void runJoinSelect() {
		Connection c = null;
		Statement stmt = null;
		
		System.out.println("*** Show JOIN *****");
		
		try {
			c = getConnection();
			stmt = c.createStatement();
			
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT 	a.id as facility_id, a.amount as amount ");
			sb.append(" FROM 	facilities a, banks b, covenants c  ");
			sb.append(" WHERE 	a.bank_id = b.id 	");
			sb.append(" AND 	a.bank_id = c.bank_id "); 
			sb.append(" AND 	a.id = c.facility_id "); 
//			sb.append(" AND c.max_default_likelihood >= 0.02 "); 
			
			String s = "SELECT a.id, a.amount, c.banned_state  "
					+ "from facilities a "
					//+ "INNER JOIN banks b ON a.bank_id = b.id "
					+ "INNER JOIN covenants c ON a.id = c.facility_id WHERE a.id=2";
			
			
			//ResultSet rs = stmt.executeQuery(sb.toString());
			ResultSet rs = stmt.executeQuery(s);
			while (rs.next()) {
				
				int id = rs.getInt(1);
				float amt = rs.getFloat(2);
				String name = rs.getString(3);
				
				
	
				System.out.println("------------------------------");
				//System.out.println("ID = " + id+", Amt = " + amt);
				System.out.println("ID = " + id+", Amt = " + amt + ", Name = " + name);
				System.out.println("------------------------------");
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
		
		/*
create table if not exists banks (id real, name text);
create table if not exists facilities (id real, interest_rate real, amount real, bank_id real, expected_yield real);
create table if not exists covenants (bank_id real, facility_id real, max_default_likelihood real, banned_state text);
		 
INSERT INTO covenants VALUES(2, 0.09, 1, 'MT');
INSERT INTO covenants VALUES(1, 0.06, 2, 'VT');
INSERT INTO covenants VALUES(1, 0.09, 2, 'CA');

INSERT INTO facilities VALUES(61104, 0.07, 2, 1,0);
INSERT INTO facilities VALUES(126122, 0.06, 1, 2,0);

--
INSERT INTO facilities VALUES(2, 0.07, 61104, 1,0);
INSERT INTO facilities VALUES(1, 0.06, 126122, 2,0);


UPDATE facilities SET amount=126122, id=1 WHERE bank_id=2


SELECT a.id, a.amount, b.name,  
from facilities a 
INNER JOIN banks b ON a.bank_id = b.id ;

SELECT a.id, a.amount, b.name, 
from facilities a 
INNER JOIN banks b ON a.bank_id = b.id 
INNER JOIN covenants c ON a.id=c.facility_id;


		 * 
		 * 
		 * */
		
	}
	
	
	
}
