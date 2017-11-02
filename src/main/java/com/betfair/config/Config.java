package com.betfair.config;

import org.beanio.StreamFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kuzmende on 11/1/17.
 */
@Configuration
public class Config {

    @Bean
    public StreamFactory streamFactory(){
        StreamFactory factory = StreamFactory.newInstance();
        factory.load("/Users/kuzmende/projects/betting-etl/src/main/resources/betting-record.xml");

        return factory;
    }
}
