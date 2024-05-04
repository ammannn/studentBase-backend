package com.university.mcmaster.models.dtos.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MethodResponse<P,Q,R> {
    private P result_1;
    private Q result_2;
    private R result_3;
    private String resultStr;
    private boolean flag;
}
