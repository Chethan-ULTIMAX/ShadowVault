package com.shadowvault.ui.screen;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

/**
 * About screen - displays information about ShadowVault.
 */
public class AboutScreen implements Screen {
    
    private final BorderPane root;
    private final Consumer<Screen> navigationCallback;
    
    public AboutScreen(Consumer<Screen> navigationCallback) {
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
        
        Label title = new Label("About ShadowVault");
        title.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 32px; -fx-font-weight: bold;");
        
        Label description = new Label("Java-based Image Steganography & Analysis Platform");
        description.setStyle("-fx-text-fill: #888; -fx-font-size: 16px;");
        
        Label version = new Label("Version 0.1.0");
        version.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");
        
        Label info = new Label(
            "ShadowVault is an experimental research platform for exploring\n" +
            "image steganography, steganalysis, and related image processing\n" +
            "techniques. It provides tools for embedding, extracting, and\n" +
            "analyzing hidden data in PNG images."
        );
        info.setStyle("-fx-text-fill: #aaa; -fx-font-size: 14px; -fx-text-alignment: center;");
        
        content.getChildren().addAll(title, description, version, info);
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
        return "About";
    }
}
