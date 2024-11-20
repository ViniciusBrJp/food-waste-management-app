package mfl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ingredient {
	String name;
	String purchase_date;
	String expriration_date;
	boolean inheritance;

	public Ingredient(String name, String purchase_date,
			String expriration_date) {
		
		if (purchase_date.contains("-")) {
			purchase_date = purchase_date.replace("-", "/");
		}
		
		if (expriration_date.contains("-")) {
			expriration_date = expriration_date.replace("-", "/");
		}
		
		this.name = name;
	    this.purchase_date = purchase_date;
	    this.expriration_date = expriration_date;
	}

	public void setInheritance(boolean value) {
		inheritance = value;
	}

	public String getName() {
		return name;
	}

	public String getPdate() {
		return purchase_date;
	}

	public String getEdate() {
		return expriration_date;
	}

	public String toString() {
		String result = "name: " + name + " Perchase:" + purchase_date + "Expriration:" + expriration_date;
		return result;
	}
}
