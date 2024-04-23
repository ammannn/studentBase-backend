package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.Address;
import com.university.mcmaster.models.entities.Contact;
import com.university.mcmaster.models.entities.RentalUnitFeatures;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddUpdateRentalUnitRequestDto {
    private String title;
    private Address address;
    private long deposit;
    private long rent;
    private RentalUnitFeatures features;
    private String posterImageId;
    private Contact contact;
    private String description;
    private int leaseTerm;
    private long leaseStartDate;
}
/*

 */
