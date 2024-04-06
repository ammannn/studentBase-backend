package com.university.mcmaster.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String country;
    private String state;
    private String zip;
    private String city;
    private boolean primary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return primary == address.primary && Objects.equals(country, address.country) && Objects.equals(state, address.state) && Objects.equals(zip, address.zip) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, state, zip, city, primary);
    }
}
