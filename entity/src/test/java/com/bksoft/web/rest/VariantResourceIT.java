package com.bksoft.web.rest;

import static com.bksoft.domain.VariantAsserts.*;
import static com.bksoft.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bksoft.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bksoft.IntegrationTest;
import com.bksoft.domain.Variant;
import com.bksoft.repository.VariantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VariantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VariantResourceIT {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final String ENTITY_API_URL = "/api/variants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVariantMockMvc;

    private Variant variant;

    private Variant insertedVariant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variant createEntity() {
        return new Variant().price(DEFAULT_PRICE).stock(DEFAULT_STOCK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variant createUpdatedEntity() {
        return new Variant().price(UPDATED_PRICE).stock(UPDATED_STOCK);
    }

    @BeforeEach
    void initTest() {
        variant = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVariant != null) {
            variantRepository.delete(insertedVariant);
            insertedVariant = null;
        }
    }

    @Test
    @Transactional
    void createVariant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Variant
        var returnedVariant = om.readValue(
            restVariantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(variant)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Variant.class
        );

        // Validate the Variant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVariantUpdatableFieldsEquals(returnedVariant, getPersistedVariant(returnedVariant));

        insertedVariant = returnedVariant;
    }

    @Test
    @Transactional
    void createVariantWithExistingId() throws Exception {
        // Create the Variant with an existing ID
        variant.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVariantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(variant)))
            .andExpect(status().isBadRequest());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVariants() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.saveAndFlush(variant);

        // Get all the variantList
        restVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variant.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)));
    }

    @Test
    @Transactional
    void getVariant() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.saveAndFlush(variant);

        // Get the variant
        restVariantMockMvc
            .perform(get(ENTITY_API_URL_ID, variant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(variant.getId().intValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK));
    }

    @Test
    @Transactional
    void getNonExistingVariant() throws Exception {
        // Get the variant
        restVariantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVariant() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.saveAndFlush(variant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the variant
        Variant updatedVariant = variantRepository.findById(variant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVariant are not directly saved in db
        em.detach(updatedVariant);
        updatedVariant.price(UPDATED_PRICE).stock(UPDATED_STOCK);

        restVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVariant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVariant))
            )
            .andExpect(status().isOk());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVariantToMatchAllProperties(updatedVariant);
    }

    @Test
    @Transactional
    void putNonExistingVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVariantMockMvc
            .perform(put(ENTITY_API_URL_ID, variant.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(variant)))
            .andExpect(status().isBadRequest());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(variant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(variant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVariantWithPatch() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.saveAndFlush(variant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the variant using partial update
        Variant partialUpdatedVariant = new Variant();
        partialUpdatedVariant.setId(variant.getId());

        partialUpdatedVariant.price(UPDATED_PRICE);

        restVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVariant))
            )
            .andExpect(status().isOk());

        // Validate the Variant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVariantUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVariant, variant), getPersistedVariant(variant));
    }

    @Test
    @Transactional
    void fullUpdateVariantWithPatch() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.saveAndFlush(variant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the variant using partial update
        Variant partialUpdatedVariant = new Variant();
        partialUpdatedVariant.setId(variant.getId());

        partialUpdatedVariant.price(UPDATED_PRICE).stock(UPDATED_STOCK);

        restVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVariant))
            )
            .andExpect(status().isOk());

        // Validate the Variant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVariantUpdatableFieldsEquals(partialUpdatedVariant, getPersistedVariant(partialUpdatedVariant));
    }

    @Test
    @Transactional
    void patchNonExistingVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, variant.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(variant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(variant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVariant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        variant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVariantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(variant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVariant() throws Exception {
        // Initialize the database
        insertedVariant = variantRepository.saveAndFlush(variant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the variant
        restVariantMockMvc
            .perform(delete(ENTITY_API_URL_ID, variant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return variantRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Variant getPersistedVariant(Variant variant) {
        return variantRepository.findById(variant.getId()).orElseThrow();
    }

    protected void assertPersistedVariantToMatchAllProperties(Variant expectedVariant) {
        assertVariantAllPropertiesEquals(expectedVariant, getPersistedVariant(expectedVariant));
    }

    protected void assertPersistedVariantToMatchUpdatableProperties(Variant expectedVariant) {
        assertVariantAllUpdatablePropertiesEquals(expectedVariant, getPersistedVariant(expectedVariant));
    }
}
