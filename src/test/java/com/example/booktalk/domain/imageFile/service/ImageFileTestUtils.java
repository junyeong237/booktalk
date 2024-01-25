package com.example.booktalk.domain.imageFile.service;

import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageFileTestUtils {

    public static MockMultipartFile createMockImageFile(String name, String format, int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Set image content (here we use a solid white color)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, 0xFFFFFF);
            }
        }

        // Create a ByteArrayOutputStream to store the image data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Write the image to the ByteArrayOutputStream in the specified format
        ImageIO.write(image, format, baos);

        // Convert the ByteArrayOutputStream to a byte array
        byte[] imageBytes = baos.toByteArray();

        // Create a MockMultipartFile
        return new MockMultipartFile(name, name, "image/" + format, new ByteArrayInputStream(imageBytes));
    }
}
