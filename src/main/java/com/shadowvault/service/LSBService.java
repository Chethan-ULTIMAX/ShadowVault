package com.shadowvault.service;

import com.shadowvault.model.ImageData;
import com.shadowvault.model.MessagePayload;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.nio.charset.StandardCharsets;

public class LSBService {
    
    /**
     * Embed a message into an image using LSB steganography.
     * Each embedded message includes a terminator byte (0x00) to mark the end.
     * Therefore, capacity required = (message bytes + 1 terminator byte) * 8 bits.
     */
    public ImageData embed(ImageData imageData, MessagePayload payload) {
        // Check if message fits in image (accounting for terminator byte)
        int maxBits = imageData.getWidth() * imageData.getHeight();
        byte[] messageBytes = payload.getMessageBytes();
        int requiredBits = (messageBytes.length + 1) * 8; // +1 for terminator
        
        if (requiredBits > maxBits) {
            throw new IllegalArgumentException(
                "Message too large! Need " + requiredBits + 
                " bits (" + messageBytes.length + " message + 1 terminator byte) but image can hold " + maxBits + " bits"
            );
        }
        
        // Get pixel data
        int[] pixels = imageData.getPixelData().clone();
        int width = imageData.getWidth();
        int height = imageData.getHeight();
        
        // Add terminator (8 zeros) to mark end of message
        byte[] dataWithTerminator = new byte[messageBytes.length + 1];
        System.arraycopy(messageBytes, 0, dataWithTerminator, 0, messageBytes.length);
        dataWithTerminator[messageBytes.length] = 0; // terminator
        
        // Embed each bit (1 bit per pixel in the LSB of blue channel)
        int bitIndex = 0;
        for (int i = 0; i < dataWithTerminator.length && bitIndex < pixels.length; i++) {
            byte b = dataWithTerminator[i];
            for (int bitPos = 7; bitPos >= 0; bitPos--) {
                int bit = (b >> bitPos) & 1;
                int pixelIndex = bitIndex; // 1 bit per pixel
                if (pixelIndex >= pixels.length) break;
                
                // Modify the LSB of the blue channel
                int pixel = pixels[pixelIndex];
                int blue = pixel & 0xFF;
                int newBlue = (blue & 0xFE) | bit; // Clear LSB, set to bit
                pixels[pixelIndex] = (pixel & 0xFFFFFF00) | newBlue;
                bitIndex++;
            }
        }
        
        // Create new image with modified pixels
        WritableImage result = new WritableImage(width, height);
        PixelWriter writer = result.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setArgb(x, y, pixels[y * width + x]);
            }
        }
        
        return new ImageData(result, 
            imageData.getFileName() + "_stego.png", 
            imageData.getFileSize());
    }
    
    /**
     * Extract a message from an image using LSB steganography
     */
    public MessagePayload extract(ImageData imageData) {
        int[] pixels = imageData.getPixelData();
        
        // Build byte array from LSBs
        byte[] extractedBytes = new byte[pixels.length / 8 + 1]; // Max possible
        int byteIndex = 0;
        int bitIndex = 0;
        byte currentByte = 0;
        
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int blue = pixel & 0xFF;
            int bit = blue & 1;
            
            // Build byte bit by bit
            currentByte = (byte) ((currentByte << 1) | bit);
            bitIndex++;
            
            if (bitIndex == 8) {
                extractedBytes[byteIndex] = currentByte;
                byteIndex++;
                bitIndex = 0;
                currentByte = 0;
                
                // Check for terminator (0 byte)
                if (extractedBytes[byteIndex - 1] == 0) {
                    break;
                }
            }
        }
        
        // Extract only the valid message (excluding terminator)
        int messageLength = byteIndex - 1; // Exclude terminator
        byte[] messageBytes = new byte[messageLength];
        System.arraycopy(extractedBytes, 0, messageBytes, 0, messageLength);
        
        String message = new String(messageBytes, StandardCharsets.UTF_8);
        return new MessagePayload(message);
    }
    
    /**
     * Calculate how many bytes can fit in an image.
     * Accounts for the terminator byte that must be appended to every message.
     * Available capacity = (pixels / 8) - 1 terminator byte
     */
    public int calculateCapacity(ImageData imageData) {
        // 1 bit per pixel, 8 bits per byte, minus 1 byte for terminator
        int totalBytes = (imageData.getWidth() * imageData.getHeight()) / 8;
        return Math.max(0, totalBytes - 1); // At least 0 (can't store negative)
    }
    
    /**
     * Display bits of a message for debugging
     */
    public String getMessageBits(MessagePayload payload) {
        StringBuilder bits = new StringBuilder();
        byte[] bytes = payload.getMessageBytes();
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                bits.append((b >> i) & 1);
            }
            bits.append(" ");
        }
        return bits.toString().trim();
    }
    
    /**
     * Display bits of an image pixel for debugging
     */
    public String getPixelBits(int pixel) {
        int blue = pixel & 0xFF;
        StringBuilder bits = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            bits.append((blue >> i) & 1);
        }
        return bits.toString();
    }

    /**
     * Check if a message can be embedded without actually embedding.
     * Accounts for the terminator byte that will be added.
     */
    public boolean canEmbed(ImageData imageData, MessagePayload payload) {
        if (imageData == null || payload == null) return false;
        int maxBits = imageData.getWidth() * imageData.getHeight();
        byte[] messageBytes = payload.getMessageBytes();
        int requiredBits = (messageBytes.length + 1) * 8; // +1 for terminator
        return requiredBits <= maxBits;
    }

    /**
     * Get detailed information about the embedding process.
     * Accounts for the terminator byte that will be added.
     */
    public EmbeddingInfo getEmbeddingInfo(ImageData imageData, MessagePayload payload) {
        int maxBits = imageData.getWidth() * imageData.getHeight();
        byte[] messageBytes = payload.getMessageBytes();
        int usedBits = (messageBytes.length + 1) * 8; // Include terminator byte
        int remainingBits = maxBits - usedBits;
        
        return new EmbeddingInfo(maxBits, usedBits, remainingBits);
    }

    public static class EmbeddingInfo {
        private final int maxBits;
        private final int usedBits;
        private final int remainingBits;
        
        public EmbeddingInfo(int maxBits, int usedBits, int remainingBits) {
            this.maxBits = maxBits;
            this.usedBits = usedBits;
            this.remainingBits = remainingBits;
        }
        
        public int getMaxBits() { return maxBits; }
        public int getUsedBits() { return usedBits; }
        public int getRemainingBits() { return remainingBits; }
        public double getUsagePercentage() {
            return (usedBits / (double) maxBits) * 100;
        }
        
        @Override
        public String toString() {
            return String.format("Max: %d bits, Used: %d bits (%.1f%%), Remaining: %d bits",
                maxBits, usedBits, getUsagePercentage(), remainingBits);
        }
    }
}
