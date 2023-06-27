package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginAccountMapperTest {

    private LoginAccountMapper loginAccountMapper;

    @BeforeEach
    public void setUp() {
        loginAccountMapper = new LoginAccountMapperImpl();
    }
}
