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

            // 既存のテーブルを削除
            stmt.execute("DROP TABLE IF EXISTS ingredients");

            // 新しいingredientsテーブルを作成
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS ingredients (" +
                "id INTEGER PRIMARY KEY, " +
                "fname VARCHAR(255), " +
                "pname VARCHAR(255), " +
                "iname VARCHAR(255), " +
                "pdate VARCHAR(255), " +
                "edate VARCHAR(255), " +
                "category VARCHAR(255) CHECK(category IN (" +
                "'穀物', '芋類', '肉類', '魚介類', '卵類', '豆類', " +
                "'野菜', '果物', 'きのこ類', '乳製品', '油脂類', " +
                "'調味料', '菓子類', '飲料類', 'その他'))" +
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
