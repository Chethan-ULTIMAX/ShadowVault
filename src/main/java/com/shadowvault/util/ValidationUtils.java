package com.shadowvault.util;

import com.shadowvault.model.ImageData;
import com.shadowvault.model.MessagePayload;

public class ValidationUtils {
    
    public static ValidationResult validateMessage(ImageData imageData, String message) {
        if (imageData == null) {
            return new ValidationResult(false, "No image loaded. Please select an image first.");
        }
        
        if (message == null || message.trim().isEmpty()) {
            return new ValidationResult(false, "Message cannot be empty. Please enter a message.");
        }
        
        MessagePayload payload = new MessagePayload(message);
        int capacity = (imageData.getWidth() * imageData.getHeight()) / 8;
        
        if (payload.getByteLength() > capacity) {
            return new ValidationResult(false, 
                String.format("Message too large! (%d bytes) Image can only hold %d bytes.",
                              payload.getByteLength(), capacity));
        }
        
        return new ValidationResult(true, "Message fits! Ready to encode.");
    }
    
    public static ValidationResult validateExtraction(ImageData imageData) {
        if (imageData == null) {
            return new ValidationResult(false, "No image loaded. Please select an image first.");
        }
        
        if (imageData.getPixelData().length < 8) {
            return new ValidationResult(false, "Image too small to contain a message.");
        }
        
        return new ValidationResult(true, "Image ready for extraction.");
    }
    
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }
}