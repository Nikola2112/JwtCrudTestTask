package com.poison.jwtcrudtesttask.repository;


import com.poison.jwtcrudtesttask.models.ERole;
import com.poison.jwtcrudtesttask.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
