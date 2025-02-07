package com.spring3.oauth.jwt.repositories;

import com.spring3.oauth.jwt.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRespository extends JpaRepository<UserRole, Long> {

}
