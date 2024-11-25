package mfl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/***
 * ManegeFoodLossクラス
 */
public class FWM {
	
	List<Ingredient> ings;

    public FWM() {}
    
    //Ingredient追加
    public void addIng(Ingredient ing) {
        try {
        	
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ingredients ("
            		+ "fname, pname, iname, pdate, edate, category) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, ing.getFName());
            ps.setString(2, ing.getPName());
            ps.setString(3, ing.getIName());
            ps.setString(4, ing.getPdate());
            ps.setString(5, ing.getEdate());
            ps.setString(6, ing.getCategory());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Ingredient削除
	public void deleteIng(int id) {
		try {
			Connection conn = Database.getConnection();
			
			PreparedStatement ps = conn.prepareStatement("DELETE FROM ingredients WHERE id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    //全データ取得
    public List<Ingredient> getIngs() {
        List<Ingredient> ings = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ingredients");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	int id = rs.getInt("id");
            	String fname = rs.getString("fname");
                String pname = rs.getString("pname");
                String iname = rs.getString("iname");
                String pdate = rs.getString("pdate");
                String edate = rs.getString("edate");
                String category = rs.getString("category");
                
                Ingredient ing = new Ingredient(fname, pname, iname, pdate, edate, category);
                ing.setId(id);
                ings.add(ing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ings;
    }
    
    //個別データ取得
	public Ingredient getIng(int id) {
		String fname = "";
		String pname = "";
        String iname = "";
        String pdate = "";
        String edate = "";
        String category = "";
		
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ingredients WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
            	fname = rs.getString("fname");
                pname = rs.getString("pname");
                iname = rs.getString("iname");
            	pdate = rs.getString("pdate");
            	edate = rs.getString("edate");
            	category = rs.getString("category");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Ingredient ing = new Ingredient(fname, pname, iname, pdate, edate, category);
        ing.setId(id);
        return ing;
	}

    public boolean isEmpty() {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM ingredients");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    //初期データ追加
    public void addInitialIngs() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    	
        addIng(new Ingredient("carrot.jpg", "人参", "人参", LocalDate.now().plusDays(0).format(formatter), LocalDate.now().plusDays(5).format(formatter), "野菜類"));
        addIng(new Ingredient("radish.jpg", "大根", "大根", LocalDate.now().plusDays(-2).format(formatter), LocalDate.now().plusDays(3).format(formatter), "野菜類"));
        addIng(new Ingredient("greenpepper.jpg", "ピーマン", "ピーマン", LocalDate.now().plusDays(-3).format(formatter), LocalDate.now().plusDays(2).format(formatter), "野菜類"));
    }
    
    //賞味期限の近い食材を取得
	public List<Ingredient> getExpiringIngs(int days) {
        List<Ingredient> ings = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(
            	    "SELECT * FROM ingredients " +
            	    "WHERE julianday(replace(edate, '/', '-')) - julianday('now') <= ?"
            	);
            ps.setInt(1, days);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	int id = rs.getInt("id");
            	String fname = rs.getString("fname");
                String pname = rs.getString("pname");
                String iname = rs.getString("iname");
                String pdate = rs.getString("pdate");
                String edate = rs.getString("edate");
                String category = rs.getString("category");
                
                Ingredient ing = new Ingredient(fname, pname, iname, pdate, edate, category);
                ing.setId(id);
                
                ings.add(ing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ings;
	}
}