package picdle;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import javafx.scene.shape.Rectangle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;

import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.collections.ObservableList;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;



import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;


import javafx.scene.image.PixelReader;

public class Controller implements Initializable {
    

    
    private Image uploadedImage;

    private List<ImageView> imagePieces;  //holds the image pieces
    
    // Variable to store the initial snapshot of the grid
    private WritableImage initialSnapshot;
    
    
    
    private double initialX;
    private double initialY;
    private double initialTranslateX;
    private double initialTranslateY;

    
    private int level;

    private Picdle picdleApp;

    public void setPicdleApp(Picdle picdleApp) {
        this.picdleApp = picdleApp;
    }
    
    @FXML
    private Button playButton;   //play button on main menu screen
    
    @FXML
    private Button instructionButton;  // instruction button on main menu screen 
    
    @FXML
    private Button BackButton;  // back button on instruction scene
    
    @FXML
    private Button BackButtonForPlayScene;  // back button on play scene 
    
    @FXML
    private ComboBox<String> levelSelector;   // level selector combobox on play scene 
    
    @FXML
    private Button uploadButton;  // upload button for the play scene 
    
    @FXML
    private Button scrambleButton;  // Scramble button for the play scene 
    
    @FXML
    private Button checkbutton;  // check button for the play scene 
    
    @FXML
    private Rectangle assemblyArea;  
    
    @FXML
    private ImageView uploadedImageView;
    
    @FXML
    private GridPane assemblyGrid;    //image grid for green pieces of images to be swapped out.
    
    @FXML
    private  Button resetButton;
    
    /*
    This basically initializes the level selector of the game so that the user first has to 
    select the level, and once selected, it will show up on the combobox
    and will be used later to determine the grid dimensions.
    */
    @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            // Perform initialization tasks that rely on UI components here
            
