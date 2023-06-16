package com.jhipster.rental.adaptor;

import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * RentalProducer.java
 * 메시지 발송
 *
 * @author sgh
 * @since 2023.06.16
 */
public interface RentalProducer {

    // 도서 서비스의 도서 상태 변경
    void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException,
        JsonProcessingException;

    // 사용자 서비스의 포인트 적립
    void savePoints(Long userId, int pointPerBooks) throws ExecutionException, InterruptedException, JsonProcessingException;

    // 도서 카탈로그 서비스의 도서 상태 변경
    void updateBookCatalogStatus(Long bookId, String eventType) throws ExecutionException, InterruptedException, JsonProcessingException;
}
