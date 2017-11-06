package com.betfair;

import com.betfair.builders.ASCIITableReportBuilder;
import com.betfair.config.Config;
import com.betfair.domain.ReportRecord;
import com.betfair.services.ETLFlow;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.List;

import static com.betfair.config.Config.BETTING_REPORT_RECORDS;
import static org.junit.Assert.assertEquals;

/**
 * Created by kuzmende on 11/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(value = "com.betfair")

@Import(Config.class)
@PropertySource("classpath:application-test.properties")
public class ApplicationITest {

    private static final String BETTING_REPORT_OUT = "expected/betting-report.out";
    private static final String BETTING_REPORT_CSV = "expected/betting-report.csv";

    @Autowired
    private ETLFlow etlFlow;

    @Autowired
    private StreamFactory streamFactory;

    @SpyBean
    private ASCIITableReportBuilder<ReportRecord> asciiTableReportBuilder;

    @Test
    public void etlFlow() throws Exception {
        Resource reportOutFile = new ClassPathResource(BETTING_REPORT_OUT);

        String expectedReportOut = new String(
                Files.readAllBytes(reportOutFile.getFile().toPath()));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<ReportRecord>> actualReportRecordsList =
                ArgumentCaptor.forClass((Class) List.class);

        Mockito.doAnswer(new CallsRealMethods() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object actualReportOut = super.answer(invocation);

                assertEquals(expectedReportOut, actualReportOut);
                return actualReportOut;
            }

        }).when(asciiTableReportBuilder).create(actualReportRecordsList.capture());

        etlFlow.execute();

        validateReportRecords(actualReportRecordsList);
    }

    private void validateReportRecords(ArgumentCaptor<List<ReportRecord>> actualReportRecordsList) throws IOException {
        Resource reportRecordsFile = new ClassPathResource(BETTING_REPORT_CSV);

        String expectedReportRecords = new String(
                Files.readAllBytes(reportRecordsFile.getFile().toPath()));

        StringWriter actualReportRecords = new StringWriter();
        BeanWriter out = streamFactory.createWriter(BETTING_REPORT_RECORDS, actualReportRecords);

        actualReportRecordsList.getValue().forEach(out::write);
        out.flush();

        assertEquals(expectedReportRecords, actualReportRecords.toString().trim());
        out.close();
    }
}
