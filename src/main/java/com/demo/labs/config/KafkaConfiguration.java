package com.demo.labs.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.kafka.listener.*;


@Configuration
public class KafkaConfiguration {
    @Bean
    @Primary
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<?, ?> template) {
        // 创建 DeadLetterPublishingRecoverer 对象
        ConsumerRecordRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        // 创建 FixedBackOff 对象
        BackOff backOff = new FixedBackOff(10 * 1000L, 3L);
        // 创建 SeekToCurrentErrorHandler 对象
        return new DefaultErrorHandler (recoverer, backOff);
    }


    @Bean
    public NewTopic demo01Topic() {
        return new NewTopic("DEMO_01", 3, (short) 1);
    }
}
