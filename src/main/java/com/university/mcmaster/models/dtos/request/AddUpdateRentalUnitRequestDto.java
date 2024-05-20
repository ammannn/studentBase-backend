package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.integrations.sheerid.model.SheerIdUniversity;
import com.university.mcmaster.models.entities.Address;
import com.university.mcmaster.models.entities.Amount;
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
    private Amount rent;
    private Amount deposit;
    private RentalUnitFeatures features;
    private String posterImageId;
    private Contact contact;
    private String description;
    private String leaseTerm;
    private long leaseStartDate;
    private SheerIdUniversity organization;
}