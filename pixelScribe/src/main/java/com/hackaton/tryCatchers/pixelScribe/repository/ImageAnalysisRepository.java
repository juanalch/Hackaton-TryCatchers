package com.hackaton.tryCatchers.pixelScribe.repository;

import com.hackaton.tryCatchers.pixelScribe.model.ImageAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageAnalysisRepository extends MongoRepository<ImageAnalysis, String> {
    List<ImageAnalysis> findByUserId(String userId);
}
