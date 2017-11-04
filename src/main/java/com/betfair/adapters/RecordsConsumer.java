package com.betfair.adapters;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;

/**
 * Created by kuzmende on 11/2/17.
 */
public interface RecordsConsumer<T> {

    <R, A> R collect(Collector<? super T, A, R> collector) throws IOException, ExecutionException, InterruptedException;
}
