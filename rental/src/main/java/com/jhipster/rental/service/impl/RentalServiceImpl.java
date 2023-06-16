package com.jhipster.rental.service.impl;

import com.jhipster.rental.adaptor.RentalProducer;
import com.jhipster.rental.domain.Rental;
import com.jhipster.rental.repository.RentalRepository;
import com.jhipster.rental.service.RentalService;
import com.jhipster.rental.service.dto.RentalDTO;
import com.jhipster.rental.service.mapper.RentalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rental}.
 */
@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;

    private final RentalMapper rentalMapper;

    private final RentalProducer rentalProducer;

    public RentalServiceImpl(RentalRepository rentalRepository, RentalMapper rentalMapper, RentalProducer rentalProducer) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.rentalProducer = rentalProducer;
    }

    /**
     * 도서 대출 처리 구현
     */
    @Override
    public Rental rentBook(Long userId, Long bookId, String bookTitle) {
        Rental rental = rentalRepository.findByUserId(userId).get();
        rental.checkRentalAvailable(); // 대출 가능 상태 확인
        rental = rental.rentBook(bookId, bookTitle); // Rental 에 대출처리 위임
        rentalRepository.save(rental);

        // 도서 서비스에 도서재고 감소를 위해 도서대출 이벤트 발송
        rentalProducer.updateBookStatus(bookId, "UNAVAILABLE");

        // 도서 카탈로그 서비스에 대출된 도서로 상태를 변경하기 위한 이벤트 발송
        rentalProducer.updateBookCatalogStatus(bookId, "RENT_BOOK");

        // 대출로 인한 사용자 포인트 적립을 위해 사용자 서비스에 이벤트 발송
        rentalProducer.savePoints(userId, 1000);

        return rental;
    }

    /**
     * 도서 반납 구현
     */
    @Override
    public Rental returnBooks(Long userId, Long bookId) {
        Rental rental = rentalRepository.findByUserId(userId).get();
        rental = rental.returnBook(bookId);
        rentalRepository.save(rental);

        // 도서 서비스에 도서재고 증가를 위해 도서반납 이벤트 발송
        rentalProducer.updateBookStatus(bookId, "AVAILABLE");

        // 도서 카탈로그 서비스에 대출 가능한 도서로 상태를 변경하기 위한 이벤트 발송
        rentalProducer.updateBookCatalogStatus(bookId, "RETURN_BOOK");

        return rental;
    }

    @Override
    public RentalDTO save(RentalDTO rentalDTO) {
        log.debug("Request to save Rental : {}", rentalDTO);
        Rental rental = rentalMapper.toEntity(rentalDTO);
        rental = rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

    @Override
    public RentalDTO update(RentalDTO rentalDTO) {
        log.debug("Request to update Rental : {}", rentalDTO);
        Rental rental = rentalMapper.toEntity(rentalDTO);
        rental = rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

    @Override
    public Optional<RentalDTO> partialUpdate(RentalDTO rentalDTO) {
        log.debug("Request to partially update Rental : {}", rentalDTO);

        return rentalRepository
            .findById(rentalDTO.getId())
            .map(existingRental -> {
                rentalMapper.partialUpdate(existingRental, rentalDTO);

                return existingRental;
            })
            .map(rentalRepository::save)
            .map(rentalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rentals");
        return rentalRepository.findAll(pageable).map(rentalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RentalDTO> findOne(Long id) {
        log.debug("Request to get Rental : {}", id);
        return rentalRepository.findById(id).map(rentalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }
}
