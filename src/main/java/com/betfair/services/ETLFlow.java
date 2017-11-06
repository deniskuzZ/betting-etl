package com.betfair.services;

import com.betfair.adapters.RecordsConsumer;
import com.betfair.adapters.ReportProducer;
import com.betfair.collectors.RecordsCollector;
import com.betfair.domain.BettingRecord;
import com.betfair.domain.RecordsAggregationKey;
import com.betfair.domain.RecordsAggregationResult;
import com.betfair.domain.ReportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by kuzmende on 11/2/17.
 */
@Service
public class ETLFlow {

    private final ReportProducer<ReportRecord> producer;
    private final RecordsConsumer<BettingRecord> consumer;

    @Autowired
    public ETLFlow(RecordsConsumer<BettingRecord> consumer, ReportProducer<ReportRecord> producer) {
        this.consumer = consumer;
        this.producer = producer;
    }

    public void execute() throws IOException, ExecutionException, InterruptedException {
        Map<RecordsAggregationKey, RecordsAggregationResult> aggregationMap = consumer.collect(
                RecordsCollector.groupByNameAndCurrency());

        List<ReportRecord> reportRecords = aggregationMap.entrySet().stream()
                .map(entry -> new ReportRecord(entry.getKey(), entry.getValue()))

                .sorted(Comparator.comparing(ReportRecord::getTotalPayoutInEur).reversed())
                .collect(Collectors.toList());

        producer.submit(reportRecords);
    }
}
