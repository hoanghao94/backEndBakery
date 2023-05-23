package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Customer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Customer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerDTO implements Serializable {

    private Long id;

    private String name;

    private String phoneNumber;

    private LocalDate dateBuy;

    private Boolean isCalled;

    private Boolean isDelived;

    private LocalDate dateDelived;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateBuy() {
        return dateBuy;
    }

    public void setDateBuy(LocalDate dateBuy) {
        this.dateBuy = dateBuy;
    }

    public Boolean getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(Boolean isCalled) {
        this.isCalled = isCalled;
    }

    public Boolean getIsDelived() {
        return isDelived;
    }

    public void setIsDelived(Boolean isDelived) {
        this.isDelived = isDelived;
    }

    public LocalDate getDateDelived() {
        return dateDelived;
    }

    public void setDateDelived(LocalDate dateDelived) {
        this.dateDelived = dateDelived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dateBuy='" + getDateBuy() + "'" +
            ", isCalled='" + getIsCalled() + "'" +
            ", isDelived='" + getIsDelived() + "'" +
            ", dateDelived='" + getDateDelived() + "'" +
            "}";
    }
}
