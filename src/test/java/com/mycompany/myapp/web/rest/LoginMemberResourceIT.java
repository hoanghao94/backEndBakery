package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LoginMember;
import com.mycompany.myapp.repository.LoginMemberRepository;
import com.mycompany.myapp.service.dto.LoginMemberDTO;
import com.mycompany.myapp.service.mapper.LoginMemberMapper;
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
 * Integration tests for the {@link LoginMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoginMemberResourceIT {

    private static final String DEFAULT_MEMBER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/login-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoginMemberRepository loginMemberRepository;

    @Autowired
    private LoginMemberMapper loginMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoginMemberMockMvc;

    private LoginMember loginMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginMember createEntity(EntityManager em) {
        LoginMember loginMember = new LoginMember().memberName(DEFAULT_MEMBER_NAME).password(DEFAULT_PASSWORD);
        return loginMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginMember createUpdatedEntity(EntityManager em) {
        LoginMember loginMember = new LoginMember().memberName(UPDATED_MEMBER_NAME).password(UPDATED_PASSWORD);
        return loginMember;
    }

    @BeforeEach
    public void initTest() {
        loginMember = createEntity(em);
    }

    @Test
    @Transactional
    void createLoginMember() throws Exception {
        int databaseSizeBeforeCreate = loginMemberRepository.findAll().size();
        // Create the LoginMember
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);
        restLoginMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeCreate + 1);
        LoginMember testLoginMember = loginMemberList.get(loginMemberList.size() - 1);
        assertThat(testLoginMember.getMemberName()).isEqualTo(DEFAULT_MEMBER_NAME);
        assertThat(testLoginMember.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createLoginMemberWithExistingId() throws Exception {
        // Create the LoginMember with an existing ID
        loginMember.setId(1L);
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);

        int databaseSizeBeforeCreate = loginMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoginMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLoginMembers() throws Exception {
        // Initialize the database
        loginMemberRepository.saveAndFlush(loginMember);

        // Get all the loginMemberList
        restLoginMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].memberName").value(hasItem(DEFAULT_MEMBER_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getLoginMember() throws Exception {
        // Initialize the database
        loginMemberRepository.saveAndFlush(loginMember);

        // Get the loginMember
        restLoginMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, loginMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loginMember.getId().intValue()))
            .andExpect(jsonPath("$.memberName").value(DEFAULT_MEMBER_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingLoginMember() throws Exception {
        // Get the loginMember
        restLoginMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoginMember() throws Exception {
        // Initialize the database
        loginMemberRepository.saveAndFlush(loginMember);

        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();

        // Update the loginMember
        LoginMember updatedLoginMember = loginMemberRepository.findById(loginMember.getId()).get();
        // Disconnect from session so that the updates on updatedLoginMember are not directly saved in db
        em.detach(updatedLoginMember);
        updatedLoginMember.memberName(UPDATED_MEMBER_NAME).password(UPDATED_PASSWORD);
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(updatedLoginMember);

        restLoginMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loginMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
        LoginMember testLoginMember = loginMemberList.get(loginMemberList.size() - 1);
        assertThat(testLoginMember.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
        assertThat(testLoginMember.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingLoginMember() throws Exception {
        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();
        loginMember.setId(count.incrementAndGet());

        // Create the LoginMember
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoginMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loginMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoginMember() throws Exception {
        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();
        loginMember.setId(count.incrementAndGet());

        // Create the LoginMember
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoginMember() throws Exception {
        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();
        loginMember.setId(count.incrementAndGet());

        // Create the LoginMember
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(loginMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoginMemberWithPatch() throws Exception {
        // Initialize the database
        loginMemberRepository.saveAndFlush(loginMember);

        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();

        // Update the loginMember using partial update
        LoginMember partialUpdatedLoginMember = new LoginMember();
        partialUpdatedLoginMember.setId(loginMember.getId());

        partialUpdatedLoginMember.memberName(UPDATED_MEMBER_NAME).password(UPDATED_PASSWORD);

        restLoginMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoginMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoginMember))
            )
            .andExpect(status().isOk());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
        LoginMember testLoginMember = loginMemberList.get(loginMemberList.size() - 1);
        assertThat(testLoginMember.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
        assertThat(testLoginMember.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateLoginMemberWithPatch() throws Exception {
        // Initialize the database
        loginMemberRepository.saveAndFlush(loginMember);

        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();

        // Update the loginMember using partial update
        LoginMember partialUpdatedLoginMember = new LoginMember();
        partialUpdatedLoginMember.setId(loginMember.getId());

        partialUpdatedLoginMember.memberName(UPDATED_MEMBER_NAME).password(UPDATED_PASSWORD);

        restLoginMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoginMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoginMember))
            )
            .andExpect(status().isOk());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
        LoginMember testLoginMember = loginMemberList.get(loginMemberList.size() - 1);
        assertThat(testLoginMember.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
        assertThat(testLoginMember.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingLoginMember() throws Exception {
        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();
        loginMember.setId(count.incrementAndGet());

        // Create the LoginMember
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoginMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loginMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoginMember() throws Exception {
        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();
        loginMember.setId(count.incrementAndGet());

        // Create the LoginMember
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoginMember() throws Exception {
        int databaseSizeBeforeUpdate = loginMemberRepository.findAll().size();
        loginMember.setId(count.incrementAndGet());

        // Create the LoginMember
        LoginMemberDTO loginMemberDTO = loginMemberMapper.toDto(loginMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginMemberMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(loginMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoginMember in the database
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoginMember() throws Exception {
        // Initialize the database
        loginMemberRepository.saveAndFlush(loginMember);

        int databaseSizeBeforeDelete = loginMemberRepository.findAll().size();

        // Delete the loginMember
        restLoginMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, loginMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoginMember> loginMemberList = loginMemberRepository.findAll();
        assertThat(loginMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
