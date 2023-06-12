package com.jhipster.rental.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RentedItemMapperTest {

    private RentedItemMapper rentedItemMapper;

    @BeforeEach
    public void setUp() {
        rentedItemMapper = new RentedItemMapperImpl();
    }
}
