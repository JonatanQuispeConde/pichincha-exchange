package com.pichincha.app.security.service;

import com.pichincha.app.exception.CustomException;
import com.pichincha.app.security.dto.CreateUserDto;
import com.pichincha.app.security.dto.LoginDto;
import com.pichincha.app.security.dto.TokenDto;
import com.pichincha.app.security.model.User;
import com.pichincha.app.security.jwt.JwtProvider;
import com.pichincha.app.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<TokenDto> login(LoginDto dto) {
        return userRepository.findByUsername(dto.getUsername())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> new TokenDto("Bearer",jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "credentiales erroneas")));
    }
    @Override
    public Mono<User> create(CreateUserDto dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(dto.getRole())
                .build();
        Mono<Boolean> userExists = userRepository.findByUsername(user.getUsername()).hasElement();
        return userExists
                .flatMap(exists -> exists ?
                        Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "nombre de usuario ya en uso"))
                        : userRepository.save(user));
    }
}
