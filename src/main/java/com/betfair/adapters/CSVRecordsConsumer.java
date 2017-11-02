package com.betfair.adapters;

import com.betfair.domain.BettingRecord;
import com.betfair.domain.Currency;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by kuzmende on 10/31/17.
 */
@Service
public class CSVRecordsConsumer implements RecordsConsumer {

    @Autowired
    private StreamFactory factory;

    public Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>> apply(Function<Stream<BettingRecord>, Callable<Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>>>> function) throws IOException, ExecutionException, InterruptedException {
        BeanReader in = factory.createReader("betting-records",
                new File("/Users/kuzmende/projects/betting-etl/input.csv"));

        File inputF = new File("/Users/kuzmende/projects/betting-etl/input.csv");

        Callable<Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>>> task;
        Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>> result;

        InputStream inputFS = new FileInputStream(inputF);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputFS))) {
            task = function.apply(
                    br.lines().skip(1).parallel().map(t -> (BettingRecord)
                            factory.createUnmarshaller("betting-records").unmarshal(t)));

            ForkJoinPool forkJoinPool = new ForkJoinPool(4);
            result = forkJoinPool.submit(task).get();
        }
        return result;
    }
}