            if(levelSelector != null)
            {
                // Add items to the levelSelector ComboBox
                levelSelector.setItems(FXCollections.observableArrayList("2", "3", "4", "5", "6", "8", "10", "12"));
                
                // Add an event listener to the ComboBox to handle changes in selection
                levelSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // newValue contains the newly selected value
                if (newValue != null) {
                    // Use the selected value in your controller logic
                    System.out.println("Selected level: " + newValue);
                    // You can perform actions based on the selected level here
                    // For example, you can call methods or update UI components accordingly
                    
                    level = Integer.parseInt(newValue);
                }
            });
            }
         
        }

     /*
     This method is used to reload or load a new play scene everytime the play button is hit.
     This means that everytime play button is pressed, it will restart a new game all the time. 
     Therefore, if you go back from the play scene and re-enter the play scene from the main menu
     You progress will be lost. (This might be later worked on future updates)
    */
    @FXML
    private void playbuttonAction(ActionEvent event) 
    {
        try {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Play.fxml"));
        Parent root = loader.load();

        // Get the current stage from the event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a new scene with the root node
        Scene scene = new Scene(root);

        // Get the controller associated with the loaded FXML
        Controller controller = loader.getController();

        // Pass the picdleApp reference to the controller
        controller.setPicdleApp(picdleApp);

        // Set the new scene on the stage
        stage.setScene(scene);
        stage.show();
        } catch (IOException e) {
        e.printStackTrace();
        }
        
        
        
        System.out.println("\n It reached here! 1");
    }
    
     // Method called when the user presses the check button
    @FXML
    private void onCheckButtonClick() {
        if (isPuzzleSolved(assemblyGrid)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText(null);
            alert.setContentText("You have solved the puzzle! Press Reset to start over again.");
            alert.showAndWait();
      
        } else {
            // Puzzle not solved
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Puzzle Not Solved");
            alert.setHeaderText(null);
            alert.setContentText("You did not solve the puzzle correctly. Try swapping pieces out and look more carefully.");
            alert.showAndWait();
            }
    }
    
    /*
     This method is used to perform scene switch from main menu scene to instruction scene 
    */
    @FXML
    private void instructionButtonAction(ActionEvent event) 
    {
        picdleApp.showInstructionsScene();
    }
    
    /*
     This method is used to perform scene switch from instruction scene to main menu scene
     It's also used for the switch from play scene to menu scene 
    */
    @FXML
     private void backButtonAction(ActionEvent event)
    {  
        picdleApp.showMenuScreen();  
    }
     
     /*
     This button is for resetting the state of the play scene.
     It's purpose is to reset the grid, images, and bring it back to 
     the original state as it was after you launch the game.
     It essentially relouds the entire play scene into the fxml loader so it restarts.
     */
     @FXML
     private void resetButtonAction(ActionEvent event) {  
    try {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Play.fxml"));
        Parent root = loader.load();

        // Get the current stage from the event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a new scene with the root node
        Scene scene = new Scene(root);

        // Get the controller associated with the loaded FXML
        Controller controller = loader.getController();

        // Pass the picdleApp reference to the controller
        controller.setPicdleApp(picdleApp);

        // Set the new scene on the stage
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
     
     
     /*
     This method will open a window that prompts the user to select a file from their system 
     And from there, it will display on the left hand rectangle of the image selected. 
    */
@FXML
public void uploadImage(ActionEvent event) {  
    // Create a file chooser dialog
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Image File");
    
    // Set filter to show only image files
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
    );
    
    // Show the file chooser dialog and get the selected file
    File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
    if (selectedFile != null) {
        // Load the image
        Image originalImage = new Image(selectedFile.toURI().toString());
        
        // Create an ImageView with the desired size
        ImageView imageView = new ImageView(originalImage);
        imageView.setFitWidth(600);
        imageView.setFitHeight(600);
        
        // Create a writable image with the same dimensions
        WritableImage writableImage = new WritableImage(600, 600);
        
        // Render the ImageView onto the writable image
        imageView.snapshot(null, writableImage);
        
        
        //set the rows and column constraints before inputting in images into grid
        SetColRowGrid(assemblyGrid);
        
        // Divide the writable image into pieces based on the selected level
        imagePieces = divideImageIntoGridSizeImages(writableImage, level, level);
        
        // Loop through the image pieces and add them to the grid pane
        inputPiecesOnGrid(imagePieces, assemblyGrid);
        
        //make the imagepieces Draggable
        if(imagePieces != null)
        {
            
            setupDraggablePieces();
            
        } 
        
       takeInitialSnapshot(assemblyGrid);
  
    }
}
     
     /*
     This method will scramble the image on the left side and put the scrambled pieces on the right hand side
     It will also make the images draggable.
    */
    @FXML
    
     private void scrambleAction(ActionEvent event)
    {
        
        
        // Clear existing content for assemblyGrid
        assemblyGrid.getChildren().clear();

        //set the rows and column constraints before inputting in images into grid
        SetColRowGrid(assemblyGrid);

        // Shuffle the pieces before inputting into the scrambledpiecesGrid
        Collections.shuffle(imagePieces); 
        
        // Display the shuffled pieces on the grid
        inputPiecesOnGrid(imagePieces, assemblyGrid);
        // Update the initial positions after shuffling

    }
     
     // Divide the uploaded image into the appropriate size and number based on level
    public List<ImageView> divideImageIntoGridSizeImages(Image image, int rows, int cols) {
    List<ImageView> imagePieces = new ArrayList<>();
    int pieceWidth = (int) Math.ceil(image.getWidth() / cols); // Adjusted to ensure all pixels are covered
    int pieceHeight = (int) Math.ceil(image.getHeight() / rows); // Adjusted to ensure all pixels are covered

    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            int x = col * pieceWidth;
            int y = row * pieceHeight;
            int width = Math.min(pieceWidth, (int) (image.getWidth() - x)); // Adjusted to handle remainder
            int height = Math.min(pieceHeight, (int) (image.getHeight() - y)); // Adjusted to handle remainder
            
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int imageX = x + i;
                    int imageY = y + j;
                    
                    if (imageX < image.getWidth() && imageY < image.getHeight()) {
                        Color color = image.getPixelReader().getColor(imageX, imageY);
                        pixelWriter.setColor(i, j, color);
                    }
                }
            }
            
            ImageView imageView = new ImageView(writableImage);
            imagePieces.add(imageView);
        }
    }
    return imagePieces;
}
    

    // Loop through the image pieces and add them to the grid pane
    private void inputPiecesOnGrid(List<ImageView> imagePieces, GridPane grid) {
        for (int i = 0; i < imagePieces.size(); i++) {
          ImageView pieceView = imagePieces.get(i);

          // Calculate row and column indices based on current index and grid layout
          int row = i / level;
          int col = i % level;

          // Add the ImageView to the grid cell
          grid.add(pieceView, col, row);
        }
      }
    
    
    
    private void setupDraggablePieces() {
    for (Node piece : imagePieces) {
        piece.setOnMousePressed(event -> {
            // Store the initial mouse coordinates
            initialX = event.getSceneX();
            initialY = event.getSceneY();

            // Store the initial position of the piece
            initialTranslateX = piece.getTranslateX();
            initialTranslateY = piece.getTranslateY();

            // Bring the piece to the front
            piece.toFront();
        });

        piece.setOnMouseDragged(event -> {
            // Calculate the offset from the initial position
            double offsetX = event.getSceneX() - initialX;
            double offsetY = event.getSceneY() - initialY;

            // Update the position of the piece
            piece.setTranslateX(initialTranslateX + offsetX);
            piece.setTranslateY(initialTranslateY + offsetY);

            // Snap the piece to the nearest grid cell if it's close enough
            snapToGrid(piece);
            });

    }
}

