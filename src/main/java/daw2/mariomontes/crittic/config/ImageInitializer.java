package daw2.mariomontes.crittic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ImageInitializer.class);
    private static final String GITHUB_RAW_URL = "https://raw.githubusercontent.com/MMD1358/crittic-backend/main/uploads/";
    private static final String[] TEST_IMAGES = {
            "elden-ring.jpg",
            "fc2e8e04-ec40-419d-8a7d-22713a5b5a48.png",
            "user.jpg",
            "witcher3.jpg",
            "zelda.jpg"
    };

    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Override
    public void run(ApplicationArguments args) {
        try {
            Path uploadDir = Paths.get(uploadPath);
            Files.createDirectories(uploadDir);
            logger.info("Upload directory ready: {}", uploadDir.toAbsolutePath());

            for (String imageName : TEST_IMAGES) {
                downloadImage(imageName, uploadDir);
            }
        } catch (IOException e) {
            logger.error("Error initializing upload directory: {}", e.getMessage());
        }
    }

    private void downloadImage(String imageName, Path uploadDir) {
        Path filePath = uploadDir.resolve(imageName);

        if (Files.exists(filePath)) {
            logger.info("Image already exists, skipping: {}", imageName);
            return;
        }

        try {
            String imageUrl = GITHUB_RAW_URL + imageName;
            URL url = new URL(imageUrl);

            try (InputStream inputStream = url.openStream()) {
                Files.copy(inputStream, filePath);
                logger.info("Successfully downloaded test image: {}", imageName);
            }
        } catch (IOException e) {
            logger.warn("Failed to download test image {}: {}", imageName, e.getMessage());
        }
    }
}