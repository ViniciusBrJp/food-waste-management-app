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
            String product_name = ctx.formParam("product-name");   // 食材名
            String ingredient_name = ctx.formParam("ingredient-name"); // 購入日
            String purchase_date = ctx.formParam("purchase-date"); // 賞味期限
            String expiry_date = ctx.formParam("expiry-date"); // 賞味期限
            String category = ctx.formParam("category"); // 賞味期限

            // 画像の取得の方法がわからない
            
            fwm.addIng(new Ingredient(product_name, purchase_date, expiry_date));
            
            ctx.redirect("/fwm");
        });

        app.get("/registered", ctx -> {
            
            Map<String, Object> model = new HashMap<>();

            // registered.html動作確認用(画像はなし)
            //*
            model.put("product_name", "サンプル食品");
            model.put("ingredient_name", "サンプル食材");;
            model.put("purchase_date", "2024-11-11");
            model.put("expiry_date", "2024-12-11");
            model.put("category", "穀物");
            //*/

            
            ctx.render("/registered.html", model);
        });

    }
}


