package app.arxivorg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class ArxivOrg extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/app/arxivorg/view/arxivorg.fxml"));
        primaryStage.setTitle("ArxivOrg");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/ico.jpg")));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
