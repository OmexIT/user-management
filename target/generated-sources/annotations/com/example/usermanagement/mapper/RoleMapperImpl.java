package com.example.usermanagement.mapper;

import com.example.usermanagement.dto.RoleDto;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleDto toDTO(RoleRepresentation roleRepresentation) {
        if ( roleRepresentation == null ) {
            return null;
        }

        RoleDto.RoleDtoBuilder roleDto = RoleDto.builder();

        roleDto.name( roleRepresentation.getName() );
        roleDto.description( roleRepresentation.getDescription() );

        roleDto.permissions( extractPermissions(roleRepresentation) );

        return roleDto.build();
    }

    @Override
    public RoleDto toDTO(RoleRepresentation roleRepresentation, Set<String> permissions) {
        if ( roleRepresentation == null && permissions == null ) {
            return null;
        }

        RoleDto.RoleDtoBuilder roleDto = RoleDto.builder();

        if ( roleRepresentation != null ) {
            roleDto.name( roleRepresentation.getName() );
            roleDto.description( roleRepresentation.getDescription() );
        }
        Set<String> set = permissions;
        if ( set != null ) {
            roleDto.permissions( new LinkedHashSet<String>( set ) );
        }

        return roleDto.build();
    }

    @Override
    public List<RoleDto> toDTOList(List<RoleRepresentation> roleRepresentations) {
        if ( roleRepresentations == null ) {
            return null;
        }

        List<RoleDto> list = new ArrayList<RoleDto>( roleRepresentations.size() );
        for ( RoleRepresentation roleRepresentation : roleRepresentations ) {
            list.add( toDTO( roleRepresentation ) );
        }

        return list;
    }

    @Override
    public RoleRepresentation toRepresentation(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        RoleRepresentation roleRepresentation = new RoleRepresentation();

        roleRepresentation.setName( roleDto.name() );
        roleRepresentation.setDescription( roleDto.description() );

        roleRepresentation.setAttributes( createAttributesWithPermissions(roleDto.permissions()) );

        return roleRepresentation;
    }
}
