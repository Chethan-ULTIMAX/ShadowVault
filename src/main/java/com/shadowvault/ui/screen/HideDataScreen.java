package com.shadowvault.ui.screen;

import com.shadowvault.controller.ImageController;
import com.shadowvault.model.ImageData;
import com.shadowvault.util.ValidationUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class HideDataScreen implements Screen {

    private final BorderPane root;
    private final Consumer<Screen> navigationCallback;
    private final ImageController imageController;

    private ImageView imageView;
    private Label imageInfo;
    private Label capacityLabel;
    private TextArea messageArea;
    private Button encodeButton;
    private Button saveButton;

    public HideDataScreen(
            Consumer<Screen> navigationCallback,
            ImageController imageController) {

        this.navigationCallback = navigationCallback;
        this.imageController = imageController;
        this.root = createUI();
    }

    private BorderPane createUI() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f3460;");

        root.setTop(createHeader());

        VBox content = new VBox(15);
        content.setPadding(new Insets(25));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: #16213e;");

        Label title = new Label("Hide Data");
        title.setStyle(
            "-fx-text-fill: #00d4ff;" +
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;"
        );

        Label subtitle = new Label(
            "Hide a secret message inside an image using LSB steganography."
        );
        subtitle.setStyle(
            "-fx-text-fill: #aaaaaa;" +
            "-fx-font-size: 15px;"
        );

        Button selectButton = new Button("Select Image");
        selectButton.setPrefWidth(180);
        selectButton.setPrefHeight(40);

        selectButton.setOnAction(e -> loadImage());

        imageInfo = new Label("No image selected");
        imageInfo.setStyle(
            "-fx-text-fill: #cccccc;" +
            "-fx-font-size: 14px;"
        );

        capacityLabel = new Label("");
        capacityLabel.setStyle(
            "-fx-text-fill: #00d4ff;" +
            "-fx-font-size: 14px;"
        );

        imageView = new ImageView();
        imageView.setFitWidth(500);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        messageArea = new TextArea();
        messageArea.setPromptText("Enter the secret message...");
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(6);
        messageArea.setMaxWidth(600);

        encodeButton = new Button("Encode Message");
        encodeButton.setPrefWidth(200);
        encodeButton.setPrefHeight(45);
        encodeButton.setDisable(true);

        encodeButton.setOnAction(e -> encodeMessage());

        saveButton = new Button("Save Stego Image");
        saveButton.setPrefWidth(200);
        saveButton.setPrefHeight(45);
        saveButton.setDisable(true);

        saveButton.setOnAction(e -> saveImage());

        content.getChildren().addAll(
            title,
            subtitle,
            selectButton,
            imageInfo,
            capacityLabel,
            imageView,
            messageArea,
            encodeButton,
            saveButton
        );

        root.setCenter(content);

        return root;
    }

    private VBox createHeader() {

        VBox header = new VBox(10);

        header.setStyle(
            "-fx-background-color: #1a1a2e;" +
            "-fx-padding: 15;" +
            "-fx-alignment: center-left;"
        );

        Button backButton = new Button("← Back to Home");

        backButton.setOnAction(e ->
            navigationCallback.accept(
                new HomeScreen(navigationCallback, imageController)
            )
        );

        header.getChildren().add(backButton);

        return header;
    }

    private void loadImage() {

        javafx.stage.Window window = root.getScene().getWindow();

        if (!(window instanceof javafx.stage.Stage)) {
            return;
        }

        ImageData imageData =
            imageController.loadImage((javafx.stage.Stage) window);

        if (imageData == null) {
            return;
        }

        imageView.setImage(imageData.getImage());

        imageInfo.setText(imageData.getInfo());

        capacityLabel.setText(
            "Maximum message size: " +
            imageController.getCapacity() +
            " bytes"
        );

        encodeButton.setDisable(false);
    }

    private void encodeMessage() {

        String message = messageArea.getText();

        ValidationUtils.ValidationResult validation =
            imageController.validateEmbed(message);

        if (!validation.isValid()) {

            showAlert(
                Alert.AlertType.ERROR,
                "Cannot Encode",
                validation.getMessage()
            );

            return;
        }

        try {

            ImageData stego =
                imageController.embedMessage(message);

            imageView.setImage(stego.getImage());

            imageInfo.setText(
                "Encoded image: " +
                stego.getFileName()
            );

            saveButton.setDisable(false);

            showAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Message successfully embedded!"
            );

        } catch (Exception ex) {

            showAlert(
                Alert.AlertType.ERROR,
                "Encoding Failed",
                ex.getMessage()
            );
        }
    }

    private void saveImage() {

        if (!imageController.hasStegoImage()) {
            return;
        }

        javafx.stage.FileChooser chooser =
            new javafx.stage.FileChooser();

        chooser.setTitle("Save Stego Image");

        chooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter(
                "PNG Image",
                "*.png"
            )
        );

        java.io.File file =
            chooser.showSaveDialog(root.getScene().getWindow());

        if (file == null) {
            return;
        }

        try {

            javafx.scene.image.Image image =
                imageController.getStegoImage().getImage();

            javafx.imageio.ImageIO.write(
                javafx.embed.swing.SwingFXUtils.fromFXImage(image, null),
                "png",
                file
            );

            showAlert(
                Alert.AlertType.INFORMATION,
                "Saved",
                "Stego image saved successfully."
            );

        } catch (Exception ex) {

            showAlert(
                Alert.AlertType.ERROR,
                "Save Failed",
                ex.getMessage()
            );
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
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
        return "Hide Data";
    }
}
