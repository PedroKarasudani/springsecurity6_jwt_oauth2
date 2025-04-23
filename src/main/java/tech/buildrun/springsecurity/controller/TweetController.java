package tech.buildrun.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.springsecurity.entities.dto.CreateTweetDto;
import tech.buildrun.springsecurity.service.TweetService;

import java.math.BigInteger;
import java.util.UUID;

@RestController
public class TweetController {

    private final TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto tweetDto, JwtAuthenticationToken token) {
        this.tweetService.createTweet(tweetDto, token.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweetById(@PathVariable("id") Long id, JwtAuthenticationToken token) {
        this.tweetService.deleteTweetById(id, token);
        return ResponseEntity.ok().build();
    }
}
