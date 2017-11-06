package com.betfair.adapters;

import com.betfair.builders.ASCIITableReportBuilder;

import java.util.List;

/**
 * Created by kuzmende on 11/2/17.
 */
public class ConsoleReportProducer<T> implements ReportProducer<T> {

    private final ASCIITableReportBuilder<T> reportBuilder;

    public ConsoleReportProducer(ASCIITableReportBuilder<T> reportBuilder) {
        this.reportBuilder = reportBuilder;
    }

    @Override
    public void submit(List<T> data) {
        String report = reportBuilder.create(data);

        System.out.print(report);
    }
}
