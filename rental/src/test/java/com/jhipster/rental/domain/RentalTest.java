package com.jhipster.rental.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.rental.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rental.class);
        Rental rental1 = new Rental();
        rental1.setId(1L);
        Rental rental2 = new Rental();
        rental2.setId(rental1.getId());
        assertThat(rental1).isEqualTo(rental2);
        rental2.setId(2L);
        assertThat(rental1).isNotEqualTo(rental2);
        rental1.setId(null);
        assertThat(rental1).isNotEqualTo(rental2);
    }
}
