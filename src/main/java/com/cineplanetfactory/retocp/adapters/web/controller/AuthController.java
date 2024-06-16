package com.cineplanetfactory.retocp.adapters.web.controller;

import com.cineplanetfactory.retocp.adapters.config.security.JwtTokenUtil;
import com.cineplanetfactory.retocp.adapters.config.security.userpackage.JwtUserDetailsService;
import com.cineplanetfactory.retocp.adapters.config.security.userpackage.UserAuthData;
import com.cineplanetfactory.retocp.adapters.config.security.userpackage.UserDTO;
import com.cineplanetfactory.retocp.adapters.web.exception.UnauthorizedException;
import com.cineplanetfactory.retocp.domain.request.JwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody JwtRequest req) throws Exception {
        authenticate(req.getUsername(), req.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserAuthData user) throws Exception{
        UserDTO dto = userDetailsService.saveUser(user);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserAuthData>> getAll() throws Exception{
        List<UserAuthData> lst = userDetailsService.findAll();
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) throws Exception{
        userDetailsService.deleteUser(id);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> readById(@PathVariable("id") Integer id) throws Exception{
        UserDTO obj = userDetailsService.findUserById(id);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UnauthorizedException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("INVALID_CREDENTIALS", e);
        }
    }
}
