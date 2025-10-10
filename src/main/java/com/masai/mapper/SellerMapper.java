package com.masai.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.masai.dto.CreateSellerDTO;
import com.masai.dto.UpdateSellerDTO;
import com.masai.model.Seller;

/**
 * MapStruct mapper for Seller entity and DTOs.
 * This mapper automatically generates implementation code at compile time.
 * When Seller entity fields change, just recompile and mappings stay in sync.
 */
@Mapper(
    componentModel = "spring", // Creates Spring bean
    unmappedTargetPolicy = ReportingPolicy.IGNORE, // Ignore unmapped fields
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // Don't map null values
)
public interface SellerMapper {

    /**
     * Maps CreateSellerDTO to Seller entity.
     * Excludes auto-generated fields like sellerId and product list.
     */
    @Mapping(target = "sellerId", ignore = true) // Auto-generated
    @Mapping(target = "product", ignore = true) // Not part of creation
    @Mapping(target = "password", ignore = true) // Will be set separately after encoding
    Seller toEntity(CreateSellerDTO createSellerDTO);

    /**
     * Maps UpdateSellerDTO to Seller entity.
     * Excludes fields that shouldn't be updated via this DTO.
     */
    @Mapping(target = "product", ignore = true) // Not part of update
    @Mapping(target = "password", ignore = true) // Updated separately for security
    Seller toEntity(UpdateSellerDTO updateSellerDTO);

    /**
     * Maps Seller entity to UpdateSellerDTO.
     * Useful for pre-populating update forms.
     */
    @Mapping(target = "sellerId", source = "sellerId")
    UpdateSellerDTO toUpdateDTO(Seller seller);

    /**
     * Updates existing Seller entity with values from UpdateSellerDTO.
     * Only updates non-null values from DTO.
     */
    @Mapping(target = "sellerId", ignore = true) // Don't update ID
    @Mapping(target = "product", ignore = true) // Don't update product list
    @Mapping(target = "password", ignore = true) // Don't update password
    void updateEntityFromDTO(UpdateSellerDTO updateSellerDTO, @MappingTarget Seller seller);
}
