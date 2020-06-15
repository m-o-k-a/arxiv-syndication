package app.arxivorg.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

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

    @Test
    void getUrlLink() {
        assertEquals(urlLink, article.getUrlLink());
    }

    @Test
    void getPublished() {
        assertEquals(published, article.getPublished());
    }

    @Test
    void getTitle() {
        assertEquals(title, article.getTitle());
    }

    @Test
    void getSummary() {
        assertEquals(summary, article.getSummary());
    }

    @Test
    void getAuthors() {
        assertEquals(authors, article.getAuthors());
    }

    @Test
    void getComment() {
        assertEquals(comment, article.getComment());
    }

    @Test
    void getPdfLink() {
        assertEquals(pdfLink, article.getPdfLink());
    }

    @Test
    void getCategories() {
        assertEquals(categories, article.getCategories());
    }

    @Test
    void getPrincipaleCategory() {
        assertEquals(catPrincipale, article.getPrincipaleCategory());
    }

    @Test
    void getUpdated() {
        assertEquals(updated, article.getUpdated());
    }

    @Test
    void testToString() {
        String toString = "title\n" +
                "Authors : Martin Dupont, Marie Martin, François Cordonnier\n" +
                "published : 01/01/2020 | updated : 02/01/2020\n" +
                "Category : catPrincipale\n" +
                "Sub-categories : [cat1, cat2]\n" +
                "\n" +
                "summary\n" +
                "\n" +
                "commentaires\n" +
                "Read it online : https://arxiv.org/abs/2004.04167 | Pdf link : https://arxiv.org/pdf/2004.04167.pdf";
    }

    @Test
    void containsAuthor() {
        assertTrue(article.containsAuthor("Martin Dupont"));
    }

    @Test
    void testEquals() {
        List<String> authors = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        authors.add("Eddy Ikhlef"); authors.add("Eunsun Yun");
        cat.add("cs.AI");
        Authors author = new Authors(authors);
        Article article1 = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author, "", "", cat, "cs.AI");

        List<String> authors2 = new ArrayList<>();
        ArrayList<String> cat2 = new ArrayList<>();
        authors2.add("Eddy Ikhlef");
        cat2.add("cs.AI");
        Authors author2 = new Authors(authors2);
        Article article2 = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author2, "", "", cat2, "cs.AI");

        List<String> authors3 = new ArrayList<>();
        ArrayList<String> cat3 = new ArrayList<>();
        authors3.add("Eddy Ikhlef"); authors3.add("Eunsun Yun");
        cat3.add("cs.AI");
        Authors author3 = new Authors(authors3);
        Article article3 = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author3, "", "", cat3, "cs.AI");

        assertTrue(article1.equals(article1));
        assertTrue(article1.equals(article3));
        assertFalse(article1.equals(article2));
    }
}