package com.pixelscribe.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ImageUploadResponse {
    private String id;
    private String fileName;
    private String status;
    private String message;
}