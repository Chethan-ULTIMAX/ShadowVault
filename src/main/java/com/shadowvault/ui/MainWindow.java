package com.shadowvault.ui;

import com.shadowvault.controller.ImageController;
import com.shadowvault.model.ImageData;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {

    private static Stage stage;
    private static ImageController imageController;
    private static ImageView originalImageView;
    private static ImageView processedImageView;
    private static Label fileNameLabel;
    private static Label imageInfoLabel;
    private static Label capacityLabel;
    private static Label statusLabel;
    private static TextField messageField;

    public static void show(Stage primaryStage) {
        stage = primaryStage;
        imageController = new ImageController();

        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createCenter());
        root.setBottom(createFooter());
        root.setStyle("-fx-background-color: #0f3460;");

        Scene scene = new Scene(root, 950, 750);
        stage.setTitle("ShadowVault");
        stage.setScene(scene);
        stage.show();
    }

    private static HBox createHeader() {
        HBox header = new HBox(10);
        header.setStyle("-fx-background-color: #1a1a2e; -fx-padding: 15; -fx-alignment: center-left;");

        Label title = new Label("SHADOWVAULT");
        title.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label version = new Label("v0.1");
        version.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        header.getChildren().addAll(title, version);
        return header;
    }

    private static VBox createCenter() {
        VBox center = new VBox(15);
        center.setStyle("-fx-alignment: top-center; -fx-padding: 20; -fx-background-color: #16213e;");

        Label subtitle = new Label("Image Steganography Engine");
        subtitle.setStyle("-fx-text-fill: #aaa; -fx-font-size: 18px;");

        HBox imageRow = createImageRow();

        imageInfoLabel = new Label("No image loaded");
        imageInfoLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        capacityLabel = new Label("Capacity: 0 bytes");
        capacityLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        HBox fileRow = createFileRow();
        HBox operationRow = createOperationRow();
        Button pixelInfoBtn = createPixelInfoButton();
        messageField = createMessageField();
        HBox encodeDecodeRow = createEncodeDecodeRow();

        center.getChildren().addAll(
                subtitle, imageRow, imageInfoLabel, capacityLabel, fileRow,
                operationRow, pixelInfoBtn, messageField, encodeDecodeRow
        );

        return center;
    }

    private static HBox createImageRow() {
        HBox imageRow = new HBox(30);
        imageRow.setStyle("-fx-alignment: center;");

        VBox originalBox = new VBox(5);
        originalBox.setStyle("-fx-alignment: center;");
        Label originalLabel = new Label("Original");
        originalLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        originalImageView = new ImageView();
        originalImageView.setPreserveRatio(true);
        originalImageView.setFitWidth(300);
        originalImageView.setFitHeight(200);
        originalImageView.setStyle("-fx-border-color: #0f3460; -fx-border-width: 2; -fx-border-radius: 5;");

        originalBox.getChildren().addAll(originalLabel, originalImageView);

        VBox processedBox = new VBox(5);
        processedBox.setStyle("-fx-alignment: center;");
        Label processedLabel = new Label("Processed / Stego");
        processedLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        processedImageView = new ImageView();
        processedImageView.setPreserveRatio(true);
        processedImageView.setFitWidth(300);
        processedImageView.setFitHeight(200);
        processedImageView.setStyle("-fx-border-color: #0f3460; -fx-border-width: 2; -fx-border-radius: 5;");

        processedBox.getChildren().addAll(processedLabel, processedImageView);

        imageRow.getChildren().addAll(originalBox, processedBox);
        return imageRow;
    }

    private static HBox createFileRow() {
        HBox fileRow = new HBox(15);
        fileRow.setStyle("-fx-alignment: center;");

        Button chooseBtn = new Button("📁 Choose Image");
        chooseBtn.setStyle(
                "-fx-background-color: #533483; " +
                        "-fx-text-fill: #fff; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 12 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );

        chooseBtn.setOnAction(e -> {
            ImageData imageData = imageController.loadImage(stage);
            if (imageData != null) {
                originalImageView.setImage(imageData.getImage());
                processedImageView.setImage(imageData.getImage());
                fileNameLabel.setText(imageData.getFileName());
                fileNameLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 13px;");
                imageInfoLabel.setText(imageData.getInfo());
                imageInfoLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 13px;");
                capacityLabel.setText("Capacity: " + imageController.getCapacity() + " bytes");
                capacityLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 13px;");
                statusLabel.setText("✅ Loaded: " + imageData.getFileName());
            }
        });

        fileNameLabel = new Label("No file selected");
        fileNameLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 13px;");

        fileRow.getChildren().addAll(chooseBtn, fileNameLabel);
        return fileRow;
    }

    private static HBox createOperationRow() {
        HBox operationRow = new HBox(15);
        operationRow.setStyle("-fx-alignment: center;");

        Button grayscaleBtn = createOperationButton("Grayscale", "#3498db");
        grayscaleBtn.setOnAction(e -> {
            ImageData result = imageController.applyGrayscale();
            if (result != null) {
                processedImageView.setImage(result.getImage());
                statusLabel.setText("✅ Applied grayscale filter");
            }
        });

        Button invertBtn = createOperationButton("Invert Colors", "#e67e22");
        invertBtn.setOnAction(e -> {
            ImageData result = imageController.applyInvert();
            if (result != null) {
                processedImageView.setImage(result.getImage());
                statusLabel.setText("✅ Applied invert colors filter");
            }
        });

        Button brightenBtn = createOperationButton("Brighten", "#2ecc71");
        brightenBtn.setOnAction(e -> {
            ImageData result = imageController.applyBrighten();
            if (result != null) {
                processedImageView.setImage(result.getImage());
                statusLabel.setText("✅ Applied brighten filter");
            }
        });

        Button resetBtn = createOperationButton("Reset", "#e74c3c");
        resetBtn.setOnAction(e -> {
            ImageData result = imageController.resetImage();
            if (result != null) {
                processedImageView.setImage(result.getImage());
                statusLabel.setText("✅ Reset to original image");
            }
        });

        operationRow.getChildren().addAll(grayscaleBtn, invertBtn, brightenBtn, resetBtn);
        return operationRow;
    }

    private static Button createOperationButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: #fff; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        return btn;
    }

    private static Button createPixelInfoButton() {
        Button btn = new Button("Show Pixel Info");
        btn.setStyle(
                "-fx-background-color: #9b59b6; " +
                        "-fx-text-fill: #fff; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        btn.setOnAction(e -> {
            String info = imageController.getPixelInfo();
            if (info != null) {
                statusLabel.setText("📊 " + info);
            }
        });
        return btn;
    }

    private static TextField createMessageField() {
        TextField field = new TextField();
        field.setPromptText("Enter your secret message...");
        field.setStyle(
                "-fx-background-color: #0f3460; " +
                        "-fx-text-fill: #fff; " +
                        "-fx-prompt-text-fill: #666; " +
                        "-fx-padding: 12; " +
                        "-fx-font-size: 14px; " +
                        "-fx-border-color: #1a1a2e; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );
        field.setMaxWidth(400);
        return field;
    }

    private static HBox createEncodeDecodeRow() {
        HBox row = new HBox(20);
        row.setStyle("-fx-alignment: center;");

        Button encodeBtn = new Button("ENCODE");
        encodeBtn.setStyle(
                "-fx-background-color: #00d4ff; " +
                        "-fx-text-fill: #1a1a2e; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 12 35; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        encodeBtn.setOnAction(e -> {
            if (!imageController.hasImage()) {
                showError("No Image", "Please load an image first!");
                return;
            }

            String message = messageField.getText();
            if (message == null || message.isEmpty()) {
                showError("No Message", "Please enter a message to hide!");
                return;
            }

            // Validate first
            var validation = imageController.validateEmbed(message);
            if (!validation.isValid()) {
                showError("Validation Failed", validation.getMessage());
                return;
            }

            try {
                // Show progress in status
                statusLabel.setText("⏳ Encoding message...");
                
                // Actually encode
                imageController.embedMessage(message);
                processedImageView.setImage(imageController.getStegoImage().getImage());
                
                statusLabel.setText("✅ Message encoded successfully!");
                showAlert("Success", "Message hidden successfully!\n\n" +
                    "Capacity: " + imageController.getCapacity() + " bytes\n" +
                    "Message: " + message.length() + " bytes\n" +
                    "Image saved as stego image.");
                    
            } catch (IllegalArgumentException ex) {
                showError("Error", ex.getMessage());
                statusLabel.setText("❌ Error: " + ex.getMessage());
            } catch (Exception ex) {
                showError("Error", "Failed to encode: " + ex.getMessage());
                statusLabel.setText("❌ Failed to encode");
            }
        });

        Button decodeBtn = new Button("DECODE");
        decodeBtn.setStyle(
                "-fx-background-color: #e94560; " +
                        "-fx-text-fill: #fff; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 12 35; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        decodeBtn.setOnAction(e -> {
            if (!imageController.hasImage()) {
                showError("No Image", "Please load an image first!");
                return;
            }

            // Validate extraction
            var validation = imageController.validateExtract();
            if (!validation.isValid()) {
                showError("Validation Failed", validation.getMessage());
                return;
            }

            try {
                statusLabel.setText("⏳ Extracting message...");
                String extracted = imageController.extractMessage();
                
                if (extracted != null && !extracted.isEmpty()) {
                    messageField.setText(extracted);
                    statusLabel.setText("✅ Message extracted: " + extracted);
                    showAlert("Message Extracted", "Hidden message:\n\n" + extracted);
                } else {
                    showAlert("No Message", "No hidden message found in this image.");
                    statusLabel.setText("ℹ️ No message found");
                }
            } catch (Exception ex) {
                showError("Error", "Failed to extract: " + ex.getMessage());
                statusLabel.setText("❌ Failed to extract");
            }
        });

        row.getChildren().addAll(encodeBtn, decodeBtn);
        return row;
    }

    private static HBox createFooter() {
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #1a1a2e; -fx-padding: 10 15; -fx-alignment: center-right;");
        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        footer.getChildren().add(statusLabel);
        return footer;
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}