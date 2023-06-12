package com.jhipster.rental.service;

import com.jhipster.rental.service.dto.RentalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.jhipster.rental.domain.Rental}.
 */
public interface RentalService {
    /**
     * Save a rental.
     *
     * @param rentalDTO the entity to save.
     * @return the persisted entity.
     */
    RentalDTO save(RentalDTO rentalDTO);

    /**
     * Updates a rental.
     *
     * @param rentalDTO the entity to update.
     * @return the persisted entity.
     */
    RentalDTO update(RentalDTO rentalDTO);

    /**
     * Partially updates a rental.
     *
     * @param rentalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RentalDTO> partialUpdate(RentalDTO rentalDTO);

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rental.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentalDTO> findOne(Long id);

    /**
     * Delete the "id" rental.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
