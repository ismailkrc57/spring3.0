package com.iso.spring3.security.repo;

import com.iso.spring3.security.models.Role;
import com.iso.spring3.security.models.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleType name);
    boolean existsByRoleName(RoleType name);
}
