package com.hackaton.tryCatchers.pixelScribe.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "images")
public class ImageAnalysis {
    @Id
    private String id;
    private String userId;
    private String imageUrl;
    private String description;
    private String fileName;
}
