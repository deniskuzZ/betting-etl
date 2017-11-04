package com.betfair.adapters;

import java.util.List;

/**
 * Created by kuzmende on 11/2/17.
 */
public interface ReportProducer<T> {

    void submit(List<T> data);
}
