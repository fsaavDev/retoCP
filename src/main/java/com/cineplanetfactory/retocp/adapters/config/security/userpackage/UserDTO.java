package com.cineplanetfactory.retocp.adapters.config.security.userpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Integer idUser;
    private String username;
    private String role;
}
