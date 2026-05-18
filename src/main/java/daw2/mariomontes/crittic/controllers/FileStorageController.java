package daw2.mariomontes.crittic.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class FileStorageController {

    private static final Logger logger =
            LoggerFactory.getLogger(FileStorageController.class);

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                logger.info("Sirviendo archivo: {}", fileName);

                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                    logger.warn("No se pudo detectar el tipo MIME del archivo {}", fileName);
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\""
                        )
                        .body(resource);
            } else {
                logger.error("El archivo {} no existe o no se puede leer.", fileName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (IOException e) {
            logger.error("Error al servir el archivo {}: {}", fileName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}