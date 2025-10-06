package com.bksoft.service.mapper;

import com.bksoft.domain.MyUser;
import com.bksoft.service.dto.MyUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MyUser} and its DTO {@link MyUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface MyUserMapper extends EntityMapper<MyUserDTO, MyUser> {}
