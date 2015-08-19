package uk.co.landbit.twittertofeed.integration;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.co.landbit.twittertofeed.integration.endpoint.TweetService;

@Configuration
@ComponentScan
@EnableIntegration
@IntegrationComponentScan
@EnableRetry
@EnableTransactionManagement
public class IntegrationFlowConfiguration {
	
	
	@Autowired
	private TweetService tweetService;

	@Bean
	@Description("Entry to the messaging system through the tweet gateway.")
	public MessageChannel receivedTweetsChannel() {
		return new DirectChannel();
	}

	@Autowired
	public NullChannel nullChannel;
	
	
//	@Bean
//	public IntegrationFlow twitterInboundFlow() {
//		return IntegrationFlows
//				.from(
//					)
//					, 
//					e -> e.id("twitterInboundFlow")
//							.poller(Pollers.fixedDelay(1, TimeUnit.MINUTES)
//							.maxMessagesPerPoll(-1)
//							)
//				)
//				.channel(receivedTweetsChannel())
//				.get();
//	}

	
//	@Bean
//	public IntegrationFlow tweetsInboundFlow() {
//		return IntegrationFlows
//				.from(receivedTweetsChannel())
//				.handle(Message.class,
//						(p, h) -> tweetService.buildRequest(p))
//				.channel(nullChannel)
//				.get();
//	}
	
	
	

	@Bean
	public Advice retryAdvice() {
		final RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();
		advice.setRetryTemplate(retryTemplate());
		return advice;
	}

	@Bean
	public RetryTemplate retryTemplate() {
		final RetryTemplate ret = new RetryTemplate();
		ret.setRetryPolicy(retryPolicy());
		ret.setBackOffPolicy(backOffPolicy());
		ret.setThrowLastExceptionOnExhausted(false);
		return ret;
	}

	@Bean
	public BackOffPolicy backOffPolicy() {
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(30*1000);
		backOffPolicy.setMultiplier(2.5);
		backOffPolicy.setMaxInterval(60*1000);
		return backOffPolicy;
	}
	
	@Bean
	public RetryPolicy retryPolicy() {
		final Map<Class<? extends Throwable>, Boolean> map = new HashMap<Class<? extends Throwable>, Boolean>() {
			{
				// TODO improve list
				//put(RuntimeException.class, true);
				put(Exception.class, true);
			}
			private static final long serialVersionUID = -1L;
		};
		final RetryPolicy ret = new SimpleRetryPolicy(3, map, true);		
		return ret;
	}
}
