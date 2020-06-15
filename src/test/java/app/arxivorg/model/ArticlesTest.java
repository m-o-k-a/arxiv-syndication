package app.arxivorg.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ArticlesTest {
    private String urlLink = "https://arxiv.org/abs/2004.04167";
    private String published = "01/01/2020";
    private String updated = "02/01/2020";
    private String title = "title";
    private String summary = "summary";
    private Authors authors = new Authors(Arrays.asList("Martin Dupont", "Marie Martin", "François Cordonnier"));
    private String comment = "commentaires";
    private String pdfLink = "https://arxiv.org/pdf/2004.04167.pdf";
    private ArrayList<String> categories = new ArrayList<>(Arrays.asList("cat1", "cat2"));
    private String catPrincipale = "catPrincipale";
    private Article article = new Article(urlLink, published, updated, title, summary, authors, comment, pdfLink, categories, catPrincipale);
    private ArrayList<Article> articleArrayList = new ArrayList<>(Arrays.asList(article));
    private Articles articles = new Articles(articleArrayList);

    @Test
    void testToString() {
        assertEquals(article.toString() + "\n", articles.toString());
    }

    @Test
    void getArticles() {
        assertEquals(articleArrayList, articles.getArticles());
    }
}