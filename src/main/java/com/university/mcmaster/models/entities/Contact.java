package com.university.mcmaster.models.entities;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    private String email;
    private String phoneNumber;
    private String name;
    private String preferredModOfContact;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(getEmail(), contact.getEmail()) && Objects.equals(getPhoneNumber(), contact.getPhoneNumber()) && Objects.equals(getName(), contact.getName()) && Objects.equals(getPreferredModOfContact(), contact.getPreferredModOfContact());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPhoneNumber(), getName(), getPreferredModOfContact());
    }
}
