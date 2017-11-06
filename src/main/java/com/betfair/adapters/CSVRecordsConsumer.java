package com.betfair.adapters;

import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collector;

/**
 * Created by kuzmende on 10/31/17.
 */
public class CSVRecordsConsumer<T> implements RecordsConsumer<T> {

    private static final String CONSUMER_PARALLELISM = "consumer.ForkJoinPool.parallelism";
    private static final String DEFAULT_PARALLELISM = "4";

    private StreamFactory streamFactory;
    private String objectMapping;

    private final InputStream inputStream;
    private final int parallelism;

    public CSVRecordsConsumer(InputStream inputStream, StreamFactory factory, String objectMapping) {
        this.inputStream = inputStream;

        this.parallelism = Integer.valueOf(
                System.getProperty(CONSUMER_PARALLELISM, DEFAULT_PARALLELISM));

        this.streamFactory = factory;
        this.objectMapping = objectMapping;
    }

    private ThreadLocal<Unmarshaller> unmarshaller = ThreadLocal.withInitial(
            () -> streamFactory.createUnmarshaller(objectMapping));

    @SuppressWarnings("unchecked")
    public <R, A> R collect(Collector<? super T, A, R> collector) throws IOException, ExecutionException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        Callable<R> task = () -> br.lines().skip(1).parallel()
                .map(t -> (T) unmarshaller.get().unmarshal(t))
                .collect(collector);

        ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
        return forkJoinPool.submit(task).get();
    }
}
