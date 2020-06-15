package app.arxivorg.model;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArticleToJsonTest {

    @Test
    void convertToJson() {
        List<String> authors = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        authors.add("Eddy Ikhlef"); authors.add("Eunsun Yun");
        cat.add("cs.AI");
        Authors author = new Authors(authors);
        Article article = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author, "", "", cat, "cs.AI");

        JSONObject jsonArticle = ArticleToJson.convertToJson(article);
        assertEquals(jsonArticle.get("title"), article.getTitle());
        assertEquals(jsonArticle.get("summary"), article.getSummary());
        assertEquals(jsonArticle.get("categories"), article.getCategories());
    }

    @Test
    void convertToArticle() {
        List<String> authors = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        authors.add("Eddy Ikhlef"); authors.add("Eunsun Yun");
        cat.add("cs.AI");
        Authors author = new Authors(authors);
        Article article = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author, "", "", cat, "cs.AI");


        Article articleJson = ArticleToJson.convertToArticle(ArticleToJson.convertToJson(article));
        assertEquals(articleJson.getUpdated(), article.getUpdated());
        assertEquals(articleJson.getPdfLink(), article.getPdfLink());
        assertEquals(articleJson.getAuthors().getData(), article.getAuthors().getData());
    }
}