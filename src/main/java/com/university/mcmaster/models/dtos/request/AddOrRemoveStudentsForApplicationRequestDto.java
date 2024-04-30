package com.university.mcmaster.models.dtos.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrRemoveStudentsForApplicationRequestDto {
    private List<String> students;
}
