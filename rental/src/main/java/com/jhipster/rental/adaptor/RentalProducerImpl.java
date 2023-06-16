package com.jhipster.rental.adaptor;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.rental.domain.event.BookCatalogChanged;
import com.jhipster.rental.domain.event.PointChanged;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * RentalProducerImpl.java
 *
 * @author sgh
 * @since 2023.06.16
 */
@Service
public class RentalProducerImpl implements RentalProducer {
    private final Logger log = LoggerFactory.getLogger(RentalProducer.class);

    // 토픽명
    private static final String TOPIC_BOOK = "topic_book";
    private static final String TOPIC_CATALOG = "topic_catalog";
    private static final String TOPIC_POINT = "topic_point";

    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RentalProducerImpl(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        log.info("Kafka producer initializing..");
        this.producer = new KafkaProducer<>(kafkaProperties.buildProducerProperties());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("kafka producer initialized");
    }

    // 도서 서비스의 도서 상태 변경에 대한 카프카 메시지 발행
    @Override
    public void updateBookStatus(Long bookId, String bookStatus) throws
        ExecutionException,
        InterruptedException,
        JsonProcessingException {
        StockChanged stockChanged = new StockChanged(bookId, bookStatus);
        String message = objectMapper.writeValueAsString(stockChanged);
        producer.send(new ProducerRecord(TOPIC_BOOK, message)).get();
    }

    // 사용자 서비스의 포인트 적립에 대한 카프카 메시지 발행
    @Override
    public void savePoints(Long userId, int points) throws
        ExecutionException,
        InterruptedException,
        JsonProcessingException {
        PointChanged pointChanged = new PointChanged(userId, points);
        String message = objectMapper.writeValueAsString(pointChanged);
        producer.send(new ProducerRecord<>(TOPIC_POINT, message));
    }

    // 도서 카탈로그 서비스의 도서 상태 변경에 대한 카프카 메시지 발행
    @Override
    public void updateBookCatalogStatus(Long bookId, String eventType) throws
        ExecutionException,
        InterruptedException,
        JsonProcessingException {
        BookCatalogChanged bookCatalogChanged = new BookCatalogChanged();
        bookCatalogChanged.setBookId(bookId);
        bookCatalogChanged.setEventType(eventType);
        String message = objectMapper.writeValueAsString(bookCatalogChanged);
        producer.send(new ProducerRecord<>(TOPIC_CATALOG, message));
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown kafka producer");
        producer.close();
    }
}
