# 🕵️ ShadowVault

**ShadowVault** is a Java desktop application for image steganography and image analysis.

It allows users to hide secret messages inside PNG images using **LSB (Least Significant Bit) steganography**, extract hidden messages, and compare original and stego images using image-quality metrics.

---

## 🚀 Version 1.0

ShadowVault v1.0 focuses on the core steganography workflow.

### ✨ Features

- 🔐 Hide text data inside PNG images
- 🔓 Extract hidden text from stego images
- 🖼️ Preview original and stego images
- 📊 Image quality analysis
- 📐 MSE calculation
- 📈 PSNR calculation
- 🧩 SSIM calculation
- 💾 Save stego images
- 🧭 Multi-screen JavaFX interface
- ⏳ Application loading screen
- 🧪 Unit testing
- 📦 Maven-based project

---

## 🧠 How LSB Steganography Works

ShadowVault uses the Least Significant Bit of image color channels to store message data.

For example:

```text
Original pixel:
Blue = 10110100

Message bit:
1

Modified pixel:
Blue = 10110101
