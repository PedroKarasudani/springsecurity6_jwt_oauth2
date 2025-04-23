package tech.buildrun.springsecurity.service;

import org.springframework.stereotype.Service;
import tech.buildrun.springsecurity.entities.Tweet;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.entities.dto.CreateTweetDto;
import tech.buildrun.springsecurity.repository.TweetRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

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

    public void createTweet(CreateTweetDto tweetDto, String name) {
        var user = this.findById(name);
        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(tweetDto.content());
        tweetRepository.save(tweet);
    }
}
