package com.bksoft.repository;

import com.bksoft.domain.Variant;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Spring Data JPA repository for the Variant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    @Query("""
        SELECT DISTINCT v FROM Variant v
        JOIN v.attributeValues av1
        JOIN v.attributeValues av2
        JOIN v.attributeValues av3
        WHERE v.product.id = :productId
          AND av1.attribute.id = :sizeAttrId
          AND av2.attribute.id = :colorAttrId
          AND av3.attribute.id = :materialAttrId
    """)
    Optional<Variant> findVariantByAttributeIds(
        @Param("productId") Long productId,
        @Param("sizeAttrId") Long sizeAttrId,
        @Param("colorAttrId") Long colorAttrId,
        @Param("materialAttrId") Long materialAttrId
    );
}
