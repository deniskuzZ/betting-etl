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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by kuzmende on 11/1/17.
 */
@Configuration
public class Config {

    @Value("${filePath}")
    private String filePath;

    @Value("classpath:betting-record.xml")
    private File beanioConfig;

    @Value("#{${exchrate_to_EUR}}")
    private Map<String, Double> exchrate_to_EUR;


    @Bean
    public StreamFactory streamFactory(){
        StreamFactory factory = StreamFactory.newInstance();
        factory.load(beanioConfig);

        return factory;
    }

    @Bean
    public RecordsConsumer<BettingRecord> csvRecordsConsumer(StreamFactory streamFactory) throws FileNotFoundException {
        RecordsConsumer<BettingRecord> csvRecordRecordsConsumer = new CSVRecordsConsumer<>(
                new FileInputStream(filePath),
                streamFactory,
                "betting-records"
        );
        return csvRecordRecordsConsumer;
    }

    @Bean
    public ASCIITableReportBuilder<ReportRecord> consoleReportBuilder(StreamFactory streamFactory){
        ASCIITableReportBuilder<ReportRecord> consoleReportBuilder = new ASCIITableReportBuilder<>(
                streamFactory,
                "betting-report-records"
        );
        return consoleReportBuilder;
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
