package com.affirm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


/**
 * @author Prashant Rai
 *
 */

public class LoanApp {

	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	public static void main(String[] args) throws IOException {
		System.out.println("Loan assignment initiated...");
		//DbUtil.initDB(); //-to initialize DB (load data)
		getLoanInput(); //--take input and generates assignment.csv
		writeYield(); //--calculates and generates yields.csv
		
		System.out.println("Loan assignment completed.");
	}
	
	
public static void getLoanInput() {
		
		String fileName = DbUtil.PATH + "loans.csv";
		final String FILE_HEADER = "loan_id,facility_id";


		BufferedReader reader = null;
		FileWriter fileWriter = null;
		// read file line by line
		String line = null;
		Scanner scanner = null;
		int index = 0;
		
		try{
			reader = new BufferedReader(new FileReader(fileName));
			
            fileWriter = new FileWriter(DbUtil.PATH + "assignments.csv");
            fileWriter.append(FILE_HEADER.toString());
            
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
		
			boolean isFirstRow = true;
			while ((line = reader.readLine()) != null) {
				
				if(isFirstRow) {
					isFirstRow = false;
					continue;
				}
				
				scanner = new Scanner(line);
				scanner.useDelimiter(",");
				
				float interest_rate = 0;
				float amount = 0;
				int id = 0;
				float default_likelihood = 0;
				String state = "";
				
				while(scanner.hasNext()){
	                //read single line, put in string
	                String data = scanner.next();
	                
	                if(index == 0)
	                	interest_rate = Float.parseFloat(data);
	                if(index == 1)
	                	amount = Float.parseFloat(data);
	                if(index == 2)
	                	id = Integer.parseInt(data);
	                if(index == 3)
	                	default_likelihood = Float.parseFloat(data);
	                if(index == 4)
	                	state = data;
	                
	                index++;
	                
	            }
				index = 0;
				
				int facilityId = pickFacility(default_likelihood, state, amount, interest_rate);
                //System.out.println(">>>>>>>>Result:: "+facilityId);
				
                fileWriter.append(String.valueOf(id));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(facilityId));
                fileWriter.append(NEW_LINE_SEPARATOR);
				
			}
			
		} catch (IOException e) {
			System.err.println("Error loading loans:: "+e);
		} finally {
			try {
				reader.close();
				fileWriter.flush();
				fileWriter.close();
			} catch(IOException e) {}
			
		} 
		
	}
	
	
	public static int pickFacility(float default_likelihood, String state, float amount, float interest_rate) {
		
		Connection c = null;
		int facility_id = 0;
		
		//--Pick the first available facility	
		StringBuilder sb = new StringBuilder();
		sb.append(" select 	a.id as facility_id, a.amount as amount, a.interest_rate as facility_interest_rate, a.expected_yield as expected_yield ");
		sb.append(" from facilities a, banks b, covenants c WHERE ");
		sb.append(" a.bank_id = b.id 	");
		sb.append(" AND a.bank_id = c.bank_id "); 
		sb.append(" AND a.id = c.facility_id "); 
		sb.append(" AND c.max_default_likelihood >= ? ");  	//--default_likelihood should be lower than facility's max_default_likelihood
		sb.append(" AND c.banned_state<>?  ");				//--state should not be banned_state
		sb.append(" AND a.amount >= ?  ");					//--amount should be lower than facility amount
		sb.append(" AND a.interest_rate <=? "); 			
		sb.append(" ORDER BY a.amount, a.interest_rate ");	
		
		try{
			
			c = DbUtil.getConnection();
			PreparedStatement stmt = c.prepareStatement(sb.toString());
			stmt.setFloat(1, default_likelihood);
			stmt.setString(2, state);
			stmt.setFloat(3, amount);
			stmt.setFloat(4, interest_rate);
			ResultSet rs = stmt.executeQuery();
			
			String sql = "UPDATE facilities SET amount = ?, expected_yield = ? WHERE id =?";
			PreparedStatement stmt2 = c.prepareStatement(sql);
			
			while (rs.next()) {
				
				facility_id = rs.getInt("facility_id");
	            float facility_interest_rate = rs.getFloat("facility_interest_rate");
	            float newAmountAvail = rs.getFloat("amount") - amount;
	            float expectedYield = rs.getFloat("expected_yield");
	            float new_yield = getYield(default_likelihood, interest_rate, amount, facility_interest_rate, expectedYield);
	            
	            stmt2.setFloat(1, newAmountAvail);
	            stmt2.setFloat(2, new_yield);
	            stmt2.setFloat(3, facility_id);
	            stmt2.executeUpdate();
			}
			
			
		} catch (SQLException e) {
			System.err.println("Error in pickFacility:: "+e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(c != null)
					c.close();
			} catch(SQLException e){}
			
		} 
		return facility_id;
	
	}
	
	
	private static float getYield(float default_likelihood, float interest_rate, float amount, float facility_interest_rate, float expected_yield) {
        return expected_yield + (1 - default_likelihood) * interest_rate * amount - default_likelihood * amount - facility_interest_rate * amount;
	}
	
	
	
	
	public static void writeYield() {
		
		final String FILE_HEADER = "facility_id,expected_yield";
		
		FileWriter fileWriter = null;
		Connection c = null;
		Statement stmt = null;
		
		try {
			fileWriter = new FileWriter(DbUtil.PATH + "yields.csv");
			fileWriter.append(FILE_HEADER.toString());
            
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
				
			String sql  = "select id, expected_yield from facilities";
			c = DbUtil.getConnection();
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				int id = rs.getInt(1);
				float expected_yield = rs.getFloat(2);
				
				fileWriter.append(String.valueOf(id));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(expected_yield));
                fileWriter.append(NEW_LINE_SEPARATOR);   
				
			}
				
		
		} catch (SQLException e) {
			System.err.println("Error in writeYield:: "+e);
			e.printStackTrace();
		} catch(IOException e) {
			System.err.println("Error in writeYield:: "+e);
			e.printStackTrace();
			
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				if(c != null)
					c.close();
			} catch(IOException e) {}
			catch(SQLException e){}
			
		} 
		
	}
	

}
