package app.arxivorg.model;

import java.time.Instant;
import java.util.ArrayList;

public class ArticleManager {
    private String category;
    private boolean isTimeAdvanced;
    private String authors;
    private String keywords;
    private Instant[] basicTime;
    private Instant timefrom;
    private Instant timeTo;
    private ArrayList<Article> articles;

    /**
     * Constructor of the class ArticleManager.
     * @param category
     * @param isTimeAdvanced
     * @param authors
     * @param keywords
     * @param basicTime
     * @param timefrom
     * @param timeTo
     * @param articles
     */
    public ArticleManager(String category, boolean isTimeAdvanced, String authors, String keywords, Instant[] basicTime, Instant timefrom, Instant timeTo, ArrayList<Article> articles) {
        this.category = category;
        this.isTimeAdvanced = isTimeAdvanced;
        this.authors = authors;
        this.keywords = keywords;
        this.basicTime = basicTime;
        this.timefrom = timefrom;
        this.timeTo = timeTo;
        this.articles = articles;
    }

    /**
     * The method filter apply all available filter of the ArticleManager class.
     * @return
     */
    public ArrayList<Article> filter() {
        articles = refreshCategory(articles);
        articles = refreshDate(articles);
        articles = refreshAuthors(articles);
        articles = refreshKeywords(articles);
        return articles;
    }

    /**
     * The method refreshCategory update the selected Article list with article corresponding to the
     * correct category.
     * @param articles
     * @return
     */
    public ArrayList<Article> refreshCategory(ArrayList<Article> articles) {
        ArrayList<Article> temp = new ArrayList<>(articles);
        for(Article article : articles) {
            if(!article.getPrincipaleCategory().equals(category) && !category.equals("Any")) {
                boolean iscat = false;
                for(String artCategory : article.getCategories()) {
                    if(artCategory.equals(category)) { iscat = true; break; }
                }
                if(!iscat)  temp.remove(article);
            }
        }
        return temp;
    }

    /**
     * The method refreshDate update the selected Article list with article corresponding to the
    * correct date.
     * @param articles
     * @return
     */
    public ArrayList<Article> refreshDate(ArrayList<Article> articles) {
        ArrayList<Article> temp = new ArrayList<>(articles);
        if(isTimeAdvanced) {
            Instant from = (timefrom == null) ? Instant.MIN : timefrom;
            Instant to = (timeTo == null) ? Instant.MAX : timeTo;
            for(Article article : articles) {
                if(!DateParser.isBetween(DateParser.getDate(article.getPublished()), from, to)) {
                    temp.remove(article);
                }
            }
        } else if(basicTime != null) {
            for(Article article : articles) {
                if(!DateParser.isBetween(DateParser.getDate(article.getPublished()), basicTime[0], basicTime[1])) {
                    temp.remove(article);
                }
            }
        }
        return temp;
    }

    /**
     * The method refreshAuthors update the selected Article list with article corresponding to the
     * correct authors.
     * the string of specified authors have to end by , or \n.
     * @param articles
     * @return
     */
    public ArrayList<Article> refreshAuthors(ArrayList<Article> articles) {
        ArrayList<Article> temp = new ArrayList<>(articles);
        if(authors.length() > 0 && (authors.endsWith(",") || authors.endsWith("\n"))) {
            String[] authorArray = authors.split("[,\n]");
            for(Article article : articles) {
                for (int i = 0; i < authorArray.length; i++) {
                    boolean isAuthor = false;
                    for(String author : article.getAuthors().getData()) {
                        if(authorArray[i].toLowerCase().equals(author.toLowerCase())) {
                            isAuthor = true;
                            break;
                        }
                    }
                    if(!isAuthor) temp.remove(article);
                }
            }
        }
        return temp;
    }

    /**
     * The method refreshKeywords update the selected Article list with article corresponding to the
     * correct keywords.
     * the string of specified keywords have to end by , or \n.
     * @param articles
     * @return
     */
    public ArrayList<Article> refreshKeywords(ArrayList<Article> articles) {
        ArrayList<Article> temp = new ArrayList<>(articles);
        if(keywords.length() > 0 && (keywords.endsWith(",") || keywords.endsWith("\n"))) {
            String[] keywordArray = keywords.split("[,\n]");
            for(Article article : articles) {
                for (int i = 0; i < keywordArray.length; i++) {
                    boolean isKeyword = false;
                    for(String keyword : keywordArray) {
                        if(article.getSummary().toLowerCase().contains(keyword.toLowerCase()) || article.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                            isKeyword = true;
                            break;
                        }
                    }
                    if(!isKeyword) temp.remove(article);
                }
            }
        }
        return temp;
    }

    public void updateList(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
