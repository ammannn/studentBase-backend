package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.Currency;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Amount {
    private long amount;
    private Currency currency;
    private String currencySymbol;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount1 = (Amount) o;
        return amount == amount1.amount && currency == amount1.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
