package com.jhipster.rental.web.rest.dto;

/**
 * BookInfoDTO.java
 *
 * 책 정보
 *
 * @author sgh
 * @since 2023.06.16
 */
public class BookInfoDTO {
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
