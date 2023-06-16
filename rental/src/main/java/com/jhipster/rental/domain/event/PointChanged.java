package com.jhipster.rental.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * PointChanged.java
 *
 * @author sgh
 * @since 2023.06.16
 */
@Getter
@Setter
@AllArgsConstructor
public class PointChanged {
    private Long userId;
    private int points;
}
