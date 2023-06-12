package com.jhipster.rental.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jhipster.rental.IntegrationTest;
import com.jhipster.rental.domain.RentedItem;
import com.jhipster.rental.repository.RentedItemRepository;
import com.jhipster.rental.service.dto.RentedItemDTO;
import com.jhipster.rental.service.mapper.RentedItemMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RentedItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RentedItemResourceIT {

    private static final Long DEFAULT_BOOK_ID = 1L;
    private static final Long UPDATED_BOOK_ID = 2L;

    private static final LocalDate DEFAULT_RENTED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RENTED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/rented-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RentedItemRepository rentedItemRepository;

    @Autowired
    private RentedItemMapper rentedItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentedItemMockMvc;

    private RentedItem rentedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentedItem createEntity(EntityManager em) {
        RentedItem rentedItem = new RentedItem().bookId(DEFAULT_BOOK_ID).rentedDate(DEFAULT_RENTED_DATE).dueDate(DEFAULT_DUE_DATE);
        return rentedItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RentedItem createUpdatedEntity(EntityManager em) {
        RentedItem rentedItem = new RentedItem().bookId(UPDATED_BOOK_ID).rentedDate(UPDATED_RENTED_DATE).dueDate(UPDATED_DUE_DATE);
        return rentedItem;
    }

    @BeforeEach
    public void initTest() {
        rentedItem = createEntity(em);
    }

    @Test
    @Transactional
    void createRentedItem() throws Exception {
        int databaseSizeBeforeCreate = rentedItemRepository.findAll().size();
        // Create the RentedItem
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);
        restRentedItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentedItemDTO)))
            .andExpect(status().isCreated());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeCreate + 1);
        RentedItem testRentedItem = rentedItemList.get(rentedItemList.size() - 1);
        assertThat(testRentedItem.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(testRentedItem.getRentedDate()).isEqualTo(DEFAULT_RENTED_DATE);
        assertThat(testRentedItem.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    void createRentedItemWithExistingId() throws Exception {
        // Create the RentedItem with an existing ID
        rentedItem.setId(1L);
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);

        int databaseSizeBeforeCreate = rentedItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentedItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentedItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRentedItems() throws Exception {
        // Initialize the database
        rentedItemRepository.saveAndFlush(rentedItem);

        // Get all the rentedItemList
        restRentedItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentedItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.intValue())))
            .andExpect(jsonPath("$.[*].rentedDate").value(hasItem(DEFAULT_RENTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));
    }

    @Test
    @Transactional
    void getRentedItem() throws Exception {
        // Initialize the database
        rentedItemRepository.saveAndFlush(rentedItem);

        // Get the rentedItem
        restRentedItemMockMvc
            .perform(get(ENTITY_API_URL_ID, rentedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rentedItem.getId().intValue()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.intValue()))
            .andExpect(jsonPath("$.rentedDate").value(DEFAULT_RENTED_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRentedItem() throws Exception {
        // Get the rentedItem
        restRentedItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRentedItem() throws Exception {
        // Initialize the database
        rentedItemRepository.saveAndFlush(rentedItem);

        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();

        // Update the rentedItem
        RentedItem updatedRentedItem = rentedItemRepository.findById(rentedItem.getId()).get();
        // Disconnect from session so that the updates on updatedRentedItem are not directly saved in db
        em.detach(updatedRentedItem);
        updatedRentedItem.bookId(UPDATED_BOOK_ID).rentedDate(UPDATED_RENTED_DATE).dueDate(UPDATED_DUE_DATE);
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(updatedRentedItem);

        restRentedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentedItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentedItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
        RentedItem testRentedItem = rentedItemList.get(rentedItemList.size() - 1);
        assertThat(testRentedItem.getBookId()).isEqualTo(UPDATED_BOOK_ID);
        assertThat(testRentedItem.getRentedDate()).isEqualTo(UPDATED_RENTED_DATE);
        assertThat(testRentedItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingRentedItem() throws Exception {
        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();
        rentedItem.setId(count.incrementAndGet());

        // Create the RentedItem
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentedItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRentedItem() throws Exception {
        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();
        rentedItem.setId(count.incrementAndGet());

        // Create the RentedItem
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRentedItem() throws Exception {
        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();
        rentedItem.setId(count.incrementAndGet());

        // Create the RentedItem
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentedItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rentedItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentedItemWithPatch() throws Exception {
        // Initialize the database
        rentedItemRepository.saveAndFlush(rentedItem);

        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();

        // Update the rentedItem using partial update
        RentedItem partialUpdatedRentedItem = new RentedItem();
        partialUpdatedRentedItem.setId(rentedItem.getId());

        partialUpdatedRentedItem.dueDate(UPDATED_DUE_DATE);

        restRentedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentedItem))
            )
            .andExpect(status().isOk());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
        RentedItem testRentedItem = rentedItemList.get(rentedItemList.size() - 1);
        assertThat(testRentedItem.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(testRentedItem.getRentedDate()).isEqualTo(DEFAULT_RENTED_DATE);
        assertThat(testRentedItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateRentedItemWithPatch() throws Exception {
        // Initialize the database
        rentedItemRepository.saveAndFlush(rentedItem);

        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();

        // Update the rentedItem using partial update
        RentedItem partialUpdatedRentedItem = new RentedItem();
        partialUpdatedRentedItem.setId(rentedItem.getId());

        partialUpdatedRentedItem.bookId(UPDATED_BOOK_ID).rentedDate(UPDATED_RENTED_DATE).dueDate(UPDATED_DUE_DATE);

        restRentedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRentedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRentedItem))
            )
            .andExpect(status().isOk());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
        RentedItem testRentedItem = rentedItemList.get(rentedItemList.size() - 1);
        assertThat(testRentedItem.getBookId()).isEqualTo(UPDATED_BOOK_ID);
        assertThat(testRentedItem.getRentedDate()).isEqualTo(UPDATED_RENTED_DATE);
        assertThat(testRentedItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingRentedItem() throws Exception {
        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();
        rentedItem.setId(count.incrementAndGet());

        // Create the RentedItem
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rentedItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRentedItem() throws Exception {
        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();
        rentedItem.setId(count.incrementAndGet());

        // Create the RentedItem
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRentedItem() throws Exception {
        int databaseSizeBeforeUpdate = rentedItemRepository.findAll().size();
        rentedItem.setId(count.incrementAndGet());

        // Create the RentedItem
        RentedItemDTO rentedItemDTO = rentedItemMapper.toDto(rentedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentedItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rentedItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RentedItem in the database
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRentedItem() throws Exception {
        // Initialize the database
        rentedItemRepository.saveAndFlush(rentedItem);

        int databaseSizeBeforeDelete = rentedItemRepository.findAll().size();

        // Delete the rentedItem
        restRentedItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, rentedItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RentedItem> rentedItemList = rentedItemRepository.findAll();
        assertThat(rentedItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
