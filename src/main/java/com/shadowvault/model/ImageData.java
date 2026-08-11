package com.shadowvault.model;

import com.shadowvault.util.FileUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class ImageData {
    
    private final Image image;
    private final int width;
    private final int height;
    private final String fileName;
    private final long fileSize;
    private final int[] pixelData;
    
    public ImageData(Image image, String fileName, long fileSize) {
        this.image = image;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.pixelData = extractPixelData(image);
    }
    
    private int[] extractPixelData(Image image) {
        PixelReader reader = image.getPixelReader();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y * width + x] = reader.getArgb(x, y);
            }
        }
        return pixels;
    }
    
    public Image getImage() { return image; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getFileName() { return fileName; }
    public long getFileSize() { return fileSize; }
    public int[] getPixelData() { return pixelData; }
    
    public String getInfo() {
        return String.format("📐 %d × %d pixels | 💾 %s | 🎨 ARGB", 
            width, height, FileUtils.getFileSizeStr(fileSize));
    }
}