package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.AddUpdateRentalUnitRequestDto;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface RentalUnitService {
    ResponseEntity<ApiResponse<?>> getRentalUnits(int limit,String lastSeen,String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> addRentalUnit(AddUpdateRentalUnitRequestDto requestDto, String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> updateRentalUnits(String rentalUnitId,AddUpdateRentalUnitRequestDto requestDto,String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> deleteRentalUnits(String rentalUnitId, String requestId, HttpServletRequest request);
    void updateRentalUnitPosterImage(String rentalUnitId, String imageId,String imagePath);
}
