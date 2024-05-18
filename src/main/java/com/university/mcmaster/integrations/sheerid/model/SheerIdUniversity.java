package com.university.mcmaster.integrations.sheerid.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdUniversity {
    private int id;
    private String idExtended;
    private String name;
    private String country;
    private String type;
}
