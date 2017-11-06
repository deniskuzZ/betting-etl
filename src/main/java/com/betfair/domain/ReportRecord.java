package com.betfair.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.betfair.utils.CurrencyUtil.formatWithCurrency;
import static com.betfair.utils.CurrencyUtil.toEUR;

/**
 * Created by kuzmende on 10/31/17.
 */
@Getter
@EqualsAndHashCode
public class ReportRecord {

    private String selectionName;
    private Currency currency;

    private Long betsNumber;
    private Double totalPayoutInEur;

    private String totalStakes;
    private String totalPayout;

    public ReportRecord(RecordsAggregationKey aggregationKey, RecordsAggregationResult aggregationResult) {
        this.selectionName = aggregationKey.getSelectionName();
        this.currency = aggregationKey.getCurrency();

        this.betsNumber = aggregationResult.getBetsNumber();

        this.totalStakes = formatWithCurrency(
                aggregationResult.getTotalStakes(), currency);

        this.totalPayout = formatWithCurrency(
                aggregationResult.getTotalPayout(), currency);

        this.totalPayoutInEur = toEUR(
                aggregationResult.getTotalPayout(), currency);
    }
}
