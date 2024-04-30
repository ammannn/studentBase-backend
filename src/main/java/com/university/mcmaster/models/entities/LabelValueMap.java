package com.university.mcmaster.models.entities;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabelValueMap {
    private String label;
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelValueMap that = (LabelValueMap) o;
        return Objects.equals(getLabel(), that.getLabel()) && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabel(), getValue());
    }
}
