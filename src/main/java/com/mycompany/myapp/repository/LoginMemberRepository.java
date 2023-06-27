package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LoginMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoginMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoginMemberRepository extends JpaRepository<LoginMember, Long> {}
