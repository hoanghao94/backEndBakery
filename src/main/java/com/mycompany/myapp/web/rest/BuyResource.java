package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.BuyRepository;
import com.mycompany.myapp.repository.CakeRepository;
import com.mycompany.myapp.service.BuyService;
import com.mycompany.myapp.service.CakeService;
import com.mycompany.myapp.service.CustomerService;
import com.mycompany.myapp.service.dto.BuyingDTO;
import com.mycompany.myapp.service.dto.CakeDTO;
import com.mycompany.myapp.service.dto.CustomerDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Cake}.
 */
@RestController
@RequestMapping("/api")
public class BuyResource {

    private final Logger log = LoggerFactory.getLogger(BuyResource.class);

    private static final String ENTITY_NAME = "buy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CakeService cakeService;

    private final CustomerService customerService;

    private final CakeRepository cakeRepository;

    private final BuyService buyService;

    private final BuyRepository buyRepository;

    public BuyResource(CakeService cakeService, CustomerService customerService, CakeRepository cakeRepository, BuyService buyService, BuyRepository buyRepository) {
        this.cakeService = cakeService;
        this.customerService = customerService;
        this.cakeRepository = cakeRepository;
        this.buyService = buyService;
        this.buyRepository = buyRepository;
    }

    /**
     * {@code POST  /cakes} : Create a new cake.
     *
     * @param buyingDTO the cakeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cakeDTO, or with status {@code 400 (Bad Request)} if the cake has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/buy")
    public ResponseEntity<BuyingDTO> createCake(@RequestBody BuyingDTO buyingDTO) throws URISyntaxException {
        log.debug("REST request to save Customer and Cake : {}", buyingDTO);
        if (buyingDTO.getCustomerDTO().getId() != null) {
            throw new BadRequestAlertException("A new cake cannot already have an ID", ENTITY_NAME, "idexists");
        }

        CustomerDTO customerDTO = customerService.save(buyingDTO.getCustomerDTO());
        for (int i = 0; i < buyingDTO.getCakeDTOList().length; i++) {
            buyingDTO.getCakeDTOList()[i].setIdCustomer(customerDTO.getId());
            CakeDTO result = cakeService.save((CakeDTO) buyingDTO.getCakeDTOList()[i]);
        }
        BuyingDTO savedBuyingDTO = new BuyingDTO();
        savedBuyingDTO.setCustomerDTO(customerDTO);
        savedBuyingDTO.setCakeDTOList(buyingDTO.getCakeDTOList());

        return ResponseEntity
            .created(new URI("/api/buy/" + customerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customerDTO.getId().toString()))
            .body(savedBuyingDTO);
    }
//
//    /**
//     * {@code PUT  /cakes/:id} : Updates an existing cake.
//     *
//     * @param id the id of the cakeDTO to save.
//     * @param cakeDTO the cakeDTO to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cakeDTO,
//     * or with status {@code 400 (Bad Request)} if the cakeDTO is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the cakeDTO couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/cakes/{id}")
//    public ResponseEntity<CakeDTO> updateCake(@PathVariable(value = "id", required = false) final Long id, @RequestBody CakeDTO cakeDTO)
//        throws URISyntaxException {
//        log.debug("REST request to update Cake : {}, {}", id, cakeDTO);
//        if (cakeDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, cakeDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!cakeRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        CakeDTO result = cakeService.update(cakeDTO);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cakeDTO.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * {@code PATCH  /cakes/:id} : Partial updates given fields of an existing cake, field will ignore if it is null
//     *
//     * @param id the id of the cakeDTO to save.
//     * @param cakeDTO the cakeDTO to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cakeDTO,
//     * or with status {@code 400 (Bad Request)} if the cakeDTO is not valid,
//     * or with status {@code 404 (Not Found)} if the cakeDTO is not found,
//     * or with status {@code 500 (Internal Server Error)} if the cakeDTO couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/cakes/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<CakeDTO> partialUpdateCake(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody CakeDTO cakeDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update Cake partially : {}, {}", id, cakeDTO);
//        if (cakeDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, cakeDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!cakeRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<CakeDTO> result = cakeService.partialUpdate(cakeDTO);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cakeDTO.getId().toString())
//        );
//    }
//
//    /**
//     * {@code GET  /cakes} : get all the cakes.
//     *
//     * @param pageable the pagination information.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cakes in body.
//     */

    @GetMapping("/buying")
    public ResponseEntity<List<BuyingDTO>> getAllCakes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cakes");
        Page<BuyingDTO> page = buyService.getAllBuying(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
//
//    /**
//     * {@code GET  /cakes/:id} : get the "id" cake.
//     *
//     * @param id the id of the cakeDTO to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cakeDTO, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/cakes/{id}")
//    public ResponseEntity<CakeDTO> getCake(@PathVariable Long id) {
//        log.debug("REST request to get Cake : {}", id);
//        Optional<CakeDTO> cakeDTO = cakeService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(cakeDTO);
//    }
//
//    /**
//     * {@code DELETE  /cakes/:id} : delete the "id" cake.
//     *
//     * @param id the id of the cakeDTO to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/cakes/{id}")
//    public ResponseEntity<Void> deleteCake(@PathVariable Long id) {
//        log.debug("REST request to delete Cake : {}", id);
//        cakeService.delete(id);
//        return ResponseEntity
//            .noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
//            .build();
//    }
}
