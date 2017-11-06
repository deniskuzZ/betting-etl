package com.betfair.builders;

import com.betfair.adapters.CSVRecordsConsumerTest;
import com.betfair.domain.Currency;
import com.betfair.domain.RecordsAggregationKey;
import com.betfair.domain.RecordsAggregationResult;
import com.betfair.domain.ReportRecord;
import com.betfair.utils.CurrencyUtil;
import org.beanio.StreamFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.betfair.config.Config.BETTING_REPORT_RECORDS;
import static com.betfair.config.Config.REPORT_HEADERS;
import static org.junit.Assert.assertEquals;

/**
 * Created by kuzmende on 11/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ASCIITableReportBuilderTest {

    private ASCIITableReportBuilder<ReportRecord> asciiTableReportBuilder;

    public ASCIITableReportBuilderTest() throws IOException {
        CurrencyUtil.exchrate_to_EUR = Collections.singletonMap("GBP", 1.14);
        StreamFactory factory = StreamFactory.newInstance();

        factory.load(CSVRecordsConsumerTest.class.getClassLoader()
                .getResourceAsStream("betting-record.xml"));

        asciiTableReportBuilder = new ASCIITableReportBuilder<>(
                factory,
                BETTING_REPORT_RECORDS,
                REPORT_HEADERS
        );
    }

    @Test
    public void asciiTableReport() throws IOException {
        RecordsAggregationKey aggregarionKey1 = new RecordsAggregationKey("Selection-1", Currency.EUR);
        RecordsAggregationResult aggregarionResult1 = new RecordsAggregationResult(2L, 1.75, 8.0);

        RecordsAggregationKey aggregarionKey2 = new RecordsAggregationKey("Selection-2", Currency.GBP);
        RecordsAggregationResult aggregarionResult2 = new RecordsAggregationResult(1L, 1.25, 5.0);

        ReportRecord record1 = new ReportRecord(aggregarionKey1, aggregarionResult1);
        ReportRecord record2 = new ReportRecord(aggregarionKey2, aggregarionResult2);

        String asciiReportOut = asciiTableReportBuilder.create(
                Stream.of(record1, record2).collect(Collectors.toList()));

        URL reportOutFile = ASCIITableReportBuilderTest.class.getClassLoader()
                .getResource("expected/betting-report-cutted.out");

        String expectedReportOut = new String(
                Files.readAllBytes(Paths.get(reportOutFile.getPath())));

        assertEquals(expectedReportOut, asciiReportOut);
    }
}