private int numRows = level; 
private int numCols = level;

private void snapToGrid(Node piece) {
    double snapThreshold = 20; // Adjust this value based on your requirement

    // Calculate the nearest grid cell position
    double nearestX = Math.round(piece.getTranslateX() / (600.0 / level)) * (600.0 / level);
    double nearestY = Math.round(piece.getTranslateY() / (600.0 / level)) * (600.0 / level);

    // Snap the piece to the nearest grid cell
    piece.setTranslateX(nearestX);
    piece.setTranslateY(nearestY);

}


    //this helper method basically helps to set the columsn and rows according to the level that is set
    private void SetColRowGrid(GridPane grid)
    {
        // Set the number of rows
        int numRows = level; // Change this to your desired number of rows
        grid.getRowConstraints().clear(); // Clear existing row constraints
        for (int i = 0; i < numRows; i++) {
            RowConstraints row = new RowConstraints();
            // Customize row constraints if needed
            grid.getRowConstraints().add(row);
        }

        // Set the number of columns
        int numCols = level; // Change this to your desired number of columns
        grid.getColumnConstraints().clear(); // Clear existing column constraints
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints col = new ColumnConstraints();
            // Customize column constraints if needed
            grid.getColumnConstraints().add(col);
        }
    }

  
    //prints out the contents of the Grid
    private void printGridContents() {
    ObservableList<Node> children = assemblyGrid.getChildren();

    for (Node node : children) {
        Integer row = GridPane.getRowIndex(node);
        Integer col = GridPane.getColumnIndex(node);

        if (row != null && col != null) {
            System.out.println("Node: " + node + " is at Row: " + row + ", Column: " + col);
        }
    }
}

    
    // Method to check if the puzzle is solved
// Method to compare the current state of the grid with the initial snapshot
    private boolean isPuzzleSolved(GridPane grid) {
    // Take a new snapshot of the current state of the grid
    WritableImage currentSnapshot = new WritableImage((int) grid.getWidth(), (int) grid.getHeight());
    
    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    currentSnapshot = grid.snapshot(params, currentSnapshot);

    // Get pixel readers for both snapshots
    PixelReader initialReader = initialSnapshot.getPixelReader();
    PixelReader currentReader = currentSnapshot.getPixelReader();

    // Compare the pixels of the two snapshots
    int width = (int) grid.getWidth();
    int height = (int) grid.getHeight();

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // Get the color of the pixel at (x, y) in both snapshots
            Color initialColor = initialReader.getColor(x, y);
            Color currentColor = currentReader.getColor(x, y);

            // If the colors don't match, the puzzle is not solved
            if (!initialColor.equals(currentColor)) {
                return false;
            }
        }
    }

    // If all pixels match, the puzzle is solved
    return true;
}

    // Method to take a snapshot of the grid
    private void takeInitialSnapshot(GridPane grid) {
        // Create a new WritableImage with the dimensions of the grid
        initialSnapshot = new WritableImage((int) grid.getWidth(), (int) grid.getHeight());

        // Create a SnapshotParameters object to configure the snapshot
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT); // Make the background transparent

        // Take a snapshot of the grid and store it in the initialSnapshot variable
        initialSnapshot = grid.snapshot(params, initialSnapshot);
    }
    

    
     
     
}




