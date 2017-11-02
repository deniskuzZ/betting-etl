package com.betfair.domain;

import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

/**
 * Created by kuzmende on 10/31/17.
 */
@Getter
@Setter
public class ReportRecord {

    private String selectionName;
    private Currency currency;
    private Long betsNumber;
    private Double totalStakes;
    private Double totalPayout;

    private Double totalPayoutInEur;

    private String totalStakesWithCurrency;
    private String totalPayoutWithCurrency;

    public ReportRecord(String selectionName, Currency currency, Long betsNumber, Double totalStakes, Double totalPayout) {
        this.selectionName = selectionName;
        this.currency = currency;
        this.betsNumber = betsNumber;
        this.totalStakes = totalStakes;
        this.totalPayout = totalPayout;

        totalPayoutInEur = Currency.GBP == currency ? totalPayout * 1.14 : totalPayout;

        totalStakesWithCurrency = formatWithCurrency(totalStakes);
        totalPayoutWithCurrency = formatWithCurrency(totalPayout);
    }

    private String formatWithCurrency(Double value) {
        return new DecimalFormat(currency.getSign()+"#.00").format(value);
    }
}
