package murray.csc325sprint1.ViewModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import murray.csc325sprint1.Model.ViewPaths;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class InitScreenController {

    private static InitScreenController instance;

    public InitScreenController() {
        if (instance != null) {
            throw new IllegalStateException("Only one InitScreenController allowed");
        }
        instance = this;
    }

    public static InitScreenController getInstance() {
        return instance;
    }

    @FXML
    private ImageView imageViewMainMenu;

    @FXML
    BorderPane initScreenBorderPane;

    private final List<Image> images = new ArrayList<>();
    private int currentIndex = 0;

//    @FXML
//    public void initialize() {
//        try {
//            // Load the default pane from the FXML resource
//            GridPane defaultPane = FXMLLoader.load(getClass().getResource("csc325socialmediaapp/inital-screen.fxml"));
//            // Set the loaded pane into the center of the BorderPane
//            initScreenBorderPane.setCenter(defaultPane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        loadImagesFromFolder();
//        startImageSlideshow();
//    }

    private void loadImagesFromFolder() {
        try {
            URL imagesFolder = getClass().getResource("");
            if (imagesFolder != null) {
                String path = imagesFolder.getPath();
                var folder = new java.io.File(path);
                for (java.io.File file : Objects.requireNonNull(folder.listFiles())) {
                    if (file.getName().matches(".*\\.(png|jpg|jpeg|gif)")) {
                        images.add(new Image(file.toURI().toString()));
                    }
                }
            } else {
                System.err.println("Images folder not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void clearContent(){
        initScreenBorderPane.setTop(null);
        initScreenBorderPane.setRight(null);
        initScreenBorderPane.setCenter(null);
        initScreenBorderPane.setLeft(null);
        initScreenBorderPane.setBottom(null);
    }

    private void startImageSlideshow() {
        if (images.isEmpty()) return;
        imageViewMainMenu.setImage(images.get(0));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            currentIndex = (currentIndex + 1) % images.size();
            imageViewMainMenu.setImage(images.get(currentIndex));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void loginBtnClicked(ActionEvent event) {
        try {
            GridPane signInGridPane = FXMLLoader.load(getClass().getResource(murray.csc325sprint1.Model.ViewPaths.LOGIN_SCREEN));
            clearContent();
            initScreenBorderPane.setCenter(signInGridPane);
            resizeBorderPaneToFitContent(signInGridPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signUpBtnClicked(ActionEvent event) {
        try {
            GridPane signUpGridPane = FXMLLoader.load(getClass().getResource(murray.csc325sprint1.Model.ViewPaths.CREATE_USER_SCREEN));
            clearContent();
            initScreenBorderPane.setLeft(signUpGridPane);
            resizeBorderPaneToFitContent(signUpGridPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void resizeBorderPaneToFitContent(GridPane gridPane) {
        // Adjust the size of the BorderPane to fit the width and height of the content
        initScreenBorderPane.setPrefWidth(initScreenBorderPane.getWidth() + gridPane.getWidth());
        initScreenBorderPane.setPrefHeight(Math.max(initScreenBorderPane.getHeight(), gridPane.getHeight()));
    }

}
