package com.hackaton.tryCatchers.pixelScribe.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @PostMapping("/upload")
    public String uploadImage() {
        return "Upload endpoint";
    }

    @GetMapping
    public String getImages() {
        return "Get images endpoint";
    }
}
