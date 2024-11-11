package mfl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Databaseクラス
 */
public class Database {
    private static Connection connection;

    public static void initializeDatabase() {
		try {
		    // データベースへの接続を作成
		    connection = DriverManager.getConnection("jdbc:sqlite:./data/mfldb.db");
	
	
		    // linksテーブルの作成
		    Statement stmt = connection.createStatement();
		    stmt.execute("CREATE TABLE IF NOT EXISTS ingredients (" +
				           "id INTEGER PRIMARY KEY, " +
				           "name VARCHAR(255), " +
				           "pdate VARCHAR(255), " +
				           "edate VARCHAR(255))");
		} catch (SQLException e) {
		    e.printStackTrace();
		}
    }

    public static Connection getConnection() {
		return connection;
    }
}
