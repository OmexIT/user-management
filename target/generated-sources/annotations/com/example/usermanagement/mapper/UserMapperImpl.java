package com.example.usermanagement.mapper;

import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.model.User;
import com.example.usermanagement.security.UserPrincipal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user, Set<String> roles, Set<String> permissions) {
        if ( user == null && roles == null && permissions == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        if ( user != null ) {
            userDto.id( user.getId() );
            userDto.externalId( user.getExternalId() );
            userDto.email( user.getEmail() );
            userDto.displayName( user.getDisplayName() );
            userDto.userType( user.getUserType() );
            userDto.status( user.getStatus() );
        }
        Set<String> set = roles;
        if ( set != null ) {
            userDto.roles( new LinkedHashSet<String>( set ) );
        }
        Set<String> set1 = permissions;
        if ( set1 != null ) {
            userDto.permissions( new LinkedHashSet<String>( set1 ) );
        }

        UserDto userDtoResult = userDto.build();

        ensureNonNullCollections( userDtoResult );

        return userDtoResult;
    }

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.externalId( user.getExternalId() );
        userDto.email( user.getEmail() );
        userDto.displayName( user.getDisplayName() );
        userDto.userType( user.getUserType() );
        userDto.status( user.getStatus() );

        userDto.roles( java.util.Collections.emptySet() );
        userDto.permissions( java.util.Collections.emptySet() );

        UserDto userDtoResult = userDto.build();

        ensureNonNullCollections( userDtoResult );

        return userDtoResult;
    }

    @Override
    public UserDto toDto(UserPrincipal principal) {
        if ( principal == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.externalId( principal.getSubject() );
        userDto.displayName( principal.getEmail() );
        userDto.email( principal.getEmail() );
        Set<String> set = principal.getRoles();
        if ( set != null ) {
            userDto.roles( new LinkedHashSet<String>( set ) );
        }
        Set<String> set1 = principal.getPermissions();
        if ( set1 != null ) {
            userDto.permissions( new LinkedHashSet<String>( set1 ) );
        }

        UserDto userDtoResult = userDto.build();

        ensureNonNullCollections( userDtoResult );

        return userDtoResult;
    }

    @Override
    public List<UserDto> toDTOList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( toDto( user ) );
        }

        return list;
    }
}
