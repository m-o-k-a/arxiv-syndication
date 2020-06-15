package app.arxivorg.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderTest {

    //TODO proceedTest

    @Test
    void addCategory() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addCategory("astro-ph");
        assertEquals("http://export.arxiv.org/api/query?search_query=cat:astro-ph", queryBuilder.getQuery());
    }

    @Test
    void addAuthor() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addAuthor("jean");
        assertEquals("http://export.arxiv.org/api/query?search_query=au:jean", queryBuilder.getQuery());
    }

    @Test
    void addTitle() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addTitle("star");
        assertEquals("http://export.arxiv.org/api/query?search_query=ti:star", queryBuilder.getQuery());
    }

    @Test
    void addStart() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addStart("0");
        queryBuilder.proceed();
        assertEquals("http://export.arxiv.org/api/query?search_query=&start=0&sortBy=submittedDate&sortOrder=descending", queryBuilder.getQuery());
    }

    @Test
    void addMaxResults() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addMaxResults("100");
        queryBuilder.proceed();
        assertEquals("http://export.arxiv.org/api/query?search_query=&max_results=100&sortBy=submittedDate&sortOrder=descending", queryBuilder.getQuery());
    }

    @Test
    void reset() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addTitle("star");
        queryBuilder.reset();
        assertEquals("http://export.arxiv.org/api/query?search_query=", queryBuilder.getQuery());
    }

    @Test
    void all() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.all();
        assertEquals("http://export.arxiv.org/api/query?search_query=all", queryBuilder.getQuery());
    }

    @Test
    void addKeyword() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addKeyword("keyword");
        assertEquals("http://export.arxiv.org/api/query?search_query=all:keyword", queryBuilder.getQuery());
    }
}