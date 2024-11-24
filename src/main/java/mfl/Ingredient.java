package mfl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ingredient {
	int    id;  // ID
	String product_name;  // 商品名
	String ingredient_name; // 食材名
	String purchase_date; // 購入日
	String expriration_date; 	// 賞味期限
	String category; // カテゴリ

	public Ingredient(
			String product_name,        // 商品名
			String ingredient_name,     // 食材名
			String purchase_date,       // 購入日
			String expriration_date,	// 賞味期限
			String category             // カテゴリ
			) {
		
		if (purchase_date.contains("-")) {
			purchase_date = purchase_date.replace("-", "/");
		}
		
		if (expriration_date.contains("-")) {
			expriration_date = expriration_date.replace("-", "/");
		}
		
		this.product_name = product_name;
		this.ingredient_name = ingredient_name;
	    this.purchase_date = purchase_date;
	    this.expriration_date = expriration_date;
	    this.category = category;
	}
	
	public void setId(int id) {
        this.id = id;
    }
	
	public int getId() {
		return id;
	}

	public String getPName() {
		return product_name;
	}
	
	public String getIName() {
		return ingredient_name;
	}

	public String getPdate() {
		return purchase_date;
	}

	public String getEdate() {
		return expriration_date;
	}
	
	public String getCategory() {
		return category;
	}
}
