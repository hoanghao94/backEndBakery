package com.bakery.myapp.service;

import com.bakery.myapp.domain.Bakery;
import com.bakery.myapp.repository.BakeryRepository;
import com.bakery.myapp.service.dto.BakeryDTO;
import com.bakery.myapp.service.mapper.BakeryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bakery}.
 */
@Service
@Transactional
public class BakeryService {

    private final Logger log = LoggerFactory.getLogger(BakeryService.class);

    private final BakeryRepository bakeryRepository;

    private final BakeryMapper bakeryMapper;

    public BakeryService(BakeryRepository bakeryRepository, BakeryMapper bakeryMapper) {
        this.bakeryRepository = bakeryRepository;
        this.bakeryMapper = bakeryMapper;
    }

    /**
     * Save a bakery.
     *
     * @param bakeryDTO the entity to save.
     * @return the persisted entity.
     */
    public BakeryDTO save(BakeryDTO bakeryDTO) {
        log.debug("Request to save Bakery : {}", bakeryDTO);
        Bakery bakery = bakeryMapper.toEntity(bakeryDTO);
        bakery = bakeryRepository.save(bakery);
        return bakeryMapper.toDto(bakery);
    }

    /**
     * Update a bakery.
     *
     * @param bakeryDTO the entity to save.
     * @return the persisted entity.
     */
    public BakeryDTO update(BakeryDTO bakeryDTO) {
        log.debug("Request to update Bakery : {}", bakeryDTO);
        Bakery bakery = bakeryMapper.toEntity(bakeryDTO);
        bakery = bakeryRepository.save(bakery);
        return bakeryMapper.toDto(bakery);
    }

    /**
     * Partially update a bakery.
     *
     * @param bakeryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BakeryDTO> partialUpdate(BakeryDTO bakeryDTO) {
        log.debug("Request to partially update Bakery : {}", bakeryDTO);

        return bakeryRepository
            .findById(bakeryDTO.getId())
            .map(existingBakery -> {
                bakeryMapper.partialUpdate(existingBakery, bakeryDTO);

                return existingBakery;
            })
            .map(bakeryRepository::save)
            .map(bakeryMapper::toDto);
    }

    /**
     * Get all the bakeries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BakeryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bakeries");
        return bakeryRepository.findAll(pageable).map(bakeryMapper::toDto);
    }

    /**
     * Get one bakery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BakeryDTO> findOne(Long id) {
        log.debug("Request to get Bakery : {}", id);
        return bakeryRepository.findById(id).map(bakeryMapper::toDto);
    }

    /**
     * Delete the bakery by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bakery : {}", id);
        bakeryRepository.deleteById(id);
    }
}
