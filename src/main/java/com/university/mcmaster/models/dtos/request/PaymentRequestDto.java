package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.enums.PaymentRequestType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDto {
    private PaymentRequestType requestType;
    private String entityId;
}
