package com.poison.jwtcrudtesttask;
import com.poison.jwtcrudtesttask.controllers.UserController;
import com.poison.jwtcrudtesttask.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.poison.jwtcrudtesttask.service.UserService;


@WebMvcTest(UserController.class)  // Тестируем только контроллеры
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Для имитации HTTP-запросов

    @MockBean
    private UserService userService;  // Мокируем зависимость UserService

    @Test
    @WithMockUser  // Добавляем mock-пользователя для аутентификации
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john_doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane_doe");
        user2.setEmail("jane.doe@example.com");

        // Мокируем метод сервиса
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // Используем MockMvc для отправки GET-запроса
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())  // Ожидаем успешный ответ
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[1].username").value("jane_doe"));
    }

    @Test
    @WithMockUser
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    @WithMockUser
    public void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setId(1L);
        newUser.setUsername("john_doe");
        newUser.setEmail("john.doe@example.com");

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser
    public void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("john_doe_updated");
        updatedUser.setEmail("john.doe.updated@example.com");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe_updated\",\"email\":\"john.doe.updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe_updated"))
                .andExpect(jsonPath("$.email").value("john.doe.updated@example.com"));
    }

    @Test
    @WithMockUser
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isNoContent());  // 204 No Content после удаления
    }
}
