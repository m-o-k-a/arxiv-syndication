package app.arxivorg.model;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class QueryBuilder {

    private String query = "http://export.arxiv.org/api/query?search_query=";
    private String start = "";
    private String max_results = "";
    private int argunmentNumber = 0;

    public QueryBuilder() {}

    // you must give the category at the API format
    public void addCategory(String category) {
        addAnd();
        query += "cat:" + category;
        argunmentNumber++;
    }

    public void addKeyword(String keyword) {
        if(!keyword.isEmpty()) {
            addAnd();
            query += "all:" + keyword;
            argunmentNumber++;
        }
    }

    public void all() {
        addAnd();
        query += "all";
        argunmentNumber++;
    }

    public void addStart(String start) {
        this.start = "&start=" + start;
    }

    public void addMaxResults(String max_results) {
        this.max_results = "&max_results=" + max_results;
    }

    public void addAuthor(String author) {
        if(!author.isEmpty()) {
            addAnd();
            query += "au:" + author;
            argunmentNumber++;
        }
    }

    public void addTitle(String title) {
        addAnd();
        query += "ti:" + title;
        argunmentNumber++;
    }

    private void addAnd() {
        if (argunmentNumber > 0) {
            query += "+AND+";
        }
    }

    public String proceed() {
        query += start + max_results + "&sortBy=submittedDate&sortOrder=descending";
//        System.out.println(query);
        try (Scanner scanner = new Scanner(new URL(this.query).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "not found";
    }

    public String getQuery() {
        return query;
    }

    public void reset() {
        query = "http://export.arxiv.org/api/query?search_query=";
        argunmentNumber = 0;
        start = "";
        max_results = "";
    }
}
