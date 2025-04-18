package tech.buildrun.springsecurity.entities.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
