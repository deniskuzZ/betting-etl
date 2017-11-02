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
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by kuzmende on 10/31/17.
 */
@Service
public class CSVRecordsConsumer implements RecordsConsumer {

    @Autowired
    private StreamFactory factory;


    public Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>> apply(Function<Stream<BettingRecord>, Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>>> function) throws IOException {
        BeanReader in = factory.createReader("betting-records",
                new File("/Users/kuzmende/projects/betting-etl/input.csv"));

        Unmarshaller marshaller = factory.createUnmarshaller("betting-records");
        File inputF = new File("/Users/kuzmende/projects/betting-etl/input.csv");

        Map<Tuple2<String, Currency>, Tuple3<Long, Double, Double>> result;
        InputStream inputFS = new FileInputStream(inputF);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputFS))) {
            result = function.apply(
                    br.lines().skip(1).map(t -> (BettingRecord) marshaller.unmarshal(t)));
        }
        return result;
    }
}
