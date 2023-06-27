package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LoginAccount;
import com.mycompany.myapp.repository.LoginAccountRepository;
import com.mycompany.myapp.service.dto.LoginAccountDTO;
import com.mycompany.myapp.service.mapper.LoginAccountMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LoginAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoginAccountResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/login-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoginAccountRepository loginAccountRepository;

    @Autowired
    private LoginAccountMapper loginAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoginAccountMockMvc;

    private LoginAccount loginAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginAccount createEntity(EntityManager em) {
        LoginAccount loginAccount = new LoginAccount().userName(DEFAULT_USER_NAME).password(DEFAULT_PASSWORD);
        return loginAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginAccount createUpdatedEntity(EntityManager em) {
        LoginAccount loginAccount = new LoginAccount().userName(UPDATED_USER_NAME).password(UPDATED_PASSWORD);
        return loginAccount;
    }

    @BeforeEach
    public void initTest() {
        loginAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createLoginAccount() throws Exception {
        int databaseSizeBeforeCreate = loginAccountRepository.findAll().size();
        // Create the LoginAccount
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);
        restLoginAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeCreate + 1);
        LoginAccount testLoginAccount = loginAccountList.get(loginAccountList.size() - 1);
        assertThat(testLoginAccount.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testLoginAccount.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createLoginAccountWithExistingId() throws Exception {
        // Create the LoginAccount with an existing ID
        loginAccount.setId(1L);
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);

        int databaseSizeBeforeCreate = loginAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoginAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLoginAccounts() throws Exception {
        // Initialize the database
        loginAccountRepository.saveAndFlush(loginAccount);

        // Get all the loginAccountList
        restLoginAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getLoginAccount() throws Exception {
        // Initialize the database
        loginAccountRepository.saveAndFlush(loginAccount);

        // Get the loginAccount
        restLoginAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, loginAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loginAccount.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingLoginAccount() throws Exception {
        // Get the loginAccount
        restLoginAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoginAccount() throws Exception {
        // Initialize the database
        loginAccountRepository.saveAndFlush(loginAccount);

        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();

        // Update the loginAccount
        LoginAccount updatedLoginAccount = loginAccountRepository.findById(loginAccount.getId()).get();
        // Disconnect from session so that the updates on updatedLoginAccount are not directly saved in db
        em.detach(updatedLoginAccount);
        updatedLoginAccount.userName(UPDATED_USER_NAME).password(UPDATED_PASSWORD);
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(updatedLoginAccount);

        restLoginAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loginAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
        LoginAccount testLoginAccount = loginAccountList.get(loginAccountList.size() - 1);
        assertThat(testLoginAccount.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testLoginAccount.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingLoginAccount() throws Exception {
        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();
        loginAccount.setId(count.incrementAndGet());

        // Create the LoginAccount
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoginAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loginAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoginAccount() throws Exception {
        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();
        loginAccount.setId(count.incrementAndGet());

        // Create the LoginAccount
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoginAccount() throws Exception {
        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();
        loginAccount.setId(count.incrementAndGet());

        // Create the LoginAccount
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoginAccountWithPatch() throws Exception {
        // Initialize the database
        loginAccountRepository.saveAndFlush(loginAccount);

        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();

        // Update the loginAccount using partial update
        LoginAccount partialUpdatedLoginAccount = new LoginAccount();
        partialUpdatedLoginAccount.setId(loginAccount.getId());

        restLoginAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoginAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoginAccount))
            )
            .andExpect(status().isOk());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
        LoginAccount testLoginAccount = loginAccountList.get(loginAccountList.size() - 1);
        assertThat(testLoginAccount.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testLoginAccount.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateLoginAccountWithPatch() throws Exception {
        // Initialize the database
        loginAccountRepository.saveAndFlush(loginAccount);

        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();

        // Update the loginAccount using partial update
        LoginAccount partialUpdatedLoginAccount = new LoginAccount();
        partialUpdatedLoginAccount.setId(loginAccount.getId());

        partialUpdatedLoginAccount.userName(UPDATED_USER_NAME).password(UPDATED_PASSWORD);

        restLoginAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoginAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoginAccount))
            )
            .andExpect(status().isOk());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
        LoginAccount testLoginAccount = loginAccountList.get(loginAccountList.size() - 1);
        assertThat(testLoginAccount.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testLoginAccount.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingLoginAccount() throws Exception {
        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();
        loginAccount.setId(count.incrementAndGet());

        // Create the LoginAccount
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoginAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loginAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoginAccount() throws Exception {
        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();
        loginAccount.setId(count.incrementAndGet());

        // Create the LoginAccount
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoginAccount() throws Exception {
        int databaseSizeBeforeUpdate = loginAccountRepository.findAll().size();
        loginAccount.setId(count.incrementAndGet());

        // Create the LoginAccount
        LoginAccountDTO loginAccountDTO = loginAccountMapper.toDto(loginAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loginAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoginAccount in the database
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoginAccount() throws Exception {
        // Initialize the database
        loginAccountRepository.saveAndFlush(loginAccount);

        int databaseSizeBeforeDelete = loginAccountRepository.findAll().size();

        // Delete the loginAccount
        restLoginAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, loginAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoginAccount> loginAccountList = loginAccountRepository.findAll();
        assertThat(loginAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
