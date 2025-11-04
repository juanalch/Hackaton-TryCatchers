package com.hackaton.tryCatchers.pixelScribe.controller;

import com.hackaton.tryCatchers.pixelScribe.dto.ImageAnalysisDTO;
import com.hackaton.tryCatchers.pixelScribe.dto.ImageUploadResponse;
import com.hackaton.tryCatchers.pixelScribe.security.SecurityUtils;
import com.hackaton.tryCatchers.pixelScribe.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Images", description = "Endpoints para gestión de imágenes")
@SecurityRequirement(name = "bearerAuth")
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir y analizar una imagen")
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            String userId = SecurityUtils.getCurrentUserId();
            ImageUploadResponse response = imageService.uploadAndAnalyze(file, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ImageUploadResponse(null, null, "FAILED", e.getMessage()));
        }
    }
    
    @GetMapping
    @Operation(summary = "Obtener todas las imágenes del usuario")
    public ResponseEntity<List<ImageAnalysisDTO>> getUserImages() {
        try {
            String userId = SecurityUtils.getCurrentUserId();
            List<ImageAnalysisDTO> images = imageService.getUserImages(userId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una imagen específica")
    public ResponseEntity<ImageAnalysisDTO> getImage(@PathVariable String id) {
        try {
            String userId = SecurityUtils.getCurrentUserId();
            ImageAnalysisDTO image = imageService.getImageById(id, userId);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}