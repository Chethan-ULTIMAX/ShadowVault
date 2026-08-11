package com.shadowvault.controller;

import com.shadowvault.model.ImageData;
import com.shadowvault.model.MessagePayload;
import com.shadowvault.service.LSBService;
import com.shadowvault.service.PixelService;
import com.shadowvault.util.FileUtils;
import com.shadowvault.util.ValidationUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import java.io.File;

public class ImageController {
    
    private ImageData currentImageData;
    private ImageData processedImageData;
    private ImageData stegoImageData;
    private final PixelService pixelService;
    private final LSBService lsbService;
    private MessagePayload extractedPayload;
    
    public ImageController() {
        this.pixelService = new PixelService();
        this.lsbService = new LSBService();
    }
    
    // ===== IMAGE LOADING =====
    
    public ImageData loadImage(Stage stage) {
        File file = FileUtils.chooseImage(stage);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ImageData imageData = new ImageData(image, file.getName(), file.length());
            this.currentImageData = imageData;
            this.processedImageData = imageData;
            this.stegoImageData = null;
            return imageData;
        }
        return null;
    }
    
    // ===== PIXEL OPERATIONS =====
    
    public ImageData applyGrayscale() {
        if (currentImageData == null) return null;
        WritableImage processed = pixelService.toGrayscale(currentImageData);
        processedImageData = new ImageData(processed, currentImageData.getFileName(), currentImageData.getFileSize());
        return processedImageData;
    }
    
    public ImageData applyInvert() {
        if (currentImageData == null) return null;
        WritableImage processed = pixelService.toInvert(currentImageData);
        processedImageData = new ImageData(processed, currentImageData.getFileName(), currentImageData.getFileSize());
        return processedImageData;
    }
    
    public ImageData applyBrighten() {
        if (currentImageData == null) return null;
        WritableImage processed = pixelService.toBrighten(currentImageData);
        processedImageData = new ImageData(processed, currentImageData.getFileName(), currentImageData.getFileSize());
        return processedImageData;
    }
    
    public ImageData resetImage() {
        if (currentImageData == null) return null;
        processedImageData = currentImageData;
        stegoImageData = null;
        return processedImageData;
    }
    
    // ===== LSB STEGANOGRAPHY =====
    
    public ImageData embedMessage(String message) {
        if (currentImageData == null) {
            throw new IllegalStateException("No image loaded!");
        }
        
        ValidationUtils.ValidationResult validation = ValidationUtils.validateMessage(currentImageData, message);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        
        MessagePayload payload = new MessagePayload(message);
        stegoImageData = lsbService.embed(currentImageData, payload);
        processedImageData = stegoImageData;
        return stegoImageData;
    }
    
    public String extractMessage() {
        if (currentImageData == null) {
            throw new IllegalStateException("No image loaded!");
        }
        
        ValidationUtils.ValidationResult validation = ValidationUtils.validateExtraction(currentImageData);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        
        ImageData imageToExtract = stegoImageData != null ? stegoImageData : currentImageData;
        extractedPayload = lsbService.extract(imageToExtract);
        return extractedPayload.getMessage();
    }
    
    public int getCapacity() {
        if (currentImageData == null) return 0;
        return lsbService.calculateCapacity(currentImageData);
    }
    
    public boolean hasStegoImage() {
        return stegoImageData != null;
    }
    
    public ImageData getStegoImage() {
        return stegoImageData;
    }
    
    public ValidationUtils.ValidationResult validateEmbed(String message) {
        return ValidationUtils.validateMessage(currentImageData, message);
    }
    
    public ValidationUtils.ValidationResult validateExtract() {
        return ValidationUtils.validateExtraction(currentImageData);
    }
    
    // ===== OTHER =====
    
    public String getPixelInfo() {
        if (currentImageData == null) return null;
        return pixelService.getPixelInfo(currentImageData);
    }
    
    public boolean hasImage() {
        return currentImageData != null;
    }
    
    public ImageData getCurrentImage() {
        return currentImageData;
    }
    
    public ImageData getProcessedImage() {
        return processedImageData;
    }
}