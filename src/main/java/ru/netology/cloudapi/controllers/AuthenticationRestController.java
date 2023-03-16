package ru.netology.cloudapi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudapi.dto.AuthenticationRequestDto;
import ru.netology.cloudapi.model.User;
import ru.netology.cloudapi.security.jwt.JwtTokenProvider;
import ru.netology.cloudapi.services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthenticationRestController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String email = requestDto.getLogin();
            User user = userService.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User with email: " + email + " not found");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), requestDto.getPassword()));

            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("auth-token", token);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String token) {
        var auth = jwtTokenProvider.getAuthentication(token);
        auth.setAuthenticated(false);
        auth.getAuthorities().clear();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginLogout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
