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
    private ImageData testImage;
    
    @BeforeEach
    void setUp() {
        lsbService = new LSBService();
        
        // Create a test image (10x10 = 100 pixels)
        WritableImage image = new WritableImage(10, 10);
        PixelWriter writer = image.getPixelWriter();
        
        // Fill with random colors
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int r = (x * 20 + y * 10) % 256;
                int g = (y * 20 + x * 10) % 256;
                int b = (x * y * 5) % 256;
                int pixel = (255 << 24) | (r << 16) | (g << 8) | b;
                writer.setArgb(x, y, pixel);
            }
        }
        
        testImage = new ImageData(image, "test.png", 1000);
    }
    
    @Test
    void testEmbedAndExtract() {
        // Test embedding and extracting a message
        String originalMessage = "Hello ShadowVault!";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(testImage, payload);
        assertNotNull(stegoImage);
        
        MessagePayload extracted = lsbService.extract(stegoImage);
        assertEquals(originalMessage, extracted.getMessage());
    }
    
    @Test
    void testEmptyMessage() {
        // Test empty message
        String originalMessage = "";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(testImage, payload);
        assertNotNull(stegoImage);
        
        MessagePayload extracted = lsbService.extract(stegoImage);
        assertEquals(originalMessage, extracted.getMessage());
    }
    
    @Test
    void testLargeMessage() {
        // Test message that's too large
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            longMessage.append("A");
        }
        
        MessagePayload payload = new MessagePayload(longMessage.toString());
        
        // Should throw exception because message is too large
        assertThrows(IllegalArgumentException.class, () -> {
            lsbService.embed(testImage, payload);
        });
    }
    
    @Test
    void testCapacityCalculation() {
        int capacity = lsbService.calculateCapacity(testImage);
        // 10x10 = 100 pixels, 100/8 = 12.5 bytes
        assertEquals(12, capacity); // Integer division floors
    }
    
    @Test
    void testSpecialCharacters() {
        // Test with special characters
        String originalMessage = "Hello! @#$%^&*()_+{}|:<>?~`\n\t";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(testImage, payload);
        assertNotNull(stegoImage);
        
        MessagePayload extracted = lsbService.extract(stegoImage);
        assertEquals(originalMessage, extracted.getMessage());
    }
    
    @Test
    void testUnicodeCharacters() {
        // Test with Unicode characters
        String originalMessage = "你好世界 🌍 Hello 世界";
        MessagePayload payload = new MessagePayload(originalMessage);
        
        ImageData stegoImage = lsbService.embed(testImage, payload);
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
        MessagePayload largePayload = new MessagePayload("This is a very long message that should not fit");
        
        assertTrue(lsbService.canEmbed(testImage, smallPayload));
        assertFalse(lsbService.canEmbed(testImage, largePayload));
    }
    
    @Test
    void testEmbeddingInfo() {
        MessagePayload payload = new MessagePayload("Hello");
        LSBService.EmbeddingInfo info = lsbService.getEmbeddingInfo(testImage, payload);
        
        assertEquals(100, info.getMaxBits()); // 10x10 = 100 bits
        assertEquals(5 * 8, info.getUsedBits()); // "Hello" = 5 bytes = 40 bits
        assertEquals(60, info.getRemainingBits()); // 100 - 40 = 60 bits
        assertEquals(40.0, info.getUsagePercentage(), 0.01); // 40%
    }
}