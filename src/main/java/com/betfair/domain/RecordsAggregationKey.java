package com.betfair.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by kuzmende on 11/2/17.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RecordsAggregationKey {

    private String selectionName;
    private Currency currency;
}
