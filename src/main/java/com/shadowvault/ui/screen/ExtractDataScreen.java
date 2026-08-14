package com.shadowvault.ui.screen;

import com.shadowvault.controller.ImageController;
import com.shadowvault.model.ImageData;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class ExtractDataScreen implements Screen {

    private final BorderPane root;
    private final Consumer<Screen> navigationCallback;
    private final ImageController imageController;
    private final Stage primaryStage;

    private ImageView imageView;
    private Label fileNameLabel;
    private Label statusLabel;
    private TextArea resultArea;
    private Button extractBtn;
    private Button saveBtn;

    public ExtractDataScreen(Consumer<Screen> navigationCallback, ImageController imageController, Stage primaryStage) {
        this.navigationCallback = navigationCallback;
        this.imageController = imageController;
        this.primaryStage = primaryStage;
        this.root = createUI();
    }

    private BorderPane createUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f3460;");

        root.setTop(createHeader());
        root.setCenter(createContent());
        root.setBottom(createFooter());

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
        backBtn.setOnAction(e -> navigationCallback.accept(new HomeScreen(navigationCallback, imageController, primaryStage)));

        header.getChildren().add(backBtn);
        return header;
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setStyle(
                "-fx-background-color: #16213e; " +
                "-fx-padding: 30; " +
                "-fx-alignment: top-center;"
        );

        Label title = new Label("Extract Data");
        title.setStyle("-fx-text-fill: #e94560; -fx-font-size: 32px; -fx-font-weight: bold;");

        HBox imageSelectionRow = createImageSelectionRow();
        HBox imageDisplayRow = createImageDisplayRow();
        Button extractButton = createExtractButton();
        VBox resultSection = createResultSection();

        content.getChildren().addAll(
                title,
                imageSelectionRow,
                imageDisplayRow,
                extractButton,
                resultSection
        );

        return content;
    }

    private HBox createImageSelectionRow() {
        HBox row = new HBox(15);
        row.setStyle("-fx-alignment: center;");

        Button selectBtn = new Button("📁 Select Stego Image");
        selectBtn.setStyle(
                "-fx-background-color: #533483; " +
                "-fx-text-fill: #fff; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        selectBtn.setOnAction(e -> selectImage());

        fileNameLabel = new Label("No image selected");
        fileNameLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 13px;");

        row.getChildren().addAll(selectBtn, fileNameLabel);
        return row;
    }

    private HBox createImageDisplayRow() {
        HBox row = new HBox(30);
        row.setStyle("-fx-alignment: center;");

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(300);
        imageView.setFitHeight(250);
        imageView.setStyle("-fx-border-color: #0f3460; -fx-border-width: 2; -fx-border-radius: 5;");

        row.getChildren().add(imageView);
        return row;
    }

    private Button createExtractButton() {
        extractBtn = new Button("🔓 Extract Data");
        extractBtn.setStyle(
                "-fx-background-color: #e94560; " +
                "-fx-text-fill: #fff; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 35; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        extractBtn.setDisable(true);
        extractBtn.setOnAction(e -> extractMessage());

        return extractBtn;
    }

    private VBox createResultSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-alignment: center;");

        Label resultLabel = new Label("Extracted Data");
        resultLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 14px; -fx-font-weight: bold;");

        resultArea = new TextArea();
        resultArea.setPromptText("Extracted message will appear here...");
        resultArea.setStyle(
                "-fx-background-color: #0f3460; " +
                "-fx-text-fill: #00d4ff; " +
                "-fx-prompt-text-fill: #666; " +
                "-fx-padding: 12; " +
                "-fx-font-size: 14px; " +
                "-fx-border-color: #1a1a2e; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;"
        );
        resultArea.setMaxWidth(500);
        resultArea.setPrefHeight(150);
        resultArea.setWrapText(true);
        resultArea.setEditable(false);

        // Save button row
        HBox buttonRow = new HBox(15);
        buttonRow.setStyle("-fx-alignment: center;");

        saveBtn = new Button("💾 Save Message");
        saveBtn.setStyle(
                "-fx-background-color: #2ecc71; " +
                "-fx-text-fill: #1a1a2e; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10 25; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        saveBtn.setDisable(true);
        saveBtn.setOnAction(e -> saveExtractedMessage());

        buttonRow.getChildren().add(saveBtn);

        section.getChildren().addAll(resultLabel, resultArea, buttonRow);
        return section;
    }

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #1a1a2e; -fx-padding: 10 15; -fx-alignment: center-right;");

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        footer.getChildren().add(statusLabel);

        return footer;
    }

    private void selectImage() {
        ImageData imageData = imageController.loadImage(primaryStage);
        if (imageData != null) {
            imageView.setImage(imageData.getImage());

            fileNameLabel.setText(imageData.getFileName());
            fileNameLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 13px;");

            extractBtn.setDisable(false);
            saveBtn.setDisable(true);
            resultArea.clear();

            statusLabel.setText("✅ Image loaded: " + imageData.getFileName());
        }
    }

    private void extractMessage() {
        try {
            statusLabel.setText("⏳ Extracting message...");
            extractBtn.setDisable(true);

            String extracted = imageController.extractMessage();

            if (extracted != null && !extracted.isEmpty()) {
                resultArea.setText(extracted);
                saveBtn.setDisable(false);
                statusLabel.setText("✅ Message extracted successfully!");
                showAlert("Success", "Message extracted successfully!\n\n" + extracted);
            } else {
                resultArea.setText("No hidden message found in this image.");
                statusLabel.setText("ℹ️ No message found");
                showAlert("No Message", "No hidden message found in this image.");
            }

        } catch (Exception ex) {
            showAlert("Error", "Failed to extract: " + ex.getMessage());
            statusLabel.setText("❌ Failed to extract");
        } finally {
            extractBtn.setDisable(false);
        }
    }

    private void saveExtractedMessage() {
        String message = resultArea.getText();
        if (message == null || message.isEmpty()) {
            showAlert("No Message", "No extracted message to save!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Extracted Message");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName("extracted_message.txt");

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                java.nio.file.Files.write(file.toPath(), message.getBytes());
                statusLabel.setText("✅ Message saved to: " + file.getName());
                showAlert("Success", "Message saved successfully!\n\nLocation: " + file.getAbsolutePath());
            } catch (Exception ex) {
                showAlert("Error", "Failed to save: " + ex.getMessage());
                statusLabel.setText("❌ Failed to save");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public Node getRoot() { return root; }

    @Override
    public void onShow() {}

    @Override
    public void onHide() {}

    @Override
    public String getTitle() { return "Extract Data"; }
}