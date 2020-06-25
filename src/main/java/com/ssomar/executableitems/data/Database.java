package com.ssomar.executableitems.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ssomar.executableitems.ExecutableItems;

/**
 *
 * @author sqlitetutorial.net
 */
public class Database {

	private static Database instance;

	private ExecutableItems main;

	private String fileName;


	public void setup(ExecutableItems main) {
		this.main=main;
		createNewDatabase("data.db");
		createNewCommandsTable();
	}    

	public void createNewDatabase(String fileName) {

		this.fileName=fileName;

		String url = "jdbc:sqlite:"+main.getDataFolder() +"/"+fileName;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				System.out.println("[ExecutableItems] "+"Connexion to the db...");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createNewCommandsTable() {
		// SQLite connection string
		String url = "jdbc:sqlite:"+main.getDataFolder() +"/" +fileName;

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS commands (\n"
				+ "	player text ,\n"
				+ "	command text NOT NULL\n"
				+ ");";

		try (Connection conn = DriverManager.getConnection(url);
				Statement stmt = conn.createStatement()) {
			System.out.println("[ExecutableItems] "+"Verification of the table...");
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println("[ExecutableItems] "+e.getMessage());
		}
	}

	private Connection connect() {
		// SQLite connection string
		String url = "jdbc:sqlite:"+main.getDataFolder() + "/"+fileName;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
			System.out.println("[ExecutableItems] "+"Connexion OKAY");
		} catch (SQLException e) {
			System.out.println("[ExecutableItems] "+e.getMessage());
		}
		return conn;
	}

	public void insert(String player, String command) {
		System.out.println("[ExecutableItems] INSERT "+player+" / "+command);
		String sql = "INSERT INTO commands(player,command) VALUES(?,?)";

		try (Connection conn = this.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, player);
			pstmt.setString(2, command);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[ExecutableItems] "+e.getMessage());
		}
	}

	public void selectAll(){
		String sql = "SELECT player, command FROM commands";

		try (Connection conn = this.connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

			// loop through the result set
			while (rs.next()) {
				System.out.println("[ExecutableItems] "+rs.getString("player") +  "\t" + 
						rs.getString("command"));
			}
		} catch (SQLException e) {
			System.out.println("[ExecutableItems] "+e.getMessage());
		}
	}

	public void deleteCommandsForPlayer(String player){
		String sql = "DELETE FROM commands where player=\""+player+"\"";
		try {
			Connection conn = this.connect();
			Statement stmt  = conn.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.out.println("[ExecutableItems] "+e.getMessage());
		}
	}

	public List<String> selectCommandsForPlayer(String player){
		String sql = "SELECT command FROM commands where player=\""+player+"\"";
		//System.out.println("[ExecutableItems] DEBUG COMMANDS FOR PLAYER");

		List<String> list = new ArrayList<>();
		try (Connection conn = this.connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

			while (rs.next()) {
				list.add(rs.getString("command"));
			}

		} catch (SQLException e) {
			System.out.println("[ExecutableItems] "+e.getMessage());
		}
		return list;
	}

	public static Database getInstance() {
		if (instance == null)
			instance = new Database(); 
		return instance;
	}


}