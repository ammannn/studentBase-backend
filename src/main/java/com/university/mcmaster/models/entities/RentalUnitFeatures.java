package com.university.mcmaster.models.entities;

import com.university.mcmaster.utils.Utility;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RentalUnitFeatures {

    private Map<String,Boolean> featuresUtilities;
    private Map<String,Boolean> featuresAmenities;
    private Map<String,Integer> featuresNumbers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalUnitFeatures that = (RentalUnitFeatures) o;
        return Utility.areMapsEqual(getFeaturesUtilities(), that.getFeaturesUtilities()) &&
                Utility.areMapsEqual(getFeaturesAmenities(), that.getFeaturesAmenities()) &&
                Utility.areMapsEqual(getFeaturesNumbers(), that.getFeaturesNumbers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFeaturesUtilities(), getFeaturesAmenities(), getFeaturesNumbers());
    }
}
