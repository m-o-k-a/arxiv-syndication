package app.arxivorg.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class UserDataManager {

    /*
        ATTRIBUTES
     */
    private int prefMaxArticle = 100;
    private int prefCategoryId = 0;
    private int prefTimeId = 0;
    private String prefDownloadDestination = "./Downloads";
    private ArrayList<Article> favArticles = new ArrayList<>();
    JSONArray favArticlesJson = new JSONArray();

    /*
        GETTER
     */
    public int getPrefMaxArticle() { return prefMaxArticle; }

    public int getPrefCategoryId() { return prefCategoryId; }

    public int getPrefTimeId() { return prefTimeId; }

    public String getPrefDownloadDestination() { return prefDownloadDestination; }

    public ArrayList<Article> getFavArticles() { return favArticles; }

    /*
        SETTER
     */
    public void setAll(String maxArticle, int categoryId, int timeId, String downloadDest) {
        setPrefMaxArticle(Integer.parseInt(maxArticle));
        setPrefCategoryId(categoryId);
        setPrefTimeId(timeId);
        setPrefDownloadDestination(downloadDest);
    }

    public void setPrefMaxArticle(int prefMaxArticle) { this.prefMaxArticle = Math.abs(prefMaxArticle); update(); }

    public void setPrefCategoryId(int prefCategoryId) { this.prefCategoryId = prefCategoryId; update(); }

    public void setPrefTimeId(int prefTimeId) { this.prefTimeId = prefTimeId; update();}

    public void setPrefDownloadDestination(String prefDownloadDestination) { this.prefDownloadDestination = prefDownloadDestination; update(); }

    public void addFavArticle(Article article) {
        for(Article fav : favArticles) {
            if (fav.equals(article)) return;
        }
        favArticles.add(article);
        update();
    }

    public void removeFavArticle(Article article) {
        if(favArticles.remove(article)) update();
    }

    /**
     * Constructor of the class UserDataManager. if the preference file exist (user.data)
     * then read its content (to update the attributes of the object with the saved ones).
     * else create (update) this file with the basics preferences.
     */
    public UserDataManager() {
        File dataFile = new File("./user.data");
        if(dataFile.exists()) {
            read();
        } else {
            update();
        }
    }

    private void read() {
        JSONParser parser = new JSONParser();
        new File(getPrefDownloadDestination()).mkdir();
        try {
            Object data = parser.parse(new FileReader("./user.data"));
            JSONObject jsonObject = (JSONObject) data;
            prefMaxArticle = Math.abs(Integer.parseInt(jsonObject.get("prefMaxArticle").toString()));
            prefCategoryId = Math.abs(Integer.parseInt(jsonObject.get("prefCategoryId").toString()));
            prefTimeId = Math.abs(Integer.parseInt(jsonObject.get("prefTimeId").toString()));
            prefDownloadDestination = jsonObject.get("prefDownloadDestination").toString();
            JSONArray fav = (JSONArray) jsonObject.get("favArticles");
            if (fav != null || fav.size() == 0) {
                for(Object article : fav) {
                    boolean isExist = false;
                    for(Article existArticle : favArticles) {
                        if(existArticle.equals(ArticleToJson.convertToArticle((JSONObject) article))) isExist = true;
                    }
                    if(!isExist) favArticles.add(ArticleToJson.convertToArticle((JSONObject) article));
                }
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
        catch (ParseException e) { update(); read(); }
    }

    private void update() {
        JSONObject data = new JSONObject();
        data.put("prefMaxArticle", prefMaxArticle);
        data.put("prefCategoryId", prefCategoryId);
        data.put("prefTimeId", prefTimeId);
        data.put("prefDownloadDestination", prefDownloadDestination);
        favArticlesJson = new JSONArray();
        for(Article article : favArticles) { favArticlesJson.add(ArticleToJson.convertToJson(article)); }
        data.put("favArticles", favArticlesJson);
        new File(getPrefDownloadDestination()).mkdir();
        try (FileWriter file = new FileWriter("./user.data")) {
            file.write(data.toJSONString());
            file.flush();
        } catch (IOException e) { e.printStackTrace(); }
    }

}
