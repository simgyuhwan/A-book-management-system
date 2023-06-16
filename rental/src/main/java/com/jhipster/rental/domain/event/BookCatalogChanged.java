package com.jhipster.rental.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * BookCatalogChanged.java
 *
 * @author sgh
 * @since 2023.06.16
 */
@Getter
@Setter
@AllArgsConstructor
public class BookCatalogChanged {
    private Long bookId;
    private String eventType;
}
