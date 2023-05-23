package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_buy", columnDefinition = "DATE default CURRENT_DATE()", nullable = false)
    private LocalDate dateBuy;

    @Column(name = "is_called", columnDefinition = "BIT default 0", nullable = false)
    private Boolean isCalled;

    @Column(name = "is_delived", columnDefinition = "BIT default 0", nullable = false)
    private Boolean isDelived;

    @Column(name = "date_delived")
    private LocalDate dateDelived;

    @PrePersist
    public void prePersist() {
        if (this.dateBuy == null) {
            this.dateBuy = LocalDate.now();
        }
        if (this.isCalled == null) {
            this.isCalled = false;
        }
        if (this.isDelived == null) {
            this.isDelived = false;
        }
    }
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Customer phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateBuy() {
        return this.dateBuy;
    }

    public Customer dateBuy(LocalDate dateBuy) {
        this.setDateBuy(dateBuy);
        return this;
    }

    public void setDateBuy(LocalDate dateBuy) {
        this.dateBuy = dateBuy;
    }

    public Boolean getIsCalled() {
        return this.isCalled;
    }

    public Customer isCalled(Boolean isCalled) {
        this.setIsCalled(isCalled);
        return this;
    }

    public void setIsCalled(Boolean isCalled) {
        this.isCalled = isCalled;
    }

    public Boolean getIsDelived() {
        return this.isDelived;
    }

    public Customer isDelived(Boolean isDelived) {
        this.setIsDelived(isDelived);
        return this;
    }

    public void setIsDelived(Boolean isDelived) {
        this.isDelived = isDelived;
    }

    public LocalDate getDateDelived() {
        return this.dateDelived;
    }

    public Customer dateDelived(LocalDate dateDelived) {
        this.setDateDelived(dateDelived);
        return this;
    }

    public void setDateDelived(LocalDate dateDelived) {
        this.dateDelived = dateDelived;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
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
