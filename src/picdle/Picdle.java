package picdle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Picdle extends Application {

    private Stage primaryStage;
    private Scene helloScene;
    private Scene instructionsScene;
    private Scene playScene;

@Override
public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;

    // Load hello scene
    FXMLLoader helloLoader = new FXMLLoader(getClass().getResource("hellofx.fxml"));
    Parent helloRoot = helloLoader.load();
    helloScene = new Scene(helloRoot);

    // Set reference to Picdle instance in Controller
    Controller controller = helloLoader.getController();
    controller.setPicdleApp(this);

    // Load instructions scene
    FXMLLoader instructionsLoader = new FXMLLoader(getClass().getResource("instructions.fxml"));
    Parent instructionsRoot = instructionsLoader.load();
    instructionsScene = new Scene(instructionsRoot);
    
    // Set reference to Picdle instance in Controller for the instruction scene
    Controller instructionsController = instructionsLoader.getController();
    instructionsController.setPicdleApp(this);
    
    // Load Play scene
    FXMLLoader playLoader = new FXMLLoader(getClass().getResource("Play.fxml"));
    Parent playRoot = playLoader.load();
    playScene = new Scene(playRoot);
    
    // Set reference to Picdle instance in Controller for the play scene
    Controller playController = playLoader.getController();
    playController.setPicdleApp(this);

    // Set initial scene
    primaryStage.setTitle("Picdle");
    primaryStage.setScene(helloScene);
    primaryStage.show();
}

    // Method to switch to instructions scene
    public void showInstructionsScene() {
        primaryStage.setScene(instructionsScene);
    }
    
    // Method to switch to Play scene
    // Method to switch to Play scene
    public void showPlayScene() {
    // Check if the play scene is already loaded
    if (playScene != null) {
        // Get the current stage and set the play scene
        primaryStage.setScene(playScene);
    } else {
        try {
            // Load the play scene if it's not already loaded (this should not happen in normal flow)
            FXMLLoader playLoader = new FXMLLoader(getClass().getResource("Play.fxml"));
            Parent playRoot = playLoader.load();
            playScene = new Scene(playRoot);
            primaryStage.setScene(playScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
    public void showMenuScreen() 
    {
        primaryStage.setScene(helloScene);
    }
 
    public static void main(String[] args) 
    {
        launch(args);
    }
}