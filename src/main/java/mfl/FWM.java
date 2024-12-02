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

    // Ingredient更新
    public void updateIng(Ingredient ing) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE ingredients SET pname = ?, iname = ?, pdate = ?, edate = ?, category = ? WHERE id = ?"
            );
            ps.setString(1, ing.getPName());
            ps.setString(2, ing.getIName());
            ps.setString(3, ing.getPdate());
            ps.setString(4, ing.getEdate());
            ps.setString(5, ing.getCategory());
            ps.setInt(6, ing.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public List<Ingredient> searchByKeyword(String keyword) {
        List<Ingredient> ings = new ArrayList<>();
        Matcher matcher = new Matcher(); // Matcher インスタンス作成
    
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
    
                // Matcher を用いた部分一致の確認
                Map<String, String> vars = new HashMap<>();
                if (matcher.matching(pname, keyword, vars) || matcher.matching(iname, keyword, vars)) {
                    ings.add(ing); // キーワードに一致した場合リストに追加
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return ings;
    }
    


/**
 * Matcherクラス
 */
class Matcher {
    StringTokenizer st1;
    StringTokenizer st2;
    Map<String, String> vars;

    Matcher() {
            vars = new HashMap<String, String>();
    }

    public boolean matching(String string1, String string2, Map<String, String> bindings) {
            this.vars = bindings;
            if (matching(string1, string2)) {
                    return true;
            } else {
                    return false;
            }
    }

    public boolean matching(String string1, String string2) {
            //System.out.println(string1);
            //System.out.println(string2);

            // 同じなら成功
            if (string1.equals(string2))
                    return true;

            // 各々トークンに分ける
            st1 = new StringTokenizer(string1);
            st2 = new StringTokenizer(string2);

            // 数が異なったら失敗
            if (st1.countTokens() != st2.countTokens())
                    return false;

            // 定数同士
            for (int i = 0; i < st1.countTokens();) {
                    if (!tokenMatching(st1.nextToken(), st2.nextToken())) {
                            // トークンが一つでもマッチングに失敗したら失敗
                            return false;
                    }
            }

            // 最後まで O.K. なら成功
            return true;
    }

    boolean tokenMatching(String token1, String token2) {
        // トークンが完全一致する場合
        if (token1.equals(token2)) {
            return true;
        }
        // 片方が変数の場合
        if (var(token1) && !var(token2)) {
            return varMatching(token1, token2);
        }
        if (!var(token1) && var(token2)) {
            return varMatching(token2, token1);
        }
        // 部分一致の場合
        if (token1.contains(token2) || token2.contains(token1)) {
            return true;
        }
        return false;
    }
    
	
	boolean varMatching(String vartoken, String token) {
	        if (vars.containsKey(vartoken)) {
	                if (token.equals(vars.get(vartoken))) {
	                        return true;
	                } else {
	                        return false;
	                }
	        } else {
	                vars.put(vartoken, token);
	        }
	        return true;
	}
	
	boolean var(String str1) {
	        // 先頭が ? なら変数
	        return str1.startsWith("?");
	}      
}
}