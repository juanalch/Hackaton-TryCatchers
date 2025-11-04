package com.hackaton.tryCatchers.pixelScribe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiApiUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Analiza una imagen usando Gemini AI
     * @param base64Image Imagen en formato Base64
     * @param mimeType Tipo MIME de la imagen (ej: image/jpeg)
     * @return Descripción generada por la IA
     * Generado con asistencia de IA - ChatGPT
     */
    public String analyzeImage(String base64Image, String mimeType) {
        logger.info("Iniciando análisis de imagen con Gemini AI");
        
        try {
            // Validar API Key
            if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.equals("your-gemini-api-key")) {
                throw new RuntimeException("Gemini API Key no configurada. Revisa tu archivo .env");
            }
            
            // Construir el request
            Map<String, Object> requestBody = buildGeminiRequest(base64Image, mimeType);
            
            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Request entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Llamar a la API de Gemini
            String url = geminiApiUrl + "?key=" + geminiApiKey;
            logger.info("Llamando a Gemini API...");
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                Map.class
            );
            
            logger.info("Respuesta recibida de Gemini AI");
            
            // Extraer la descripción
            String description = extractDescription(response.getBody());
            
            if (description == null || description.isEmpty()) {
                throw new RuntimeException("Gemini no devolvió una descripción válida");
            }
            
            logger.info("Análisis completado exitosamente");
            return description;
            
        } catch (HttpClientErrorException e) {
            logger.error("Error HTTP al llamar a Gemini: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Error en la solicitud a Gemini. Verifica el formato de la imagen.");
            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new RuntimeException("API Key de Gemini inválida o sin permisos.");
            } else {
                throw new RuntimeException("Error al conectar con Gemini: " + e.getMessage());
            }
            
        } catch (Exception e) {
            logger.error("Error inesperado al analizar la imagen", e);
            throw new RuntimeException("Error al analizar la imagen con IA: " + e.getMessage());
        }
    }
    
    /**
     * Construye el request body para Gemini API
     * Generado con asistencia de IA - ChatGPT
     */
    private Map<String, Object> buildGeminiRequest(String base64Image, String mimeType) {
        // Limpiar el Base64 (remover prefijo data:image si existe)
        String cleanBase64 = base64Image.replaceFirst("^data:image/[a-zA-Z]+;base64,", "");
        
        // Parte 1: El texto del prompt
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", buildPrompt());
        
        // Parte 2: La imagen en Base64
        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mimeType", mimeType);
        inlineData.put("data", cleanBase64);
        
        Map<String, Object> imagePart = new HashMap<>();
        imagePart.put("inlineData", inlineData);
        
        // Contenido completo
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(textPart, imagePart));
        
        // Request final
        Map<String, Object> request = new HashMap<>();
        request.put("contents", List.of(content));
        
        // Configuración de generación (opcional pero recomendado)
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 500);
        request.put("generationConfig", generationConfig);
        
        return request;
    }
    
    /**
     * Construye el prompt para Gemini
     */
    private String buildPrompt() {
        return "Analiza esta imagen en español y proporciona:\n" +
               "1. Una descripción detallada de lo que ves\n" +
               "2. Los objetos principales presentes\n" +
               "3. Los colores dominantes\n" +
               "4. El contexto o escenario de la imagen\n" +
               "5. Cualquier texto visible (si existe)\n\n" +
               "Sé descriptivo pero conciso. Máximo 200 palabras.";
    }
    
    /**
     * Extrae la descripción de la respuesta de Gemini
     * Generado con asistencia de IA - ChatGPT
     */
    private String extractDescription(Map<String, Object> responseBody) {
        try {
            logger.info("Extrayendo descripción de la respuesta");
            
            // Navegar por la estructura de respuesta de Gemini
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            
            if (candidates == null || candidates.isEmpty()) {
                logger.warn("No se encontraron candidatos en la respuesta");
                return "No se pudo generar una descripción para esta imagen.";
            }
            
            Map<String, Object> candidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) candidate.get("content");
            
            if (content == null) {
                logger.warn("No se encontró contenido en la respuesta");
                return "La IA no pudo procesar esta imagen.";
            }
            
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            
            if (parts == null || parts.isEmpty()) {
                logger.warn("No se encontraron partes en el contenido");
                return "No se generó una descripción válida.";
            }
            
            String text = (String) parts.get(0).get("text");
            
            if (text == null || text.trim().isEmpty()) {
                logger.warn("El texto de la respuesta está vacío");
                return "La descripción generada está vacía.";
            }
            
            logger.info("Descripción extraída exitosamente");
            return text.trim();
            
        } catch (Exception e) {
            logger.error("Error al extraer la descripción de la respuesta", e);
            return "Error al procesar la respuesta de la IA.";
        }
    }
    
    /**
     * Verifica si la API Key de Gemini está configurada correctamente
     */
    public boolean isConfigured() {
        return geminiApiKey != null 
            && !geminiApiKey.isEmpty() 
            && !geminiApiKey.equals("your-gemini-api-key");
    }
    
    /**
     * Endpoint de prueba para verificar la conexión con Gemini
     */
    public String testConnection() {
        try {
            if (!isConfigured()) {
                return "❌ Gemini API Key no configurada";
            }
            
            // Crear una imagen de prueba simple (1x1 pixel rojo en Base64)
            String testImage = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCwAA8A/9k=";
            
            String result = analyzeImage(testImage, "image/jpeg");
            return "✅ Conexión exitosa con Gemini. Respuesta: " + result.substring(0, Math.min(100, result.length()));
            
        } catch (Exception e) {
            return "❌ Error al conectar con Gemini: " + e.getMessage();
        }
    }
}