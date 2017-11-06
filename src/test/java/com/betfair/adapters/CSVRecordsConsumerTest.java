package com.betfair.adapters;

import com.betfair.domain.BettingRecord;
import com.betfair.domain.Currency;
import org.beanio.StreamFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.betfair.config.Config.BETTING_RECORDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kuzmende on 11/5/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CSVRecordsConsumerTest {

    private RecordsConsumer<BettingRecord> recordRecordsConsumer;

    public CSVRecordsConsumerTest() throws IOException {
        StreamFactory factory = StreamFactory.newInstance();

        factory.load(CSVRecordsConsumerTest.class.getClassLoader()
                .getResourceAsStream("betting-record.xml"));

        recordRecordsConsumer = new CSVRecordsConsumer<>(
                CSVRecordsConsumerTest.class.getClassLoader()
                        .getResourceAsStream("input/betting-records-cutted.csv"),
                factory,
                BETTING_RECORDS);
    }

    @Test
    public void processBettingRecordsFromCSVFile() throws InterruptedException, ExecutionException, IOException {
        List<BettingRecord> bettingRecords = recordRecordsConsumer.collect(Collectors.toList());

        BettingRecord record1 = new BettingRecord();
        record1.setBetId("Bet-10");
        record1.setBetTimeStamp(1489490156000L);
        record1.setSelectionId(1L);
        record1.setSelectionName("Selection-1");
        record1.setStake(0.5);
        record1.setPrice(6.0);
        record1.setCurrency(Currency.GBP);

        BettingRecord record2 = new BettingRecord();
        record2.setBetId("Bet-11");
        record2.setBetTimeStamp(1489490156000L);
        record2.setSelectionId(2L);
        record2.setSelectionName("Selection-2");
        record2.setStake(1.25);
        record2.setPrice(4.0);
        record2.setCurrency(Currency.EUR);

        assertEquals(2, bettingRecords.size());

        assertTrue(bettingRecords.contains(record1));
        assertTrue(bettingRecords.contains(record2));
    }
}