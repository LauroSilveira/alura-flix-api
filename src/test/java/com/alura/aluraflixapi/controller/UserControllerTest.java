package com.alura.aluraflixapi.controller;

import com.alura.aluraflixapi.controller.user.UserController;
import com.alura.aluraflixapi.domain.user.dto.RolesDto;
import com.alura.aluraflixapi.domain.user.dto.UserDTO;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.alura.aluraflixapi.infraestructure.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Spy
    private UserService userService;

    @Test
    @DisplayName("Should save a new user and response 200 OK")
    void createUser_test() {
        //Given
        final var userId = UUID.randomUUID().toString();
        final var roleId = UUID.randomUUID().toString();
        final var user = new UserDTO(userId, "guest@aluraflix.com.br", "123456",
                Set.of(new RolesDto(roleId, RolesEnum.ROLE_GUEST)));
        when(this.userService.saveUser(any())).thenReturn(user);

        //When
        final var response = this.userController.createUser(user);

        //Then
        assertThat(response).isNotNull();
        assertThat(user).usingRecursiveComparison().isEqualTo(response.getBody());
    }

    @Test
    @DisplayName("Should return a list of users and response 200 OK")
    void getUsers_test() {
        //Given
        final var usersList = List.of(new UserDTO(UUID.randomUUID().toString(),
                        "guest@aluraflix.com.br", "123456",
                        Set.of(new RolesDto(UUID.randomUUID().toString(), RolesEnum.ROLE_GUEST))),
                new UserDTO(UUID.randomUUID().toString(), "admin@aluraflix.com.br", "admin",
                        Set.of(new RolesDto(UUID.randomUUID().toString(), RolesEnum.ROLE_ADMIN))));
        when(this.userService.getUsers()).thenReturn(usersList);

        //When
        final var response = this.userController.getUsers();

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(usersList);
    }
}