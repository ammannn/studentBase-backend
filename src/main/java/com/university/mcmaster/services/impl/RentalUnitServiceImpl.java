package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.ActionNotAllowedException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.AddUpdateRentalUnitRequestDto;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.repositories.RentalUnitRepo;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RentalUnitServiceImpl implements RentalUnitService {

    private final RentalUnitRepo rentalUnitRepo;

    @Override
    public ResponseEntity<ApiResponse<?>> getRentalUnits(int limit,String lastSeen,String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(Arrays.stream(userDetails.getRoles()).anyMatch(s->s.equals(UserRole.student.name()))){
            return getRentalUnitsForStudent(userDetails,limit,lastSeen,requestId);
        }
        if(Arrays.stream(userDetails.getRoles()).anyMatch(s->s.equals(UserRole.rental_unit_owner.name()))){
            return getRentalUnitsForRentalUnitOwner(userDetails,limit,lastSeen,requestId);
        }
        throw new ActionNotAllowedException("get_rental_units","user is not registered either as student or rental unit owner");
    }

    private ResponseEntity<ApiResponse<?>> getRentalUnitsForRentalUnitOwner(CustomUserDetails userDetails, int limit, String lastSeen, String requestId) {

        return null;
    }

    private ResponseEntity<ApiResponse<?>> getRentalUnitsForStudent(CustomUserDetails userDetails,int limit,String lastSeen, String requestId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> addRentalUnit(AddUpdateRentalUnitRequestDto requestDto, String requestId, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateRentalUnits(String requestId, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteRentalUnits(String rentalUnitId, String requestId, HttpServletRequest request) {
        return null;
    }
}
