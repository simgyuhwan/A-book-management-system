package com.jhipster.rental.service;

import com.jhipster.rental.service.dto.RentedItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.jhipster.rental.domain.RentedItem}.
 */
public interface RentedItemService {
    /**
     * Save a rentedItem.
     *
     * @param rentedItemDTO the entity to save.
     * @return the persisted entity.
     */
    RentedItemDTO save(RentedItemDTO rentedItemDTO);

    /**
     * Updates a rentedItem.
     *
     * @param rentedItemDTO the entity to update.
     * @return the persisted entity.
     */
    RentedItemDTO update(RentedItemDTO rentedItemDTO);

    /**
     * Partially updates a rentedItem.
     *
     * @param rentedItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RentedItemDTO> partialUpdate(RentedItemDTO rentedItemDTO);

    /**
     * Get all the rentedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentedItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rentedItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentedItemDTO> findOne(Long id);

    /**
     * Delete the "id" rentedItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
