package com.betfair.services;

import com.betfair.domain.BettingRecord;
import com.betfair.domain.Currency;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by kuzmende on 11/1/17.
 */
@Service
public class RecordsAggregator implements Function<Stream<BettingRecord>, Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>>> {

    @Override
    public Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>> apply(Stream<BettingRecord> bettingRecords) {
        return bettingRecords
                .collect(groupingBy(a -> Tuple.tuple(a.getSelectionName(), a.getCurrency()),
                        Tuple.collectors(
                                Collectors.counting(),
                                Collectors.summingDouble(BettingRecord::getStake),
                                Collectors.summingDouble(BettingRecord::getBetPayout))));
    }
}
