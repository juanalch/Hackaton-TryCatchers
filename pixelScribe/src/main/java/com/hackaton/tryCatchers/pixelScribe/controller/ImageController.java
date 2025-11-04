package com.hackaton.tryCatchers.pixelScribe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Imágenes", description = "Endpoints para subir y analizar imágenes")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Operation(summary = "Subir imagen", description = "Sube una imagen para análisis con IA")
    @PostMapping("/upload")
    public String uploadImage() {
        return "Upload endpoint";
    }

    @Operation(summary = "Obtener imágenes", description = "Obtiene todas las imágenes analizadas del usuario autenticado")
    @GetMapping
    public String getImages() {
        return "Get images endpoint";
    }
}
