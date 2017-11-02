package com.betfair.services;

import com.betfair.domain.ReportRecord;
import com.betfair.domain.Currency;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kuzmende on 11/2/17.
 */
@Service
public class RecordsCombiner {

    public List<ReportRecord> sort(Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>> bettingRecords){
        return bettingRecords.entrySet().stream()
                .map(e -> new ReportRecord(e.getKey().v1, e.getKey().v2,
                        e.getValue().v1, e.getValue().v2, e.getValue().v3))
                .sorted(Comparator.comparing(ReportRecord::getTotalPayoutInEur).reversed())
                .collect(Collectors.toList());
    }
}
