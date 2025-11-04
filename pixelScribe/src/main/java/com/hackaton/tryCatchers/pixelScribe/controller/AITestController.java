package com.hackaton.tryCatchers.pixelScribe.controller;

import com.pixelscribe.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Testing", description = "Endpoints para probar la integración con IA")
public class AITestController {
    
    @Autowired
    private AIService aiService;
    
    @GetMapping("/test")
    @Operation(summary = "Probar conexión con Gemini AI")
    public ResponseEntity<Map<String, String>> testAI() {
        Map<String, String> response = new HashMap<>();
        
        try {
            String result = aiService.testConnection();
            response.put("status", "success");
            response.put("message", result);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/status")
    @Operation(summary = "Verificar estado de configuración de IA")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        
        boolean isConfigured = aiService.isConfigured();
        response.put("configured", isConfigured);
        response.put("message", isConfigured 
            ? "Gemini API está configurada correctamente" 
            : "Gemini API Key no está configurada");
        
        return ResponseEntity.ok(response);
    }
}