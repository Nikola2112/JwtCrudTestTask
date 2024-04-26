package com.poison.jwtcrudtesttask.service;

import com.poison.jwtcrudtesttask.models.House;
import com.poison.jwtcrudtesttask.models.User;

import java.util.List;
import java.util.Optional;

public interface HouseService {
    House createHouse(String address, User owner);
    Optional<House> getHouseById(Long id);
    List<House> getAllHouses();
    Optional<House> updateHouse(Long id, String newAddress);
    void deleteHouse(Long id);
    void addTenant(Long houseId, Long userId, User owner);
}