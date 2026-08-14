package com.shadowvault.ui.screen;

import com.shadowvault.controller.ImageController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class AboutScreen implements Screen {

    private final BorderPane root;
    private final Consumer<Screen> navigationCallback;
    private final ImageController imageController;

    public AboutScreen(Consumer<Screen> navigationCallback, ImageController imageController) {
        this.navigationCallback = navigationCallback;
        this.imageController = imageController;
        this.root = createUI();
    }

    private BorderPane createUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f3460;");

        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: #1a1a2e; -fx-padding: 15; -fx-alignment: center-left;");
        Button backBtn = new Button("← Back to Home");
        backBtn.setStyle("-fx-background-color: #533483; -fx-text-fill: #fff; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");
        backBtn.setOnAction(e -> navigationCallback.accept(new HomeScreen(navigationCallback, imageController, null)));
        header.getChildren().add(backBtn);

        VBox content = new VBox(20);
        content.setStyle("-fx-background-color: #16213e; -fx-padding: 40; -fx-alignment: center;");
        Label title = new Label("About ShadowVault");
        title.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 32px; -fx-font-weight: bold;");

        Label info = new Label(
                "ShadowVault\n\n" +
                "Image Steganography & Analysis Platform\n\n" +
                "Version 0.1.0\n\n" +
                "Technology:\n" +
                "• Java 25\n" +
                "• JavaFX\n" +
                "• Maven\n\n" +
                "Created as a research and learning project."
        );
        info.setStyle("-fx-text-fill: #aaa; -fx-font-size: 14px; -fx-text-alignment: center;");
        info.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        content.getChildren().addAll(title, info);

        root.setTop(header);
        root.setCenter(content);

        return root;
    }

    @Override
    public Node getRoot() { return root; }
    @Override
    public void onShow() {}
    @Override
    public void onHide() {}
    @Override
    public String getTitle() { return "About"; }
}