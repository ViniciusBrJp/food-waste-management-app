package mfl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
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
        // SQLiteデータベースの初期化
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
        Javalin app = Javalin.create().start(50103);
        
        // uploadDirにファイルを保存するディレクトリのパスを指定
        String uploadDir = "src/main/resources/uploads/";
        
        // ディレクトリが存在しない場合は作成
        if (!Files.exists(Paths.get(uploadDir))) {
			try {
				Files.createDirectories(Paths.get(uploadDir));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
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
            
            String file_name = "noimage.png"; //ファイル名の初期値

            // uploadedFileオブジェクトにはファイル名("11305.jpg"のような"/"を含まない形)、
            //*
            var uploadedFile = ctx.uploadedFile("product-image"); // 読み込んだファイルを取得
            if (uploadedFile != null) {
                try {
                	file_name = uploadedFile.filename();

                    // 保存するファイルのパスを指定
                    File file = new File(uploadDir + file_name); // uploads/touhu.jpg
                    
					if (!file.exists()) {
						// アップロードされたファイルの内容を指定されたパスにコピー
	                    Files.copy(uploadedFile.content(), file.toPath());
					}
					
                    // ファイルのパスを出力(確認で利用)
                    //System.out.println("File uploaded to: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //*/
            
            fwm.addIng(new Ingredient(file_name, product_name, ingredient_name,
            		purchase_date, expiry_date, category));
            
            ctx.redirect("/fwm");
        });

        app.get("/registered", ctx -> {

            Map<String, Object> model = new HashMap<>();
            
            // URLパラメータからidを取得
            String id = ctx.queryParam("id");
            
            //idから食材情報を取得
            Ingredient ing = fwm.getIng(Integer.parseInt(id));

            // ingからデータを取得
            String fileName = ing.getFName();
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
            model.put("file_name", fileName);
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
        
        // 画像ファイルを提供
        app.get("/images/{filename}", ctx -> {
            String filename = ctx.pathParam("filename");
            var imagePath = Paths.get(uploadDir, filename); // 画像フォルダのパス
            String extension = getFileExtension(filename);

            if (Files.exists(imagePath)) {
                ctx.contentType("image/" + extension); // 必要に応じて適切なContent-Typeを設定 (例: "image/png")
                ctx.result(Files.newInputStream(imagePath));

                //System.out.println("画像ファイルを提供: " + imagePath);
            } else {
                ctx.status(404).result("Image not found");
            }
        });

        app.get("/search", ctx -> {
            ctx.render("/search.html");
        });

        app.post("/search", ctx -> {
            String keyword = ctx.formParam("keyword"); // フォームからキーワードを取得
            List<Ingredient> searchResults = fwm.searchByKeyword(keyword); // Matcher を用いた検索
        
            if (!searchResults.isEmpty()) {
                Map<String, Object> model = new HashMap<>();
                model.put("results", searchResults);
                ctx.sessionAttribute("searchResults", model); // 結果をセッションに保存
                ctx.redirect("/found");
            } else {
                ctx.redirect("/notfound");
            }
        });
        


        app.get("/found", ctx -> {
            Map<String, Object> model = ctx.sessionAttribute("searchResults");
            if (model != null) {
                ctx.render("/found.html", model);
            } else {
                ctx.redirect("/notfound");
            }
        });

        app.get("/notfound", ctx -> {
            ctx.render("/notfound.html");
        });

    }
    
    public static String datechange(String date) {
		if (date.contains("/")) {
			date = date.replace("/", "-");
		}
		return date;
	}
    
    // 拡張子を取得するメソッド
    private static String getFileExtension(String filename) {
        String extension = "";
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex != -1) {
            extension = filename.substring(lastIndex + 1).toLowerCase();
        }
        return extension;
    }
}

// 実行方法
// mvn exec:java -Dexec.mainClass="mfl.FoodWasteManagementApp"

