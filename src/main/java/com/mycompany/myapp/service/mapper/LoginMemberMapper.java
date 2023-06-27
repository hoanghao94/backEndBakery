package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.LoginMember;
import com.mycompany.myapp.service.dto.LoginMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoginMember} and its DTO {@link LoginMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoginMemberMapper extends EntityMapper<LoginMemberDTO, LoginMember> {}
