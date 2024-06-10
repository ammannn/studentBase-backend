package com.university.mcmaster.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.integrations.sheerid.SheerIdService;
import com.university.mcmaster.integrations.sheerid.model.SheerIdUniversity;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.SheerIdOrgSearchRequestDto;
import com.university.mcmaster.utils.GcpStorageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/version")
    public ResponseEntity<?> version() {
        return ResponseEntity.ok("v0.0.3");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/countries")
    public ResponseEntity<?> getCountries() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(GcpStorageUtil.getCountries());
            System.out.println(node);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(node)
                    .build());
        } catch (JsonProcessingException e) {
            log.trace(e.getMessage());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .data(new HashMap<>())
                .build());
    }

    @GetMapping("/time-zones")
    public ResponseEntity<?> getTimeZones() {
        List<String[]> res = new ArrayList<>();
        for (String zoneId : ZoneId.getAvailableZoneIds()) {
            ZoneId id = ZoneId.of(zoneId);
            ZoneOffset offset = id.getRules().getOffset(java.time.Instant.now());
            String offsetStr = offset.toString();
            res.add(new String[]{zoneId,zoneId + " : GMT" + (offsetStr.startsWith("-") ? offsetStr : "+" + offsetStr)});
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sheerId/orgs")
    public ResponseEntity<?> getSheerIdOrgs(
            @RequestBody SheerIdOrgSearchRequestDto requestDto,
            @RequestHeader("requestId") String requestId
    ) {
        SheerIdUniversity[] res = SheerIdService.searchUniversities(requestDto.getSearchTerm(),requestDto.getCountry());
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(res)
                .build());
    }
}
