package com.bksoft.repository;

import com.bksoft.domain.Attribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {}
