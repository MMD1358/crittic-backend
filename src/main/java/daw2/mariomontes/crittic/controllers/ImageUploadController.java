package daw2.mariomontes.crittic.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/uploads")
public class ImageUploadController {

    private final Path uploadPath = Paths.get("uploads");

    @PostMapping("/profile-image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        Files.createDirectories(uploadPath);

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = UUID.randomUUID() + extension;

        Path destination = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), destination);

        return ResponseEntity.ok(Map.of("filename", filename));
    }
}