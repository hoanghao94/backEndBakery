package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.LoginAccount;
import com.mycompany.myapp.repository.LoginAccountRepository;
import com.mycompany.myapp.service.LoginAccountService;
import com.mycompany.myapp.service.dto.LoginAccountDTO;
import com.mycompany.myapp.service.mapper.LoginAccountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoginAccount}.
 */
@Service
@Transactional
public class LoginAccountServiceImpl implements LoginAccountService {

    private final Logger log = LoggerFactory.getLogger(LoginAccountServiceImpl.class);

    private final LoginAccountRepository loginAccountRepository;

    private final LoginAccountMapper loginAccountMapper;

    public LoginAccountServiceImpl(LoginAccountRepository loginAccountRepository, LoginAccountMapper loginAccountMapper) {
        this.loginAccountRepository = loginAccountRepository;
        this.loginAccountMapper = loginAccountMapper;
    }

    @Override
    public LoginAccountDTO save(LoginAccountDTO loginAccountDTO) {
        log.debug("Request to save LoginAccount : {}", loginAccountDTO);
        LoginAccount loginAccount = loginAccountMapper.toEntity(loginAccountDTO);
        loginAccount = loginAccountRepository.save(loginAccount);
        return loginAccountMapper.toDto(loginAccount);
    }

    @Override
    public LoginAccountDTO update(LoginAccountDTO loginAccountDTO) {
        log.debug("Request to update LoginAccount : {}", loginAccountDTO);
        LoginAccount loginAccount = loginAccountMapper.toEntity(loginAccountDTO);
        loginAccount = loginAccountRepository.save(loginAccount);
        return loginAccountMapper.toDto(loginAccount);
    }

    @Override
    public Optional<LoginAccountDTO> partialUpdate(LoginAccountDTO loginAccountDTO) {
        log.debug("Request to partially update LoginAccount : {}", loginAccountDTO);

        return loginAccountRepository
            .findById(loginAccountDTO.getId())
            .map(existingLoginAccount -> {
                loginAccountMapper.partialUpdate(existingLoginAccount, loginAccountDTO);

                return existingLoginAccount;
            })
            .map(loginAccountRepository::save)
            .map(loginAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoginAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoginAccounts");
        return loginAccountRepository.findAll(pageable).map(loginAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoginAccountDTO> findOne(Long id) {
        log.debug("Request to get LoginAccount : {}", id);
        return loginAccountRepository.findById(id).map(loginAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoginAccount : {}", id);
        loginAccountRepository.deleteById(id);
    }

    @Override
    public LoginAccountDTO findByLoginAccount(LoginAccountDTO loginAccountDTO) {
        LoginAccount loginAccount = loginAccountMapper.toEntity(loginAccountDTO);
        loginAccount = loginAccountRepository.findByLoginAccount(loginAccountDTO.getUserName(),loginAccountDTO.getPassword());
        return  loginAccountMapper.toDto(loginAccount);
    }
}
