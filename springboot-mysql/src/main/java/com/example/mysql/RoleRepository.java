package com.example.mysql;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    List<Role> findByNameContaining(String name);

    Role findOneByNameContaining(String name);
}
