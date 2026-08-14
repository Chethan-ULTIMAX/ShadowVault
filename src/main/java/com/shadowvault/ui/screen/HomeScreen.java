package com.shadowvault.ui.screen;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

import com.shadowvault.controller.ImageController;

/**
 * Home screen for ShadowVault.
 * Displays main navigation options to other screens.
 */
public class HomeScreen implements Screen {

    private final VBox root;
    private final Consumer<Screen> navigationCallback;
    private final ImageController imageController;

    public HomeScreen(
            Consumer<Screen> navigationCallback,
            ImageController imageController) {

        this.navigationCallback = navigationCallback;
        this.imageController = imageController;
        this.root = createUI();
    }

    private VBox createUI() {
        VBox root = new VBox(40);

        root.setStyle(
            "-fx-background-color: #0f3460; " +
            "-fx-padding: 40; " +
            "-fx-alignment: center;"
        );

        Label title = new Label("SHADOWVAULT");
        title.setStyle(
            "-fx-text-fill: #00d4ff; " +
            "-fx-font-size: 48px; " +
            "-fx-font-weight: bold;"
        );

        Label subtitle = new Label(
            "Image Steganography & Analysis Platform"
        );

        subtitle.setStyle(
            "-fx-text-fill: #aaa; " +
            "-fx-font-size: 18px;"
        );

        VBox buttonsContainer = createButtonsContainer();

        root.getChildren().addAll(
            title,
            subtitle,
            buttonsContainer
        );

        return root;
    }

    private VBox createButtonsContainer() {

        VBox container = new VBox(15);
        container.setStyle(
            "-fx-alignment: center; -fx-padding: 20;"
        );

        HBox row1 = new HBox(20);
        row1.setStyle("-fx-alignment: center;");

        row1.getChildren().addAll(

            createNavButton(
                "Hide Data",
                200,
                () -> navigationCallback.accept(
                    new HideDataScreen(
                        navigationCallback,
                        imageController
                    )
                )
            ),

            createNavButton(
                "Extract Data",
                200,
                () -> navigationCallback.accept(
                    new ExtractDataScreen(
                        navigationCallback,
                        imageController
                    )
                )
            )
        );

        HBox row2 = new HBox(20);
        row2.setStyle("-fx-alignment: center;");

        row2.getChildren().addAll(

            createNavButton(
                "Image Analysis",
                200,
                () -> navigationCallback.accept(
                    new AnalysisScreen(
                        navigationCallback,
                        imageController
                    )
                )
            ),

            createNavButton(
                "Experiments",
                200,
                () -> navigationCallback.accept(
                    new ExperimentsScreen(
                        navigationCallback,
                        imageController
                    )
                )
            )
        );

        HBox row3 = new HBox(20);
        row3.setStyle("-fx-alignment: center;");

        row3.getChildren().addAll(

            createNavButton(
                "Settings",
                150,
                () -> navigationCallback.accept(
                    new SettingsScreen(
                        navigationCallback,
                        imageController
                    )
                )
            ),

            createNavButton(
                "About",
                150,
                () -> navigationCallback.accept(
                    new AboutScreen(
                        navigationCallback,
                        imageController
                    )
                )
            )
        );

        container.getChildren().addAll(
            row1,
            row2,
            row3
        );

        return container;
    }

    private Button createNavButton(
            String text,
            double width,
            Runnable onAction) {

        Button btn = new Button(text);

        btn.setPrefWidth(width);
        btn.setPrefHeight(60);

        btn.setStyle(
            "-fx-background-color: #1a1a2e; " +
            "-fx-text-fill: #00d4ff; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-color: #00d4ff; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 8; " +
            "-fx-padding: 15; " +
            "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #00d4ff; " +
            "-fx-text-fill: #1a1a2e; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-color: #00d4ff; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 8; " +
            "-fx-padding: 15; " +
            "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: #1a1a2e; " +
            "-fx-text-fill: #00d4ff; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-color: #00d4ff; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 8; " +
            "-fx-padding: 15; " +
            "-fx-cursor: hand;"
        ));

        btn.setOnAction(e -> onAction.run());

        return btn;
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
        return "Home";
    }
}
