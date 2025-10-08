package com.bksoft.domain;

import static com.bksoft.domain.AttributeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bksoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribute.class);
        Attribute attribute1 = getAttributeSample1();
        Attribute attribute2 = new Attribute();
        assertThat(attribute1).isNotEqualTo(attribute2);

        attribute2.setId(attribute1.getId());
        assertThat(attribute1).isEqualTo(attribute2);

        attribute2 = getAttributeSample2();
        assertThat(attribute1).isNotEqualTo(attribute2);
    }
}
