package com.shadowvault;

import com.shadowvault.model.ImageData;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageDataTest {

    @Test
    void testImageDataCreation() {
        // Create a 5x5 test image
        WritableImage image = new WritableImage(5, 5);
        PixelWriter writer = image.getPixelWriter();
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                writer.setArgb(x, y, 0xFF000000);
            }
        }

        ImageData imageData = new ImageData(image, "test.png", 1024);

        // Verify properties
        assertEquals(5, imageData.getWidth());
        assertEquals(5, imageData.getHeight());
        assertEquals("test.png", imageData.getFileName());
        assertEquals(1024, imageData.getFileSize());
        assertNotNull(imageData.getPixelData());
        assertEquals(25, imageData.getPixelData().length); // 5x5 = 25 pixels
    }

    @Test
    void testImageInfo() {
        // Create a 100x200 test image
        WritableImage image = new WritableImage(100, 200);
        PixelWriter writer = image.getPixelWriter();
        for (int y = 0; y < 200; y++) {
            for (int x = 0; x < 100; x++) {
                writer.setArgb(x, y, 0xFF000000);
            }
        }

        ImageData imageData = new ImageData(image, "photo.png", 5120);
        String info = imageData.getInfo();

        // Verify info contains dimensions and size
        assertTrue(info.contains("100"));
        assertTrue(info.contains("200"));
        assertTrue(info.contains("5.00 KB"));
    }
}