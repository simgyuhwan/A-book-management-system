package com.jhipster.rental.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.rental.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentedItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentedItemDTO.class);
        RentedItemDTO rentedItemDTO1 = new RentedItemDTO();
        rentedItemDTO1.setId(1L);
        RentedItemDTO rentedItemDTO2 = new RentedItemDTO();
        assertThat(rentedItemDTO1).isNotEqualTo(rentedItemDTO2);
        rentedItemDTO2.setId(rentedItemDTO1.getId());
        assertThat(rentedItemDTO1).isEqualTo(rentedItemDTO2);
        rentedItemDTO2.setId(2L);
        assertThat(rentedItemDTO1).isNotEqualTo(rentedItemDTO2);
        rentedItemDTO1.setId(null);
        assertThat(rentedItemDTO1).isNotEqualTo(rentedItemDTO2);
    }
}
