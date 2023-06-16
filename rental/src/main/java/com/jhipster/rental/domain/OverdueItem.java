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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 연체아이템
 */
@Entity
@Getter
@Setter
@Table(name = "overdue_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OverdueItem {

    /**
     * 연체아이템 일련번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 대출한 재고 도서 일련번호(도서 서비스에서 발행한 번호)
     */
    @Column(name = "book_id")
    private Long bookId;

    /**
     * 반납 예정일자
     */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * 대출한 도서명
     */
    @Column(name = "book_title")
    private String bookTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rental rental;

    public OverdueItem() {}

    public OverdueItem(Long bookId, String bookTitle, LocalDate dueDate) {
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.bookTitle = bookTitle;
    }

    public static OverdueItem createOverdueItem(Long bookId, String bookTitle, LocalDate dueDate) {
        return new OverdueItem(bookId, bookTitle, dueDate);

    }
}
