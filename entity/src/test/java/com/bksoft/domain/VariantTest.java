package com.bksoft.domain;

import static com.bksoft.domain.AttributeValueTestSamples.*;
import static com.bksoft.domain.ProductTestSamples.*;
import static com.bksoft.domain.VariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bksoft.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VariantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Variant.class);
        Variant variant1 = getVariantSample1();
        Variant variant2 = new Variant();
        assertThat(variant1).isNotEqualTo(variant2);

        variant2.setId(variant1.getId());
        assertThat(variant1).isEqualTo(variant2);

        variant2 = getVariantSample2();
        assertThat(variant1).isNotEqualTo(variant2);
    }

    @Test
    void attributeValueTest() {
        Variant variant = getVariantRandomSampleGenerator();
        AttributeValue attributeValueBack = getAttributeValueRandomSampleGenerator();

        variant.addAttributeValue(attributeValueBack);
        assertThat(variant.getAttributeValues()).containsOnly(attributeValueBack);
        assertThat(attributeValueBack.getVariant()).isEqualTo(variant);

        variant.removeAttributeValue(attributeValueBack);
        assertThat(variant.getAttributeValues()).doesNotContain(attributeValueBack);
        assertThat(attributeValueBack.getVariant()).isNull();

        variant.attributeValues(new HashSet<>(Set.of(attributeValueBack)));
        assertThat(variant.getAttributeValues()).containsOnly(attributeValueBack);
        assertThat(attributeValueBack.getVariant()).isEqualTo(variant);

        variant.setAttributeValues(new HashSet<>());
        assertThat(variant.getAttributeValues()).doesNotContain(attributeValueBack);
        assertThat(attributeValueBack.getVariant()).isNull();
    }

    @Test
    void productTest() {
        Variant variant = getVariantRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        variant.setProduct(productBack);
        assertThat(variant.getProduct()).isEqualTo(productBack);

        variant.product(null);
        assertThat(variant.getProduct()).isNull();
    }
}
