package com.shadowvault.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileUtils {
    
    public static File chooseImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image File");
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG Files", "*.png"),
            new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg"),
            new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.jpeg")
        );
        
        return fileChooser.showOpenDialog(stage);
    }
    
    public static String getFileSizeStr(long size) {
        if (size < 1024) {
            return size + " bytes";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        }
    }
}