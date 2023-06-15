package com.jhipster.rental.repository;

import java.util.Optional;

import com.jhipster.rental.domain.Rental;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    Optional<Rental> findByUserId(Long userId);
}
