package com.alanpatrik.ecommerce_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CircuitBreakerConfiguration {

//    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerConfiguration.class);
//
////    @Bean
//    public RegistryEventConsumer<CircuitBreaker> cbLog() {
//        return new RegistryEventConsumer<CircuitBreaker>() {
//            @Override
//            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
////                entryAddedEvent
////                        .getAddedEntry()
////                        .getEventPublisher()
////                        .onStateTransition(event -> {
////                            logger.info(event.toString());
////                        });
//            }
//
//            @Override
//            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
//
//            }
//
//            @Override
//            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
//
//            }
//        };
//    }
}
