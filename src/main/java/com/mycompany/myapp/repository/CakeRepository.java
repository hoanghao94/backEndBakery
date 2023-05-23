package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cake;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cake entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CakeRepository extends JpaRepository<Cake, Long> {}
