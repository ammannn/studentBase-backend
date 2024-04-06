package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.Amenity;
import com.university.mcmaster.utils.Utility;
import lombok.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RentalUnitFeatures {
    private int rooms;
    private int bathrooms;
    private boolean furnished;
    private double areaInSqFt;
    private boolean parking;
    private boolean garden;
    private boolean petsAllowed;
    private boolean airConditioning;
    private List<Amenity> amenities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalUnitFeatures that = (RentalUnitFeatures) o;
        return rooms == that.rooms && bathrooms == that.bathrooms && furnished == that.furnished && Double.compare(areaInSqFt, that.areaInSqFt) == 0 && parking == that.parking && garden == that.garden && petsAllowed == that.petsAllowed && airConditioning == that.airConditioning && Utility.areListsEqual(amenities, that.amenities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rooms, bathrooms, furnished, areaInSqFt, parking, garden, petsAllowed, airConditioning, amenities);
    }
}
