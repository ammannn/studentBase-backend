package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDocFile {
    private String path;
    private String name;
}
