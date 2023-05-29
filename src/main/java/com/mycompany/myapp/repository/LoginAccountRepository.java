package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LoginAccount;
import com.mycompany.myapp.service.dto.LoginAccountDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoginAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoginAccountRepository extends JpaRepository<LoginAccount, Long> {
    @Query("SELECT m FROM LoginAccount m WHERE m.userName = :userName AND m.password = :password")
    LoginAccount findByLoginAccount(@Param("userName") String userName, @Param("password") String password);

}
