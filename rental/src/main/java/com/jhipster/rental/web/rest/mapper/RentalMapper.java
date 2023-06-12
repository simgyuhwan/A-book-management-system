package com.jhipster.rental.service.mapper;

import com.jhipster.rental.domain.Rental;
import com.jhipster.rental.service.dto.RentalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rental} and its DTO {@link RentalDTO}.
 */
@Mapper(componentModel = "spring")
public interface RentalMapper extends EntityMapper<RentalDTO, Rental> {}
