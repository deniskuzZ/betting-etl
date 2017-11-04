package com.betfair.collectors;

import com.betfair.domain.BettingRecord;
import com.betfair.domain.RecordsAggregationKey;
import com.betfair.domain.RecordsAggregationResult;
import org.jooq.lambda.tuple.Tuple;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

/**
 * Created by kuzmende on 11/1/17.
 */
public final class RecordsCollector {

    public static Collector<BettingRecord, ?, Map<RecordsAggregationKey, RecordsAggregationResult>> groupByNameAndCurrency() {
        Collector<BettingRecord, ?, RecordsAggregationResult> compositeCollector =
                Collectors.collectingAndThen(
                        Tuple.collectors(
                                counting(),
                                Collectors.summingDouble(BettingRecord::getStake),
                                Collectors.summingDouble(BettingRecord::getBetPayout)),

                        accumulator -> new RecordsAggregationResult(
                                accumulator.v1, accumulator.v2, accumulator.v3)
                );

        return Collectors.groupingBy(
                record -> new RecordsAggregationKey(
                        record.getSelectionName(), record.getCurrency()),
                compositeCollector
        );
    }
}

