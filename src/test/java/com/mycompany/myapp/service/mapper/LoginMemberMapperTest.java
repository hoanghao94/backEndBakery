package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginMemberMapperTest {

    private LoginMemberMapper loginMemberMapper;

    @BeforeEach
    public void setUp() {
        loginMemberMapper = new LoginMemberMapperImpl();
    }
}
