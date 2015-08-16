package uk.co.landbit.twittertofeed.user.service;

import uk.co.landbit.twittertofeed.controller.SignupForm;
import uk.co.landbit.twittertofeed.user.domain.User;

public interface UserService {

	public User signup(SignupForm user);
			
}
