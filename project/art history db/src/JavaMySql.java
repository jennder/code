/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

db.mysql.url="jdbc:mysql://localhost:3306/db?characterEncoding=UTF-8&useSSL=false"
 */
// Jennifer Der
// CS 3200 Summer 1 2017

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;


/**
 *
 * @author kath
 */
public class JavaMySql {
	public static void main(String[] args) {
		System.out.println("enter username");
		Scanner myScanner = new Scanner(System.in);
		userName = myScanner.nextLine();
		System.out.println("username: " + userName);
		System.out.println("enter password");
		password = myScanner.nextLine();
		JavaMySql app = new JavaMySql();
		app.run();
		ArrayList<String> allCharacters;
		try {
			allCharacters = getCharacters();
		} catch (SQLException e) {
			System.out.println("db error");
			e.printStackTrace();
			return;
		}
		for (String s : allCharacters) {
			System.out.println(s);
		}


		boolean success = false;
		while (!success) {
			System.out.println("enter character name");
			String curr = myScanner.nextLine();
			if (allCharacters.contains(curr)) {
				charName = curr;
				System.out.println("tracking characters");
				success = true;
			}
			else {
				System.out.println("character does not exist");
			}
		}
		try {
			trackCharacters();
		} catch (SQLException e) {
			System.out.println("unsuccessful tracking of" + charName);
			e.printStackTrace();
			return;
		}
		myScanner.close();
	}
	/** The name of the MySQL account to use (or empty for anonymous) */
	private static String userName = "";

	/** The password for the MySQL account (or empty for anonymous) */
	private static String password = "";

	/** The name of the computer running MySQL */
	private static String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private static int portNumber = 3306;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private static String dbName = "starwarsfinal";

	/** The name of the table we are testing with */
	private final static String tableName = "timetable";
	private final boolean useSSL = false;

	private static String charName = "";
	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);

		conn = DriverManager.getConnection("jdbc:mysql://"
				+ serverName + ":" + portNumber + "/" + dbName + "?characterEncoding=UTF-8&useSSL=false",
				connectionProps);
		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public static boolean executeUpdate(Connection conn, String command) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(command); // This will throw a SQLException if it fails
			return true;
		} finally {

			// This will run whether we throw an exception or not
			if (stmt != null) { stmt.close(); }
		}
	}

	/**
	 * Connect to MySQL and do some stuff.
	 */
	public static void run() {

		// Connect to MySQL
		Connection conn = null;
		try {
			conn = getConnection();
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}
	}


	/**
	 * Connect to the DB and do some stuff
	 * @param args
	 * @throws SQLException 
	 */

	public static ArrayList<String> getCharacters() throws SQLException {
		ArrayList<String> allCharacters = new ArrayList<String>();
		Statement stmt = getConnection().createStatement();

		String sql = "SELECT character_name FROM characters";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			String characterName  = rs.getString("character_name");
			allCharacters.add(characterName);
		}
		return allCharacters;
	}

	public static void trackCharacters() throws SQLException {
		Statement stmt = getConnection().createStatement();
		String sql = "CALL track_character('" + charName + "')";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			String charName = rs.getString("character_name");
			String planetName = rs.getString("planet_name");
			String movieName = rs.getString("title");
			int scenes  = rs.getInt("scenes");

			System.out.print("character: " + charName);
			System.out.print(", planet: " + planetName);
			System.out.print(", movie: " + movieName);
			System.out.println(", number of scenes: " + scenes);
		}
		rs.close();
	}

}


