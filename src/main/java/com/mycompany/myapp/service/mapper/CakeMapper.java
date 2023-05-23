package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cake;
import com.mycompany.myapp.service.dto.CakeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cake} and its DTO {@link CakeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CakeMapper extends EntityMapper<CakeDTO, Cake> {}
