package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    private String email;
    private String phoneNumber;
}
