package tech.buildrun.springsecurity.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.dto.LoginRequest;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public static Long expiresIn = 300L;

    public TokenService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public String generatedTokenJwt(LoginRequest loginRequest) {
        var scopes = this.validLogin(loginRequest).get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(this.validLogin(loginRequest).get().getUserId().toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private Optional<User> validLogin(LoginRequest loginRequest) {
        var user = this.repository.findByUsername(loginRequest.username());
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }
        return user;
    }
}
