package app.arxivorg.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class ArticleToJson {

    public static JSONObject convertToJson(Article article) {
        JSONObject json = new JSONObject();
        json.put("urlLink", article.getUrlLink());
        json.put("published", article.getPublished());
        json.put("updated", article.getUpdated());
        json.put("title", article.getTitle());
        json.put("summary", article.getSummary());

        JSONArray authors = new JSONArray();
        for(String author : article.getAuthors().getData()) { authors.add(author); }
        json.put("authors", authors);

        json.put("comment", article.getComment());
        json.put("pdfLink", article.getPdfLink());

        JSONArray categories = new JSONArray();
        for(String category : article.getCategories()) { categories.add(category); }
        json.put("categories", categories);


        json.put("principaleCategory", article.getPrincipaleCategory());
        return json;
    }

    public static Article convertToArticle(JSONObject json) {
        ArrayList<String> authors = new ArrayList<>();
        JSONArray authorsJSON = (JSONArray) json.get("authors");
        if (authorsJSON != null) {
            for(Object author : authorsJSON) {
                authors.add(author.toString());
            }
        }

        ArrayList<String> categories = new ArrayList<>();
        JSONArray categoriesJSON = (JSONArray) json.get("categories");
        if (authorsJSON != null) {
            for(Object category : categoriesJSON) {
                categories.add(category.toString());
            }
        }

        return  new Article(json.get("urlLink").toString(), json.get("published").toString(), json.get("updated").toString(),
                json.get("title").toString(), json.get("summary").toString(), new Authors(authors), json.get("comment").toString(),
                json.get("pdfLink").toString(), categories, json.get("principaleCategory").toString());
    }


}