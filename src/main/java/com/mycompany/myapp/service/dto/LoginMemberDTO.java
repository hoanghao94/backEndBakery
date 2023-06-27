package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.LoginMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoginMemberDTO implements Serializable {

    private Long id;

    private String memberName;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoginMemberDTO)) {
            return false;
        }

        LoginMemberDTO loginMemberDTO = (LoginMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loginMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginMemberDTO{" +
            "id=" + getId() +
            ", memberName='" + getMemberName() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
