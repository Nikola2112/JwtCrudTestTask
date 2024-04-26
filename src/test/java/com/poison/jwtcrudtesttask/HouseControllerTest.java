package com.poison.jwtcrudtesttask;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.poison.jwtcrudtesttask.controllers.HouseController;
import com.poison.jwtcrudtesttask.models.House;
import com.poison.jwtcrudtesttask.models.User;
import com.poison.jwtcrudtesttask.service.HouseService;
import com.poison.jwtcrudtesttask.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.security.Principal;
import java.util.*;

@WebMvcTest(HouseController.class)
public class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseService houseService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser  // Добавляем mock-пользователя
    public void testCreateHouse() throws Exception {
        User user = new User();
        user.setUsername("owner");

        House newHouse = new House();
        newHouse.setAddress("123 Main St");

        when(userService.getUserByName("owner")).thenReturn(user);  // Имитируем получение пользователя
        when(houseService.createHouse("123 Main St", user)).thenReturn(newHouse);  // Имитируем создание дома

        mockMvc.perform(post("/houses")
                        .param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    @WithMockUser
    public void testGetAllHouses() throws Exception {
        House house1 = new House();
        house1.setAddress("123 Main St");

        House house2 = new House();
        house2.setAddress("456 Elm St");

        when(houseService.getAllHouses()).thenReturn(Arrays.asList(house1, house2));

        mockMvc.perform(get("/houses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[1].address").value("456 Elm St"));
    }

    @Test
    @WithMockUser
    public void testGetHouseById() throws Exception {
        House house = new House();
        house.setAddress("123 Main St");

        when(houseService.getHouseById(1L)).thenReturn(Optional.of(house));  // Имитация получения дома по ID

        mockMvc.perform(get("/houses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    @WithMockUser
    public void testUpdateHouse() throws Exception {
        House updatedHouse = new House();
        updatedHouse.setAddress("123 New St");

        when(houseService.updateHouse(1L, "123 New St")).thenReturn(Optional.of(updatedHouse));

        mockMvc.perform(put("/houses/1")
                        .param("newAddress", "123 New St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 New St"));
    }

    @Test
    @WithMockUser
    public void testDeleteHouse() throws Exception {
        doNothing().when(houseService).deleteHouse(1L);  // Имитируем удаление

        mockMvc.perform(delete("/houses/1"))
                .andExpect(status().isNoContent());  // Ожидаем 204 No Content после удаления
    }

    @Test
    @WithMockUser
    public void testAddTenant() throws Exception {
        User owner = new User();
        owner.setUsername("owner");

        doNothing().when(houseService).addTenant(1L, 2L, owner);  // Имитация добавления арендатора

        mockMvc.perform(post("/houses/1/tenants/2"))
                .andExpect(status().isOk());
    }
}
