package com.betfair.adapters;

import com.betfair.builders.ASCIITableReportBuilder;
import com.betfair.domain.ReportRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

/**
 * Created by kuzmende on 11/5/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConsoleReportProducerTest {

    @Mock
    private ASCIITableReportBuilder<ReportRecord> asciiTableReportBuilder;

    @Spy
    @InjectMocks
    private ConsoleReportProducer<ReportRecord> consoleReportProducer;

    @Captor
    private ArgumentCaptor<List<ReportRecord>> argumentCaptor;

    @Test
    public void builderWithCorrectArgsInvoked(){
        consoleReportProducer.submit(Collections.emptyList());

        Mockito.verify(asciiTableReportBuilder, times(1))
                .create(argumentCaptor.capture());

        assertEquals(Collections.emptyList(), argumentCaptor.getValue());
    }
}
