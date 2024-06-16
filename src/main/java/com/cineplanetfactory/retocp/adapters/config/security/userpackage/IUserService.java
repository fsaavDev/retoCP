package com.cineplanetfactory.retocp.adapters.config.security.userpackage;

import java.util.List;

public interface IUserService {
    UserDTO findUserById(Integer id);
    UserDTO saveUser(UserAuthData req);
    void deleteUser(Integer id);
    List<UserAuthData> findAll();
}
