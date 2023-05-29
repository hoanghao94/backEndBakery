package com.mycompany.myapp.web.rest;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.mycompany.myapp.repository.LoginAccountRepository;
import com.mycompany.myapp.service.LoginAccountService;
import com.mycompany.myapp.service.dto.LoginAccountDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LoginAccount}.
 */
@RestController
@RequestMapping("/api")
public class LoginAccountResource {

    private final Logger log = LoggerFactory.getLogger(LoginAccountResource.class);

    private static final String ENTITY_NAME = "loginAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoginAccountService loginAccountService;

    private final LoginAccountRepository loginAccountRepository;

    public LoginAccountResource(LoginAccountService loginAccountService, LoginAccountRepository loginAccountRepository) {
        this.loginAccountService = loginAccountService;
        this.loginAccountRepository = loginAccountRepository;
    }

    /**
     * {@code POST  /login-accounts} : Create a new loginAccount.
     *
     * @param loginAccountDTO the loginAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loginAccountDTO, or with status {@code 400 (Bad Request)} if the loginAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/login-accounts")
    public ResponseEntity<LoginAccountDTO> createLoginAccount(@RequestBody LoginAccountDTO loginAccountDTO) throws URISyntaxException {
        log.debug("REST request to save LoginAccount : {}", loginAccountDTO);
        if (loginAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new loginAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoginAccountDTO result = loginAccountService.save(loginAccountDTO);
        return ResponseEntity
            .created(new URI("/api/login-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /login-accounts/:id} : Updates an existing loginAccount.
     *
     * @param id the id of the loginAccountDTO to save.
     * @param loginAccountDTO the loginAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginAccountDTO,
     * or with status {@code 400 (Bad Request)} if the loginAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loginAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/login-accounts/{id}")
    public ResponseEntity<LoginAccountDTO> updateLoginAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoginAccountDTO loginAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoginAccount : {}, {}", id, loginAccountDTO);
        if (loginAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loginAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoginAccountDTO result = loginAccountService.update(loginAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loginAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /login-accounts/:id} : Partial updates given fields of an existing loginAccount, field will ignore if it is null
     *
     * @param id the id of the loginAccountDTO to save.
     * @param loginAccountDTO the loginAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginAccountDTO,
     * or with status {@code 400 (Bad Request)} if the loginAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loginAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loginAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/login-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoginAccountDTO> partialUpdateLoginAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoginAccountDTO loginAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoginAccount partially : {}, {}", id, loginAccountDTO);
        if (loginAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loginAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoginAccountDTO> result = loginAccountService.partialUpdate(loginAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loginAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /login-accounts} : get all the loginAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loginAccounts in body.
     */
    @GetMapping("/login-accounts")
    public ResponseEntity<List<LoginAccountDTO>> getAllLoginAccounts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LoginAccounts");
        Page<LoginAccountDTO> page = loginAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/check-login-accounts")
    public ResponseEntity<?> checkLoginAccounts(@RequestBody LoginAccountDTO loginAccountDTO) {
        log.debug("REST request to check LoginAccount");

        // Kiểm tra nếu loginAccount là null hoặc empty
        if (loginAccountDTO == null || StringUtils.isEmpty(loginAccountDTO.getUserName()) || StringUtils.isEmpty(loginAccountDTO.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid LoginAccount");
        }

        // Thực hiện kiểm tra LoginAccount
        LoginAccountDTO loginAccount = loginAccountService.findByLoginAccount(loginAccountDTO);

        // Kiểm tra nếu không tìm thấy LoginAccount
        if (loginAccount == null) {
            return ResponseEntity.notFound().build();
        }

        // Trả về phản hồi thành công nếu tìm thấy LoginAccount
        return ResponseEntity.ok(loginAccount);
    }

    /**
     * {@code GET  /login-accounts/:id} : get the "id" loginAccount.
     *
     * @param id the id of the loginAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loginAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/login-accounts/{id}")
    public ResponseEntity<LoginAccountDTO> getLoginAccount(@PathVariable Long id) {
        log.debug("REST request to get LoginAccount : {}", id);
        Optional<LoginAccountDTO> loginAccountDTO = loginAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loginAccountDTO);
    }

    /**
     * {@code DELETE  /login-accounts/:id} : delete the "id" loginAccount.
     *
     * @param id the id of the loginAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/login-accounts/{id}")
    public ResponseEntity<Void> deleteLoginAccount(@PathVariable Long id) {
        log.debug("REST request to delete LoginAccount : {}", id);
        loginAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
