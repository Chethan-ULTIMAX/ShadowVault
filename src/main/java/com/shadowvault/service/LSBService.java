package com.shadowvault.service;

import com.shadowvault.model.ImageData;
import com.shadowvault.model.MessagePayload;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class LSBService {
    
    /**
     * Embed a message into an image using LSB steganography
     */
    public ImageData embed(ImageData imageData, MessagePayload payload) {
        // Check if message fits in image
        int maxBits = imageData.getWidth() * imageData.getHeight();
        if (payload.getBitLength() > maxBits) {
            throw new IllegalArgumentException(
                "Message too large! Need " + payload.getBitLength() + 
                " bits but image can hold " + maxBits + " bits"
            );
        }
        
        // Get message bits
        byte[] messageBytes = payload.getMessageBytes();
        int[] pixels = imageData.getPixelData().clone();
        int width = imageData.getWidth();
        int height = imageData.getHeight();
        
        // Add terminator (8 zeros) to mark end of message
        byte[] dataWithTerminator = new byte[messageBytes.length + 1];
        System.arraycopy(messageBytes, 0, dataWithTerminator, 0, messageBytes.length);
        dataWithTerminator[messageBytes.length] = 0; // terminator
        
        // Embed each bit
        int bitIndex = 0;
        for (int i = 0; i < dataWithTerminator.length && bitIndex < pixels.length * 8; i++) {
            byte b = dataWithTerminator[i];
            for (int bitPos = 7; bitPos >= 0; bitPos--) {
                int bit = (b >> bitPos) & 1;
                int pixelIndex = bitIndex / 8; // 8 bits per pixel (using blue channel only)
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
        
        String message = new String(messageBytes);
        return new MessagePayload(message);
    }
    
    /**
     * Calculate how many bytes can fit in an image
     */
    public int calculateCapacity(ImageData imageData) {
        // 1 bit per pixel, 8 bits per byte
        return (imageData.getWidth() * imageData.getHeight()) / 8;
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
     * Check if a message can be embedded without actually embedding
     */
    public boolean canEmbed(ImageData imageData, MessagePayload payload) {
        if (imageData == null || payload == null) return false;
        int maxBits = imageData.getWidth() * imageData.getHeight();
        return payload.getBitLength() <= maxBits;
    }

    /**
     * Get detailed information about the embedding process
     */
    public EmbeddingInfo getEmbeddingInfo(ImageData imageData, MessagePayload payload) {
        int maxBits = imageData.getWidth() * imageData.getHeight();
        int usedBits = payload.getBitLength();
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
