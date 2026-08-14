package com.shadowvault.ui.screen;

import com.shadowvault.controller.ImageController;
import com.shadowvault.model.ImageData;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class AnalysisScreen implements Screen {

    private final BorderPane root;
    private final Consumer<Screen> navigationCallback;
    private final ImageController imageController;
    private final Stage primaryStage;

    // UI Components
    private ImageView originalImageView;
    private ImageView stegoImageView;
    private Label fileNameLabel;
    private Label statusLabel;
    private Label mseLabel;
    private Label psnrLabel;
    private Label ssimLabel;
    private Label qualityLabel;
    private Button analyzeBtn;
    private Button loadStegoBtn;

    public AnalysisScreen(Consumer<Screen> navigationCallback, ImageController imageController, Stage primaryStage) {
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

        Label title = new Label("Image Analysis");
        title.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 32px; -fx-font-weight: bold;");

        // Image selection row
        HBox selectionRow = createSelectionRow();

        // Image display (Original + Stego side by side)
        HBox imageDisplayRow = createImageDisplayRow();

        // Metrics display
        VBox metricsSection = createMetricsSection();

        // Analyze button
        HBox actionRow = createActionRow();

        content.getChildren().addAll(
                title,
                selectionRow,
                imageDisplayRow,
                metricsSection,
                actionRow
        );

        return content;
    }

    private HBox createSelectionRow() {
        HBox row = new HBox(15);
        row.setStyle("-fx-alignment: center;");

        Button loadOriginalBtn = new Button("📁 Load Original Image");
        loadOriginalBtn.setStyle(
                "-fx-background-color: #533483; " +
                "-fx-text-fill: #fff; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        loadOriginalBtn.setOnAction(e -> loadOriginalImage());

        loadStegoBtn = new Button("📁 Load Stego Image");
        loadStegoBtn.setStyle(
                "-fx-background-color: #2980b9; " +
                "-fx-text-fill: #fff; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 25; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        loadStegoBtn.setDisable(true);

        fileNameLabel = new Label("No images loaded");
        fileNameLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 13px;");

        row.getChildren().addAll(loadOriginalBtn, loadStegoBtn, fileNameLabel);
        return row;
    }

    private HBox createImageDisplayRow() {
        HBox row = new HBox(30);
        row.setStyle("-fx-alignment: center;");

        // Original Image
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

        // Stego Image
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

    private VBox createMetricsSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-alignment: center; -fx-padding: 10;");

        Label metricsTitle = new Label("Quality Metrics");
        metricsTitle.setStyle("-fx-text-fill: #aaa; -fx-font-size: 16px; -fx-font-weight: bold;");

        HBox metricsRow = new HBox(30);
        metricsRow.setStyle("-fx-alignment: center;");

        mseLabel = new Label("MSE: --");
        mseLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");

        psnrLabel = new Label("PSNR: -- dB");
        psnrLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");

        ssimLabel = new Label("SSIM: --");
        ssimLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");

        qualityLabel = new Label("Quality: --");
        qualityLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");

        metricsRow.getChildren().addAll(mseLabel, psnrLabel, ssimLabel, qualityLabel);

        section.getChildren().addAll(metricsTitle, metricsRow);
        return section;
    }

    private HBox createActionRow() {
        HBox row = new HBox(20);
        row.setStyle("-fx-alignment: center;");

        analyzeBtn = new Button("📊 Analyze Images");
        analyzeBtn.setStyle(
                "-fx-background-color: #f39c12; " +
                "-fx-text-fill: #1a1a2e; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 35; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
        );
        analyzeBtn.setDisable(true);
        analyzeBtn.setOnAction(e -> analyzeImages());

        row.getChildren().add(analyzeBtn);
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

    // ===== ACTION METHODS =====

    private void loadOriginalImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Original Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            ImageData imageData = imageController.loadImage(primaryStage);
            if (imageData != null) {
                originalImageView.setImage(imageData.getImage());
                loadStegoBtn.setDisable(false);
                statusLabel.setText("✅ Original image loaded: " + file.getName());
                updateFileNameLabel();
            }
        }
    }

    private void loadStegoImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Stego Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            ImageData imageData = imageController.loadImage(primaryStage);
            if (imageData != null) {
                stegoImageView.setImage(imageData.getImage());
                // Store as stego image
                analyzeBtn.setDisable(false);
                statusLabel.setText("✅ Stego image loaded: " + file.getName());
                updateFileNameLabel();
            }
        }
    }

    private void updateFileNameLabel() {
        String original = originalImageView.getImage() != null ? "✓ Original" : "✗ Original";
        String stego = stegoImageView.getImage() != null ? "✓ Stego" : "✗ Stego";
        fileNameLabel.setText(original + " | " + stego);
        fileNameLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 13px;");
    }

    private void analyzeImages() {
        if (originalImageView.getImage() == null || stegoImageView.getImage() == null) {
            showAlert("Error", "Please load both original and stego images!");
            return;
        }

        try {
            statusLabel.setText("⏳ Analyzing images...");
            analyzeBtn.setDisable(true);

            // Calculate metrics
            double mse = imageController.calculateMSE();
            double psnr = imageController.calculatePSNR();
            double ssim = imageController.calculateSSIM();

            // Update labels
            mseLabel.setText(String.format("MSE: %.4f", mse));
            mseLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 14px;");

            psnrLabel.setText(String.format("PSNR: %.2f dB", psnr));
            psnrLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 14px;");

            ssimLabel.setText(String.format("SSIM: %.4f", ssim));
            ssimLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 14px;");

            // Quality assessment
            String quality;
            String color;
            if (psnr > 40) {
                quality = "Excellent";
                color = "#2ecc71";
            } else if (psnr > 30) {
                quality = "Good";
                color = "#f39c12";
            } else if (psnr > 20) {
                quality = "Fair";
                color = "#e67e22";
            } else {
                quality = "Poor";
                color = "#e74c3c";
            }

            qualityLabel.setText("Quality: " + quality);
            qualityLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-weight: bold;");

            statusLabel.setText("✅ Analysis complete!");

            showAlert("Analysis Complete",
                    "Image Quality Analysis Results:\n\n" +
                    "MSE:  " + String.format("%.4f", mse) + "\n" +
                    "PSNR: " + String.format("%.2f dB", psnr) + "\n" +
                    "SSIM: " + String.format("%.4f", ssim) + "\n\n" +
                    "Overall Quality: " + quality);

        } catch (Exception ex) {
            showAlert("Error", "Failed to analyze: " + ex.getMessage());
            statusLabel.setText("❌ Failed to analyze");
        } finally {
            analyzeBtn.setDisable(false);
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
    public String getTitle() { return "Image Analysis"; }
}