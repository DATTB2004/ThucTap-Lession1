package com.bksoft.domain;

import static com.bksoft.domain.AttributeTestSamples.*;
import static com.bksoft.domain.AttributeValueTestSamples.*;
import static com.bksoft.domain.VariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bksoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributeValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeValue.class);
        AttributeValue attributeValue1 = getAttributeValueSample1();
        AttributeValue attributeValue2 = new AttributeValue();
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);

        attributeValue2.setId(attributeValue1.getId());
        assertThat(attributeValue1).isEqualTo(attributeValue2);

        attributeValue2 = getAttributeValueSample2();
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
    }

    @Test
    void attributeTest() {
        AttributeValue attributeValue = getAttributeValueRandomSampleGenerator();
        Attribute attributeBack = getAttributeRandomSampleGenerator();

        attributeValue.setAttribute(attributeBack);
        assertThat(attributeValue.getAttribute()).isEqualTo(attributeBack);

        attributeValue.attribute(null);
        assertThat(attributeValue.getAttribute()).isNull();
    }

    @Test
    void variantTest() {
        AttributeValue attributeValue = getAttributeValueRandomSampleGenerator();
        Variant variantBack = getVariantRandomSampleGenerator();

        attributeValue.setVariant(variantBack);
        assertThat(attributeValue.getVariant()).isEqualTo(variantBack);

        attributeValue.variant(null);
        assertThat(attributeValue.getVariant()).isNull();
    }
}
