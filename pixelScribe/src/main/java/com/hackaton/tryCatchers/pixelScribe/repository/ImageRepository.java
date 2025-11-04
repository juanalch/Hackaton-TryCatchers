package com.pixelscribe.repository;

import com.pixelscribe.model.ImageAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<ImageAnalysis, String> {
    List<ImageAnalysis> findByUserIdOrderByUploadedAtDesc(String userId);
    long countByUserId(String userId);
}