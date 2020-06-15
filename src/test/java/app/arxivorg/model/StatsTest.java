package app.arxivorg.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    @Test
    void amountByCategoriesMap() {
        List<String> authors = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        authors.add("Eddy Ikhlef"); authors.add("Eunsun Yun");
        cat.add("cs.AI");
        Authors author = new Authors(authors);
        Article article1 = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author, "", "", cat, "cs.AI");

        List<String> authors2 = new ArrayList<>();
        ArrayList<String> cat2 = new ArrayList<>();
        authors2.add("Eunsun Yun");
        cat2.add("math.AG");
        Authors author2 = new Authors(authors2);
        Article article2 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Geometry of panda",
                "geometry of a panda, that's it", author2, "", "", cat2, "math.AG");

        List<String> authors3 = new ArrayList<>();
        ArrayList<String> cat3 = new ArrayList<>();
        authors3.add("Eddy Ikhlef"); authors3.add("Shuman Tang"); authors3.add("Eunsun Yun");
        cat2.add("q-bio.GN");
        Authors author3 = new Authors(authors3);
        Article article3 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Bamboo fiber DNA",
                "how to get DNA sequence of bamboo from their fibers", author3, "", "", cat3, "q-bio.GN");
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article1); articles.add(article2); articles.add(article3);
        Stats stats = new Stats(articles);
        HashMap<String, Integer> result = stats.amountByCategoriesMap();
        Iterator iterator = result.keySet().iterator();
        assertTrue(result.get(iterator.next()) == 1);
        assertTrue(result.get(iterator.next()) == 1);
        assertTrue(result.get(iterator.next()) == 1);
    }

    @Test
    void amountByDateMap() {
        List<String> authors = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        authors.add("Eddy Ikhlef"); authors.add("Eunsun Yun");
        cat.add("cs.AI");
        Authors author = new Authors(authors);
        Article article1 = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author, "", "", cat, "cs.AI");

        List<String> authors2 = new ArrayList<>();
        ArrayList<String> cat2 = new ArrayList<>();
        authors2.add("Eunsun Yun");
        cat2.add("math.AG");
        Authors author2 = new Authors(authors2);
        Article article2 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Geometry of panda",
                "geometry of a panda, that's it", author2, "", "", cat2, "math.AG");

        List<String> authors3 = new ArrayList<>();
        ArrayList<String> cat3 = new ArrayList<>();
        authors3.add("Eddy Ikhlef"); authors3.add("Shuman Tang"); authors3.add("Eunsun Yun");
        cat2.add("q-bio.GN");
        Authors author3 = new Authors(authors3);
        Article article3 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Bamboo fiber DNA",
                "how to get DNA sequence of bamboo from their fibers", author3, "", "", cat3, "q-bio.GN");
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article1); articles.add(article2); articles.add(article3);
        Stats stats = new Stats(articles);
        HashMap<String, Integer> result = stats.amountByDateMap();
        Iterator iterator = result.keySet().iterator();
        assertTrue(result.get(iterator.next()) == 2);
        assertTrue(result.get(iterator.next()) == 1);
    }

    @Test
    void amountByAuthorMap() {
        List<String> authors = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        authors.add("Eddy Ikhlef"); authors.add("Eunsun Yun");
        cat.add("cs.AI");
        Authors author = new Authors(authors);
        Article article1 = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author, "", "", cat, "cs.AI");

        List<String> authors2 = new ArrayList<>();
        ArrayList<String> cat2 = new ArrayList<>();
        authors2.add("Eunsun Yun");
        cat2.add("math.AG");
        Authors author2 = new Authors(authors2);
        Article article2 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Geometry of panda",
                "geometry of a panda, that's it", author2, "", "", cat2, "math.AG");

        List<String> authors3 = new ArrayList<>();
        ArrayList<String> cat3 = new ArrayList<>();
        authors3.add("Eddy Ikhlef"); authors3.add("Shuman Tang"); authors3.add("Eunsun Yun");
        cat2.add("q-bio.GN");
        Authors author3 = new Authors(authors3);
        Article article3 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Bamboo fiber DNA",
                "how to get DNA sequence of bamboo from their fibers", author3, "", "", cat3, "q-bio.GN");
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article1); articles.add(article2); articles.add(article3);
        Stats stats = new Stats(articles);
        HashMap<String, Integer> result = stats.amountByAuthorMap();
        Iterator iterator = result.keySet().iterator();
        assertTrue(result.get(iterator.next()) == 1);
        assertTrue(result.get(iterator.next()) == 3);
        assertTrue(result.get(iterator.next()) == 2);
    }

    @Test
    void mostPublishedAuthor() {
        List<String> authors = new ArrayList<>();
        ArrayList<String> cat = new ArrayList<>();
        authors.add("Eddy Ikhlef"); authors.add("Eunsun Yun");
        cat.add("cs.AI");
        Authors author = new Authors(authors);
        Article article1 = new Article("url", "2020-08-17T14:05:46Z",  "2020-08-17T14:05:46Z","Panda AI",
                "AI that simulate panda behaviour", author, "", "", cat, "cs.AI");

        List<String> authors2 = new ArrayList<>();
        ArrayList<String> cat2 = new ArrayList<>();
        authors2.add("Eunsun Yun");
        cat2.add("math.AG");
        Authors author2 = new Authors(authors2);
        Article article2 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Geometry of panda",
                "geometry of a panda, that's it", author2, "", "", cat2, "math.AG");

        List<String> authors3 = new ArrayList<>();
        ArrayList<String> cat3 = new ArrayList<>();
        authors3.add("Eddy Ikhlef"); authors3.add("Shuman Tang"); authors3.add("Eunsun Yun");
        cat2.add("q-bio.GN");
        Authors author3 = new Authors(authors3);
        Article article3 = new Article("url", "2020-08-18T14:05:46Z",  "2020-08-18T14:05:46Z","Bamboo fiber DNA",
                "how to get DNA sequence of bamboo from their fibers", author3, "", "", cat3, "q-bio.GN");
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(article1); articles.add(article2); articles.add(article3);
        Stats stats = new Stats(articles);
        String result = stats.mostPublishedAuthor(3);
        assertTrue(result.substring(0, 14).equals("Eunsun Yun (3)"));

    }
}