package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.LoginAccount;
import com.mycompany.myapp.service.dto.LoginAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoginAccount} and its DTO {@link LoginAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoginAccountMapper extends EntityMapper<LoginAccountDTO, LoginAccount> {}
