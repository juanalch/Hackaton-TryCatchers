package com.pixelscribe.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageAnalysisDTO {
    private String id;
    private String fileName;
    private String imageBase64;
    private String description;
    private String status;
    private LocalDateTime uploadedAt;
    private LocalDateTime analyzedAt;
}