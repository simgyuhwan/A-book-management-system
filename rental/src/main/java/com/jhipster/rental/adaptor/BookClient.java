package com.jhipster.rental.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jhipster.rental.config.FeignConfiguration;
import com.jhipster.rental.web.rest.dto.BookInfoDTO;

/**
 * BookClient.java
 * 도서 마이크로서비스 Client
 *
 * @author sgh
 * @since 2023.06.16
 */
@FeignClient(name = "book", configuration = {FeignConfiguration.class})
public interface BookClient {

    @GetMapping("/api/books/bookInfo/{bookId}")
    ResponseEntity<BookInfoDTO> findBookInfo(@PathVariable("bookId") Long bookId);
}
