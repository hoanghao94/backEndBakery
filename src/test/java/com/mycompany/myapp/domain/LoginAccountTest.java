package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoginAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginAccount.class);
        LoginAccount loginAccount1 = new LoginAccount();
        loginAccount1.setId(1L);
        LoginAccount loginAccount2 = new LoginAccount();
        loginAccount2.setId(loginAccount1.getId());
        assertThat(loginAccount1).isEqualTo(loginAccount2);
        loginAccount2.setId(2L);
        assertThat(loginAccount1).isNotEqualTo(loginAccount2);
        loginAccount1.setId(null);
        assertThat(loginAccount1).isNotEqualTo(loginAccount2);
    }
}
