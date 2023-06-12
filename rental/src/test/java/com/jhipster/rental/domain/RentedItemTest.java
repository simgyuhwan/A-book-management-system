package com.jhipster.rental.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.rental.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentedItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentedItem.class);
        RentedItem rentedItem1 = new RentedItem();
        rentedItem1.setId(1L);
        RentedItem rentedItem2 = new RentedItem();
        rentedItem2.setId(rentedItem1.getId());
        assertThat(rentedItem1).isEqualTo(rentedItem2);
        rentedItem2.setId(2L);
        assertThat(rentedItem1).isNotEqualTo(rentedItem2);
        rentedItem1.setId(null);
        assertThat(rentedItem1).isNotEqualTo(rentedItem2);
    }
}
