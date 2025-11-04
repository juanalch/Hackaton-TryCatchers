package com.pixelscribe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiApiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // Generado con asistencia de IA
    public String analyzeImage(String base64Image, String mimeType) {
        try {
            // Construir el request para Gemini
            Map<String, Object> requestBody = buildGeminiRequest(base64Image, mimeType);
            
            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Request entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Llamar a la API
            String url = geminiApiUrl + "?key=" + geminiApiKey;
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            // Extraer la descripción de la respuesta
            return extractDescription(response.getBody());
            
        } catch (Exception e) {
            throw new RuntimeException("Error al analizar la imagen: " + e.getMessage());
        }
    }
    
    private Map<String, Object> buildGeminiRequest(String base64Image, String mimeType) {
        // Remover el prefijo data:image/...;base64, si existe
        String cleanBase64 = base64Image.replaceFirst("^data:image/[a-zA-Z]+;base64,", "");
        
        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mimeType", mimeType);
        inlineData.put("data", cleanBase64);
        
        Map<String, Object> imagePart = new HashMap<>();
        imagePart.put("inlineData", inlineData);
        
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", "Describe esta imagen en español de forma detallada. " +
                             "Incluye qué objetos ves, colores principales, y el contexto general de la imagen.");
        
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(textPart, imagePart));
        
        Map<String, Object> request = new HashMap<>();
        request.put("contents", List.of(content));
        
        return request;
    }
    
    private String extractDescription(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
            return "No se pudo generar una descripción";
        } catch (Exception e) {
            return "Error al procesar la respuesta de la IA";
        }
    }
}