package com.alura.aluraflixapi.infraestructure.service;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.RolesDto;
import com.alura.aluraflixapi.domain.user.dto.UserDto;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import com.alura.aluraflixapi.domain.user.roles.RolesEnum;
import com.alura.aluraflixapi.infraestructure.mapper.UserMapperImpl;
import com.alura.aluraflixapi.infraestructure.repository.RoleRepository;
import com.alura.aluraflixapi.infraestructure.repository.UserRepository;
import com.alura.aluraflixapi.infraestructure.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private UserMapperImpl mapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save a new user")
    void saveUser_test() {
        //Given
        final var userDto = new UserDto(UUID.randomUUID().toString(),
                "user@aluraflix.com", new BCryptPasswordEncoder().encode("user@123456"),
                Set.of(new RolesDto(UUID.randomUUID().toString(), RolesEnum.ROLE_USER)));

        final var user = new User(userDto.id(), userDto.username(), userDto.password(),
                Set.of(Roles.builder().id(userDto.roles().stream().toList()
                        .getFirst().id()).role(userDto.roles().stream().toList().getFirst().role()).build()));

        when(this.mapper.mapToEntity(any())).thenReturn(user);
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
        final var usersDto = List.of(
                new UserDto(UUID.randomUUID().toString(), "user@aluraflix.com", "user@123456", Set.of(new RolesDto(UUID.randomUUID().toString(), RolesEnum.ROLE_USER))),
                new UserDto(UUID.randomUUID().toString(), "guest@aluraflix.com", "quest@123456", Set.of(new RolesDto(UUID.randomUUID().toString(), RolesEnum.ROLE_GUEST))),
                new UserDto(UUID.randomUUID().toString(), "admin@aluraflix.com", "admin@admin", Set.of(new RolesDto(UUID.randomUUID().toString(), RolesEnum.ROLE_ADMIN)))
        );
        final var users = List.of(
                new User(usersDto.get(0).id(), usersDto.get(0).username(), usersDto.get(0).password(), Set.of(Roles.builder().role(usersDto.get(0).roles().stream().toList().get(0).role()).build())),
                new User(usersDto.get(1).id(), usersDto.get(1).username(), usersDto.get(1).password(), Set.of(Roles.builder().role(usersDto.get(0).roles().stream().toList().get(0).role()).build())),
                new User(usersDto.get(2).id(), usersDto.get(2).username(), usersDto.get(2).password(), Set.of(Roles.builder().role(usersDto.get(0).roles().stream().toList().get(0).role()).build())
                ));

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