package tech.buildrun.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.springsecurity.controller.dto.LoginRequest;
import tech.buildrun.springsecurity.controller.dto.LoginResponse;
import tech.buildrun.springsecurity.service.TokenService;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final TokenService tokenService;

    public TokenController(JwtEncoder jwtEncoder, TokenService tokenService) {
        this.jwtEncoder = jwtEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        var jwt = this.tokenService.generatedTokenJwt(loginRequest);
        return ResponseEntity.ok().body(new LoginResponse(jwt, TokenService.expiresIn));
    }
}
