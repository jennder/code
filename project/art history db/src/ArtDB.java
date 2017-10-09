/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

db.mysql.url="jdbc:mysql://localhost:3306/db?characterEncoding=UTF-8&useSSL=false"
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class ArtDB {

  /** The name of the MySQL account to use (or empty for anonymous) */
  private static String userName = "root"; 

  /** The password for the MySQL account (or empty for anonymous) */
  private static String password = "database17";

  /** The string for the type of user using the program */
  private static String userType = "";

  /** The table the user is interested in */
  private static String desiredTable = "";

  /** The desired operation by the user */
  private static String desiredOp = "";

  /** The field the user is interested in */
  private static String fieldName = "";

  /** The instance the user is interested in */
  private static String instanceName = "";

  /** The name of the computer running MySQL */
  private final static String serverName = "localhost";

  /** The port of the MySQL server (default is 3306) */
  private final static int portNumber = 3306;

  /** The name of the database we are testing with (this default is installed with MySQL) */
  private static String dbName = "arthistorydb";

  // our SQL SELECT query
  private static String query = "select";

  // list of exhibits
  private static ArrayList<String> exhibitList = new ArrayList<String>();

  // determines whether the user inputs a valid artist
  private static boolean validArtist = false;

  // determines whether the user inputs a valid operation
  private static boolean validOperation = false;

  // determines whether the user inputs a valid exhibit
  private static boolean validExhibit = false;

  /**
   * Connect to the DB and do some stuff
   * @param args
   * @throws SQLException 
   */
  public static void main(String[] args) throws SQLException {
    Scanner sc = new Scanner(System.in);
    System.out.println("Are you a curator or guest?");
    userType = sc.nextLine();
    if (userType.equals("curator")) {
      while (!validOperation) {
        System.out.println("Would you like to update, create, read, or delete?");
        desiredOp = sc.nextLine();
        if (desiredOp.equals("update") || desiredOp.equals("create") || desiredOp.equals("delete") || desiredOp.equals("read")) {
          validOperation = true;
        }
      }
      curatorCommands();
    }
    else if (userType.equals("guest")) {
      System.out.println("Would you like to access information about an exhibit or an artist?");
      if (sc.hasNextLine()){desiredTable = sc.nextLine();}
      guestCommands();
    }

    ArtDB app = new ArtDB();
    app.run();
    sc.close();
  }

  // actions that take place if the user is a curator
  public static void curatorCommands() throws SQLException {
    Scanner sc = new Scanner(System.in);
    if (desiredOp.equals("update")) {
      curatorUpdate();
    }
    else if (desiredOp.equals("create")) {
      curatorCreate();
    }
    else if (desiredOp.equals("delete")) {
      curatorDelete();
    }
    else {
      System.out.println("Would you like to read information about an exhibit or an artist?");
      desiredTable = sc.nextLine();
      guestCommands();
    }
    sc.close();
  }

  //actions that take place if the user is a museum guest
  public static void guestCommands() {
    Scanner sc = new Scanner(System.in);

    ArrayList<String> artistList = generateArtists();
    if (desiredTable.equals("artist")) {
      System.out.println("The artists that we currently have on display are: ");
      for (int i=0; i < artistList.size(); i++) {
        System.out.println(artistList.get(i));
      }  
      while (!validArtist) {
        System.out.println("Which artist are you interested in learning about?");
        fieldName = sc.nextLine();
        if (artistList.contains(fieldName)) {
          validArtist = true;
        }
        else {
          System.out.println("Sorry, but this artist is not available to be picked right now.");
        }
      }
      System.out.println("The artworks we have by this artist are: ");
      try {
        trackArtist();
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    generateExhibits();
    if (desiredTable.equals("exhibit")) {
      System.out.println("The exhibits that we currently have are: ");
      for (int i=0; i < exhibitList.size(); i++) {
        System.out.println(exhibitList.get(i));
      }  
      while (!validExhibit) {
        System.out.println("Which exhibit are you interested in learning about?");
        fieldName = sc.nextLine();
        if (exhibitList.contains(fieldName)) {
          validExhibit = true;
        }
        else {
          System.out.println("Sorry, but this exhibit is not available to be picked right now.");
        }
      }
      System.out.println("The artworks we have in this exhibit are: ");
      try {
        trackExhibit();
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    sc.close();
  }

  // handles the update operation used by the curator
  public static void curatorUpdate() {
    Scanner sc = new Scanner(System.in);
    System.out.println("Pick a table to update, the tables that are available are: artist");
    desiredTable = sc.nextLine();
    ArrayList<String> artistList = generateArtists();
    if (desiredTable.equals("artist")) {
      System.out.println("The artists that we currently have on display are: ");
      for (int i=0; i < artistList.size(); i++) {
        System.out.println(artistList.get(i));
      }  
      while (!validArtist) {
        System.out.println("Which artist are you interested in updating?");
        instanceName = sc.nextLine();
        if (artistList.contains(instanceName)) {
          validArtist = true;
        }
        else {
          System.out.println("Sorry, but this artist is not available to be picked right now.");
        }
      }
      System.out.println("Would you like to update the yod or period?");
      fieldName = sc.nextLine();
      if (fieldName.equals("period")) {
        System.out.println("Valid Periods: Impressionism, Modern, Renaissance, Cubism, Surrealism");
      }
      System.out.println("What would you like the new value to be for "+fieldName+" ?");
      String newValue = sc.nextLine();
      if (fieldName.equals("yod")) {
        try {
          yoProcedure(newValue);
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }
      else {
        try {
          periodProcedure(newValue);
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    sc.close();
  }

  // handles the create operation used by the curator
  public static void curatorCreate() throws SQLException {
    Scanner sc = new Scanner(System.in);
    System.out.println("Pick a table to create a new instance in. The tables that are available are: artist, art_piece");
    desiredTable = sc.nextLine();
    if (desiredTable.equals("artist")) {
      createArtist();
    }
    if (desiredTable.equals("art_piece")) {
      createArtPiece();
    }
    sc.close();
  }

  // creates an artist
  public static void createArtist() throws SQLException {
    Scanner sc = new Scanner(System.in);
    Connection conn = getConnection();
    String sql = "INSERT INTO artist VALUES (?, ?, ?, ?)";
    System.out.println("What would you like the name of the artist to be?");
    String name = sc.nextLine();
    System.out.println("What would you like the year of birth of the artist to be?");
    int yob = sc.nextInt();
    System.out.println("What would you like the year of death of the artist to be?");
    int yod = sc.nextInt(); 
    System.out.println("The default period is 'Contemporary', to change this, update the period of the artist.");
    String period = "Contemporary";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, name);
    ps.setInt(2, yob);
    ps.setInt(3, yod);
    ps.setString(4, period);
    ps.execute();
    conn.close();
    sc.close();
  }

  // creates a piece
  public static void createArtPiece() throws SQLException {
    Scanner sc = new Scanner(System.in);
    Connection conn = getConnection();
    String sql = "INSERT INTO art_piece VALUES (?, ?, ?, ?)";
    System.out.println("What would you like the name of the art piece to be?");
    String name = sc.nextLine();
    ArrayList<String> artistList = generateArtists();
    System.out.println("The possible artists for the piece are: ");
    for (int i=0; i < artistList.size(); i++) {
      System.out.println(artistList.get(i));
    }  
    while (!validArtist) {
      System.out.println("Which one of these artist is the artist of the piece?");
      instanceName = sc.nextLine();
      if (artistList.contains(instanceName)) {
        validArtist = true;
      }
      else {
        System.out.println("Sorry, but this is not accurate.");
      }
    }
    String artist = instanceName;
    System.out.println("Pick one of the following exhibits to place the art piece in: modern, high renaissance");
    String exhibit = sc.nextLine();    
    System.out.println("What date was this piece acquired?(mmyyyy)");
    int date = sc.nextInt();    
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, name);
    ps.setString(2, artist);
    ps.setString(3, exhibit);
    ps.setInt(4, date);
    ps.execute();
    conn.close();
    sc.close();
  }

  // handles the delete operation used by the curator
  public static void curatorDelete() throws SQLException {
    Scanner sc = new Scanner(System.in);
    Connection conn = getConnection();
    String sql = "DELETE FROM art_piece WHERE piece_name = ?";
    ArrayList<String> artList = generateArtwork();
    System.out.println("Choose a piece of artwork to delete:");
    for (String s : artList) {
      System.out.println(s);
    }
    String piece = sc.nextLine();
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, piece);
    ps.execute();
    conn.close();
    sc.close();
  }

  // calls the yob/yod procedure
  public static void yoProcedure(String newValue) throws SQLException{
    Connection conn = getConnection();
    String sql = "CALL update_artist_year(?,?)";
    PreparedStatement preparedStmt = conn.prepareStatement(sql);
    preparedStmt.setString(1, instanceName);
    preparedStmt.setInt(2, Integer.parseInt(newValue));
    preparedStmt.execute();
    conn.close();
  }

  // calls the period procedure
  public static void periodProcedure(String newValue) throws SQLException{
    Connection conn = getConnection();
    String sql = "CALL update_artist_period(?,?)";
    PreparedStatement preparedStmt = conn.prepareStatement(sql);
    preparedStmt.setString(1, instanceName);
    preparedStmt.setString(2, newValue);
    preparedStmt.execute();
    conn.close();
  }

  // tracks the artist
  public static void trackArtist() throws SQLException {
    Statement stmt = getConnection().createStatement();
    String sql = "CALL track_artist('" + fieldName + "')";
    ResultSet rs = stmt.executeQuery(sql);
    while (rs.next()) {
      String piece_name = rs.getString("piece_name");
      String artist = rs.getString("artist");
      String exhibit = rs.getString("exhibit");
      int date_acquired  = rs.getInt("date_acquired");

      System.out.print("piece name: " + piece_name);
      System.out.print(", artist's name: " + artist);
      System.out.print(", exhibit: " + exhibit);
      System.out.println(", date acquired: " + date_acquired);
    }
    rs.close();
  }

  // tracks the exhibit
  public static void trackExhibit() throws SQLException {
    Statement stmt = getConnection().createStatement();
    String sql = "CALL track_exhibit('" + fieldName + "')";
    ResultSet rs = stmt.executeQuery(sql);
    while (rs.next()) {
      String piece_name = rs.getString("piece_name");
      String artist = rs.getString("artist");
      String exhibit = rs.getString("exhibit");
      int date_acquired  = rs.getInt("date_acquired");

      System.out.print("piece name: " + piece_name);
      System.out.print(", artist's name: " + artist);
      System.out.print(", exhibit: " + exhibit);
      System.out.println(", date acquired: " + date_acquired);
    }
    rs.close();
  }

  // generates a list of artists 
  public static ArrayList<String> generateArtists() {
    ArrayList<String> artistList = new ArrayList<String>();
    try
    {
      Connection conn = null;
      Properties connectionProps = new Properties();
      connectionProps.put("user", userName);
      connectionProps.put("password", password);
      conn = DriverManager.getConnection("jdbc:mysql://"
          + serverName + ":" + portNumber + "/" + dbName + "?characterEncoding=UTF-8&useSSL=false",
          connectionProps);
      query = "SELECT aname FROM artist";
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      // iterate through the java resultset
      while (rs.next())
      {
        artistList.add(rs.getString("aname"));
      }
      st.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
    return artistList;
  }

  //generates a list of art pieces 
  public static ArrayList<String> generateArtwork() {
    ArrayList<String> pieceList = new ArrayList<String>();
    try
    {
      Connection conn = null;
      Properties connectionProps = new Properties();
      connectionProps.put("user", userName);
      connectionProps.put("password", password);
      conn = DriverManager.getConnection("jdbc:mysql://"
          + serverName + ":" + portNumber + "/" + dbName + "?characterEncoding=UTF-8&useSSL=false",
          connectionProps);
      query = "SELECT piece_name FROM art_piece";
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      // iterate through the java resultset
      while (rs.next())
      {
        pieceList.add(rs.getString("piece_name"));
      }
      st.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
    return pieceList;
  }

  //generates a list of exhibits 
  public static void generateExhibits() {
    exhibitList = new ArrayList<String>();
    try
    {
      Connection conn = null;
      Properties connectionProps = new Properties();
      connectionProps.put("user", userName);
      connectionProps.put("password", password);
      conn = DriverManager.getConnection("jdbc:mysql://"
          + serverName + ":" + portNumber + "/" + dbName + "?characterEncoding=UTF-8&useSSL=false",
          connectionProps);
      query = "SELECT ename FROM exhibit";
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(query);
      // iterate through the java resultset
      while (rs.next())
      {
        exhibitList.add(rs.getString("ename"));
      }
      st.close();
    }
    catch (Exception e)
    {
      System.err.println("Got an exception! ");
      System.err.println(e.getMessage());
    }
  }

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
  public boolean executeUpdate(Connection conn, String command) throws SQLException {
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
  public void run() {

    // Connect to MySQL
    Connection conn = null;
    try {
      conn = this.getConnection();
    } 
    catch (SQLException e) {
      System.out.println("ERROR: Could not connect to the database");
      e.printStackTrace();
      return;
    }
  }
}