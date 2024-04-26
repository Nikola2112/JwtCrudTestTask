package com.poison.jwtcrudtesttask.controllers;
import com.poison.jwtcrudtesttask.models.House;
import com.poison.jwtcrudtesttask.models.User;

import com.poison.jwtcrudtesttask.service.HouseService;
import com.poison.jwtcrudtesttask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final UserService userService;

    @PostMapping
    public House createHouse(@RequestParam String address, Principal principal) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User owner = userService.getUserByName(username);

        return houseService.createHouse(address, owner);
    }

    @GetMapping
    public List<House> getAllHouses() {
        return houseService.getAllHouses();
    }

    @GetMapping("/{id}")
    public Optional<House> getHouseById(@PathVariable Long id) {
        return houseService.getHouseById(id);
    }

    @PutMapping("/{id}")
    public Optional<House> updateHouse(@PathVariable Long id, @RequestParam String newAddress) {
        return houseService.updateHouse(id, newAddress);
    }

    @DeleteMapping("/{id}")
    public void deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
    }

    @PostMapping("/{houseId}/tenants/{userId}")
    public void addTenant(@PathVariable("houseId") Long houseId, @PathVariable("userId") Long userId, Principal principal) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User owner = userService.getUserByName(username);

        houseService.addTenant(houseId, userId, owner);
    }
}
