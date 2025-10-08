package com.bksoft.web.rest;

import static com.bksoft.domain.AttributeValueAsserts.*;
import static com.bksoft.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bksoft.IntegrationTest;
import com.bksoft.domain.AttributeValue;
import com.bksoft.repository.AttributeValueRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AttributeValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributeValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attribute-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributeValueMockMvc;

    private AttributeValue attributeValue;

    private AttributeValue insertedAttributeValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createEntity() {
        return new AttributeValue().value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createUpdatedEntity() {
        return new AttributeValue().value(UPDATED_VALUE);
    }

    @BeforeEach
    void initTest() {
        attributeValue = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAttributeValue != null) {
            attributeValueRepository.delete(insertedAttributeValue);
            insertedAttributeValue = null;
        }
    }

    @Test
    @Transactional
    void createAttributeValue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AttributeValue
        var returnedAttributeValue = om.readValue(
            restAttributeValueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeValue)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AttributeValue.class
        );

        // Validate the AttributeValue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAttributeValueUpdatableFieldsEquals(returnedAttributeValue, getPersistedAttributeValue(returnedAttributeValue));

        insertedAttributeValue = returnedAttributeValue;
    }

    @Test
    @Transactional
    void createAttributeValueWithExistingId() throws Exception {
        // Create the AttributeValue with an existing ID
        attributeValue.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attributeValue.setValue(null);

        // Create the AttributeValue, which fails.

        restAttributeValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttributeValues() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getAttributeValue() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get the attributeValue
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL_ID, attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributeValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingAttributeValue() throws Exception {
        // Get the attributeValue
        restAttributeValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttributeValue() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeValue
        AttributeValue updatedAttributeValue = attributeValueRepository.findById(attributeValue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAttributeValue are not directly saved in db
        em.detach(updatedAttributeValue);
        updatedAttributeValue.value(UPDATED_VALUE);

        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributeValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttributeValueToMatchAllProperties(updatedAttributeValue);
    }

    @Test
    @Transactional
    void putNonExistingAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeValue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttributeValueWithPatch() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeValue using partial update
        AttributeValue partialUpdatedAttributeValue = new AttributeValue();
        partialUpdatedAttributeValue.setId(attributeValue.getId());

        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeValueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttributeValue, attributeValue),
            getPersistedAttributeValue(attributeValue)
        );
    }

    @Test
    @Transactional
    void fullUpdateAttributeValueWithPatch() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeValue using partial update
        AttributeValue partialUpdatedAttributeValue = new AttributeValue();
        partialUpdatedAttributeValue.setId(attributeValue.getId());

        partialUpdatedAttributeValue.value(UPDATED_VALUE);

        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeValueUpdatableFieldsEquals(partialUpdatedAttributeValue, getPersistedAttributeValue(partialUpdatedAttributeValue));
    }

    @Test
    @Transactional
    void patchNonExistingAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(attributeValue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttributeValue() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the attributeValue
        restAttributeValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributeValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return attributeValueRepository.count();
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

    protected AttributeValue getPersistedAttributeValue(AttributeValue attributeValue) {
        return attributeValueRepository.findById(attributeValue.getId()).orElseThrow();
    }

    protected void assertPersistedAttributeValueToMatchAllProperties(AttributeValue expectedAttributeValue) {
        assertAttributeValueAllPropertiesEquals(expectedAttributeValue, getPersistedAttributeValue(expectedAttributeValue));
    }

    protected void assertPersistedAttributeValueToMatchUpdatableProperties(AttributeValue expectedAttributeValue) {
        assertAttributeValueAllUpdatablePropertiesEquals(expectedAttributeValue, getPersistedAttributeValue(expectedAttributeValue));
    }
}
