package app.arxivorg.model;

import javafx.scene.control.DatePicker;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArticleManagerTest {

    private final String ZONEID = "America/New_York";
    String category = "cs.AI";
    boolean isTimeAdvanced = false;
    Instant[] basicTime = DateParser.getBasicDate("yesterday", ZONEID);
    Instant timefrom = LocalDate.now().minusDays(2).atStartOfDay(ZoneId.of(ZONEID)).toInstant();
    Instant timeTo = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.of(ZONEID)).toInstant();

    ArrayList<Article> articles;
    ArticleManager articleManager;

    @Test
    void refreshCategory() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.all(); queryBuilder.addMaxResults("50");
        articles = new AtomReader(queryBuilder.proceed()).getArticles().getArticles();

        articleManager = new ArticleManager(category, isTimeAdvanced, "", "", basicTime, timefrom, timeTo, articles);
        ArrayList<Article> refresh = articleManager.refreshCategory(articles);
        for(Article article : refresh) {
            boolean isCat = false;
            if(article.getPrincipaleCategory().toLowerCase().equals(category.toLowerCase())) isCat = true;
            else {
                for(String cat : article.getCategories()) { if(cat.toLowerCase().equals(category.toLowerCase())) isCat = true; break; }
            }
            assertTrue(isCat);
        }
    }

    @Test
    void refreshDate() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.all(); queryBuilder.addMaxResults("120");
        articles = new AtomReader(queryBuilder.proceed()).getArticles().getArticles();

        articleManager = new ArticleManager(category, isTimeAdvanced, "", "", basicTime, timefrom, timeTo, articles);
        ArrayList<Article> refresh = articleManager.refreshDate(articles);
        for(Article article : refresh) {
            assertTrue(DateParser.isBetween(DateParser.getDate(article.getPublished()), basicTime[0], basicTime[1]));
        }
        articleManager = new ArticleManager(category, true, "", "", basicTime, timefrom, timeTo, articles);
        refresh = articleManager.refreshDate(articles);
        for(Article article : refresh) {
            assertTrue(DateParser.isBetween(DateParser.getDate(article.getPublished()), timefrom, timeTo));
        }
    }

    @Test
    void refreshAuthors() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.all(); queryBuilder.addMaxResults("15");
        articles = new AtomReader(queryBuilder.proceed()).getArticles().getArticles();
        String authors = "Han Fu,\n";
        articleManager = new ArticleManager(category, isTimeAdvanced, authors, "", basicTime, timefrom, timeTo, articles);
        ArrayList<Article> refresh = articleManager.refreshAuthors(articles);
        for(Article article : refresh) {
            boolean isAuthor = false;
            for(String author : article.getAuthors().getData()) {
                if(author.toLowerCase().equals(authors.toLowerCase())) { isAuthor = true; break; }
            }
            assertTrue(isAuthor);
        }
    }

    @Test
    void refreshKeywords() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.all(); queryBuilder.addMaxResults("33");
        articles = new AtomReader(queryBuilder.proceed()).getArticles().getArticles();
        String keywords = "learning,";
        articleManager = new ArticleManager(category, isTimeAdvanced, "", keywords, basicTime, timefrom, timeTo, articles);
        ArrayList<Article> refresh = articleManager.refreshKeywords(articles);
        for(Article article : refresh) {
            boolean isKeyword = false;
            for(Article keyword : articles) {
                String keywordGood = keywords.toLowerCase().substring(0, keywords.length() - 1);
                if(keyword.getTitle().toLowerCase().contains(keywordGood) || keyword.getSummary().toLowerCase().contains(keywordGood)) {
                    isKeyword = true;
                    break;
                }
            }
            assertTrue(isKeyword);
        }
    }

    @Test
    void filter() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.all(); queryBuilder.addMaxResults("222");
        articles = new AtomReader(queryBuilder.proceed()).getArticles().getArticles();
        String keywords = "the\n";
        String authors = articles.get(0).getAuthors().getData().get(0);

        articleManager = new ArticleManager(category, isTimeAdvanced, authors, keywords, basicTime, timefrom, timeTo, articles);
        ArrayList<Article> refresh = articleManager.filter();
        for(Article article : refresh) {
            boolean isCat = false;
            if(article.getPrincipaleCategory().toLowerCase().equals(category.toLowerCase())) isCat = true;
            else {
                for(String cat : article.getCategories()) { if(cat.toLowerCase().equals(category.toLowerCase())) isCat = true; break;}
            }
            assertTrue(isCat);
            assertTrue(DateParser.isBetween(DateParser.getDate(article.getPublished()), timefrom, timeTo));
            boolean isAuthor = false;
            for(String author : article.getAuthors().getData()) {
                if(author.toLowerCase().equals(authors.toLowerCase())) { isAuthor = true; break; }
            }
            assertTrue(isAuthor);
            boolean isKeyword = false;
            if(article.getTitle().toLowerCase().contains(keywords.toLowerCase()) || article.getSummary().toLowerCase().contains(keywords.toLowerCase())) {
                isKeyword = true;
            }
            assertTrue(isKeyword);
        }
    }
}