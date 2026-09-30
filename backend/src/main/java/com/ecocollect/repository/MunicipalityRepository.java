package com.ecocollect.repository;

import com.ecocollect.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    Optional<Municipality> findByCode(String code);

    boolean existsByCode(String code);
}
