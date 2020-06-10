package com.kopo.teamProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

public class DB {
	private Connection connection;
	private String dbFileName;
	private String dbTableName;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		public DB(String databaseFileName, String dbTableName) {
			this.dbFileName = databaseFileName;
			this.dbTableName = dbTableName;
		}
		public boolean open() {
			try {
				SQLiteConfig config = new SQLiteConfig();
				//sqlite를 사용하여, this.dbFIleName에 입력된 db파일과 연결
				this.connection = DriverManager.getConnection("jdbc:sqlite:/" + this.dbFileName, config.toProperties());
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		public boolean close() {
			if (this.connection == null) {
				return true;
			}
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}	
		public class delect {
			public void deleteData(String id) throws SQLException{
				String query = "DELETE FROM " + this.dbTableName + " WHERE id=?;";
				PreparedStatement preparedStatement = this.connection.prepareStatement(query);
				preparedStatement.setString(1, id);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
	
		}
		
		public void insertData(Teacher teacher) throws SQLException {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = dateFormat.format(System.currentTimeMillis());
			String insertQuery = "INSERT INTO " + this.dbTableName + " (name, id, pw, nclass, address, phone, created) values('" + teacher.name + "', '" + 
					teacher.id + "', '" + teacher.pw + "', '" + teacher.nclass + "', '" + teacher.address + "', " + teacher.phone + ", '" + date + "');";
			Statement statement = this.connection.createStatement();
			statement.executeUpdate(insertQuery);
			statement.close();
		}
}
