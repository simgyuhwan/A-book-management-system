package com.jhipster.rental.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 대출 아이템
 */
@Entity
@Table(name = "rented_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RentedItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 대출아이템 일련번호
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
     * 대출 시작일자
     */
    @Column(name = "rented_date")
    private LocalDate rentedDate;

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
    @JsonIgnoreProperties("rentedItems")
    private Rental rental;

    // 대출 아이템을 생성하는 메서드
    public static RentedItem createRentedItem(Long bookId, String bookTitle, LocalDate rentedDate) {
        RentedItem rentedItem = new RentedItem();
        rentedItem.setBookId(bookId);
        rentedItem.setBookTitle(bookTitle);
        rentedItem.setRentedDate(rentedDate);
        rentedItem.setDueDate(rentedDate.plusWeeks(2));
        return rentedItem;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Long getId() {
        return this.id;
    }

    public RentedItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public RentedItem bookId(Long bookId) {
        this.setBookId(bookId);
        return this;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getRentedDate() {
        return this.rentedDate;
    }

    public RentedItem rentedDate(LocalDate rentedDate) {
        this.setRentedDate(rentedDate);
        return this;
    }

    public void setRentedDate(LocalDate rentedDate) {
        this.rentedDate = rentedDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public RentedItem dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Rental getRental() {
        return this.rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public RentedItem rental(Rental rental) {
        this.setRental(rental);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentedItem)) {
            return false;
        }
        return id != null && id.equals(((RentedItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentedItem{" +
            "id=" + getId() +
            ", bookId=" + getBookId() +
            ", rentedDate='" + getRentedDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            "}";
    }
}
