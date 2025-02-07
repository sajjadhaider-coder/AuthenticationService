package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.UserRole;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    UserRole addRole(UserRole userRole);
    List<UserRole> getRoles();

    Optional<UserRole> getRoleById(Long id);
}
