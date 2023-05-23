package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cake;
import com.mycompany.myapp.repository.CakeRepository;
import com.mycompany.myapp.service.dto.CakeDTO;
import com.mycompany.myapp.service.mapper.CakeMapper;
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
 * Integration tests for the {@link CakeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CakeResourceIT {

    private static final Long DEFAULT_ID_CUSTOMER = 1L;
    private static final Long UPDATED_ID_CUSTOMER = 2L;

    private static final String DEFAULT_NAME_CAKE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_CAKE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final String ENTITY_API_URL = "/api/cakes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CakeRepository cakeRepository;

    @Autowired
    private CakeMapper cakeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCakeMockMvc;

    private Cake cake;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cake createEntity(EntityManager em) {
        Cake cake = new Cake().idCustomer(DEFAULT_ID_CUSTOMER).nameCake(DEFAULT_NAME_CAKE).quantity(DEFAULT_QUANTITY).price(DEFAULT_PRICE);
        return cake;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cake createUpdatedEntity(EntityManager em) {
        Cake cake = new Cake().idCustomer(UPDATED_ID_CUSTOMER).nameCake(UPDATED_NAME_CAKE).quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);
        return cake;
    }

    @BeforeEach
    public void initTest() {
        cake = createEntity(em);
    }

    @Test
    @Transactional
    void createCake() throws Exception {
        int databaseSizeBeforeCreate = cakeRepository.findAll().size();
        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);
        restCakeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isCreated());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeCreate + 1);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getIdCustomer()).isEqualTo(DEFAULT_ID_CUSTOMER);
        assertThat(testCake.getNameCake()).isEqualTo(DEFAULT_NAME_CAKE);
        assertThat(testCake.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testCake.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createCakeWithExistingId() throws Exception {
        // Create the Cake with an existing ID
        cake.setId(1L);
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        int databaseSizeBeforeCreate = cakeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCakeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCakes() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        // Get all the cakeList
        restCakeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cake.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCustomer").value(hasItem(DEFAULT_ID_CUSTOMER.intValue())))
            .andExpect(jsonPath("$.[*].nameCake").value(hasItem(DEFAULT_NAME_CAKE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getCake() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        // Get the cake
        restCakeMockMvc
            .perform(get(ENTITY_API_URL_ID, cake.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cake.getId().intValue()))
            .andExpect(jsonPath("$.idCustomer").value(DEFAULT_ID_CUSTOMER.intValue()))
            .andExpect(jsonPath("$.nameCake").value(DEFAULT_NAME_CAKE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE));
    }

    @Test
    @Transactional
    void getNonExistingCake() throws Exception {
        // Get the cake
        restCakeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCake() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();

        // Update the cake
        Cake updatedCake = cakeRepository.findById(cake.getId()).get();
        // Disconnect from session so that the updates on updatedCake are not directly saved in db
        em.detach(updatedCake);
        updatedCake.idCustomer(UPDATED_ID_CUSTOMER).nameCake(UPDATED_NAME_CAKE).quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);
        CakeDTO cakeDTO = cakeMapper.toDto(updatedCake);

        restCakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cakeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getIdCustomer()).isEqualTo(UPDATED_ID_CUSTOMER);
        assertThat(testCake.getNameCake()).isEqualTo(UPDATED_NAME_CAKE);
        assertThat(testCake.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCake.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cakeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCakeWithPatch() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();

        // Update the cake using partial update
        Cake partialUpdatedCake = new Cake();
        partialUpdatedCake.setId(cake.getId());

        partialUpdatedCake.idCustomer(UPDATED_ID_CUSTOMER).quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);

        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCake.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCake))
            )
            .andExpect(status().isOk());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getIdCustomer()).isEqualTo(UPDATED_ID_CUSTOMER);
        assertThat(testCake.getNameCake()).isEqualTo(DEFAULT_NAME_CAKE);
        assertThat(testCake.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCake.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateCakeWithPatch() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();

        // Update the cake using partial update
        Cake partialUpdatedCake = new Cake();
        partialUpdatedCake.setId(cake.getId());

        partialUpdatedCake.idCustomer(UPDATED_ID_CUSTOMER).nameCake(UPDATED_NAME_CAKE).quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);

        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCake.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCake))
            )
            .andExpect(status().isOk());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
        Cake testCake = cakeList.get(cakeList.size() - 1);
        assertThat(testCake.getIdCustomer()).isEqualTo(UPDATED_ID_CUSTOMER);
        assertThat(testCake.getNameCake()).isEqualTo(UPDATED_NAME_CAKE);
        assertThat(testCake.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCake.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cakeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cakeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCake() throws Exception {
        int databaseSizeBeforeUpdate = cakeRepository.findAll().size();
        cake.setId(count.incrementAndGet());

        // Create the Cake
        CakeDTO cakeDTO = cakeMapper.toDto(cake);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCakeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cakeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cake in the database
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCake() throws Exception {
        // Initialize the database
        cakeRepository.saveAndFlush(cake);

        int databaseSizeBeforeDelete = cakeRepository.findAll().size();

        // Delete the cake
        restCakeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cake.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cake> cakeList = cakeRepository.findAll();
        assertThat(cakeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
