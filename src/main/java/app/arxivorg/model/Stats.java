package app.arxivorg.model;

import java.util.*;

public class Stats {
    private List<Article> articles;

    /**
     * Constructor of the class Stats. Take as parameters an array of Article.
     * @param articles
     */
    public Stats(List<Article> articles) { this.articles = articles; }

    /**
     * Generate the statistics of the amount of articles by categories in the previous query.
     * @return formatted string of amount of articles by categories
     */
    public String amountByCategories() {
        HashMap<String, Integer> stats = amountByCategoriesMap();
        String result = "";
        Iterator iterate = stats.entrySet().iterator();
        while(iterate.hasNext()) {
            Map.Entry<String, Integer> category = (Map.Entry<String, Integer>) iterate.next();
            result += category.getKey()+": "+category.getValue()+" published articles\n";
        }
        return result;
    }

    /**
     * Generate the statistics of the amount of articles by categories in the previous query.
     * @return  hashMap of amount of articles by categories
     */
    public HashMap<String, Integer> amountByCategoriesMap() {
        HashMap<String, Integer> stats = new HashMap<>();
        for(Article article : articles) {
            if(stats.containsKey(article.getPrincipaleCategory())) {
                int amount = stats.get(article.getPrincipaleCategory());
                amount++;
                stats.replace(article.getPrincipaleCategory(), amount);
            } else {
                stats.put(article.getPrincipaleCategory(), 1);
            }
        }
        return stats;
    }

    /**
     * Generate the statistics of the amount of articles by date in the previous query.
     * @return formatted string of amount of articles by date
     */
    public String amountByDate() {
        HashMap<String, Integer> stats = amountByDateMap();
        String result = "";
        Iterator iterate = stats.entrySet().iterator();
        while(iterate.hasNext()) {
            Map.Entry<String, Integer> date = (Map.Entry<String, Integer>) iterate.next();
            result += date.getKey()+": "+date.getValue()+" published articles\n";
        }
        return result;
    }

    /**
     * Generate the statistics of the amount of articles by date in the previous query.
     * @return Hashmap of amount of articles by date
     */
    public HashMap<String, Integer> amountByDateMap() {
        HashMap<String, Integer> stats = new HashMap<>();
        for(Article article : articles) {
            String date = DateParser.getDate(article.getPublished()).toString().substring(0, 10);
            if (stats.containsKey(date)) {
                int amount = stats.get(date);
                amount++;
                stats.replace(date, amount);
            } else {
                stats.put(date, 1);
            }
        }
        return stats;
    }

    /**
     * Generate the statistics of the amount of articles by authors in the previous query.
     * @return HashMap of amount of articles by authors
     */
    public HashMap<String, Integer> amountByAuthorMap() {
        HashMap<String, Integer> stats = new HashMap<>();
        for(Article article : articles) {
            Authors authors = article.getAuthors();
            for(String author : authors.getData()) {
                if(author.isEmpty()) continue;
                if(stats.containsKey(author)) {
                    int amount = stats.get(author);
                    amount++;
                    stats.replace(author, amount);
                } else {
                    stats.put(author, 1);
                }
            }
        }
        return stats;
    }

    /**
     * Generate the statistics of the amount of the (max) most published author.
     * @return formatted string of the most published authors (in range 0..max)
     */
    public String mostPublishedAuthor(int max) {
        max++;
        TreeMap<String, Integer> amount = sortByValue(amountByAuthorMap());
        if(amount.keySet().size() < max) max = amount.keySet().size();
        HashMap<String, Integer> stats = new HashMap<>();
        for(int i = 0; i<max; i++) {
            Map.Entry<String, Integer> author = amount.pollFirstEntry();
            stats.put(author.getKey(), author.getValue());
        }
        TreeMap<String, Integer> sortedStats = sortByValue(stats);
        String result = "";;
        while(!sortedStats.isEmpty()) {
            Map.Entry<String, Integer> author = sortedStats.pollFirstEntry();
            result += author.getKey()+" ("+author.getValue()+")\n";
        }
        return result;
    }

    private TreeMap<String, Integer> sortByValue(HashMap<String, Integer> map){
        Comparator<String> comparator = new StatsComparator(map);
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }

}
