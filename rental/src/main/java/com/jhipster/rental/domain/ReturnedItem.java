package com.jhipster.rental.domain;

import java.time.LocalDate;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * 반납 아이템
 */
@Entity
@Table(name = "returned_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReturnedItem {

    /**
     * 반납아이템 일련번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 반납한 재고 도서 일련번호(도서 서비스에서 발행한 재고 도서 일련번호)
     */
    @Column(name = "book_id")
    private Long bookId;

    /**
     * 반납 일자
     */
    @Column(name = "returned_date")
    private LocalDate returnedDate;

    /**
     * 반납 도서명
     */
    @Column(name = "book_title")
    private String bookTitle;

    public static ReturnedItem createReturnedItem(Long bookId, String bookTitle, LocalDate now) {
        ReturnedItem returnedItem = new ReturnedItem();
        returnedItem.setBookId(bookId);
        returnedItem.setBookTitle(bookTitle);
        returnedItem.setReturnedDate(now);
        return returnedItem;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Rental rental;

    public ReturnedItem() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Rental getRental() {
        return rental;
    }
}
