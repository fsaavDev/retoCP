package com.cineplanetfactory.retocp.adapters.config.security.userpackage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService, IUserService {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthData user = userRepository.findOneByUsername(username)
                .orElseThrow(()-> {
                    log.warn("Usuario no encontrado con el username: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        List<GrantedAuthority> roles = new ArrayList<>();
        String role = user.getRole();
        roles.add(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }
    @Override
    public UserDTO findUserById(Integer id) {
        UserAuthData entity = userRepository.findById(id).orElse(null);
        if(entity == null){
            return null;
        }
        return new UserDTO(entity.getIdUser(),entity.getUsername(),entity.getRole());
    }

    @Override
    public UserDTO saveUser(UserAuthData req) {
        String encodedPassword = passwordEncoder.encode(req.getPassword());
        req.setPassword(encodedPassword);
        UserAuthData entity = userRepository.save(req);
        return new UserDTO(entity.getIdUser(),entity.getUsername(),entity.getRole());
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserAuthData> findAll() {
        return userRepository.findAll();
    }
}
