package com.jhipster.rental.domain.enumeration;

/**
 * The RentalStatus enumeration.
 */
public enum RentalStatus {
    RENT_AVAILABLE(0, "대출가능", "대출가능상태"),
    RENT_UNAVAILABLE(1, "대출불가", "대출불가상태");

    private final int code;
    private final String title;
    private final String status;

    RentalStatus(int code, String title, String status) {
        this.code = code;
        this.title = title;
        this.status = status;
    }
}
