package com.university.mcmaster.models.dtos.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingAndReviewResponse {
    private String name;
    private String review;
    private int rating;
}
