package com.etu.chat.repository;

import com.etu.chat.entity.RegistrationRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistrationRequestRepository extends CrudRepository<RegistrationRequest, Long> {

    List<RegistrationRequest> findByIdBeforeOrderByIdDesc(Long startId, Pageable pageable);

    List<RegistrationRequest> findByOrderByIdDesc(Pageable pageable);

}
