package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoginMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginMember.class);
        LoginMember loginMember1 = new LoginMember();
        loginMember1.setId(1L);
        LoginMember loginMember2 = new LoginMember();
        loginMember2.setId(loginMember1.getId());
        assertThat(loginMember1).isEqualTo(loginMember2);
        loginMember2.setId(2L);
        assertThat(loginMember1).isNotEqualTo(loginMember2);
        loginMember1.setId(null);
        assertThat(loginMember1).isNotEqualTo(loginMember2);
    }
}
