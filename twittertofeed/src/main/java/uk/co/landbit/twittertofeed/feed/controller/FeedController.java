package uk.co.landbit.twittertofeed.feed.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.co.landbit.twittertofeed.user.repository.UserRepository;

// TODO optimization : Stream and Non blocking endpoint
// ideas : expose also an rss endpoint as its costless with Rome library
/**
 * Main controller, API
 * 
 * This generate your Atom Feed 
 *
 */
@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class);
    private static final String UTF8 = ";charset=UTF-8";
    
    private UsersConnectionRepository usersConnectionRepository;
    private UserRepository userRepository;

    @Autowired
    public FeedController(UserRepository userRepository, UsersConnectionRepository usersConnectionRepository) {
	this.userRepository = userRepository;
	this.usersConnectionRepository = usersConnectionRepository;
    }
    
    // TODO better exception handling
    @RequestMapping(value = "/{uuid}/atom",
            method = {RequestMethod.GET},
            produces = {  MediaType.TEXT_XML_VALUE + UTF8 })
    public ResponseEntity<String> writeAtomFeed(@PathVariable String uuid) throws Exception {
	
	//
	
	// TODO replace ""
	return new ResponseEntity<>("", HttpStatus.OK);
    }
    
}
