package com.betfair.adapters;

import com.betfair.domain.BettingRecord;
import com.betfair.domain.Currency;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by kuzmende on 11/2/17.
 */
public interface RecordsConsumer {

    Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>> apply(Function<Stream<BettingRecord>, Callable<Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>>>> function) throws IOException, ExecutionException, InterruptedException;
}
