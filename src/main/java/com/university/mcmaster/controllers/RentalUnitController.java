package com.university.mcmaster.controllers;

import com.university.mcmaster.models.dtos.request.AddUpdateRentalUnitRequestDto;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.SearchRentalUnitRequestDto;
import com.university.mcmaster.services.RentalUnitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RentalUnitController {

    private final RentalUnitService rentalUnitService;

    @GetMapping("/rental-units")
    public ResponseEntity<ApiResponse<?>> getRentalUnits(
            @RequestHeader("requestId") String requestId,
            @RequestParam(name = "limit",required = false,defaultValue = "10") int limit,
            @RequestParam(name = "lastSeen",required = false) String lastSeen,
            @RequestParam(name = "fetchLiveOnly",required = false) boolean fetchLiveOnly,
            HttpServletRequest request
    ){
        return rentalUnitService.getRentalUnits(fetchLiveOnly,limit,lastSeen,requestId,request);
    }

    @PostMapping("/rental-units/search")
    public ResponseEntity<ApiResponse<?>> searchRentalUnits(
            @RequestBody SearchRentalUnitRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            @RequestParam(name = "limit",required = false,defaultValue = "10") int limit,
            @RequestParam(name = "lastSeen",required = false) String lastSeen,
            HttpServletRequest request
    ){
        return rentalUnitService.searchRentalUnits(requestDto,limit,lastSeen,requestId,request);
    }

    @GetMapping("/rental-units/{rentalUnitId}")
    public ResponseEntity<ApiResponse<?>> getRentalUnitById(
            @PathVariable("rentalUnitId") String rentalUnitId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return rentalUnitService.getRentalUnitById(rentalUnitId,requestId,request);
    }

    @PostMapping("/rental-units")
    public ResponseEntity<ApiResponse<?>> addRentalUnit(
            @RequestBody AddUpdateRentalUnitRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return rentalUnitService.addRentalUnit(requestDto,requestId,request);
    }

    @PutMapping("/rental-units/{rentalUnitId}")
    public ResponseEntity<ApiResponse<?>> updateRentalUnits(
            @PathVariable("rentalUnitId") String rentalUnitId,
            @RequestBody AddUpdateRentalUnitRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return rentalUnitService.updateRentalUnits(rentalUnitId,requestDto,requestId,request);
    }

    @DeleteMapping("/rental-units/{rentalUnitId}")
    public ResponseEntity<ApiResponse<?>> deleteRentalUnits(
            @PathVariable("rentalUnitId") String rentalUnitId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return rentalUnitService.deleteRentalUnits(rentalUnitId,requestId,request);
    }

    @GetMapping("/rental-units/static")
    public ResponseEntity<ApiResponse<?>> getRentalUnitFeaturesStaticData(
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return rentalUnitService.getRentalUnitFeaturesStaticData(requestId,request);
    }
}
