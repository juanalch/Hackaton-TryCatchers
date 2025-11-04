package com.pixelscribe.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "image_analysis")
public class ImageAnalysis {
    
    @Id
    private String id;
    
    private String userId;
    
    private String fileName;
    
    private String contentType; // image/jpeg, image/png, etc.
    
    private String imageBase64; // Imagen en Base64
    
    private String description; // Descripción generada por IA
    
    private String status; // PROCESSING, COMPLETED, FAILED
    
    private LocalDateTime uploadedAt;
    
    private LocalDateTime analyzedAt;
    
    public ImageAnalysis(String userId, String fileName, String contentType, String imageBase64) {
        this.userId = userId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.imageBase64 = imageBase64;
        this.status = "PROCESSING";
        this.uploadedAt = LocalDateTime.now();
    }
}