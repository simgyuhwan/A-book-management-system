package com.jhipster.rental.adaptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * StockChanged.java
 *
 * @author sgh
 * @since 2023.06.16
 */
@Getter
@Setter
@AllArgsConstructor
public class StockChanged {
    // 도서 일련번호
    private Long bookId;
    // 도서 상태
    private String bookStatus;
}
