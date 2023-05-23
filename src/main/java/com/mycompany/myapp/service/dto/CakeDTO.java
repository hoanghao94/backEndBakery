package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Cake;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Cake} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CakeDTO implements Serializable {

    private Long id;

    private Long idCustomer;

    private String nameCake;

    private Integer quantity;

    private Integer price;

    public CakeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNameCake() {
        return nameCake;
    }

    public void setNameCake(String nameCake) {
        this.nameCake = nameCake;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CakeDTO)) {
            return false;
        }

        CakeDTO cakeDTO = (CakeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cakeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CakeDTO{" +
            "id=" + getId() +
            ", idCustomer=" + getIdCustomer() +
            ", nameCake='" + getNameCake() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            "}";
    }
}
