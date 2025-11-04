package com.hackaton.tryCatchers.pixelScribe.service;

import com.hackaton.tryCatchers.pixelScribe.dto.ImageAnalysisDTO;
import com.hackaton.tryCatchers.pixelScribe.dto.ImageUploadResponse;
import com.hackaton.tryCatchers.pixelScribe.model.ImageAnalysis;
import com.hackaton.tryCatchers.pixelScribe.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private AIService aiService;
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/jpg", "image/webp");
    
    public ImageUploadResponse uploadAndAnalyze(MultipartFile file, String userId) {
        try {
            // Validaciones
            validateFile(file);
            
            // Convertir a Base64
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            
            // Crear registro en BD
            ImageAnalysis imageAnalysis = new ImageAnalysis(
                userId,
                file.getOriginalFilename(),
                file.getContentType(),
                base64Image
            );
            
            imageAnalysis = imageRepository.save(imageAnalysis);
            
            // Analizar con IA (síncrono para categoría Junior)
            try {
                String description = aiService.analyzeImage(base64Image, file.getContentType());
                imageAnalysis.setDescription(description);
                imageAnalysis.setStatus("COMPLETED");
                imageAnalysis.setAnalyzedAt(LocalDateTime.now());
            } catch (Exception e) {
                imageAnalysis.setStatus("FAILED");
                imageAnalysis.setDescription("Error al analizar la imagen: " + e.getMessage());
            }
            
            imageRepository.save(imageAnalysis);
            
            return new ImageUploadResponse(
                imageAnalysis.getId(),
                imageAnalysis.getFileName(),
                imageAnalysis.getStatus(),
                "Imagen subida y analizada correctamente"
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
        }
    }
    
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("El archivo excede el tamaño máximo de 5MB");
        }
        
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new RuntimeException("Tipo de archivo no permitido. Solo se aceptan: JPEG, PNG, JPG, WEBP");
        }
    }
    
    public List<ImageAnalysisDTO> getUserImages(String userId) {
        List<ImageAnalysis> images = imageRepository.findByUserIdOrderByUploadedAtDesc(userId);
        
        return images.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public ImageAnalysisDTO getImageById(String id, String userId) {
        ImageAnalysis image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        
        if (!image.getUserId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para ver esta imagen");
        }
        
        return convertToDTO(image);
    }
    
    private ImageAnalysisDTO convertToDTO(ImageAnalysis image) {
        return new ImageAnalysisDTO(
            image.getId(),
            image.getFileName(),
            image.getImageBase64(),
            image.getDescription(),
            image.getStatus(),
            image.getUploadedAt(),
            image.getAnalyzedAt()
        );
    }
}