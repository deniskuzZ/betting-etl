package com.betfair.adapters;

import com.betfair.domain.ReportRecord;

import java.util.List;

/**
 * Created by kuzmende on 11/2/17.
 */
public interface ReportProducer {

    void accept(List<ReportRecord> records);
}
