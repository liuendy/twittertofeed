package uk.co.landbit.twittertofeed.integration.endpoint;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TweetService {

	private final static Logger log = LoggerFactory.getLogger(TweetService.class);

	@Retryable
	@Transactional
	@ServiceActivator
	public String buildRequest(Message<File> msg) {
		log.info("Building request, fileName: {}", msg.getPayload().getName());

		return "";
	}
}
