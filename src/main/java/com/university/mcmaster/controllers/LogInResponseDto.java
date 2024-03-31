package com.university.mcmaster.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.university.mcmaster.models.dtos.response.RentalUnitOwnerLogInResponse;
import com.university.mcmaster.models.dtos.response.StudentLogInResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogInResponseDto {
    private boolean registered;
    private StudentLogInResponse student;
    private RentalUnitOwnerLogInResponse rentalUnitOwner;
}
