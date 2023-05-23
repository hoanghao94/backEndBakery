package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.service.dto.BuyingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c, ca FROM Customer c INNER JOIN Cake ca ON c.id = ca.idCustomer")
    Page<Object[]> findBuying(Pageable pageable);
}
