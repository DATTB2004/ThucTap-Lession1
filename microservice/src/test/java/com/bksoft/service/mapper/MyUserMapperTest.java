package com.bksoft.service.mapper;

import static com.bksoft.domain.MyUserAsserts.*;
import static com.bksoft.domain.MyUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyUserMapperTest {

    private MyUserMapper myUserMapper;

    @BeforeEach
    void setUp() {
        myUserMapper = new MyUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMyUserSample1();
        var actual = myUserMapper.toEntity(myUserMapper.toDto(expected));
        assertMyUserAllPropertiesEquals(expected, actual);
    }
}
