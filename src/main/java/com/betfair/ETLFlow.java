package com.betfair;

import com.betfair.adapters.RecordsConsumer;
import com.betfair.adapters.ReportProducer;
import com.betfair.domain.ReportRecord;
import com.betfair.services.RecordsAggregator;
import com.betfair.services.RecordsCombiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by kuzmende on 11/2/17.
 */
@Service
public class ETLFlow {

    @Autowired
    private ReportProducer producer;

    @Autowired
    private RecordsConsumer consumer;

    @Autowired
    private RecordsAggregator aggregator;

    @Autowired
    private RecordsCombiner combiner;

    public void execute() throws IOException {
        List<ReportRecord> reportRecords = combiner.sort(consumer.apply(aggregator));
        producer.accept(reportRecords);
    }
}
