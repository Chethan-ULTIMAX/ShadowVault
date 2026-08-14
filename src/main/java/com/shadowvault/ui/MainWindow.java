package com.shadowvault.ui;

import com.shadowvault.controller.ImageController;
import com.shadowvault.ui.screen.HomeScreen;
import com.shadowvault.ui.screen.Screen;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow {
    
    private static Stage stage;
    private static ImageController imageController;
    private static Screen currentScreen;
    private static BorderPane root;
    
    public static void show(Stage primaryStage) {
        stage = primaryStage;
        imageController = new ImageController();
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0f3460;");
        
        // Show Home screen
        navigateTo(new HomeScreen(MainWindow::navigateTo, imageController, stage));
        
        Scene scene = new Scene(root, 950, 750);
        stage.setTitle("ShadowVault");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void navigateTo(Screen screen) {
        if (currentScreen != null) {
            currentScreen.onHide();
        }
        currentScreen = screen;
        currentScreen.onShow();
        root.setCenter(screen.getRoot());
        stage.setTitle("ShadowVault - " + screen.getTitle());
    }
}