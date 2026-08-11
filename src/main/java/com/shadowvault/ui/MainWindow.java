package com.shadowvault.ui;

import com.shadowvault.ui.screen.HomeScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainWindow is the entry point for the ShadowVault application UI.
 * It sets up the navigation manager and displays the home screen.
 */
public class MainWindow {

    public static void show(Stage primaryStage) {
        // Create navigation manager
        NavigationManager navigationManager = new NavigationManager();
        
        // Display the home screen
        navigationManager.navigateTo(new HomeScreen(navigationManager::navigateTo));
        
        // Create scene with the navigation manager's root
        Scene scene = new Scene(navigationManager.getRoot(), 1000, 800);
        primaryStage.setTitle("ShadowVault");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
