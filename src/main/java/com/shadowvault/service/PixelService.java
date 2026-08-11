package com.shadowvault.service;

import com.shadowvault.model.ImageData;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class PixelService {
    
    public WritableImage toGrayscale(ImageData imageData) {
        int width = imageData.getWidth();
        int height = imageData.getHeight();
        int[] pixels = imageData.getPixelData();
        
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int alpha = (pixel >> 24) & 0xFF;
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = pixel & 0xFF;
            
            int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
            int newPixel = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
            
            int x = i % width;
            int y = i / width;
            writer.setArgb(x, y, newPixel);
        }
        
        return result;
    }
    
    public WritableImage toInvert(ImageData imageData) {
        int width = imageData.getWidth();
        int height = imageData.getHeight();
        int[] pixels = imageData.getPixelData();
        
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int alpha = (pixel >> 24) & 0xFF;
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = pixel & 0xFF;
            
            int newPixel = (alpha << 24) | ((255 - red) << 16) | ((255 - green) << 8) | (255 - blue);
            
            int x = i % width;
            int y = i / width;
            writer.setArgb(x, y, newPixel);
        }
        
        return result;
    }
    
    public WritableImage toBrighten(ImageData imageData) {
        int width = imageData.getWidth();
        int height = imageData.getHeight();
        int[] pixels = imageData.getPixelData();
        int brightness = 50;
        
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int alpha = (pixel >> 24) & 0xFF;
            int red = Math.min(255, ((pixel >> 16) & 0xFF) + brightness);
            int green = Math.min(255, ((pixel >> 8) & 0xFF) + brightness);
            int blue = Math.min(255, (pixel & 0xFF) + brightness);
            
            int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
            
            int x = i % width;
            int y = i / width;
            writer.setArgb(x, y, newPixel);
        }
        
        return result;
    }
    
    public String getPixelInfo(ImageData imageData) {
        int[] pixels = imageData.getPixelData();
        if (pixels.length == 0) return "No pixel data";
        
        int tlPixel = pixels[0];
        int centerPixel = pixels[(pixels.length / 2)];
        int brPixel = pixels[pixels.length - 1];
        
        return String.format(
            "TL: A=%d, R=%d, G=%d, B=%d | Center: A=%d, R=%d, G=%d, B=%d | BR: A=%d, R=%d, G=%d, B=%d",
            (tlPixel >> 24) & 0xFF, (tlPixel >> 16) & 0xFF, (tlPixel >> 8) & 0xFF, tlPixel & 0xFF,
            (centerPixel >> 24) & 0xFF, (centerPixel >> 16) & 0xFF, (centerPixel >> 8) & 0xFF, centerPixel & 0xFF,
            (brPixel >> 24) & 0xFF, (brPixel >> 16) & 0xFF, (brPixel >> 8) & 0xFF, brPixel & 0xFF
        );
    }
}