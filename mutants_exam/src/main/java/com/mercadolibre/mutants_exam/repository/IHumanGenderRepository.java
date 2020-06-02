package com.mercadolibre.mutants_exam.repository;

import com.mercadolibre.mutants_exam.entity.HumanGender;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface IHumanGenderRepository extends CrudRepository<HumanGender, Long> {

    @Cacheable
    Optional<HumanGender> getBydna(UUID dna);
}
