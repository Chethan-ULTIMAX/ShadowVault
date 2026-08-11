package com.shadowvault.ui;

import com.shadowvault.ui.screen.Screen;
import javafx.scene.layout.StackPane;

/**
 * NavigationManager handles switching between screens in the ShadowVault application.
 * Uses a StackPane to display one screen at a time.
 */
public class NavigationManager {
    
    private final StackPane root;
    private Screen currentScreen;
    
    public NavigationManager() {
        this.root = new StackPane();
        this.root.setStyle("-fx-background-color: #0f3460;");
    }
    
    /**
     * Navigate to a new screen.
     * Hides the current screen and shows the new one.
     */
    public void navigateTo(Screen screen) {
        if (currentScreen != null) {
            currentScreen.onHide();
            root.getChildren().remove(currentScreen.getRoot());
        }
        
        currentScreen = screen;
        screen.onShow();
        root.getChildren().add(screen.getRoot());
    }
    
    /**
     * Get the root StackPane for adding to the scene.
     */
    public StackPane getRoot() {
        return root;
    }
    
    /**
     * Get the currently displayed screen.
     */
    public Screen getCurrentScreen() {
        return currentScreen;
    }
}
