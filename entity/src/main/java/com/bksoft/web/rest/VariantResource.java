package com.bksoft.web.rest;

import com.bksoft.domain.Variant;
import com.bksoft.repository.VariantRepository;
import com.bksoft.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bksoft.domain.Variant}.
 */
@RestController
@RequestMapping("/api/variants")
@Transactional
public class VariantResource {

    private static final Logger LOG = LoggerFactory.getLogger(VariantResource.class);

    private static final String ENTITY_NAME = "entityVariant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VariantRepository variantRepository;

    public VariantResource(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    /**
     * {@code POST  /variants} : Create a new variant.
     *
     * @param variant the variant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new variant, or with status {@code 400 (Bad Request)} if the variant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Variant> createVariant(@RequestBody Variant variant) throws URISyntaxException {
        LOG.debug("REST request to save Variant : {}", variant);
        if (variant.getId() != null) {
            throw new BadRequestAlertException("A new variant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        variant = variantRepository.save(variant);
        return ResponseEntity.created(new URI("/api/variants/" + variant.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, variant.getId().toString()))
            .body(variant);
    }

    /**
     * {@code PUT  /variants/:id} : Updates an existing variant.
     *
     * @param id the id of the variant to save.
     * @param variant the variant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variant,
     * or with status {@code 400 (Bad Request)} if the variant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the variant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Variant> updateVariant(@PathVariable(value = "id", required = false) final Long id, @RequestBody Variant variant)
        throws URISyntaxException {
        LOG.debug("REST request to update Variant : {}, {}", id, variant);
        if (variant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!variantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        variant = variantRepository.save(variant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, variant.getId().toString()))
            .body(variant);
    }

    /**
     * {@code PATCH  /variants/:id} : Partial updates given fields of an existing variant, field will ignore if it is null
     *
     * @param id the id of the variant to save.
     * @param variant the variant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variant,
     * or with status {@code 400 (Bad Request)} if the variant is not valid,
     * or with status {@code 404 (Not Found)} if the variant is not found,
     * or with status {@code 500 (Internal Server Error)} if the variant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Variant> partialUpdateVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Variant variant
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Variant partially : {}, {}", id, variant);
        if (variant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!variantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Variant> result = variantRepository
            .findById(variant.getId())
            .map(existingVariant -> {
                if (variant.getPrice() != null) {
                    existingVariant.setPrice(variant.getPrice());
                }
                if (variant.getStock() != null) {
                    existingVariant.setStock(variant.getStock());
                }

                return existingVariant;
            })
            .map(variantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, variant.getId().toString())
        );
    }

    /**
     * {@code GET  /variants} : get all the variants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of variants in body.
     */
    @GetMapping("")
    public List<Variant> getAllVariants() {
        LOG.debug("REST request to get all Variants");
        return variantRepository.findAll();
    }

    /**
     * {@code GET  /variants/:id} : get the "id" variant.
     *
     * @param id the id of the variant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Variant> getVariant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Variant : {}", id);
        Optional<Variant> variant = variantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(variant);
    }

    /**
     * {@code DELETE  /variants/:id} : delete the "id" variant.
     *
     * @param id the id of the variant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Variant : {}", id);
        variantRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
