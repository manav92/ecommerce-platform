package com.eshop.order.messaging;

import com.eshop.order.dto.OrderPlacedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final ObjectMapper objectMapper = new ObjectMapper();
    Logger Log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderPlaced(OrderPlacedEvent event) {
        try {
            Log.info("Start to Publish");
            kafkaTemplate.send("order.placed", event.orderId().toString(), objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException exception){
            Log.error("publishOrderPlaced" + exception);
        }
    }
}
