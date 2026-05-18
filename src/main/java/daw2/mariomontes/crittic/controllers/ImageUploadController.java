package daw2.mariomontes.crittic.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/uploads")
public class ImageUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/profile-image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")
                && !extension.equals(".webp")) {
            throw new IllegalArgumentException("Only JPG, PNG and WEBP images are allowed.");
        }

        Path uploadDirectory = Paths.get(uploadPath);
        Files.createDirectories(uploadDirectory);

        String filename = UUID.randomUUID() + extension;

        Path destination = uploadDirectory
                .resolve(filename)
                .normalize();

        Files.copy(
                file.getInputStream(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );

        return ResponseEntity.ok(Map.of("filename", filename));
    }
}