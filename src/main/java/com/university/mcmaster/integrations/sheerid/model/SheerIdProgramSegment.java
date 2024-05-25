package com.university.mcmaster.integrations.sheerid.model;

import lombok.*;

/*
"segment": "some segment",
"subSegment": "some subsegment",
"organization": {},
"active": true,
"startDate": 1111111111111,
"endDate": 2222222222222
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdProgramSegment {
    private String segment;
    private String subSegment;
    private SheerIdUniversity organization;
    private boolean active;
    private long startDate;
    private long endDate;
}

