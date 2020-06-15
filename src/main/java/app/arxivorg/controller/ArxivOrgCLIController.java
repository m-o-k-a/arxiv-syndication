package app.arxivorg.controller;

import app.arxivorg.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class ArxivOrgCLIController {
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ArrayList<Article> articleList = new ArrayList<>();
    private final String ZONEID = ZoneId.systemDefault().toString();
    private Instant minDate = Instant.MIN;
    private Instant maxDate = Instant.MAX;
    private UserDataManager userDataManager = new UserDataManager();
    private int maxNumber = userDataManager.getPrefMaxArticle();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> authors = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> keyWords = new ArrayList<>();
    private ArrayList<Article> selectedArticles = new ArrayList<>();
    private final String PAS = "10";
    private String[] allCat = Category.allCat;

    public ArxivOrgCLIController() {

    }

    public int readCommand(String command) {
        String[] commandArgument = command.split(" ");
        switch (commandArgument[0]) {
            case "read":
                commandRead(commandArgument);
                break;
            case "list":
                commandList(commandArgument);
                break;
            case "download":
                commandDownload(commandArgument);
                break;
            case "preferences":
                commandPreferences(commandArgument);
                break;
            case "categories":
                printCategories();
                break;
            case "help":
                printHelp();
                break;
            case "select":
                commandSelect(commandArgument);
                break;
            case "fav":
                commandFav(commandArgument);
                break;
            case "clear":
                commandClear(commandArgument);
                break;
            case "stat":
                commandStat(commandArgument);
                break;
            case "exit":
                return -1;
            default:
                printError(command);
                break;
        }
        return 0;

    }

    private void printCategories() {
        for (String cat : allCat) {
            System.out.println(cat);
        }
    }

    private void resetQueryBuilder() {
        queryBuilder.reset();
        minDate = Instant.MIN;
        maxDate = Instant.MAX;
        maxNumber = userDataManager.getPrefMaxArticle();
        titles.clear();
        authors.clear();
        categories.clear();
    }

    private void constructQuery(int start) {
        queryBuilder = new QueryBuilder();
        queryBuilder.addMaxResults(PAS);
        if (categories.isEmpty() && authors.isEmpty()) {
            categories.add(allCat[userDataManager.getPrefCategoryId()]);
        }
        for (String title : titles) {
            queryBuilder.addTitle(title);
        }
        for (String cat : categories) {
            queryBuilder.addCategory(cat);
        }
        for (String author : authors) {
            queryBuilder.addAuthor(author);
        }
        for (String keyWord : keyWords) {
            queryBuilder.addKeyword(keyWord);
        }
        queryBuilder.addStart(Integer.toString(start));
    }

    private boolean setArticleList() {
        ArrayList<Article> result;
        System.out.println("searching");
        articleList = new ArrayList<>();
        int start = 0;
        while (articleList.size() < maxNumber) {
            constructQuery(start);
            result = new ArrayList<>(new AtomReader(queryBuilder.proceed()).getArticles().getArticles());
            if (!result.isEmpty() && DateParser.getDate(result.get(result.size()-1).getPublished()).isBefore(minDate)) break;
            for (int i = result.size() - 1; i >= 0; i--) {
                if (!DateParser.isBetween(DateParser.getDate(result.get(i).getPublished()), minDate, maxDate)) {
                    result.remove(i);
                }
            }
            articleList.addAll(result);
            start += Integer.parseInt(PAS);
        }
        if (!articleList.isEmpty())
            articleList = new ArrayList<>(articleList.subList(0, Math.min(maxNumber, articleList.size())));
        if (articleList.isEmpty()) {
            System.out.println("No articles found");
            return false;
        }
        return true;
    }

    private void printArticle(int index) {
        System.out.println(articleList.get(index));
    }

    private void printArticleList() {
        if (articleList.isEmpty()) {
            System.out.println("The list is Empty");
        }
        else {
            for (int i = 0; i < articleList.size(); i++) {
                System.out.println(i + " " + articleList.get(i).getTitle());
            }
        }
    }

    private void printHelp() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/helpCLI.txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printError(String command) {
        System.out.println("Sorry, I don't understand '" + command + "'");
        System.out.println("Type \"help\" to get information");
    }

    private void downloadArticle(int index, String path) {
        path = convertPath(path);
        Article article = articleList.get(index);
        Downloader downloader = new Downloader(article.getPdfLink(), path);
        System.out.println("Article '" + article.getTitle() + "' downloaded at " + path);
    }

    private void downloadSelectedArticles(String path) {
        downloadList(selectedArticles, path);
    }

    private void downloadArticleList(String path) {
        downloadList(articleList, path);
    }

    private void downloadList(ArrayList<Article> list, String path) {
        path = convertPath(path);
        System.out.println("downloading to " + path);
        for (int i = 0; i < list.size(); i++) {
            downloadArticle(i, path);
        }
    }

    private String convertPath(String path) {
        if (path.equals("default")) {
            path = userDataManager.getPrefDownloadDestination();
        }
        return path;
    }

    private void favArticle(ArrayList<Article> list, int index) {
        userDataManager.addFavArticle(list.get(index));
        System.out.println("Article " + list.get(index).getTitle() + "add to favorite");
    }

    private void favSelectedArticles() {
        for (int i = 0; i < selectedArticles.size(); i++) {
            favArticle(selectedArticles, i);
        }
    }

    private void unFavSelectedArticles() {
        System.out.println("taille selected " + selectedArticles.size());
        for (int i = 0; i < selectedArticles.size(); i++) {
            unFavArticle(selectedArticles, i);
        }
        selectedArticles.clear();
    }

    private void unFavArticle(ArrayList<Article> list, int index) {
        Article article = list.get(index);
        userDataManager.removeFavArticle(article);
        System.out.println("Article " + article.getTitle() + " remove of favorite");
        articleList = userDataManager.getFavArticles();
    }

    private int argsParcours(int first, String[] commandArgument) {
        Instant[] instants = null;
        for (int i = first; i < commandArgument.length; i++) {
            switch (commandArgument[i]) {
                case "-c":
                    categories.add(commandArgument[++i]);
                    break;
                case "-au":
                    authors.add(commandArgument[++i]);
                    break;
                case "-ti":
                    titles.add(commandArgument[++i]);
                    break;
                case "-kw":
                    keyWords.add(commandArgument[++i]);
                    break;
                case "-m":
                    try {
                        maxNumber = Integer.parseInt(commandArgument[++i]);
                    } catch (Exception e) {
                        System.out.println("Argument -m must be a number");
                    }
                    break;
                case "-from":
                    instants = DateParser.getBasicDate(commandArgument[++i], ZONEID);
                    if (instants == null) {
                        try {
                            minDate = LocalDate.parse(commandArgument[i]).atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                        } catch (Exception e) {
                            System.out.println("Error in date format. Should be -from YYYY-MM-DD but was -from " + commandArgument[i]);
                            resetQueryBuilder();
                            return 0;
                        }
                    } else {
                        minDate = instants[0];
                    }
                    break;
                case "-to":
                    instants = DateParser.getBasicDate(commandArgument[++i], ZONEID);
                    if (instants == null) {
                        try {
                            maxDate = LocalDate.parse(commandArgument[i]).atStartOfDay(ZoneId.of(ZONEID)).toInstant();
                        } catch (Exception e) {
                            System.out.println("Error in date format. Should be -to YYYY-MM-DD but was -to " + commandArgument[i]);
                            resetQueryBuilder();
                            return 0;
                        }
                    } else {
                        maxDate = instants[1];
                    }
                    break;
                default:
                    System.out.println("Invalid argument. Type help for more information");
                    resetQueryBuilder();
                    return 0;
            }
        }
        if (minDate.isAfter(maxDate)) {
            System.out.println("-from must be a prior date at -to");
            resetQueryBuilder();
            return 0;
        }
        return 1;
    }

    private void setMaxNumber() {
        this.maxNumber = userDataManager.getPrefMaxArticle();
    }

    private void commandRead(String[] commandArgument) {
        if (commandArgument.length == 2) {
            if (!articleList.isEmpty()) {
                int arg = testId(commandArgument[1]);
                if (arg < articleList.size() && arg >= 0) printArticle(arg);
                else System.out.println("You can only read article between id 0 and " + (articleList.size() - 1));
            }
            else System.out.println("The list is empty");
        }
        else System.out.println("read needs 2 arguments");
    }

    private void commandList(String[] commandArgument) {
        if (commandArgument.length % 2 != 0 && commandArgument.length >= 3) {
            selectedArticles.clear();
            int exit = argsParcours(1, commandArgument);
            if (exit == 0) return;
            if (setArticleList()) {
                printArticleList();
            }
            resetQueryBuilder();
        } else {
            System.out.println("Invalid number of arguments. Type help for more information");
        }
    }

    private void commandDownload(String[] commandArgument) {
        if (commandArgument.length % 2 == 0 && commandArgument.length >= 4) {
            int exit = argsParcours(2, commandArgument);
            if (exit == 0) return;
            if (setArticleList()) {
                downloadArticleList(commandArgument[1]);
            }
            resetQueryBuilder();
        }
        else if (commandArgument.length == 3) {
            if (!articleList.isEmpty()) {
                int arg = testId(commandArgument[1]);
                if (arg < articleList.size() && arg >= 0) {
                     downloadArticle(Integer.parseInt(commandArgument[2]), commandArgument[1]);
                } else
                    System.out.println("You can only download article between id 0 and " + (articleList.size() - 1));
            } else System.out.println("The list is empty");
        }
        else if (commandArgument.length == 2) {
            if (selectedArticles.isEmpty()) {
                System.out.println("No articles selected");
                return;
            }
            downloadSelectedArticles(commandArgument[1]);
        }
        else System.out.println("Invalid number of arguments. Type help for more information");
    }

    private void commandPreferences(String[] commandArgument) {
        if (commandArgument.length % 2 != 0 && commandArgument.length >= 3) {
            for (int i = 1; i < commandArgument.length; i++) {
                switch (commandArgument[i]) {
                    case "-maxA":
                        try {
                            userDataManager.setPrefMaxArticle(Integer.parseInt(commandArgument[++i]));
                            setMaxNumber();
                        } catch (Exception e) {
                            System.out.println("-maxA must be a number");
                            return;
                        }
                        System.out.println("Prefered maximum number of articles set to " + userDataManager.getPrefMaxArticle());
                        break;
                    case "-c":
                        boolean found = false;
                        i++;
                        for (int catId = 0; catId < allCat.length; catId++) {
                            if (allCat[catId].equals(commandArgument[i])) {
                                userDataManager.setPrefCategoryId(catId);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println("This isn't a category. To see the category list type categories");
                            return;
                        }
                        System.out.println("Prefered categorie set to " + commandArgument[i]);
                        break;
                    case "-dl":
                        userDataManager.setPrefDownloadDestination(commandArgument[++i]);
                        System.out.println("Prefered download destination set to " + userDataManager.getPrefDownloadDestination());
                        break;
                    default:
                        System.out.println("Invalid argument. Type help for more information");
                        resetQueryBuilder();
                        return;
                }
            }
        }
        else {
            System.out.println("Invalid number of arguments. Type help for more information");
        }
    }

    private void commandSelect(String[] commandArgument) {
        selectedArticles.clear();
        if (commandArgument.length > 1) {
            if (articleList.isEmpty()) {
                System.out.println("The list is empty");
                return;
            }
            int articleId;
            for (int i = 1; i < commandArgument.length; i++) {
                try {
                    articleId = Integer.parseInt(commandArgument[i]);
                } catch (Exception e) {
                    System.out.println("Arguments must be numbers");
                    return;
                }
                if (articleId < articleList.size() && articleId >= 0) {
                    selectedArticles.add(articleList.get(articleId));
                } else {
                    System.out.println("You can only select article between id 0 and " + (articleList.size() - 1));
                    selectedArticles.clear();
                    return;
                }
            }
        } else {
            System.out.println("Invalid number of arguments. Type help for more information");
            return;
        }
        StringBuilder string = new StringBuilder("Article(s) ");
        for (int i = 1; i < commandArgument.length; i++) {
            string.append(commandArgument[i]).append(" ");
        }
        string.append("succesfuly selected");
        System.out.println(string);
    }

    private void commandClear(String[] commandArgument) {
        if (commandArgument.length == 1) {
            selectedArticles.clear();
            System.out.println("Selection cleared");
        }
        else System.out.println("clear goes alone");
    }

    private void caseAddFav(String[] commandArgument) {
        if (commandArgument.length == 3) {
            if (!articleList.isEmpty()) {
                int arg = testId(commandArgument[2]);
                if (arg < articleList.size() && arg >= 0) {
                    favArticle(articleList, arg);
                }
                else System.out.println("You can only fav article between id 0 and " + (articleList.size() - 1));
            }
            else System.out.println("The list is empty");
        }
        else if (commandArgument.length == 2) {
            if (selectedArticles.isEmpty()) {
                System.out.println("No articles selected");
            }
            else favSelectedArticles();
        }
        else System.out.println("Invalid number of arguments. Type help for more information");
    }

    private void caseSeeFav(String[] commandArgument) {
        if (commandArgument.length == 2) {
            articleList = userDataManager.getFavArticles();
            selectedArticles.clear();
            if (articleList.isEmpty()) System.out.println("You have no favorite");
            else printArticleList();
        }
        else System.out.println("Invalid number of arguments. Type help for more information");
    }

    private void caseRemoveFav(String[] commandArgument) {
        if (commandArgument.length == 3) {
            if (!articleList.isEmpty()) {
                int arg = testId(commandArgument[2]);
                System.out.println(arg);
                if (arg < articleList.size() && arg >= 0) {
                    unFavArticle(articleList, arg);
                    selectedArticles.clear();
                }
                else System.out.println("You can only unfav article between id 0 and " + (articleList.size() - 1));
            }
            else System.out.println("The list is empty");
        }
        else if (commandArgument.length == 2) {
            if (selectedArticles.isEmpty()) {
                System.out.println("No articles selected");
            }
            else unFavSelectedArticles();
        }
        else System.out.println("Invalid number of arguments. Type help for more information");
    }

    private void commandFav(String[] commandArgument) {
        if (commandArgument.length >= 2) {
            switch (commandArgument[1]) {
                case "-a":
                    caseAddFav(commandArgument);
                    break;
                case "-r":
                    caseRemoveFav(commandArgument);
                    break;
                case "-s":
                    caseSeeFav(commandArgument);
                    break;
                default:
                    System.out.println("Invalid argument. Type help for more information");
                    break;
            }
        }

    }

    private void commandStat(String[] commandArgument) {
        Stats stats = new Stats(articleList);
        if (commandArgument.length == 2) {
            switch (commandArgument[1]) {
                case "-date":
                    if (articleList.isEmpty()) System.out.println("The list is empty");
                    else System.out.println(stats.amountByDate());
                    break;
                case "-cat":
                    if (articleList.isEmpty()) System.out.println("The list is empty");
                    else System.out.println(stats.amountByCategories());
                    break;
                default:
                    System.out.println("Invalid argument. Type help for more information");
                    break;
            }
        }
        else if (commandArgument.length == 3) {
            if ("-au".equals(commandArgument[1])) {
                if (articleList.isEmpty()) System.out.println("The list is empty");
                else {
                    try {
                        int number = Integer.parseInt(commandArgument[2]);
                        if (number >= 1) System.out.println(stats.mostPublishedAuthor(number - 1));
                        else System.out.println("The third argument (number) must be a number > 0");
                    }
                    catch (Exception e) {
                        System.out.println("The third argument (number) must be a number > 0");
                    }
                }
            } else {
                System.out.println("stat -au takes 3 argument");
            }
        }
        else System.out.println("Invalid number of arguments. Type help for more information");
    }

    private int testId(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            System.out.println("the third argument (id) must be a number");
        }
        return -1;
    }
}
