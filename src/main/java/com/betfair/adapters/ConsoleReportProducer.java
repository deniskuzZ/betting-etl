package com.betfair.adapters;

import com.betfair.domain.ReportRecord;
import com.betfair.builders.ConsoleReportBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kuzmende on 11/2/17.
 */
@Service
public class ConsoleReportProducer implements ReportProducer {

    @Autowired
    private ConsoleReportBuilder reportBuilder;

    @Override
    public void accept(List<ReportRecord> records) {
        System.out.print(reportBuilder.create(records));
    }
}
