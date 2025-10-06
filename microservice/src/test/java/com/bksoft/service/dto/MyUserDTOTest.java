package com.bksoft.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bksoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyUserDTO.class);
        MyUserDTO myUserDTO1 = new MyUserDTO();
        myUserDTO1.setId(1L);
        MyUserDTO myUserDTO2 = new MyUserDTO();
        assertThat(myUserDTO1).isNotEqualTo(myUserDTO2);
        myUserDTO2.setId(myUserDTO1.getId());
        assertThat(myUserDTO1).isEqualTo(myUserDTO2);
        myUserDTO2.setId(2L);
        assertThat(myUserDTO1).isNotEqualTo(myUserDTO2);
        myUserDTO1.setId(null);
        assertThat(myUserDTO1).isNotEqualTo(myUserDTO2);
    }
}
