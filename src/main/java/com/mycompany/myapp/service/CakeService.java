package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cake;
import com.mycompany.myapp.repository.CakeRepository;
import com.mycompany.myapp.service.dto.CakeDTO;
import com.mycompany.myapp.service.mapper.CakeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cake}.
 */
@Service
@Transactional
public class CakeService {

    private final Logger log = LoggerFactory.getLogger(CakeService.class);

    private final CakeRepository cakeRepository;

    private final CakeMapper cakeMapper;

    public CakeService(CakeRepository cakeRepository, CakeMapper cakeMapper) {
        this.cakeRepository = cakeRepository;
        this.cakeMapper = cakeMapper;
    }

    /**
     * Save a cake.
     *
     * @param cakeDTO the entity to save.
     * @return the persisted entity.
     */
    public CakeDTO save(CakeDTO cakeDTO) {
        log.debug("Request to save Cake : {}", cakeDTO);
        Cake cake = cakeMapper.toEntity(cakeDTO);
        cake = cakeRepository.save(cake);
        return cakeMapper.toDto(cake);
    }

    /**
     * Update a cake.
     *
     * @param cakeDTO the entity to save.
     * @return the persisted entity.
     */
    public CakeDTO update(CakeDTO cakeDTO) {
        log.debug("Request to update Cake : {}", cakeDTO);
        Cake cake = cakeMapper.toEntity(cakeDTO);
        cake = cakeRepository.save(cake);
        return cakeMapper.toDto(cake);
    }

    /**
     * Partially update a cake.
     *
     * @param cakeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CakeDTO> partialUpdate(CakeDTO cakeDTO) {
        log.debug("Request to partially update Cake : {}", cakeDTO);

        return cakeRepository
            .findById(cakeDTO.getId())
            .map(existingCake -> {
                cakeMapper.partialUpdate(existingCake, cakeDTO);

                return existingCake;
            })
            .map(cakeRepository::save)
            .map(cakeMapper::toDto);
    }

    /**
     * Get all the cakes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CakeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cakes");
        return cakeRepository.findAll(pageable).map(cakeMapper::toDto);
    }

    /**
     * Get one cake by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CakeDTO> findOne(Long id) {
        log.debug("Request to get Cake : {}", id);
        return cakeRepository.findById(id).map(cakeMapper::toDto);
    }

    /**
     * Delete the cake by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cake : {}", id);
        cakeRepository.deleteById(id);
    }
}
