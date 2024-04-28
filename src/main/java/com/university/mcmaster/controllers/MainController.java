package com.university.mcmaster.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping("/version")
    public ResponseEntity<?> version() {
        return ResponseEntity.ok("v0.0.1");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/countries")
    public ResponseEntity<?> getCountries() {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonMap = null;
        try {
            jsonMap = mapper.readValue(new File(Paths.get("src","main","resources","static","countries.json").toString()), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .data(jsonMap)
                .build());
    }
}
