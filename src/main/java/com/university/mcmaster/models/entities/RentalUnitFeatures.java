package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.Amenity;
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
    private Map<String,Boolean> featuresFlags;
    private Map<String,Double> featuresNumbers;
    private List<String> extraFeatures;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalUnitFeatures that = (RentalUnitFeatures) o;
        return Utility.areMapsEqual(getFeaturesFlags(), that.getFeaturesFlags()) && Utility.areMapsEqual(getFeaturesNumbers(), that.getFeaturesNumbers()) && Utility.areListsEqual(getExtraFeatures(), that.getExtraFeatures());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getFeaturesFlags(), getFeaturesNumbers(), getExtraFeatures());
    }
}
