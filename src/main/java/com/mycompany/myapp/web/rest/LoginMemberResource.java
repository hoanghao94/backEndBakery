package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LoginMemberRepository;
import com.mycompany.myapp.service.LoginMemberService;
import com.mycompany.myapp.service.dto.LoginMemberDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LoginMember}.
 */
@RestController
@RequestMapping("/api")
public class LoginMemberResource {

    private final Logger log = LoggerFactory.getLogger(LoginMemberResource.class);

    private static final String ENTITY_NAME = "loginMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoginMemberService loginMemberService;

    private final LoginMemberRepository loginMemberRepository;

    public LoginMemberResource(LoginMemberService loginMemberService, LoginMemberRepository loginMemberRepository) {
        this.loginMemberService = loginMemberService;
        this.loginMemberRepository = loginMemberRepository;
    }

    /**
     * {@code POST  /login-members} : Create a new loginMember.
     *
     * @param loginMemberDTO the loginMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loginMemberDTO, or with status {@code 400 (Bad Request)} if the loginMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/login-members")
    public ResponseEntity<LoginMemberDTO> createLoginMember(@RequestBody LoginMemberDTO loginMemberDTO) throws URISyntaxException {
        log.debug("REST request to save LoginMember : {}", loginMemberDTO);
        if (loginMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new loginMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoginMemberDTO result = loginMemberService.save(loginMemberDTO);
        return ResponseEntity
            .created(new URI("/api/login-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /login-members/:id} : Updates an existing loginMember.
     *
     * @param id the id of the loginMemberDTO to save.
     * @param loginMemberDTO the loginMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginMemberDTO,
     * or with status {@code 400 (Bad Request)} if the loginMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loginMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/login-members/{id}")
    public ResponseEntity<LoginMemberDTO> updateLoginMember(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoginMemberDTO loginMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoginMember : {}, {}", id, loginMemberDTO);
        if (loginMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loginMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoginMemberDTO result = loginMemberService.update(loginMemberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loginMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /login-members/:id} : Partial updates given fields of an existing loginMember, field will ignore if it is null
     *
     * @param id the id of the loginMemberDTO to save.
     * @param loginMemberDTO the loginMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginMemberDTO,
     * or with status {@code 400 (Bad Request)} if the loginMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loginMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loginMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/login-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoginMemberDTO> partialUpdateLoginMember(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoginMemberDTO loginMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoginMember partially : {}, {}", id, loginMemberDTO);
        if (loginMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loginMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoginMemberDTO> result = loginMemberService.partialUpdate(loginMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, loginMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /login-members} : get all the loginMembers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loginMembers in body.
     */
    @GetMapping("/login-members")
    public ResponseEntity<List<LoginMemberDTO>> getAllLoginMembers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LoginMembers");
        Page<LoginMemberDTO> page = loginMemberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /login-members/:id} : get the "id" loginMember.
     *
     * @param id the id of the loginMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loginMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/login-members/{id}")
    public ResponseEntity<LoginMemberDTO> getLoginMember(@PathVariable Long id) {
        log.debug("REST request to get LoginMember : {}", id);
        Optional<LoginMemberDTO> loginMemberDTO = loginMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loginMemberDTO);
    }

    /**
     * {@code DELETE  /login-members/:id} : delete the "id" loginMember.
     *
     * @param id the id of the loginMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/login-members/{id}")
    public ResponseEntity<Void> deleteLoginMember(@PathVariable Long id) {
        log.debug("REST request to delete LoginMember : {}", id);
        loginMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
