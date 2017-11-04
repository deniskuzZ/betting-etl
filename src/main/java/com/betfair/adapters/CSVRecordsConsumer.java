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

    private StreamFactory streamFactory;
    private String objectMapping;

    private InputStream inputStream;

    public CSVRecordsConsumer(InputStream inputStream, StreamFactory factory, String objectMapping) {
        this.inputStream = inputStream;

        this.streamFactory = factory;
        this.objectMapping = objectMapping;
    }

    private ThreadLocal<Unmarshaller> unmarshaller = ThreadLocal.withInitial(
            () -> streamFactory.createUnmarshaller(objectMapping));

    @SuppressWarnings("unchecked")
    public <R, A> R collect(Collector<? super T, A, R> collector) throws IOException, ExecutionException, InterruptedException {
        R result;

        try (InputStreamReader is = new InputStreamReader(inputStream); BufferedReader br = new BufferedReader(is)) {
            Callable<R> task = () -> br.lines().skip(1).parallel()
                    .map(t -> (T) unmarshaller.get().unmarshal(t))
                    .collect(collector);

            ForkJoinPool forkJoinPool = new ForkJoinPool(4);
            result = forkJoinPool.submit(task).get();
        }
        return result;
    }
}
