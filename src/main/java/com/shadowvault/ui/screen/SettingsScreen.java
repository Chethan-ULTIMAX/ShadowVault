package com.shadowvault.ui.screen;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

/**
 * Settings screen - placeholder for V1.
 * Allows users to configure application preferences.
 */
public class SettingsScreen implements Screen {
    
    private final BorderPane root;
    private final Consumer<Screen> navigationCallback;
    
    public SettingsScreen(Consumer<Screen> navigationCallback) {
        this.navigationCallback = navigationCallback;
        this.root = createUI();
    }
    
    private BorderPane createUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f3460;");
        
        root.setTop(createHeader());
        
        VBox content = new VBox(20);
        content.setStyle(
            "-fx-background-color: #16213e; " +
            "-fx-padding: 40; " +
            "-fx-alignment: top-center;"
        );
        
        Label title = new Label("Settings");
        title.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 32px; -fx-font-weight: bold;");
        
        Label placeholder = new Label("Feature coming in future phase...");
        placeholder.setStyle("-fx-text-fill: #888; -fx-font-size: 18px;");
        
        content.getChildren().addAll(title, placeholder);
        root.setCenter(content);
        
        return root;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setStyle(
            "-fx-background-color: #1a1a2e; " +
            "-fx-padding: 15; " +
            "-fx-alignment: center-left;"
        );
        
        Button backBtn = new Button("← Back to Home");
        backBtn.setStyle(
            "-fx-background-color: #533483; " +
            "-fx-text-fill: #fff; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> navigationCallback.accept(new HomeScreen(navigationCallback)));
        
        header.getChildren().add(backBtn);
        return header;
    }
    
    @Override
    public Node getRoot() {
        return root;
    }
    
    @Override
    public void onShow() {}
    
    @Override
    public void onHide() {}
    
    @Override
    public String getTitle() {
        return "Settings";
    }
}
