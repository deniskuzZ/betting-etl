package com.betfair.config;

import com.betfair.adapters.CSVRecordsConsumer;
import com.betfair.adapters.ConsoleReportProducer;
import com.betfair.adapters.RecordsConsumer;
import com.betfair.builders.ASCIITableReportBuilder;
import com.betfair.domain.BettingRecord;
import com.betfair.domain.ReportRecord;
import com.betfair.utils.CurrencyUtil;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by kuzmende on 11/1/17.
 */
@Configuration
public class Config {

    public static final String BETTING_RECORDS = "betting-records";
    public static final String BETTING_REPORT_RECORDS = "betting-report-records";

    public static final Object[] REPORT_HEADERS =
            {"Selection Name", "Currency", "No Of Bets", "Total Stakes", "Total Payout"};

    @Value("${inputPath}")
    private File inputPath;

    @Value("classpath:betting-record.xml")
    private Resource beanioConfig;

    @Value("#{${exchrate_to_EUR}}")
    private Map<String, Double> exchrate_to_EUR;

    @Bean
    public StreamFactory streamFactory() throws IOException {
        StreamFactory factory = StreamFactory.newInstance();
        factory.load(beanioConfig.getInputStream());

        return factory;
    }

    @Bean
    public RecordsConsumer<BettingRecord> csvRecordsConsumer(StreamFactory streamFactory) throws FileNotFoundException {
        RecordsConsumer<BettingRecord> csvRecordRecordsConsumer = new CSVRecordsConsumer<>(
                new FileInputStream(inputPath),
                streamFactory,
                BETTING_RECORDS
        );
        return csvRecordRecordsConsumer;
    }

    @Bean
    public ASCIITableReportBuilder<ReportRecord> asciiTableReportBuilder(StreamFactory streamFactory){
        ASCIITableReportBuilder<ReportRecord> asciiTableReportBuilder = new ASCIITableReportBuilder<>(
                streamFactory,
                BETTING_REPORT_RECORDS,
                REPORT_HEADERS
        );
        return asciiTableReportBuilder;
    }

    @Bean
    public ConsoleReportProducer<ReportRecord> consoleReportProducer(ASCIITableReportBuilder<ReportRecord> consoleReportBuilder){
        ConsoleReportProducer<ReportRecord> consoleReportProducer = new ConsoleReportProducer<>(
                consoleReportBuilder
        );
        return consoleReportProducer;
    }

    @PostConstruct
    private void init() {
        CurrencyUtil.exchrate_to_EUR = exchrate_to_EUR;
    }
}
