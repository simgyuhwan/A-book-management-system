package com.jhipster.rental.service.mapper;

import com.jhipster.rental.domain.Rental;
import com.jhipster.rental.domain.RentedItem;
import com.jhipster.rental.service.dto.RentalDTO;
import com.jhipster.rental.service.dto.RentedItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RentedItem} and its DTO {@link RentedItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface RentedItemMapper extends EntityMapper<RentedItemDTO, RentedItem> {
    @Mapping(target = "rental", source = "rental", qualifiedByName = "rentalId")
    RentedItemDTO toDto(RentedItem s);

    @Named("rentalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RentalDTO toDtoRentalId(Rental rental);
}
