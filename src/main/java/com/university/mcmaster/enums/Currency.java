package com.university.mcmaster.enums;

import lombok.Getter;

@Getter
public enum Currency {
    usd("$"),inr("₹"),cad("ca$"),gbp("£");
    private String symbol;
    Currency(String symbol){
        this.symbol = symbol;
    }
}
