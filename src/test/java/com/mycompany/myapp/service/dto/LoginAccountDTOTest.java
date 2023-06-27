package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoginAccountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginAccountDTO.class);
        LoginAccountDTO loginAccountDTO1 = new LoginAccountDTO();
        loginAccountDTO1.setId(1L);
        LoginAccountDTO loginAccountDTO2 = new LoginAccountDTO();
        assertThat(loginAccountDTO1).isNotEqualTo(loginAccountDTO2);
        loginAccountDTO2.setId(loginAccountDTO1.getId());
        assertThat(loginAccountDTO1).isEqualTo(loginAccountDTO2);
        loginAccountDTO2.setId(2L);
        assertThat(loginAccountDTO1).isNotEqualTo(loginAccountDTO2);
        loginAccountDTO1.setId(null);
        assertThat(loginAccountDTO1).isNotEqualTo(loginAccountDTO2);
    }
}
