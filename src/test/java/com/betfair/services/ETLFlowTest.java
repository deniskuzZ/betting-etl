package com.betfair.services;

import com.betfair.adapters.RecordsConsumer;
import com.betfair.adapters.ReportProducer;
import com.betfair.domain.*;
import com.betfair.utils.CurrencyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;

import static org.mockito.Mockito.times;

/**
 * Created by kuzmende on 11/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ETLFlowTest {

    @Mock
    private ReportProducer<ReportRecord> producer;

    @Mock
    private RecordsConsumer<BettingRecord> consumer;

    @Spy
    @InjectMocks
    private ETLFlow etlFlow;

    @Captor
    private ArgumentCaptor<List<ReportRecord>> argumentCaptor;

    @Test
    @SuppressWarnings("unchecked")
    public void execute() throws InterruptedException, ExecutionException, IOException {
        CurrencyUtil.exchrate_to_EUR = Collections.singletonMap("GBP", 1.14);

        RecordsAggregationKey aggregarionKey1 = new RecordsAggregationKey("Selection-1", Currency.EUR);
        RecordsAggregationResult aggregarionResult1 = new RecordsAggregationResult(2L, 1.75, 8.0);

        RecordsAggregationKey aggregarionKey2 = new RecordsAggregationKey("Selection-2", Currency.GBP);
        RecordsAggregationResult aggregarionResult2 = new RecordsAggregationResult(1L, 1.45, 7.5);

        Map<RecordsAggregationKey, RecordsAggregationResult> aggregationMap = new HashMap<RecordsAggregationKey, RecordsAggregationResult>(){{
            put(aggregarionKey1, aggregarionResult1);
            put(aggregarionKey2, aggregarionResult2);
        }};

        Mockito.when(consumer.collect(Mockito.any(Collector.class))).thenReturn(aggregationMap);
        etlFlow.execute();

        Mockito.verify(producer, times(1))
                .submit(argumentCaptor.capture());

        Assert.assertEquals(2, argumentCaptor.getValue().size());

        Assert.assertEquals("£7.50", argumentCaptor.getValue().get(0).getTotalPayout());
        Assert.assertEquals("€8.00", argumentCaptor.getValue().get(1).getTotalPayout());

        Assert.assertEquals(new ReportRecord(aggregarionKey2, aggregarionResult2),
                argumentCaptor.getValue().get(0));

        Assert.assertEquals(new ReportRecord(aggregarionKey1, aggregarionResult1),
                argumentCaptor.getValue().get(1));
    }
}
