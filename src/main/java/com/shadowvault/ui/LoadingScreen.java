package com.shadowvault.ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingScreen {
    
    private static Stage loadingStage;
    private static ProgressBar progressBar;
    private static Label statusText;
    
    public static void show(Stage primaryStage, Runnable onComplete) {
        loadingStage = new Stage();
        loadingStage.initStyle(StageStyle.UNDECORATED);
        
        VBox loadingContent = new VBox(20);
        loadingContent.setStyle(
            "-fx-background-color: #0f3460; " +
            "-fx-padding: 50; " +
            "-fx-alignment: center;"
        );
        
        Label title = new Label("SHADOWVAULT");
        title.setStyle(
            "-fx-text-fill: #00d4ff; " +
            "-fx-font-size: 36px; " +
            "-fx-font-weight: bold;"
        );
        
        Label subtitle = new Label("Secure Steganography Engine");
        subtitle.setStyle(
            "-fx-text-fill: #aaa; " +
            "-fx-font-size: 16px;"
        );
        
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setStyle(
            "-fx-accent: #00d4ff; " +
            "-fx-background-color: #1a1a2e; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10;"
        );
        
        statusText = new Label("Initializing modules...");
        statusText.setStyle(
            "-fx-text-fill: #888; " +
            "-fx-font-size: 14px;"
        );
        
        Label version = new Label("Version 0.1.0");
        version.setStyle(
            "-fx-text-fill: #555; " +
            "-fx-font-size: 12px;"
        );
        
        loadingContent.getChildren().addAll(
            title, subtitle, progressBar, statusText, version
        );
        
        Scene loadingScene = new Scene(loadingContent, 500, 400);
        loadingStage.setScene(loadingScene);
        loadingStage.show();
        
        startLoading(onComplete);
    }
    
    private static void startLoading(Runnable onComplete) {
        Thread loadThread = new Thread(() -> {
            try {
                String[] messages = {
                    "Loading core modules...",
                    "Loading UI resources...",
                    "Initializing steganography engine...",
                    "Loading security modules...",
                    "Ready!"
                };
                
                for (int i = 0; i < messages.length; i++) {
                    double progress = (i + 1) / (double) messages.length;
                    updateProgress(progress, messages[i]);
                    Thread.sleep(500);
                }
                
                Thread.sleep(300);
                
                javafx.application.Platform.runLater(() -> {
                    loadingStage.close();
                    onComplete.run();
                });
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        loadThread.start();
    }
    
    private static void updateProgress(double progress, String message) {
        javafx.application.Platform.runLater(() -> {
            progressBar.setProgress(progress);
            statusText.setText(message);
        });
    }
}