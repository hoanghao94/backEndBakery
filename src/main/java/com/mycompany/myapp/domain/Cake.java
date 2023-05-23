package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Cake.
 */
@Entity
@Table(name = "cake")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cake implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_customer")
    private Long idCustomer;

    @Column(name = "name_cake")
    private String nameCake;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cake id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCustomer() {
        return this.idCustomer;
    }

    public Cake idCustomer(Long idCustomer) {
        this.setIdCustomer(idCustomer);
        return this;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNameCake() {
        return this.nameCake;
    }

    public Cake nameCake(String nameCake) {
        this.setNameCake(nameCake);
        return this;
    }

    public void setNameCake(String nameCake) {
        this.nameCake = nameCake;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Cake quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return this.price;
    }

    public Cake price(Integer price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cake)) {
            return false;
        }
        return id != null && id.equals(((Cake) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cake{" +
            "id=" + getId() +
            ", idCustomer=" + getIdCustomer() +
            ", nameCake='" + getNameCake() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            "}";
    }
}
