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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class HideDataScreen implements Screen {

    private final BorderPane root;
    private final Consumer<Screen> navigationCallback;
    private final ImageController imageController;
    private final Stage primaryStage;

    private ImageView originalImageView;
    private ImageView stegoImageView;
    private Label fileNameLabel;
    private Label capacityLabel;
    private Label statusLabel;
    private TextArea messageArea;
    private Button embedBtn;
    private Button saveBtn;
    private Button selectImageBtn;

    public HideDataScreen(Consumer<Screen> navigationCallback, ImageController imageController, Stage primaryStage) {
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

        Label title = new Label("Hide Data");
        title.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 32px; -fx-font-weight: bold;");

        HBox imageSelectionRow = createImageSelectionRow();
        HBox imageDisplayRow = createImageDisplayRow();
        HBox infoRow = createInfoRow();
        VBox messageSection = createMessageSection();
        HBox actionRow = createActionRow();

        content.getChildren().addAll(
                title,
                imageSelectionRow,
                imageDisplayRow,
                infoRow,
                messageSection,
                actionRow
        );

        return content;
    }

    private HBox createImageSelectionRow() {
        HBox row = new HBox(15);
        row.setStyle("-fx-alignment: center;");

        selectImageBtn = new Button("📁 Select Image");
        selectImageBtn.setStyle(
                "-fx-background-color: #533483; " +
                "-fx-text-fill: #fff; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        selectImageBtn.setOnAction(e -> selectImage());

        fileNameLabel = new Label("No image selected");
        fileNameLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 13px;");

        row.getChildren().addAll(selectImageBtn, fileNameLabel);
        return row;
    }

    private HBox createImageDisplayRow() {
        HBox row = new HBox(30);
        row.setStyle("-fx-alignment: center;");

        VBox originalBox = new VBox(5);
        originalBox.setStyle("-fx-alignment: center;");
        Label originalLabel = new Label("Original");
        originalLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        originalImageView = new ImageView();
        originalImageView.setPreserveRatio(true);
        originalImageView.setFitWidth(250);
        originalImageView.setFitHeight(200);
        originalImageView.setStyle("-fx-border-color: #0f3460; -fx-border-width: 2; -fx-border-radius: 5;");

        originalBox.getChildren().addAll(originalLabel, originalImageView);

        VBox stegoBox = new VBox(5);
        stegoBox.setStyle("-fx-alignment: center;");
        Label stegoLabel = new Label("Stego Image");
        stegoLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        stegoImageView = new ImageView();
        stegoImageView.setPreserveRatio(true);
        stegoImageView.setFitWidth(250);
        stegoImageView.setFitHeight(200);
        stegoImageView.setStyle("-fx-border-color: #0f3460; -fx-border-width: 2; -fx-border-radius: 5;");

        stegoBox.getChildren().addAll(stegoLabel, stegoImageView);

        row.getChildren().addAll(originalBox, stegoBox);
        return row;
    }

    private HBox createInfoRow() {
        HBox row = new HBox(30);
        row.setStyle("-fx-alignment: center;");

        capacityLabel = new Label("Capacity: 0 bytes");
        capacityLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        row.getChildren().addAll(capacityLabel);
        return row;
    }

    private VBox createMessageSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-alignment: center;");

        Label msgLabel = new Label("Secret Message");
        msgLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 14px; -fx-font-weight: bold;");

        messageArea = new TextArea();
        messageArea.setPromptText("Type your secret message here...");
        messageArea.setStyle(
                "-fx-background-color: #0f3460; " +
                "-fx-text-fill: #fff; " +
                "-fx-prompt-text-fill: #666; " +
                "-fx-padding: 12; " +
                "-fx-font-size: 14px; " +
                "-fx-border-color: #1a1a2e; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;"
        );
        messageArea.setMaxWidth(500);
        messageArea.setPrefHeight(100);
        messageArea.setWrapText(true);

        section.getChildren().addAll(msgLabel, messageArea);
        return section;
    }

    private HBox createActionRow() {
        HBox row = new HBox(20);
        row.setStyle("-fx-alignment: center;");

        embedBtn = new Button("🔒 Embed Data");
        embedBtn.setStyle(
                "-fx-background-color: #00d4ff; " +
                "-fx-text-fill: #1a1a2e; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 35; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        embedBtn.setDisable(true);
        embedBtn.setOnAction(e -> embedMessage());

        saveBtn = new Button("💾 Save Stego Image");
        saveBtn.setStyle(
                "-fx-background-color: #2ecc71; " +
                "-fx-text-fill: #1a1a2e; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        saveBtn.setDisable(true);
        saveBtn.setOnAction(e -> saveStegoImage());

        row.getChildren().addAll(embedBtn, saveBtn);
        return row;
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
            originalImageView.setImage(imageData.getImage());
            stegoImageView.setImage(null);

            fileNameLabel.setText(imageData.getFileName());
            fileNameLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 13px;");

            int capacity = imageController.getCapacity();
            capacityLabel.setText("Capacity: " + capacity + " bytes");
            capacityLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 13px;");

            embedBtn.setDisable(false);
            saveBtn.setDisable(true);

            statusLabel.setText("✅ Image loaded: " + imageData.getFileName());
        }
    }

    private void embedMessage() {
        String message = messageArea.getText();
        if (message == null || message.trim().isEmpty()) {
            showAlert("No Message", "Please enter a secret message to hide!");
            return;
        }

        var validation = imageController.validateEmbed(message);
        if (!validation.isValid()) {
            showAlert("Validation Failed", validation.getMessage());
            return;
        }

        try {
            statusLabel.setText("⏳ Encoding message...");
            embedBtn.setDisable(true);

            imageController.embedMessage(message);
            ImageData stegoImage = imageController.getStegoImage();

            if (stegoImage != null) {
                stegoImageView.setImage(stegoImage.getImage());
                saveBtn.setDisable(false);
                statusLabel.setText("✅ Message encoded successfully!");
                showAlert("Success", "Message hidden successfully!\n\n" +
                        "Capacity: " + imageController.getCapacity() + " bytes\n" +
                        "Message: " + message.length() + " bytes");
            }

        } catch (IllegalArgumentException ex) {
            showAlert("Error", ex.getMessage());
            statusLabel.setText("❌ Error: " + ex.getMessage());
        } catch (Exception ex) {
            showAlert("Error", "Failed to encode: " + ex.getMessage());
            statusLabel.setText("❌ Failed to encode");
        } finally {
            embedBtn.setDisable(false);
        }
    }

    private void saveStegoImage() {
        ImageData stegoImage = imageController.getStegoImage();
        if (stegoImage == null) {
            showAlert("No Image", "Please embed a message first!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Stego Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
        fileChooser.setInitialFileName("stego_image.png");

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                javafx.scene.image.Image fxImage = stegoImage.getImage();
                BufferedImage bufferedImage = new BufferedImage(
                        (int) fxImage.getWidth(),
                        (int) fxImage.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );

                javafx.scene.image.PixelReader reader = fxImage.getPixelReader();
                for (int y = 0; y < fxImage.getHeight(); y++) {
                    for (int x = 0; x < fxImage.getWidth(); x++) {
                        bufferedImage.setRGB(x, y, reader.getArgb(x, y));
                    }
                }

                ImageIO.write(bufferedImage, "png", file);
                statusLabel.setText("✅ Image saved: " + file.getName());
                showAlert("Success", "Stego image saved successfully!\n\n" +
                        "Location: " + file.getAbsolutePath());

            } catch (Exception e) {
                showAlert("Error", "Failed to save image: " + e.getMessage());
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
    public Node getRoot() {
        return root;
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
    }

    @Override
    public String getTitle() {
        return "Hide Data";
    }
}