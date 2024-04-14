package com.university.mcmaster.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private String id;
    private String entityId;
    private Map<String,Integer> ratingMap;
}
