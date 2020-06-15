package app.arxivorg.controller;
import app.arxivorg.ArxivOrg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
@ExtendWith(ApplicationExtension.class)
public class ArxivOrgControllerTest {

    @Start
    public void start (Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(ArxivOrg.class.getResource("/app/arxivorg/view/arxivorg.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
    }

    @Test
    void testDrawArticlesListEmpty(FxRobot robot) {
        ListView listView = robot.lookup("#contentPane").queryListView();
        Assertions.assertThat(listView.getItems().size() == 0);
    }

    @Test
    void testDrawArticlesList(FxRobot robot) {
        ListView listView = robot.lookup("#contentPane").queryListView();
        Assertions.assertThat(listView.getItems().size() != 0);
    }

    @Test
    void testAuthors(FxRobot robot) {
        ComboBox category = robot.lookup("#categoryBox").queryComboBox();
        category.getSelectionModel().select(0);
        robot.clickOn("#authorsBox");
        robot.write("Han Fu,");
        ListView listView = robot.lookup("#contentPane").queryListView();
        for(Object data : listView.getItems()) {
            HBox box = (HBox) data;
            Assertions.assertThat(((HBox) data).getChildren().get(1).toString().contains("Han Fu"));
        }
    }

    @Test
    void testKeywords(FxRobot robot) {
        robot.clickOn("#keywordsBox");
        robot.write("Big, \ndata,");
        ListView listView = robot.lookup("#contentPane").queryListView();
        for(Object data : listView.getItems()) {
            HBox box = (HBox) data;
            Assertions.assertThat(((HBox) data).getChildren().get(1).toString().contains("Big data"));
        }
    }

    @Test
    void testCategory(FxRobot robot) {
        robot.clickOn("#categoryBox");
        robot.moveBy(0, 200);
        robot.clickOn(MouseButton.PRIMARY);
        ListView listView = robot.lookup("#contentPane").queryListView();
        for(int i = 0; i<listView.getItems().size(); i++) {
            listView.getSelectionModel().select(i);
            String data = robot.lookup("#infoText").toString();
            Assertions.assertThat(data.contains("cs-CV"));
        }
    }

    @Test
    void testSelection(FxRobot robot) {
        ListView listView = robot.lookup("#contentPane").queryListView();
        HBox data = (HBox) listView.getItems().get(0);
        CheckBox check = (CheckBox) data.getChildren().get(0);
        Assertions.assertThat(check.isSelected() == false);
        check.setSelected(true);
        Assertions.assertThat(check.isSelected() == true);
    }

    @Test
    void testStats(FxRobot robot) {
        boolean isStats = false;
        robot.clickOn("#menuStats");
        VBox subMenu = (VBox) robot.lookup("#primaryWindow").queryAll().iterator().next();
        for(Object children : subMenu.getChildren()) {
            if(children instanceof javafx.scene.control.ScrollPane) {
                ScrollPane stats = (ScrollPane) children;
                TextFlow statsText = (TextFlow) stats.getContent();
                for(Object childChild : statsText.getChildren())
                    if(childChild instanceof javafx.scene.text.Text) {
                        Text childChildText = (Text) childChild;
                        if(childChildText.getText().contains("Amount")) isStats = true;
                    }
            }
        }
        Assert.assertTrue(isStats);
    }

    @Test
    void testPreferences(FxRobot robot) {
        boolean isPreferences = false;
        robot.clickOn("#menuPref");
        VBox subMenu = (VBox) robot.lookup("#primaryWindow").queryAll().iterator().next();
        for(Object children : subMenu.getChildren()) {
            if(children instanceof javafx.scene.control.ScrollPane) {
                ScrollPane stats = (ScrollPane) children;
                TextFlow statsText = (TextFlow) stats.getContent();
                for(Object childChild : statsText.getChildren())
                    if(childChild instanceof javafx.scene.text.Text) {
                        Text childChildText = (Text) childChild;
                        if(childChildText.getText().contains("Max Articles per Query")) isPreferences = true;
                    }
            }
        }
        Assert.assertTrue(isPreferences);
    }

    @Test
    void testReload(FxRobot robot) {
        robot.clickOn("#menuReload");
        ListView listView = robot.lookup("#contentPane").queryListView();
        Assertions.assertThat(listView.getItems().size() > 0);
    }

    @Test
    void testSelectAll(FxRobot robot) {
        robot.clickOn("#menuSelAll");
        ListView listView = robot.lookup("#contentPane").queryListView();
        for(Object article : listView.getItems()) {
            HBox data = (HBox) article;
            CheckBox check = (CheckBox) data.getChildren().get(0);
            Assertions.assertThat(check.isSelected() == true);
        }
    }
}