package com.shadowvault;

import com.shadowvault.model.ImageData;
import com.shadowvault.model.MessagePayload;
import com.shadowvault.service.LSBService;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LSBServiceTest {
    
    private LSBService lsbService;
    private ImageData smallTestImage;  // 10x10 = 100 pixels
    private ImageData largeTestImage;  // 50x50 = 2500 pixels
    
    @BeforeEach
    void setUp() {
        lsbService = new LSBService();
        
        // Create a small test image (10x10 = 100 pixels)
        smallTestImage = createTestImage(10, 10);
        
        // Create a large test image (50x50 = 2500 pixels) for longer messages
        largeTestImage = createTestImage(50, 50);
    }
    
    private ImageData createTestImage(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        
        // Fill with random colors
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (x * 20 + y * 10) % 256;
                int g = (y * 20 + x * 10) % 256;
                int b = (x * y * 5) % 256;
                int pixel = (255 << 24) | (r << 16) | (g << 8) | b;
                writer.setArgb(x, y, pixel);
            }
        }
        
        return new ImageData(image, "test.png", 1000);
    }
    
    @Test
    void testEmbedAndExtract() {
        // Test embedding and extracting a message (short message for small image)
        String originalMessage = "Hello!";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(smallTestImage, payload);
        assertNotNull(stegoImage);
        
        MessagePayload extracted = lsbService.extract(stegoImage);
        assertEquals(originalMessage, extracted.getMessage());
    }
    
    @Test
    void testEmptyMessage() {
        // Test empty message (still requires 1 byte for terminator)
        String originalMessage = "";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(smallTestImage, payload);
        assertNotNull(stegoImage);
        
        MessagePayload extracted = lsbService.extract(stegoImage);
        assertEquals(originalMessage, extracted.getMessage());
    }
    
    @Test
    void testLargeMessage() {
        // Test message that's too large for small image
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            longMessage.append("A");
        }
        
        MessagePayload payload = new MessagePayload(longMessage.toString());
        
        // Should throw exception because message is too large for small image
        assertThrows(IllegalArgumentException.class, () -> {
            lsbService.embed(smallTestImage, payload);
        });
    }
    
    @Test
    void testCapacityCalculation() {
        int capacity = lsbService.calculateCapacity(smallTestImage);
        // 10x10 = 100 pixels, 100/8 = 12 bytes, minus 1 for terminator = 11 bytes
        assertEquals(11, capacity);
    }
    
    @Test
    void testSpecialCharacters() {
        // Test with special characters (use large image for these longer messages)
        String originalMessage = "Hello! @#$%^&*()_+";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(largeTestImage, payload);
        assertNotNull(stegoImage);
        
        MessagePayload extracted = lsbService.extract(stegoImage);
        assertEquals(originalMessage, extracted.getMessage());
    }
    
    @Test
    void testUnicodeCharacters() {
        // Test with Unicode characters (use large image for these)
        String originalMessage = "你好世界 🌍 Hello";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(largeTestImage, payload);
        assertNotNull(stegoImage);
        
        MessagePayload extracted = lsbService.extract(stegoImage);
        assertEquals(originalMessage, extracted.getMessage());
    }
    
    @Test
    void testMessageBits() {
        // Test getting message bits
        String message = "A";
        MessagePayload payload = new MessagePayload(message);
        String bits = lsbService.getMessageBits(payload);
        assertEquals("01000001", bits.replace(" ", ""));
    }
    
    @Test
    void testCanEmbed() {
        // Test canEmbed method
        MessagePayload smallPayload = new MessagePayload("Hi");
        MessagePayload largePayload = new MessagePayload("This is a very long message that should not fit in small image");
        
        assertTrue(lsbService.canEmbed(smallTestImage, smallPayload));
        assertFalse(lsbService.canEmbed(smallTestImage, largePayload));
        assertTrue(lsbService.canEmbed(largeTestImage, largePayload));
    }
    
    @Test
    void testEmbeddingInfo() {
        MessagePayload payload = new MessagePayload("Hello");
        LSBService.EmbeddingInfo info = lsbService.getEmbeddingInfo(smallTestImage, payload);
        
        assertEquals(100, info.getMaxBits()); // 10x10 = 100 bits
        assertEquals(48, info.getUsedBits()); // "Hello" = 5 bytes + 1 terminator = 6 bytes = 48 bits
        assertEquals(52, info.getRemainingBits()); // 100 - 48 = 52 bits
        assertEquals(48.0, info.getUsagePercentage(), 0.01); // 48%
    }
}