package com.adidas.backend.prioritysaleservice.infrastructure.driven_adapter;

import com.adidas.backend.prioritysaleservice.domain.Event;
import com.adidas.backend.prioritysaleservice.domain.ThreadLocalData;
import com.adidas.backend.prioritysaleservice.infrastructure.driven_adapter.gateways.PublisherGateway;
import org.apache.kafka.common.config.ConfigException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class PublisherAdapter implements PublisherGateway {

    @Autowired
    private KafkaTemplate<String, Object> producer;

    @Value("${topic.exception.name}")
    private String exception;

    @Retryable(value = { ConfigException.class }, maxAttempts = 3, backoff = @Backoff(delay = 60000))
    public void publishException(Event event) {
        this.producer.send(exception, event);
        ThreadLocalData.setValue(Strings.EMPTY);
    }

}
