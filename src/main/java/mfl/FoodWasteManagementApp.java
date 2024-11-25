package mfl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;

/**
 * SemNetAppクラス
 */
public class FoodWasteManagementApp {
    public static void main(String[] args) {
        // H2データベースの初期化
        Database.initializeDatabase();

        // Thymeleafのテンプレート設定
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("HTML");
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");

        // TemplateEngineの設定
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // JavalinにThymeleafを登録
        JavalinRenderer.register(new JavalinThymeleaf(templateEngine), ".html");

        // Javalinアプリの作成
        Javalin app = Javalin.create().start(50083);
        
        FWM fwm = new FWM();

        app.get("/fwm", ctx -> {
            Map<String, Object> model = new HashMap<>();
            
            if (fwm.isEmpty()) {
                fwm.addInitialIngs();
            }
            
            model.put("fwm", fwm);
            
            ctx.render("/fwm.html", model);
        });
        
        app.get("/register", ctx -> {
            ctx.render("/register.html");
        });
        
        app.post("/register", ctx -> {
            // フォームから送信された値を取得
            String product_name = ctx.formParam("product-name");   // 商品名
            String ingredient_name = ctx.formParam("ingredient-name"); // 食材名
            String purchase_date = ctx.formParam("purchase-date"); // 購入日
            String expiry_date = ctx.formParam("expiry-date"); // 賞味期限
            String category = ctx.formParam("category"); // カテゴリ

            // 画像の取得の方法がわからない
            
            fwm.addIng(new Ingredient(product_name, ingredient_name,
            		purchase_date, expiry_date, category));
            
            ctx.redirect("/fwm");
        });

        app.get("/registered", ctx -> {

            Map<String, Object> model = new HashMap<>();
            
            // URLパラメータからidを取得
            String id = ctx.queryParam("id");
            
            //idから食材情報を取得
            Ingredient ing = fwm.getIng(Integer.parseInt(id));

            // URLパラメータからデータを取得
            String productName = ing.getPName();
            String ingredientName = ing.getIName();
            String purchaseDate = ing.getPdate();
            String expiryDate = ing.getEdate();
            String category = ing.getCategory();

            // pdateは"yyyy//mm/dd"の形

            // 日付の変換 /から-へ
            purchaseDate = datechange(purchaseDate);
            expiryDate = datechange(expiryDate);

            // データをモデルに追加
            model.put("product_name", productName);
            model.put("ingredient_name", ingredientName);
            model.put("purchase_date", purchaseDate);
            model.put("expiry_date", expiryDate);
            model.put("category", category);
            
            ctx.render("/registered.html", model);
        });
        
        //削除機能
        app.get("/delete", ctx -> {
            
            // URLパラメータからidを取得
            String id = ctx.queryParam("id");
            
            System.out.println("削除: " + fwm.getIng(Integer.parseInt(id)).toString());
            
            fwm.deleteIng(Integer.parseInt(id));
        
            ctx.redirect("/fwm"); // ホームページにリダイレクト
        });
    }
    
    public static String datechange(String date) {
		if (date.contains("/")) {
			date = date.replace("/", "-");
		}
		return date;
	}
}


