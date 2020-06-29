package app.arxivorg.controller;

import app.arxivorg.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.net.URL;
import java.time.*;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class ArxivOrgController implements Initializable {

    /*
        ATTRIBUTES
    */
    private QueryBuilder queryBuilder;
    private ArrayList<Article> downloadedarticles = new ArrayList<>();
    private ArrayList<Article> articles = new ArrayList<>();
    private ArrayList<Article> selectedAuthorArticles = new ArrayList<>();
    private ArrayList<CheckBox> articlesBox = new ArrayList<>();
    @FXML private Pane filterPane;
    @FXML private ListView contentPane;
    @FXML private Label label;
    @FXML private TextFlow infoText;
    @FXML private VBox timeBasic;
    @FXML private VBox timeAdvanced;
    @FXML private ComboBox categoryBox;
    @FXML private ChoiceBox timeBox;
    @FXML private DatePicker timeBoxFrom;
    @FXML private DatePicker timeBoxTo;
    @FXML private TextArea authorsBox;
    @FXML private TextArea keywordsBox;
    @FXML private HBox specialSearch;
    @FXML private CheckBox favoriteCheckBox;
    @FXML private Hyperlink downloadThisArticle;
    @FXML private Hyperlink downloadSelected;
    @FXML private VBox primaryWindow;

    private ScrollPane subMenu = new ScrollPane();
    private TextField prefMaxArticle;
    private ComboBox prefCategoryId;
    private ChoiceBox prefTimeID;
    private TextField prefDownloadDestination;

    private boolean isTimeAdvanced = false;
    private final String ZONEID = ZoneId.systemDefault().toString();
    private String maxSize;
    private boolean isFromAuthor = false;
    private boolean isFavorite = false;
    private boolean forceRefresh = true;
    private UserDataManager userDataManager;

    /**
     * Initialize the javaFX scene in 3 steps:
     * graphic Initialize: set Styles and fill ChoiceBox then create listeners.
     * preferences Initialize: initialize the preferences.
     * articlesList Initialize: downlaod from API and fill in the listView.
     * @param location
     * @param resourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        graphicInitialize();
        preferencesInitialize();
        articlesListInitialize();
    }

    private void graphicInitialize() {
        label.setStyle("-fx-border-color: deepskyblue; -fx-border-style: hidden solid hidden hidden; -fx-border-width: 2; -fx-text-fill: deepskyblue;");
        filterPane.setStyle("-fx-border-color: deepskyblue; -fx-border-style: solid solid hidden hidden; -fx-border-width: 2;");
        contentPane.setStyle("-fx-background-color: white;");
        infoText.setStyle("-fx-background-color: white;");
        createBasicDate();
        createCategories();
        contentPaneListeners();
    }

    private void preferencesInitialize() {
        userDataManager = new UserDataManager();
        maxSize = String.valueOf(userDataManager.getPrefMaxArticle());
        categoryBox.getSelectionModel().select(userDataManager.getPrefCategoryId());
        timeBox.getSelectionModel().select(userDataManager.getPrefTimeId());
    }

    private void articlesListInitialize() {
        specialSearch.setPrefHeight(0); specialSearch.setMinHeight(0); specialSearch.setMaxHeight(0);
        contentPane.getItems().clear();
        articles.clear();
        articles = downloadFromApi();
        downloadedarticles.addAll(articles);
        contentPane.setPrefHeight(720.0);
        contentPane.setMaxHeight(1.7976931348623157E308);
        getContent(noAPIFilter(articles), true);
        refresh();
    }

    /**
     * The method getContent takes articles from a list of articles and reate the items of the
     * listView then add them in the listView.
     * @param articles
     * @param overwrite if true, clear the listView before adding articles.
     */
    private void getContent(ArrayList<Article> articles, boolean overwrite) {
        showList();
        if (overwrite) {
            this.articles = articles;
            contentPane.getItems().clear();
        }
        for (Article article : articles) {
            HBox hBox = new HBox();
            contentPane.getItems().add(hBox);
            articlesBox.add(new CheckBox());
            hBox.getChildren().add(articlesBox.get(articlesBox.size()-1));

            VBox data = new VBox();
            data.getChildren().add(new Text(article.getTitle()));
            HBox authorsData = new HBox();
            for(String author : article.getAuthors().getData()) {
                Hyperlink temp = new Hyperlink(author);
                temp.setOnAction(getArticlesFrom);
                authorsData.getChildren().add(temp);
            }
            data.getChildren().add(authorsData);
            hBox.getChildren().add(data);
        }
        contentPane.refresh();
    }

    /**
     * The method showList change value of the listView to show articles.
     */
    private void showList() {
        if(!isFromAuthor && !isFavorite) {
            if(primaryWindow.getChildren().contains(subMenu)) primaryWindow.getChildren().remove(subMenu);
            specialSearch.setPrefHeight(0); specialSearch.setMinHeight(0); specialSearch.setMaxHeight(0);
        }
        contentPane.setPrefHeight(720.0); contentPane.setMaxHeight(1.7976931348623157E308);
    }

    /**
     * The method reloadData make a new query to the API with the specified filters and then
     * add the new articles in the listView (clear it before).
     * @throws InterruptedException
     */
    @FXML private void reloadData() throws InterruptedException {
        forceRefresh = false; isFromAuthor = false; isFavorite = false;
        showList();
        contentPane.getItems().clear(); infoText.getChildren().clear();
        contentPane.refresh();
        ArrayList<Article> reload = downloadFromApi();
        downloadedarticles.clear(); downloadedarticles.addAll(reload);
        getContent(reload, true);
        forceRefresh = true;
    }

    /**
     * The method selectAll check all the checkBox of each articles in the listView.
     * if all articles are selected, it will unselect all articles.
     */
    @FXML private void selectAll() {
        boolean selection = true;
        if(allSelected()) { selection = false; }
        for(Object l : contentPane.getItems()) {
            HBox line = (HBox) l;
            CheckBox check = (CheckBox) line.getChildren().get(0);
            check.setSelected(selection);
        }
    }

    private boolean allSelected() {
        for(Object l : contentPane.getItems()) {
            HBox line = (HBox) l;
            CheckBox check = (CheckBox) line.getChildren().get(0);
            if(!check.isSelected()) return false;
        }
        return true;
    }

    @FXML private void getFavorites() {
        isFromAuthor = false; isFavorite = true;
        disableArticleAction();
        contentPane.getItems().clear();
        specialSearch.getChildren().clear();
        getContent(userDataManager.getFavArticles(), true);
        Hyperlink textFullSearch = new Hyperlink("<- Back to complete results");
        textFullSearch.setOnAction(getFullSearch);
        specialSearch.getChildren().add(textFullSearch);
        specialSearch.getChildren().add(new Text("User Favorites Articles"));
        specialSearch.setPrefHeight(20); specialSearch.setMinHeight(20); specialSearch.setMaxHeight(20);
        infoText.getChildren().clear(); favoriteCheckBox.setSelected(false);
    }

    @FXML private void getStats() {
        disableArticleAction();
        isFromAuthor = false; isFavorite = false;
        if(primaryWindow.getChildren().contains(subMenu)) { primaryWindow.getChildren().remove(subMenu); }
        Stats stats = new Stats(articles);
        contentPane.getItems().clear();
        specialSearch.getChildren().clear();
        selectedAuthorArticles.clear();
        Hyperlink textAuthor = new Hyperlink("<- Back to complete results");
        textAuthor.setOnAction(getFullSearch);
        specialSearch.getChildren().add(textAuthor);
        specialSearch.getChildren().add(new Text("Statistics from the previous Entry"));
        specialSearch.setPrefHeight(20); specialSearch.setMinHeight(20); specialSearch.setMaxHeight(20);
        TextFlow statsBox = new TextFlow();
        statsBox.getChildren().add(new Text("Amount of Articles per Days:\n"));
        statsBox.getChildren().add(new Text(stats.amountByDate()+"\n\n"));
        statsBox.getChildren().add(new Text("Most published Authors:\n"));
        statsBox.getChildren().add(new Text(stats.mostPublishedAuthor(5)+"\n\n"));
        statsBox.getChildren().add(new Text("Amount of Articles per Categories:\n"));
        statsBox.getChildren().add(new Text(stats.amountByCategories()+"\n\n"));
        setScrollSubMenu(statsBox);
    }

    @FXML private void getPreferences() {
        disableArticleAction();
        isFromAuthor = false; isFavorite = false;
        if(primaryWindow.getChildren().contains(subMenu)) { primaryWindow.getChildren().remove(subMenu); }
        contentPane.getItems().clear();
        specialSearch.getChildren().clear();
        selectedAuthorArticles.clear();
        Hyperlink textAuthor = new Hyperlink("<- Back to complete results");
        textAuthor.setOnAction(getFullSearch);
        specialSearch.getChildren().add(textAuthor);
        specialSearch.getChildren().add(new Text("Preferences"));
        specialSearch.setPrefHeight(20); specialSearch.setMinHeight(20); specialSearch.setMaxHeight(20);

        TextFlow prefBox = new TextFlow();
        prefMaxArticle = new TextField(String.valueOf(userDataManager.getPrefMaxArticle()));
        prefBox.getChildren().add(new Text("\n\tMax Articles per Query:\t"));
        prefBox.getChildren().add(prefMaxArticle);
        prefCategoryId = new ComboBox(categoryBox.getItems());
        prefCategoryId.getSelectionModel().select(userDataManager.getPrefCategoryId());
        prefBox.getChildren().add(new Text("\n\n\tOn Start Category:\t"));
        prefBox.getChildren().add(prefCategoryId);
        prefTimeID = new ChoiceBox(timeBox.getItems());
        prefTimeID.getSelectionModel().select(userDataManager.getPrefTimeId());
        prefBox.getChildren().add(new Text("\n\n\tOn Start Time:\t"));
        prefBox.getChildren().add(prefTimeID);
        prefDownloadDestination = new TextField(String.valueOf(userDataManager.getPrefDownloadDestination()));
        prefBox.getChildren().add(new Text("\n\n\tDownload Folder:\t"));
        prefBox.getChildren().add(prefDownloadDestination);
        prefBox.getChildren().add(new Text("\n\n\t"));
        Button save = new Button("Save");
        prefBox.getChildren().add(save);
        save.setOnAction(this::savePreferences);
        setScrollSubMenu(prefBox);
    }

    @FXML private void getHelp() {
        disableArticleAction();
        isFromAuthor = false; isFavorite = false;
        if(primaryWindow.getChildren().contains(subMenu)) { primaryWindow.getChildren().remove(subMenu); }
        Stats stats = new Stats(articles);
        contentPane.getItems().clear();
        specialSearch.getChildren().clear();
        selectedAuthorArticles.clear();
        Hyperlink textAuthor = new Hyperlink("<- Back to complete results");
        textAuthor.setOnAction(getFullSearch);
        specialSearch.getChildren().add(textAuthor);
        specialSearch.getChildren().add(new Text("Help"));
        specialSearch.setPrefHeight(20); specialSearch.setMinHeight(20); specialSearch.setMaxHeight(20);
        TextFlow statsBox = new TextFlow();
        statsBox.getChildren().add(new Text("2020 - Year 2 Computer Science Licence at Aix-Marseille University\n" +
                "Project done by Eddy Ikhlef - Eunsun YUn - Yann Forner - Jeremy Gau\n\n-----\n\n<Authors, Keywords search>\n" +
                "To start a query, the field have to end with a coma (,) or a next line (Enter)"));
        setScrollSubMenu(statsBox);
    }

    private void setScrollSubMenu(TextFlow box) {
        subMenu = new ScrollPane();
        subMenu.setContent(box); subMenu.setFitToWidth(true);
        primaryWindow.getChildren().add(subMenu);
        primaryWindow.setVgrow(subMenu, Priority.ALWAYS);
        contentPane.setPrefHeight(0); contentPane.setMinHeight(0); contentPane.setMaxHeight(0);
    }

    private void savePreferences(ActionEvent event) {
        userDataManager.setAll(String.valueOf(prefMaxArticle.getText()), prefCategoryId.getSelectionModel().getSelectedIndex(),
                prefTimeID.getSelectionModel().getSelectedIndex(), prefDownloadDestination.getText());
    }

    private void getInformation(int ID) {
        infoText.getChildren().clear();
        Article article;
        try {
            if (isFromAuthor) article = selectedAuthorArticles.get(ID);
            else article = articles.get(ID);
            Text title = new Text(article.getTitle()+"\n");
            title.setStyle("-fx-font-size: 24px; -fx-underline: true;");
            Text authors = new Text(article.getAuthors()+"\nPublished at "+article.getPublished()+" (Updated at "+article.getUpdated()+")\nOn: "+article.getPrincipaleCategory()+" "+article.getCategories()+"\n\n");
            authors.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
            Text summary = new Text("SUMMARY: \n"+articles.get(ID).getSummary());
            summary.setStyle("-fx-font-size: 12px;");
            infoText.getChildren().add(title);
            infoText.getChildren().add(authors);
            infoText.getChildren().add(summary);
            checkFavorite(article);
        } catch (Exception e) {
            disableArticleAction();
        }
    }

    private void checkFavorite(Article article) {
        for(Article favArticle : userDataManager.getFavArticles()) {
            if(article.equals(favArticle)) {
                favoriteCheckBox.setSelected(true);
                return;
            }
        }
        favoriteCheckBox.setSelected(false);
    }

    @FXML private void updateFavorite() {
        if(!favoriteCheckBox.isSelected()) {
            userDataManager.removeFavArticle(articles.get(contentPane.getSelectionModel().getSelectedIndex()));
            if(isFavorite) contentPane.getItems().remove(contentPane.getSelectionModel().getSelectedItem());
        } else {
            userDataManager.addFavArticle(articles.get(contentPane.getSelectionModel().getSelectedIndex()));
        }
    }

    private void disableArticleAction() {
        if(contentPane.getSelectionModel().isEmpty()) favoriteCheckBox.setDisable(true); downloadThisArticle.setDisable(true); downloadSelected.setDisable(true);
    }

    /**
     * The method refresh will do a new query from API with the specified filters and return the query result.
     */
    @FXML private void refresh() {
        if(!forceRefresh) return;
        setMinDate(); setMaxDate(); Instant[] fromTo = new Instant[2]; fromTo[0] = Instant.MIN; fromTo[1] = Instant.now();
        if(isTimeAdvanced) fromTo = DateParser.getAdvancedDate(timeBoxFrom, timeBoxTo, ZONEID);
        else if(!timeBox.getValue().toString().equals("Most Recent")) fromTo = DateParser.getBasicDate(timeBox.getValue().toString(), ZONEID);
        ArticleManager articleManager = new ArticleManager(categoryBox.getSelectionModel().getSelectedItem().toString(), isTimeAdvanced, authorsBox.getText(), keywordsBox.getText(), DateParser.getBasicDate(timeBox.getValue().toString(), ZONEID), fromTo[0], fromTo[1], downloadedarticles);
        if(articles.size() != 0) {
            if(isTimeAdvanced && DateParser.isBefore(DateParser.getDate(articles.get(0).getPublished()), fromTo[1])) getContent(downloadFromApi(), true);
        }
        if(isFromAuthor) articleManager.updateList(selectedAuthorArticles);
        else if(isFavorite) { articleManager.updateList(userDataManager.getFavArticles()); }
        if(!isFromAuthor && !isFavorite && articleManager.filter().size() < userDataManager.getPrefMaxArticle()
                || ((authorsBox.getText().endsWith(",") || authorsBox.getText().endsWith("\n")) &&
                (keywordsBox.getText().endsWith(",") || keywordsBox.getText().endsWith("\n")))) {
            getContent(downloadFromApi(), true);
        }
        else getContent(articleManager.filter(), true);
        if(contentPane.getItems().size() == 0) disableArticleAction();
    }

    /**
     * The method handleMouseClick is used to disable or able the download function of the articles
     * of the listView if the Interface don't show articles.
     */
    public void handleMouseClick() {
        getInformation(contentPane.getSelectionModel().getSelectedIndex());
        if (contentPane.getItems().size() > 0) {
            if(!contentPane.getSelectionModel().isEmpty()) checkFavorite(articles.get(contentPane.getSelectionModel().getSelectedIndex()));
            favoriteCheckBox.setDisable(false);
            downloadThisArticle.setDisable(false);
            downloadSelected.setDisable(false);
        }
        else {
            favoriteCheckBox.setDisable(true);
            downloadThisArticle.setDisable(true);
            downloadSelected.setDisable(true);
        }
    }

    @FXML private void setTimeMode() {
        if(isTimeAdvanced) {
            isTimeAdvanced = false;
            timeAdvanced.setVisible(false);
            timeBasic.setVisible(true);
        }
        else {
            isTimeAdvanced = true;
            timeBasic.setVisible(false);
            timeAdvanced.setVisible(true);
        }
    }

    private ArrayList<Article> downloadFromApi() {
        ArrayList<Article> downloaded = new ArrayList<>();
        int start = 0;
        while(downloaded.size()<Integer.parseInt(maxSize)) {
            queryBuilder = new QueryBuilder();
            if(articles.size() != 0 && keywordsBox.getText().length() > 0 && (keywordsBox.getText().endsWith(",") || keywordsBox.getText().endsWith("\n"))) { filterKeywords(queryBuilder); }
            if(!categoryBox.getSelectionModel().getSelectedItem().toString().equals("Any")) { filterCategories(queryBuilder); }
            else { queryBuilder.all(); }
            if(articles.size() != 0 && authorsBox.getText().length() > 0 && (authorsBox.getText().endsWith(",") || authorsBox.getText().endsWith("\n"))) { filterAuthor(queryBuilder); }
            queryBuilder.addStart(String.valueOf(start));
            queryBuilder.addMaxResults("100");
            downloaded.addAll(new AtomReader(queryBuilder.proceed()).getArticles().getArticles());
            downloaded = noAPIFilter(downloaded);
            if(downloaded.size() < Integer.parseInt(maxSize) && (timeBox.getSelectionModel().getSelectedItem().toString().equals("Today")
                    || timeBox.getSelectionModel().getSelectedItem().toString().equals("Yesterday")
                    || timeBox.getSelectionModel().getSelectedItem().toString().equals("This Week"))) return downloaded;
            if(downloaded.size() != 0 && !timeBox.getSelectionModel().getSelectedItem().toString().equals("Most Recent")) {
                Instant[] fromTo;
                if(isTimeAdvanced) {
                    fromTo = DateParser.getAdvancedDate(timeBoxFrom, timeBoxTo, ZONEID);
                    if(DateParser.getDate(downloaded.get(downloaded.size()-1).getPublished()).isBefore(fromTo[0])) return downloaded;
                }
                else if(DateParser.getDate(downloaded.get(downloaded.size()-1).getPublished()).isBefore(
                        DateParser.getBasicDate(timeBox.getSelectionModel().getSelectedItem().toString(), ZONEID)[0])) return downloaded;
            }
            else if(downloaded.size() > Integer.parseInt(maxSize)) break;
            start+=100;
        }
        downloadedarticles.clear(); downloadedarticles.addAll(downloaded);
        return new ArrayList<>(downloaded.subList(0, Integer.parseInt(maxSize)));
    }

    @FXML private void downloadAll() {
        for(int i = 0; i<contentPane.getItems().size(); i++) {
            Article article;
            if(isFromAuthor) article = selectedAuthorArticles.get(i);
            else if(isFavorite) article = userDataManager.getFavArticles().get(i);
            else article = articles.get(i);
            new Downloader(article.getPdfLink(), userDataManager.getPrefDownloadDestination());
        }
        showMessageDialog(null, "Sucessfully downloaded all articles of the request\n\n");
    }

    @FXML private void downloadArticle() {
        Article article;
        if(isFromAuthor) article = selectedAuthorArticles.get(contentPane.getSelectionModel().getSelectedIndex());
        else if(isFavorite) article = userDataManager.getFavArticles().get(contentPane.getSelectionModel().getSelectedIndex());
        else article = articles.get(contentPane.getSelectionModel().getSelectedIndex());
        new Downloader(article.getPdfLink(), userDataManager.getPrefDownloadDestination());
        showMessageDialog(null, "Sucessfully downloaded article\n\n"+article.getTitle());
    }

    @FXML private void downloadSelectedArticle() {
        String prompt = "Sucessfully downloaded article\n\n";
        for(int i = 0; i<contentPane.getItems().size(); i++) {
            HBox selected = (HBox) contentPane.getItems().get(i);
            CheckBox selectedCheck = (CheckBox) selected.getChildren().get(0);
            if (selectedCheck.isSelected()) {
                Article article;
                if(isFromAuthor) article = selectedAuthorArticles.get(i);
                else if(isFavorite) article = userDataManager.getFavArticles().get(i);
                else article = articles.get(i);
                new Downloader(article.getPdfLink(), userDataManager.getPrefDownloadDestination());
                prompt += article.getTitle()+"\n";
            }
        }
        showMessageDialog(null, prompt);
    }

    private void filterKeywords(QueryBuilder queryBuilder) {
        String[] keywords = keywordsBox.getText().split("[,\n]");
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = keywords[i].trim().replace(" ", "+");
            queryBuilder.addKeyword(keywords[i]);
        }
    }

    private void filterCategories(QueryBuilder queryBuilder) {
        queryBuilder.addCategory(categoryBox.getSelectionModel().getSelectedItem().toString());
    }

    private void filterAuthor(QueryBuilder queryBuilder) {
        String[] authors = authorsBox.getText().split("[,\n]");
        for (int i = 0; i < authors.length; i++) {
            authors[i] = authors[i].trim().replace(" ", "+");
            queryBuilder.addAuthor(authors[i]);
        }
    }

    private ArrayList<Article> filterDate(ArrayList<Article> articles) {
        if((timeBox.getValue().toString().equals("Most Recent") && !isTimeAdvanced) ||  (timeBoxFrom.getValue() == null && timeBoxTo.getValue() == null) && isTimeAdvanced) { return articles; }
        Instant[] fromTo ;
        ArrayList<Article> filtered = new ArrayList<>();
        if(isTimeAdvanced) { fromTo = DateParser.getAdvancedDate(timeBoxFrom, timeBoxTo, ZONEID); }
        else { fromTo = DateParser.getBasicDate(timeBox.getValue().toString(), ZONEID); }
        for(Article article : articles) {
            Instant articleDate = DateParser.getDate(article.getPublished());
            if(DateParser.isBetween(articleDate, fromTo[0], fromTo[1])) { filtered.add(article); }
        }
        return filtered;
    }

    private ArrayList<Article> noAPIFilter(ArrayList articles) {
        ArrayList<Article> filtered = filterDate(articles);
        return filtered;
    }

    /**
     * The method ContentPaneListeners will create a listener on the listView.
     * delete: press DEL to dleete an article of the list.
     * move: using directinnal arrays or pg Up / pg DW user can move from an article to another.
     */
    private void contentPaneListeners() {
        contentPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyEvent) {
                if (contentPane.getId() != null) {
                    //Initialize listeners : delete
                    if (contentPane.getId() != null && keyEvent.getCode().equals(KeyCode.DELETE)) {
                        articles.remove(contentPane.getSelectionModel().getSelectedIndex());
                        contentPane.getItems().remove(contentPane.getSelectionModel().getSelectedItem());
                        getInformation(contentPane.getSelectionModel().getSelectedIndex());
                    }
                    //Initialize listeners : move with arrow
                    if(keyEvent.getCode().equals(KeyCode.UP) || keyEvent.getCode().equals(KeyCode.DOWN) || keyEvent.getCode().equals(KeyCode.PAGE_UP) || keyEvent.getCode().equals(KeyCode.PAGE_DOWN)) {
                        getInformation(contentPane.getSelectionModel().getSelectedIndex());
                    }
                    disableArticleAction();
                }}});
    }
    /**
     * the method getArticlesFrom initialize a listener that is used for the Hyperlink of
     * Authors in the listView.
     */
    private EventHandler<ActionEvent> getArticlesFrom =
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    isFromAuthor = true; isFavorite = false;
                    disableArticleAction();
                    specialSearch.getChildren().clear();
                    Hyperlink author = (Hyperlink) e.getSource();
                    contentPane.getItems().clear();
                    specialSearch.getChildren().clear();
                    selectedAuthorArticles.clear();
                    queryBuilder = new QueryBuilder();
                    queryBuilder.addMaxResults(maxSize);
                    queryBuilder.addAuthor(author.getText().replace(" ", "+AND+"));
                    selectedAuthorArticles = new AtomReader(queryBuilder.proceed()).getArticles().getArticles();
                    getContent(noAPIFilter(selectedAuthorArticles), false);
                    Hyperlink textAuthor = new Hyperlink("<- Back to complete results");
                    textAuthor.setOnAction(getFullSearch);
                    specialSearch.getChildren().add(textAuthor);
                    specialSearch.getChildren().add(new Text(" Articles From: "+author.getText()));
                    getContent(selectedAuthorArticles, true);
                    specialSearch.setPrefHeight(20); specialSearch.setMinHeight(20); specialSearch.setMaxHeight(20);
                }
            };

    /**
     * the method getArticlesFrom initialize a listener that is used for the Hyperlink of
     * "go back to full search" in the listView when we are seeing articles from an author or
     * statistics of the query.
     */
    private EventHandler<ActionEvent> getFullSearch =
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    isFromAuthor = false; isFavorite = false;
                    disableArticleAction();
                    contentPane.getItems().clear();
                    showList();
                    int max = (downloadedarticles.size() > userDataManager.getPrefMaxArticle()) ? userDataManager.getPrefMaxArticle() : downloadedarticles.size();
                    getContent(new ArrayList<>(downloadedarticles.subList(0, max)), true);
                }
            };

    private void setMinDate() {
        if(timeBoxFrom.getValue() == null) return;
        LocalDate minDate = DateParser.getDate(timeBoxFrom, ZONEID).atZone(ZoneId.of(ZONEID)).toLocalDate();
        timeBoxTo.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(minDate) < 0 );
            }
        });
    }
    private void setMaxDate() {
        if(timeBoxTo.getValue() == null) return;
        LocalDate minDate = DateParser.getDate(timeBoxTo, ZONEID).atZone(ZoneId.of(ZONEID)).toLocalDate();
        timeBoxFrom.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(minDate) > 0 );
            }
        });
    }

    private void createBasicDate() {
        timeBox.getItems().add("Most Recent");
        timeBox.getItems().add("Today");
        timeBox.getItems().add("Yesterday");
        timeBox.getItems().add("This Week");
        timeBox.getItems().add("This Month");
        timeBox.getItems().add("This Year");
    }

    private void createCategories() {
        categoryBox.getItems().add("Any");
        String[] allCat = Category.allCat;
        for(int i = 0; i<allCat.length; i++) { categoryBox.getItems().add(allCat[i]); }
    }
}
