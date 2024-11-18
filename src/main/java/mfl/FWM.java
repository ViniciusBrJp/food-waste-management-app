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

    public FWM() {}
    
    //Ingredient追加
    public void addIng(Ingredient ing) {
        try {
        	
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ingredients (name, pdate, edate) VALUES (?, ?, ?)");
            ps.setString(1, ing.getName());
            ps.setString(2, ing.getPdate());
            ps.setString(3, ing.getEdate());
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
            PreparedStatement ps = conn.prepareStatement("SELECT name, pdate, edate FROM ingredients");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String pdate = rs.getString("pdate");
                String edate = rs.getString("edate");
                ings.add(new Ingredient(name, pdate, edate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ings;
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
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	
        addIng(new Ingredient("人参", LocalDate.now().plusDays(0).format(formatter), LocalDate.now().plusDays(5).format(formatter)));
        addIng(new Ingredient("大根", LocalDate.now().plusDays(-2).format(formatter), LocalDate.now().plusDays(3).format(formatter)));
        addIng(new Ingredient("ピーマン", LocalDate.now().plusDays(-3).format(formatter), LocalDate.now().plusDays(2).format(formatter)));
    }
    
    //賞味期限の近い食材を取得
	public List<Ingredient> getExpiringIngs(int days) {
        List<Ingredient> ings = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT name, pdate, edate FROM ingredients" +
            										     " WHERE julianday(edate) - julianday('now') <= " + days);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String pdate = rs.getString("pdate");
                String edate = rs.getString("edate");
                ings.add(new Ingredient(name, pdate, edate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ings;
	}
    
    /*
    public String query(ArrayList<Ingredient> queries) {
        System.out.println("*** Query ***");
        for (Link q : queries) {
            System.out.println(q.toString());
        }

        List<List<Map<String, String>>> bindingsList = new ArrayList<>();
        for (Link query : queries) {
            List<Map<String, String>> bindings = queryLink(query);
            if (bindings.isEmpty()) {
                // クエリが失敗した場合
                return "クエリ結果がありません";
            } else {
                bindingsList.add(bindings);
            }
        }

        List<Map<String, String>> results = join(bindingsList);

        if (results.isEmpty()) {
            return "クエリ結果がありません";
        }

        StringBuilder sb = new StringBuilder();
        for (Map<String, String> result : results) {
            sb.append(result.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<Map<String, String>> queryLink(Link theQuery) {
        List<Map<String, String>> bindingsList = new ArrayList<>();
        List<Link> links = getLinks();
        for (Link link : links) {
            Map<String, String> bindings = new HashMap<>();
            if (new Matcher().matching(theQuery.getFullName(), link.getFullName(), bindings)) {
                bindingsList.add(bindings);
            }
        }
        return bindingsList;
    }

    public List<Map<String, String>> join(List<List<Map<String, String>>> bindingsList) {
        int size = bindingsList.size();
        switch (size) {
            case 0:
                // バインディングがない場合
                return new ArrayList<>();
            case 1:
                return bindingsList.get(0);
            case 2:
                return joinBindings(bindingsList.get(0), bindingsList.get(1));
            default:
                List<Map<String, String>> first = bindingsList.get(0);
                bindingsList.remove(0);
                List<Map<String, String>> rest = join(bindingsList);
                return joinBindings(first, rest);
        }
    }

    public List<Map<String, String>> joinBindings(List<Map<String, String>> bindings1, List<Map<String, String>> bindings2) {
        List<Map<String, String>> resultBindings = new ArrayList<>();
        for (Map<String, String> b1 : bindings1) {
            for (Map<String, String> b2 : bindings2) {
                Map<String, String> merged = joinBinding(b1, b2);
                if (merged != null) {
                    resultBindings.add(merged);
                }
            }
        }
        return resultBindings;
    }

    public Map<String, String> joinBinding(Map<String, String> b1, Map<String, String> b2) {
        Map<String, String> result = new HashMap<>(b1);
        for (String key : b2.keySet()) {
            String value1 = result.get(key);
            String value2 = b2.get(key);
            if (value1 != null && !value1.equals(value2)) {
                // 競合がある場合、失敗
                return null;
            } else {
                result.put(key, value2);
            }
        }
        return result;
    }
    */
    
    public void printings() {
        System.out.println("*** Ingredients ***");
        List<Ingredient> links = getIngs();
        for (int i = 0; i < links.size(); i++) {
                System.out.println(((Ingredient) links.get(i)).toString());
        }
    }
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
        //System.out.println(token1+"<->"+token2);
        if (token1.equals(token2))
                return true;
        if (var(token1) && !var(token2))
                return varMatching(token1, token2);
        if (!var(token1) && var(token2))
                return varMatching(token2, token1);
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