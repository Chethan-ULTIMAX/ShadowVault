package com.shadowvault.service;

import com.shadowvault.model.ImageData;

public class QualityMetrics {
    
    /**
     * Calculate Mean Squared Error between two images
     */
    public double calculateMSE(ImageData original, ImageData processed) {
        if (original == null || processed == null) return -1;
        if (original.getWidth() != processed.getWidth() || 
            original.getHeight() != processed.getHeight()) {
            return -1;
        }
        
        int[] origPixels = original.getPixelData();
        int[] procPixels = processed.getPixelData();
        
        long sum = 0;
        for (int i = 0; i < origPixels.length; i++) {
            int origR = (origPixels[i] >> 16) & 0xFF;
            int origG = (origPixels[i] >> 8) & 0xFF;
            int origB = origPixels[i] & 0xFF;
            
            int procR = (procPixels[i] >> 16) & 0xFF;
            int procG = (procPixels[i] >> 8) & 0xFF;
            int procB = procPixels[i] & 0xFF;
            
            sum += (origR - procR) * (origR - procR);
            sum += (origG - procG) * (origG - procG);
            sum += (origB - procB) * (origB - procB);
        }
        
        return sum / (double) (origPixels.length * 3);
    }
    
    /**
     * Calculate Peak Signal-to-Noise Ratio
     */
    public double calculatePSNR(double mse) {
        if (mse <= 0) return Double.POSITIVE_INFINITY;
        double maxPixel = 255.0;
        return 10 * Math.log10((maxPixel * maxPixel) / mse);
    }
    
    /**
     * Calculate Structural Similarity Index
     * Simplified version
     */
    public double calculateSSIM(ImageData original, ImageData processed) {
        if (original == null || processed == null) return -1;
        if (original.getWidth() != processed.getWidth() || 
            original.getHeight() != processed.getHeight()) {
            return -1;
        }
        
        int[] origPixels = original.getPixelData();
        int[] procPixels = processed.getPixelData();
        
        // Calculate means
        double meanOrig = 0, meanProc = 0;
        for (int i = 0; i < origPixels.length; i++) {
            int origGray = ((origPixels[i] >> 16) & 0xFF) * 3;
            int procGray = ((procPixels[i] >> 16) & 0xFF) * 3;
            meanOrig += origGray;
            meanProc += procGray;
        }
        meanOrig /= origPixels.length * 3;
        meanProc /= procPixels.length * 3;
        
        // Calculate variance and covariance
        double varOrig = 0, varProc = 0, covar = 0;
        for (int i = 0; i < origPixels.length; i++) {
            int origGray = ((origPixels[i] >> 16) & 0xFF) * 3;
            int procGray = ((procPixels[i] >> 16) & 0xFF) * 3;
            varOrig += (origGray - meanOrig) * (origGray - meanOrig);
            varProc += (procGray - meanProc) * (procGray - meanProc);
            covar += (origGray - meanOrig) * (procGray - meanProc);
        }
        varOrig /= origPixels.length * 3;
        varProc /= origPixels.length * 3;
        covar /= origPixels.length * 3;
        
        // SSIM constants
        double c1 = 0.01 * 255 * 0.01 * 255;
        double c2 = 0.03 * 255 * 0.03 * 255;
        
        return (2 * meanOrig * meanProc + c1) * (2 * covar + c2) /
               ((meanOrig * meanOrig + meanProc * meanProc + c1) * 
                (varOrig + varProc + c2));
    }
}