package tech.buildrun.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.springsecurity.entities.dto.CreateUserDto;
import tech.buildrun.springsecurity.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    public ResponseEntity<Void> newUser (@RequestBody CreateUserDto userDto){
        this.userService.createdUser(userDto);
        return ResponseEntity.ok().build();
    }
}
