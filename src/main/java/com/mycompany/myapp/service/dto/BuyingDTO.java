package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Cake;
import com.mycompany.myapp.domain.Customer;

public class BuyingDTO {

    private CustomerDTO customerDTO;
    private CakeDTO[] cakeDTOList;

    public BuyingDTO(CakeDTO[] cakeDTOList, CustomerDTO customerDTO) {
        this.cakeDTOList = cakeDTOList;
        this.customerDTO = customerDTO;
    }

    public BuyingDTO(Cake cake, Customer customer) {
        this.cakeDTOList = new CakeDTO[]{};
//        this.cakeDTOList = new CakeDTO[] { new CakeDTO(cake) };
        this.customerDTO = new CustomerDTO();
    }

    public BuyingDTO() {
    }

    public CakeDTO[] getCakeDTOList() {
        return cakeDTOList;
    }

    public void setCakeDTOList(CakeDTO[] cakeDTOList) {
        this.cakeDTOList = cakeDTOList;
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }
}
