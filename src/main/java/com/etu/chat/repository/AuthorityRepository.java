package com.etu.chat.repository;

import com.etu.chat.entity.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Optional<Authority> findByName(String name);

    void deleteByName(String name);

}
