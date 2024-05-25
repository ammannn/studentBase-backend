package com.university.mcmaster.integrations.sheerid.model;

import lombok.*;

import java.util.Map;

/*
"firstName": "Joe",
"lastName": "Verify",
"email": "joe.verify@sheerid.com",
"birthDate": "1991-01-01",
"metadata": {
"my": "stuff"
},
"organization": {
"id": 1234,
"name": "Some Organization"
}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdPersonInfo {
    private String email;
    private String birthDate;
    private String firstName;
    private String lastName;
    private SheerIdUniversity organization;
    private String phoneNumber;
    private Map<String,Object> metadata;
}
