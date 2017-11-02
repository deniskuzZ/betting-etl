package com.betfair.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by kuzmende on 10/31/17.
 */

@Getter
@Setter
@NoArgsConstructor
public class BettingRecord {

    private String betId;
    private Long betTimeStamp;
    private Long selectionId;
    private String selectionName;
    private Double stake;
    private Double price;
    private Currency currency;

    public Double getBetPayout(){
        return stake*price;
    }

}
