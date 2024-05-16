package com.etu.chat.repository;

import com.etu.chat.entity.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    void deleteByName(String name);

}
