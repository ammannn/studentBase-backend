package com.university.mcmaster.models.dtos.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdUniversityRequestDto {
    private int id;
    private String idExtended;
    private String name;
}
