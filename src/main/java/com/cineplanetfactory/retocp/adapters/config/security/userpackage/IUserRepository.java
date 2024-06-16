package com.cineplanetfactory.retocp.adapters.config.security.userpackage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserAuthData,Integer> {
    Optional<UserAuthData> findOneByUsername(String username);
}
