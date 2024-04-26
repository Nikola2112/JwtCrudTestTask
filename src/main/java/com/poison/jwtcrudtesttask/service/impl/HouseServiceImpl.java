package com.poison.jwtcrudtesttask.service.impl;
import java.util.List;
import java.util.Optional;

import com.poison.jwtcrudtesttask.models.House;
import com.poison.jwtcrudtesttask.models.User;
import com.poison.jwtcrudtesttask.repository.HouseRepository;
import com.poison.jwtcrudtesttask.repository.UserRepository;
import com.poison.jwtcrudtesttask.service.HouseService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    public House createHouse(String address, User owner) {
        House house = new House();
        house.setAddress(address);
        house.setOwner(owner);
        return houseRepository.save(house);
    }

    public Optional<House> getHouseById(Long id) {
        return houseRepository.findById(id);
    }

    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }

    public Optional<House> updateHouse(Long id, String newAddress) {
        return houseRepository.findById(id).map(existingHouse -> {
            existingHouse.setAddress(newAddress);
            return houseRepository.save(existingHouse);
        });
    }

    public void deleteHouse(Long id) {
        houseRepository.deleteById(id);
    }

    public void addTenant(Long houseId, Long userId, User owner) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new RuntimeException("Дом не найден"));

        if (!house.getOwner().equals(owner)) {
            throw new SecurityException("Только хозяин дома может добавлять жильцов");
        }

        User tenant = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!house.getTenants().contains(tenant)) {
            house.getTenants().add(tenant);
            tenant.getRentedHouses().add(house);
            houseRepository.save(house);
            userRepository.save(tenant);
        }
    }
}
