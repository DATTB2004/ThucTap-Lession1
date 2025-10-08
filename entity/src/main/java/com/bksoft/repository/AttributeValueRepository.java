package com.bksoft.repository;

import com.bksoft.domain.AttributeValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AttributeValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {}
