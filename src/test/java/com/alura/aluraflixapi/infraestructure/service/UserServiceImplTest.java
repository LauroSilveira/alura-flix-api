package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.UserDto;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.alura.aluraflixapi.infraestructure.mapper.UserMapperImpl;
import com.alura.aluraflixapi.infraestructure.repository.RoleRepository;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @SpyBean
    private UserServiceImpl userService;

    @MockBean
    private UserMapperImpl mapper;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save a new user")
    void saveUser_test() {
        //Given
        final var userDto = new UserDto(UUID.randomUUID().toString(),
                "user@aluraflix.com", new BCryptPasswordEncoder().encode("user@123456"),
                Set.of(Roles.builder()
                .id(UUID.randomUUID().toString())
                .role(RolesEnum.ROLE_USER)
                .build()));
        final var user = new User(userDto.id(), userDto.username(), userDto.password(), userDto.roles());
        when(this.mapper.mapToEntity(any())).thenReturn(user);
        when(this.roleRepository.saveAll(Mockito.anyList())).thenReturn(userDto.roles().stream().toList());
        when(this.userRepository.save(any())).thenReturn(user);
        when(this.mapper.mapToDto(any())).thenReturn(userDto);

        //When
        final var newUser = this.userService.saveUser(userDto);
        //Then
        assertThat(newUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userDto);
    }

    @Test
    @DisplayName("Should retrieve all users")
    void getUsers_test() {
        //Given
        final var usersDto = List.of(new UserDto(UUID.randomUUID().toString(),
                        "user@aluraflix.com", "user@123456", Set.of(Roles.builder()
                        .id(UUID.randomUUID().toString())
                        .role(RolesEnum.ROLE_USER)
                        .build())),
                new UserDto(UUID.randomUUID().toString(),
                        "admin@aluraflix.com", "admin@admin", Set.of(Roles.builder()
                        .id(UUID.randomUUID().toString())
                        .role(RolesEnum.ROLE_ADMIN)
                        .build())),
                new UserDto(UUID.randomUUID().toString(),
                        "guest@aluraflix.com", "quest@123456", Set.of(Roles.builder()
                        .id(UUID.randomUUID().toString())
                        .role(RolesEnum.ROLE_GUEST)
                        .build()))
        );
        final var users = List.of(
                new User(usersDto.get(0).id(), usersDto.get(0).username(), usersDto.get(0).password(), usersDto.get(0).roles()),
                new User(usersDto.get(1).id(), usersDto.get(1).username(), usersDto.get(1).password(), usersDto.get(1).roles()),
                new User(usersDto.get(2).id(), usersDto.get(2).username(), usersDto.get(2).password(), usersDto.get(2).roles())
        );

        when(this.userRepository.findAll()).thenReturn(users);

        when(this.mapper.mapToUsersDto(users)).thenReturn(usersDto);

        //When
        final var response = this.userService.getUsers();
        //Then
        assertThat(response)
                .isNotNull()
                .hasSize(3)
                .usingRecursiveComparison()
                .isEqualTo(usersDto);
    }
}