package com.shadowvault;

import com.shadowvault.ui.LoadingScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        LoadingScreen.show(stage, () -> {
            com.shadowvault.ui.MainWindow.show(stage);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}