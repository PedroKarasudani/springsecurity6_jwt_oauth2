package tech.buildrun.springsecurity.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.Tweet;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.entities.dto.CreateTweetDto;
import tech.buildrun.springsecurity.entities.dto.FeedItemDto;
import tech.buildrun.springsecurity.repository.TweetRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    private Optional<User> findById(String name) {
        return this.userRepository.findById(UUID.fromString(name));
    }

    private Boolean isAdmin(String name) {
        return this.findById(name).get().getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
    }

    public void createTweet(CreateTweetDto tweetDto, String name) {
        var user = this.findById(name);
        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(tweetDto.content());
        tweetRepository.save(tweet);
    }

    public void deleteTweetById(Long id, JwtAuthenticationToken token) {
        var tweet = this.tweetRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var isAdmin = this.isAdmin(token.getName());
        if(tweet.getUser().getUserId().equals(UUID.fromString(token.getName())) || isAdmin){
            this.tweetRepository.deleteById(id);
        } else {
            throw new ResponseStatusException((HttpStatus.FORBIDDEN));
        }
    }


    public Page<FeedItemDto> pages(int page, int pageSize) {
        return this.tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet -> new FeedItemDto(tweet.getTweetId(), tweet.getContent(), tweet.getUser().getUsername()));
    }
}
