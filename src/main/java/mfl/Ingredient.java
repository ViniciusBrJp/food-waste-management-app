package mfl;

public class Ingredient {
	int    id;  // ID
	String file_name; // ファイル名
	String product_name;  // 商品名
	String ingredient_name; // 食材名
	String purchase_date; // 購入日
	String expriration_date; 	// 賞味期限
	String category; // カテゴリ

	public Ingredient(
			String file_name,  	        // ファイル名
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
		
		this.file_name = file_name;
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
	
	public String getFName() {
		return file_name;
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
	
	public String toString() {
		return product_name + " " + ingredient_name + " " + purchase_date + " " + expriration_date + " " + category;
	}
}
