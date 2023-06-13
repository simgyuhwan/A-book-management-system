package com.jhipster.rental.domain;

import com.jhipster.rental.domain.enumeration.RentalStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rental 애그리거트 루트, 앤티티 클래스
 */
@Entity
@Table(name = "rental")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Rental 일련번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 일련번호(사용자 식별값)
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 대춮 가능 여부
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rental_status")
    private RentalStatus rentalStatus;

    /**
     * 연체료
     */
    @Column(name = "late_fee")
    private Long lateFee;

    /**
     * 대출 아이템
     */
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RentalItem> rentedItems = new HashSet<>();

    /**
     * 연체 아이템
     */
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OverdueItem> overdueItems = new HashSet<>();

    /**
     * 반납 아이템
     */
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReturnedItem> returnedItems = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Rental id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Rental userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public RentalStatus getRentalStatus() {
        return this.rentalStatus;
    }

    public Rental rentalStatus(RentalStatus rentalStatus) {
        this.setRentalStatus(rentalStatus);
        return this;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Long getLateFee() {
        return this.lateFee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rental)) {
            return false;
        }
        return id != null && id.equals(((Rental) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rental{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", rentalStatus='" + getRentalStatus() + "'" +
            "}";
    }
}
