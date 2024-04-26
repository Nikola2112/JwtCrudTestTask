package com.poison.jwtcrudtesttask.repository;

import com.poison.jwtcrudtesttask.models.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {
}
