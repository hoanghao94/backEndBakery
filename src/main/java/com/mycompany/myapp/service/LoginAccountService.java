package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.LoginAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.LoginAccount}.
 */
public interface LoginAccountService {
    /**
     * Save a loginAccount.
     *
     * @param loginAccountDTO the entity to save.
     * @return the persisted entity.
     */
    LoginAccountDTO save(LoginAccountDTO loginAccountDTO);

    /**
     * Updates a loginAccount.
     *
     * @param loginAccountDTO the entity to update.
     * @return the persisted entity.
     */
    LoginAccountDTO update(LoginAccountDTO loginAccountDTO);

    /**
     * Partially updates a loginAccount.
     *
     * @param loginAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoginAccountDTO> partialUpdate(LoginAccountDTO loginAccountDTO);

    /**
     * Get all the loginAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoginAccountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loginAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoginAccountDTO> findOne(Long id);

    /**
     * Delete the "id" loginAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    LoginAccountDTO findByLoginAccount(LoginAccountDTO loginAccountDTO);
}
