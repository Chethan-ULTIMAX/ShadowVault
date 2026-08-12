package com.shadowvault.ui;

import com.shadowvault.controller.ImageController;
import com.shadowvault.ui.screen.HomeScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow {

    public static void show(Stage primaryStage) {

        // One shared controller for the whole application
        ImageController imageController = new ImageController();

        NavigationManager navigationManager = new NavigationManager();

        navigationManager.navigateTo(
            new HomeScreen(
                navigationManager::navigateTo,
                imageController
            )
        );

        Scene scene = new Scene(
            navigationManager.getRoot(),
            1000,
            800
        );

        primaryStage.setTitle("ShadowVault");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
