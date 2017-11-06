package com.betfair.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by kuzmende on 11/4/17.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RecordsAggregationResult {

    private Long betsNumber;
    private Double totalStakes;
    private Double totalPayout;
}
