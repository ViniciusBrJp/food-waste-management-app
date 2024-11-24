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
	
	
		    // ingredientsテーブルの作成
		    Statement stmt = connection.createStatement();
		    
		    stmt.execute("DROP TABLE IF EXISTS ingredients");
		    
		    stmt.execute(
		    	    "CREATE TABLE IF NOT EXISTS ingredients (" +
		    	    "id INTEGER PRIMARY KEY, " +
		    	    "pname VARCHAR(255), " +
		    	    "iname VARCHAR(255), " +
		    	    "pdate VARCHAR(255), " +
		    	    "edate VARCHAR(255), " +
		    	    "category VARCHAR(255) CHECK(category IN (" +
		    	    "'穀類', 'いも及びでん粉類', '砂糖及び甘味類', '豆類', '種実類', " +
		    	    "'野菜類', 'きのこ類', '藻類', '魚介類', '肉類', " +
		    	    "'卵類', '乳類', '油脂類', '菓子類', '飲料類', " +
		    	    "'調味料類', 'その他'))" +
		    	    ")"
		    	);

		} catch (SQLException e) {
		    e.printStackTrace();
		}
    }

    public static Connection getConnection() {
		return connection;
    }
}
