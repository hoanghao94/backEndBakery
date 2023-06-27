package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoginMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginMemberDTO.class);
        LoginMemberDTO loginMemberDTO1 = new LoginMemberDTO();
        loginMemberDTO1.setId(1L);
        LoginMemberDTO loginMemberDTO2 = new LoginMemberDTO();
        assertThat(loginMemberDTO1).isNotEqualTo(loginMemberDTO2);
        loginMemberDTO2.setId(loginMemberDTO1.getId());
        assertThat(loginMemberDTO1).isEqualTo(loginMemberDTO2);
        loginMemberDTO2.setId(2L);
        assertThat(loginMemberDTO1).isNotEqualTo(loginMemberDTO2);
        loginMemberDTO1.setId(null);
        assertThat(loginMemberDTO1).isNotEqualTo(loginMemberDTO2);
    }
}
