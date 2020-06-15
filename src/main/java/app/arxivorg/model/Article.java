package app.arxivorg.model;

import java.util.ArrayList;

public class Article {

    /*
        ATTRIBUTES
    */

    private String urlLink;
    private String published;
    private String updated;
    private String title;
    private String summary;
    private Authors authors;
    private String comment;
    private String pdfLink;
    private ArrayList<String> categories;
    private String principaleCategory;

    /*
        CONSTRUCTOR
    */

    public Article(String urlLink, String published,String updated, String title, String summary, Authors authors, String comment, String pdfLink, ArrayList<String> categories, String principaleCategory) {
        this.urlLink = urlLink;
        this.published = published;
        this.updated = updated;
        this.title = title;
        this.summary = summary;
        this.authors = authors;
        this.comment = comment;
        this.pdfLink = pdfLink;
        this.categories = categories;
        this.principaleCategory = principaleCategory;
    }

    /*
        GETTERS
    */

    public String getUrlLink() {
        return urlLink;
    }

    public String getPublished() {
        return published;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Authors getAuthors() {
        return authors;
    }

    public String getComment() {
        return comment;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public String getPrincipaleCategory() {
        return principaleCategory;
    }

    public String getUpdated() {
        return updated;
    }

    /*
        FUNCTIONS
    */

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.title);
        str.append("\n");
        str.append("Authors : ").append(this.authors);
        str.append("\n");
        str.append("published : ").append(this.published).append(" | updated : ").append(this.updated);
        str.append("\n");
        str.append("Category : ").append(this.principaleCategory);
        str.append("\n");
        str.append("Sub-categories : ").append(this.categories);
        str.append("\n\n");
        str.append(putLineFeed(this.summary));
        str.append("\n\n");
        str.append(this.comment);
        str.append("\n");
        str.append("Read it online : ").append(this.urlLink).append(" | Pdf link : ").append(this.pdfLink);
        str.append("\n");
        return str.toString();
    }

    private String putLineFeed(String summary){
        StringBuilder mySummary = new StringBuilder(summary);
        int j = 0;
        for (int i = 0; i < summary.length() ; i++,j++) {
            if(j>=130){
                if(summary.charAt(i) == ' '){
                    mySummary.setCharAt(i,'\n');
                    j = 0;
                }
            }
        }
        return mySummary.toString();
    }
    public boolean containsAuthor(String author){
        return authors.contains(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        if(article.getAuthors().getData().size() != this.getAuthors().getData().size()) return false;
        for(int i = 0; i<this.getAuthors().getData().size(); i++) {
            if(!article.getAuthors().getData().get(i).equals(this.getAuthors().getData().get(i))) return false;
        }
        return urlLink.equals(article.urlLink) &&
                published.equals(article.published) &&
                updated.equals(article.updated) &&
                title.equals(article.title) &&
                summary.equals(article.summary) &&
                comment.equals(article.comment) &&
                pdfLink.equals(article.pdfLink) &&
                principaleCategory.equals(article.principaleCategory);
    }
}
