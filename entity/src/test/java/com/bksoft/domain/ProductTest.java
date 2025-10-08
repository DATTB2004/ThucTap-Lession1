package com.bksoft.domain;

import static com.bksoft.domain.ProductTestSamples.*;
import static com.bksoft.domain.VariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bksoft.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void variantTest() {
        Product product = getProductRandomSampleGenerator();
        Variant variantBack = getVariantRandomSampleGenerator();

        product.addVariant(variantBack);
        assertThat(product.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getProduct()).isEqualTo(product);

        product.removeVariant(variantBack);
        assertThat(product.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getProduct()).isNull();

        product.variants(new HashSet<>(Set.of(variantBack)));
        assertThat(product.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getProduct()).isEqualTo(product);

        product.setVariants(new HashSet<>());
        assertThat(product.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getProduct()).isNull();
    }
}
