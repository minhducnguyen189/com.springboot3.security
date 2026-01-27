package com.springboot.project.entity;

import com.springboot.project.entity.converter.UserRolesConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_user")
public class LoginUserEntity extends BaseEntity {

    @Serial private static final long serialVersionUID = -2545913750827242590L;

    @Column(name = "user_name")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "roles")
    @Convert(converter = UserRolesConverter.class)
    private List<UserRoleEnumEntity> roles;
}
