package com.alura.aluraflixapi.infraestructure.mapper;

import com.alura.aluraflixapi.domain.user.User;
import com.alura.aluraflixapi.domain.user.dto.UserDto;
import com.alura.aluraflixapi.domain.user.roles.Roles;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-11T11:12:17+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User mappToEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.password( password( dto.password() ) );
        user.id( dto.id() );
        user.username( dto.username() );
        List<Roles> list = dto.roles();
        if ( list != null ) {
            user.roles( new ArrayList<Roles>( list ) );
        }

        return user.build();
    }

    @Override
    public UserDto mappToDto(User newUser) {
        if ( newUser == null ) {
            return null;
        }

        String id = null;
        String username = null;
        String password = null;
        List<Roles> roles = null;

        id = newUser.getId();
        username = newUser.getUsername();
        password = newUser.getPassword();
        List<Roles> list = newUser.getRoles();
        if ( list != null ) {
            roles = new ArrayList<Roles>( list );
        }

        UserDto userDto = new UserDto( id, username, password, roles );

        return userDto;
    }

    @Override
    public List<UserDto> mappToUsersDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( mappToDto( user ) );
        }

        return list;
    }
}
