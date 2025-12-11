package com.example.usermanagement.producer;


import com.example.usermanagement.entity.User;
import com.example.usermanagement.dto.UserEvent;
import com.example.usermanagement.dto.UserEventType;

import com.example.usermanagement.exception.EventPublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.topic.user-events}")
    private String userEventsTopic;

    public void sendUserEvent(User user, UserEventType eventType) {
        UserEvent event = createUserEvent(user, eventType);
        String key = user.getId();

        try {
            CompletableFuture<SendResult<String, UserEvent>> future =
                    kafkaTemplate.send(userEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    handleFailure(user, eventType, ex);
                } else {
                    log.info("Sent {} event for user: {} (partition: {})",
                            eventType,
                            user.getEmail(),
                            result.getRecordMetadata().partition());
                }
            });
        } catch (Exception ex) {
            handleFailure(user, eventType, ex);
            throw new EventPublishException("Failed to publish user event", ex);
        }
    }

    private void handleFailure(User user, UserEventType eventType, Throwable ex) {
        log.error("Failed to send {} event for user: {}. Error: {}",
                eventType, user.getEmail(), ex.getMessage(), ex);
        // You can add additional error handling here, like metrics or retry logic
    }

    //UMBE
    private UserEvent createUserEvent(User user, UserEventType eventType) {
        return UserEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .build();
    }
}