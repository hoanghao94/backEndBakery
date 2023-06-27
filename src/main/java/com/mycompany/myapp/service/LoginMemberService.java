package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LoginMember;
import com.mycompany.myapp.repository.LoginMemberRepository;
import com.mycompany.myapp.service.dto.LoginMemberDTO;
import com.mycompany.myapp.service.mapper.LoginMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoginMember}.
 */
@Service
@Transactional
public class LoginMemberService {

    private final Logger log = LoggerFactory.getLogger(LoginMemberService.class);

    private final LoginMemberRepository loginMemberRepository;

    private final LoginMemberMapper loginMemberMapper;

    public LoginMemberService(LoginMemberRepository loginMemberRepository, LoginMemberMapper loginMemberMapper) {
        this.loginMemberRepository = loginMemberRepository;
        this.loginMemberMapper = loginMemberMapper;
    }

    /**
     * Save a loginMember.
     *
     * @param loginMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public LoginMemberDTO save(LoginMemberDTO loginMemberDTO) {
        log.debug("Request to save LoginMember : {}", loginMemberDTO);
        LoginMember loginMember = loginMemberMapper.toEntity(loginMemberDTO);
        loginMember = loginMemberRepository.save(loginMember);
        return loginMemberMapper.toDto(loginMember);
    }

    /**
     * Update a loginMember.
     *
     * @param loginMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public LoginMemberDTO update(LoginMemberDTO loginMemberDTO) {
        log.debug("Request to update LoginMember : {}", loginMemberDTO);
        LoginMember loginMember = loginMemberMapper.toEntity(loginMemberDTO);
        loginMember = loginMemberRepository.save(loginMember);
        return loginMemberMapper.toDto(loginMember);
    }

    /**
     * Partially update a loginMember.
     *
     * @param loginMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LoginMemberDTO> partialUpdate(LoginMemberDTO loginMemberDTO) {
        log.debug("Request to partially update LoginMember : {}", loginMemberDTO);

        return loginMemberRepository
            .findById(loginMemberDTO.getId())
            .map(existingLoginMember -> {
                loginMemberMapper.partialUpdate(existingLoginMember, loginMemberDTO);

                return existingLoginMember;
            })
            .map(loginMemberRepository::save)
            .map(loginMemberMapper::toDto);
    }

    /**
     * Get all the loginMembers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LoginMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoginMembers");
        return loginMemberRepository.findAll(pageable).map(loginMemberMapper::toDto);
    }

    /**
     * Get one loginMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LoginMemberDTO> findOne(Long id) {
        log.debug("Request to get LoginMember : {}", id);
        return loginMemberRepository.findById(id).map(loginMemberMapper::toDto);
    }

    /**
     * Delete the loginMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LoginMember : {}", id);
        loginMemberRepository.deleteById(id);
    }
}
