package com.betfair.utils;

import com.betfair.domain.Currency;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by kuzmende on 11/4/17.
 */
public final class CurrencyUtil {

    private static final String DECIMAL_FORMAT = "0.00";

    public static Map<String, Double> exchrate_to_EUR;

    public static Double toEUR(Double value, Currency currency){
        return Currency.EUR != currency ?
                value * exchrate_to_EUR.get(currency.name()) : value;
    }

    public static String formatWithCurrency(Double value, Currency currency) {
        return new DecimalFormat(currency.getSign() + DECIMAL_FORMAT).format(value);
    }
}
