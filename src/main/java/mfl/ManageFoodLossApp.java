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
public class ManageFoodLossApp {
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

        app.get("/mfl", ctx -> {
            Map<String, Object> model = new HashMap<>();
            MFL mfl = new MFL();
            if (mfl.isEmpty()) {
                mfl.addInitialIngs();
            }
            
            model.put("mfl", mfl);
            
            ctx.render("/mfl.html", model);
        });
    }
}


