package com.springboot.project.entity.converter;

import com.springboot.project.entity.UserRoleEnumEntity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

@Converter
public class UserRolesConverter implements AttributeConverter<List<UserRoleEnumEntity>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<UserRoleEnumEntity> userRoleEnumEntities) {
        return CollectionUtils.isEmpty(userRoleEnumEntities)
                ? StringUtils.EMPTY
                : StringUtils.join(userRoleEnumEntities, SEPARATOR);
    }

    @Override
    public List<UserRoleEnumEntity> convertToEntityAttribute(String roles) {
        if (StringUtils.isNotEmpty(roles)) {
            String[] userRoles = StringUtils.split(roles, SEPARATOR);
            return Arrays.stream(userRoles)
                    .map(UserRoleEnumEntity::valueOf)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return new ArrayList<>();
    }
}
