package com.jhipster.rental.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jhipster.rental.adaptor.BookClient;
import com.jhipster.rental.domain.Rental;
import com.jhipster.rental.repository.RentalRepository;
import com.jhipster.rental.service.RentalService;
import com.jhipster.rental.service.dto.RentalDTO;
import com.jhipster.rental.service.mapper.RentalMapper;
import com.jhipster.rental.web.rest.dto.BookInfoDTO;
import com.jhipster.rental.web.rest.dto.LateFeeDto;
import com.jhipster.rental.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import feign.FeignException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jhipster.rental.domain.Rental}.
 */
@RestController
@RequestMapping("/api")
public class RentalResource {

    private final Logger log = LoggerFactory.getLogger(RentalResource.class);

    private static final String ENTITY_NAME = "rentalRental";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentalService rentalService;

    private final RentalRepository rentalRepository;

    private final RentalMapper rentalMapper;

    private final BookClient bookClient;

    public RentalResource(RentalService rentalService, RentalRepository rentalRepository, RentalMapper rentalMapper, BookClient bookClient) {
        this.rentalService = rentalService;
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.bookClient = bookClient;
    }

    /**
     * 도서 대출 API
     *
     * @param userid
     * @param bookId
     * @return
     */
    @PostMapping("/rentals/{userid}/RentedItem/{book}")
    public ResponseEntity<RentalDTO> rentBook(@PathVariable("userid") Long userid, @PathVariable("book") Long bookId) throws
        ExecutionException,
        InterruptedException,
        JsonProcessingException {
        // 도서 서비스를 호출해 도서 정보 가져오기
        ResponseEntity<BookInfoDTO> bookInfoResult = bookClient.findBookInfo(bookId);
        BookInfoDTO bookInfoDTO = bookInfoResult.getBody();

        Rental rental = rentalService.rentBook(userid, bookInfoDTO.getId(), bookInfoDTO.getTitle());
        RentalDTO rentalDTO = rentalMapper.toDto(rental);
        return ResponseEntity.ok(rentalDTO);
    }

    /**
     * 도서 반납 API
     *
     * @param userid
     * @param book
     * @return
     */
    @DeleteMapping("/rentals/{userid}/RentedItem/{book}")
    public ResponseEntity<RentalDTO> returnBook(@PathVariable("userid") Long userid, @PathVariable("book") Long book) throws
        ExecutionException,
        InterruptedException,
        JsonProcessingException {
        Rental rental = rentalService.returnBooks(userid, book);
        RentalDTO rentalDTO = rentalMapper.toDto(rental);
        return ResponseEntity.ok(rentalDTO);
    }

    /**
     * 대출 불가 해제 API
     *
     * @param userId
     * @return
     */
    @PutMapping("/rentals/release-overdue/user/{userId}")
    public ResponseEntity releaseOverdue(@PathVariable("userId") Long userId) {
        LateFeeDto lateFeeDto = new LateFeeDto();
        lateFeeDto.setUserId(userId);
        lateFeeDto.setLateFee(rentalService.findLateFee(userId));

        try {
            userClient.usePoint(lateFeeDto);
        } catch (FeignException.FeignClientException e) {
            e.printStackTrace();
        }

        RentalDTO rentalDTO = rentalMapper.toDto(rentalService.releaseOverdue(userId));
        return ResponseEntity.ok(rentalDTO);
    }

    /**
     * {@code POST  /rentals} : Create a new rental.
     *
     * @param rentalDTO the rentalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentalDTO, or with status {@code 400 (Bad Request)} if the rental has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rentals")
    public ResponseEntity<RentalDTO> createRental(@RequestBody RentalDTO rentalDTO) throws URISyntaxException {
        log.debug("REST request to save Rental : {}", rentalDTO);
        if (rentalDTO.getId() != null) {
            throw new BadRequestAlertException("A new rental cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentalDTO result = rentalService.save(rentalDTO);
        return ResponseEntity
            .created(new URI("/api/rentals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rentals/:id} : Updates an existing rental.
     *
     * @param id the id of the rentalDTO to save.
     * @param rentalDTO the rentalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalDTO,
     * or with status {@code 400 (Bad Request)} if the rentalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> updateRental(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RentalDTO rentalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Rental : {}, {}", id, rentalDTO);
        if (rentalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rentalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RentalDTO result = rentalService.update(rentalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rentals/:id} : Partial updates given fields of an existing rental, field will ignore if it is null
     *
     * @param id the id of the rentalDTO to save.
     * @param rentalDTO the rentalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentalDTO,
     * or with status {@code 400 (Bad Request)} if the rentalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rentalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rentalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rentals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RentalDTO> partialUpdateRental(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RentalDTO rentalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rental partially : {}, {}", id, rentalDTO);
        if (rentalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rentalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rentalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RentalDTO> result = rentalService.partialUpdate(rentalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rentals} : get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentals in body.
     */
    @GetMapping("/rentals")
    public ResponseEntity<List<RentalDTO>> getAllRentals(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rentals");
        Page<RentalDTO> page = rentalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rentals/:id} : get the "id" rental.
     *
     * @param id the id of the rentalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        log.debug("REST request to get Rental : {}", id);
        Optional<RentalDTO> rentalDTO = rentalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rentalDTO);
    }

    /**
     * {@code DELETE  /rentals/:id} : delete the "id" rental.
     *
     * @param id the id of the rentalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rentals/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        log.debug("REST request to delete Rental : {}", id);
        rentalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
