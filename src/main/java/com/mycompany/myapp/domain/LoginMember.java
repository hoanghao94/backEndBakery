package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A LoginMember.
 */
@Entity
@Table(name = "login_member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoginMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "password")
    private String password;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoginMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public LoginMember memberName(String memberName) {
        this.setMemberName(memberName);
        return this;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPassword() {
        return this.password;
    }

    public LoginMember password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoginMember)) {
            return false;
        }
        return id != null && id.equals(((LoginMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginMember{" +
            "id=" + getId() +
            ", memberName='" + getMemberName() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
