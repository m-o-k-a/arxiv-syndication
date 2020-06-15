package app.arxivorg.model;

import java.util.ArrayList;

public class Articles {

    /*
        ATTRIBUTES
    */

    private ArrayList<Article> articles;

    /*
        CONSTRUCTOR
    */

    public Articles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        StringBuilder str= new StringBuilder();
        for (Article a:articles) {
            str.append(a).append("\n");
        }
        return str.toString();
    }

    /*
        GETTERS
    */

    public ArrayList<Article> getArticles() {
        return articles;
    }
}
