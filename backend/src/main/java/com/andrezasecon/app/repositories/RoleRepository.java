package com.andrezasecon.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andrezasecon.app.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
