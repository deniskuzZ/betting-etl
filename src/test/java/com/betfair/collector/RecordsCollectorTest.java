package com.betfair.collector;

import com.betfair.collectors.RecordsCollector;
import com.betfair.domain.BettingRecord;
import com.betfair.domain.Currency;
import com.betfair.domain.RecordsAggregationKey;
import com.betfair.domain.RecordsAggregationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kuzmende on 11/5/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class RecordsCollectorTest {

    @Test
    public void groupByNameAndCurrency(){
        BettingRecord record1 = new BettingRecord();
        record1.setBetId("Bet-10");
        record1.setBetTimeStamp(1489490156000L);
        record1.setSelectionId(1L);
        record1.setSelectionName("Selection-1");
        record1.setStake(0.5);
        record1.setPrice(6.0);
        record1.setCurrency(Currency.EUR);

        BettingRecord record2 = new BettingRecord();
        record2.setBetId("Bet-11");
        record2.setBetTimeStamp(1489490156000L);
        record2.setSelectionId(2L);
        record2.setSelectionName("Selection-1");
        record2.setStake(1.25);
        record2.setPrice(4.0);
        record2.setCurrency(Currency.EUR);

        BettingRecord record3 = new BettingRecord();
        record3.setBetId("Bet-11");
        record3.setBetTimeStamp(1489490156000L);
        record3.setSelectionId(2L);
        record3.setSelectionName("Selection-2");
        record3.setStake(1.25);
        record3.setPrice(4.0);
        record3.setCurrency(Currency.EUR);

        Map<RecordsAggregationKey, RecordsAggregationResult> aggregationMap = Stream.of(record1, record2, record3).collect(
                RecordsCollector.groupByNameAndCurrency());

        RecordsAggregationKey expectedAggregarionKey1 = new RecordsAggregationKey("Selection-1", Currency.EUR);
        RecordsAggregationResult expectedAggregarionResult1 = new RecordsAggregationResult(2L, 1.75, 8.0);

        RecordsAggregationKey expectedAggregarionKey2 = new RecordsAggregationKey("Selection-2", Currency.EUR);
        RecordsAggregationResult expectedAggregarionResult2 = new RecordsAggregationResult(1L, 1.25, 5.0);

        assertEquals(2, aggregationMap.size());

        assertTrue(aggregationMap.containsKey(expectedAggregarionKey1));
        assertEquals(aggregationMap.get(expectedAggregarionKey1), expectedAggregarionResult1);

        assertTrue(aggregationMap.containsKey(expectedAggregarionKey2));
        assertEquals(aggregationMap.get(expectedAggregarionKey2), expectedAggregarionResult2);
    }
}
