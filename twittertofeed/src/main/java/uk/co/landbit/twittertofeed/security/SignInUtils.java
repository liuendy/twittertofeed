/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.landbit.twittertofeed.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.co.landbit.twittertofeed.user.domain.User;

public class SignInUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SignInUtils.class);

	
	/**
	 * Programmatically signs in the user with the given the user ID.
	 */
	public static void signin(String userId) {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, null));	
	}

	    public static void signin(User user) {
	        LOGGER.info("Logging in user: {}", user);

	       SocialUserDetails userDetails = SocialUserDetails.getBuilder()
	                .firstName(user.getFirstName())
	                .id(user.getId())
	                .lastName(user.getLastName())
	                .password(user.getPassword())
	                .role(user.getRole())
	                .socialSignInProvider(user.getSignInProvider())
	                .username(user.getEmail())
	                .build();
	        LOGGER.debug("Logging in principal: {}", userDetails);

	        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        LOGGER.info("User: {} has been logged in.", userDetails);
	    }
}
