package com.betfair.domain;

/**
 * Created by kuzmende on 11/1/17.
 */
public enum Currency {
    GBP("£"),
    EUR("€");

    private String sign;

    Currency(String sign) {
        this.sign = sign;
    }

    public String getSign(){
        return sign;
    }
}
