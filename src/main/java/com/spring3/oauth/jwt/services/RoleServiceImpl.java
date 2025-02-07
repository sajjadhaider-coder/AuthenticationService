package com.spring3.oauth.jwt.services;

import com.spring3.oauth.jwt.models.UserRole;
import com.spring3.oauth.jwt.repositories.RoleRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRespository roleRespository;

    @Override
    public UserRole addRole(UserRole userRole) {
        return roleRespository.save(userRole);
    }

    @Override
    public List<UserRole> getRoles() {
        return roleRespository.findAll();
    }

    @Override
    public Optional<UserRole> getRoleById(Long id) {
        return roleRespository.findById(id);
    }
}
