package com.jhipster.rental.repository;

import com.jhipster.rental.domain.RentedItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RentedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentedItemRepository extends JpaRepository<RentedItem, Long> {}
